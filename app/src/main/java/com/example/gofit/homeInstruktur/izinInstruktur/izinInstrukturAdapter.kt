package com.example.gofit.homeInstruktur.izinInstruktur

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.gofit.R
import com.example.gofit.model.izinInstruktur
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*
import kotlin.collections.ArrayList

class izinInstrukturAdapter(private var izinInstrukturList: List<izinInstruktur>, context: Context) :
    RecyclerView.Adapter<izinInstrukturAdapter.ViewHolder>(), Filterable {
    private var filteredIzinList: MutableList<izinInstruktur>
    private val context: Context

    init {
        filteredIzinList = ArrayList(izinInstrukturList)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_izin_instruktur, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredIzinList.size
    }

    fun setIzinInstrukuturList(IzinInstrkuturList: Array<izinInstruktur>){
        this.izinInstrukturList = IzinInstrkuturList.toList()
        filteredIzinList = IzinInstrkuturList.toMutableList()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val izinInstruktur = filteredIzinList[position]
        holder.tvIdIzin.text = izinInstruktur.id_izin
        holder.tvIdInstruktur.text = "Nama Instruktur: " + izinInstruktur.instruktur.nama
        holder.tvIdJadwalHarian.text = "Nama Kelas: " + izinInstruktur.jadwal_harian.kelas.nama_kelas
        holder.tvKeterangan.text = "Keterangan: " + izinInstruktur.keterangan
        holder.tvTglIzin.text = "Tanggal: " + izinInstruktur.tgl_izin
        holder.tvStatus.text = "Status: " + izinInstruktur.status
        holder.tvIdInstrukturPengganti.text = "Instruktur Pengganti: " + izinInstruktur.id_instruktur_pengganti
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charSequenceString = charSequence.toString()
                val filtered: MutableList<izinInstruktur> = java.util.ArrayList()
                if(charSequenceString.isEmpty()){
                    filtered.addAll(izinInstrukturList)
                }else{
                    for(izin in izinInstrukturList){
                        if(izin.instruktur.nama.lowercase(Locale.getDefault())
                                .contains(charSequenceString.lowercase(Locale.getDefault()))
                        )filtered.add(izin)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filtered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults?) {
                filteredIzinList.clear()
                filteredIzinList.addAll(filterResults?.values as List<izinInstruktur>)
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvIdIzin : TextView
        var tvIdInstruktur : TextView
        var tvIdJadwalHarian : TextView
        var tvKeterangan : TextView
        var tvTglIzin : TextView
        var tvStatus : TextView
        var tvIdInstrukturPengganti : TextView
        var cvIzin : CardView

        init {
            tvIdIzin = itemView.findViewById(R.id.tv_idIzin)
            tvIdInstruktur = itemView.findViewById(R.id.tv_idInstruktur)
            tvIdJadwalHarian = itemView.findViewById(R.id.tv_idJadwalHarian)
            tvKeterangan = itemView.findViewById(R.id.tv_keterangan)
            tvTglIzin = itemView.findViewById(R.id.tv_tglIzin)
            tvStatus = itemView.findViewById(R.id.tv_status)
            tvIdInstrukturPengganti = itemView.findViewById(R.id.tv_instrukturPengganti)
            cvIzin = itemView.findViewById(R.id.cv_izin)
        }
    }
}