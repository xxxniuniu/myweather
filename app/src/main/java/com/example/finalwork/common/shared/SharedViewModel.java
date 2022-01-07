package com.example.finalwork.common.shared;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<String> whichCity;
    private MutableLiveData<String> whichCityName;
    private MutableLiveData<String> nowFocusCity;
    private MutableLiveData<String> allFocusCity;
    private MutableLiveData<Double> lat;
    private MutableLiveData<Double> lon;
    private MutableLiveData<String> username;
    public void setWhichCity(String city){
        whichCity.setValue(city);
    }

    public void setLat(MutableLiveData<Double> lat) {
        this.lat = lat;
    }

    public void setLon(MutableLiveData<Double> lon) {
        this.lon = lon;
    }

    public void setWhichCityName(MutableLiveData<String> whichCityName) {
        this.whichCityName = whichCityName;
    }

    public void setNowFocusCity(MutableLiveData<String> nowFocusCity) {
        this.nowFocusCity = nowFocusCity;
    }

    public void setAllFocusCity(MutableLiveData<String> allFocusCity) {
        this.allFocusCity = allFocusCity;
    }

    public void setUsername(MutableLiveData<String> username) {
        this.username = username;
    }

    public MutableLiveData<String> getWhichCityName() {
        if (whichCityName == null){
            whichCityName = new MutableLiveData<String>();
        }
        return whichCityName;
    }

    public MutableLiveData<String> getWhichCity(){
        if (whichCity == null){
            whichCity = new MutableLiveData<String>();
        }
        return whichCity;
    }

    public MutableLiveData<Double> getLat(){
        if (lat == null){
            lat = new MutableLiveData<Double>();
        }
        return lat;
    }

    public MutableLiveData<Double> getLon(){
        if (lon == null){
            lon = new MutableLiveData<Double>();
        }
        return lon;
    }

    public MutableLiveData<String> getUsername() {
        if (username == null){
            username = new MutableLiveData<String>();
        }
        return username;
    }

    public MutableLiveData<String> getNowFocusCity() {
        if (nowFocusCity == null){
            nowFocusCity = new MutableLiveData<String>();
        }
        return nowFocusCity;
    }

    public MutableLiveData<String> getAllFocusCity() {
        if (allFocusCity == null){
            allFocusCity = new MutableLiveData<String>();
        }
        return allFocusCity;
    }
}
