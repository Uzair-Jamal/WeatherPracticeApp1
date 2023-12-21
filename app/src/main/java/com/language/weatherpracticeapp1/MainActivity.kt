package com.language.weatherpracticeapp1

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.language.weatherpracticeapp1.data.WeatherExample
import com.language.weatherpracticeapp1.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Calendar
import java.util.Locale

@SuppressLint("StaticFieldLeak")
const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

//https://api.openweathermap.org/data/2.5/forecast?q=karachi&appid=e302bff06adce54a80e5318505ca1871&units=metric

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dateTime()
        fetchWeatherData()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun dateTime(){
        val dateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        val formattedDate = dateTime.format(formatter)
        binding.dateTime.text = formattedDate.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTomorrow(){
        val tomorrow = LocalDate.now().plusDays(1)
        val dayAfterTomorrow = LocalDate.now().plusDays(2)
        val dayAfterTomorrow2 = LocalDate.now().plusDays(3)

        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
            .withLocale(Locale.getDefault())

        val formattedTomorrow = tomorrow.format(formatter)
        val formattedDayAfterTomorrow = dayAfterTomorrow.format(formatter)
        val formattedDayAfterTomorrow2 = dayAfterTomorrow2.format(formatter)

        binding.tomorrow1Date.text = formattedTomorrow.toString()
        binding.tomorrow2Date.text = formattedDayAfterTomorrow.toString()
        binding.tomorrow3Date.text = formattedDayAfterTomorrow2.toString()
    }

    private fun fetchWeatherData() {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)
 //       val countryName = binding.countryName.text.toString()
        val response =
            retrofit.getWeatherData("Karachi", "e302bff06adce54a80e5318505ca1871", "metric")

        Log.d("Tag","Api Request URL: ${response.request().url()}")
        response.enqueue(object : Callback<WeatherExample> {
            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(call: Call<WeatherExample>, response: Response<WeatherExample>
            ) {
                if (response.isSuccessful){
                val responseBody = response.body()
                    if (responseBody != null && responseBody.list.isNotEmpty()){
                        binding.tvTemp.text = responseBody.list[0].main.temp.toString()
                        when (responseBody.list[0].weather[0].main) {
                            "Clouds" -> {
                                binding.weatherImage.setImageResource(R.drawable.cloudy)
                            }
                            "Clear" -> {
                                binding.weatherImage.setImageResource(R.drawable.night)
                            }
                            else -> {
                                binding.weatherImage.setImageResource(R.mipmap.ic_launcher_round)
                            }
                        }
                    }
                    else{
                        Log.e("Tag","Response body is null or empty")
                    }
                }
                    else{
                        Log.e("Tag","Response not successful: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<WeatherExample>, t: Throwable) {
                Log.e("Log",t.toString())
            }
        })
    }
}