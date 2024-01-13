package com.maks_boss.jcwapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.maks_boss.jcwapp.data.WeatherModel
import com.maks_boss.jcwapp.queries.GetWeatherByDays
import com.maks_boss.jcwapp.screens.DialogSearch

import com.maks_boss.jcwapp.screens.MainCard
import com.maks_boss.jcwapp.screens.TabLayout


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val daysList=remember{
                mutableStateOf(listOf<WeatherModel>())
            }
            val currentDay=remember{
                mutableStateOf(WeatherModel(
                    "","","1.0","","","1.0","1.0","",))
            }
            val dialogState=remember{
                mutableStateOf(false)
            }
            if(dialogState.value){
                DialogSearch(dialogState, onSubmit = {
                    getData(it,this,daysList,currentDay )
                })
            }


            getData("Lviv",this,daysList,currentDay)
            Image(
                painter = painterResource(id = R.drawable.weath),
                contentDescription = "im1",
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.85f),
                contentScale = ContentScale.FillBounds
            )
            Column {
                MainCard(currentDay,onClickSync={
                    getData("Lviv",this@MainActivity,daysList,currentDay)

                },
                onClickSearch = {
                    dialogState.value=true


                })
                TabLayout(daysList,currentDay)
            }


        }

    }
}


val API_KEY="21c2065d21a8450186a182710233110"
fun getData(city:String,context: Context,
            daysList: MutableState<List<WeatherModel>>,
            currentDay:MutableState<WeatherModel>){
    val url="https://api.weatherapi.com/v1/forecast.json?" +
            "key=$API_KEY" +
            "&q=$city" +
            "&days=3" +
            "&aqi=no" +
            "&alerts=no"
    val queue= Volley.newRequestQueue(context)
    val sRequest= StringRequest(
        Request.Method.GET,
        url,
        {response->
            val list= GetWeatherByDays(response)
            daysList.value=list
            currentDay.value=list[0]
        },
        {error->
            Log.d("MyLog","VolleyError: $error")
        }
    )
    queue.add(sRequest)

}


