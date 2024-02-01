package com.example.projectpmobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.projectpmobile.databinding.ActivityDetailPetBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class DetailPetActivity : AppCompatActivity() {
    var imageUrl = ""
    var key = ""
    private lateinit var binding: ActivityDetailPetBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        if (bundle != null) {
            binding.detailDescPet.text = bundle.getString("Description")
            binding.detailNamaPet.text = bundle.getString("Nama")
            binding.detailKategoriPet.text = bundle.getString("Kategori")
            binding.detailRasPet.text = bundle.getString("Ras")
            binding.detailTglLahirPet.text = bundle.getString("Tanggal Lahir")
            binding.detailUmurPet.text = bundle.getString("Umur")
            key = bundle.getString("Key")!!
            imageUrl = bundle.getString("Image")!!
            Glide.with(this).load(bundle.getString("Image"))
                .into(binding.detailImagePet)
        }

        binding.fabDeleteData.setOnClickListener{
            val reference = FirebaseDatabase.getInstance("https://tugas-proyek-pmobile-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Data Pet")
            val storage = FirebaseStorage.getInstance("gs://tugas-proyek-pmobile.appspot.com")
            val storageReference = storage.getReferenceFromUrl(imageUrl)

            storageReference.delete().addOnSuccessListener {
                reference.child(key).removeValue()
                Toast.makeText(this@DetailPetActivity, "Data sudah dihapus", Toast.LENGTH_SHORT).show()
                startActivity(Intent(applicationContext, DaftarDataPetActivity::class.java))
                finish()
            }
        }

        binding.fabEditData.setOnClickListener {
            val intent = Intent(this@DetailPetActivity, UpdatePetActivity::class.java)
                .putExtra("Description", binding.detailDescPet.text.toString())
                .putExtra("Nama", binding.detailNamaPet.text.toString())
                .putExtra("Kategori", binding.detailKategoriPet.text.toString())
                .putExtra("Ras", binding.detailRasPet.text.toString())
                .putExtra("Tanggal Lahir", binding.detailTglLahirPet.text.toString())
                .putExtra("Umur", binding.detailUmurPet.text.toString())
                .putExtra("Image", imageUrl)
                .putExtra("Key", key)
            startActivity(intent)
        }

    }
}