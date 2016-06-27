package com.fgliu.coolweather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fgliu.coolweather.R;
import com.fgliu.coolweather.model.City;
import com.fgliu.coolweather.model.CoolWeatherDB;
import com.fgliu.coolweather.model.Country;
import com.fgliu.coolweather.model.Province;
import com.fgliu.coolweather.util.HttpCallBackListener;
import com.fgliu.coolweather.util.HttpUtil;
import com.fgliu.coolweather.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andy on 16/6/26.
 */
public class ChooseAreaActivity extends Activity{

    public static final int LEVEL_PROVINCE =0;
    public static final int LEVEL_CITY =1;
    public static final int LEVEL_COUNTRY =2;

    private ProgressDialog progressDialog;
    private TextView titleText;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private CoolWeatherDB coolWeatherDB;
    private List<String> dataList = new ArrayList<String>();

    private List<Province> provinceList;

    private List<City> cityList;

    private List<Country> countryList;

    private Province selectedProvince;

    private City selectedCity;

    private int currentLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);
        listView = (ListView) findViewById(R.id.list_view);
        titleText = (TextView) findViewById(R.id.title_text);

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);

        coolWeatherDB = CoolWeatherDB.getInstance(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentLevel ==LEVEL_PROVINCE){
                    selectedProvince = provinceList.get(position);
                    queryCities();
                }
                else if(currentLevel == LEVEL_CITY){
                    selectedCity = cityList.get(position);
                    queryCounties();
                }
            }
        });
        queryProvinces();
    }

    private void queryProvinces(){
        provinceList = coolWeatherDB.loadProvinces();
        if(provinceList.size()>0){
            dataList.clear();
            for (Province province : provinceList){
                dataList.add(province.getProvinceName());
            }

            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText("中国");
            currentLevel = LEVEL_PROVINCE;
        }else{
            queryFromServer(null,LEVEL_PROVINCE);
        }


    }

    private void queryCities(){

        cityList = coolWeatherDB.loadCities(selectedProvince.getId());
        if(cityList.size()>0){
            dataList.clear();
            for(City city : cityList){
                dataList.add(city.getCityName());
            }

            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedProvince.getProvinceName());
            currentLevel = LEVEL_CITY;
        }else {
            queryFromServer(selectedProvince.getProvinceCode(),LEVEL_CITY);
        }

    }

    private void queryCounties(){

        countryList = coolWeatherDB.loadCounties(selectedCity.getId());
        if(countryList.size()>0){
            dataList.clear();
            for (Country country : countryList){
                dataList.add(country.getCountryName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedCity.getCityName());
            currentLevel =LEVEL_COUNTRY;
        }else {
            queryFromServer(selectedCity.getCityCode(),LEVEL_COUNTRY);
        }
    }

    private void queryFromServer(final String code, final int type) {
        String address = null;
        switch (type) {
            case LEVEL_PROVINCE:
                //address = "http://www.weather.com.cn/data/city3jdata/china.html";
                address = "http://fj.weather.com.cn/data/city3jdata/china.html";
                break;
            case LEVEL_CITY:
                address = "http://fj.weather.com.cn/data/city3jdata/provshi/" + code + ".html";
                break;
            case LEVEL_COUNTRY:
                address = "http://fj.weather.com.cn/data/city3jdata/station/" + code + ".html";
                break;
        }
        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                boolean result = false;
                if(LEVEL_PROVINCE == type){
                    result = Utility.handleProvinceResponse(coolWeatherDB,response);
                }else if(LEVEL_CITY == type){
                    result = Utility.handlerCitiesResponse(coolWeatherDB,response,selectedProvince.getId());
                }else if(LEVEL_COUNTRY == type){
                    result = Utility.handlerCountiesResponse(coolWeatherDB,response,selectedCity.getId());
                }
                if(result){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            switch (type) {
                                case LEVEL_PROVINCE:
                                    queryProvinces();
                                    break;
                                case LEVEL_CITY:
                                    queryCities();
                                    break;
                                case LEVEL_COUNTRY:
                                    queryCounties();
                                    break;
                            }

                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ChooseAreaActivity.this,"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    private void showProgressDialog(){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("loading......");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog(){
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if(currentLevel == LEVEL_COUNTRY) {
            queryCities();
        }else if(currentLevel == LEVEL_CITY){
            queryProvinces();
        }else{
            finish();
        }

    }
}
