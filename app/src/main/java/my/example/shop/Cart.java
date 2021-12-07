package my.example.shop;

import androidx.annotation.NonNull;

import java.util.Locale;

/***
 * Описание характеристик покупки на одно наименование товара.
 */
public class Cart {

    /***
     * Идентификатор покупки.
     */
    public String id = "";

    /***
     * Идентификатор покупателя.
     */
    public String user_id = "";

    /***
     * Идентификатор товара.
     */
    public String product_id = "";

    /***
     * Дата помещения товара в корзину.
     */
    public long date_add = 0L;

    /***
     * Дата выкупа товара.
     * 0 - не выкуплен
     */
    public long date_buy = 0L;

    /***
     * Вес (весовой) или количество штук (не весовой).
     */
    public long total = 0L;


    /***
     * Создать покупку со стандартными параметрами.
     */
    public Cart() {}


    /***
     * Сохранить данные покупки в базу.
     */
    public void save() {
        FileDB.remove(DB.CARTS, id);
        FileDB.add(DB.CARTS, toString());
    }


    /***
     * Удалить данные покупки из базы.
     */
    public void remove() {
        FileDB.remove(DB.CARTS, id);
    }


    /***
     * Найти параметры покупки в базе по идентификатору и использовать их.
     * @param id идентификатор покупки
     */
    public void find(String id) {
        String data = FileDB.find(DB.CARTS, id);
        if (!data.equals("")) {
            // если данные найдены, то используем их как параметры покупки
            fromString(data);
        }
    }


    /***
     * Получить новый уникальный идентификатор покупки.
     */
    public void setId() {
        id = FileDB.generateId(DB.CARTS);
    }


    /***
     * Установить параметры текущей покупки в соответствии с данными в строке.
     * @param str строка параметров разделённых "|".
     */
    public void fromString(String str) {
        String[] val = str.split("\\|");
        id = val[0];
        user_id = val[1];
        product_id = val[2];
        date_add = Long.parseLong(val[3]);
        date_buy = Long.parseLong(val[4]);
        total = Long.parseLong(val[5]);
    }


    /***
     * Получить строку параметров товара разделённых "|".
     * @return строка удобная для хранения в файле.
     */
    @NonNull
    @Override
    public String toString() {
        // идентификатор пишем первым (требуется для поиска по базе)
        return id + "|" + user_id + "|" + product_id + "|" +
                String.format(Locale.ENGLISH,"%d", date_add) + "|" +
                String.format(Locale.ENGLISH,"%d", date_buy) + "|" +
                String.format(Locale.ENGLISH,"%d", total);
    }

}
