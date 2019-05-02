package luc.klara.adventures.models

class DialogueItemViewModel(val story: Story) {
    val id: String
    val image: String
    val text: String
    val choice_one: String
    val choice_two: String

    init {
        id = story.id
        image = story.image
        text = story.text
        choice_one = story.choice_one
        choice_two = story.choice_two
    }
}