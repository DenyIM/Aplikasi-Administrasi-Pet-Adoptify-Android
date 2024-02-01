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

class UpdateAdopterActivity : AppCompatActivity() {
    private lateinit var up_et_nama_adopter: EditText
    private lateinit var up_et_nohp_adopter: EditText
    private lateinit var up_et_email_adopter: EditText
    private lateinit var up_et_jenisRumah_adopter: EditText
    private lateinit var up_et_alamat_adopter: EditText
    private lateinit var up_et_tujuan_adopter: EditText
    private lateinit var up_iv_tambahData_adopter: ImageView

    private lateinit var up_btn_pilihImage_adopter: Button
    private lateinit var up_btn_tambahData_adopter: Button

    private var up_nama_adopter: String = ""
    private var up_nohp_adopter: String = ""
    private var up_email_adopter: String = ""
    private var up_jenisRumah_adopter: String = ""
    private var up_alamat_adopter: String = ""
    private var up_tujuan_adopter: String = ""
    private var imageUrl: String = ""
    private var key: String = ""
    private var oldImageURL: String = ""
    private lateinit var uri: Uri
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_adopter)

        up_et_nama_adopter = findViewById(R.id.up_et_nama_adopter)
        up_et_nohp_adopter = findViewById(R.id.up_et_nohp_adopter)
        up_et_email_adopter = findViewById(R.id.up_et_email_adopter)
        up_et_jenisRumah_adopter = findViewById(R.id.up_et_jenisRumah_adopter)
        up_et_alamat_adopter = findViewById(R.id.up_et_alamat_adopter)
        up_et_tujuan_adopter = findViewById(R.id.up_et_tujuan_adopter)
        up_iv_tambahData_adopter = findViewById(R.id.up_iv_tambahData_adopter)
        up_btn_pilihImage_adopter = findViewById(R.id.up_btn_pilihImage_adopter)
        up_btn_tambahData_adopter = findViewById(R.id.up_btn_tambahData_adopter)

        uri = Uri.EMPTY

        val activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data
                    uri = data?.data!!
                    up_iv_tambahData_adopter.setImageURI(uri)
                } else {
                    Toast.makeText(this@UpdateAdopterActivity, "No Image Selected", Toast.LENGTH_SHORT).show()
                }
            }

        val bundle = intent.extras
        if (bundle != null) {
            Glide.with(this@UpdateAdopterActivity).load(bundle.getString("Image")).into(up_iv_tambahData_adopter)
            up_et_nama_adopter.setText(bundle.getString("Nama"))
            up_et_nohp_adopter.setText(bundle.getString("No Hp"))
            up_et_email_adopter.setText(bundle.getString("Email"))
            up_et_jenisRumah_adopter.setText(bundle.getString("Jenis Tempat Tinggal"))
            up_et_alamat_adopter.setText(bundle.getString("Alamat"))
            up_et_tujuan_adopter.setText(bundle.getString("Tujuan Adopsi"))
            key = bundle.getString("Key")!!
            oldImageURL = bundle.getString("Image")!!
        }

        databaseReference = FirebaseDatabase.getInstance("https://tugas-proyek-pmobile-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Data Adopter").child(key)

        up_btn_pilihImage_adopter.setOnClickListener {
            val photoPicker = Intent(Intent.ACTION_PICK)
            photoPicker.type = "image/*"
            activityResultLauncher.launch(photoPicker)
        }

        up_btn_tambahData_adopter.setOnClickListener {
            saveData()
            val intent = Intent(this@UpdateAdopterActivity, DaftarDataAdopterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun saveData() {
        if (uri == Uri.EMPTY) {
            Toast.makeText(this@UpdateAdopterActivity, "Select an image first", Toast.LENGTH_SHORT).show()
            return
        }

        storageReference = FirebaseStorage.getInstance("gs://tugas-proyek-pmobile.appspot.com").getReference().child("Gambar Adopter")
            .child(uri.lastPathSegment!!)

        val builder = AlertDialog.Builder(this@UpdateAdopterActivity)
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
        up_nama_adopter = up_et_nama_adopter.text.toString().trim { it <= ' ' }
        up_nohp_adopter = up_et_nohp_adopter.text.toString().trim { it <= ' ' }
        up_email_adopter = up_et_email_adopter.text.toString().trim { it <= ' ' }
        up_jenisRumah_adopter = up_et_jenisRumah_adopter.text.toString().trim { it <= ' ' }
        up_alamat_adopter = up_et_alamat_adopter.text.toString().trim { it <= ' ' }
        up_tujuan_adopter = up_et_tujuan_adopter.text.toString()

        val dataClass = ModelAdopter(up_alamat_adopter, up_email_adopter, imageUrl, up_nama_adopter, up_nohp_adopter, up_jenisRumah_adopter, up_tujuan_adopter)

        databaseReference.setValue(dataClass).addOnCompleteListener(OnCompleteListener {
            if (it.isSuccessful) {
                val reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURL)
                reference.delete()
                Toast.makeText(this@UpdateAdopterActivity, "Data sudah ter update!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }).addOnFailureListener(OnFailureListener { e ->
            Toast.makeText(this@UpdateAdopterActivity, e.message.toString(), Toast.LENGTH_SHORT).show()
        })
    }
}