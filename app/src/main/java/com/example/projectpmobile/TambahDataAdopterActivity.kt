package com.example.projectpmobile

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.example.projectpmobile.databinding.ActivityTambahDataAdopterBinding
import com.example.projectpmobile.databinding.ActivityTambahDataPetBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.DateFormat
import java.util.Calendar

class TambahDataAdopterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTambahDataAdopterBinding
    var imageURL: String? = null
    var uri: Uri? = null

    private lateinit var storageRef: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahDataAdopterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val activityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == RESULT_OK){
                val data = result.data
                uri = data!!.data
                binding.ivTambahDataAdopter.setImageURI(uri)
            }else{
                Toast.makeText(this@TambahDataAdopterActivity, "Gambar Tidak Terpilih", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnPilihImageAdopter.setOnClickListener{
            val photoPicker = Intent(Intent.ACTION_PICK)
            photoPicker.type = "image/*"
            activityResultLauncher.launch(photoPicker)
        }
        binding.btnTambahDataAdopter.setOnClickListener{
            saveDataAdopter()
        }
    }

    private fun saveDataAdopter() {
        storageRef = FirebaseStorage.getInstance().reference.child("Gambar Adopter")
            .child(uri!!.lastPathSegment!!)

        val builder = AlertDialog.Builder(this@TambahDataAdopterActivity)
        builder.setCancelable(false)
        builder.setView(R.layout.progressbar_layout)
        val dialog = builder.create()
        dialog.show()

        storageRef.putFile(uri!!).addOnSuccessListener { taskSnapshot ->
            val uriTask = taskSnapshot.storage.downloadUrl
            while (!uriTask.isComplete);
            val urlImage = uriTask.result
            imageURL = urlImage.toString()
            tambahDataAdopter()
            dialog.dismiss()
        }.addOnFailureListener(){
            dialog.dismiss()
        }
    }

    private fun tambahDataAdopter() {
        val namaAdopter = binding.etNamaAdopter.text.toString()
        val noHpAdopter = binding.etNohpAdopter.text.toString()
        val emailAdopter = binding.etEmailAdopter.text.toString()
        val jenisRumahAdopter = binding.etJenisRumahAdopter.text.toString()
        val alamatAdopter = binding.etAlamatAdopter.text.toString()
        val tujuanAdopter = binding.etTujuanAdopter.text.toString()

        val dataAdopter = ModelAdopter(alamatAdopter, emailAdopter, imageURL, namaAdopter, noHpAdopter, jenisRumahAdopter, tujuanAdopter)
        val currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().time)

        FirebaseDatabase.getInstance().getReference("Data Adopter").child(currentDate)
            .setValue(dataAdopter).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@TambahDataAdopterActivity, "Data Tersimpan", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                }
            }.addOnFailureListener{ e ->
                Toast.makeText(this@TambahDataAdopterActivity, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }
}