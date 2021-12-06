package my.example.shop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/***
 * Экран (для покупателя) просмотра товара и выбора количества преобретаемого товара,
 * либо (для админа) просмотра и редактирования товара.
 */
public class ProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        setTitle("Подробно о товаре");
    }
}