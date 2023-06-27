package com.example.gofit.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.gofit.api.loginApi
import com.example.gofit.databinding.ActivityLoginBinding
import com.example.gofit.homeInstruktur.homeInstrukturActivity
import com.example.gofit.homeMO.homeMoActivity
import com.example.gofit.homeMember.homeMemberActivity
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class LoginActivity : AppCompatActivity() {
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val inputLayoutEmail = binding.etEmail
        val inputLayoutPassword = binding.etPassword

        val btnSignIn = binding.btnSignIn

        queue = Volley.newRequestQueue(this)

        val jsonobj = JSONObject()

        btnSignIn.setOnClickListener(View.OnClickListener {
            val inputEmail = inputLayoutEmail.getText().toString()
            val inputPassword = inputLayoutPassword.getText().toString()

            jsonobj.put("email", inputEmail)
            jsonobj.put("password", inputPassword)
            val request = JsonObjectRequest(Request.Method.POST, loginApi.LOGIN_URL, jsonobj,
                Response.Listener { response ->
                    // Respon sukses, lakukan sesuatu setelah pengguna berhasil masuk
                    Log.d("Responsenya: ", response["message"].toString())

                    Toast.makeText(
                        this@LoginActivity,
                        response["message"].toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    var bundle = Bundle()
                    var intent: Intent

                    if(response["id"].toString().contains('P')){
                        intent = Intent(this, homeMoActivity::class.java)
                        bundle.putString("id_user", response["id"].toString())
                        Log.d("ID User: ", response["id"].toString())
                        Log.d("ID Role: ", response["role"].toString())
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }else if(response["id"].toString().contains('I')){
                        intent = Intent(this, homeInstrukturActivity::class.java)
                        bundle.putString("id_user", response["id"].toString())
                        Log.d("ID User: ", response["id"].toString())
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }else{
                        intent = Intent(this, homeMemberActivity::class.java)
                        bundle.putString("id_user", response["id"].toString())
                        Log.d("ID User: ", response["id"].toString())
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }
                },
                Response.ErrorListener { error ->
                    try{
                        val errorData = String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errorJson = JSONObject(errorData)
                        if(error.networkResponse.statusCode != 400){
                            Toast.makeText(
                                this@LoginActivity,
                                errorJson["message"].toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }catch (e: Exception){
                        Log.d("Error Login", e.message.toString())
                        Toast.makeText(this@LoginActivity,"exception: " + e.message, Toast.LENGTH_SHORT).show()
                    }
                })

            queue?.add(request)
        })
    }
}