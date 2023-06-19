package com.example.sanalgaleri

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.sanalgaleri.Model.User
import com.example.sanalgaleri.Model.VehicleList
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_favorites.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_vehicle_list.*
import org.json.JSONObject

class ProfileActivity : AppCompatActivity() {
    private var layoutManager: RecyclerView.LayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val logindata = getSharedPreferences("login", Context.MODE_PRIVATE)
        val user_id = logindata.getInt("_id",0)


        val VehicleListe = ArrayList<VehicleList>()

        val user = ArrayList<User>()

        layoutManager = LinearLayoutManager(this)
        VehicleRecyclerView.layoutManager = layoutManager

        var volleyRequestQueue: RequestQueue? = null
        var dialog: ProgressDialog? = null
        val serverAPIURL: String = "http://10.0.2.2:5000/profile"
        val TAG = "My Api"

        fun VerileriGetir() {

            val serverurl = "http://10.0.2.2:5000/machine-learning"

            volleyRequestQueue = Volley.newRequestQueue(this)
            Log.d("res1","aaaaa")
            val json = JSONObject()
            json.put("a","1")
            val strReq: JsonObjectRequest = object : JsonObjectRequest(
                Method.POST,serverurl,json,
                Response.Listener { response ->
                    try {
                        val responseObj = response
                        val gson =Gson()
                        val sItems = responseObj.getJSONArray("res")

                        Log.d("res1","aaaaa :" + sItems)


                        for( i in 0 .. sItems.length()-1){
                            var sItem: VehicleList =
                                gson.fromJson(sItems.get(i).toString(),VehicleList::class.java)
                            VehicleListe.add(sItem)

                        }
                        var adapter = VehicleListRecyclerAdapter(VehicleListe)

                        VehicleRecyclerView.adapter = adapter

                        adapter.setOnItemCLickListener(object :
                            VehicleListRecyclerAdapter.onItemCLickListener {
                            override fun onItemClick(position: Int) {

                                val intent = Intent(this@ProfileActivity, CarouselActivity::class.java)
                                intent.putExtra("ad_id",VehicleListe.get(position).ad_id)
                                startActivity(intent)

                            }

                        })

                        //burda dönen veriyi ekrana bastırmaca yaoılacak

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

        fun userData(id: Int){
            volleyRequestQueue = Volley.newRequestQueue(this)

            val json = JSONObject()
            json.put("_id",id)
            val JsonApi: JsonObjectRequest = object : JsonObjectRequest(
                Method.POST,serverAPIURL,json,
                Response.Listener { response ->
                    try {
                        val responseObj = response
                        val gson = Gson()

                        val sItems = responseObj.getJSONArray("res")

                        for (i in 0..sItems.length()-1) {
                            var sItem: User =
                                gson.fromJson(sItems.get(i).toString(), User::class.java)
                            user.add(sItem)
                        }

                        prof_user_name.text = user[0].userName

                        Glide.with(this).load(user[0].userImage).into(profielimg)
                        Log.e(TAG, "res"+ response )
                    } catch (e: Exception) { // caught while parsing the response
                        Log.e(TAG, "problem occurred"+ e )
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { volleyError -> // error occurred
                    Log.e(TAG, "problem occurred, volley error: " + volleyError.message)
                }) {

                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {

                    val headers: MutableMap<String, String> = HashMap()
                    // Add your Header paramters here
                    return headers
                }
            }
            // Adding request to request queue
            volleyRequestQueue?.add(JsonApi)
        }
        userData(user_id)
    }
}