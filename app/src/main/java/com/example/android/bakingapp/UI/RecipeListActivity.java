package com.example.android.bakingapp.UI;

import android.os.Parcelable;
import android.support.v4.content.Loader;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapters.RecipeAdapter;
import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.loaders.RecipeLoader;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks{

    private static final String SAVED_LAYOUT_MANAGER = "saved_layout_manager";
    private static final int ID_LOADER_RECIPE = 25;
    @BindView(R.id.recipes_rc)
    RecyclerView mRecipesRc;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar mProgressBar;
    RecipeAdapter mRecipeAdapter;
    private Parcelable layoutManagerSavedState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_search_recipe);
        ButterKnife.bind(this);
        GridLayoutManager layoutManager = new GridLayoutManager(RecipeListActivity.this, numberOfColumns());
        mRecipesRc.setLayoutManager(layoutManager);
        mRecipesRc.setHasFixedSize(true);

        mRecipeAdapter = new RecipeAdapter();
        mRecipesRc.setAdapter(mRecipeAdapter);

        if(savedInstanceState != null )
            layoutManagerSavedState = savedInstanceState.getParcelable(SAVED_LAYOUT_MANAGER);

        loadRecipesData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        layoutManagerSavedState = mRecipesRc.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(SAVED_LAYOUT_MANAGER, layoutManagerSavedState);
        super.onSaveInstanceState(outState);
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
        if (layoutManagerSavedState != null) {
            mRecipesRc.getLayoutManager().onRestoreInstanceState(layoutManagerSavedState);
        }
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
