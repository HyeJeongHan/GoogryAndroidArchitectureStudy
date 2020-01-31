package com.example.archstudy.ui.main

import android.content.Context
import com.example.archstudy.data.repository.NaverQueryRepositoryImpl
import com.example.archstudy.data.source.local.AppDatabase
import com.example.archstudy.data.source.local.MovieData
import com.example.archstudy.data.source.local.NaverQueryLocalDataSourceImpl
import com.example.archstudy.data.source.remote.NaverQueryRemoteDataSourceImpl

class MainPresenter : MainInterface.Presenter {

    private var mView: MainInterface.View? = null
    private var naverQueryRepositoryImpl: NaverQueryRepositoryImpl

    init {
        var context: Context? = mView?.let { it.getApplicationContext() }

        val localMovieDao = AppDatabase.getInstance(context!!)?.localMovieDao()
        val searchWordDao = AppDatabase.getInstance(context!!)?.searchWordDao()
        val localData = NaverQueryLocalDataSourceImpl(localMovieDao, searchWordDao)
        val remoteData = NaverQueryRemoteDataSourceImpl()
        naverQueryRepositoryImpl = NaverQueryRepositoryImpl(localData, remoteData)
    }

    override fun bindView(view: MainInterface.View) {
        this.mView = view

    }

    override fun unBindView() {
        mView = null
    }

    override fun getRemoteDataByQuery(query: String?) {

        if (query.isNullOrEmpty()) {
            return
        }

        naverQueryRepositoryImpl.requestRemoteData(query, successCallback = {
            // Remote Data 요청이 성공했을 경우 MainActivity에 데이터 전달
            mView?.showDataList(it)

        }, failCallback = {
            // Remote Data 요청이 실패했을 경우 에러 메시지 출력
            mView?.showErrorMessage(it)
        })
    }

    override fun getLocalQuery(): String? {
        var localQuery: String
        naverQueryRepositoryImpl.apply {
            localQuery = RequestLocalQueryAsync().execute().get() ?: ""
        }
        return localQuery
    }

    override fun getLocalData(query: String?) {

        // 검색어가 null 이거나 비었을 경우 오류 메세지 출력
        if (query.isNullOrEmpty()) {
            mView?.showErrorMessage(Throwable("최근 검색한 결과가 없습니다."))
            // query 가 비어있거나 null 값이 아닐 경우 로컬 DB에 데이터 요청
        } else {
            naverQueryRepositoryImpl.apply {
                val result = RequestLocalDataAsync(query).execute().get()
                mView?.showDataList(result)
            }
        }
    }

    override fun insertData(query: String, data: MutableList<MovieData>) {
        naverQueryRepositoryImpl.InsertLocalDataAsync(query,data).execute()
    }
}