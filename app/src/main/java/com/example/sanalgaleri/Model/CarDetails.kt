package com.example.sanalgaleri.Model

import com.google.gson.JsonArray
import org.json.JSONObject


data class CarDetails(
    val _id:String,//ilan id
    //val title:String,//ilan başlığı
    val images: ArrayList<String>,//Carousel için resimler
    //val detail:String,//açıklaması
    //val modelYear:String,//model yılı
    //val price:String,//Fiyat
    //val brand:String,//marka
    //val model:String,//MODEL
    //val km:String,//KM
    //val enginePower:String,//Motor gücü (HP)
    //val engineCapacity:String,//Motor Hacmi
    //val color:String //Renk
)
