package com.example.sanalgaleri

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.t2r2.volleyexample.FileDataPart
import com.t2r2.volleyexample.VolleyFileUploadRequest
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_new_advertise.*
import kotlinx.android.synthetic.main.vehicle_list_card.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import kotlin.math.log

class NewAdvertiseActivity : AppCompatActivity() {

    private var imageData: ByteArray? = null


    companion object {
        private const val IMAGE_PICK_CODE = 999
    }

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

        @Throws(IOException::class)
        fun createImageData(uri: Uri) {
            val inputStream = contentResolver.openInputStream(uri)
            inputStream?.buffered()?.use {
                imageData = it.readBytes()
            }
        }

         val getResult =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()) {

                    if (it.resultCode == Activity.RESULT_OK) {
                        val uri = it.data?.data

                        if (uri != null) {
                            imageView.setImageURI(uri)
                            createImageData(uri)
                        }
                    }

            }

        fun launchGallery() {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            // Caller
            getResult.launch(intent)
            // Receiver

        }

        



        fun uploadImage() {
            val postURL: String = "http://10.0.2.2:5000/file-upload"

            val request = object : VolleyFileUploadRequest(
                Method.POST,
                postURL,
                Response.Listener {
                    Log.d("dodo","response is: $it")
                },
                Response.ErrorListener {
                    println("error is: $it")
                }
            ) {
                override fun getByteData(): MutableMap<String, FileDataPart> {
                    val params = HashMap<String, FileDataPart>()
                    params["file"] = FileDataPart("image.png", imageData!!, "*")
                    return params
                }
            }
            Volley.newRequestQueue(this).add(request)
        }



        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
                val uri = data?.data
                if (uri != null) {
                    imageView.setImageURI(uri)
                    createImageData(uri)
                }
            }
            super.onActivityResult(requestCode, resultCode, data)
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

        imageButton.setOnClickListener {
            launchGallery()
        }

        sendButton.setOnClickListener {
            uploadImage()
        }

    }
}