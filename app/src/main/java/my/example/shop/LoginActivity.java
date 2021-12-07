package my.example.shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/***
 * Экран авторизации пользователя и админа.
 * Возможна самостоятельная регистрация.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText e_login;
    private EditText e_password;
    private Button b_login;
    private Button b_register;

    // логини и пароль админа хранится только в приложении
    String admin_login = "admin";
    String admin_password = "admin";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        e_login = findViewById(R.id.e_login);
        e_password = findViewById(R.id.e_password);
        b_login = findViewById(R.id.b_login);
        b_register = findViewById(R.id.b_register);

        b_login.setOnClickListener(v -> {
            String login = e_login.getText().toString().trim();
            String password = e_password.getText().toString().trim();
            goIfCan(login, password);
        });

        // интерфейс с единственным методом можно заменить на лямбда-функцию
        // компилятор сам разберётся, что к чему
        b_register.setOnClickListener(v -> {
            // v - нажатая кнопка
            String login = e_login.getText().toString().trim();
            String password = e_password.getText().toString().trim();
            register(login, password);
        });
    }

    /***
     * Регистрация нового пользователя.
     * @param login логин (уникальный идентификатор).
     * @param password пароль.
     */
    private void register(String login, String password) {
        if (!login.equals("") && !password.equals("")) {
            User user = new User(DB.USERS);
            user.find(login);
            if (user.id.equals("")) {
                // если пользователь "пустой", значит можно использовать этот логин для регистрации
                user.name = "Укажите своё имя";
                user.id = login;
                user.password = password;
                user.save();

                // сразу после регистрации пользователь считается авторизованным
                goIfCan(login, password);
            } else {
                Toast.makeText(this, "Логин уже занят", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Заполните все поля!", Toast.LENGTH_LONG).show();
        }
    }

    /***
     * Произвести переход на нужный экран, если пользователь с указанными логином и паролем найден.
     * @param login логин.
     * @param password пароль.
     */
    private void goIfCan(String login, String password) {
        if (!login.equals("") && !password.equals("")) {
            // проверить логин и пароль
            if (login.equals(admin_login) && password.equals(admin_password)) {
                // запускаем Activity админа
                startActivity(new Intent(this, EditorActivity.class));
            } else {
                User user = new User(DB.USERS);
                user.find(login);
                if (user.id.equals(login) && user.password.equals(password)) {
                    // запускаем Activity покупателя
                    startActivity(new Intent(this, BuyerActivity.class));
                } else {
                    Toast.makeText(this, "Такой пользователь не найден", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            Toast.makeText(this, "Заполните все поля!", Toast.LENGTH_LONG).show();
        }

    }
}