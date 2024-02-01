package com.example.projectpmobile

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AdapterPet(private val context: Context, private var dataList: List<ModelPet>) :
    RecyclerView.Adapter<MyViewHolderPet>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderPet {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_pet, parent, false)
        return MyViewHolderPet(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderPet, position: Int){
        Glide.with(context)
            .load(dataList[position].dataImagePet)
            .placeholder(R.drawable.close)
            .into(holder.recImage)
        holder.recKategoriPet.text = dataList[position].dataKategoriPet
        holder.recNamaPet.text = dataList[position].dataNamaPet
        holder.recRasPet.text = dataList[position].dataRasPet
        holder.recCard.setOnClickListener {
            val intent = Intent(context, DetailPetActivity::class.java)
            intent.putExtra("Description", dataList[holder.adapterPosition].dataDescPet)
            intent.putExtra("Image", dataList[holder.adapterPosition].dataImagePet)
            intent.putExtra("Kategori", dataList[holder.adapterPosition].dataKategoriPet)
            intent.putExtra("Nama", dataList[holder.adapterPosition].dataNamaPet)
            intent.putExtra("Ras", dataList[holder.adapterPosition].dataRasPet)
            intent.putExtra("Tanggal Lahir", dataList[holder.adapterPosition].dataTglLahirPet)
            intent.putExtra("Umur", dataList[holder.adapterPosition].dataUmurPet)
            intent.putExtra("Key", dataList[holder.adapterPosition].key)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun searchDataList(searchList: List<ModelPet>) {
        dataList = searchList
        notifyDataSetChanged()
    }

}
class MyViewHolderPet(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var recImage: ImageView = itemView.findViewById(R.id.recImage)
    var recKategoriPet: TextView = itemView.findViewById(R.id.recKategoriPet)
    var recNamaPet: TextView = itemView.findViewById(R.id.recNamaPet)
    var recRasPet: TextView = itemView.findViewById(R.id.recRasPet)
    var recCard: CardView = itemView.findViewById(R.id.recCard)
}