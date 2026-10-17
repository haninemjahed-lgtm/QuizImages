package com.example.quizimages

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.quizimages.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Les 5 questions du quiz (statiques)
    private val questions = arrayOf(
        Question(
            "Combien de côtés a cette forme ?",
            R.drawable.triangle,
            arrayOf("3", "4", "5"),
            0,
            "Un triangle rouge"
        ),
        Question(
            "Quel est le nom de cette forme ?",
            R.drawable.carre,
            arrayOf("Rectangle", "Carré", "Losange"),
            1,
            "Un carré bleu"
        ),
        Question(
            "Cette forme n'a aucun angle. Quelle est-elle ?",
            R.drawable.cercle,
            arrayOf("Ovale", "Hexagone", "Cercle"),
            2,
            "Un cercle vert"
        ),
        Question(
            "Combien de pointes a cette étoile ?",
            R.drawable.etoile,
            arrayOf("6", "5", "4"),
            1,
            "Une étoile jaune"
        ),
        Question(
            "Quelle est cette forme ?",
            R.drawable.rectangle,
            arrayOf("Rectangle", "Cercle", "Triangle"),
            0,
            "Un rectangle violet"
        )
    )

    // État du jeu
    private var indexQuestion = 0      // question courante
    private var score = 0              // nombre de bonnes réponses
    private var aRepondu = false       // a-t-on déjà répondu à cette question ?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Clic sur chaque choix : on transmet l'indice (0, 1 ou 2)
        binding.btnChoix1.setOnClickListener { verifierReponse(0) }
        binding.btnChoix2.setOnClickListener { verifierReponse(1) }
        binding.btnChoix3.setOnClickListener { verifierReponse(2) }

        binding.btnSuivant.setOnClickListener { questionSuivante() }
        binding.btnRejouer.setOnClickListener { rejouer() }

        afficherQuestion()
    }

    // Affiche la question courante
    private fun afficherQuestion() {
        val q = questions[indexQuestion]
        aRepondu = false

        binding.txtNumero.text = "Question ${indexQuestion + 1} sur ${questions.size}"
        binding.imgQuestion.setImageResource(q.imageRes)
        binding.imgQuestion.contentDescription = q.descriptionImage
        binding.txtQuestion.text = q.texte

        binding.btnChoix1.text = q.choix[0]
        binding.btnChoix2.text = q.choix[1]
        binding.btnChoix3.text = q.choix[2]
        activerChoix(true)

        binding.txtRetour.text = ""
        binding.btnSuivant.visibility = View.GONE
        majBandeau()
    }

    // Vérifie la réponse choisie
    private fun verifierReponse(choisi: Int) {
        if (aRepondu) return          // empêche un 2e point
        aRepondu = true
        activerChoix(false)

        val q = questions[indexQuestion]
        if (choisi == q.bonneReponse) {
            score++
            binding.txtRetour.text = "✔ Bonne réponse !"
            binding.txtRetour.setTextColor(Color.parseColor("#2E7D32"))
        } else {
            binding.txtRetour.text =
                "✘ Faux. La bonne réponse : ${q.choix[q.bonneReponse]}"
            binding.txtRetour.setTextColor(Color.parseColor("#C62828"))
        }

        majBandeau()

        // Dernière question ? bouton "Voir le résultat"
        binding.btnSuivant.text =
            if (indexQuestion == questions.size - 1) getString(R.string.voir_resultat)
            else getString(R.string.question_suivante)
        binding.btnSuivant.visibility = View.VISIBLE
    }

    // Passe à la question suivante ou affiche le résultat
    private fun questionSuivante() {
        if (indexQuestion < questions.size - 1) {
            indexQuestion++
            afficherQuestion()
        } else {
            afficherResultat()
        }
    }

    // Score + progression (progression = nombre de réponses données)
    private fun majBandeau() {
        val reponsesDonnees = if (aRepondu) indexQuestion + 1 else indexQuestion
        binding.txtScore.text = "Score : $score/${questions.size}"
        binding.progressQuiz.max = questions.size
        binding.progressQuiz.progress = reponsesDonnees
    }

    // Écran final
    private fun afficherResultat() {
        binding.zoneChoix.visibility = View.GONE
        binding.imgQuestion.visibility = View.GONE
        binding.txtQuestion.visibility = View.GONE
        binding.txtRetour.visibility = View.GONE
        binding.btnSuivant.visibility = View.GONE

        binding.txtNumero.text = "Quiz terminé"
        binding.txtResultatFinal.text = "Score final : $score/${questions.size}"
        binding.txtResultatFinal.visibility = View.VISIBLE
        binding.btnRejouer.visibility = View.VISIBLE
    }

    // Réinitialise tout
    private fun rejouer() {
        indexQuestion = 0
        score = 0

        binding.zoneChoix.visibility = View.VISIBLE
        binding.imgQuestion.visibility = View.VISIBLE
        binding.txtQuestion.visibility = View.VISIBLE
        binding.txtRetour.visibility = View.VISIBLE
        binding.txtResultatFinal.visibility = View.GONE
        binding.btnRejouer.visibility = View.GONE

        afficherQuestion()
    }

    private fun activerChoix(actif: Boolean) {
        binding.btnChoix1.isEnabled = actif
        binding.btnChoix2.isEnabled = actif
        binding.btnChoix3.isEnabled = actif
    }
}