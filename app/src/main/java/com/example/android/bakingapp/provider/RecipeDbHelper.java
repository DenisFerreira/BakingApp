package com.example.android.bakingapp.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecipeDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "baking_app_database.db";
    private static final int VERSION = 1;

    RecipeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_RECIPE_TABLE = "CREATE TABLE " +
                RecipeContract.RecipeEntry.TABLE_NAME + " (" +
                RecipeContract.RecipeEntry.COLUMN_ID + " INTEGER PRIMARY KEY, " +
                RecipeContract.RecipeEntry.COLUMN_NAME + " TEXT, " +
                RecipeContract.RecipeEntry.COLUMN_SERVINGS + " TEXT, " +
                RecipeContract.RecipeEntry.COLUMN_IMAGE + " TEXT);";
        db.execSQL(CREATE_RECIPE_TABLE);

        final String CREATE_INGREDIENT_TABLE = "CREATE TABLE " +
                RecipeContract.RecipeIngredientEntry.TABLE_NAME + " (" +
                RecipeContract.RecipeIngredientEntry.COLUMN_ID + " TEXT PRIMARY KEY, " +
                RecipeContract.RecipeIngredientEntry.COLUMN_RECIPE_ID + " TEXT, " +
                RecipeContract.RecipeIngredientEntry.COLUMN_INGREDIENT + " TEXT, " +
                RecipeContract.RecipeIngredientEntry.COLUMN_MEASURE + " TEXT, " +
                RecipeContract.RecipeIngredientEntry.COLUMN_CHECK + " INTEGER NOT NULL DEFAULT 0, " +
                RecipeContract.RecipeIngredientEntry.COLUMN_QUANTITY + " TEXT);";
        db.execSQL(CREATE_INGREDIENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RecipeContract.RecipeEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RecipeContract.RecipeIngredientEntry.TABLE_NAME);
    }
}
