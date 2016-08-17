package tools;

/**
 * 数据类型  integer----->整型
 *          real ------->浮点型
 *          text ------->文本类型
 *          blob ------->二进制类型
 * 使用primary key将id列设为主键，并用autoincrement表示id列是自增长的
 */
public class SQLUtils {
    public static final String CREATE_BOOK = "create table Book" +
            "(id integer primary key autoincrement," +
            "author text," +
            "price real," +
            "pages integer," +
            "name text)";
    public static final String CREATE_CATEGORY = "create table Category" +
            "(id integer primary key autoincrement," +
            "category_name text," +
            "category_code integer)";
}
