package com.example.kangraemin.model

import com.example.kangraemin.model.local.datadao.LocalMovieDataSource
import com.example.kangraemin.model.local.datamodel.Movie
import com.example.kangraemin.model.remote.datadao.MovieRemoteDataSource
import com.example.kangraemin.model.remote.datamodel.MovieDetail
import com.example.kangraemin.model.remote.datamodel.Movies
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class MovieSearchRepository(
    val remoteMovieDatasource: MovieRemoteDataSource,
    val localMovieDataSource: LocalMovieDataSource
) {

    fun getMovieData(query: String): Flowable<Movies> {
        return Flowable
            .defer {
                val getLocalMovies = localMovieDataSource.getAll()
                    .subscribeOn(Schedulers.io())
                    .map { getMovieDataInRoom(it) }
                    .toFlowable()

                val getRemoteMovies = remoteMovieDatasource
                    .getMovies(
                        query = query
                    )
                    .subscribeOn(Schedulers.io())
                    .flatMap {
                        localMovieDataSource
                            .deleteAll()
                            .subscribeOn(Schedulers.io())
                            .andThen(Single.just(it))
                            .map { mappingMovieDataToLocal(it) }
                            .flatMapCompletable {
                                localMovieDataSource.insertMovies(it)
                                    .subscribeOn(Schedulers.io())
                            }
                            .andThen(Single.just(it))
                    }
                    .toFlowable()

                if (query.isNotEmpty()) {
                    Flowable
                        .concat(
                            getLocalMovies,
                            getRemoteMovies
                        )
                } else {
                    getLocalMovies
                }
            }
    }

    private fun getMovieDataInRoom(movies: List<Movie>): Movies {
        return Movies(
            items = ArrayList(
                movies
                    .map {
                        MovieDetail(
                            title = it.title,
                            image = it.image,
                            director = it.director,
                            actor = it.actor,
                            userRating = it.userRating,
                            pubDate = it.pubDate
                        )
                    })
        )
    }

    private fun mappingMovieDataToLocal(movies: Movies): List<Movie> {
        return movies.items.toList()
            .map {
                Movie(
                    title = it.title,
                    image = it.image,
                    director = it.director,
                    actor = it.actor,
                    userRating = it.userRating,
                    pubDate = it.pubDate
                )
            }
    }
}