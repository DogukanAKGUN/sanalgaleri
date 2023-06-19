package com.example.sanalgaleri

import android.app.Activity
import android.app.ProgressDialog
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
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.sanalgaleri.Model.ItemMenuModel
import com.google.gson.Gson
import com.t2r2.volleyexample.FileDataPart
import com.t2r2.volleyexample.VolleyFileUploadRequest
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.activity_new_advertise.*
import kotlinx.android.synthetic.main.vehicle_list_card.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.log
import kotlin.streams.asSequence

class NewAdvertiseActivity : AppCompatActivity() {

    private var imageData: ByteArray? = null
    private var imageDataArray: ArrayList<ByteArray>? = null

    companion object {
        private const val IMAGE_PICK_CODE = 999
    }

    var item = arrayOf<String>("BMW","Mercedes-Benz","Volkswagen","Audi")

    var categoryitem = arrayOf<String>("Otomobil","SUV","Motorsiklet")

    var carItem = arrayOf<String>("BMW","Mercedes-Benz","Volkswagen","Audi")

    var suvItem = arrayOf<String>("BMW","Range Rover","Volkswagen","Jeep","Volvo")

    var motorcycleItem = arrayOf<String>("BMW","Suzuki","Yamaha","Honda","Kawasaki")

    var last_id = 0

    lateinit var adapterItems :ArrayAdapter<String>

    lateinit var adapterCategory :ArrayAdapter<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_advertise)

        val logindata = getSharedPreferences("login", Context.MODE_PRIVATE)
        val user_id = logindata.getInt("_id",0)

        var clickedbrand= ""

        var clickedcategory = ""


        var volleyRequestQueue: RequestQueue? = null
        val serverAPIURL: String = "http://10.0.2.2:5000/newadid"
        val TAG = "My Api"

        fun VerileriGetir() {
            volleyRequestQueue = Volley.newRequestQueue(this)

            val strReq: StringRequest = object : StringRequest(
                Method.GET,serverAPIURL,
                Response.Listener { response ->
                    try {
                        last_id = response.toInt()
                        Log.d("dodo","last id: $last_id")

                    } catch (e: Exception) { // caught while parsing the response
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { volleyError -> // error occurred
                    Log.e(TAG, "problem occurred, volley error: " + volleyError.message)
                }) {
            }
            // Adding request to request queue
            volleyRequestQueue?.add(strReq)
        }

        VerileriGetir()


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
        val STRING_LENGTH = 5
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        fun randomStringByJavaRandom() = ThreadLocalRandom.current()
            .ints(STRING_LENGTH.toLong(), 0, charPool.size)
            .asSequence()
            .map(charPool::get)
            .joinToString("")

        var reqCount = 0

        fun uploadImage() {
            val postURL= "http://10.0.2.2:5000/file-upload"

            val request = object : VolleyFileUploadRequest(
                Method.POST,
                postURL,
                Response.Listener {

                },
                Response.ErrorListener {
                    Log.d("dodo2", "alooooooo error: $it")

                }
            )

            {

                override fun getByteData(): MutableMap<String, FileDataPart> {
                    val params = HashMap<String, FileDataPart>()

                    params["file"] = FileDataPart(randomStringByJavaRandom() + ".png", imageData!!, "*")

                    return params
                }
            }
            Volley.newRequestQueue(this).add(request)

        }


        @Throws(IOException::class)
        fun createImageData(uri: Uri) {
            val inputStream = contentResolver.openInputStream(uri)
            inputStream?.buffered()?.use {
                imageData = it.readBytes()
                //imageDataArray?.add(it.readBytes())
                uploadImage();
                reqCount++

            }

        }

        val getResult =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()) {

                if (it.resultCode == Activity.RESULT_OK) {

                    Log.d("dodo","response is: $it")

                    val len= it.data?.clipData?.itemCount


                    for (i in 0..len!! -1){

                        val uri = it.data?.clipData?.getItemAt(i)?.uri
                        if (uri != null) {
                            //imageView.setImageURI(uri)
                            createImageData(uri)
                        }
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

        fun addVehicle(){
            val url = "http://10.0.2.2:5000/vehicleadd"

            val json = JSONObject()
            json.put("title", adtitle.text.toString())
            json.put("detail", addescription.text.toString())
            json.put("modelYear", mdlyear.text.toString())
            json.put("price", vehicleprice.text.toString())
            json.put("km", vehiclekm.text.toString())
            json.put("enginePower", vehiclehp.text.toString())
            json.put("engineCapacity", vehiclecapacity.text.toString())
            json.put("color", vehiclecolor.text.toString())
            json.put("brand", clickedbrand)
            json.put("category", clickedcategory)
            json.put("created_by", user_id)
            //json.put("last_id", last_id)

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

            //uploadImage()
            /*
            //image upload
            val postURL: String = "http://10.0.2.2:5000/file-upload"


            for (i in 0..reqCount - 1) {
                val request = object : VolleyFileUploadRequest(
                    Method.POST,
                    postURL,
                    Response.Listener {
                        Log.d("dodo", "response is: $it")
                    },
                    Response.ErrorListener {
                        println("error is: $it")
                    }
                ) {
                    override fun getByteData(): MutableMap<String, FileDataPart> {
                        val params = HashMap<String, FileDataPart>()

                        params["file"] = FileDataPart(randomStringByJavaRandom(), imageDataArray!![i], "*")

                        return params
                    }
                }
                Volley.newRequestQueue(this).add(request)
            }
            */
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