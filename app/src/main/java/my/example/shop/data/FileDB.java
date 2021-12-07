package my.example.shop.data;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/***
 * Класс ответственный за получение и сохранение информации.
 */
public class FileDB {

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


    public static ArrayList<String> getAll(DB db) {
        ArrayList<String> list = new ArrayList<>();
        File file_from = new File(dir, db.toString());
        try {
            if (!file_from.exists()) {
                file_from.createNewFile();
            }
            // Подготавливаем Scanner для получения строк базы
            Reader reader = new FileReader(file_from);
            Scanner scanner = new Scanner(reader);
            scanner.useDelimiter("\n");
            while (scanner.hasNextLine()) {
                // добавить очередную строку данных из файла в список
                list.add(scanner.nextLine());
            }
            // закрыть все использованные потоки
            reader.close();
            scanner.close();
        } catch (IOException e) {
            // отладочная информация не выводится на основной экран приложения
            Log.e("get all", e.getMessage());
        }
        return list;
    }

    /***
     * Добавить данные в указанный файл
     * @param db файл базы данных
     * @param str данных
     */
    public static void add(DB db, String str) {
        File file = new File(dir, db.toString());
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
    public static void remove(DB db, String id) {
        // сделать копию файла для последующего обновления данных
        File file_from = new File(dir, db.toString() + ".bak");
        File file_to = new File(dir, db.toString());
        if (file_to.exists()) file_to.renameTo(file_from);
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
    public static String find(DB db, String id) {
        File file_from = new File(dir, db.toString());
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
    public static String generateId(DB db) {
        String newId = "";
        while (true) {
            newId = "id" + new Random().nextInt();
            String[] val = FileDB.find(db, newId).split("\\|");
            // идентификатор записан в строке как первый параметр
            // (урегулировано в соответствующих классах User, Product, Cart)
            if (val[0].equals("")) {
                return newId;
            }
        }
    }
}
