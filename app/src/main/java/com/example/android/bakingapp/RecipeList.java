package com.example.android.bakingapp;

import android.support.v4.content.Loader;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.utils.RecipeLoader;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeList extends AppCompatActivity implements LoaderManager.LoaderCallbacks{

    private static final int ID_LOADER_RECIPE = 25;
    @BindView(R.id.recipes_rc)
    RecyclerView mRecipesRc;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar mProgressBar;
    RecipeAdapter mRecipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        ButterKnife.bind(this);
        GridLayoutManager layoutManager = new GridLayoutManager(RecipeList.this, numberOfColumns());
        mRecipesRc.setLayoutManager(layoutManager);
        mRecipesRc.setHasFixedSize(true);

        mRecipeAdapter = new RecipeAdapter();
        mRecipesRc.setAdapter(mRecipeAdapter);

        loadRecipesData();
    }

    private void loadRecipesData() {
        mProgressBar.setVisibility(View.VISIBLE);
        mRecipesRc.setVisibility(View.INVISIBLE);
        Loader loader = getSupportLoaderManager().getLoader(ID_LOADER_RECIPE);
        if(loader == null)
            getSupportLoaderManager().initLoader(ID_LOADER_RECIPE, null, this);
        else
            getSupportLoaderManager().restartLoader(ID_LOADER_RECIPE, null, this);
    }

    private void updateAdapterData(List<Recipe> data) {
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecipesRc.setVisibility(View.VISIBLE);
        mRecipeAdapter.setData(data);
    }
    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthDivider = 500;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }


    //AssyncTask functions
    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id){
            case ID_LOADER_RECIPE:
                return new RecipeLoader(this);
        }
        return null;
    }
    @Override
    public void onLoadFinished(Loader loader, Object data) {
        if (loader != null)
            switch (loader.getId()) {
                case ID_LOADER_RECIPE:
                    if (data != null)
                        updateAdapterData((List<Recipe>) data);
            }
    }
    @Override
    public void onLoaderReset(Loader loader) {}
}
