package com.fgliu.coolweather.util;

import android.text.TextUtils;

import com.fgliu.coolweather.model.City;
import com.fgliu.coolweather.model.CoolWeatherDB;
import com.fgliu.coolweather.model.Country;
import com.fgliu.coolweather.model.Province;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by andy on 16/6/26.
 */
public class Utility {

    public synchronized static boolean handleProvinceResponse(CoolWeatherDB coolWeatherDB,String response){
        if(!TextUtils.isEmpty(response)){
            Gson gson = new Gson();
            Map<String,String> allProvinces = gson.fromJson(response, new TypeToken<Map<String, String>>() {
            }.getType());
            Iterator<Map.Entry<String, String>> iter = allProvinces.entrySet().iterator();
            Map.Entry<String, String> entry;
            while (iter.hasNext()) {
                entry = iter.next();
                Province province = new Province();
                province.setProvinceCode(entry.getKey());
                province.setProvinceName(entry.getValue());
                coolWeatherDB.saveProvince(province);
            }
            return true;
//            String[] allProvinces = response.split(",");
//            if(allProvinces != null && allProvinces.length>0){
//                for (String p : allProvinces){
//                    String[] array = p.split("\\|");
//                    Province province = new Province();
//                    province.setProvinceCode(array[0]);
//                    province.setProvinceName(array[1]);
//
//                    coolWeatherDB.saveProvince(province);
//                }
//                return true;
//            }
        }
        return false;
    }

    public static boolean handlerCitiesResponse(CoolWeatherDB coolWeatherDB,String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            Gson gson = new Gson();
            Map<String,String> allCities = gson.fromJson(response, new TypeToken<Map<String, String>>() {
            }.getType());
            Iterator<Map.Entry<String, String>> iter = allCities.entrySet().iterator();
            Map.Entry<String, String> entry;
            while (iter.hasNext()) {
                entry = iter.next();
                City city = new City();
                city.setCityCode(entry.getKey());
                city.setCityName(entry.getValue());
                city.setProvinceId(provinceId);
                coolWeatherDB.saveCity(city);
            }
            return true;

//            String[] allCities = response.split(",");
//            if(allCities != null && allCities.length>0){
//                for (String c : allCities){
//                    String[] array = c.split("\\|");
//                    City city = new City();
//                    city.setCityCode(array[0]);
//                    city.setCityName(array[1]);
//                    city.setProvinceId(provinceId);
//
//                    coolWeatherDB.saveCity(city);
//                }
//                return  true;
//            }

        }
        return false;
    }

    public static boolean handlerCountiesResponse(CoolWeatherDB coolWeatherDB,String response ,int cityId){
        if(!TextUtils.isEmpty(response)){
            Gson gson = new Gson();
            Map<String,String> allCities = gson.fromJson(response, new TypeToken<Map<String, String>>() {
            }.getType());
            Iterator<Map.Entry<String, String>> iter = allCities.entrySet().iterator();
            Map.Entry<String, String> entry;
            while (iter.hasNext()) {
                entry = iter.next();
                Country country = new Country();
                country.setCountryCode(entry.getKey());
                country.setCountryName(entry.getValue());
                country.setCity_id(cityId);

                coolWeatherDB.saveCountry(country);
            }
            return true;

//            String[] allCounties = response.split(",");
//            if(allCounties != null && allCounties.length>0){
//                for (String c:allCounties){
//                    String[] array = c.split("\\|");
//                    Country country = new Country();
//                    country.setCountryCode(array[0]);
//                    country.setCountryName(array[1]);
//                    country.setCity_id(cityId);
//
//                    coolWeatherDB.saveCountry(country);
//                }
//                return true;
//            }
        }
        return  false;
    }

}
