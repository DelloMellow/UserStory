package com.example.userstory

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.userstory.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userPreference: UserPreferences
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreference = UserPreferences(this)
        userModel = userPreference.getUser()

        binding.rvUserStory.layoutManager = LinearLayoutManager(this)

        showLoading(true)
        showStoryList()

        binding.fabAddStory.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.location -> {
                val toLocation = Intent(this, LocationActivity::class.java)
                startActivity(toLocation)
                return true
            }

            R.id.logout -> {
                deleteSession()
                val logout = Intent(this, LoginActivity::class.java)
                startActivity(logout)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteSession() {
        val mUserPreference = UserPreferences(this)
        userModel.token = null
        userModel.isLoggedIn = false
        mUserPreference.setUser(userModel)
    }

    private fun showStoryList() {
        val adapter = UserStoryAdapter()
        binding.rvUserStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        mainViewModel.story.observe(this) {
            adapter.submitData(lifecycle, it)
        }
        showLoading(false)
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        lateinit var userModel: UserModel
    }
}