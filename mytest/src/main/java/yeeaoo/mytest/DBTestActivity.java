package yeeaoo.mytest;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import tools.MyDateBaseHelper;

/**
 * Created by yo on 2016/6/12.
 */
public class DBTestActivity extends AppCompatActivity implements View.OnClickListener{
    private Button bt_create, bt_upgrade, bt_insert, bt_update,
            bt_delete, bt_query, bt_replace;
    private MyDateBaseHelper dbHelper;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbtest);
        bt_create = (Button) findViewById(R.id.id_createDB);
        bt_upgrade = (Button) findViewById(R.id.id_upDateDB);
        bt_insert = (Button) findViewById(R.id.id_insertDB);
        bt_update = (Button) findViewById(R.id.id_upDateDate);
        bt_delete = (Button) findViewById(R.id.id_deleteDate);
        bt_query = (Button) findViewById(R.id.id_queryDate);
        bt_replace = (Button) findViewById(R.id.id_replaceDate);
        bt_create.setOnClickListener(this);
        bt_upgrade.setOnClickListener(this);
        bt_insert.setOnClickListener(this);
        bt_update.setOnClickListener(this);
        bt_delete.setOnClickListener(this);
        bt_query.setOnClickListener(this);
        bt_replace.setOnClickListener(this);

        if (dbHelper == null){
            dbHelper = new MyDateBaseHelper(this, "BookStore.db", null, 1);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_createDB:
                dbHelper = new MyDateBaseHelper(this, "BookStore.db", null, 1);
                break;
            case R.id.id_upDateDB:
                dbHelper = new MyDateBaseHelper(this, "BookStore.db", null, 2);
                break;
            case R.id.id_insertDB:
                ContentValues values = new ContentValues();
                values.put("name", "The Da Vinci Code");
                values.put("author", "Dan Brown");
                values.put("pages", 454);
                values.put("price", 16.96);
                dbHelper.insertDate("Book", values);
                values.clear();
                values.put("name", "The Lost Symbol");
                values.put("author", "Dan Brown");
                values.put("pages", 510);
                values.put("price", 19.95);
                dbHelper.insertDate("Book", values);
                break;

            case R.id.id_upDateDate:
                ContentValues values1 = new ContentValues();
                values1.put("price", 10.99);
                dbHelper.upDate("Book", values1, "name=?", new String[]{"The Da Vinci Code"});
                break;
            case R.id.id_deleteDate:
                dbHelper.deleteDate("Book", "pages>?", new String[]{"500"});
                break;
            case R.id.id_queryDate:
                Cursor cursor = dbHelper.queryDate("Book", null, null, null, null, null, null);
                if (cursor.moveToFirst()){
                    do {
                        // 遍历Cursor对象，取出数据并打印
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));
                        Log.i("MainActivity", "book name is " + name);
                        Log.i("MainActivity", "book author is " + author);
                        Log.i("MainActivity", "book pages is " + pages);
                        Log.i("MainActivity", "book price is " + price);
                    }while (cursor.moveToNext());
                }
                cursor.close();
                break;
            // deleteBook中的数据同时insert新数据，但要同时操作，同时完成，否则就要保存元数据
            case R.id.id_replaceDate:
                db = dbHelper.getWritableDatabase();
                db.beginTransaction(); // 开启事务
                try{
                    db.delete("Book", null, null);
//                    if (true){
//                        // 在这里手动抛出一个异常，让事务失败
//                        throw new NullPointerException();
//                    }
                    ContentValues va = new ContentValues();
                    va.put("name", "Game of Thrones");
                    va.put("author", "George martin");
                    va.put("pages", 720);
                    va.put("price", 20.85);
                    db.insert("Book", null, va);
                    db.setTransactionSuccessful(); // 事务已经执行成功
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    db.endTransaction(); // 结束事务
                }
                break;
        }
    }
}
