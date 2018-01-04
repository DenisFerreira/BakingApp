package com.example.android.bakingapp.widgets;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.provider.RecipeContract;

/**
 * Created by Denis on 03/01/2018.
 */

public class CheckItemService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public CheckItemService() {
        super("CheckItemService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        int ingredientCheck = intent.getIntExtra(RecipeContract.RecipeIngredientEntry.COLUMN_CHECK, 0);
        String ingredientID = intent.getStringExtra(RecipeContract.RecipeIngredientEntry.COLUMN_ID);
        Log.e("TESTE WIDGET", "ID = "+ ingredientID + " Check = "+ ingredientCheck);
        ContentValues contentValues = new ContentValues();
        String whereClause = RecipeContract.RecipeIngredientEntry.COLUMN_ID + " = ? ";
        String[] args = {ingredientID};
        if(ingredientCheck > 0)
            contentValues.put(RecipeContract.RecipeIngredientEntry.COLUMN_CHECK, 0);
        else
            contentValues.put(RecipeContract.RecipeIngredientEntry.COLUMN_CHECK, 1);
        int i = getContentResolver().update(RecipeContract.RecipeIngredientEntry.CONTENT_URI, contentValues, whereClause, args);
        //Log.e("TESTE WIDGET", "ID = "+ ingredientID + " Check = "+ ingredientCheck + " Lines updated= " + i);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingAppWidget.class));

        //Trigger data update to handle the GridView widgets and force a data refresh

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.app_widget_list_view);
        for(int id : appWidgetIds)
            BakingAppWidget.updateAppWidget(this, appWidgetManager, id);
    }
}
