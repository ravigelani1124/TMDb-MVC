package com.interview.tmdb_mvc.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.interview.tmdb_mvc.R
import com.interview.tmdb_mvc.adapter.MovieAdapter
import com.interview.tmdb_mvc.model.movie.MovieResponseModel
import com.interview.tmdb_mvc.model.movie.MovieResult
import com.interview.tmdb_mvc.service.ServiceGenerator
import com.interview.tmdb_mvc.utility.PaginationScrollListener
import org.json.JSONObject
import retrofit2.Response

class HomeScreen : AppCompatActivity(), MovieAdapter.OnMovieViewClick {

    private lateinit var mContext: HomeScreen
    private var currentPage: Int = 1
    private var totalPage: Int = 1
    private var isloading: Boolean = false
    private var islastpage: Boolean = false
    private var movieList: ArrayList<MovieResult> = ArrayList()
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var rvMovie: RecyclerView
    private lateinit var pbLoadMore: ProgressBar
    private lateinit var tvNoData: AppCompatTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
        mContext = this
        initUI()
    }

    private fun initUI() {
        findViewReference()
        setUpMovieRecyclerView()
        callMovieListAPI(currentPage, true)
        setRecyclerPagination()
    }

    private fun findViewReference() {
        pbLoadMore = findViewById(R.id.pbLoadMore)
        rvMovie = findViewById(R.id.rvMovie)
        tvNoData = findViewById(R.id.tvNoData)
    }

    private fun setUpMovieRecyclerView() {
        rvMovie.layoutManager = GridLayoutManager(mContext, 2)
        movieAdapter = MovieAdapter(mContext, movieList, this)
        rvMovie.adapter = movieAdapter
    }

    private fun setRecyclerPagination() {
        rvMovie.addOnScrollListener(object :
            PaginationScrollListener(rvMovie.layoutManager as GridLayoutManager) {
            override fun loadMoreItems() {
                if (currentPage < totalPage) {
                    currentPage++
                    isloading = true
                    callMovieListAPI(currentPage, false)
                }
            }

            override fun getTotalPageCount(): Int {
                TODO("Not yet implemented")
            }

            override fun isLoading(): Boolean {
                return isloading
            }

            override fun isLastPage(): Boolean {

                return islastpage
            }
        })

    }

    private fun callMovieListAPI(currentPageAPI: Int, isLoderShow: Boolean) {

        if (currentPageAPI > 1) {
            pbLoadMore.visibility = View.VISIBLE
        }

        ServiceGenerator(
            mContext,
            isLoderShow,
            object : ServiceGenerator.ServiceGeneratorInterfaceWithFailure {
                override fun onSuccess(response: Response<JsonObject?>) {
                    isloading = false

                    if (response.body() != null) {
                        val jsonObject = JSONObject(response.body().toString())
                        currentPage = jsonObject.optInt("page")
                        totalPage = jsonObject.optInt("total_pages")

                        movieList.addAll(Gson().fromJson(JSONObject(response.body().toString())
                                    .getJSONArray("results").toString(),
                                object : TypeToken<ArrayList<MovieResult>>() {}.type)
                        )
                        movieAdapter.notifyDataSetChanged()

                        if (currentPage == totalPage) {
                            islastpage = true
                        }
                        if (movieList.size == 0) {
                            tvNoData.visibility = View.VISIBLE
                        } else {
                            tvNoData.visibility = View.GONE
                        }
                        if (pbLoadMore.visibility == View.VISIBLE) {
                            pbLoadMore.visibility = View.GONE
                        }
                    }
                }

                override fun onFailure(call1: Throwable) {
                    Toast.makeText(mContext, call1.message, Toast.LENGTH_SHORT).show()
                }

            },
            ServiceGenerator.createAPI(mContext)
                .getPopularMovieList(resources.getString(R.string.api_key), currentPageAPI)
        )

    }

    override fun goToMovieDetails(position: Int, movieResponseModel: MovieResponseModel) {

    }
}