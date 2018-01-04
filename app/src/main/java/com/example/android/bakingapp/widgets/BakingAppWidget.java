package com.example.android.bakingapp.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.android.bakingapp.R;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link BakingAppWidgetConfigureActivity BakingAppWidgetConfigureActivity}
 */
public class BakingAppWidget extends AppWidgetProvider {

    static public String RECIPE_ID_EXTRA = "com.example.android.bakingapp.widget.recipeID";
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_ingredients);
        String title = BakingAppWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        int recipeID = BakingAppWidgetConfigureActivity.loadRecipeId(context, appWidgetId);

        String widgetTitle = title + " " +
                context.getResources().getString(R.string.ingredient_list);

        views.setTextViewText(R.id.appwidget_text, widgetTitle);
        Intent intent = new Intent(context, BakingAppWidgetService.class);
        intent.putExtra(RECIPE_ID_EXTRA,
                recipeID);
        views.setRemoteAdapter(R.id.app_widget_list_view, intent);

        Intent appIntent = new Intent(context, CheckItemService.class);
        PendingIntent appPendingIntent = PendingIntent.getService(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.app_widget_list_view, appPendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            BakingAppWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

