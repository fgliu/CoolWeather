package com.fgliu.coolweather.util;

import android.text.TextUtils;

import com.fgliu.coolweather.model.City;
import com.fgliu.coolweather.model.CoolWeatherDB;
import com.fgliu.coolweather.model.Country;
import com.fgliu.coolweather.model.Province;

import org.w3c.dom.Text;

/**
 * Created by andy on 16/6/26.
 */
public class Utility {

    public synchronized static boolean handleProvinceResponse(CoolWeatherDB coolWeatherDB,String response){
        if(!TextUtils.isEmpty(response)){
            String[] allProvinces = response.split(",");
            if(allProvinces != null && allProvinces.length>0){
                for (String p : allProvinces){
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);

                    coolWeatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    public static boolean handlerCitiesResponse(CoolWeatherDB coolWeatherDB,String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            String[] allCities = response.split(",");
            if(allCities != null && allCities.length>0){
                for (String c : allCities){
                    String[] array = c.split("\\|");
                    City city = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);

                    coolWeatherDB.saveCity(city);
                }
                return  true;
            }

        }
        return false;
    }

    public static boolean handlerCountiesResponse(CoolWeatherDB coolWeatherDB,String response ,int cityId){
        if(!TextUtils.isEmpty(response)){
            String[] allCounties = response.split(",");
            if(allCounties != null && allCounties.length>0){
                for (String c:allCounties){
                    String[] array = c.split("\\|");
                    Country country = new Country();
                    country.setCountryCode(array[0]);
                    country.setCountryName(array[1]);
                    country.setCity_id(cityId);

                    coolWeatherDB.saveCountry(country);
                }
                return true;
            }
        }
        return  false;
    }

}
