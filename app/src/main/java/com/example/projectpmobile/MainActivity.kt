package com.example.projectpmobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.example.projectpmobile.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var btn_pet_menu: ImageButton
    private lateinit var btn_adopter_menu: ImageButton
    private lateinit var btn_maps_menu: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btn_pet_menu = findViewById(R.id.ib_pet_menu)
        btn_pet_menu.setOnClickListener{
            val intent = Intent(this, DaftarDataPetActivity::class.java)
            startActivity(intent)
        }

        btn_adopter_menu = findViewById(R.id.ib_adopter_menu)
        btn_adopter_menu.setOnClickListener{
            val intent = Intent(this, DaftarDataAdopterActivity::class.java)
            startActivity(intent)
        }

        btn_maps_menu = findViewById(R.id.ib_maps)
        btn_maps_menu.setOnClickListener{
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
    }
}