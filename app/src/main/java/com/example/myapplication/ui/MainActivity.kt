package com.example.myapplication.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
        setupObservers()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val (boundX, boundY) = Pair(binding.frameLayout.width, binding.frameLayout.height)

        return when (item.itemId) {
            R.id.action_tini_tuna -> {
                viewModel.createSpecificSeaCreature(Pair(boundX, boundY), SeaCreatureType.TINI_TUNA)
                true
            }

            R.id.action_shark -> {
                viewModel.createSpecificSeaCreature(Pair(boundX, boundY), SeaCreatureType.SHARK)
                true
            }

            R.id.action_turtle -> {
                viewModel.createSpecificSeaCreature(Pair(boundX, boundY), SeaCreatureType.TURTLE)
                true
            }

            R.id.action_jellyfish -> {
                viewModel.createSpecificSeaCreature(Pair(boundX, boundY), SeaCreatureType.JELLYFISH)
                true
            }

            R.id.action_random -> {
                viewModel.createRandomSeaCreature(Pair(boundX, boundY))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun createSeaCreatureImageView(seaCreature: SeaCreatureData): ImageView {
        return ImageView(this).apply {
            flipIfNeed(seaCreature.velocity.first)
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
                binding.frameLayout.removeAllViews()

                seaCreatures.forEach { seaCreature ->
                    val imageView = createSeaCreatureImageView(seaCreature)
                    binding.frameLayout.addView(imageView)
                }
            }
            .launchIn(lifecycleScope)
    }
}