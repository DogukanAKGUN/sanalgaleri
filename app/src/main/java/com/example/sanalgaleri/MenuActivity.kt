package com.example.sanalgaleri

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.sanalgaleri.Model.ItemMenuModel
import com.example.sanalgaleri.Model.User
import com.example.sanalgaleri.RecyclerAdapter.onItemCLickListener
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.nav_header.*
import org.json.JSONObject


class MenuActivity : AppCompatActivity() {

    private var layoutManager: LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null
    //private var liste: ArrayList<ItemMenuModel> = ArrayList();

    lateinit var toggle:ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val logindata = getSharedPreferences("login", Context.MODE_PRIVATE)
        val user_id = logindata.getInt("_id",0)

        //drawer işlemleri burada yapıldı
        val drawerLayout:DrawerLayout= findViewById(R.id.drawerLayout)

        val navView:NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.profile -> startActivity(Intent(this@MenuActivity, ProfileActivity::class.java))
                R.id.favorites -> startActivity(Intent(this@MenuActivity, FavoritesActivity::class.java))
                R.id.myads -> startActivity(Intent(this@MenuActivity, OwnAddsActivity::class.java))
                R.id.addnewad -> startActivity(Intent(this@MenuActivity, NewAdvertiseActivity::class.java))
            }
            true
        }
        //drawer işlemleri bitişi

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


        fun userData(id: Int){
            val user = ArrayList<User>()

            var volleyRequestQueue: RequestQueue? = null
            var dialog: ProgressDialog? = null
            val serverAPIURL: String = "http://10.0.2.2:5000/profile"
            val TAG = "My Api"
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

                        nav_user_name.text = user[0].userName
                        Glide.with(this).load(user[0].userImage).into(nav_profile_image)
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
    //Tıklanan menü itemin ekranda tıklı bir şekilde gözükmesini sağlıyor
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
