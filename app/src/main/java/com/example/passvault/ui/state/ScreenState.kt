package com.example.passvault.ui.state

sealed class ScreenState<T> {
    class PreLoad<T> : ScreenState<T>()
    class Loading<T> : ScreenState<T>()
    class Loaded<T>(val result: T) : ScreenState<T>()
    class Error<T>(val message: T?) : ScreenState<T>()
}