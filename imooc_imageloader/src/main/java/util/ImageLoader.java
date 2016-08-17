package util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 图片加载类
 */
public class ImageLoader {
    private static ImageLoader mInstance;
    /**
     * 图片缓存的核心对象
     */
    private LruCache<String, Bitmap> mLruCache;
    /**
     * 线程池
     */
    private ExecutorService mThreadPool;
    private static final int DEFAULT_THREAD_COUNT = 1;

    /**
     * 队列的调度方式
     */
    private Type mType = Type.LIFO;
    /**
     * 任务队列
     */
    private LinkedList<Runnable> mTaskQueue;
    /**
     * 后台轮训线程
     */
    private Thread mPoolThread;
    private Handler mPoolThreadHandler;
    /**
     * UI线程中的Handler
     */
    private Handler mUIHandler;

    private Semaphore mSemaphorePoolThreadHandler = new Semaphore(0);

    private Semaphore mSemaphoreThreadPool;

    public enum Type{
        FIFO, LIFO;
    }
    private ImageLoader(int threadCount, Type type){
        init(threadCount, type);
    }

    /**
     * 初始化
     * @param threadCount 线程数
     * @param type  类型
     */
    private void init(int threadCount, Type type) {
        // 后台轮训线程
        mPoolThread = new Thread(){
            @Override
            public void run() {
                super.run();
                Looper.prepare();
                mPoolThreadHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        // 线程取出一个任务进行执行
                        mThreadPool.execute(getTask());
                        try {
                            mSemaphoreThreadPool.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                // 释放一个信号量
                mSemaphorePoolThreadHandler.release();
                Looper.loop();
            }
        };
        mPoolThread.start();
        // 获取我们应用的最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory / 8;
        mLruCache = new LruCache<String, Bitmap>(cacheMemory){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
       // 线程池的初始化
        mThreadPool = Executors.newFixedThreadPool(threadCount);
        mTaskQueue = new LinkedList<>();
        mType = type;

        mSemaphoreThreadPool = new Semaphore(threadCount);
    }

    /**
     * 从任务队列取出一个方法
     * @return
     */
    private Runnable getTask() {
        if (mType == Type.FIFO){
           return mTaskQueue.removeFirst();
        }else if (mType == Type.LIFO){
            return mTaskQueue.removeLast();
        }
        return null;
    }

    public static ImageLoader getInstance(int threadCount, Type type){
        if (mInstance == null){
            synchronized (ImageLoader.class){
                if (mInstance == null){
                    mInstance = new ImageLoader(threadCount, type);
                }
            }
        }
        return mInstance;
    }

    /**
     * 根据path为imageView设置图片
     * @param path
     * @param imageView
     */
    public void loadImage(final String path, final ImageView imageView){
        imageView.setTag(path);

        if (mUIHandler == null){
            mUIHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    // 获取得到图片，为imageView回调设置图片
                    ImgBeanHolder holder = (ImgBeanHolder) msg.obj;
                    Bitmap bm = holder.bitmap;
                    ImageView imageView = holder.imageView;
                    String path = holder.path;
                    // 将path与getTag存储路径进行比较
                    if (imageView.getTag().toString().equals(path)){
                        imageView.setImageBitmap(bm);
                    }
                }
            };
        }
        // 根据path在缓存中获取bitmap
        Bitmap bm = getBitmapFromLruCache(path);
        if (bm != null){
            refreshBitmap(path, imageView, bm);
        }else {
            addTask(new Runnable(){

                @Override
                public void run() {
                    // 加载图片 图片压缩
                    // 1、获取图片需要显示的大小
                    ImageSize imageSize = getImageViewSize(imageView);
                    // 2、压缩图片
                    Bitmap bm = decodeSampledBitmapFromPath(path, imageSize.width, imageSize.height);
                    // 3、图片加入到缓存
                    addBitmapToLruCache(path, bm);

                    refreshBitmap(path, imageView, bm);

                    mSemaphoreThreadPool.release();
                }
            });
        }
    }

    private void refreshBitmap(String path, ImageView imageView, Bitmap bm) {
        Message message = Message.obtain();
        ImgBeanHolder holder = new ImgBeanHolder();
        holder.path = path;
        holder.imageView = imageView;
        holder.bitmap = bm;
        message.obj = holder;
        mUIHandler.sendMessage(message);
    }

    /**
     * 将图片加入缓存
     * @param path
     * @param bm
     */
    private void addBitmapToLruCache(String path, Bitmap bm) {
        if (getBitmapFromLruCache(path) == null){
            if (bm != null){
                mLruCache.put(path, bm);
            }
        }
    }

    /**
     * 根据图片需要显示的宽和高对图片进行压缩
     * @param path
     * @param width
     * @param height
     * @return
     */
    private Bitmap decodeSampledBitmapFromPath(String path, int width, int height) {
        // 获得图片的宽和高，并不把图片加载到内存中
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        options.inSampleSize = caculateInSampleSize(options, width, height);
        // 使用获取到的inSampleSize再次解析图片
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        return bitmap;
    }

    /**
     * 根据需求的宽和高以及图片的实际宽和高计算SampleSize
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private int caculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;
        if (width > reqWidth || height > reqHeight){
            int widthRadio = Math.round(width * 1.0f / reqWidth);
            int heightRadio = Math.round(height * 1.0f / reqHeight);
            inSampleSize = Math.max(widthRadio, heightRadio);
        }
        return inSampleSize;
    }

    /**
     * 根据imageView获取适当的压缩的宽和高
     * @param imageView
     * @return
     */
    private ImageSize getImageViewSize(ImageView imageView) {
        ImageSize imageSize = new ImageSize();
        DisplayMetrics displayMetrics = imageView.getContext().getResources().getDisplayMetrics();
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        // 获取imageView的实际宽度
        int width = imageView.getWidth();
        if (width <= 0){
            // 获取imageView在layout中声明的宽度
            width = layoutParams.width;
        }
        if (width <= 0){
            width = getImageViewFiledValue(imageView, "mMaxWidth");// 检查最大值
        }
        if (width <= 0){
            width = displayMetrics.widthPixels;
        }

        // 获取imageView的实际高度
        int height = imageView.getHeight();
        if (height <= 0){
            // 获取imageView在layout中声明的高度
            height = layoutParams.height;
        }
        if (height <= 0){
            height = getImageViewFiledValue(imageView, "mMaxHeight");// 检查最大值
        }
        if (height <= 0){
            height = displayMetrics.heightPixels;
        }
        imageSize.width = width;
        imageSize.height = height;
        return imageSize;
    }

    /**
     * 通过反射获取ImageView的某个值
     * @param object
     * @return
     */
    private static int getImageViewFiledValue(Object object, String fieldName){
        int value = 0;
        try {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = field.getInt(object);
            if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE){
                value = fieldValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    private synchronized void addTask(Runnable runnable) {
        mTaskQueue.add(runnable);
        try {
            if (mPoolThreadHandler == null) {
                mSemaphorePoolThreadHandler.acquire();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mPoolThreadHandler.sendEmptyMessage(0x110);
    }

    /**
     * 根据path在缓存中获取bitmap
     * @param key
     * @return
     */
    private Bitmap getBitmapFromLruCache(String key) {
        return mLruCache.get(key);
    }

    private class ImgBeanHolder{
        Bitmap bitmap;
        ImageView imageView;
        String path;
    }

    private class ImageSize{
        int width;
        int height;
    }
}
