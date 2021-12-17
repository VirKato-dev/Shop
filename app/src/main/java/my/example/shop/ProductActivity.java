package my.example.shop;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import my.example.shop.data.DB;
import my.example.shop.data.Product;

/***
 * Экран (для покупателя) просмотра товара и выбора количества преобретаемого товара,
 * либо (для админа) просмотра и редактирования товара.
 */
public class ProductActivity extends AppCompatActivity {

    private EditText e_info_prod_name;
    private EditText e_info_prod_weight;
    private EditText e_info_prod_price;
    private EditText e_info_prod_description;
    private ImageView i_info_prod_image;
    private Button b_info_prod_save;

    private String prod;
    private Product product = new Product(DB.PRODUCTS);

    static final int REQUEST_IMAGE_CAPTURE = 1;


    /***
     * Подготовка экрана
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        e_info_prod_name = findViewById(R.id.e_info_prod_name);
        e_info_prod_weight = findViewById(R.id.e_info_prod_weight);
        e_info_prod_price = findViewById(R.id.e_info_prod_price);
        e_info_prod_description = findViewById(R.id.e_info_prod_description);
        i_info_prod_image = findViewById(R.id.i_info_prod_image);
        i_info_prod_image.setOnClickListener(v -> {
            dispatchTakePictureIntent();
        });
        b_info_prod_save = findViewById(R.id.b_info_prod_save);
        b_info_prod_save.setOnClickListener(v -> {
            saveProduct();
        });

        setTitle("Подробно о товаре");

        prod = getIntent().getStringExtra("product");
        if (prod == null) {
            finish();
        } else {
            product.fromString(prod);
            showDetail();
        }

        if (getIntent().getStringExtra("edit").equals("")) {
            // когда не режим режим редактирования
            b_info_prod_save.setVisibility(View.GONE);
        }

    }


    /***
     * Для получения картинки с камеры.
     * @param requestCode номер запроса.
     * @param resultCode состояние ответа.
     * @param data тело ответа на запрос.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            saveBitmapToFile(product.id, imageBitmap);
            i_info_prod_image.setImageBitmap(imageBitmap);
        }
    }


    /***
     * Показать детали товара
     */
    private void showDetail() {
        e_info_prod_name.setText(product.name);
        e_info_prod_weight.setText(String.format(Locale.ENGLISH, "%d", product.weight));
        e_info_prod_price.setText(String.format(Locale.ENGLISH, "%.2f", product.price));
        e_info_prod_description.setText(product.description);
        setImageFromFile(i_info_prod_image, product.picture);
    }


    /***
     * Сохранить изменения данных о товаре.
     */
    private void saveProduct() {
        if (e_info_prod_weight.getText().toString().trim().equals("") ||
                e_info_prod_name.getText().toString().trim().equals("") ||
                e_info_prod_price.getText().toString().trim().equals("") ||
                e_info_prod_description.getText().toString().trim().equals("")
        ) {
            Toast.makeText(this, "Заполните все поля!", Toast.LENGTH_LONG).show();
        } else {
            if (product.id.equals("")) {
                // для нового товара задать ID
                product.setId();
            }
            product.weight = Long.parseLong(e_info_prod_weight.getText().toString().trim());
            product.name = e_info_prod_name.getText().toString().trim();
            product.price = Double.parseDouble(e_info_prod_price.getText().toString().trim().replaceAll(",", "."));
            product.description = e_info_prod_description.getText().toString().trim();
            product.save();
            Toast.makeText(this, "Товар сохранён", Toast.LENGTH_LONG).show();
        }
    }


    /***
     * Получить картинку с камеры.
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
        File f = new File(getExternalCacheDir(), "prod_" + name + ".png");
        if (f.exists()) f.delete();
        try {
            f.createNewFile();
        } catch (IOException ignore) {
        }
        try (FileOutputStream fOut = new FileOutputStream(f)) {
            bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            // запишем полный путь к файлу сразу в данные профиля
            product.picture = f.getAbsolutePath();
        } catch (IOException ignore) {
        }
    }


    /***
     * Установить картинку из файла по указанному пути
     * @param path путь к файлу
     */
    private void setImageFromFile(ImageView iv, String path) {
        File imgFile = new File(path);
        if (imgFile.exists()) {
            Bitmap bm = BitmapFactory.decodeFile(path);
            iv.setImageBitmap(bm);
        } else {
            iv.setImageResource(R.drawable.ic_image);
        }
    }

}