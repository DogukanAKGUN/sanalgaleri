package com.example.sanalgaleri

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_new_advertise.*
import kotlinx.android.synthetic.main.vehicle_list_card.*
import org.json.JSONObject
import kotlin.math.log

class NewAdvertiseActivity : AppCompatActivity() {



    var item = arrayOf<String>("BMW","Mercedes-Benz","Voklswagen","Audi")

    var categoryitem = arrayOf<String>("Otomobil","SUV","Motorsiklet")

    var carItem = arrayOf<String>("BMW","Mercedes-Benz","Voklswagen","Audi")

    var suvItem = arrayOf<String>("BMW","Range Rover","Voklswagen","Jeep","Volvo")

    var motorcycleItem = arrayOf<String>("BMW","Suzuki","Yamaha","Honda","Kawasaki")


    lateinit var adapterItems :ArrayAdapter<String>

    lateinit var adapterCategory :ArrayAdapter<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_advertise)

        val logindata = getSharedPreferences("login", Context.MODE_PRIVATE)
        val user_id = logindata.getInt("_id",0)

        var clickedbrand= ""

        var clickedcategory = ""


        adapterItems = ArrayAdapter<String>(this, R.layout.list_item,item)

        adapterCategory = ArrayAdapter(this,R.layout.list_item,categoryitem)

        spinner.setAdapter(adapterItems)

        spinner2.setAdapter(adapterCategory)


        fun caradapter(){
            adapterItems = ArrayAdapter<String>(this, R.layout.list_item,carItem)
            spinner.setAdapter(adapterItems)
        }
        fun suvadapter(){
            adapterItems = ArrayAdapter<String>(this, R.layout.list_item,suvItem)
            spinner.setAdapter(adapterItems)
        }
        fun motorcycleadapter(){
            adapterItems = ArrayAdapter<String>(this, R.layout.list_item,motorcycleItem)
            spinner.setAdapter(adapterItems)
        }

        //marka
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                clickedbrand = parent?.getItemAtPosition(position).toString()

            }

        }

        //Kategori
        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                clickedcategory = parent?.getItemAtPosition(position).toString()

                if (clickedcategory == "Otomobil"){
                    caradapter()
                }
                else if(clickedcategory == "SUV"){
                    suvadapter()
                }
                else{
                    motorcycleadapter()
                }
            }

        }

        fun addVehicle(){
            val url = "http://10.0.2.2:5000/vehicleadd"

            val json = JSONObject()
            json.put("title",adtitle.text.toString())
            json.put("detail",addescription.text.toString())
            json.put("modelYear",mdlyear.text.toString())
            json.put("price",vehicleprice.text.toString())
            json.put("km",vehiclekm.text.toString())
            json.put("enginePower",vehiclehp.text.toString())
            json.put("engineCapacity",vehiclecapacity.text.toString())
            json.put("color",vehiclecolor.text.toString())
            json.put("brand",clickedbrand)
            json.put("category",clickedcategory)
            json.put("created_by",user_id)


            val queue = Volley.newRequestQueue(this)
            val JsonApi: JsonObjectRequest = object : JsonObjectRequest(
                Method.POST, url, json,
                Response.Listener { response ->
                    Toast.makeText(this, "Kayıt Başarılı", Toast.LENGTH_SHORT).show()
                },
                Response.ErrorListener {
                    Toast.makeText(this, "Kayıt Başarısız", Toast.LENGTH_SHORT).show()
                }) {}
            queue.add(JsonApi)

        }

        savevehicle.setOnClickListener {
            addVehicle()
        }


    }
}