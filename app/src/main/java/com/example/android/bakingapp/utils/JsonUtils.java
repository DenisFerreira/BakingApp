package com.example.android.bakingapp.utils;

import com.example.android.bakingapp.data.Recipe;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lsitec205.ferreira on 03/08/17.
 */

public class JsonUtils {

    public static List<Recipe> getRecipesFromJson(String resultJson) throws JSONException {

        Gson gson = new Gson();
        ArrayList<Recipe> recipes = null;
        if(resultJson != null) {
            Type collectionType = new TypeToken<List<Recipe>>(){}.getType();
            recipes = gson.fromJson(resultJson, collectionType);
        }
        return recipes;
    }
}
