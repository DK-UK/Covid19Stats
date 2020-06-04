package com.example.covid19stats;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.leo.simplearcloader.SimpleArcLoader;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class countryStats extends Fragment {

    SimpleArcLoader simpleArcLoader;
    TextView cases, todayCases,deaths,
            todayDeaths,
            recovered,
            active,
            critical,
            countryStats;
    ScrollView scrollView;
    public static final String TAG = "Main";

    private static String name;
    public countryStats() {
        // Required empty public constructor
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 0 && resultCode == Activity.RESULT_OK){
            name = data.getStringExtra("country_name");
            Log.e(TAG, "onActivityResult: Name : "+name);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_country_stats, container, false);


        initializeViews(view);
        simpleArcLoader.start();
        getCountryStats(name);

        return view;
    }

    private void getCountryStats(String name) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://corona.lmao.ninja/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        getStats getStat = retrofit.create(getStats.class);

        Call<Stats> call = getStat.getCountryStats(name);

        call.enqueue(new Callback<Stats>() {
            @Override
            public void onResponse(Call<Stats> call, Response<Stats> response) {
                if (!response.isSuccessful()){
                    Log.e(TAG, "onResponse: "+response.code());
                    return;
                }


                simpleArcLoader.stop();
                simpleArcLoader.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);

                Stats stats = response.body();
                setTextViewValues(stats);

            }

            @Override
            public void onFailure(Call<Stats> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage());
                simpleArcLoader.stop();
                simpleArcLoader.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initializeViews(View view) {
        simpleArcLoader = view.findViewById(R.id.loader);
        countryStats = view.findViewById(R.id.stats);
        cases = view.findViewById(R.id.total_Cases);
        todayCases = view.findViewById(R.id.today_Cases);
        deaths = view.findViewById(R.id.deaths);
        todayDeaths = view.findViewById(R.id.today_deaths);
        recovered = view.findViewById(R.id.recovered);
        active = view.findViewById(R.id.active);
        critical = view.findViewById(R.id.critical);
        scrollView = view.findViewById(R.id.scrollStats);
    }

    private void setTextViewValues(Stats stats) {
        countryStats.setText(name + " Stats");
        cases.setText(String.valueOf(stats.getCases()));
        todayCases.setText(String.valueOf(stats.getTodayCases()));
        deaths.setText(String.valueOf(stats.getDeaths()));
        todayDeaths.setText(String.valueOf(stats.getTodayDeaths()));
        recovered.setText(String.valueOf(stats.getRecovered()));
        active.setText(String.valueOf(stats.getActive()));
        critical.setText(String.valueOf(stats.getCritical()));
    }
}
