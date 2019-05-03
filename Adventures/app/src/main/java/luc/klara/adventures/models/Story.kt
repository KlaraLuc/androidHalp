package luc.klara.adventures.models

data class Story (
    val id: String,
    val image: String,
    val text: String,
    val choice_one: String,
    val choice_two: String,
    val display: String
)
