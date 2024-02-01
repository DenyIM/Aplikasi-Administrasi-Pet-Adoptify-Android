package com.example.projectpmobile

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AdapterAdopter(private val context: Context, private var dataList: List<ModelAdopter>) :
    RecyclerView.Adapter<MyViewHolderAdopter>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderAdopter {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_adopter, parent, false)
        return MyViewHolderAdopter(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderAdopter, position: Int){
        Glide.with(context)
            .load(dataList[position].dataImageAdopter)
            .placeholder(R.drawable.close)
            .into(holder.recImage)
        holder.recNoHpAdopter.text = dataList[position].dataNoHpAdopter
        holder.recNamaAdopter.text = dataList[position].dataNamaAdopter
        holder.recJenisRumahAdopter.text = dataList[position].dataJenisRumahAdopter
        holder.recCard.setOnClickListener {
            val intent = Intent(context, DetailAdopterActivity::class.java)
            intent.putExtra("Alamat", dataList[holder.adapterPosition].dataAlamatAdopter)
            intent.putExtra("Email", dataList[holder.adapterPosition].dataEmailAdopter)
            intent.putExtra("Image", dataList[holder.adapterPosition].dataImageAdopter)
            intent.putExtra("Nama", dataList[holder.adapterPosition].dataNamaAdopter)
            intent.putExtra("No Hp", dataList[holder.adapterPosition].dataNoHpAdopter)
            intent.putExtra("Jenis Tempat Tinggal", dataList[holder.adapterPosition].dataJenisRumahAdopter)
            intent.putExtra("Tujuan Adopsi", dataList[holder.adapterPosition].dataTujuanAdopter)
            intent.putExtra("Key", dataList[holder.adapterPosition].key)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun searchDataList(searchList: List<ModelAdopter>) {
        dataList = searchList
        notifyDataSetChanged()
    }

}
class MyViewHolderAdopter(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var recImage: ImageView = itemView.findViewById(R.id.recImage)
    var recNoHpAdopter: TextView = itemView.findViewById(R.id.recNoHpAdopter)
    var recNamaAdopter: TextView = itemView.findViewById(R.id.recNamaAdopter)
    var recJenisRumahAdopter: TextView = itemView.findViewById(R.id.recJenisRumahAdopter)
    var recCard: CardView = itemView.findViewById(R.id.recCard)
}