package com.appbest.moodmatch.repository

import com.appbest.moodmatch.model.Genre

class GenreRepository {
    fun getGenres(): List<Genre> {
        return listOf(
            Genre("Rock"),
            Genre("Pop"),
            Genre("Classical"),
            Genre("Jazz"),
            Genre("Hip Hop"),
            Genre("Country"),
            Genre("Blues"),
            Genre("Electronic"),
            Genre("Reggae"),
            Genre("Folk")
        )
    }
}
