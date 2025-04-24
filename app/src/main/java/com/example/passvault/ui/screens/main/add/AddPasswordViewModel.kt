package com.example.passvault.ui.screens.main.add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AddPasswordViewModel : ViewModel() {

    var title by mutableStateOf("")
        private set
    var username by mutableStateOf("") //optional
        private set
    var password by mutableStateOf("")
        private set
    var showPassword by mutableStateOf(false)
        private set
    var website by mutableStateOf("") //optional
        private set
    var notes by mutableStateOf("") //optional
        private set

    fun onTitleChange(title: String) {
        this.title = title
    }

    fun onUsernameChange(username: String) {
        this.username = username
    }

    fun onPasswordChange(password: String) {
        this.password = password
    }

    fun togglePasswordVisibility() {
        showPassword = !showPassword
    }

    fun onWebsiteChange(website: String) {
        this.website = website
    }

    fun onNotesChange(notes: String) {
        this.notes = notes
    }

    var titleError by mutableStateOf("")
        private set

    var passwordError by mutableStateOf("")
        private set

    private fun areInputsValid(): Boolean {
        titleError = if (title.trim().isBlank()) "Title is required" else ""
        passwordError = if (password.trim().isBlank()) "Password is required" else ""
        return titleError.isBlank()
    }

    fun createPassword() {
        if (!areInputsValid()) return
    }
}