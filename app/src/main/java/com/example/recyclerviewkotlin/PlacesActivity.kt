package com.example.recyclerviewkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recyclerviewkotlin.adapters.PlacesAdapter
import com.example.recyclerviewkotlin.databinding.ActivityPlacesBinding
import com.example.recyclerviewkotlin.viewmodels.PlacesViewModel

class PlacesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlacesBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var placesAdapter: PlacesAdapter
    private lateinit var placeViewModel: PlacesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_places)

        binding = ActivityPlacesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.title = getString(R.string.places_list)
        actionBar.setDisplayHomeAsUpEnabled(true)

        placeViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(PlacesViewModel::class.java)

        placeViewModel.getPlacesList().observe(this, Observer {
            placesAdapter.notifyDataSetChanged()
            Toast.makeText(this, "Total items: " + placesAdapter.itemCount, Toast.LENGTH_SHORT).show()
        })

        placeViewModel.getIsPlaceAdded().observe(this, Observer {
            placesAdapter.notifyDataSetChanged()
            Toast.makeText(this, "Total items after adding: " + placesAdapter.itemCount, Toast.LENGTH_SHORT).show()
//            binding.nestedScrollView.smoothScrollTo(0, binding.placesRecyclerView.bottom)
            binding.placesRecyclerView.smoothScrollToPosition(placesAdapter.itemCount-1)
        })

        binding.floatingActionButton.setOnClickListener {
            placeViewModel.addPlaces()
        }

        initPlacesRecyclerViewApply()
        initPlaces()

        Toast.makeText(this, "Start Total items: " + placesAdapter.itemCount, Toast.LENGTH_SHORT).show()


    }



    private fun initializePlacesRecyclerView() {
        linearLayoutManager = LinearLayoutManager(this@PlacesActivity)
        binding.placesRecyclerView.layoutManager = linearLayoutManager
        placesAdapter = PlacesAdapter()
        binding.placesRecyclerView.adapter = placesAdapter

//        gridLayoutManager = GridLayoutManager(this,2)
//        binding.placesRecyclerView.layoutManager = gridLayoutManager

    }

    private fun initPlacesRecyclerViewApply() {
        binding.placesRecyclerView.apply {
//            gridLayoutManager = GridLayoutManager(this@PlacesActivity,2)
//            binding.placesRecyclerView.layoutManager = gridLayoutManager

            linearLayoutManager = LinearLayoutManager(this@PlacesActivity)
            layoutManager = linearLayoutManager

            placesAdapter = PlacesAdapter()
            adapter = placesAdapter
            ViewCompat.setNestedScrollingEnabled(binding.placesRecyclerView, false);
        }
    }

    private fun initPlaces() {
        placeViewModel.getPlacesList().value?.let {
            placesAdapter.submitPlacesList(it)
        }
    }
}