package my.example.shop;

import androidx.annotation.NonNull;

import java.util.Locale;

/***
 * Описание характеристик товара.
 */
public class Product {

    /***
     * Идентификатор товара. Уникальный.
     */
    public String id = "";

    /***
     * Название товара.
     */
    public String name = "";

    /***
     * Вес в гр. (для весового товара).
     * -1 - товар штучный.
     */
    public long weight = -1;

    /***
     * Цена за единицу товара.
     * Если товар весовой, то итоговая цена расчитывается по итоговому весу.
     * Инача цена считается за штуку.
     */
    public double price = -1d;

    /***
     * Краткое описание товара.
     */
    public String description = "";


    /***
     * Создать товар со стандартным набором параметров.
     * Пустой.
     */
    public Product() {}


    /***
     * Сохранить данные товара в базу.
     */
    public void save() {
        FileDB.addProduct(this);
    }


    /***
     * Удалить данные товара из базы.
     */
    public void remove() {
        FileDB.removeProduct(this);
    }


    /***
     * Установить параметры текущего товара в соответствии с данными в строке.
     * @param str строка параметров разделённых "|".
     */
    public void fromString(String str) {
        String[] val = str.split("\\|");
        id = val[0];
        name = val[1];
        weight = Integer.parseInt(val[2]);
        price = Double.parseDouble(val[3]);
        if (val.length > 4) description = val[4];
    }


    /***
     * Получить строку параметров товара разделённых "|".
     * @return строка удобная для хранения в файле.
     */
    @NonNull
    @Override
    public String toString() {
        // идентификатор пишем первым (требуется для поиска по базе)
        return id + "|" + name + "|" +
                String.format(Locale.ENGLISH,"%d", weight) + "|" +
                String.format(Locale.ENGLISH,"%.2f", price) + "|" +
                description;
    }
}
