package my.example.shop.data;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Locale;

/***
 * Описание характеристик товара.
 */
public class Product extends DataEntity {

    /***
     * Название товара.
     * Следует дописывать объём/массу товара здесь.
     */
    public String name = "";

    /***
     * Вес в гр. (для весового товара).
     * Используется только для расчётов. Не для демонстрации на экране.
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
     * И определить файл хранения данных.
     * Пустой.
     */
    public Product(DB db) {
        super(db);
    }


    /***
     * Получить все товары в виде списка
     * @return список Product
     */
    public ArrayList<Product> getAll() {
        ArrayList<Product> list = new ArrayList<>();
        for (String str : FileDB.getAll(db)) {
            Product p = new Product(db);
            p.fromString(str);
            list.add(p);
        }
        return list;
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
        if (val.length > 4) description = val[4]; // последний элемент массива может пропасть, если параметр пуст
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
