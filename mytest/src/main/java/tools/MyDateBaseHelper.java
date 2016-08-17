package tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * 数据库的Helper类
 */
public class MyDateBaseHelper extends SQLiteOpenHelper {
    private Context mContext;
    private SQLiteDatabase db;
    /**
     *  构造方法
     * @param context  上下文对象
     * @param name     数据库的名称
     * @param factory  允许我们在查询数据的时候返回一个自定义的Cursor，一般传入null
     * @param version  数据库的版本号，可用于数据库进行升级
     */
    public MyDateBaseHelper(Context context, String name, android.database.sqlite.SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLUtils.CREATE_BOOK);
        db.execSQL(SQLUtils.CREATE_CATEGORY);
        Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("drop table if exists book");
//        db.execSQL("drop table if exists Book");
//        db.execSQL("drop table if exists Category");
//        onCreate(db);
        // 上面这样强制的删除在调用onCreate方法在app上线之后是不可取的，会导致用户的数据丢失
        switch (oldVersion){
            case 1:
                db.execSQL(SQLUtils.CREATE_CATEGORY);
            case 2:
                // 为Book表添加一列
                db.execSQL("alter table Book add column category_id integer");
        }
        // 不加break以保证修改能够全部执行，不丢失数据
    }

    /**
     *  向数据库中插入数据
     * @param TableName   表名称
     * @param values      插入的数据
     */
    public void insertDate(String TableName, ContentValues values){
        if (db == null){
            db = getWritableDatabase();
        }
        db.insert(TableName, null, values);
    }

    /**
     *  更新数据库表中的数据
     * @param TableName     表名称
     * @param values        更新的数据
     * @param whereClause   更新的条件
     * @param whereArgs     更新条件
     *        第三第四参数不指定的话默认就是更新所有行
     */
    public void upDate(String TableName, ContentValues values,
                       String whereClause, String[] whereArgs){
        if (db == null){
            db = getWritableDatabase();
        }
        db.update(TableName, values, whereClause, whereArgs);
    }

    /**
     *  删除表中的数据
     * @param TableName    表名称
     * @param whereClause
     * @param whereArgs
     *     三四参数就约束删除的条件，不指定默认删除所有行
     */
    public void deleteDate(String TableName,
                       String whereClause, String[] whereArgs){
        if (db == null){
            db = getWritableDatabase();
        }
        db.delete(TableName, whereClause, whereArgs);
    }

    /**
     *  查询数据
     * @param table             表名称
     * @param columns           查询的类名
     * @param selection         指定where的约束条件
     * @param selectionArgs     为where的占位符提供具体的值
     * @param groupBy           指定需要group by的列
     * @param having            对Group by后的结果进一步约束
     * @param orderBy           指定查询的排列方式
     * @return
     */
    public Cursor queryDate(String table,
                            String[] columns, String selection,
                            String[] selectionArgs, String groupBy,
                            String having, String orderBy){
        if (db == null){
            db = getWritableDatabase();
        }
        Cursor cursor = null;
        cursor = db.query(table, columns, selection, selectionArgs,
                groupBy, having, orderBy);
        return cursor;
    }
}
