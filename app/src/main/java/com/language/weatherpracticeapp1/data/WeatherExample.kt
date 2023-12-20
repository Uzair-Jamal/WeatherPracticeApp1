package com.language.weatherpracticeapp1.data

data class WeatherExample(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<DT>,
    val message: Int
)