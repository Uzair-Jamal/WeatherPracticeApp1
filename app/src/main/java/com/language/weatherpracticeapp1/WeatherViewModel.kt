package com.language.weatherpracticeapp1

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.language.weatherpracticeapp1.data.WeatherExample
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherViewModel : ViewModel() {
    val cityName = MutableLiveData<String>()
    val weatherData = MutableLiveData<WeatherExample>()


    fun getWeatherData(cityName: String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)

        val response = retrofit.getWeatherData(
            cityName,
            "e302bff06adce54a80e5318505ca1871",
            "metric"
        )
        Log.d("Tag", "Api Request URL: ${response.request().url()}")
        response.enqueue(object : Callback<WeatherExample> {
            @SuppressLint("SuspiciousIndentation", "SetTextI18n")
            override fun onResponse(
                call: Call<WeatherExample>,
                response: Response<WeatherExample>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.list.isNotEmpty()) {
                        weatherData.value = responseBody
                    } else {
                        Log.e("Tag", "Response body is null or empty")
                    }
                } else {
                    Log.e("Tag", "Response not successful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<WeatherExample>, t: Throwable) {
                Log.e("Log", t.toString())
            }
        })
    }
    }
