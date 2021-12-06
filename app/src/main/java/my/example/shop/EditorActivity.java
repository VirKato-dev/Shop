package my.example.shop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/***
 * Экран администратора.
 * Возможности:
 * создавать, редактировать, удалять "товарные карточки"
 */
public class EditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        setTitle("Администратор");

    }
}