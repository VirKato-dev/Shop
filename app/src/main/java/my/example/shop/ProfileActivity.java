package my.example.shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import my.example.shop.data.DB;
import my.example.shop.data.User;

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

    private String profile;
    private User user = new User(DB.USERS);

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        i_avatar = findViewById(R.id.i_avatar);
        i_avatar.setOnClickListener(v -> {
            dispatchTakePictureIntent();
        });

        e_my_login = findViewById(R.id.e_my_login);
        e_my_password = findViewById(R.id.e_my_password);
        e_my_name = findViewById(R.id.e_my_name);
        b_cart = findViewById(R.id.b_cart);
        b_cart.setOnClickListener(v -> {
            //TODO показать список покупок
            Toast.makeText(v.getContext(), "Список покупок", Toast.LENGTH_LONG).show();
        });

        b_my_save = findViewById(R.id.b_my_save);
        b_my_save.setOnClickListener(v -> {
            saveProfile();
        });

        // получить строчку с параметрами пользователя из вызвавшей Activity
        profile = getIntent().getStringExtra("profile");
        setTitle("Профиль");

        if (profile == null) finish();
        else {
            // если строчка не пустая, значит есть что показать на экране
            user.fromString(profile);
            showDetail();

        }
    }


    /***
     * Для получения картинки с камеры
     * @param requestCode номер запроса
     * @param resultCode состояние ответа
     * @param data тело ответа на запрос
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            user.avatar = bitmapToText(imageBitmap);
            i_avatar.setImageBitmap(imageBitmap);
        }
    }

    /***
     * Показать детали профиля покупателя
     */
    private void showDetail() {
        e_my_login.setText(user.id);
        e_my_password.setText(user.password);
        e_my_name.setText(user.name);
        if (!user.avatar.equals("")) {
            i_avatar.setImageBitmap(textToBitmap(user.avatar));
        }
    }


    private void saveProfile() {
        user.name = e_my_name.getText().toString().trim();
        user.id = e_my_login.getText().toString().trim();
        user.password = e_my_password.getText().toString().trim();
        // аватарка добавляется в данные о пользователе в момент выбора картинки
        user.save();
        Toast.makeText(this, "Профиль сохранён", Toast.LENGTH_LONG).show();
    }


    /***
     * Получить картинку с камеры
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }


    /***
     * Закодировать картинку в текст
     * @param bm картинка
     * @return текст
     */
    private String bitmapToText(Bitmap bm) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
    }


    /***
     * Раскодировать текст как картинку
     * @param str кодированная картинка
     * @return картинка
     */
    private Bitmap textToBitmap(String str) {
        byte[] decodedString = Base64.decode(str, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }


}