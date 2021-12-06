package my.example.shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

/***
 * Экран авторизации пользователя и админа.
 * Возможна самостоятельная регистрация.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText e_login;
    private EditText e_password;
    private Button b_login;
    private Button b_buyer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        e_login = findViewById(R.id.e_login);
        e_password = findViewById(R.id.e_password);
        b_login = findViewById(R.id.b_login);
        b_buyer = findViewById(R.id.b_register);

        b_login.setOnClickListener(v -> {
            // проверить логин и пароль
            String admin_login = "admin";
            String admin_password = "admin";
            String login = e_login.getText().toString().trim();
            String password = e_password.getText().toString().trim();

            if (login.equals(admin_login) && password.equals(admin_password)) {
                // когда логин и пароль подошли
                // запускаем Activity админа
                startActivity(new Intent(v.getContext(), EditorActivity.class));
            }
        });

        // интерфейс с единственным методом можно заменить на лямбда-функцию
        // компилятор сам разберётся, что к чему
        b_buyer.setOnClickListener(v -> {
            // v - нажатая кнопка
            // v.getContext - необходим для получения контекста вызывающей Activity
            // если вместо этого использовать просто this, то возникнет ошибка
            // так как в данной ситуации this будет относиться к контексту интерфейса OnClickListener

            // запускаем Activity покупателя
            startActivity(new Intent(v.getContext(), BuyerActivity.class));
        });
    }
}