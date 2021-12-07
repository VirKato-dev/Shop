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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
            saveBitmapToFile(user.id, imageBitmap);
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
            setImageFromFile(i_avatar, user.avatar);
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
     * Сохранить картинку в файл для повторного использования.
     * @param name id пользователя будет именем файла.
     * @param bm картинка.
     */
    private void saveBitmapToFile(String name, Bitmap bm) {
        File f = new File(getExternalCacheDir(), name + ".png");
        try {
            f.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
            // при удачном сохранении и закрытии потока
            // запишем полный путь к файлу сразу в данные профиля
            user.avatar = f.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /***
     * Установить картинку из файла по указанному пути
     * @param path путь к файлу
     */
    private void setImageFromFile(ImageView iv, String path) {
        File imgFile = new File(path);
        if (imgFile.exists()) {
//            Log.e("showImage", path + " exists");
            Bitmap bm = BitmapFactory.decodeFile(path);
            iv.setImageBitmap(bm);
        }
    }
}