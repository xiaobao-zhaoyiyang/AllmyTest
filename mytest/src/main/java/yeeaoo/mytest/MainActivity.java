package yeeaoo.mytest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.io.File;

import receiver.MyBroadcastReceiver;

public class MainActivity extends AppCompatActivity {
    private ImageView mImageView1, mImageView2;
    private String mUrl = "http://inthecheesefactory.com/uploads/source/glidepicasso/cover.jpg";

    private IntentFilter intentFilter;
    private MyBroadcastReceiver mbr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView1 = (ImageView) findViewById(R.id.imageView1);
        mImageView2 = (ImageView) findViewById(R.id.imageView2);
//        Glide.with(this).load(mUrl).transform(new GlideCircleTransform(this)).into(mImageView1);
        Glide.with(this).load(mUrl).transform(new GlideRoundTransform(this, 50)).into(mImageView2);

        mImageView1.setVisibility(View.VISIBLE);
        mImageView1.setImageResource(R.drawable.test);
        AnimationDrawable drawable = (AnimationDrawable) mImageView1.getDrawable();
        drawable.start();

        File myName = Glide.getPhotoCacheDir(this, "MyName");
        Log.i("TAG", myName.exists() + "");

        intentFilter = new IntentFilter();
        intentFilter.addAction("broadcastTest");
        intentFilter.addAction("broad");
        mbr = new MyBroadcastReceiver();
        registerReceiver(mbr, intentFilter);

    }

    public void FragmentTest(View view){
        Intent intent = new Intent(this, TestFragmentActivity.class);
        startActivity(intent);
    }
    public void BroadcastReceiverTest(View view){
        Log.i("MainActivity", "发送广播");
        Intent intent = new Intent();
        intent.setAction("broadcastTest");
        sendBroadcast(intent);
    }
    public void BroadTest(View view){
        Log.i("MainActivity", "发送广播");
        Intent intent = new Intent();
        intent.setAction("broad");
        sendBroadcast(intent);
    }

    public void DBTest(View view){
        Intent intent = new Intent(this, DBTestActivity.class);
        startActivity(intent);
    }

    public void NotificationTest(View view){
        Intent intent = new Intent(this, NotificationActivity.class);
        startActivity(intent);
    }

    public void PhotoTest(View view){
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    public void ServiceTest(View view){
        Intent intent = new Intent(this, ServiceTestActivity.class);
        startActivity(intent);
    }

    public void locationTest(View view){
        Intent intent = new Intent(this, LocationTestActivity.class);
        startActivity(intent);
    }
    public void sensorTest(View view){
        Intent intent = new Intent(this, LightSensorTestActivity.class);
        startActivity(intent);
    }
    public void EmojiTest(View view){
        Intent intent = new Intent(this, EmojiTestActivity.class);
        Person person = new Person();
        person.setName("JOM");
        person.setAge(15);
        intent.putExtra("person", person);
        startActivity(intent);
    }
    public void WidgetTest(View view){
        Intent intent = new Intent(this, WidgetTestActivity.class);
        startActivity(intent);
    }
    public void ViewFlipperTest(View view){
        Intent intent = new Intent(this, ViewFlipperTestActivity.class);
        startActivity(intent);
    }
    public void SlidingTest(View view){
        Intent intent = new Intent(this, SlidingTestActivity.class);
        startActivity(intent);
    }
    public void IndicatorTest(View view){
        Intent intent = new Intent(this, IndicatorTestActivity.class);
        startActivity(intent);
    }
    public void ScreenshotTest(View view){
        Intent intent = new Intent(this, ScreenshotTestActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mbr);
    }

    public class GlideCircleTransform extends BitmapTransformation {
        public GlideCircleTransform(Context context) {
            super(context);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            // TODO this could be acquired from the pool too
            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName();
        }
    }

    public class GlideRoundTransform extends BitmapTransformation {

        private float radius = 0f;

        public GlideRoundTransform(Context context) {
            this(context, 4);
        }

        public GlideRoundTransform(Context context, int dp) {
            super(context);
            this.radius = Resources.getSystem().getDisplayMetrics().density * dp;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return roundCrop(pool, toTransform);
        }

        private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
            canvas.drawRoundRect(rectF, radius, radius, paint);
            return result;
        }

        @Override public String getId() {
            return getClass().getName() + Math.round(radius);
        }
    }

    //    内存缓存是在设备的 RAM 中去维护图片的。这里没有 IO 行为，所以这个操作是很快的。另一方面是 RAM(内存) 的大小是非常有限的。
// 寻找一个大内存缓存的平衡点（大量图像空间）与一个小内存缓存（最大限度减少我们 App 的资源消耗）并不容易。
// Glide 内部使用了 MemorySizeCalculator类去决定内存缓存大小以及 bitmap 的缓存池。
// bitmap 池维护了你 App 的堆中的图像分配。正确的 bitmpa 池是非常必要的，因为它避免很多的图像重复回收，这样可以确保垃圾回收器的管理更加合理。
//
//    幸运的是，你已经得到了 Glide 的 MemorySizeCalculator类以及默认的计算：
//
//    MemorySizeCalculator calculator = new MemorySizeCalculator(context);
//    int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
//    int defaultBitmapPoolSize = calculator.getBitmapPoolSize();
//    上面这段代码相当有用，如果我们想要用默认值作为基准，然后调整它。
//    比如，如果你认为你的 app 需要 20% 大的缓存作为 Glide 的默认值，用我们上面的变量去计算他们：
//
//    int customMemoryCacheSize = (int) (1.2 * defaultMemoryCacheSize);
//    int customBitmapPoolSize = (int) (1.2 * defaultBitmapPoolSize);
//    因为我们已经计算出了我们的内存缓存和 bitmap 池的大小，我们可以在我们的 Glide module 代码里去得到。
//   在 applyOptions()方法中，我们可以在 GlideBuilder对象中调用相应的方法。
//    public class CustomCachingGlideModule implements GlideModule {
//        @Override
//        public void applyOptions(Context context, GlideBuilder builder) {
//            MemorySizeCalculator calculator = new MemorySizeCalculator(context);
//            int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
//            int defaultBitmapPoolSize = calculator.getBitmapPoolSize();
//            int customMemoryCacheSize = (int) (1.2 * defaultMemoryCacheSize);
//            int customBitmapPoolSize = (int) (1.2 * defaultBitmapPoolSize);
//            builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
//            builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));
//        }
//
//        @Override
//        public void registerComponents(Context context, Glide glide) {
//            // nothing to do here
//        }
//    }

    /**
     * 自定义磁盘缓存
     * 调整磁盘缓存和和刚才的很像，但是我们有一个更大的决定去做，磁盘缓存可以位于应用的私有目录（换句话说，除了它自己，没有别的应用可以访问）。
     * 否则，磁盘缓存也可以位于外部存储，公有目录（更多信息，请看 Storage Options）。
     * 不能一起设置这两个为之。
     * Glide 为这两个选项都提供了它的实现： InternalCacheDiskCacheFactory和 ExternalCacheDiskCacheFactory。
     * 就像内存缓存的构造函数一样，在它们的构造函数内都传一个磁盘缓存的工厂类：
     */
//    public class CustomCachingGlideModule implements GlideModule {
//        @Override
//        public void applyOptions(Context context, GlideBuilder builder) {
//            // set size & external vs. internal
//            int cacheSize100MegaBytes = 104857600;
//            InternalCacheDiskCacheFactory factory = new InternalCacheDiskCacheFactory(context, cacheSize100MegaBytes);
//            builder.setDiskCache(new InternalCacheDiskCacheFactory(context, cacheSize100MegaBytes));
//            builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, cacheSize100MegaBytes));
//        }

//        @Override
//        public void registerComponents(Context context, Glide glide) { // nothing to do here
//        }
//    }
    /**
     * 上面的代码将设置磁盘缓存到应用的内部目录，并且设置了最大的大小为 100M。
     * 下面注释的那行代码会设置磁盘缓存到外部存储（也设置了最大大小为 100M）。
     */

    /**这两个选项都不让你选一个特点的目录。如果你要让磁盘缓存到指定的目录，你要使用 DiskLruCacheFactory：

     // or any other path
     String downloadDirectoryPath = Environment.getDownloadCacheDirectory().getPath();
     builder.setDiskCache(new DiskLruCacheFactory(downloadDirectoryPath, cacheSize100MegaBytes));
     // In case you want to specify a cache sub folder (i.e. "glidecache"):
     // builder.setDiskCache(// new DiskLruCacheFactory( downloadDirectoryPath, "glidecache", cacheSize100MegaBytes ) //); 自定义缓存实现
     目前为止，我们已经向你展示了如何去移动和设置缓存为确定的大小。然而，所有的调用都引用了缓存的原始实现。如果你有你自己的缓存实现呢？

     嗯，你看到我们总是创建一个 Glide 的默认缓存的实现的新实例。你可以完成你自己的实现，创建和实例化它，并用上上面所有你看到的方法。你必须确保你的缓存代码实现了如下接口方法：

     Memory cache needs to implement: MemoryCache Bitmap pool needs to implement BitmapPool Disk cache needs to implement: DiskCache Outlook
     */
}
