package com.belarusianin.tic_tac_toe_mobile.presentation.tic_tac_toe.view

import androidx.compose.runtime.collectAsState
import com.belarusianin.common.presentation.fragment.BaseFragment
import com.belarusianin.game.core.interfaces.GameStatus
import com.belarusianin.tic_tac_toe_mobile.R
import com.belarusianin.tic_tac_toe_mobile.databinding.FragmentTicTacToeBinding
import com.belarusianin.tic_tac_toe_mobile.presentation.tic_tac_toe.viewModel.TicTacToeViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.ref.WeakReference

class TicTacToeFragment :
    BaseFragment<FragmentTicTacToeBinding, TicTacToeViewModel>(FragmentTicTacToeBinding::inflate) {

    override val viewModel by viewModel<TicTacToeViewModel>()

    private var snackbar: WeakReference<Snackbar>? = null

    override fun FragmentTicTacToeBinding.bindUI() {
        restartButton.setOnClickListener {
            viewModel.restartGame()
        }
    }

    override fun TicTacToeViewModel.subscribeUI() {
        binding.gameField.setContent {
            val cellsState = cells.collectAsState()
            Field(cells = cellsState.value, onCellClick = viewModel::makeMove)
        }

        state.observe(viewLifecycleOwner) { state ->
            //TODO: debug only
            // stateChangedNotification(state)
        }

        xScore.observe(viewLifecycleOwner) { xWinsCounter ->
            updateWinStates(xWinsCounter = xWinsCounter)
        }

        oScore.observe(viewLifecycleOwner) { oWinsCounter ->
            updateWinStates(oWinsCounter = oWinsCounter)
        }
    }

    private fun stateChangedNotification(state: GameStatus) {
        snackbar?.clear()
        snackbar = WeakReference(
            Snackbar.make(requireView(), state.toString(), Snackbar.LENGTH_SHORT).apply {
                setAction("Cancel") {}
                show()
            }
        )
    }

    private fun updateWinStates(xWinsCounter: Int? = null, oWinsCounter: Int? = null) {
        xWinsCounter?.let { counter ->
            binding.xWinsCounter.text = resources.getString(R.string.x_wins_counter_text, counter)
        }
        oWinsCounter?.let { counter ->
            binding.oWinsCounter.text = resources.getString(R.string.o_wins_counter_text, counter)
        }
    }

    override fun onStop() {
        super.onStop()
        snackbar?.clear()
        snackbar = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        snackbar?.clear()
        snackbar = null
    }
}