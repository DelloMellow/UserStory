package com.example.userstory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.userstory.databinding.ActivityStoryDetailBinding
import com.squareup.picasso.Picasso

class StoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryDetailBinding
    private lateinit var userPreference: UserPreferences
    private lateinit var userModel: UserModel
    private lateinit var photoUrl: String
    private lateinit var name: String
    private lateinit var description: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreference = UserPreferences(this)
        userModel = userPreference.getUser()

        photoUrl = intent.getStringExtra(EXTRA_PHOTO_URL).toString()
        name = intent.getStringExtra(EXTRA_NAME).toString()
        description = intent.getStringExtra(EXTRA_DESCRIPTION).toString()

        setStoryDetail(photoUrl, name, description)
    }


    private fun setStoryDetail(photoUrl: String, name: String, description: String) {
        Picasso.get().load(photoUrl).into(binding.ivStoryDetail)
        binding.tvNameDetail.text = name
        binding.tvDescDetail.text = description
    }

    companion object {
        const val EXTRA_PHOTO_URL = "photoUrl"
        const val EXTRA_NAME = "name"
        const val EXTRA_DESCRIPTION = "description"
    }
}