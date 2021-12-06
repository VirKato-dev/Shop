package my.example.shop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/***
 * Экран пользовательских данных.
 * Переход в корзину по кнопке.
 * Можно изменить аватарку кликнув по ней.
 */
public class ProfileActivity extends AppCompatActivity {

    private ImageView i_avatar;
    private EditText e_my_login;
    private EditText e_my_password;
    private EditText e_my_name;
    private Button b_cart;
    private Button b_my_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setTitle("Профиль");
    }
}