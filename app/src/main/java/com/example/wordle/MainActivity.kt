package com.example.wordle

import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var etGuess: EditText
    private lateinit var btnAction: Button
    private lateinit var tvAnswer: TextView

    private lateinit var guessViews: List<TextView>
    private lateinit var checkViews: List<TextView>

    private var wordToGuess: String = ""
    private var guessCount = 0
    private val maxGuesses = 3
    private var gameOver = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val root = findViewById<View>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, systemBars.top, 0, systemBars.bottom)
            insets
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        etGuess = findViewById(R.id.etGuess)
        btnAction = findViewById(R.id.btnAction)
        tvAnswer = findViewById(R.id.tvAnswer)

        guessViews = listOf(
            findViewById(R.id.tvGuess1),
            findViewById(R.id.tvGuess2),
            findViewById(R.id.tvGuess3)
        )

        checkViews = listOf(
            findViewById(R.id.tvCheck1),
            findViewById(R.id.tvCheck2),
            findViewById(R.id.tvCheck3)
        )

        startGame()

        btnAction.setOnClickListener {
            if (gameOver) {
                startGame()
            } else {
                submitGuess()
            }
        }
    }

    private fun startGame() {
        wordToGuess = FourLetterWordList.getRandomFourLetterWord()
        guessCount = 0
        gameOver = false

        for (i in 0 until maxGuesses) {
            guessViews[i].text = "----"
            checkViews[i].text = "----"
        }

        tvAnswer.text = "----"
        tvAnswer.visibility = View.INVISIBLE

        etGuess.setText("")
        etGuess.isEnabled = true
        etGuess.requestFocus()

        btnAction.text = "GUESS!"
    }

    private fun submitGuess() {
        if (guessCount >= maxGuesses) {
            endGame()
            return
        }

        val guess = etGuess.text.toString().trim().uppercase()

        if (guess.length != 4) {
            Toast.makeText(this, "Guess must be exactly 4 letters.", Toast.LENGTH_SHORT).show()
            return
        }

        if (!guess.all { it in 'A'..'Z' }) {
            Toast.makeText(this, "Guess must only contain letters A-Z.", Toast.LENGTH_SHORT).show()
            return
        }

        val correctness = checkGuess(guess)

        guessViews[guessCount].text = guess
        checkViews[guessCount].setText(colorizeGuess(guess, correctness), TextView.BufferType.SPANNABLE)

        guessCount++

        etGuess.setText("")
        hideKeyboard()

        if (guess == wordToGuess || guessCount == maxGuesses) {
            endGame()
        }
    }

    private fun endGame() {
        gameOver = true
        tvAnswer.text = wordToGuess
        tvAnswer.visibility = View.VISIBLE
        etGuess.isEnabled = false
        btnAction.text = "RESTART"
    }

    private fun checkGuess(guess: String): String {
        var result = ""
        for (i in 0..3) {
            result += when {
                guess[i] == wordToGuess[i] -> "O"
                guess[i] in wordToGuess -> "+"
                else -> "X"
            }
        }
        return result
    }

    private fun colorizeGuess(guess: String, correctness: String): SpannableString {
        val spannable = SpannableString(guess)

        val green = ContextCompat.getColor(this, android.R.color.holo_green_light)
        val red = ContextCompat.getColor(this, android.R.color.holo_red_dark)
        val gray = ContextCompat.getColor(this, android.R.color.darker_gray)

        for (i in 0..3) {
            val color = when (correctness[i]) {
                'O' -> green
                '+' -> red
                else -> gray
            }
            spannable.setSpan(
                ForegroundColorSpan(color),
                i,
                i + 1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        return spannable
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(etGuess.windowToken, 0)
    }
}
