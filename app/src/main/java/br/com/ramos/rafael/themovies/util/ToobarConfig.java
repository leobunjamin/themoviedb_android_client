package br.com.ramos.rafael.themovies.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import br.com.ramos.rafael.themovies.R;


/**
 * Created by Rafael Felipe on 23/03/2017.
 *
 */

public class ToobarConfig {

    /**
     * Esse metodo configura um Toolbar. Como são muitas propriedades coloquei os principais
     * nesse metodo
     * @param toolbar Toolbar usada na Acitivty
     * @param activity contexto da activty
     */
    public static void setToolbar(Toolbar toolbar, final AppCompatActivity activity) {
        activity.setSupportActionBar(toolbar);
        //Ativa o botão de voltar
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitleTextColor(Color.WHITE);

        //seta o homeButton na cor branca
        final Drawable upArrow;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            upArrow = activity.getDrawable(R.drawable.abc_ic_ab_back_material);
        }else {
            upArrow = activity.getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        }
        upArrow.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationIcon(upArrow);

        //HomeButton fecha a Activity atual.
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

}
