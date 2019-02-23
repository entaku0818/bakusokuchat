package com.entaku.bakusokuchat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.entaku.bakusokuchat.adapters.*
import com.entaku.bakusokuchat.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)

        val binding= DataBindingUtil.setContentView<ActivityMainBinding>(this@MainActivity,R.layout.activity_main)
        val viewModel=ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        binding.contentMain.viewModel =viewModel
        setSupportActionBar(toolbar)
        viewModel.initializeViewModel()
        binding.lifecycleOwner=this

        val adapter= MessageAdapter(viewModel.data)
        recycler_message.adapter=adapter
        recycler_message.layoutManager=LinearLayoutManager(this)
        button_send.setOnClickListener {
            viewModel.sendMessage()
        }
        viewModel.dataChanged.observe(this, Observer {
            adapter.notifyDataSetChanged()
        })
        viewModel.dataInserted.observe(this, Observer{adapter.notifyItemInserted(it)})


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
