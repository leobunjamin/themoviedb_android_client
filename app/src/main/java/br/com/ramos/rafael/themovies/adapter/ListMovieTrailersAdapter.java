package br.com.ramos.rafael.themovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.rafaelcrz.tmdbandroidwrapper_lib.model.Trailer;

import java.util.List;

import br.com.ramos.rafael.themovies.R;



public class ListMovieTrailersAdapter extends RecyclerView.Adapter<ListMovieTrailersAdapter.MyViewHolder> {

    private Context context;
    private LayoutInflater mLayoutInflater;
    private List<Trailer> trailerList;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;


    public ListMovieTrailersAdapter(Context c, List<Trailer> trailerList) {
        this.mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.trailerList = trailerList;
        this.context = c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.item_trailer_video, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, int position) {

        Trailer trailer = trailerList.get(position);

        myViewHolder.tvTitle.setText(trailer.getName());
        Glide.with(context).load("https://img.youtube.com/vi/" + trailer.getKey() + "/default.jpg").into(myViewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        try {
            return this.trailerList.size();
        } catch (Exception e) {
            return 0;
        }
    }


    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r) {
        mRecyclerViewOnClickListenerHack = r;
    }

    public void addItem(Trailer movie, int position) {
        trailerList.add(movie);
        notifyItemInserted(position);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvTitle;
        public ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.thumbnail);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTrailerTitle);

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
