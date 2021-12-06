package my.example.shop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/***
 * Экран просмотра товаров в корзине.
 * Возможно изменение количества преобретаемых товаров.
 */
public class CartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
    }
}