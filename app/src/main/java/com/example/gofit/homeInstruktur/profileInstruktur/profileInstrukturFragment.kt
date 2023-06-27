package com.example.gofit.homeInstruktur.profileInstruktur

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gofit.R
import com.example.gofit.api.instrukturApi
import com.example.gofit.api.memberApi
import com.example.gofit.databinding.FragmentProfileInstrukturBinding
import com.example.gofit.databinding.FragmentProfileMemberBinding
import com.example.gofit.model.instruktur
import com.example.gofit.model.member
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class profileInstrukturFragment : Fragment() {

    private var _binding: FragmentProfileInstrukturBinding? = null
    private val binding get() = _binding!!
    private var queue: RequestQueue? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileInstrukturBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id_user = arguments?.getString("id_user")
        val requestQueue = Volley.newRequestQueue(requireContext())

        val stringRequest: StringRequest = object :
            StringRequest(
                Method.GET, instrukturApi.GET_BY_ID_URL + id_user, Response.Listener { response ->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                val instruktur = gson.fromJson(
                    jsonObject.getJSONObject("data").toString(), instruktur::class.java
                )

                binding.tvNamaInstruktur.text = instruktur.nama
                binding.tvEmail.text = instruktur.email
                binding.tvNoTelp.text = instruktur.no_telp
                binding.tvAlamat.text = instruktur.alamat
                binding.tvJumlahTerlambat.text = instruktur.jumlah_terlambat
            },
                Response.ErrorListener { error ->
                    // Handle error
                    try{
                        val responseBody =
                            String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toast.makeText(
                            context,
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    }catch (e: Exception){
                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    }
                }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        requestQueue!!.add(stringRequest)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}