package my.example.shop.data;

import androidx.annotation.NonNull;

import java.util.Locale;

/***
 * Описание характеристик покупки на одно наименование товара.
 */
public class Cart  extends DataEntity{

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
     * Создаваемый экземляр должен сразу быть привязан к определённому файлу хранения данных.
     * Экземпляр изначально поинициализирован параметрами-пустышки.
     * Поэтому называется "Пустой".
     * @param db имя файла данных.
     */
    protected Cart(DB db) {
        super(db);
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
