package com.example.userstory.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.userstory.ApiService
import com.example.userstory.ListStoryItem
import com.example.userstory.database.StoryDatabase

class StoryRepository(
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService
) {
    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.userStoryDao().getAllStory()
            }
        ).liveData
    }
}