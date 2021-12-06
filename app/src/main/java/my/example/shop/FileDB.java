package my.example.shop;

import android.content.Context;

import java.io.File;

/***
 * Класс ответственный за получение и сохранение информации.
 */
public class FileDB {

    /***
     * имя базы данных о пользователях
     */
    private final static String db_users = "users.db";

    /***
     * имя базы данных о товарах
     */
    private final static String db_prods = "products.db";

    /***
     * имя базы данных о содержимом корзин пользователей
     */
    private final static String db_carts = "carts.db";

    /***
     * Папка для хранения файлов базы данных
     */
    private static File folder;

    /***
     * Требуется единственный экземпляр класса
     */
    private static FileDB instance = null;


    /***
     * Запретим ручное создание множества экзепляров класса вручную.
     * Для корректной работы достаточно одного экземпляра.
     */
    private FileDB() {}


    /***
     * Инициализация папки для хранения файлов базы данных
     * @param context контекст приложения или активности
     */
    public static void getInstance(Context context) {
        if (instance == null) {
            folder = context.getExternalCacheDir();
            instance = new FileDB();
        }
    }

}
