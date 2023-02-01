package com.tutelar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.tutelar.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Tutelar.init(this, "tutpk_live_xi9MWDPkOUOv")
        setUpUi()
    }

    private fun setUpUi() {
        binding.apply {
            getDetailsButton.setOnClickListener {
                detailsTextView.text = Tutelar.getDeviceDetails(this@MainActivity)
            }
        }
    }

}