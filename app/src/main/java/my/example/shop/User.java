package my.example.shop;

import androidx.annotation.NonNull;

/***
 * Описание характеристик пользователя.
 */
public class User {

    /***
     * Логин пользователя. Уникальный.
     * Используется для идентификации пользователя в базе данных.
     */
    public String id = "";

    /***
     * Пароль пользователя. Не уникальный.
     */
    public String password = "";

    /***
     * Имя пользователя
     */
    public String name = "";

    /***
     * Статус пользователя, как админа.
     */
    public boolean isAdmin = false;

    /***
     * Путь к файлу аватарки пользователя.
     */
    public String avatar = "";


    /***
     * Создать пользователя со стандартным набором параметров.
     * Пустой.
     */
    public User() {}


    /***
     * Сохранить данные пользователя в базу
     */
    public void save() {
        FileDB.addUser(this);
    }


    /***
     * Установить параметры текущего пользователя в соответствии с данными в строке.
     * @param str строка параметров разделённых "|".
     */
    public void fromString(String str) {
        String[] val = str.split("\\|");
        id = val[0];
        password = val[1];
        name = val[2];
        isAdmin = Boolean.parseBoolean(val[3]);
        if (val.length > 4) avatar = val[4];
    }


    /***
     * Получить строку параметров пользователя разделённых "|".
     * @return строка удобная для хранения в файле.
     */
    @NonNull
    @Override
    public String toString() {
        // идентификатор пишем первым (требуется для поиска по базе)
        return id + "|" + password + "|" + name + "|" + isAdmin + "|" + avatar;
    }
}
