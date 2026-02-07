package com.example.spaceflightnewsapp.data.model

@kotlinx.serialization.Serializable
data class InfoResponse(
    val news_sites: List<String>
)