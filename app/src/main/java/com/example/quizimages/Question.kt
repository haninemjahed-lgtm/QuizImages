package com.example.quizimages

// Représente une question du quiz
data class Question(
    val texte: String,
    val imageRes: Int,
    val choix: Array<String>,
    val bonneReponse: Int,
    val descriptionImage: String
)