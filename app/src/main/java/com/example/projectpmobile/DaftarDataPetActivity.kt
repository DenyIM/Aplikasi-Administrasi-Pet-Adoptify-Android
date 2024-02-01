package com.example.projectpmobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.example.projectpmobile.databinding.ActivityDaftarDataPetBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale
import android.view.View
import androidx.appcompat.widget.SearchView

class DaftarDataPetActivity : AppCompatActivity() {
    var databaseReference: DatabaseReference? = null
    var eventListener: ValueEventListener? = null
    private lateinit var dataList: ArrayList<ModelPet>
    private lateinit var adapterPet: AdapterPet
    private lateinit var binding: ActivityDaftarDataPetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarDataPetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gridLayoutManager = GridLayoutManager(this@DaftarDataPetActivity, 1)
        binding.recyclerViewPet.layoutManager = gridLayoutManager
        binding.searchPet.clearFocus()

        val builder = AlertDialog.Builder(this@DaftarDataPetActivity)
        builder.setCancelable(false)
        builder.setView(R.layout.progressbar_layout)

        val dialog = builder.create()
        dialog.show()
        dataList = ArrayList()
        adapterPet = AdapterPet(this@DaftarDataPetActivity, dataList)
        binding.recyclerViewPet.adapter = adapterPet
        databaseReference = FirebaseDatabase.getInstance("https://tugas-proyek-pmobile-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Data Pet")
        dialog.show()
        eventListener = databaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                for (itemSnapshot in snapshot.children) {
                    val modelPet = itemSnapshot.getValue(ModelPet::class.java)
                    if (modelPet != null) {
                        modelPet.key = itemSnapshot.key
                        dataList.add(modelPet)
                    }
                }
                adapterPet.notifyDataSetChanged()
                Log.d("DaftarDataPetActivity", "Jumlah data: ${dataList.size}")
                dialog.dismiss()
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Error: ${error.message}")
                dialog.dismiss()
            }
        })
        binding.fabPet.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@DaftarDataPetActivity, TambahDataPetActivity::class.java)
            startActivity(intent)
        })
        binding.fabBack.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@DaftarDataPetActivity, MainActivity::class.java)
            startActivity(intent)
        })
        binding.searchPet.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                searchList(newText)
                return true
            }

        })
    }

    fun searchList(text: String) {
        val searchList = java.util.ArrayList<ModelPet>()
        for (dataClass in dataList) {
            if (dataClass.dataNamaPet?.lowercase()
                    ?.contains(text.lowercase(Locale.getDefault())) == true
            ) {
                searchList.add(dataClass)
            }
        }
        adapterPet.searchDataList(searchList)
    }
}