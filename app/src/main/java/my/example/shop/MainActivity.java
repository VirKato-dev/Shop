package my.example.shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.Date;

import my.example.shop.data.Cart;
import my.example.shop.data.DB;
import my.example.shop.data.FileDB;
import my.example.shop.data.Product;
import my.example.shop.data.User;

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

        initDemoData();

        // перейдём к авторизации
        startActivity(new Intent(this, LoginActivity.class));
        // эта Activity больше не нужна
        finish();
    }


    /***
     * Подготовить некоторые данные для демонстрации работоспособности приложения.
     */
    private void initDemoData() {
        // добавить пользователя
        User user = new User(DB.USERS);
        user.id = "login";
        user.password = "password";
        user.name = "DEMO";
        user.save();

        // добавить товар
        Product product = new Product(DB.PRODUCTS);
        product.id = "test_1";
        product.name = "Молоко 1л";
        product.weight = -1L; // -1 - считать как 1 шт.
        product.price = 100d; // цена (за упаковку)
        product.save();

        product.id = "test_2";
        product.name = "Печенье овсянное";
        product.weight = 1000; // >0 - весовой товар
        product.price = 145d; // цена (за килограмм)
        product.save();

        // товар в корзине
        Cart cart = new Cart(DB.CARTS);
        cart.id = "test_1";
        cart.total = 1500; // 1.5 кг
        cart.product_id = "test_2"; // печенье
        cart.user_id = "login"; // корзина этого пользователя
        cart.date_add = new Date().getTime();
        cart.save();
    }
}