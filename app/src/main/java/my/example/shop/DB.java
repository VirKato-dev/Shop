package my.example.shop;

import androidx.annotation.NonNull;

/***
 * Перечисление имён файлов баз данных.
 * Понадобилось для декомпозиции FileDB.
 */
public enum DB {
    /***
     * Пользователи
     */
    USERS("users.db"),
    /***
     * Товары
     */
    PRODUCTS("products.db"),
    /***
     * Покупки
     */
    CARTS("carts.db");

    /***
     * Имя файла базы данных
     */
    String filename;

    /***
     * Конструктор позволяющий задать константе дополнительные значения
     * @param filename имя файла
     */
    DB(String filename) {
        this.filename = filename;
    }

    /***
     * Получить имя файла базы данных
     * @return имя файла
     */
    @NonNull
    @Override
    public String toString() {
        return filename;
    }
}
