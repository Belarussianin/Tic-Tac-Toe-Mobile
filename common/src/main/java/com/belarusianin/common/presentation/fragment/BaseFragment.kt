package com.belarusianin.common.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.belarusianin.common.presentation.navigation.NavigationObserver
import com.belarusianin.common.presentation.viewmodel.BaseViewModel

abstract class BaseFragment<Binding : ViewBinding, ViewModel : BaseViewModel>(
    private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> Binding
) : Fragment() {

    private var _binding: Binding? = null
    protected val binding: Binding get() = _binding!!

    protected abstract val viewModel: ViewModel
    protected open val navObserver by lazy { NavigationObserver(viewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navObserver.register(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflate.invoke(inflater, container, false).apply { _binding = this }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bindUI()
        viewModel.subscribeUI()
    }

    protected open fun Binding.bindUI() {}

    protected open fun ViewModel.subscribeUI() {}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}