package yeeaoo.imooc_mooo;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import utils.HttpUtils;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }
    public void testSendInfo()
{
    String res = HttpUtils.doGet("给我讲个笑话");
    Log.e("TAG", res);
    res = HttpUtils.doGet("给我讲个鬼故事");
    Log.e("TAG", res);
}}