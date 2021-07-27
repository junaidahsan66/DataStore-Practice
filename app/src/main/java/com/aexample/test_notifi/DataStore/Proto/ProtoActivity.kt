package com.aexample.test_notifi.DataStore.Proto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.aexample.test_notifi.R
import com.aexample.test_notifi.databinding.ActivityProtoBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProtoActivity : AppCompatActivity() {
    lateinit var binding: ActivityProtoBinding
    lateinit var userPref: UserProtoPref
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userPref = UserProtoPref(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_proto)
        binding.btnSave.setOnClickListener {
            CoroutineScope(IO).launch {
                userPref.writeToLocal(
                    binding.etName.text.toString().trim(),
                    if (binding.etAge.text.isNullOrBlank())
                        0
                    else
                    (binding.etAge.text?:0) .toString().toInt())
            }
        }

        CoroutineScope(IO).launch {
            userPref.readFromLocal.collect {
                withContext(Main){
                    binding.tvName.text = it.name
                    binding.tvAge.text = it.age.toString()
                }
            }
        }
    }
}