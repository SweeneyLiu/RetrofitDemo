package com.lsw.demo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lsw.demo.R;
import com.lsw.demo.api.WeatherApi;
import com.lsw.demo.model.WeatherEntity;

import java.io.IOException;
import java.lang.reflect.Method;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherActivity extends AppCompatActivity {

    private Button getScore;
    private TextView result;
    private static final String TAG = "WeatherActivity";
    public static final String WEATHER_URL = "https://free-api.heweather.com/x3/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initView();
        getScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestData();
            }
        });
    }

    private void requestData() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WEATHER_URL)
                .addConverterFactory(GsonConverterFactory.create())//添加 json 转换器
                .build();
        WeatherApi weatherApi = retrofit.create(WeatherApi.class);
        Call<WeatherEntity> call = weatherApi.getWeather("北京","99df2f4473bc40fb9990d2317e07c6ae");
        //同步
/*        try {
            Response<WeatherEntity> response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        //异步
        call.enqueue(new Callback<WeatherEntity>() {
            @Override
            public void onResponse(Call<WeatherEntity> call, Response<WeatherEntity> response) {
                result.setText(response.body().getHeWeather().get(0).getNow().getCond().getTxt());
            }

            @Override
            public void onFailure(Call<WeatherEntity> call, Throwable t) {

            }
        });
    }

    private void initView() {
        getScore = (Button) findViewById(R.id.getScore);
        result = (TextView) findViewById(R.id.result);
    }
}
