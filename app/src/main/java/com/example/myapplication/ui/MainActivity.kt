package com.example.myapplication.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.data.model.Shark
import com.example.myapplication.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel by lazy {
        MainViewModel(Pair(1080f, 1962f))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupViews()
        setupListeners()
        setupObservers()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                Toast.makeText(this, "Search clicked", Toast.LENGTH_SHORT).show()
                true
            }

            R.id.action_settings -> {
                Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show()
                true
            }

            R.id.action_random -> {
                val randomX = Random.nextInt(0, binding.frameLayout.width - 200).toFloat()
                val randomY = Random.nextInt(0, binding.frameLayout.height - 200).toFloat()
                val shark = Shark(position = Pair(randomX, randomY))

                val iv = ImageView(this).apply {
                    id = shark.id.toInt()
                    setBackgroundColor(Color.BLUE)
                    setImageResource(R.drawable.nemo)
                    layoutParams = FrameLayout.LayoutParams(50, 50)
                    x = randomX
                    y = randomY
                }

                binding.root.addView(iv)
                viewModel.addSeaCreature(shark)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupViews() {
        setSupportActionBar(binding.toolbar)
    }

    private fun setupObservers() {
        viewModel.seaCreatures
            .map { it }
            .onEach {
                Log.d("SonLN", "list : $it")
            }
            .launchIn(lifecycleScope)
    }

    private fun setupListeners() {

    }
}