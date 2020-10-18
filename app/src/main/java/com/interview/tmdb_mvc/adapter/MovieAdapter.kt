package com.interview.tmdb_mvc.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.interview.tmdb_mvc.R
import com.interview.tmdb_mvc.model.movie.MovieResponseModel
import com.interview.tmdb_mvc.model.movie.MovieResult
import com.interview.tmdb_mvc.utility.TinyDb
import com.interview.tmdb_mvc.utility.Util
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

data class MovieAdapter(
    val mContext: Context,
    val movieList: ArrayList<MovieResult>,
    val onMovieViewClick: OnMovieViewClick
) :
    RecyclerView.Adapter<MovieAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.list_movie, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val result = movieList[position]

        if (result.title.isNotEmpty()) {
            holder.tvMovieName.text = result.title
        }
        if (result.release_date.isNotEmpty()) {
            holder.tvMovieDesc.text = Util.parseDateToddMMyyyy(result.release_date)
        }

        if (result.poster_path.isNotEmpty()) {
            val mImageUrl = TinyDb(mContext).getString(TinyDb.posterImagePath) + result.poster_path
            Picasso.get().load(mImageUrl)
                .into(holder.ivMovie, object : Callback {
                    override fun onSuccess() {
                        holder.progressBar.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                        holder.progressBar.visibility = View.GONE
                    }
                })
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    interface OnMovieViewClick {
        fun goToMovieDetails(position: Int, movieResponseModel: MovieResponseModel)
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivMovie: AppCompatImageView = view.findViewById(R.id.ivMovie)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)
        val tvMovieName: AppCompatTextView = view.findViewById(R.id.tvMovieName)
        val tvMovieDesc: AppCompatTextView = view.findViewById(R.id.tvMovieDesc)
        val llMovieView: CardView = view.findViewById(R.id.llMovieView)
    }

}