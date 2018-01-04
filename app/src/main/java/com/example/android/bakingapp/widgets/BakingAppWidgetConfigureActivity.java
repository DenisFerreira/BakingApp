package com.example.android.bakingapp.widgets;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.provider.RecipeContract;

import java.util.ArrayList;

/**
 * The configuration screen for the {@link BakingAppWidget BakingAppWidget} AppWidget.
 */
public class BakingAppWidgetConfigureActivity extends Activity {

    private static final String PREFS_NAME = "com.example.android.bakingapp.widgets.BakingAppWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    private static final String PREF_ID = "_pref_id";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    Spinner mAppWidgetSpinner;
    Button mButton;
    private Cursor mRecipesData;


    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = BakingAppWidgetConfigureActivity.this;

            // When the button is clicked, store the string locally
            mRecipesData.moveToPosition(mAppWidgetSpinner.getSelectedItemPosition());
            int id = mRecipesData.getInt(mRecipesData.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_ID));
            String title = mRecipesData.getString(mRecipesData.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_NAME));
            saveTitlePref(context, mAppWidgetId, title);
            saveRecipeIdPref(context, mAppWidgetId, id);

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            BakingAppWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    public BakingAppWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.widget_ingredients_configure);
        mAppWidgetSpinner = findViewById(R.id.recipe_spinner);
        mButton = findViewById(R.id.add_button);
        mButton.setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        mRecipesData = getContentResolver().query(RecipeContract.RecipeEntry.CONTENT_URI,
                new String[]{RecipeContract.RecipeEntry.COLUMN_ID, RecipeContract.RecipeEntry.COLUMN_NAME},
                null,
                null,
                null);
        ArrayList<String> recipesName = new ArrayList<>();
        if (mRecipesData != null) {
            mButton.setClickable(true);
            while(mRecipesData.moveToNext()){
                recipesName.add(mRecipesData.getString(
                        mRecipesData.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_NAME)));
            }
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, recipesName);
        mAppWidgetSpinner.setAdapter(dataAdapter);

    }

    public static int loadRecipeId(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getInt(PREF_PREFIX_KEY + appWidgetId + PREF_ID, 0);
    }

    static void saveRecipeIdPref(Context context, int appWidgetId, int recipeID) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putInt(PREF_PREFIX_KEY + appWidgetId + PREF_ID, recipeID);
        prefs.apply();
    }
}

