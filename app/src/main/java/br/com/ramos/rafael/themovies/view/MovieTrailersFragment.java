package br.com.ramos.rafael.themovies.view;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.rafaelcrz.tmdbandroidwrapper_lib.api.TheMovieApi;
import com.github.rafaelcrz.tmdbandroidwrapper_lib.api.TheMovieApiListener;
import com.github.rafaelcrz.tmdbandroidwrapper_lib.model.Trailer;
import com.github.rafaelcrz.tmdbandroidwrapper_lib.model.TrailerCatalog;

import java.util.ArrayList;
import java.util.List;

import br.com.ramos.rafael.themovies.R;
import br.com.ramos.rafael.themovies.adapter.ListMovieTrailersAdapter;
import br.com.ramos.rafael.themovies.adapter.RecyclerViewOnClickListenerHack;
import br.com.ramos.rafael.themovies.util.TheMovieConstants;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Rafael Felipe on 29/05/2017.
 */

public class MovieTrailersFragment extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ListMovieTrailersAdapter trailersAdapter;

    private final String YOUTUBE = "youtube";
    private Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = MovieTrailersFragment.this.getActivity();

        layoutManager = new LinearLayoutManager(mContext);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_trailer_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerMovieTrailers);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            int movie_id = arguments.getInt(TheMovieConstants.MOVIE_ID);

            List<Trailer> trailerList = new ArrayList<>();
            trailersAdapter = new ListMovieTrailersAdapter(mContext, trailerList);

            TheMovieApi theMovieApi = new TheMovieApi(mContext,TheMovieConstants.API_KEY);
            //theMovieApi.setLanguage("en");



            theMovieApi.getVideosFromMovie(movie_id, new TheMovieApiListener<TrailerCatalog>() {
                @Override
                public void onResponse(Call<TrailerCatalog> call, Response<TrailerCatalog> movieResultResponse) {
                    if (movieResultResponse.isSuccessful()) {
                        final TrailerCatalog trailerCatalog = movieResultResponse.body();
                        for (Trailer trailer : trailerCatalog.getResults()) {
                            if (trailer.getSite().equalsIgnoreCase(YOUTUBE)) {
                                trailersAdapter.addItem(trailer, trailersAdapter.getItemCount() - 1);
                            }
                        }
                        recyclerView.setAdapter(trailersAdapter);

                        trailersAdapter.setRecyclerViewOnClickListenerHack(new RecyclerViewOnClickListenerHack() {
                            @Override
                            public void onClickListener(View view, int position) {
                                Trailer trailer = trailerCatalog.getResults().get(position);
                                if (trailer.getSite().equalsIgnoreCase(YOUTUBE)) {
                                    showTrailerOnYoutube(trailer.getKey());
                                }
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<TrailerCatalog> call, Throwable throwable) {
                    throwable.printStackTrace();
                }
            });
        }
    }

    public void showTrailerOnYoutube(String movie_id) {
        Intent youtubeAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + movie_id));
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + movie_id));
        try {
            startActivity(youtubeAppIntent);
        } catch (ActivityNotFoundException e) {
            startActivity(browserIntent);
        }
    }

}
