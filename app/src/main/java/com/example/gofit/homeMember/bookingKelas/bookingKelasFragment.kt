package com.example.gofit.homeMember.bookingKelas

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
import com.example.gofit.api.bookingKelasApi
import com.example.gofit.databinding.FragmentBookingKelasBinding
import com.example.gofit.model.bookingKelas
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class bookingKelasFragment : Fragment() {

    private var _binding: FragmentBookingKelasBinding? = null
    private val binding get() = _binding!!
    private var srBookingKelas: SwipeRefreshLayout? = null
    private var adapter: bookingKelasAdapter? = null
    private var svBookingKelas: SearchView? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBookingKelasBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id_user = arguments?.getString("id_user")
        Log.d("ID User: ", id_user.toString())
        queue = Volley.newRequestQueue(requireContext())

        srBookingKelas = binding.srBookingKelas
        svBookingKelas = binding.svBookingKelas

        srBookingKelas?.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener { allBookingKelas(id_user!!) })
        svBookingKelas?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
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
            val moveToAddEdit = Intent(context, bookingKelasActivity::class.java)
            moveToAddEdit.putExtras(bundle)
            startActivity(moveToAddEdit)
        }

        val rvProduk = binding.rvBookingKelas
        adapter = bookingKelasAdapter(ArrayList(), requireContext())
        rvProduk.layoutManager = LinearLayoutManager(context)
        rvProduk.adapter = adapter
        allBookingKelas(id_user!!)
    }

    private fun allBookingKelas(id_booking_kelas: String){
        srBookingKelas!!.isRefreshing = true
        val StringRequest: StringRequest = object : StringRequest(Method.GET, bookingKelasApi.GET_BY_MEMBER + id_booking_kelas,
            Response.Listener { response->
                val jsonObject = JSONObject(response)
                val gson = Gson()
                val bookingKelas = gson.fromJson(
                    jsonObject.getJSONArray("data").toString(), Array<bookingKelas>::class.java
                )

                adapter!!.setBookingKelasList(bookingKelas)
                adapter!!.filter.filter(svBookingKelas!!.query)
                srBookingKelas!!.isRefreshing=false

                if(!bookingKelas.isEmpty()){
                    Toast.makeText(context,"Data Berhasil Diambil", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context,"Data Kosong", Toast.LENGTH_SHORT).show()
                }
            }, Response.ErrorListener { error->
                srBookingKelas!!.isRefreshing=false
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