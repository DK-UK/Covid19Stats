package com.example.covid19stats;

import com.google.gson.annotations.SerializedName;

public class countryDetails {

    @SerializedName("country")
    private String country;

    @SerializedName("countryInfo")
    public countryInfo countryInfo;

    public String getCountry() {
        return country;
    }

    public com.example.covid19stats.countryInfo getCountryInfo() {
        return countryInfo;
    }

    private String flag;
    public countryDetails(String flag,String country) {
        this.country = country;
        this.flag = flag;
    }

    public String get_Flag(){
        return flag;
    }


}
