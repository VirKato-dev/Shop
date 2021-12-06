package my.example.shop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/***
 * Экран покупателя.
 * Возможности:
 * выбирать "товарные карточки" с указанием количества наименований каждого товара.
 * Просмотр своей товарной корзины.
 */
public class BuyerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer);

        setTitle("Все товары");
    }
}