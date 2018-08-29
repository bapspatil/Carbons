package com.bapspatil.carbons.network;
/*
 ** Created by Bapusaheb Patil {@link https://bapspatil.com}
 */

import com.bapspatil.carbons.model.FlickrResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FlickrAPI {

    @GET("rest")
    Call<FlickrResponse> searchForImages(@Query("method") String METHOD, @Query("api_key") String API_KEY, @Query("format") String FORMAT, @Query("nojsoncallback") int NOJSONCALLBACK, @Query("extras") String EXTRAS, @Query("text") String TEXT, @Query("page") int PAGE);

}
