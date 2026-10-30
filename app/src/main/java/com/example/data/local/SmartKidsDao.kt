package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
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
import kotlinx.coroutines.flow.Flow

@Dao
interface SmartKidsDao {

    // Children
    @Query("SELECT * FROM child_profiles ORDER BY id ASC")
    fun getAllChildren(): Flow<List<ChildProfile>>

    @Query("SELECT * FROM child_profiles WHERE id = :childId")
    fun getChildById(childId: Long): Flow<ChildProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChild(child: ChildProfile): Long

    @Update
    suspend fun updateChild(child: ChildProfile)

    @Query("UPDATE child_profiles SET totalStars = totalStars + :starsToAdd WHERE id = :childId")
    suspend fun addStarsToChild(childId: Long, starsToAdd: Int)

    @Query("UPDATE child_profiles SET currentStreakDays = currentStreakDays + 1 WHERE id = :childId")
    suspend fun incrementStreak(childId: Long)

    @Query("DELETE FROM child_profiles WHERE id = :childId")
    suspend fun deleteChild(childId: Long)

    // Books
    @Query("SELECT * FROM books ORDER BY createdAt DESC")
    fun getAllBooks(): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE id = :bookId")
    fun getBookById(bookId: Long): Flow<Book?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book): Long

    @Update
    suspend fun updateBook(book: Book)

    @Query("DELETE FROM books WHERE id = :bookId")
    suspend fun deleteBook(bookId: Long)

    // Pages
    @Query("SELECT * FROM book_pages WHERE bookId = :bookId ORDER BY pageNumber ASC")
    fun getPagesForBook(bookId: Long): Flow<List<BookPage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPages(pages: List<BookPage>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPage(page: BookPage): Long

    @Query("DELETE FROM book_pages WHERE bookId = :bookId")
    suspend fun deletePagesForBook(bookId: Long)

    // Questions
    @Query("SELECT * FROM quiz_questions WHERE bookId = :bookId")
    fun getQuestionsForBook(bookId: Long): Flow<List<QuizQuestion>>

    @Query("SELECT * FROM quiz_questions")
    fun getAllQuestions(): Flow<List<QuizQuestion>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuizQuestion>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: QuizQuestion): Long

    @Query("DELETE FROM quiz_questions WHERE id = :questionId")
    suspend fun deleteQuestion(questionId: Long)

    // Quiz Attempts
    @Query("SELECT * FROM quiz_attempts WHERE childId = :childId ORDER BY timestamp DESC")
    fun getQuizAttemptsForChild(childId: Long): Flow<List<QuizAttempt>>

    @Query("SELECT * FROM quiz_attempts ORDER BY timestamp DESC")
    fun getAllQuizAttempts(): Flow<List<QuizAttempt>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizAttempt(attempt: QuizAttempt): Long

    // Reading Sessions
    @Query("SELECT * FROM reading_sessions WHERE childId = :childId ORDER BY timestamp DESC")
    fun getReadingSessionsForChild(childId: Long): Flow<List<ReadingSession>>

    @Query("SELECT * FROM reading_sessions ORDER BY timestamp DESC")
    fun getAllReadingSessions(): Flow<List<ReadingSession>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReadingSession(session: ReadingSession): Long

    // Screen Time
    @Query("SELECT * FROM screen_time_configs WHERE childId = :childId")
    fun getScreenTimeConfig(childId: Long): Flow<ScreenTimeConfig?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScreenTimeConfig(config: ScreenTimeConfig)

    @Update
    suspend fun updateScreenTimeConfig(config: ScreenTimeConfig)

    // Parent Rewards
    @Query("SELECT * FROM parent_rewards ORDER BY starsCost ASC")
    fun getAllRewards(): Flow<List<ParentReward>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReward(reward: ParentReward): Long

    @Update
    suspend fun updateReward(reward: ParentReward)

    @Query("DELETE FROM parent_rewards WHERE id = :rewardId")
    suspend fun deleteReward(rewardId: Long)

    // Smart Revision System
    @Query("SELECT * FROM revision_items WHERE childId = :childId ORDER BY isMastered ASC, nextRevisionDueTimestamp ASC")
    fun getAllRevisionsForChild(childId: Long): Flow<List<RevisionItem>>

    @Query("SELECT * FROM revision_items WHERE childId = :childId AND isMastered = 0 AND nextRevisionDueTimestamp <= :currentTime ORDER BY nextRevisionDueTimestamp ASC")
    fun getDueRevisionsForChild(childId: Long, currentTime: Long): Flow<List<RevisionItem>>

    @Query("SELECT * FROM revision_items WHERE childId = :childId AND isMastered = 1 ORDER BY lastAttemptTimestamp DESC")
    fun getMasteredRevisionsForChild(childId: Long): Flow<List<RevisionItem>>

    @Query("SELECT * FROM revision_items WHERE childId = :childId AND questionId = :questionId LIMIT 1")
    suspend fun getRevisionByQuestionAndChild(childId: Long, questionId: Long): RevisionItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRevisionItem(item: RevisionItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRevisionItems(items: List<RevisionItem>)

    @Update
    suspend fun updateRevisionItem(item: RevisionItem)

    @Query("DELETE FROM revision_items WHERE id = :id")
    suspend fun deleteRevisionItem(id: Long)

    @Query("DELETE FROM revision_items WHERE childId = :childId AND isMastered = 1")
    suspend fun clearMasteredRevisionsForChild(childId: Long)

    // Vocabulary & Word Bank
    @Query("SELECT * FROM vocabulary_words WHERE childId = :childId ORDER BY dateCollected DESC")
    fun getVocabularyWordsForChild(childId: Long): Flow<List<VocabularyWord>>

    @Query("SELECT * FROM vocabulary_words WHERE childId = :childId AND word = :word LIMIT 1")
    suspend fun getWordByText(childId: Long, word: String): VocabularyWord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVocabularyWord(word: VocabularyWord): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVocabularyWords(words: List<VocabularyWord>)

    @Update
    suspend fun updateVocabularyWord(word: VocabularyWord)

    @Query("DELETE FROM vocabulary_words WHERE id = :id")
    suspend fun deleteVocabularyWord(id: Long)

    // Voice Recordings
    @Query("SELECT * FROM voice_recordings WHERE childId = :childId ORDER BY timestamp DESC")
    fun getVoiceRecordingsForChild(childId: Long): Flow<List<VoiceRecording>>

    @Query("SELECT * FROM voice_recordings ORDER BY timestamp DESC")
    fun getAllVoiceRecordings(): Flow<List<VoiceRecording>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVoiceRecording(recording: VoiceRecording): Long

    @Query("DELETE FROM voice_recordings WHERE id = :id")
    suspend fun deleteVoiceRecording(id: Long)
}
