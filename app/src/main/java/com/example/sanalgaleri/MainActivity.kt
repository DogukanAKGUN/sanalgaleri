package com.example.sanalgaleri

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

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
                            val intent = Intent(this, MenuActivity::class.java)
                            intent.putExtra("userName", response.getString("userName").toString())
                            Toast.makeText(this, "Giriş Yaptı:"+ userName, Toast.LENGTH_SHORT).show()
                            startActivity(intent)
                        }
                    },
                    Response.ErrorListener {
                        Toast.makeText(this, "Kullanıcı Adı veya Şifre Yanlış", Toast.LENGTH_SHORT).show()
                    }) {}
                queue.add(JsonApi)

            }

            login.setOnClickListener {

                var userName = username.text.toString()
                var password = password.text.toString()

                login(userName, password)


            }

            signin.setOnClickListener{
                val siginIntent = Intent(this, RegisterActivity::class.java)
                startActivity(siginIntent)
            }

        }
    }
