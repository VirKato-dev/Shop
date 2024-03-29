package my.example.shop.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import my.example.shop.R;
import my.example.shop.data.Product;

/***
 * Адаптер для заполнения ListView товаров
 */
public class ProductsAdapter extends BaseAdapter {

    private ArrayList<Product> data;


    /***
     * Первоначальная привязка списка к виджету списка
     * @param data список товаров
     */
    public ProductsAdapter(ArrayList<Product> data) {
        setList(data);
    }


    /***
     * Для обновления списка товаров в виджете (на экране)
     * @param data новый список товаров
     */
    public void setList(ArrayList<Product> data) {
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
     * Получить товар по номеру позиции в списке.
     * @param position позиция в списке.
     * @return товар.
     */
    @Override
    public Product getItem(int position) {
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.a_product, null);
        }

        TextView t_product_name = convertView.findViewById(R.id.t_product_name);
        TextView t_product_price = convertView.findViewById(R.id.t_product_price);
        TextView t_product_description = convertView.findViewById(R.id.t_product_description);
        ImageView i_product_picture = convertView.findViewById(R.id.i_product_picture);

        t_product_name.setText(getItem(position).name);
        t_product_price.setText(String.format(Locale.ENGLISH, "%.2f", getItem(position).price));
        t_product_description.setText(getItem(position).description);
        setImageFromFile(i_product_picture, getItem(position).picture);

        return convertView;
    }


    /***
     * Установить картинку из файла по указанному пути
     * @param path путь к файлу
     */
    private void setImageFromFile(ImageView iv, String path) {
        File imgFile = new File(path);
        if (imgFile.exists()) {
            Bitmap bm = BitmapFactory.decodeFile(path);
            iv.setImageBitmap(bm);
        } else {
            iv.setImageResource(R.drawable.ic_image);
        }
    }

}
