package com.mirkamol.pinterestclonemyproject.model.homephoto

data class Sponsorship(
    val impression_urls: List<String>,
    val sponsor: Sponsor,
    val tagline: String,
    val tagline_url: String
)