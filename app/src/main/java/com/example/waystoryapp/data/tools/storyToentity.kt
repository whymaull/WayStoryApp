package com.example.waystoryapp.data.tools

import com.example.waystoryapp.data.datamodel.Story
import com.example.waystoryapp.data.local.entity.StoryEntity

fun storyToentity(story: Story): StoryEntity {
    return StoryEntity(
        id = story.id,
        photoUrl = story.photoUrl
    )
}