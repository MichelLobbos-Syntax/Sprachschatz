package de.syntax.androidabschluss.data.models

data class WordResult(
    val word: String,
    val phonetic: String?,
    val meanings: List<Meaning>,
)

data class Meaning(
    val partOfSpeech: String,
    val definitions: List<Definition>,
    val synonyms: List<String>,
    val antonyms: List<Any?>,
)

data class Definition(
    val definition: String,

)