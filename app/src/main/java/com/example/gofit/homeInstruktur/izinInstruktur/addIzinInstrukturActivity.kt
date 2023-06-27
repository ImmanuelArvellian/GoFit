package com.example.gofit.homeInstruktur.izinInstruktur

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.gofit.R
import com.example.gofit.api.izinInstrukturApi
import com.example.gofit.databinding.ActivityAddIzinInstrukturBinding
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class addIzinInstrukturActivity : AppCompatActivity() {

    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityAddIzinInstrukturBinding = ActivityAddIzinInstrukturBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val cancelBtn = binding.btnCancel
        val saveBtn = binding.btnSave

        val inputLayoutIdJadwalHarian = binding.layoutIdJadwalharian
        val inputLayoutKeterangan = binding.layoutKeterangan
        val inputLayoutIdInstrukturPengganti = binding.layoutIdInstrukturPengganti

        val bundle = intent.extras
        val id_instruktur = bundle?.getString("id_instruktur")

        queue = Volley.newRequestQueue(this)

        val jsonobj = JSONObject()

        saveBtn.setOnClickListener{
            val inputIdJadwalHarian = inputLayoutIdJadwalHarian.getEditText()?.getText().toString()
            val inputKeterangan = inputLayoutKeterangan.getEditText()?.getText().toString()
            val inputIdInstrukturPengganti = inputLayoutIdInstrukturPengganti.getEditText()?.getText().toString()

            jsonobj.put("id_izin", "1")
            jsonobj.put("id_instruktur", id_instruktur)
            jsonobj.put("id_jadwal_harian", inputIdJadwalHarian)
            jsonobj.put("keterangan", inputKeterangan)
            jsonobj.put("tgl_izin", "1")
            jsonobj.put("status_konfirmasi", 1)
            jsonobj.put("id_instruktur_pengganti", inputIdInstrukturPengganti)

            val request = JsonObjectRequest(
                Request.Method.POST, izinInstrukturApi.ADD_URL,jsonobj,
                { response ->
                    Log.d("Responsenya: ", response["message"].toString())

                    Toast.makeText(
                        this@addIzinInstrukturActivity,
                        "Berhasil mengajukan izin",
                        Toast.LENGTH_SHORT
                    ).show()

                    var bundle = Bundle()
                    val move = Intent(this, izinInstrukturFragment::class.java)
                    bundle.putString("id_instruktur", id_instruktur)
                    move.putExtras(bundle)
                    startActivity(move)

                }, { error->
                    try{
                        val errorData = String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errorJson = JSONObject(errorData)
                        if(error.networkResponse.statusCode != 400){
                            Toast.makeText(
                                this@addIzinInstrukturActivity,
                                errorJson["message"].toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }catch (e: Exception){
                        Log.d("Error Login", e.message.toString())
                        Toast.makeText(this@addIzinInstrukturActivity,"exception: " + e.message, Toast.LENGTH_SHORT).show()
                    }
                })
            queue!!.add(request)
        }

        cancelBtn.setOnClickListener{
            var bundle = Bundle()
            bundle.putString("id_instruktur", id_instruktur)
            val moveToHome = Intent(this@addIzinInstrukturActivity, izinInstrukturFragment::class.java)
            moveToHome.putExtras(bundle)
            startActivity(moveToHome)
        }
    }
}