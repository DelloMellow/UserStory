package com.example.userstory

object DataDummy {
    fun generateDummyListItemStory(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                "photoUrl $i",
                "created_at $i",
                "name $i",
                "description $i",
                i.toFloat(),
                i.toString(),
                i.toFloat(),
            )
            items.add(story)
        }
        return items
    }
}