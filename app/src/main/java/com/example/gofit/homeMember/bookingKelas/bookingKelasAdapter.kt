package com.example.gofit.homeMember.bookingKelas

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.gofit.R
import com.example.gofit.model.bookingKelas
import java.util.*
import kotlin.collections.ArrayList

class bookingKelasAdapter (private var bookingKelasList: List<bookingKelas>, context: Context) :
    RecyclerView.Adapter<bookingKelasAdapter.ViewHolder>(), Filterable {
    private var filteredBookingKelasList: MutableList<bookingKelas>
    private val context: Context

    init {
        filteredBookingKelasList = ArrayList(bookingKelasList)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_booking_kelas, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredBookingKelasList.size
    }

    fun setBookingKelasList(bookingKelasList: Array<bookingKelas>){
        this.bookingKelasList = bookingKelasList.toList()
        filteredBookingKelasList = bookingKelasList.toMutableList()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bookingKelas = filteredBookingKelasList[position]

        holder.tvNamaKelas.text = "Nama Kelas: " + bookingKelas.jadwal_harian.kelas.nama_kelas
        holder.tvNamaInstruktur.text = "Nama Instruktur: " + bookingKelas.jadwal_harian.instruktur.nama
        holder.tvTglTujuan.text = "Booking untuk : " + bookingKelas.jadwal_harian.tanggal
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charSequenceString = charSequence.toString()
                val filtered: MutableList<bookingKelas> = java.util.ArrayList()
                if(charSequenceString.isEmpty()){
                    filtered.addAll(bookingKelasList)
                }else{
                    for(bookingKelas in bookingKelasList){
                        if(bookingKelas.jadwal_harian.kelas.nama_kelas.lowercase(Locale.getDefault())
                                .contains(charSequenceString.lowercase(Locale.getDefault()))
                        )filtered.add(bookingKelas)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filtered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults?) {
                filteredBookingKelasList.clear()
                filteredBookingKelasList.addAll(filterResults?.values as List<bookingKelas>)
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvNamaKelas : TextView
        var tvNamaInstruktur : TextView
        var tvTglTujuan : TextView
        var cvBookingKelas : CardView

        init {
            tvNamaKelas = itemView.findViewById(R.id.tv_namaKelas)
            tvNamaInstruktur = itemView.findViewById(R.id.tv_namaInstruktur)
            tvTglTujuan = itemView.findViewById(R.id.tv_tglTujuan)
            cvBookingKelas = itemView.findViewById(R.id.cv_bookingKelas)
        }
    }
}