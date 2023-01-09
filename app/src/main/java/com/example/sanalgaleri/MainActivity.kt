package com.example.sanalgaleri

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            val logindata = getSharedPreferences("login", Context.MODE_PRIVATE)
            val editor = logindata.edit()


            fun login(userName: String,password: String ){
                val url = "http://10.0.2.2:5000/login"

                val json = JSONObject()
                json.put("userName",userName)
                json.put("password",password)

                val queue = Volley.newRequestQueue(this)
                val JsonApi: JsonObjectRequest = object : JsonObjectRequest(
                    Method.POST, url, json,
                    Response.Listener { response ->
                        if (response.getString("userName").toString() == userName) {
                            editor.putInt("_id",response.getInt("_id"))
                            editor.apply()

                            val intent = Intent(this, MenuActivity::class.java)


                            //intent.putExtra("userName", response.getString("userName").toString())
                            Toast.makeText(this, "Giriş Yaptı:"+ userName, Toast.LENGTH_SHORT).show()
                            startActivity(intent)
                        }
                    },
                    Response.ErrorListener {
                        Toast.makeText(this, "Kullanıcı Adı veya Şifre Yanlış", Toast.LENGTH_SHORT).show()
                    }) {}
                queue.add(JsonApi)

            }

            //giriş yapıldıktan sonra username ve password olarak giriş yapmaya atama
            login.setOnClickListener {

                var userName = username.text.toString()
                var password = password.text.toString()
                login(userName, password)
            }

            //kayıt ekranına geçiş
            signin.setOnClickListener{
                val siginIntent = Intent(this, RegisterActivity::class.java)
                startActivity(siginIntent)
            }


        }
    }
