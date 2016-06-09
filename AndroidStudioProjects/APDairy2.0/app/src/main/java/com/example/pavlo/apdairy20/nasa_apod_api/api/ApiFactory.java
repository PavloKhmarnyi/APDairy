package com.example.pavlo.apdairy20.nasa_apod_api.api;

import com.example.pavlo.apdairy20.nasa_apod_api.models.Data;
import com.example.pavlo.apdairy20.nasa_apod_api.utilities.Config;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by pavlo on 08.06.16.
 */
public class ApiFactory {

    private static final OkHttpClient CLIENT  = new OkHttpClient();

    private ApiService apiService;

    public ApiFactory() {
        apiService = getService();
    }

    public static ApiService getService() {
        return getRetrofit().create(ApiService.class);
    }

    private static Retrofit getRetrofit() {
        return new Retrofit.Builder().
                baseUrl(Config.NASA_APOD_URL).
                addConverterFactory(GsonConverterFactory.create()).
                addCallAdapterFactory(RxJavaCallAdapterFactory.create()).
                client(CLIENT).
                build();
    }

    public Observable<Data> getDataOfDay(String date) {
        Observable<Data> observable = apiService.
                getDataOfDay(Config.API_KEY, date).
                subscribeOn(Schedulers.io()).
                observeOn(Schedulers.newThread()).
                cache();

        return observable;
    }
}
