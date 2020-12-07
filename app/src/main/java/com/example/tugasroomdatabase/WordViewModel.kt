package com.example.tugasroomdatabase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

//dibutuhkan aplikasi sebagai parameter
class WordViewModel (application: Application) : AndroidViewModel(application){

    //ViewModel mempertahankan repository untuk mendapatkan data
    private val repository: WordRepository

    //LiveData memberikan kata yang diperbarui
    val allWords : LiveData<List<Word>>

    //untuk mendapatkan referensi WordDao dari WordRoomDatabase untuk membuat WordRepository yang benar
    init {
        val wordsDao = WordRoomDatabase.getDatabase(application, viewModelScope).wordDao()
        repository = WordRepository(wordsDao)
        allWords = repository.allWords
    }
    fun insert(word:Word) = viewModelScope.launch {
        repository.insert(word)
    }
    fun deleteALL() = viewModelScope.launch {
        repository.deleteALL()
    } }