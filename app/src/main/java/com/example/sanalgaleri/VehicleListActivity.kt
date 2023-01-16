package com.example.sanalgaleri

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.sanalgaleri.Model.VehicleList
import com.example.sanalgaleri.Model.suvBrandModel
import com.google.gson.Gson

import kotlinx.android.synthetic.main.activity_suv_brand.*
import kotlinx.android.synthetic.main.activity_vehicle_list.*
import org.json.JSONObject

class VehicleListActivity : AppCompatActivity() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    var brand = ""
    var type = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_list)

        val VehicleListe = ArrayList<VehicleList>()

        val extras = intent.extras
        if (extras != null) {
            brand = extras.getString("brand").toString()
            type = extras.getString("type").toString()
        }

        layoutManager = LinearLayoutManager(this)
        VehicleListRecyclerView.layoutManager = layoutManager

        var volleyRequestQueue: RequestQueue? = null
        var dialog: ProgressDialog? = null
        val serverAPIURL: String = "http://10.0.2.2:5000/vehiclelist"
        val TAG = "My Api"

        fun VerileriGetir(brandName:String,type:String) {
            volleyRequestQueue = Volley.newRequestQueue(this)
            dialog = ProgressDialog.show(this, "", "Lütfen bekleyin...", true);
            val parameters: MutableMap<String, String> = HashMap()

            val json = JSONObject()
            json.put("brand",brandName)
            json.put("type",type)
            val strReq: JsonObjectRequest = object : JsonObjectRequest(
                Method.POST,serverAPIURL,json,
                Response.Listener { response ->
                    Log.e(TAG, "response: " + response)
                    try {
                        val responseObj = response
                        val gson = Gson()

                        val sItems = responseObj.getJSONArray("res")

                        for (i in 0..sItems.length()-1) {
                            var sItem: VehicleList =
                                gson.fromJson(sItems.get(i).toString(), VehicleList::class.java)
                            VehicleListe.add(sItem)

                        }
                        var adapter = VehicleListRecyclerAdapter(VehicleListe)

                        VehicleListRecyclerView.adapter = adapter

                        dialog?.dismiss()
                        adapter.setOnItemCLickListener(object :
                            VehicleListRecyclerAdapter.onItemCLickListener {
                            override fun onItemClick(position: Int) {

                                val intent = Intent(this@VehicleListActivity, CarouselActivity::class.java)
                                intent.putExtra("ad_id",VehicleListe.get(position).ad_id)
                                startActivity(intent)

                            }

                        })
                    } catch (e: Exception) { // caught while parsing the response
                        Log.e(TAG, "problem occurred"+ e )
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { volleyError -> // error occurred
                    Log.e(TAG, "problem occurred, volley error: " + volleyError.message)
                }) {

                override fun getParams(): MutableMap<String, String> {
                    return parameters;
                }

                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {

                    val headers: MutableMap<String, String> = HashMap()
                    // Add your Header paramters here
                    return headers
                }
            }
            // Adding request to request queue
            volleyRequestQueue?.add(strReq)
        }
        VerileriGetir(brand,type)
    }
}