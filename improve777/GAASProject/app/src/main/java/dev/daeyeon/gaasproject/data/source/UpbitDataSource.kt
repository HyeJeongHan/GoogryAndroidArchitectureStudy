package dev.daeyeon.gaasproject.data.source

import dev.daeyeon.gaasproject.data.Ticker

interface UpbitDataSource {

    var markets: String

    fun getTicker(
        baseCurrency: String,
        searchTicker: String,
        success: (tickerList: List<Ticker>) -> Unit,
        fail: (msg: String) -> Unit
    )

    fun getMarkets(success: (markets: String) -> Unit, fail: (msg: String) -> Unit)

    /**
     * 마켓 코드 요청 중지
     */
    fun cancelMarketCall()

    /**
     * 코인 시세 정보 요청 중지
     */
    fun cancelTickerCall()

    companion object {
        const val ALL_CURRENCY = "전체"
    }
}