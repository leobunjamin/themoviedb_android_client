package br.com.ramos.rafael.themovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.rafaelcrz.tmdbandroidwrapper_lib.api.ImageSize;
import com.github.rafaelcrz.tmdbandroidwrapper_lib.api.TheMovieApi;
import com.github.rafaelcrz.tmdbandroidwrapper_lib.model.Movie;

import java.util.List;

import br.com.ramos.rafael.themovies.R;


public class ListMovieAdapter extends RecyclerView.Adapter<ListMovieAdapter.MyViewHolder> {

    private Context context;
    private LayoutInflater mLayoutInflater;
    private List<Movie> movieList;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

    public ListMovieAdapter(Context c, List<Movie> movieList) {
        this.mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.movieList = movieList;
        this.context = c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.item_movie, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, int position) {

        Movie movie = movieList.get(position);

        try {
            Glide.with(context).load(ImageSize.setImageSize(movie.getPoster_path(), ImageSize.Poster.W154)).placeholder(R.drawable.placeholder).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(myViewHolder.imgImage);
        } catch (NullPointerException e) {
            Glide.with(context).load(R.drawable.placeholder).into(myViewHolder.imgImage);
        }

    }

    @Override
    public int getItemCount() {
        try {
            return this.movieList.size();
        } catch (Exception e) {
            return 0;
        }
    }


    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r) {
        mRecyclerViewOnClickListenerHack = r;
    }

    public void addItem(Movie movie, int position) {
        movieList.add(movie);
        notifyItemInserted(position);
    }

    public void clear() {
        movieList.clear();
        notifyDataSetChanged();
    }

    public List<Movie> getList() {
        return movieList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvTitle;
        public ImageView imgImage;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tvItemMovieTitle);
            imgImage = (ImageView) itemView.findViewById(R.id.imgItemMoviePicture);
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            if (mRecyclerViewOnClickListenerHack != null) {
                mRecyclerViewOnClickListenerHack.onClickListener(v, getPosition());
            }
        }
    }


}
