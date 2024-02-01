package com.example.projectpmobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.projectpmobile.databinding.ActivityDetailAdopterBinding
import com.example.projectpmobile.databinding.ActivityDetailPetBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class DetailAdopterActivity : AppCompatActivity() {
    var imageUrl = ""
    var key = ""
    private lateinit var binding: ActivityDetailAdopterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailAdopterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        if (bundle != null) {
            binding.detailAlamatAdopter.text = bundle.getString("Alamat")
            binding.detailEmailAdopter.text = bundle.getString("Email")
            binding.detailNamaAdopter.text = bundle.getString("Nama")
            binding.detailNoHpAdopter.text = bundle.getString("No Hp")
            binding.detailJenisRumahAdopter.text = bundle.getString("Jenis Tempat Tinggal")
            binding.detailTujuanAdopter.text = bundle.getString("Tujuan Adopsi")
            key = bundle.getString("Key")!!
            imageUrl = bundle.getString("Image")!!
            Glide.with(this).load(bundle.getString("Image"))
                .into(binding.detailImageAdopter)
        }

        binding.fabDeleteData.setOnClickListener{
            val reference = FirebaseDatabase.getInstance("https://tugas-proyek-pmobile-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Data Adopter")
            val storage = FirebaseStorage.getInstance("gs://tugas-proyek-pmobile.appspot.com")
            val storageReference = storage.getReferenceFromUrl(imageUrl)

            storageReference.delete().addOnSuccessListener {
                reference.child(key).removeValue()
                Toast.makeText(this@DetailAdopterActivity, "Data sudah dihapus", Toast.LENGTH_SHORT).show()
                startActivity(Intent(applicationContext, DaftarDataAdopterActivity::class.java))
                finish()
            }
        }

        binding.fabEditData.setOnClickListener {
            val intent = Intent(this@DetailAdopterActivity, UpdateAdopterActivity::class.java)
                .putExtra("Alamat", binding.detailAlamatAdopter.text.toString())
                .putExtra("Email", binding.detailEmailAdopter.text.toString())
                .putExtra("Nama", binding.detailNamaAdopter.text.toString())
                .putExtra("No Hp", binding.detailNoHpAdopter.text.toString())
                .putExtra("Jenis Tempat Tinggal", binding.detailJenisRumahAdopter.text.toString())
                .putExtra("Tujuan Adopsi", binding.detailTujuanAdopter.text.toString())
                .putExtra("Image", imageUrl)
                .putExtra("Key", key)
            startActivity(intent)
        }

    }
}