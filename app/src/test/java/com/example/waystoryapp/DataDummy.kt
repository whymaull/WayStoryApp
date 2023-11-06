package com.example.waystoryapp

import com.example.waystoryapp.data.database.Entities


object DataDummy {

    fun generateDummyQuoteResponse(): List<Entities> {
        val items: MutableList<Entities> = arrayListOf()
        for (i in 0..100) {
            val quote = Entities(
                idStory = "i"
            )
            items.add(quote)
        }
        return items
    }
}