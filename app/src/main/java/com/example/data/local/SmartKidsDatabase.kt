package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.Book
import com.example.data.model.BookPage
import com.example.data.model.ChildProfile
import com.example.data.model.ParentReward
import com.example.data.model.QuizAttempt
import com.example.data.model.QuizQuestion
import com.example.data.model.ReadingSession
import com.example.data.model.RevisionItem
import com.example.data.model.ScreenTimeConfig
import com.example.data.model.VocabularyWord
import com.example.data.model.VoiceRecording
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ChildProfile::class,
        Book::class,
        BookPage::class,
        QuizQuestion::class,
        QuizAttempt::class,
        ReadingSession::class,
        ScreenTimeConfig::class,
        ParentReward::class,
        RevisionItem::class,
        VocabularyWord::class,
        VoiceRecording::class
    ],
    version = 3,
    exportSchema = false
)
abstract class SmartKidsDatabase : RoomDatabase() {

    abstract fun smartKidsDao(): SmartKidsDao

    companion object {
        @Volatile
        private var INSTANCE: SmartKidsDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): SmartKidsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SmartKidsDatabase::class.java,
                    "smart_kids_database"
                )
                .addCallback(SmartKidsDatabaseCallback(scope))
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class SmartKidsDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.smartKidsDao())
                }
            }
        }

        suspend fun populateDatabase(dao: SmartKidsDao) {
            for (child in PreloadedData.initialChildren()) {
                dao.insertChild(child)
            }
            for (config in PreloadedData.initialScreenTimeConfigs()) {
                dao.insertScreenTimeConfig(config)
            }
            for (reward in PreloadedData.initialRewards()) {
                dao.insertReward(reward)
            }
            for (book in PreloadedData.initialBooks()) {
                dao.insertBook(book)
            }
            dao.insertPages(PreloadedData.initialPages())
            dao.insertQuestions(PreloadedData.initialQuestions())
            dao.insertRevisionItems(PreloadedData.initialRevisionItems())
            dao.insertVocabularyWords(PreloadedData.initialVocabularyWords())
            for (recording in PreloadedData.initialVoiceRecordings()) {
                dao.insertVoiceRecording(recording)
            }

            // Seed initial sample reading sessions for analytics
            dao.insertReadingSession(
                ReadingSession(
                    childId = 1,
                    bookId = 1,
                    bookTitle = "The Brave Little Fox & The Star Compass",
                    durationSeconds = 12 * 60,
                    pagesRead = 4,
                    audioListenedSeconds = 8 * 60
                )
            )
            dao.insertReadingSession(
                ReadingSession(
                    childId = 2,
                    bookId = 2,
                    bookTitle = "Mystery of the Solar System",
                    durationSeconds = 22 * 60,
                    pagesRead = 4,
                    audioListenedSeconds = 18 * 60
                )
            )
            dao.insertQuizAttempt(
                QuizAttempt(
                    childId = 1,
                    bookId = 1,
                    bookTitle = "The Brave Little Fox",
                    chapterTitle = "Chapter 1: The Glowing Spark",
                    scorePercent = 100,
                    totalQuestions = 2,
                    correctCount = 2,
                    timeSpentSeconds = 64
                )
            )
            dao.insertQuizAttempt(
                QuizAttempt(
                    childId = 2,
                    bookId = 2,
                    bookTitle = "Mystery of the Solar System",
                    chapterTitle = "Chapter 2: The Gas Giants",
                    scorePercent = 85,
                    totalQuestions = 3,
                    correctCount = 2,
                    timeSpentSeconds = 110,
                    isRevisionNeeded = true
                )
            )
        }
    }
}
