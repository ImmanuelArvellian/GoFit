package com.example.gofit.homeMember.profileMember

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gofit.R
import com.example.gofit.api.mainApi
import com.example.gofit.api.memberApi
import com.example.gofit.databinding.FragmentProfileMemberBinding
import com.example.gofit.model.member
import com.google.gson.Gson
import org.json.JSONObject
import java.lang.reflect.Member
import java.nio.charset.StandardCharsets

class profileMemberFragment : Fragment() {

    private var _binding: FragmentProfileMemberBinding? = null
    private val binding get() = _binding!!
    private var queue: RequestQueue? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileMemberBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id_user = arguments?.getString("id_user")
        val requestQueue = Volley.newRequestQueue(requireContext())

        val btnDepositKelas = binding.btnDepositKelas
        btnDepositKelas.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id_member", id_user)
            val move = Intent(context, depositKelasActivity::class.java)
            move.putExtras(bundle)
            startActivity(move)
        }

        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, memberApi.GET_BY_ID_URL + id_user, Response.Listener { response ->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                val member = gson.fromJson(
                    jsonObject.getJSONObject("data").toString(), member::class.java
                )

                var status_membership = member.status_membership
                var tgl_kadaluarsa = member.tgl_exp_membership

                if(status_membership.equals("1", true)){
                    status_membership = "AKTIF"
                    tgl_kadaluarsa = "Sampai " + tgl_kadaluarsa
                }else{
                    status_membership = "TIDAK AKTIF"
                    tgl_kadaluarsa = "-"
                }

                binding.tvNamaMember.text = member.nama
                binding.tvStatusMembership.text = status_membership
                binding.tvEmail.text = member.email
                binding.tvNoTelp.text = member.no_telp
                binding.tvTglLahir.text = member.tgl_lahir
                binding.tvAlamat.text = member.alamat
                binding.tvDepositUang.text = "Rp" + member.sisa_deposit + ",-"
                binding.tvTglExp.text = tgl_kadaluarsa
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