package com.example.myapplication.ui

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.data.model.seacreature.SeaCreatureData
import com.example.myapplication.data.model.seacreature.SeaCreatureType
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.ext.flipIfNeed
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    // 1440f, 2397f
    // 1080f, 1962f
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
        setupObservers()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val bounds = Pair(binding.frameLayout.width, binding.frameLayout.height)

        return when (item.itemId) {
            R.id.action_tini_tuna -> spawnSeaCreature(bounds, SeaCreatureType.TINI_TUNA)
            R.id.action_shark -> spawnSeaCreature(bounds, SeaCreatureType.SHARK)
            R.id.action_turtle -> spawnSeaCreature(bounds, SeaCreatureType.TURTLE)
            R.id.action_jellyfish -> spawnSeaCreature(bounds, SeaCreatureType.JELLYFISH)
            R.id.action_random -> spawnSeaCreature(bounds)

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun spawnSeaCreature(bounds: Pair<Int, Int>,type: SeaCreatureType? = null): Boolean {
        viewModel.createSeaCreature(bounds, type) {
            runOnUiThread {
                updateViewIfNeed(it)
            }
        }
        return true
    }

    private fun updateViewIfNeed(seaCreatureData: SeaCreatureData) {
        val imageView = findViewById<ImageView>(seaCreatureData.id.toInt())

        if (imageView != null) {
            imageView.flipIfNeed(seaCreatureData.velocity.first)
            imageView.layoutParams = FrameLayout.LayoutParams(seaCreatureData.size, seaCreatureData.size)
            imageView.x = seaCreatureData.position.first
            imageView.y = seaCreatureData.position.second
        }
    }

    private fun setupViews() {
        setSupportActionBar(binding.toolbar)
    }

    private fun setupObservers() {
        viewModel.seaCreaturesFlow
            .map { it }
            .onEach { seaCreatures ->
                val seaCreaturesIds = seaCreatures.map { it.id.toInt() }
                val viewToRemove = mutableListOf<View>()

                for (imageView in binding.frameLayout.children) {
                    if (imageView.id !in seaCreaturesIds) {
                        viewToRemove.add(imageView)
                    }
                }

                viewToRemove.forEach { binding.frameLayout.removeView(it) }

                seaCreatures.forEach {
                    val imageView = findViewById<ImageView>(it.id.toInt())
                    if (imageView == null) {
                        binding.frameLayout.addView(createSeaCreatureImageView(it))
                    }
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun createSeaCreatureImageView(seaCreature: SeaCreatureData): ImageView {
        return ImageView(this).apply {
            flipIfNeed(seaCreature.velocity.first)
            layoutParams = FrameLayout.LayoutParams(seaCreature.size, seaCreature.size)

            id = seaCreature.id.toInt()
            setImageResource(seaCreature.image)
            setBackgroundColor(Color.BLUE)
            x = seaCreature.position.first
            y = seaCreature.position.second
        }
    }
}