package ado.sabgil.studyproject.view.coinlist

import ado.sabgil.studyproject.R
import ado.sabgil.studyproject.adapter.TickerAdapter
import ado.sabgil.studyproject.data.CoinRepositoryImpl
import ado.sabgil.studyproject.databinding.FragmnetCoinListBinding
import ado.sabgil.studyproject.view.base.BaseFragment
import ado.sabgil.studyproject.view.home.HomeViewModel
import ado.sabgil.studyproject.view.home.HomeViewModelFactory
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer


class CoinListFragment : BaseFragment<FragmnetCoinListBinding>(R.layout.fragmnet_coin_list) {

    private val coinListViewModel: CoinListViewModel by lazy {
        getFragmentScopeViewModel(
            CoinListViewModel::class.java,
            CoinListViewModelFactory(baseCurrency, CoinRepositoryImpl)
        )
    }

    private val homeViewModel: HomeViewModel by lazy {
        getActivityScopeViewModel(
            HomeViewModel::class.java,
            HomeViewModelFactory(CoinRepositoryImpl)
        )
    }

    private val baseCurrency: String by lazy {
        requireArguments().getString(ARGUMENT_BASE_CURRENCY)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = binding.pgCoinList

        bind {
            vm = coinListViewModel
            rvTickerList.adapter = TickerAdapter()
        }

        registerEvent()
    }

    override fun onStart() {
        super.onStart()
        coinListViewModel.subscribeRemote()
    }

    override fun onStop() {
        coinListViewModel.unSubscribeRemote()
        super.onStop()
    }

    private fun registerEvent() {
        coinListViewModel.run {
            showToastEvent.observe(this@CoinListFragment, Observer(::showToastMessage))
        }

        homeViewModel.run {
            selectedSortField.observe(this@CoinListFragment, Observer(coinListViewModel::setSortingCriteria))
            sortingOrder.observe(this@CoinListFragment, Observer(coinListViewModel::setSortingOrder))
        }
    }

    companion object {
        const val ARGUMENT_BASE_CURRENCY = "BASE_CURRENCY"
        fun newInstance(baseCurrency: String) =
            CoinListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARGUMENT_BASE_CURRENCY, baseCurrency)
                }
            }
    }
}