package my.example.shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import my.example.shop.data.FileDB;

/***
 * Пустой экран (заставка)
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // необходимо для подоготовки к работе с файлами
        FileDB.getInstance(this);

        // перейдём к авторизации
        startActivity(new Intent(this, LoginActivity.class));
        // эта Activity больше не нужна
        finish();
    }
}