package tools;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by yo on 2016/6/14.
 */
public class ParseUtils {

    /**
     * 解析如下格式的XML文件
     * <apps slick-uniqueid="3">
     *     <app>
     *         <id>1</id>
     *         <name>Google Maps</name>
     *         <version>1.0</version>
     *     </app>
     *     .
     *     .
     *     .
     * </apps>
     * @param xmlData
     */
    public static void parseXMLWithPull(String xmlData){
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventType = xmlPullParser.getEventType();
            String id = "";
            String name = "";
            String version = "";
            while(eventType != XmlPullParser.END_DOCUMENT){
                String nodeName = xmlPullParser.getName();
                switch (eventType){
                    // 开始解析某个节点
                    case XmlPullParser.START_TAG:
                        if (nodeName.equals("id")){
                            id = xmlPullParser.nextText();
                        }else if (nodeName.equals("name")){
                            name = xmlPullParser.nextText();
                        }else if (nodeName.equals("version")){
                            version = xmlPullParser.nextText();
                        }
                        break;
                    // 完成解析某个节点
                    case XmlPullParser.END_TAG:
                        if (nodeName.equals("app")){
                            Log.d("ParseUtils", "id is " + id);
                            Log.d("ParseUtils", "name is " + name);
                            Log.d("ParseUtils", "version is " + version);
                        }
                }
                eventType = xmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
