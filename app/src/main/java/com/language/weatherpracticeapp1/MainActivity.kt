package com.language.weatherpracticeapp1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.language.weatherpracticeapp1.data.WeatherExample
import com.language.weatherpracticeapp1.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private lateinit var binding: ActivityMainBinding
const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
var temperature = binding.tvTemp.toString()

//https://api.openweathermap.org/data/2.5/forecast?q=karachi&appid=e302bff06adce54a80e5318505ca1871&units=metric

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchWeatherData()

    }
}


private fun fetchWeatherData(){
    val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()
        .create(ApiInterface::class.java)
    val countryName = binding.countryName.text.toString()
    val response = retrofit.getWeatherData(countryName,"e302bff06adce54a80e5318505ca1871", "metric")

    response.enqueue(object : Callback<WeatherExample>{
        override fun onResponse(call: Call<WeatherExample>, response: Response<WeatherExample>) {
            val responseBody = response.body()!!
            temperature = responseBody.list[0].main.temp.toString()
        }

        override fun onFailure(call: Call<WeatherExample>, t: Throwable) {
            TODO("Not yet implemented")
        }

    })

}