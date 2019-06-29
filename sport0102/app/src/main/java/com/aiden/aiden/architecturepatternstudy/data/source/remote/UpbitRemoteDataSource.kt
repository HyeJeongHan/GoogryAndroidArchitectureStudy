/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiden.aiden.architecturepatternstudy.data.source.remote

import com.aiden.aiden.architecturepatternstudy.api.UpbitApi
import com.aiden.aiden.architecturepatternstudy.api.model.MarketResponse
import com.aiden.aiden.architecturepatternstudy.api.model.TickerResponse
import com.aiden.aiden.architecturepatternstudy.data.source.UpbitDataSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Implementation of the data source that adds a latency simulating network.
 */
class UpbitRemoteDataSource private constructor(private val upbitApi: UpbitApi) : UpbitDataSource {

    override fun getMarketList(callback: UpbitDataSource.GetMarketListCallback) {

        upbitApi.getMarketList().enqueue(object : Callback<ArrayList<MarketResponse>> {

            override fun onFailure(call: Call<ArrayList<MarketResponse>>?, t: Throwable?) {
                callback.onDataNotAvailable()
            }

            override fun onResponse(call: Call<ArrayList<MarketResponse>>?, response: Response<ArrayList<MarketResponse>>?) {
                response?.body()?.let {
                    callback.onMarketListLoaded(it)
                }
            }

        })

    }

    override fun getTickerList(marketList: List<MarketResponse>, callback: UpbitDataSource.GetTickerListCallback) {

        upbitApi.getTickerInfo(marketList.joinToString { marketModel -> marketModel.market })
            .enqueue(object : Callback<ArrayList<TickerResponse>> {
                override fun onFailure(call: Call<ArrayList<TickerResponse>>, t: Throwable) {
                    callback.onDataNotAvailable()
                }

                override fun onResponse(
                    call: Call<ArrayList<TickerResponse>>,
                    response: Response<ArrayList<TickerResponse>>
                ) {
                    response.body()?.let {
                        callback.onTickerListLoaded(it)
                    }
                }

            })
    }

    companion object {

        private var instance: UpbitRemoteDataSource? = null

        operator fun invoke(upbitApi: UpbitApi) =
            instance ?: UpbitRemoteDataSource(upbitApi)
                .apply { instance = this }

    }

}
