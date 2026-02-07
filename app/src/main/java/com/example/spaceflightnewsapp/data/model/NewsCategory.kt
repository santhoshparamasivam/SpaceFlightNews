package com.example.spaceflightnewsapp.data.model

enum class NewsCategory(val keywords: List<String>) {
    ALL(emptyList()),
    EDUCATION(listOf("education", "school", "college", "student")),
    WORLD(listOf("world", "global", "international")),
    SCIENCE(listOf("science", "research", "space")),
    SPORTS(listOf("sport", "football", "match", "cricket"))
}
