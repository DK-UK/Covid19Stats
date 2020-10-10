package com.example.covid19stats;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leo.simplearcloader.SimpleArcLoader;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class recyclerViewFrag extends Fragment {

    RecyclerView recyclerView;
    countryAdapter adapter;
   public static ArrayList<countryDetails> countryInfoArrayList;
    SimpleArcLoader simpleArcLoader;
    public static final String TAG = "Main";
    static EditText searchBar;
    public recyclerViewFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.e(TAG, "ON CREATE VIEW");
        View view =  inflater.inflate(R.layout.fragment_recycler_view, container, false);
        setHasOptionsMenu(true);
        simpleArcLoader = view.findViewById(R.id.loader);
        searchBar = view.findViewById(R.id.searchCountry);
        recyclerView = view.findViewById(R.id.recyclerView);

        countryInfoArrayList = new ArrayList<>();

        getCountryInfo();

        adapter = new countryAdapter(getActivity(),countryInfoArrayList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }




    private void getCountryInfo() {
        try {
            simpleArcLoader.start();

            countryInfoArrayList.clear();

            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://corona.lmao.ninja/v2/")
                    .build();

            getStats getStat = retrofit.create(getStats.class);

            Call<List<countryDetails>> call = getStat.getCountryInfo();

            call.enqueue(new Callback<List<countryDetails>>() {
                @Override
                public void onResponse(Call<List<countryDetails>> call, Response<List<countryDetails>> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(getActivity(), "Code : " + response.code(), Toast.LENGTH_LONG).show();
                        return;
                    }

                    simpleArcLoader.stop();
                    simpleArcLoader.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    List<countryDetails> countryInfoList = response.body();


                    for (countryDetails countryInfo : countryInfoList) {
                        countryInfo info = countryInfo.getCountryInfo();
                        Log.e(TAG, "flag : " + info.getFlag() + " Name : " + countryInfo.getCountry());
                        countryInfoArrayList.add(new countryDetails(info.getFlag(), countryInfo.getCountry()));
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<List<countryDetails>> call, Throwable t) {
                    Log.e(TAG, "onFailure: " + t.getMessage());
                    simpleArcLoader.stop();
                    simpleArcLoader.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            });

        }
        catch(Exception e){
            Log.e(TAG, "Exception : "+ e.toString());
        }
    }
}
