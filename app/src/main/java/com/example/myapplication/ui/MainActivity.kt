package com.example.myapplication.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.data.model.SeaCreature
import com.example.myapplication.data.model.SeaCreatureData
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.ext.flip
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel by lazy {
        MainViewModel(Pair(1440f, 2397f))
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
            R.id.action_tini_tuna -> {
                val (randomX, randomY) = generateRandomPosition()
                viewModel.createSpecificSeaCreature(Pair(randomX, randomY))
                true
            }

            R.id.action_random -> {
                addRandomSeaCreature()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addRandomSeaCreature() {
        val (randomX, randomY) = generateRandomPosition()
        viewModel.createRandomSeaCreature(Pair(randomX, randomY))
    }

    private fun generateRandomPosition(): Pair<Float, Float> {
        val x = Random.nextInt(0, binding.frameLayout.width - 200).toFloat()
        val y = Random.nextInt(0, binding.frameLayout.height - 200).toFloat()

        return Pair(x, y)
    }

    private fun createSeaCreatureImageView(seaCreature: SeaCreatureData): ImageView {
        return ImageView(this).apply {
            id = seaCreature.id.toInt()
            setImageResource(seaCreature.image)
            layoutParams = FrameLayout.LayoutParams(seaCreature.size, seaCreature.size)
            x = seaCreature.position.first
            y = seaCreature.position.second
        }
    }

    private fun setupViews() {
        setSupportActionBar(binding.toolbar)
    }

    private fun setupObservers() {
        viewModel.uiState
            .map { it.seaCreatures }
            .onEach { seaCreatures ->
                Log.d("SonLN", "setupObservers: $seaCreatures")
                binding.frameLayout.removeAllViews()

                seaCreatures.forEach { seaCreature ->
                    val imageView = ImageView(this@MainActivity).apply {
                        id = seaCreature.id.toInt()
                        setImageResource(seaCreature.image)
                        layoutParams = FrameLayout.LayoutParams(seaCreature.size, seaCreature.size)
                        x = seaCreature.position.first
                        y = seaCreature.position.second
                    }
                    binding.frameLayout.addView(imageView)
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun setupListeners() {

    }
}