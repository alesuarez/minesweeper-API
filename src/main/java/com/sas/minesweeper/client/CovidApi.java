package com.sas.minesweeper.client;

import com.sas.minesweeper.entities.dtos.covid.Regions;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.http.GET;

public interface CovidApi {
    // https://covid-api.com/api/
    @GET("/api/regions")
    Call<Regions> getRegions();
}
