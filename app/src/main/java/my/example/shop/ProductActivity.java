package my.example.shop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

import my.example.shop.data.DB;
import my.example.shop.data.Product;

/***
 * Экран (для покупателя) просмотра товара и выбора количества преобретаемого товара,
 * либо (для админа) просмотра и редактирования товара.
 */
public class ProductActivity extends AppCompatActivity {

    private EditText e_info_prod_name;
    private EditText e_info_prod_weight;
    private EditText e_info_prod_price;
    private EditText e_info_prod_description;
    private Button b_info_prod_save;

    private String prod;
    private Product product = new Product(DB.PRODUCTS);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        e_info_prod_name = findViewById(R.id.e_info_prod_name);
        e_info_prod_weight = findViewById(R.id.e_info_prod_weight);
        e_info_prod_price = findViewById(R.id.e_info_prod_price);
        e_info_prod_description = findViewById(R.id.e_info_prod_description);
        b_info_prod_save = findViewById(R.id.b_info_prod_save);
        b_info_prod_save.setOnClickListener(v -> {
            saveProduct();
        });

        setTitle("Подробно о товаре");

        prod = getIntent().getStringExtra("product");
        if (prod == null) {
            finish();
        } else {
            product.fromString(prod);
            showDetail();
        }
    }


    /***
     * Показать детали товара
     */
    private void showDetail() {
        e_info_prod_name.setText(product.name);
        e_info_prod_weight.setText(String.format(Locale.ENGLISH, "%d", product.weight));
        e_info_prod_price.setText(String.format(Locale.ENGLISH, "%.2f", product.price));
        e_info_prod_description.setText(product.description);
    }


    /***
     * Сохранить изменения данных о товаре.
     */
    private void saveProduct() {
        if (product.id.equals("")) {
            // для нового товара задать ID
            product.setId();
        }
        product.weight = Long.parseLong(e_info_prod_weight.getText().toString().trim());
        product.name = e_info_prod_name.getText().toString().trim();
        product.price = Double.parseDouble(e_info_prod_price.getText().toString().trim().replaceAll(",", "."));
        product.description = e_info_prod_description.getText().toString().trim();
        product.save();
        Toast.makeText(this, "Товар сохранён", Toast.LENGTH_LONG).show();
    }


}