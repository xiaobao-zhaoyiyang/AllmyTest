package com.retrofitdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.click_me_BN)
    Button clickMeBN;
    @Bind(R.id.result_TV)
    TextView resultTV;

    private Subscriber<List<Subject>> subscriber;

    private ProgressSubscriber.SubscriberOnNextListener getTopMovieOnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getTopMovieOnNext = new ProgressSubscriber.SubscriberOnNextListener<List<Subject>>() {
            @Override
            public void onNext(List<Subject> subjects) {
                resultTV.setText(subjects.toString());
            }
        };


    }
    @OnClick(R.id.click_me_BN)
    public void onClick(){
        getMovie();
    }

    // 进行网络请求
    private void getMovie() {
//        String baseUrl = "https://api.douban.com/v2/movie/";
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(baseUrl)
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())// 添加了RxJava
//                .build();

        // 不使用RxJAva
//        MovieService movieService = retrofit.create(MovieService.class);
//        Call<MovieEntity> call = movieService.getTopMovie(0, 10);
//        call.enqueue(new Callback<MovieEntity>() {
//            @Override
//            public void onResponse(Call<MovieEntity> call, Response<MovieEntity> response) {
//                resultTV.setText(response.body().toString());
//            }
//
//            @Override
//            public void onFailure(Call<MovieEntity> call, Throwable t) {
//                resultTV.setText(t.getMessage());
//            }
//        });
        

        // 使用RxJava但未封装
//        MovieService movieService = retrofit.create(MovieService.class);
//
//        movieService.getTopMovie(0, 10)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<MovieEntity>() {
//                    @Override
//                    public void onCompleted() {
//                        Toast.makeText(MainActivity.this, "Get Top movie Completed", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        resultTV.setText(e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(MovieEntity movieEntity) {
//                        resultTV.setText(movieEntity.toString());
//                    }
//                });

// ---------------------------------华丽丽的分割线-------------------------------------------------------------

        // 封装之后
//        subscriber = new Subscriber<List<Subject>>(){
//
//            @Override
//            public void onCompleted() {
//                Toast.makeText(MainActivity.this, "Get Top movie Completed", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                resultTV.setText(e.getMessage());
//            }
//
//            @Override
//            public void onNext(List<Subject> subjects) {
//                resultTV.setText(subjects.get(0).toString());
//            }
//        };
//
//        HttpMethods.getInstance().getTopMovie(subscriber, 0, 10);

        // 进一步进行封装，只关心数据部分
        HttpMethods.getInstance().getTopMovie(
                new ProgressSubscriber<List<Subject>>(getTopMovieOnNext, MainActivity.this),
                0, 10
        );
    }
}
