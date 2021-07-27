package com.aexample.test_notifi.DataStore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.aexample.test_notifi.R
import com.aexample.test_notifi.databinding.ActivityDsMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DsMainActivity : AppCompatActivity() {
    lateinit var binding: ActivityDsMainBinding
    lateinit var pref: UserPref
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pref = UserPref(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ds_main)
        
        binding.btnSave.setOnClickListener {
            CoroutineScope(IO).launch {
                pref.addData(
                    binding.etName.text.toString().trim(),
                    binding.etAge.text.toString().toInt()
                )
            }
        }


        pref.getAge.asLiveData().observe(this, Observer {
            binding.tvAge.text = it.toString()
        })

        CoroutineScope(IO).launch {
            pref.getName.collect {
                withContext(Main){
                    binding.tvName.text = it
                }
            }
        }


    }
}