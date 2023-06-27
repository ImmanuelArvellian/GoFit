package com.example.gofit.homeMember.profileMember

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
import com.example.gofit.homeInstruktur.izinInstruktur.izinInstrukturAdapter
import com.example.gofit.model.depositKelas
import com.example.gofit.model.izinInstruktur
import java.util.*
import kotlin.collections.ArrayList

class depositKelasAdapter (private var depositKelasList: List<depositKelas>, context: Context) :
    RecyclerView.Adapter<depositKelasAdapter.ViewHolder>(), Filterable {
    private var filteredIzinList: MutableList<depositKelas>
    private val context: Context

    init {
        filteredIzinList = ArrayList(depositKelasList)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_deposit_kelas, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredIzinList.size
    }

    fun setDepositKelasList(depositKelasList: Array<depositKelas>){
        this.depositKelasList = depositKelasList.toList()
        filteredIzinList = depositKelasList.toMutableList()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val depositKelas = filteredIzinList[position]
        holder.tvIdKelas.text = depositKelas.kelas.nama_kelas
        holder.tvDepositKelas.text = "Sisa Deposit: " + depositKelas.deposit_kelas.toString()
        holder.tvTglExp.text = "Berlaku Sampai " + depositKelas.tgl_exp
}

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charSequenceString = charSequence.toString()
                val filtered: MutableList<depositKelas> = java.util.ArrayList()
                if(charSequenceString.isEmpty()){
                    filtered.addAll(depositKelasList)
                }else{
                    for(deposit_kelas in depositKelasList){
                        if(deposit_kelas.kelas.nama_kelas.lowercase(Locale.getDefault())
                                .contains(charSequenceString.lowercase(Locale.getDefault()))
                        )filtered.add(deposit_kelas)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filtered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults?) {
                filteredIzinList.clear()
                filteredIzinList.addAll(filterResults?.values as List<depositKelas>)
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvIdKelas : TextView
        var tvDepositKelas : TextView
        var tvTglExp : TextView

        init {
            tvIdKelas = itemView.findViewById(R.id.tv_depositKelasNamaKelas)
            tvDepositKelas = itemView.findViewById(R.id.tv_depositKelasJumlahDeposit)
            tvTglExp = itemView.findViewById(R.id.tv_depositKelasTglKadaluarsa)
        }
    }
}