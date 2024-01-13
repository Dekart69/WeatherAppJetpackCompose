package com.maks_boss.jcwapp.queries

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.maks_boss.jcwapp.data.WeatherModel
import org.json.JSONObject




fun GetWeatherByDays(response:String):List<WeatherModel>{
    if (response.isEmpty())return listOf()
    val list=ArrayList<WeatherModel>()
    val mainObject=JSONObject(response)
    val city=mainObject.getJSONObject("location").getString("name")
    val days=mainObject.getJSONObject("forecast").getJSONArray("forecastday")

    for(i in 0 until days.length()){
        val item=days[i] as JSONObject
        list.add(
            WeatherModel(
                city,
                item.getString("date"),
                "",
                item.getJSONObject("day").getJSONObject("condition").getString("text"),
                item.getJSONObject("day").getJSONObject("condition").getString("icon"),
                item.getJSONObject("day").getString("maxtemp_c"),
                item.getJSONObject("day").getString("mintemp_c"),
                item.getJSONArray("hour").toString()
            )
        )
    }
    list[0]=list[0].copy(
        time = mainObject.getJSONObject("current").getString("last_updated"),
        currentTemp = mainObject.getJSONObject("current").getString("temp_c")
    )

    return list
}