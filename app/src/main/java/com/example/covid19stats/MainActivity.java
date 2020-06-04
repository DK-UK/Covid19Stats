package com.example.covid19stats;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private PieChart pieChart;
    SimpleArcLoader simpleArcLoader;
    TextView cases, todayCases,deaths,
    todayDeaths,
    recovered,
    active,
    critical;
    ScrollView scrollView;
    private Button trackCountry;

    public static final String TAG = "Main";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        simpleArcLoader = findViewById(R.id.loader);
        pieChart = findViewById(R.id.pie_Chart);
        cases = findViewById(R.id.total_Cases);
        todayCases = findViewById(R.id.today_Cases);
        deaths = findViewById(R.id.deaths);
        todayDeaths = findViewById(R.id.today_deaths);
        recovered = findViewById(R.id.recovered);
        active = findViewById(R.id.active);
        critical = findViewById(R.id.critical);
        scrollView = findViewById(R.id.scrollStats);
        trackCountry = findViewById(R.id.btn);

        getGlobalStats();

        trackCountry.setOnClickListener(this);
    }

    private void getGlobalStats() {
        simpleArcLoader.start();
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://corona.lmao.ninja/v2/")
                .build();


        getStats getstats = retrofit.create(getStats.class);

        Call<Stats> call = getstats.getGlobalStats();


        call.enqueue(new Callback<Stats>() {
            @Override
            public void onResponse(Call<Stats> call, Response<Stats> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Code : "+response.code(),Toast.LENGTH_LONG).show();
                    return;
                }

                Stats stat = response.body();

                cases.setText(String.valueOf(stat.getCases()));
                todayCases.setText(String.valueOf(stat.getTodayCases()));
                deaths.setText(String.valueOf(stat.getDeaths()));
                todayDeaths.setText(String.valueOf(stat.getTodayDeaths()));
                recovered.setText(String.valueOf(stat.getRecovered()));
                active.setText(String.valueOf(stat.getActive()));
                critical.setText(String.valueOf(stat.getCritical()));

                pieChart.addPieSlice(new PieModel("Cases",Integer.parseInt(cases.getText().toString()),Color.parseColor("#F64F1A")));
                pieChart.addPieSlice(new PieModel("Active",Integer.parseInt(active.getText().toString()),Color.parseColor("#517DF8")));
                pieChart.addPieSlice(new PieModel("Recovered",Integer.parseInt(recovered.getText().toString()),Color.parseColor("#03B14B")));
                pieChart.addPieSlice(new PieModel("Deaths",Integer.parseInt(deaths.getText().toString()),Color.parseColor("#EF1414")));
                pieChart.startAnimation();

                simpleArcLoader.stop();
                simpleArcLoader.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<Stats> call, Throwable t) {
                simpleArcLoader.stop();
                simpleArcLoader.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                Log.e(TAG, "onFailure: "+t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn){
            startActivity(new Intent(getApplicationContext(),countries.class));
        }
    }
}
