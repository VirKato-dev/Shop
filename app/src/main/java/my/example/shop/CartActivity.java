package my.example.shop;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import my.example.shop.adapter.CartsAdapter;
import my.example.shop.data.Cart;
import my.example.shop.data.DB;
import my.example.shop.data.Product;
import my.example.shop.data.User;

/***
 * Экран просмотра товаров в корзине.
 * Возможно изменение количества преобретаемых товаров.
 */
public class CartActivity extends AppCompatActivity {

    private LinearLayout l_edit_cart;
    private TextView t_cart_prod_name;
    private EditText e_cart_total;
    private TextView t_cart_measure;
    private TextView t_total_amount;
    private Button b_cart_confirm;
    private TextView t_total_product_count;
    private TextView t_total_product_amount;
    private Button b_total_pay;


    private Cart cart = new Cart(DB.CARTS);
    private CartsAdapter cartsAdapter;
    private ListView lv_carts;

    private String prod;
    private Product product = new Product(DB.PRODUCTS);

    private String usr;
    private User user = new User(DB.USERS);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        usr = getIntent().getStringExtra("user");
        if (usr == null) {
            finish();
        } else {
            user.fromString(usr);

            t_total_product_count = findViewById(R.id.t_total_product_count);
            t_total_product_amount = findViewById(R.id.t_total_product_amount);
            b_total_pay = findViewById(R.id.b_total_pay);
            b_total_pay.setOnClickListener(v -> {
                applyBuy();
            });

            l_edit_cart = findViewById(R.id.l_edit_cart);
            l_edit_cart.setVisibility(View.GONE);

            t_cart_prod_name = findViewById(R.id.t_cart_prod_name);
            e_cart_total = findViewById(R.id.e_cart_total);
            e_cart_total.addTextChangedListener(new TextWatcher() {
                // считаем общую сумму для этого товара сразу при вводе количества товара
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (l_edit_cart.getVisibility() == View.VISIBLE) {
                        try {
                            cart.total = Long.parseLong(e_cart_total.getText().toString());
                        } catch (Exception ignore) {
                            cart.total = 0L;
                        }
                        t_total_amount.setText(String.format(Locale.ENGLISH, "%.2f", calc(cart)));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            t_cart_measure = findViewById(R.id.t_cart_measure);
            t_total_amount = findViewById(R.id.t_total_amount);
            b_cart_confirm = findViewById(R.id.b_cart_confirm);
            b_cart_confirm.setOnClickListener(v -> {
                saveCart();
            });

            lv_carts = findViewById(R.id.lv_carts);
            cartsAdapter = new CartsAdapter(cart.getAllFor(user.id));
            lv_carts.setAdapter(cartsAdapter);
            lv_carts.setOnItemClickListener((parent, view, position, id) -> {
                cart = cartsAdapter.getItem(position);
                product.find(cart.product_id);
                showDetail();
            });

            prod = getIntent().getStringExtra("product");
            if (prod != null) {
                // если совершаем новую покупку
                product.fromString(prod);
                cart.product_id = product.id;
                showDetail();
            }

            showList();
        }

        setTitle("Корзина");
    }


    /***
     * Сохранить покупку в базе.
     */
    private void saveCart() {
        l_edit_cart.setVisibility(View.GONE);
        if (cart.id.equals("")) {
            cart.setId();
        }
        cart.user_id = user.id;
        cart.product_id = product.id;
        cart.total = Long.parseLong(e_cart_total.getText().toString());
        cart.date_add = new Date().getTime();

        cart.save();
        showList();
    }

    /***
     * Показать детали покупки для редактирования.
     */
    private void showDetail() {
        b_cart_confirm.setText("Сохранить");
        if (cart.id.equals("")) {
            b_cart_confirm.setText("Добавить");
        }
        l_edit_cart.setVisibility(View.VISIBLE);
        t_cart_prod_name.setText(product.name);
        e_cart_total.setText(String.format(Locale.ENGLISH, "%d", cart.total));
        double amount = calc(cart);
        t_total_amount.setText(String.format(Locale.ENGLISH, "%.2f", amount));
    }


    /***
     * Посчитать общую стоимость для одного товара.
     * @return общая стоимость этого товара.
     */
    private double calc(Cart cart) {
        Product prod = new Product(DB.PRODUCTS);
        prod.find(cart.product_id);

        double amount = 0d;
        if (prod.weight < 0) {
            // считать как за штуку
            amount = cart.total * prod.price;
            t_cart_measure.setText("шт.");
        } else {
            // считать как весовое
            amount = cart.total * (prod.price / prod.weight);
            t_cart_measure.setText("мл (гр)");
        }
        return amount;
    }


    /***
     * Оплатить товары в корзине.
     */
    private void applyBuy() {
        ArrayList<Cart> list = cart.getAllFor(user.id);
        for (Cart cart : list) {
            cart.date_buy = new Date().getTime();
            cart.save();
        }
        showList();
    }


    /***
     * Показать обновлённый список покупок без покупок с нулевым количеством.
     */
    private void showList() {
        double total_amount = 0d;
        int total_count = 0;
        ArrayList<Cart> list = cart.getAllFor(user.id);
        Iterator<Cart> it = list.iterator();
        while (it.hasNext()) {
            Cart cart = it.next();
            if (cart.total == 0) {
                // удалить, потому что количество купленного товара = 0
                cart.remove();
                it.remove();
            } else {
                if (cart.date_buy == 0) {
                    // не считать, если товар уже куплен
                    total_count++;
                    total_amount += calc(cart);
                }
            }
        }
        cartsAdapter.setList(list);

        t_total_product_count.setText(String.format(Locale.ENGLISH, "%d", total_count));
        t_total_product_amount.setText(String.format(Locale.ENGLISH, "%.2f", total_amount));
    }

}