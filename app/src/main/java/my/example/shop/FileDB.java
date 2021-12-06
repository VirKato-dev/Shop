package my.example.shop;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Random;
import java.util.Scanner;

/***
 * Класс ответственный за получение и сохранение информации.
 */
public class FileDB {

    /***
     * имя базы данных о пользователях.
     */
    private final static String db_users = "users.db";

    /***
     * имя базы данных о товарах.
     */
    private final static String db_prods = "products.db";

    /***
     * имя базы данных о содержимом корзин пользователей.
     */
    private final static String db_carts = "carts.db";

    /***
     * Папка для хранения файлов базы данных.
     */
    private static File dir;

    /***
     * Требуется единственный экземпляр класса.
     */
    private static FileDB instance = null;


    /***
     * Запретим ручное создание множества экзепляров класса вручную.
     * Для корректной работы достаточно одного экземпляра.
     */
    private FileDB() {
    }


    /***
     * Инициализация папки для хранения файлов базы данных.
     * @param context контекст приложения или активности.
     */
    public static void getInstance(Context context) {
        if (instance == null) {
            dir = context.getExternalCacheDir();
            instance = new FileDB();
        }
    }


    /***
     * Добавить данные в указанный файл
     * @param db файл базы данных
     * @param str данных
     */
    private static void add(String db, String str) {
        // метод приватный, потому что название файла неизвестно за пределами данного класса

        File file = new File(dir, db);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            // открыть файл в режиме добавления текста
            Writer fw = new FileWriter(file, true);
            String line = str + "\n";
            fw.write(line);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            Log.e("add data", e.getMessage());
        }
    }


    /***
     * Удалить данные из указанного файла по идентификатору.
     */
    private static void remove(String db, String id) {
        // метод приватный, потому что название файла неизвестно за пределами данного класса.
        // сделать копию файла для последующего обновления данных
        File file_from = new File(dir, db + ".bak");
        File file_to = new File(dir, db);
        if (file_to.exists()) file_to.renameTo(new File(dir, db + ".bak"));
        try {
            if (!file_from.exists()) {
                // для правильной работы нам требуется наличие файла-источника (даже пустого)
                file_from.createNewFile();
            }
            if (!file_to.exists()) {
                // требуется наличие файла-приёмника
                file_to.createNewFile();
            }
            // запись в файла будет производится в режиме дополнения информации
            FileWriter writer = new FileWriter(file_to, true);
            Reader reader = new FileReader(file_from);
            Scanner scanner = new Scanner(reader);
            scanner.useDelimiter("\n"); // разделитель строк
            while (scanner.hasNextLine()) {
                // получить очередную строку данных
                String line = scanner.nextLine();
                // разбить на составляющие части
                String[] val = line.split("\\|");
                // взять только первый параметр (он всегда является идентификатором)
                if (!val[0].equals(id)) {
                    // если не те данные, которые нужно удалить, то записать их в новый файл
                    writer.write(line + "\n");
                    writer.flush();
                }
            }
            // закрыть все потоки
            writer.close();
            reader.close();
            scanner.close();
        } catch (IOException e) {
            Log.e("remove data", e.getMessage());
        }
        // удалить старую версию файла
        file_from.delete();
    }


    /***
     * Поиск данных в файле по идентификатору
     * @param id идентификатор
     * @return данные о сеансе
     */
    private static String find(String db, String id) {
        // метод приватный, потому что название файла неизвестно за пределами данного класса
        File file_from = new File(dir, db);
        try {
            if (!file_from.exists()) {
                file_from.createNewFile();
            }
            // Подготавливаем Scanner для получения строк базы
            Reader reader = new FileReader(file_from);
            Scanner scanner = new Scanner(reader);
            scanner.useDelimiter("\n");
            while (scanner.hasNextLine()) {
                // читаем очередную строку данных из файла
                String line = scanner.nextLine();
                // разбить на составляющие части
                String[] val = line.split("\\|");
                // взять только первый параметр (он всегда является идентификатором)
                if (val[0].equals(id)) {
                    // найдены нужные данные
                    return line;
                }
            }
            // закрыть все использованные потоки
            reader.close();
            scanner.close();
        } catch (IOException e) {
            // отладочная информация не выводится на основной экран приложения
            Log.e("find session", e.getMessage());
        }
        // не найдены нужные данные
        return "";
    }

    /***
     * Получить уникальный ID для данных в указанной базе.
     * @param db файл базы
     * @return уникальный идентификатор
     */
    private String generateId(String db) {
        String newId = "";
        while (true){
            newId = "id" + new Random().nextInt();
            String[] val = FileDB.find(db, newId).split("\\|");
            if (!val[0].equals("")) break;
        };
        // когда не нашлись данные с таким идентификатором
        return newId;
    }


    /***
     * Найти пользователя в базе по логину.
     * @param login идентификатор пользователя.
     * @return пользователь с параметрами либо "пустой".
     */
    public static User findUserByLogin(String login) {
        User user = new User();
        String data = find(db_users, login);
        if (!data.equals("")) {
            // если данные найдены, то используем их как параметры пользователя
            user.fromString(data);
        }
        return user;
    }


    /***
     * Поместить данные пользователя в базу.
     */
    public static void addUser(User user) {
        // старые данные из базы удалить, чтобы небыло дубликатов информации
        remove(db_users, user.id);
        add(db_users, user.toString());
    }


    /***
     * Найти товар в базе по идентификатору.
     * @param id идентификатор товара
     * @return товар с параметрами либо "пустой".
     */
    public static Product findProductById(String id) {
        Product product = new Product();
        String data = find(db_prods, id);
        if (!data.equals("")) {
            // если данные найдены, то используем их как параметры товара
            product.fromString(data);
        }
        return product;
    }


    /***
     * Получить новый уникальный идентификатор товара.
     * @return уникальный идентификатор товара.
     */
    public String getNewProductId() {
        return generateId(db_prods);
    }


    /***
     * Поместить данные товара в базу.
     */
    public static void addProduct(Product product) {
        // старые данные из базы удалить, чтобы небыло дубликатов информации
        remove(db_prods, product.id);
        add(db_prods, product.toString());
    }


    /***
     * Удалить данные товара из базы.
     */
    public static void removeProduct(Product product) {
        remove(db_prods, product.id);
    }


    /***
     * Найти покупку в базе по идентификатору.
     * @param id идентификатор покупки
     * @return покупка с параметрами либо "пустая".
     */
    public static Cart findCartById(String id) {
        Cart cart = new Cart();
        String data = find(db_carts, id);
        if (!data.equals("")) {
            // если данные найдены, то используем их как параметры покупки
            cart.fromString(data);
        }
        return cart;
    }


    /***
     * Получить новый уникальный идентификатор покупки.
     * @return уникальный идентификатор покупки.
     */
    public String getNewCartId() {
        return generateId(db_carts);
    }


    /***
     * Поместить данные покупки в базу.
     */
    public static void addCart(Cart cart) {
        // старые данные из базы удалить, чтобы небыло дубликатов информации
        remove(db_carts, cart.id);
        add(db_carts, cart.toString());
    }


    /***
     * Удалить данные покупки из базы.
     */
    public static void removeCart(Cart cart) {
        remove(db_carts, cart.id);
    }

}
