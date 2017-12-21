package com.example.android.bakingapp;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.utils.RecipeLoader;
import java.util.List;

public class RecipeList extends AppCompatActivity implements LoaderManager.LoaderCallbacks{

    private static final int ID_LOADER_RECIPE = 25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        getSupportLoaderManager().initLoader(ID_LOADER_RECIPE, null, this);
    }

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
        switch (loader.getId()) {
            case ID_LOADER_RECIPE:
                if(data != null) {
                    updateData((List<Recipe>)data);
                }
        }
    }

    private void updateData(List<Recipe> data) {
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
