package com.example.audio

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class SmartKidsTTSManager(context: Context) {

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentSentenceIndex = MutableStateFlow(0)
    val currentSentenceIndex: StateFlow<Int> = _currentSentenceIndex.asStateFlow()

    private val _speechRate = MutableStateFlow(1.0f)
    val speechRate: StateFlow<Float> = _speechRate.asStateFlow()

    private val _currentLanguage = MutableStateFlow("en")
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

    private var sentences: List<String> = emptyList()
    var onPageCompleteCallback: (() -> Unit)? = null

    init {
        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isInitialized = true
                tts?.language = Locale.US
                tts?.setPitch(1.15f) // Slightly higher, friendly child-suited pitch
                setupUtteranceListener()
            } else {
                Log.e("SmartKidsTTS", "TTS Initialization failed")
            }
        }
    }

    private fun setupUtteranceListener() {
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                _isPlaying.value = true
            }

            override fun onDone(utteranceId: String?) {
                if (utteranceId?.startsWith("sentence_") == true) {
                    val nextIndex = _currentSentenceIndex.value + 1
                    if (nextIndex < sentences.size) {
                        _currentSentenceIndex.value = nextIndex
                        speakSentence(nextIndex)
                    } else {
                        _isPlaying.value = false
                        onPageCompleteCallback?.invoke()
                    }
                } else if (utteranceId == "single_word") {
                    _isPlaying.value = false
                }
            }

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) {
                _isPlaying.value = false
            }

            override fun onError(utteranceId: String?, errorCode: Int) {
                _isPlaying.value = false
            }
        })
    }

    fun preparePage(textContent: String, languageCode: String = "en") {
        stop()
        _currentLanguage.value = languageCode
        updateLanguage(languageCode)

        // Split by standard English punct (. ! ?) and Bengali dari (।)
        val rawSentences = textContent.split(Regex("(?<=[.!?।])\\s+"))
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        sentences = if (rawSentences.isNotEmpty()) rawSentences else listOf(textContent)
        _currentSentenceIndex.value = 0
    }

    private fun updateLanguage(lang: String) {
        if (!isInitialized) return
        val locale = if (lang == "bn") Locale("bn", "BD") else Locale.US
        val result = tts?.setLanguage(locale)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            tts?.language = Locale.US
        }
    }

    fun play() {
        if (sentences.isEmpty()) return
        _isPlaying.value = true
        speakSentence(_currentSentenceIndex.value)
    }

    fun pause() {
        _isPlaying.value = false
        tts?.stop()
    }

    fun stop() {
        _isPlaying.value = false
        tts?.stop()
        _currentSentenceIndex.value = 0
    }

    private fun speakSentence(index: Int) {
        if (!isInitialized || index !in sentences.indices) return
        val text = sentences[index]
        tts?.setSpeechRate(_speechRate.value)
        val params = android.os.Bundle()
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "sentence_$index")
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, params, "sentence_$index")
    }

    fun repeatCurrentSentence() {
        speakSentence(_currentSentenceIndex.value)
    }

    fun nextSentence() {
        if (_currentSentenceIndex.value < sentences.size - 1) {
            _currentSentenceIndex.value++
            if (_isPlaying.value) {
                speakSentence(_currentSentenceIndex.value)
            }
        }
    }

    fun previousSentence() {
        if (_currentSentenceIndex.value > 0) {
            _currentSentenceIndex.value--
            if (_isPlaying.value) {
                speakSentence(_currentSentenceIndex.value)
            }
        }
    }

    fun setSpeed(rate: Float) {
        _speechRate.value = rate
        tts?.setSpeechRate(rate)
    }

    fun speakWord(word: String) {
        if (!isInitialized) return
        val params = android.os.Bundle()
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "single_word")
        tts?.setSpeechRate(0.85f)
        tts?.speak(word, TextToSpeech.QUEUE_FLUSH, params, "single_word")
    }

    fun speak(text: String) {
        speakWord(text)
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
    }
}
