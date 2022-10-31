package com.example.tmdb.ui.activities.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.example.tmdb.R
import com.example.tmdb.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val queryHint = "Search Movie by Name"
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        setUpNavControllerWithToolBar()
        funOnDestinationChangedListener()

    }

    private fun setUpNavControllerWithToolBar() {
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setSupportActionBar(binding.toolbarLayout.materialToolbar)
        binding.toolbarLayout.materialToolbar.setupWithNavController(
            navController,
            appBarConfiguration
        )

    }

    private fun funOnDestinationChangedListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.movieFragment) {
                binding.toolbarLayout.materialToolbar.menu.setGroupVisible(R.id.main_menu_grp, true)
            } else {
                binding.toolbarLayout.materialToolbar.menu.setGroupVisible(
                    R.id.main_menu_grp,
                    false
                )
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

        val searchItem: MenuItem? = menu?.findItem(R.id.search_menu)
        val searchView: SearchView = searchItem?.actionView as SearchView

        setUpSearchViewUI(searchView)
        searchQueryListener(searchView)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    private fun setUpSearchViewUI(searchView: SearchView) {
        searchView.queryHint = queryHint
        searchView.maxWidth = Int.MAX_VALUE
        val text: TextView = searchView.findViewById(androidx.appcompat.R.id.search_src_text)

        ContextCompat.getColor(this, R.color.white).let {
            text.setTextColor(it)
            text.setHintTextColor(it)
        }

        val searchPlate: View = searchView.findViewById(androidx.appcompat.R.id.search_plate)
        searchPlate.setBackgroundColor(resources.getColor(R.color.purple_500, null))


    }

    private fun searchQueryListener(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                mainViewModel.queryLiveData.postValue(newText ?: "")
                return true
            }

        })
    }

}
