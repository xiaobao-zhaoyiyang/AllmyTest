package yeeaoo.sqlitedemo;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;

import javax.security.auth.login.LoginException;

public class MainActivity extends AppCompatActivity {
    private EditText e1, e2, e3;
    private Button b1;
    private SQLiteDatabase db;
    private ContentValues values;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 每个程序都有自己的数据库，默认情况下是各自互不干扰
        // 创建一个数据库并且打开
//        SQLiteDatabase db = openOrCreateDatabase("user.db",
//                MODE_PRIVATE, null);
//        String sql = "create table if not exists usertb(_id integer primary key autoincrement, name text not null, age integer not null, sex text not null)";
//        db.execSQL(sql);
//        String sql1 = "insert into usertb(name,sex,age) values('张三','女',18)";
//        db.execSQL(sql1);
//        db.execSQL("insert into usertb(name,sex,age) values('李四','女',19)");
//        db.execSQL("insert into usertb(name,sex,age) values('王五','女',20)");
//
//        Cursor cursor = db.rawQuery("select * from usertb", null);
//        if (cursor != null){
//            cursor.moveToFirst();
//            while (cursor.moveToNext()){
//                Log.i("info", "_id: " + cursor.getInt(cursor.getColumnIndex("_id")));
//                Log.i("info", "name: " + cursor.getString(cursor.getColumnIndex("name")));
//                Log.i("info", "sex: " + cursor.getString(cursor.getColumnIndex("sex")));
//                Log.i("info", "age: " + cursor.getInt(cursor.getColumnIndex("age")));
//                Log.i("info", "!!!!!!!!!!!!");
//            }
//            cursor.close();
//        }
//        db.close();

        // 使用ContentValues
//        final SQLiteDatabase db = openOrCreateDatabase("user1.db",
//                MODE_PRIVATE, null);
//        db.execSQL("create table if not exists userTb" +
//                "(_id integer primary key autoincrement, " +
//                "name text not null, age integer not null, " +
//                "sex text not null)");
//        ContentValues values = new ContentValues();
//        values.put("name", "张珊");
//        values.put("age", 20);
//        values.put("sex", "女");
//        db.insert("userTb", null, values);
//        values.clear();
//        values.put("name", "张珊shan");
//        values.put("age", 26);
//        values.put("sex", "女");
//        db.insert("userTb", null, values);
//        values.clear();
//        values.put("sex", "男");
//        // 将id大于2的人性别改为男
//        db.update("userTb", values, "_id>?", new String[]{"2"});
//        db.close();
//        e1 = (EditText) findViewById(R.id.e1);
//        e2 = (EditText) findViewById(R.id.e2);
//        e3 = (EditText) findViewById(R.id.e3);
        b1 = (Button) findViewById(R.id.insert);
//        db = openOrCreateDatabase("user2.db", MODE_PRIVATE, null);
//        db.execSQL("create table if not exists usertb2" +
//                "(_id integer primary key autoincrement,name text not null,sex text not null,age integer not null)");
//
//        b1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                String a = e1.getText().toString().trim();
//                String b = e2.getText().toString();
//                int c = Integer.parseInt(e3.getText().toString());
//                values = new ContentValues();
//                values.put("age", c);
//                values.put("sex", b);
//                values.put("name", a);
//                db.insert("usertb2", null, values);
//                //db.execSQL("insert into userdb1(name,sex,age)values(a,b,11)");
//                //1,用execSQL语句怎么把edittext的内容插入到表中呢？
//                values.clear();
//                db.execSQL("insert into usertb2(name, sex, age) values('" + a + "','" + b + "'," + c + ")");
//                db.close();//2，为什么输入关闭数据库的语句Logcat就会出错？
//            }
//        });
//
//        DBOpenHelper helper = new DBOpenHelper(this, "Student.db");
////        helper.getReadableDatabase();// 获取一个只读的数据库
//        SQLiteDatabase db = helper.getWritableDatabase();
//        insertData();
//        getContact();
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });
    }
    // 插入数据
    private void insertData(){
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        // 向联系人中插入一行数据
        Uri uri = cr.insert(RawContacts.CONTENT_URI, values);
        long raw_contact_id = ContentUris.parseId(uri);
        values.clear();
        // 插入人名
        values.put(StructuredName.RAW_CONTACT_ID, raw_contact_id);
        values.put(StructuredName.DISPLAY_NAME, "张珊珊");
        values.put(StructuredName.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
        uri = cr.insert(ContactsContract.Data.CONTENT_URI, values);
        // 插入电话信息
        values.clear();
        values.put(Phone.RAW_CONTACT_ID, raw_contact_id);
        values.put(Phone.NUMBER, "13563214589");
        values.put(Phone.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
        uri = cr.insert(ContactsContract.Data.CONTENT_URI, values);
    }

    // 查询手机联系人
    private void getContact(){
        ContentResolver contentResolver = getContentResolver();
        Cursor c = contentResolver.query(Contacts.CONTENT_URI,
                new String[]{Contacts._ID, Contacts.DISPLAY_NAME},
                null, null, null);
        if (c != null){
            while (c.moveToNext()){
                int id = c.getInt(c.getColumnIndex(Contacts._ID));
                Log.i("info", "_id:" + id);
                Log.i("info", "name:" + c.getString(c.getColumnIndex(Contacts.DISPLAY_NAME)));
                // 根据联系人的id查询联系人的电话
                Cursor c1 = contentResolver.query(Phone.CONTENT_URI,
                        new String[]{Phone.NUMBER, Phone.TYPE},
                        Phone.CONTACT_ID + "=" + id, null, null);
                if (c1 != null){
                    while (c1.moveToNext()){
                        int type = c1.getInt(c1.getColumnIndex(Phone.TYPE));
                        if (type == Phone.TYPE_HOME){
                            Log.i("info", "家庭电话" + c1.getString(c1.getColumnIndex(Phone.NUMBER)));
                        }else if (type == Phone.TYPE_MOBILE){
                            Log.i("info", "手机" + c1.getString(c1.getColumnIndex(Phone.NUMBER)));
                        }
                    }
                    c1.close();
                }
                // 根据联系人的id查询联系人的邮箱
                Cursor c2 = contentResolver.query(Email.CONTENT_URI,
                        new String[]{Email.DATA, Email.TYPE},
                        Email.CONTACT_ID + "=" + id,
                        null, null);
                if (c2 != null){
                    while (c2.moveToNext()){
                        int type = c2.getInt(c2.getColumnIndex(Email.TYPE));
                        if (type == Email.TYPE_WORK){
                            Log.i("info", "工作邮箱" + c2.getString(c2.getColumnIndex(Email.DATA)));
                        }
                    }
                    c2.close();
                }
            }
            c.close();
        }
    }
}
