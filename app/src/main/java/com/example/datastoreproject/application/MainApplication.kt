package com.example.datastoreproject.application

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore


/**
 * 데이터 스토어는 싱글톤으로 한번만 생성하여 불러와 사용해야 한다.
 * 따라서 Application 수준에서 객체를 생성하거나 DI(의존성 주입)등을 활용하여 관리하는 것이 좋다.
 *
 */
class MainApplication: Application() {
    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    }
}