package com.bapspatil.carbons.util;
/*
 ** Created by Bapusaheb Patil {@link https://bapspatil.com}
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkUtils {

    // Check if the device has Internet or not
    public static Boolean hasNetwork(Context context) {
        Boolean isConnected = false; // Initial Value
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (connectivityManager != null)
            activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting())
            isConnected = true;
        return isConnected;
    }

    /*
        Get a cache-enabled Retrofit,
        with 10MB cache,
        and which loads new data if the device is connected to the Internet;
        if not, loads cached data that stays cached for 7 days
    */
    public static Retrofit getCacheEnabledRetrofit(final Context context) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(new Cache(context.getCacheDir(), 10 * 1024 * 1024)) // Setting the cache size to 10MB
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    // If the device is connected to the Internet, fetch new data immediately
                    if(hasNetwork(context))
                        request = request.newBuilder().header("Cache-Control", "public, max-age=" + 1).build();
                    // Else, load cached data that stays cached up to 7 days
                    else
                        request = request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build();
                    return chain.proceed(request);
                })
                .build();

        // Create the Retrofit instance with the above configuration
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .baseUrl(Constants.BASE_URL)
                .build();
    }

}
