package com.example.gofit.homeInstruktur.izinInstruktur

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gofit.api.izinInstrukturApi
import com.example.gofit.databinding.FragmentIzinInstrukturBinding
import com.example.gofit.model.izinInstruktur
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class izinInstrukturFragment : Fragment() {

    private var _binding: FragmentIzinInstrukturBinding? = null
    private val binding get() = _binding!!
    private var srIzinInstruktur: SwipeRefreshLayout? = null
    private var adapter: izinInstrukturAdapter? = null
    private var svIzinInstruktur: SearchView? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentIzinInstrukturBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id_user = arguments?.getString("id_user")
        Log.d("ID User: ", id_user.toString())
        queue = Volley.newRequestQueue(requireContext())

        srIzinInstruktur = binding.srIzinInstruktur
        svIzinInstruktur = binding.svIzinInstruktur

        srIzinInstruktur?.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener { allIzin(id_user!!) })
        svIzinInstruktur?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }
            override fun onQueryTextChange(s: String): Boolean {
                adapter!!.filter.filter(s)
                return false
            }
        })

        val fabAdd = binding.fabAdd
        Log.d("Halo: ", id_user.toString())
        fabAdd.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("id_instruktur", id_user)
            Log.d("ID Instruktur: ", id_user.toString())
            val moveToAddEdit = Intent(context, addIzinInstrukturActivity::class.java)
            moveToAddEdit.putExtras(bundle)
            startActivity(moveToAddEdit)
        }

        val rvProduk = binding.rvIzinInstruktur
        adapter = izinInstrukturAdapter(ArrayList(), requireContext())
        rvProduk.layoutManager = LinearLayoutManager(context)
        rvProduk.adapter = adapter
        allIzin(id_user!!)
    }

    private fun allIzin(id_izin_instruktur: String){
        srIzinInstruktur!!.isRefreshing = true
        val StringRequest: StringRequest = object : StringRequest(Method.GET, izinInstrukturApi.GET_BY_INSTRUKTUR + id_izin_instruktur,
            Response.Listener { response->
                val jsonObject = JSONObject(response)
                val gson = Gson()
                val izinInstruktur = gson.fromJson(
                    jsonObject.getJSONArray("data").toString(), Array<izinInstruktur>::class.java
                )

                adapter!!.setIzinInstrukuturList(izinInstruktur)
                adapter!!.filter.filter(svIzinInstruktur!!.query)
                srIzinInstruktur!!.isRefreshing=false

                if(!izinInstruktur.isEmpty()){
                    Toast.makeText(context,"Data Berhasil Diambil", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context,"Data Kosong", Toast.LENGTH_SHORT).show()
                }
            }, Response.ErrorListener { error->
                srIzinInstruktur!!.isRefreshing=false
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        context,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(context,e.message, Toast.LENGTH_SHORT).show()
                }
            }
        ){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String,String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(StringRequest)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}