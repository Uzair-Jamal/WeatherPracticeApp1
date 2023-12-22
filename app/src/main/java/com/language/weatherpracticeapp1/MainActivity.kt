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
        val cityName = binding.countryName.text.toString()
        dateTime()
        getTomorrow()
        fetchWeatherData(cityName)

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
        val today = LocalDate.now()
        val tomorrow = today.plusDays(1)
        val dayAfterTomorrow = today.plusDays(2)
        val dayAfterTomorrow2 = today.plusDays(3)

        val formatter = DateTimeFormatter.ofPattern("EEEE" , Locale.getDefault())

        val formattedTomorrow = tomorrow.format(formatter)
        val formattedDayAfterTomorrow = dayAfterTomorrow.format(formatter)
        val formattedDayAfterTomorrow2 = dayAfterTomorrow2.format(formatter)

        binding.tomorrow1Day.text = formattedTomorrow.toString()
        binding.tomorrow2Day.text = formattedDayAfterTomorrow.toString()
        binding.tomorrow3Day.text = formattedDayAfterTomorrow2.toString()
    }

    private fun fetchWeatherData(cityName: String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)
        val response =
            retrofit.getWeatherData("Karachi", "e302bff06adce54a80e5318505ca1871", "metric")

        Log.d("Tag","Api Request URL: ${response.request().url()}")
        response.enqueue(object : Callback<WeatherExample> {
            @SuppressLint("SuspiciousIndentation", "SetTextI18n")
            override fun onResponse(call: Call<WeatherExample>, response: Response<WeatherExample>
            ) {
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.list.isNotEmpty()){
                        binding.tvTemp.text = "${responseBody.list[0].main.temp}°C"
                        binding.tomorrow1Date.text = "${responseBody.list[3].main.temp}°C"
                        binding.tomorrow2Date.text = "${responseBody.list[11].main.temp}°C"
                        binding.tomorrow3Date.text = "${responseBody.list[19].main.temp}°C"
                        when (responseBody.list[0].weather[0].main) {
                            "Clouds" -> {
                                binding.weatherImage.setImageResource(R.drawable.cloudy)
                            }
                            "Clear" -> {
                                binding.weatherImage.setImageResource(R.drawable.night)
                            }
                            "Hot" -> {
                                binding.weatherImage.setImageResource(R.drawable.sunny)
                            }
                            else -> {
                                binding.weatherImage.setImageResource(R.mipmap.ic_launcher_round)
                            }
                        }
                        when (responseBody.list[3].weather[0].main) {
                            "Clouds" -> {
                                binding.tomorrowIcon1.setImageResource(R.drawable.cloudy)
                            }

                            "Clear" -> {
                                binding.tomorrowIcon1.setImageResource(R.drawable.night)
                            }

                            "Hot" -> {
                                binding.tomorrowIcon1.setImageResource(R.drawable.sunny)
                            }

                            else -> {
                                binding.tomorrowIcon1.setImageResource(R.mipmap.ic_launcher_round)
                            }
                        }

                        when (responseBody.list[11].weather[0].main){
                            "Clouds" -> {
                                binding.tomorrowIcon2.setImageResource(R.drawable.cloudy)
                            }
                            "Clear" -> {
                                binding.tomorrowIcon2.setImageResource(R.drawable.night)
                            }
                            "Hot" -> {
                                binding.tomorrowIcon2.setImageResource(R.drawable.sunny)
                            }
                            else -> {
                                binding.tomorrowIcon2.setImageResource(R.mipmap.ic_launcher_round)
                            }
                        }
                        when (responseBody.list[19].weather[0].main){
                            "Clouds" -> {
                                binding.tomorrowIcon3.setImageResource(R.drawable.cloudy)
                            }
                            "Clear" -> {
                                binding.tomorrowIcon3.setImageResource(R.drawable.night)
                            }
                            "Hot" -> {
                                binding.tomorrowIcon3.setImageResource(R.drawable.sunny)
                            }
                            else -> {
                                binding.tomorrowIcon3.setImageResource(R.mipmap.ic_launcher_round)
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