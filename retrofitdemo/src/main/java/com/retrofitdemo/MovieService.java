package com.retrofitdemo;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;


/**
 * Created by yo on 2016/9/7.
 */
public interface MovieService {
    /**
     * 在不使用RxJava时，方法返回的是Call：Call<MovieEntity> getTopMovie()
     * 但是当使用RxJava时，方法返回的将不是Call，而是一个Observable
     * @param start
     * @param count
     * @return
     */
    @GET("top250")
    Observable<HttpResult<List<Subject>>> getTopMovie(@Query("start") int start, @Query("count") int count);
}
