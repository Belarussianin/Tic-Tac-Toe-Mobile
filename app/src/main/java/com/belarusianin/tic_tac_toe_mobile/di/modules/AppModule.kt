package com.belarusianin.tic_tac_toe_mobile.di.modules

import android.content.Context
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.belarusianin.game.core.TicTacToe
import com.belarusianin.game.core.interfaces.ITicTacToe
import com.belarusianin.tic_tac_toe_mobile.data.preferences.AppPreferencesRepository
import com.belarusianin.tic_tac_toe_mobile.data.preferences.AppPreferencesRepository.Companion.APP_PREFERENCES_NAME
import com.belarusianin.tic_tac_toe_mobile.presentation.main.viewmodel.MainViewModel
import com.belarusianin.tic_tac_toe_mobile.presentation.settings.viewmodel.SettingsViewModel
import com.belarusianin.tic_tac_toe_mobile.presentation.tic_tac_toe.viewmodel.TicTacToeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    factory<ITicTacToe> { TicTacToe() }

    single {
        val appContext = get<Context>().applicationContext
        PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            migrations = listOf(SharedPreferencesMigration(appContext, APP_PREFERENCES_NAME)),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { appContext.preferencesDataStoreFile(APP_PREFERENCES_NAME) }
        )
    }

    single { AppPreferencesRepository(get()) }

    viewModel { MainViewModel() }
    viewModel { TicTacToeViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
}