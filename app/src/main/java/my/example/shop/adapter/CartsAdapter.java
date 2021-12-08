package my.example.shop.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import my.example.shop.R;
import my.example.shop.data.Cart;
import my.example.shop.data.DB;
import my.example.shop.data.Product;

/***
 * Адаптер для заполнения ListView покупками
 */
public class CartsAdapter extends BaseAdapter {

    private ArrayList<Cart> data;

    /***
     * Первоначальная привязка списка к виджету списка
     * @param data список покупок
     */
    public CartsAdapter(ArrayList<Cart> data) {
        setList(data);
    }


    /***
     * Для обновления списка покупок в виджете (на экране)
     * @param data новый список покупок
     */
    public void setList(ArrayList<Cart> data) {
        this.data = data;
        notifyDataSetChanged();
    }


    /***
     * Размер списка
     * @return размер
     */
    @Override
    public int getCount() {
        return data.size();
    }


    /***
     * Получить покупку по номеру позиции в списке.
     * @param position позиция в списке.
     * @return покупка.
     */
    @Override
    public Cart getItem(int position) {
        return data.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    /***
     * Подготовить изображение элемента списка к выводу на экран.
     * @param position позиция элемента в списке данных.
     * @param convertView виджет элемента списка (переиспользуемый).
     * @param parent контейнер (ListView).
     * @return подготовленный виджет с данными.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            // если виджет небыл ранее создан (иначе используем ранее созданный виджет)
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.a_cart, null);
        }

        TextView t_cart_product_name = convertView.findViewById(R.id.t_cart_product_name);
        TextView t_cart_product_lot = convertView.findViewById(R.id.t_cart_product_lot);
        TextView t_cart_product_measure = convertView.findViewById(R.id.t_cart_product_measure);
        TextView t_cart_product_amount = convertView.findViewById(R.id.t_cart_product_amount);

        Product product = new Product(DB.PRODUCTS);
        product.find(getItem(position).product_id);
        t_cart_product_name.setText(product.name);

        long lot = getItem(position).total;
        t_cart_product_lot.setText(String.format(Locale.ENGLISH, "%d", lot));

        double amount = 0d;
        if (product.weight < 0) {
            // считать как за штуку
            amount = lot * product.price;
            t_cart_product_measure.setText("шт.");
        } else {
            // считать как весовое
            amount = lot *  (product.price / product.weight);
            t_cart_product_measure.setText("мл (гр)");
        }
        t_cart_product_amount.setText(String.format(Locale.ENGLISH, "%.2f", amount));

        return convertView;
    }
}
