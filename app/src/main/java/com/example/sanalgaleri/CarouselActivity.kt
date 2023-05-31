package com.example.sanalgaleri

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.sanalgaleri.Model.CarDetails
import com.google.gson.Gson
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.activity_carousel.*
import org.json.JSONObject


class CarouselActivity : AppCompatActivity() {

    //lateinit var imageUrl: ArrayList<String>

    lateinit var sliderView: SliderView

    lateinit var sliderAdapter: SliderAdapter

    var ad_id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carousel)

        val logindata = getSharedPreferences("login", Context.MODE_PRIVATE)
        val user_id = logindata.getInt("_id",0)

        sliderView = findViewById(R.id.slider)

        val extras = intent.extras
        if (extras != null) {
            ad_id = extras.getInt("ad_id")

        }

        val carDetail = ArrayList<CarDetails>()


        var volleyRequestQueue: RequestQueue? = null
        var dialog: ProgressDialog? = null
        val url = "http://10.0.2.2:5000/cardetails"
        val TAG = "My Api"

        fun GetItemById(id: Int){
            val send = JSONObject()
            send.put("_id", id)

            volleyRequestQueue = Volley.newRequestQueue(this)
            val JsonApi: JsonObjectRequest = object : JsonObjectRequest(
                Method.POST,url,send,
                Response.Listener { response ->

                        Log.e(TAG, "response: " + response)
                        try {
                            val responseObj = response
                            val gson = Gson()
                            val sItems = responseObj.getJSONArray("res")
                            //val sItem = gson.fromJson(sItems.toString(), CarDetails::class.java)

                            for (i in 0..sItems.length()-1) {
                                val sItem: CarDetails =
                                    gson.fromJson(sItems.get(i).toString(), CarDetails::class.java)
                                carDetail.add(sItem)
                            }

                            sliderAdapter = SliderAdapter(carDetail)
                            sliderView.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
                            sliderView.setSliderAdapter(sliderAdapter)
                            sliderView.scrollTimeInSec =10
                            sliderView.isAutoCycle = true
                            sliderView.startAutoCycle()

                            Title.text = carDetail[0].title
                            Detail.text = carDetail[0].detail
                            ModelYear.text = carDetail[0].modelYear
                            Price.text = carDetail[0].price
                            Brand.text = carDetail[0].brand
                            Modal.text = carDetail[0].model
                            KM.text = carDetail[0].km
                            EnginePower.text = carDetail[0].enginePower
                            EngineCapacity.text = carDetail[0].engineCapacity
                            Color.text = carDetail[0].color

                            dialog?.dismiss()
                        } catch (e: Exception) { // caught while parsing the response
                            Log.e(TAG, "problem occurred"+ e )
                            e.printStackTrace()
                    }
                },
                Response.ErrorListener{
                    Toast.makeText(this,"Bulunamadı",Toast.LENGTH_SHORT).show()
                }) {}
            volleyRequestQueue?.add(JsonApi)

        }
        GetItemById(ad_id)


        val favurl = "http://10.0.2.2:5000/addtofavorites"
        val FavTAG = "My Api"

        fun AddToFav(id: Int){
            val send = JSONObject()
            send.put("ad_id", id)
            send.put("user_id",user_id)
            volleyRequestQueue = Volley.newRequestQueue(this)
            val FavJsonApi: JsonObjectRequest = object : JsonObjectRequest(
                Method.POST,favurl,send,
                Response.Listener { response ->

                    Log.e(FavTAG, "response: " + response)
                    try {
                        val responseObj = response
                        val gson = Gson()
                        val sItems = responseObj.getJSONArray("res")
                        //val sItem = gson.fromJson(sItems.toString(), CarDetails::class.java)
                        Toast.makeText(this, "Sonuç:"+response, Toast.LENGTH_SHORT).show()



                        dialog?.dismiss()
                    } catch (e: Exception) { // caught while parsing the response
                        Log.e(FavTAG, "problem occurred"+ e )
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener{

                }) {}
            volleyRequestQueue?.add(FavJsonApi)

        }

        addtofav.setOnClickListener{
            AddToFav(ad_id)
        }



    }

}