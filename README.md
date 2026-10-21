# codepath-wordle

A simple Wordle-style guessing game for Android with 4-letter words and three guesses. I built it for CodePath's Android course.

![Walkthrough of a game](https://github.com/user-attachments/assets/2e2d853e-5396-489a-b114-5f51a8958d6d)

## What it does

- The app picks a random 4-letter word from a built-in list.
- You get three guesses. Each guess must be exactly 4 letters, A to Z.
- After each guess the app shows it with colored letters. Green means the right letter in the right spot. Red means the letter is in the word but in a different spot. Gray means it isn't in the word.
- The game ends when you guess the word or use all three guesses. Then it shows the answer, and the button changes to Restart for a new word.

## Tech stack

Kotlin, Android SDK (min SDK 24).

## How to run it

1. Clone the repo and open it in Android Studio.
2. Let Gradle sync, then run the `app` configuration on an emulator or a device.

## Limitations

Guesses aren't checked against a dictionary, so any four letters count as a guess.
