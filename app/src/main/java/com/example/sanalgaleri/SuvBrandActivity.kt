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
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.sanalgaleri.Model.suvBrandModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_suv_brand.*
import org.json.JSONObject

class SuvBrandActivity : AppCompatActivity() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suv_brand)

        val suvBrandListe = ArrayList<suvBrandModel>()

        layoutManager = LinearLayoutManager(this)
        SuvBrandRecyclerView.layoutManager = layoutManager

        var volleyRequestQueue: RequestQueue? = null
        var dialog: ProgressDialog? = null
        val serverAPIURL: String = "http://10.0.2.2:5000/suvbrands"
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
                            var sItem: suvBrandModel =
                                gson.fromJson(sItems.get(i).toString(), suvBrandModel::class.java)
                            suvBrandListe.add(sItem)

                        }
                        var adapter = SuvBrandRecyclerAdapter(suvBrandListe)

                        SuvBrandRecyclerView.adapter = adapter

                        dialog?.dismiss()
                        adapter.setOnItemCLickListener(object :
                            SuvBrandRecyclerAdapter.onItemCLickListener {
                            override fun onItemClick(position: Int) {

                                val intent = Intent(this@SuvBrandActivity, VehicleListActivity::class.java)
                                intent.putExtra("brand",suvBrandListe.get(position).brandName)
                                intent.putExtra("type",suvBrandListe.get(position).type)
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

        VerileriGetir()
    }
}