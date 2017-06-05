package br.com.ramos.rafael.themovies.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.rafaelcrz.tmdbandroidwrapper_lib.api.ImageSize;
import com.github.rafaelcrz.tmdbandroidwrapper_lib.api.TheMovieApi;
import com.github.rafaelcrz.tmdbandroidwrapper_lib.api.TheMovieApiListener;
import com.github.rafaelcrz.tmdbandroidwrapper_lib.model.MovieDetail;

import br.com.ramos.rafael.themovies.BuildConfig;
import br.com.ramos.rafael.themovies.R;
import br.com.ramos.rafael.themovies.util.TheMovieConstants;
import br.com.ramos.rafael.themovies.util.ToobarConfig;
import retrofit2.Call;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity {

    private TheMovieApi mTheMovieApi;

    private ImageView imgPoster;
    private TextView tvTitle, tvYear, tvGenres, tvVotes, tvVoteAverage, tvOverview;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        initWidgetXmlView();

        Intent intent = getIntent();
        if (intent != null) {

            showProgressDialog(); //Show the ProgressDialog

            int movie_id = intent.getExtras().getInt(TheMovieConstants.MOVIE_ID);

            mTheMovieApi = new TheMovieApi(this, TheMovieConstants.API_KEY);
            //mTheMovieApi.setLanguage("en");


            mTheMovieApi.getDatailMovie(movie_id, "", new TheMovieApiListener<MovieDetail>() {
                @Override
                public void onResponse(Call<MovieDetail> call, Response<MovieDetail> movieResultResponse) {
                    if (movieResultResponse.isSuccessful()) {
                        MovieDetail movieDetail = movieResultResponse.body();

                        try {
                            Glide.with(MovieDetailActivity.this).load(ImageSize.setImageSize(movieDetail.getBackdrop_path(), ImageSize.Backdrop.W780)).placeholder(R.drawable.placeholder).into(imgPoster);
                        } catch (NullPointerException e) {
                            Glide.with(MovieDetailActivity.this).load(R.drawable.placeholder).into(imgPoster);
                        }
                        tvTitle.setText(movieDetail.getTitle());
                        tvYear.setText(movieDetail.getRelease_date());
                        tvGenres.setText(movieDetail.getGenres().toString().replace("[", "").replace("]", ""));
                        tvVotes.setText(String.valueOf(movieDetail.getVote_count()));
                        tvVoteAverage.setText(String.valueOf(movieDetail.getVote_average()));
                        tvOverview.setText(movieDetail.getOverview());

                        replaceFragment(movieDetail.getId());
                        if (mProgressDialog != null)
                            mProgressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<MovieDetail> call, Throwable throwable) {
                    throwable.printStackTrace();
                }
            });
        }
    }

    private void initWidgetXmlView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ToobarConfig.setToolbar(toolbar, this);
        getSupportActionBar().setTitle("");

        imgPoster = (ImageView) findViewById(R.id.imgMovieDetailPoster);
        tvTitle = (TextView) findViewById(R.id.tvMovieDetailTitle);
        tvYear = (TextView) findViewById(R.id.tvMovieDetailYear);
        tvGenres = (TextView) findViewById(R.id.tvMovieDetailGenres);
        tvVotes = (TextView) findViewById(R.id.tvMovieDetailVotes);
        tvVoteAverage = (TextView) findViewById(R.id.tvMovieDetailVotesAverag);
        tvOverview = (TextView) findViewById(R.id.tvMovieDetailOverview);
    }

    private void replaceFragment(int movie_id) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        MovieTrailersFragment movieFragment = new MovieTrailersFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TheMovieConstants.MOVIE_ID, movie_id);
        movieFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.content_movie_detail, movieFragment);
        fragmentTransaction.commit();
    }

    private void showProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading...");
        //mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

}
