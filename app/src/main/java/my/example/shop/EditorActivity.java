package my.example.shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import my.example.shop.adapter.ProductsAdapter;
import my.example.shop.data.DB;
import my.example.shop.data.Product;

/***
 * Экран администратора.
 * Возможности:
 * создавать, редактировать, удалять "товарные карточки"
 */
public class EditorActivity extends AppCompatActivity {

    private FloatingActionButton fab_add_product;
    private ListView lv_products;

    private ProductsAdapter productsAdapter;

    private Product product = new Product(DB.PRODUCTS);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        lv_products = findViewById(R.id.lv_products_all);
        fab_add_product = findViewById(R.id.fab_add_product);
        fab_add_product.setOnClickListener(v -> {
            showDetail(new Product(DB.PRODUCTS));
        });

        productsAdapter = new ProductsAdapter(product.getAll());
        lv_products.setAdapter(productsAdapter);
        lv_products.setOnItemClickListener((parent, view, position, id) -> {
            showDetail(productsAdapter.getItem(position));
        });

        setTitle("Администратор");
    }


    @Override
    protected void onResume() {
        super.onResume();
        productsAdapter.setList(product.getAll());
    }


    /***
     * Показать подробную информацию о товаре с возможностью редактирования.
     * @param product товар.
     */
    private void showDetail(Product product) {
        if (!product.id.equals("")) {
            Toast.makeText(this, "Товар: " + product.name, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Создать новый товар", Toast.LENGTH_LONG).show();
        }

        Intent i = new Intent(this, ProductActivity.class);
        // передать параметры товара в Activity для просмотр
        i.putExtra("product", product.toString());
        // админ млжет редактировать данные о товаре
        i.putExtra("edit", "true");
        startActivity(i);
    }
}