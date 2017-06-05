package br.com.ramos.rafael.themovies.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.rafaelcrz.tmdbandroidwrapper_lib.api.TheMovieApi;
import com.github.rafaelcrz.tmdbandroidwrapper_lib.api.TheMovieApiListener;
import com.github.rafaelcrz.tmdbandroidwrapper_lib.model.Movie;
import com.github.rafaelcrz.tmdbandroidwrapper_lib.model.MovieCatalog;

import java.util.ArrayList;

import br.com.ramos.rafael.themovies.BuildConfig;
import br.com.ramos.rafael.themovies.R;
import br.com.ramos.rafael.themovies.adapter.ListMovieAdapter;
import br.com.ramos.rafael.themovies.adapter.RecyclerViewOnClickListenerHack;
import br.com.ramos.rafael.themovies.util.TheMovieConstants;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Rafael Felipe on 27/05/2017.
 */

public class MovieFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout refreshLayout;
    private GridLayoutManager mLayoutManager;
    private ListMovieAdapter mMovieAdapter;
    private Context mContext;

    private TheMovieApi mTheMovieApi;

    private String urlMovieCategory = TheMovieApi.NOW_PLAYING;

    private boolean isLoading = false;
    private int nextPage;

    private int visibleItemCount = 0;
    private int totalItemCount = 0;
    private int pastVisibleItems = 0;

    private int total_page = 1000;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = MovieFragment.this.getActivity();
        mLayoutManager = new GridLayoutManager(mContext, 3);

        mMovieAdapter = new ListMovieAdapter(mContext, new ArrayList<Movie>());

        mTheMovieApi = new TheMovieApi(mContext, TheMovieConstants.API_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        refreshLayout.setOnRefreshListener(this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerMovies);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //mLayoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mMovieAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //mTheMovieApi.setLanguage("en");

        Bundle arguments = getArguments();
        if (arguments != null) {

            urlMovieCategory = arguments.getString(TheMovieConstants.MOVIE_CATEGORY);

            getMovieCatalog(urlMovieCategory);

            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (dy > 0) {
                        visibleItemCount = mLayoutManager.getChildCount();
                        totalItemCount = mLayoutManager.getItemCount();
                        pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
                        if (!isLoading) {
                            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                                if (nextPage <= total_page) {
                                    mTheMovieApi.setPage(nextPage);
                                    getMovieCatalog(urlMovieCategory);
                                }
                            }
                        }
                    }
                }
            });

        }
    }

    private void getMovieCatalog(String urlMovieCategory) {
        Log.i("teste", String.valueOf(total_page));
        isLoading = true;
        refreshLayout.setRefreshing(true);
        mTheMovieApi.getMovieCatalog(urlMovieCategory, new TheMovieApiListener<MovieCatalog>() {
            @Override
            public void onResponse(Call<MovieCatalog> call, Response<MovieCatalog> movieResultResponse) {
                if (movieResultResponse.isSuccessful()) {
                    final MovieCatalog movieCatalog = movieResultResponse.body();
                    for (Movie movie : movieCatalog.getResults()) {
                        mMovieAdapter.addItem(movie, mMovieAdapter.getItemCount() - 1);
                    }

                    refreshLayout.setRefreshing(false);
                    isLoading = false;
                    nextPage = mTheMovieApi.getPage() + 1;

                    total_page = movieCatalog.getTotal_pages();

                    mMovieAdapter.setRecyclerViewOnClickListenerHack(new RecyclerViewOnClickListenerHack() {
                        @Override
                        public void onClickListener(View view, int position) {
                            Movie movie = mMovieAdapter.getList().get(position);
                            Intent intentDetail = new Intent(mContext, MovieDetailActivity.class);
                            intentDetail.putExtra(TheMovieConstants.MOVIE_ID, movie.getId());
                            startActivity(intentDetail);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<MovieCatalog> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });

    }

    @Override
    public void onRefresh() {
        mMovieAdapter.clear();
        mTheMovieApi.setPage(1);
        getMovieCatalog(urlMovieCategory);
    }
}
