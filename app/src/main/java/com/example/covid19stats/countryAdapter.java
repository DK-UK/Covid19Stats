package com.example.covid19stats;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class countryAdapter extends RecyclerView.Adapter<countryAdapter.viewHolder> implements Filterable {
    private ArrayList<countryDetails> countryInfoArrayList;
    private ArrayList<countryDetails> countryAdapterArrayListFull;

    public static final String TAG = "Main";
    Context context;

    public countryAdapter(Context context, ArrayList<countryDetails> countryInfoArrayList){
        {

            this.context = context;
            this.countryInfoArrayList = countryInfoArrayList;
            countryAdapterArrayListFull = countryInfoArrayList;

        }
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint == null || constraint.length() == 0){
                filterResults.count = countryInfoArrayList.size();
                filterResults.values = countryInfoArrayList;
            }
            else{
                List<countryDetails> details = new ArrayList<>();
                String searchStr = constraint.toString().toLowerCase();

                for (countryDetails details1 : countryInfoArrayList){
                    if (details1.getCountry().toLowerCase().contains(searchStr)){
                        details.add(details1);
                    }
                    filterResults.count = details.size();
                    filterResults.values = details;
                }
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            countryAdapterArrayListFull = (ArrayList<countryDetails>) results.values;
            recyclerViewFrag.countryInfoArrayList = (ArrayList<countryDetails>) results.values;
            notifyDataSetChanged();
        }
    };

    public static class viewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        LinearLayout linearLayout;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.country_flag);
            textView = itemView.findViewById(R.id.country_name);
            linearLayout = itemView.findViewById(R.id.item_layout);

        }
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.countries_info,parent,false);
        viewHolder viewHolder = new viewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        try {
            Log.e(TAG, "onBindViewHolder: " + context.toString());
            Log.e(TAG, "onBindViewHolder: " + holder.imageView.toString());
        }
        catch(NullPointerException e){
            Log.e(TAG, "onBindViewHolder: "+e.getMessage());
        }
        Log.e(TAG, "onBindViewHolder: "+countryAdapterArrayListFull.size());
        Log.e(TAG, "onBindViewHolder: "+countryAdapterArrayListFull.get(position).getCountry());
        countryDetails countryInfo = countryAdapterArrayListFull.get(position);

        Glide.with(context).load(countryInfo.get_Flag()).into(holder.imageView);
        holder.textView.setText(countryInfo.getCountry());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countryStats stats = new countryStats();
                FragmentTransaction ft = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
                stats.onActivityResult(0, Activity.RESULT_OK,new Intent().putExtra("country_name",countryInfo.getCountry()));
                ft.replace(R.id.fragment,stats);
                ft.addToBackStack(null);
                ft.commit();
            }
        });


    }

    @Override
    public int getItemCount() {
        return countryAdapterArrayListFull.size();
    }

    @Override
    public long getItemId(int position) {
        return  position;
    }
}
