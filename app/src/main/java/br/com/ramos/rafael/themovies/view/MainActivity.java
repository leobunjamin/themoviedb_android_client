package br.com.ramos.rafael.themovies.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.github.rafaelcrz.tmdbandroidwrapper_lib.api.TheMovieApi;

import br.com.ramos.rafael.themovies.R;
import br.com.ramos.rafael.themovies.util.TheMovieConstants;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        replaceFragment(TheMovieApi.NOW_PLAYING);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_now_playing:
                    replaceFragment(TheMovieApi.NOW_PLAYING);
                    return true;
                case R.id.navigation_popular:
                    replaceFragment(TheMovieApi.POPULAR);
                    return true;
                case R.id.navigation_top_rated:
                    replaceFragment(TheMovieApi.TOP_RATED);
                    return true;
            }
            return false;
        }

    };

    private void replaceFragment(String category) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        MovieFragment movieFragment = new MovieFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TheMovieConstants.MOVIE_CATEGORY, category);
        movieFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.content, movieFragment);
        fragmentTransaction.commit();
    }

}
