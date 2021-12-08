package my.example.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import my.example.shop.adapter.ProductsAdapter;
import my.example.shop.data.DB;
import my.example.shop.data.Product;
import my.example.shop.data.User;

/***
 * Экран покупателя.
 * Возможности:
 * выбирать "товарные карточки" с указанием количества наименований каждого товара.
 * Просмотр своей товарной корзины.
 */
public class BuyerActivity extends AppCompatActivity {

    private Button b_profile;
    private ListView lv_products;

    private ProductsAdapter productsAdapter;
    private Product product = new Product(DB.PRODUCTS);

    private String profile;
    private User user = new User(DB.USERS);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer);

        b_profile = findViewById(R.id.b_profile);
        b_profile.setOnClickListener(v -> {
            showProfile();
        });

        productsAdapter = new ProductsAdapter(product.getAll());
        lv_products = findViewById(R.id.lv_products);
        lv_products.setAdapter(productsAdapter);
        lv_products.setOnItemClickListener((parent, view, position, id) -> {
            showCart(productsAdapter.getItem(position));
        });

        setTitle("Все товары");

        profile = getIntent().getStringExtra("profile");
        if (profile == null) {
            finish();
        } else {
            user.fromString(profile);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        productsAdapter.setList(product.getAll());
    }


    /***
     * Показать данные профиля покупателя.
     */
    private void showProfile() {
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("profile", user.toString());
        startActivity(i);
    }


    private void showCart(Product product) {
        Intent i = new Intent(this, CartActivity.class);
        // передать товар в корзину для указания количества покупаемого товара
        i.putExtra("product", product.toString());
        i.putExtra("user", user.toString());
        startActivity(i);
    }
}