package tools;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by yo on 2016/6/12.
 */
public class FileCollector {
    /**
     * 将内容保存到data/data 中
     * @param context   上下文对象
     * @param data      保存的内容
     * @param fileName  保存文件的名称
     */
    public static void saveFile(Context context, String data, String fileName){
        FileOutputStream fos = null;
        BufferedWriter writer = null;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(fos));
            writer.write(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (writer != null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     *  根据名称读取文件
     * @param context   上下文对象
     * @param fileName  文件名称
     * @return
     */
    public static String readFile(Context context, String fileName){
        FileInputStream fis = null;
        BufferedReader reader = null;
        StringBuffer content = new StringBuffer();
        try {
            fis = context.openFileInput(fileName);
            reader = new BufferedReader(new InputStreamReader(fis));
            String line = "";
            while ((line = reader.readLine()) != null){
                content.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }

    /**
     *  使用SharedPreferences对数据进行存储
     * @param context  上下文对象
     * @param SPName  储存的文件名称
     * @param itemName  文件中小项的名称
     * @param content   小项对应的内容
     */
    public static void saveFileBySP(Context context, String SPName, String itemName, String content){
        SharedPreferences.Editor editor = (SharedPreferences.Editor) context.getSharedPreferences(SPName, Context.MODE_PRIVATE);
        editor.putString(itemName, content);
        editor.commit();
    }

    /**
     *  读取SharedPreferences中存储的数据
     * @param context  上下文对象
     * @param SPName    文件名称
     * @param itemName  储存小项的名称
     * @return  该小项下所储存的内容
     */
    public static String readFileFromSP(Context context, String SPName, String itemName){
        SharedPreferences pref = context.getSharedPreferences(SPName, Context.MODE_PRIVATE);
        String content = pref.getString(itemName, "");
        return content;
    }
}
