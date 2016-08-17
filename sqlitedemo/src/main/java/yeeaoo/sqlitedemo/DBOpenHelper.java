package yeeaoo.sqlitedemo;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yo on 2016/4/7.
 */
public class DBOpenHelper extends SQLiteOpenHelper {

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBOpenHelper(Context context, String name) {
        super(context, name, null, 1);
    }

    // 首次创建数据库的时候调用
    // 一般可以把建库建表的操作放在这里
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if exists stutb(_id integer primary key autoincrement, name text not null, sex text not null, age integer not null)");
        db.execSQL("insert into stutb(name,sex,age)values('张珊','女'23)");
    }
    // 当数据库的版本发生变化是会自动执行
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
