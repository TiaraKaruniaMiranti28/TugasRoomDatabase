package com.example.tugasroomdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// anotasi kelas menjadi room database dengan tabel word class
@Database (entities = arrayOf(Word::class), version = 1, exportSchema = false)
public abstract class WordRoomDatabase : RoomDatabase(){
    abstract fun wordDao(): WordDao

    private class  WordDataBaseCallBack (
        private val scope: CoroutineScope) : RoomDatabase.Callback(){
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.wordDao())
                }
            }
        }
        suspend fun populateDatabase(wordDao: WordDao){
            //hapus semua konten
            wordDao.deleteALL()

            //contoh kata
            var word = Word("Ji Chang Wook")
            wordDao.insert(word)
            word = Word("Park Seo Joon")
            wordDao.insert(word)
        }
    }

    //singleton mencegah beberapa contoh pembukaan database pada saat yang bersamaan
    companion object {
        @Volatile
        private var INSTANCE: WordRoomDatabase? = null

        fun getDatabase(context: Context,
                        scope: CoroutineScope): WordRoomDatabase {
            //jika INSTANCE bukan null, maka kembalikan, jika ya, maka buat database
            val tempInstance = INSTANCE
            if (tempInstance!=null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext,WordRoomDatabase::class.java,
                    "word_database")
                    .addCallback(WordDataBaseCallBack(scope))
                    .build()
                INSTANCE = instance
                return instance
            } } } }