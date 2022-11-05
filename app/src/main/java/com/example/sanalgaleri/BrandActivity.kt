package com.example.sanalgaleri

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.sanalgaleri.Model.otomobilBrandModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_brand.*
import org.json.JSONObject

class BrandActivity : AppCompatActivity() {


    private var layoutManager: LayoutManager? = null
    private var adapter: RecyclerView.Adapter<BrandRecyclerAdapter.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_brand)

        val brandListe = ArrayList<otomobilBrandModel>()

        layoutManager = LinearLayoutManager(this)

        brandRecyclerView.layoutManager = layoutManager

        var volleyRequestQueue: RequestQueue? = null
        var dialog: ProgressDialog? = null
        val serverAPIURL: String = "http://10.0.2.2:5000/brands"
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
                            var sItem: otomobilBrandModel =
                                gson.fromJson(sItems.get(i).toString(), otomobilBrandModel::class.java)
                            brandListe.add(sItem)

                        }
                        var adapter = BrandRecyclerAdapter(brandListe)

                        brandRecyclerView.adapter = adapter

                        dialog?.dismiss()
                        adapter.setOnItemCLickListener(object :
                            BrandRecyclerAdapter.onItemCLickListener {
                            override fun onItemClick(position: Int) {

                                //val intent = Intent(this@BrandActivity, BrandActivity::class.java)

                                //Toast.makeText(this@BrandActivity, "Giriş Yaptı:"+ position, Toast.LENGTH_SHORT).show()
                                //startActivity(intent)

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