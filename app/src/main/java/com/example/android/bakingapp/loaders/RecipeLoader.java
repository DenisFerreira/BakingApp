package com.example.android.bakingapp.loaders;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.bakingapp.data.Ingredient;
import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.provider.RecipeContract;
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
            for(Recipe recipe : recipes) {
                insertRecipe(getContext(), recipe);
                int counter = 0;
                for( Ingredient ingredient : recipe.getIngredients()) {
                    insertIngredient(getContext(), recipe, ingredient, counter);
                    counter++;
                }
            }
            return recipes;
        } catch (IOException | JSONException e) {
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

    private static Uri insertRecipe(Context context, Recipe recipe) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(RecipeContract.RecipeEntry.COLUMN_ID, recipe.getId());
        contentValues.put(RecipeContract.RecipeEntry.COLUMN_NAME, recipe.getName());
        contentValues.put(RecipeContract.RecipeEntry.COLUMN_SERVINGS, recipe.getServings());
        contentValues.put(RecipeContract.RecipeEntry.COLUMN_IMAGE, recipe.getImage());
        return context.getContentResolver().insert(RecipeContract.RecipeEntry.CONTENT_URI, contentValues);
    }

    private static Uri insertIngredient(Context context, Recipe recipe, Ingredient ingredient, int counter) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(RecipeContract.RecipeIngredientEntry.COLUMN_RECIPE_ID, recipe.getId());
        contentValues.put(RecipeContract.RecipeIngredientEntry.COLUMN_ID, String.valueOf(recipe.getId() + "_" + counter));
        contentValues.put(RecipeContract.RecipeIngredientEntry.COLUMN_INGREDIENT, ingredient.getIngredient());
        contentValues.put(RecipeContract.RecipeIngredientEntry.COLUMN_MEASURE, ingredient.getMeasure());
        contentValues.put(RecipeContract.RecipeIngredientEntry.COLUMN_QUANTITY, ingredient.getQuantity());
        return context.getContentResolver().insert(RecipeContract.RecipeIngredientEntry.CONTENT_URI, contentValues);
    }
}