package com.example.sanalgaleri

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.sanalgaleri.Model.ItemMenuModel
import com.example.sanalgaleri.RecyclerAdapter.onItemCLickListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_menu.*
import org.json.JSONObject


class MenuActivity : AppCompatActivity() {

    private var layoutManager: LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null
    //private var liste: ArrayList<ItemMenuModel> = ArrayList();



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val liste = ArrayList<ItemMenuModel>()

        layoutManager = LinearLayoutManager(this)

        recyclerView.layoutManager = layoutManager

        var volleyRequestQueue: RequestQueue? = null
        var dialog: ProgressDialog? = null
        val serverAPIURL: String = "http://10.0.2.2:5000/vehicles"
        val TAG = "My Api"



        fun VerileriGetir() {
            volleyRequestQueue = Volley.newRequestQueue(this)
            dialog = ProgressDialog.show(this, "", "Lütfen bekleyin...", true);
            val parameters: MutableMap<String, String> = HashMap()

            val strReq: StringRequest = object : StringRequest(
                Method.GET,serverAPIURL,
                Response.Listener { response ->
                    Log.e(TAG, "response: " + response)
                    try {
                        val responseObj = JSONObject(response)
                        val gson = Gson()

                        val sItems = responseObj.getJSONArray("res")

                        for (i in 0..sItems.length()-1) {
                            var sItem: ItemMenuModel =
                                gson.fromJson(sItems.get(i).toString(), ItemMenuModel::class.java)
                            liste.add(sItem)

                        }
                        var Menuadapter = RecyclerAdapter(liste)

                        recyclerView.adapter = Menuadapter

                        dialog?.dismiss()
                        Menuadapter.setOnItemCLickListener(object : onItemCLickListener{
                            override fun onItemClick(position: Int) {

                                if (position == 0){
                                    val intent = Intent(this@MenuActivity, BrandActivity::class.java)
                                    startActivity(intent)
                                }
                                else if(position==1){
                                    val intent = Intent(this@MenuActivity, SuvBrandActivity::class.java)
                                    startActivity(intent)
                                }
                                else if(position==2){
                                    val intent = Intent(this@MenuActivity, motorcycleBrandActivity::class.java)
                                    startActivity(intent)
                                }


                                Toast.makeText(this@MenuActivity, "Giriş Yaptı:"+ position, Toast.LENGTH_SHORT).show()


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

        VerileriGetir()

        //var adapter = RecyclerAdapter(liste)
        //recyclerView.adapter = adapter




        //getData()
    }

/*
fun getData(){
        val url = "http://10.0.2.2:5000/vehicles"
        val requestQueue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(com.android.volley.Request.Method.GET,url,
            Response.Listener { response ->
                Log.d("response",response)
                    val jsonObject = JSONObject(response)

                    val jsonArray = jsonObject.getJSONArray("data")
                    for (i in 0..jsonArray.length()-1){
                        val jo = jsonArray.getJSONObject(i)
                        val id = jo.get("_id").toString()
                        val title = jo.get("title").toString()
                        val image = jo.get("image").toString()
                        val detail = jo.get("detail").toString()
                        val item = ItemMenuModel(id,title,image,detail)
                        liste.add(item)

                        layoutManager = LinearLayoutManager(this)

                        recyclerView.layoutManager = layoutManager

                        adapter = RecyclerAdapter(liste)

                        recyclerView.adapter = adapter
                    }


            },Response.ErrorListener { error ->

            })

        requestQueue.add(stringRequest)
    }
*/

}
