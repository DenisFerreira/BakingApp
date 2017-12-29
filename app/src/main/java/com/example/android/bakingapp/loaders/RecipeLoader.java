package com.example.android.bakingapp.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.utils.JsonUtils;
import com.example.android.bakingapp.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class RecipeLoader extends AsyncTaskLoader<List<Recipe>> {

    private List<Recipe> mLastResult;

    public RecipeLoader(Context context) {
        super(context);
    }

    @Override
    public List<Recipe> loadInBackground() {
        try {
            String json = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildUrl());
            List<Recipe> recipes = JsonUtils.getRecipesFromJson(json);
            return recipes;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deliverResult(List<Recipe> data) {
        mLastResult = data;
        super.deliverResult(data);
    }

    @Override
    protected void onStartLoading() {
        if (mLastResult != null) {
            deliverResult(mLastResult);
        } else {
            forceLoad();
        }
    }
}