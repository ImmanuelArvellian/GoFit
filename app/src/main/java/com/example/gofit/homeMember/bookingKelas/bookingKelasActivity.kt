package com.example.gofit.homeMember.bookingKelas

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
import com.example.gofit.api.bookingKelasApi
import com.example.gofit.api.izinInstrukturApi
import com.example.gofit.databinding.ActivityAddIzinInstrukturBinding
import com.example.gofit.databinding.ActivityBookingKelasBinding
import com.example.gofit.homeInstruktur.izinInstruktur.izinInstrukturFragment
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class bookingKelasActivity : AppCompatActivity() {

    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityBookingKelasBinding = ActivityBookingKelasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val cancelBtn = binding.btnCancel
        val saveBtn = binding.btnSave

        val inputLayoutIdJadwalHarian = binding.layoutIdJadwalharian

        val bundle = intent.extras
        val id_member = bundle?.getString("id_member")

        queue = Volley.newRequestQueue(this)

        val jsonobj = JSONObject()

        saveBtn.setOnClickListener{
            val inputIdJadwalHarian = inputLayoutIdJadwalHarian.getEditText()?.getText().toString()

            jsonobj.put("id_booking_kelas", "1")
            jsonobj.put("id_member", id_member)
            jsonobj.put("id_jadwal_harian", inputIdJadwalHarian)
            jsonobj.put("no_struk", "1")
            jsonobj.put("tipe_pembayaran", "1")
            jsonobj.put("tgl_presensi", "1")
            jsonobj.put("id_instruktur_pengganti", "1")

            val request = JsonObjectRequest(
                Request.Method.POST, bookingKelasApi.ADD_URL,jsonobj,
                { response ->
                    Log.d("Responsenya: ", response["message"].toString())

                    Toast.makeText(
                        this@bookingKelasActivity,
                        "Berhasil Melakukan Booking",
                        Toast.LENGTH_SHORT
                    ).show()

                    var bundle = Bundle()
                    val move = Intent(this, bookingKelasFragment::class.java)
                    bundle.putString("id_member", id_member)
                    move.putExtras(bundle)
                    startActivity(move)

                }, { error->
                    try{
                        val errorData = String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errorJson = JSONObject(errorData)
                        if(error.networkResponse.statusCode != 400){
                            Toast.makeText(
                                this@bookingKelasActivity,
                                errorJson["message"].toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }catch (e: Exception){
                        Log.d("Error Login", e.message.toString())
                        Toast.makeText(this@bookingKelasActivity,"exception: " + e.message, Toast.LENGTH_SHORT).show()
                    }
                })
            queue!!.add(request)
        }

        cancelBtn.setOnClickListener{
            var bundle = Bundle()
            bundle.putString("id_member", id_member)
            val moveToHome = Intent(this@bookingKelasActivity, bookingKelasFragment::class.java)
            moveToHome.putExtras(bundle)
            startActivity(moveToHome)
        }
    }
}