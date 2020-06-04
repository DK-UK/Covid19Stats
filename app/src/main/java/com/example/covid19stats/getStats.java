package com.example.covid19stats;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface getStats {

    @GET("all")
    Call<Stats> getGlobalStats();

    @GET("countries")
    Call<List<countryDetails>> getCountryInfo();

    @GET("countries/{name}")
    Call<Stats> getCountryStats(@Path("name") String name);
}
