package com.maks_boss.jcwapp.screens


import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.indication
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.maks_boss.jcwapp.R
import com.maks_boss.jcwapp.data.WeatherModel
import com.maks_boss.jcwapp.ui.theme.BlueLight
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject




@Composable
fun MainCard(currentDay:MutableState<WeatherModel>,
             onClickSync:()->Unit,
             onClickSearch:()->Unit) {
    Column(
        modifier = Modifier
            .padding(5.dp),

        ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .background(BlueLight),
            colors = CardDefaults.cardColors(
                containerColor = BlueLight
            ),
            elevation = CardDefaults.cardElevation(
                5.dp
            ),
            shape = RoundedCornerShape(10.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.padding(top = 3.dp, start = 8.dp),
                        text = currentDay.value.time,
                        style = TextStyle(fontSize = 15.sp),
                        color = Color.White
                    )
                    AsyncImage(
                        model = "https:${currentDay.value.icon}",
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .padding(top = 3.dp, end = 8.dp)
                    )

                }

                Text(
                    text=currentDay.value.city,
                    style=TextStyle(fontSize = 20.sp),
                    color=Color.White
                )
                Text(
                    text=if(currentDay.value.currentTemp.isNotEmpty())
                        currentDay.value.currentTemp.toFloat().toInt().toString()+'C'
                    else "${currentDay.value.maxTemp.toFloat().toInt().toString()}C / " +
                            "${currentDay.value.minTemp.toFloat().toInt().toString()}C",
                    style=TextStyle(fontSize = 65.sp),
                    color=Color.White
                )
                Text(
                    text=currentDay.value.condition,
                    style=TextStyle(fontSize = 16.sp),
                    color=Color.White
                )
                Row(
                    modifier=Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    IconButton(onClick = {
                        onClickSearch.invoke()

                    }) {
                        Icon(painter= painterResource(id = R.drawable.ic_search),
                        contentDescription = null,
                            tint=Color.White)


                    }

                    Text(
                        text="${currentDay.value.maxTemp.toFloat().toInt().toString()}C / " +
                                "${currentDay.value.minTemp.toFloat().toInt().toString()}C",
                        style=TextStyle(fontSize = 15.sp),
                        color=Color.White
                    )

                    IconButton(onClick = {
                        onClickSync.invoke()

                    }) {
                        Icon(painter= painterResource(id = R.drawable.ic_sync),
                            contentDescription = null,
                        tint=Color.White)


                    }

                }


            }
        }

    }


}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabLayout(daysList: MutableState<List<WeatherModel>>,currentDay:MutableState<WeatherModel>) {
    val tabList = listOf("Hours", "Days")

    var selectedTabIndex by remember {
        mutableStateOf(0)
    }
    val pagerState=rememberPagerState()
    val tabIndex=pagerState.currentPage
    val coroutineScope= rememberCoroutineScope()
    Column(
        modifier = Modifier
            .padding(start = 5.dp, end = 5.dp)
            .clip(RoundedCornerShape(5.dp))
    ) {
        TabRow(
            //selectedTabIndex = selectedTabIndex,
            selectedTabIndex = tabIndex,
            //Modifier.pagerTabIndicatorOffset(),
            containerColor = BlueLight,
            contentColor = Color.White
        ) {
            tabList.forEachIndexed { index, text ->
                Tab(
                    //selected = index == selectedTabIndex,
                    selected = false,
                    onClick = {

                    //selectedTabIndex = index
                    coroutineScope.launch{
                        pagerState.animateScrollToPage(index)
                    }

                }, text = {
                    Text(text = text)
                })

            }

        }


        HorizontalPager(
            count = tabList.size,
            state = pagerState,
            modifier = Modifier.weight(1.0f)
        ) { index ->
            val list=when(index){
                0->GetWeatherByHours(currentDay.value.hours)
                1->daysList.value
                else ->daysList.value
            }


            MainList(list, currentDay )




        }

    }
}

private fun GetWeatherByHours(hours: String): List<WeatherModel> {
    if (hours.isEmpty()) return listOf()
    val hoursArray = JSONArray(hours)

    val list = ArrayList<WeatherModel>()

    for (i in 0 until hoursArray.length()) {
        val item = hoursArray[i] as JSONObject
        list.add(
            WeatherModel(
                "",
                item.getString("time"),
                item.getString("temp_c").toFloat().toInt().toString() + "C",
                item.getJSONObject("condition").getString("text"),
                item.getJSONObject("condition").getString("icon"),
                "",
                "",
                ""
            )
        )

    }
    return list
}



