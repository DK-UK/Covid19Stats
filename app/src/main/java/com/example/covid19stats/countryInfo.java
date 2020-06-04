
package com.example.covid19stats;

import com.google.gson.annotations.SerializedName;

public class countryInfo {
    @SerializedName("flag")
    private String flag;

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getFlag() {
        return flag;
    }
}
