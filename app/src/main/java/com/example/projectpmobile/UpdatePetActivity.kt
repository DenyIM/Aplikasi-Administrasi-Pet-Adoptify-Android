package com.example.projectpmobile

import android.app.Activity
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
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class UpdatePetActivity : AppCompatActivity() {
    private lateinit var up_et_nama_pet: EditText
    private lateinit var up_et_kategori_pet: EditText
    private lateinit var up_et_ras_pet: EditText
    private lateinit var up_et_tglLahir_pet: EditText
    private lateinit var up_et_umur_pet: EditText
    private lateinit var up_et_desc_pet: EditText
    private lateinit var up_iv_tambahData_pet: ImageView

    private lateinit var up_btn_pilihImage_pet: Button
    private lateinit var up_btn_tambahData_pet: Button

    private var up_nama_pet: String = ""
    private var up_kategori_pet: String = ""
    private var up_ras_pet: String = ""
    private var up_tglLahir_pet: String = ""
    private var up_umur_pet: String = ""
    private var up_desc_pet: String = ""
    private var imageUrl: String = ""
    private var key: String = ""
    private var oldImageURL: String = ""
    private lateinit var uri: Uri
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_pet)

        up_et_nama_pet = findViewById(R.id.up_et_nama_pet)
        up_et_kategori_pet = findViewById(R.id.up_et_kategori_pet)
        up_et_ras_pet = findViewById(R.id.up_et_ras_pet)
        up_et_tglLahir_pet = findViewById(R.id.up_et_tglLahir_pet)
        up_et_umur_pet = findViewById(R.id.up_et_umur_pet)
        up_et_desc_pet = findViewById(R.id.up_et_desc_pet)
        up_iv_tambahData_pet = findViewById(R.id.up_iv_tambahData_pet)
        up_btn_pilihImage_pet = findViewById(R.id.up_btn_pilihImage_pet)
        up_btn_tambahData_pet = findViewById(R.id.up_btn_tambahData_pet)

        uri = Uri.EMPTY

        val activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data
                    uri = data?.data!!
                    up_iv_tambahData_pet.setImageURI(uri)
                } else {
                    Toast.makeText(this@UpdatePetActivity, "No Image Selected", Toast.LENGTH_SHORT).show()
                }
            }

        val bundle = intent.extras
        if (bundle != null) {
            Glide.with(this@UpdatePetActivity).load(bundle.getString("Image")).into(up_iv_tambahData_pet)
            up_et_nama_pet.setText(bundle.getString("Nama"))
            up_et_kategori_pet.setText(bundle.getString("Kategori"))
            up_et_ras_pet.setText(bundle.getString("Ras"))
            up_et_tglLahir_pet.setText(bundle.getString("Tanggal Lahir"))
            up_et_umur_pet.setText(bundle.getString("Umur"))
            up_et_desc_pet.setText(bundle.getString("Description"))
            key = bundle.getString("Key")!!
            oldImageURL = bundle.getString("Image")!!
        }

        databaseReference = FirebaseDatabase.getInstance("https://tugas-proyek-pmobile-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Data Pet").child(key)

        up_btn_pilihImage_pet.setOnClickListener {
            val photoPicker = Intent(Intent.ACTION_PICK)
            photoPicker.type = "image/*"
            activityResultLauncher.launch(photoPicker)
        }

        up_btn_tambahData_pet.setOnClickListener {
            saveData()
            val intent = Intent(this@UpdatePetActivity, DaftarDataPetActivity::class.java)
            startActivity(intent)
        }
    }

    private fun saveData() {
        if (uri == Uri.EMPTY) {
            Toast.makeText(this@UpdatePetActivity, "Select an image first", Toast.LENGTH_SHORT).show()
            return
        }

        storageReference = FirebaseStorage.getInstance("gs://tugas-proyek-pmobile.appspot.com").getReference().child("Gambar Pet")
            .child(uri.lastPathSegment!!)

        val builder = AlertDialog.Builder(this@UpdatePetActivity)
        builder.setCancelable(false)
        builder.setView(R.layout.progressbar_layout)
        val dialog = builder.create()
        dialog.show()

        storageReference.putFile(uri).addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
            val uriTask = taskSnapshot.storage.downloadUrl
            while (!uriTask.isComplete);
            val urlImage = uriTask.result
            imageUrl = urlImage.toString()
            updateData()
            dialog.dismiss()
        }).addOnFailureListener(OnFailureListener { e ->
            dialog.dismiss()
        })
    }

    private fun updateData() {
        up_nama_pet = up_et_nama_pet.text.toString().trim { it <= ' ' }
        up_kategori_pet = up_et_kategori_pet.text.toString().trim { it <= ' ' }
        up_ras_pet = up_et_ras_pet.text.toString().trim { it <= ' ' }
        up_tglLahir_pet = up_et_tglLahir_pet.text.toString().trim { it <= ' ' }
        up_umur_pet = up_et_umur_pet.text.toString().trim { it <= ' ' }
        up_desc_pet = up_et_desc_pet.text.toString()

        val dataClass = ModelPet(up_desc_pet, imageUrl, up_kategori_pet, up_nama_pet, up_ras_pet, up_tglLahir_pet, up_umur_pet)

        databaseReference.setValue(dataClass).addOnCompleteListener(OnCompleteListener {
            if (it.isSuccessful) {
                val reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURL)
                reference.delete()
                Toast.makeText(this@UpdatePetActivity, "Data sudah ter update!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }).addOnFailureListener(OnFailureListener { e ->
            Toast.makeText(this@UpdatePetActivity, e.message.toString(), Toast.LENGTH_SHORT).show()
        })
    }
}