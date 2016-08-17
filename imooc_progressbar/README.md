# WithTextprogressBar
带进度显示的ProgressBar，有水平和圆形两种

通过自定义的View实现进度条的显示
使用方法：
1、在xml文件中进行引用，并进行相关属性的设置
    <views.HorizontalProgressbarWithProgress
            android:id="@+id/id_progress1"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:progress="50"
            android:padding="5dp"
            android:layout_marginTop="30dp"
            app:progress_text_color="#44ff0000"
            app:progress_reach_color="#ffff0000"
            app:progress_unreach_color="#44ff0000"/>
    <views.RoundProgressBarWithProgress
            android:id="@+id/id_progress2"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:progress="50"
            android:padding="5dp"
            android:layout_marginTop="30dp"
            app:progress_text_color="#44ff0000"
            app:progress_reach_color="#ffff0000"
            app:progress_unreach_color="#44ff0000"
            app:radius="50dp"/>
2、在Activity中获取控件
    mHProgress = (HorizontalProgressbarWithProgress) findViewById(R.id.id_progress1);
    mRProgress = (RoundProgressBarWithProgress) findViewById(R.id.id_progress2);
3、通过相关方法更近进度显示就可以了，比如Handler.
