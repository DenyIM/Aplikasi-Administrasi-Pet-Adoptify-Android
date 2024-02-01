package com.example.projectpmobile

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.example.projectpmobile.databinding.ActivityTambahDataPetBinding
import com.google.android.play.core.integrity.e
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.security.Key
import java.text.DateFormat
import java.util.Calendar

class TambahDataPetActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTambahDataPetBinding
    var imageURL: String? = null
    var uri: Uri? = null

    private lateinit var storageRef: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahDataPetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val activityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == RESULT_OK){
                val data = result.data
                uri = data!!.data
                binding.ivTambahDataPet.setImageURI(uri)
            }else{
                Toast.makeText(this@TambahDataPetActivity, "Gambar Tidak Terpilih", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnPilihImagePet.setOnClickListener{
            val photoPicker = Intent(Intent.ACTION_PICK)
            photoPicker.type = "image/*"
            activityResultLauncher.launch(photoPicker)
        }
        binding.btnTambahDataPet.setOnClickListener{
            saveDataPet()
        }
    }

    private fun saveDataPet() {
        storageRef = FirebaseStorage.getInstance().reference.child("Gambar Pet")
            .child(uri!!.lastPathSegment!!)

        val builder = AlertDialog.Builder(this@TambahDataPetActivity)
        builder.setCancelable(false)
        builder.setView(R.layout.progressbar_layout)
        val dialog = builder.create()
        dialog.show()

        storageRef.putFile(uri!!).addOnSuccessListener { taskSnapshot ->
            val uriTask = taskSnapshot.storage.downloadUrl
            while (!uriTask.isComplete);
            val urlImage = uriTask.result
            imageURL = urlImage.toString()
            tambahDataPet()
            dialog.dismiss()
        }.addOnFailureListener(){
            dialog.dismiss()
        }
    }

    private fun tambahDataPet() {
        val namaPet = binding.etNamaPet.text.toString()
        val kategoriPet = binding.etKategoriPet.text.toString()
        val rasPet = binding.etRasPet.text.toString()
        val tglLahirPet = binding.etTglLahirPet.text.toString()
        val umurPet = binding.etUmurPet.text.toString()
        val descPet = binding.etDescPet.text.toString()

        val dataPet = ModelPet(descPet, imageURL, kategoriPet, namaPet, rasPet, tglLahirPet, umurPet)
        val currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().time)

        FirebaseDatabase.getInstance().getReference("Data Pet").child(currentDate)
            .setValue(dataPet).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@TambahDataPetActivity, "Data Tersimpan", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                }
            }.addOnFailureListener{ e ->
                Toast.makeText(this@TambahDataPetActivity, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }
}