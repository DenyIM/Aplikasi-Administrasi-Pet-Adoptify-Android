package com.example.projectpmobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.projectpmobile.databinding.ActivityDaftarDataAdopterBinding
import com.example.projectpmobile.databinding.ActivityDaftarDataPetBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale

class DaftarDataAdopterActivity : AppCompatActivity() {
    var databaseReference: DatabaseReference? = null
    var eventListener: ValueEventListener? = null
    private lateinit var dataList: ArrayList<ModelAdopter>
    private lateinit var adapterAdopter: AdapterAdopter
    private lateinit var binding: ActivityDaftarDataAdopterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarDataAdopterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gridLayoutManager = GridLayoutManager(this@DaftarDataAdopterActivity, 1)
        binding.recyclerViewAdopter.layoutManager = gridLayoutManager
        binding.searchAdopter.clearFocus()

        val builder = AlertDialog.Builder(this@DaftarDataAdopterActivity)
        builder.setCancelable(false)
        builder.setView(R.layout.progressbar_layout)

        val dialog = builder.create()
        dialog.show()
        dataList = ArrayList()
        adapterAdopter = AdapterAdopter(this@DaftarDataAdopterActivity, dataList)
        binding.recyclerViewAdopter.adapter = adapterAdopter
        databaseReference = FirebaseDatabase.getInstance("https://tugas-proyek-pmobile-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Data Adopter")
        dialog.show()
        eventListener = databaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                for (itemSnapshot in snapshot.children) {
                    val modelAdopter = itemSnapshot.getValue(ModelAdopter::class.java)
                    if (modelAdopter != null) {
                        modelAdopter.key = itemSnapshot.key
                        dataList.add(modelAdopter)
                    }
                }
                adapterAdopter.notifyDataSetChanged()
                Log.d("DaftarDataAdopterActivity", "Jumlah data: ${dataList.size}")
                dialog.dismiss()
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Error: ${error.message}")
                dialog.dismiss()
            }
        })
        binding.fabAdopter.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@DaftarDataAdopterActivity, TambahDataAdopterActivity::class.java)
            startActivity(intent)
        })
        binding.fabBack.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@DaftarDataAdopterActivity, MainActivity::class.java)
            startActivity(intent)
        })
        binding.searchAdopter.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
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
        val searchList = java.util.ArrayList<ModelAdopter>()
        for (dataClass in dataList) {
            if (dataClass.dataNamaAdopter?.lowercase()
                    ?.contains(text.lowercase(Locale.getDefault())) == true
            ) {
                searchList.add(dataClass)
            }
        }
        adapterAdopter.searchDataList(searchList)
    }
}