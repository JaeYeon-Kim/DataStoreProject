package com.example.datastoreproject

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.example.datastoreproject.application.MainApplication
import com.example.datastoreproject.application.MainApplication.Companion.dataStore
import com.example.datastoreproject.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var application : MainApplication

    // 임의의 key 값을 설정
    private val stringKey = "key"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 코루틴에서 save 메소드 작동
        binding.btnSaveText.setOnClickListener {
            lifecycleScope.launch {
                save(binding.etMainText.text.toString())
            }
        }

        // 데이터 불러오기
        lifecycleScope.launch {
           load().collect {
               binding.tvShowText.text = it
           }
        }
    }

    /**
     * 데이터 저장
     * DataStore의 작업은 비동기로 코루틴에서 작동하므로 suspend 키워드로 선언
     * DataStore에서 사용하는 키의 타입은 PreferencesKey("Key 이름")의 형태로 선언해야 한다.
     * 그 후 edit를 활용하여 Key - Value형태의 값을 저장할 수 있다.
     */
    private suspend fun save(value: String) {
        val key = stringPreferencesKey(stringKey)
        dataStore.edit {
            it[key] = value
        }
    }

    /**
     * 데이터 불러오기
     * dataStore에 접근하여 예외 처리를 해주고
     * 정상적으로 실행된 경우에 key값에 해당하는 value 값을 리턴한다.
     */
    private suspend fun load(): Flow<String> {
        val key = stringPreferencesKey(stringKey)
        return dataStore.data.catch { e->
            if (e is IOException) {
                emit(emptyPreferences())
            } else {
                throw e
            }
        }.map {
            it[key] ?: "기본 텍스트입니다."
        }
    }
}