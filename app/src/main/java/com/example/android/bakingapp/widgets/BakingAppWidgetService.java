package com.example.android.bakingapp.widgets;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.provider.RecipeContract;


/**
 * Created by lsitec205.ferreira on 29/12/17.
 */

public class BakingAppWidgetService extends RemoteViewsService {

    public static final String ACTION_CLICK_RECIPE = "com.example.android.bakingapp.action.click_recipe";
    private Intent mIntent;

    private class MyRemoteViewFactory implements RemoteViewsFactory {

        private Cursor data = null;
        private Context mContext;
        private Intent mIntent;

        public MyRemoteViewFactory(Context context, Intent intent) {
            mContext = context;
            mIntent = intent;
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
            if (data != null)
                data.close();
            int recipeID = 0;
            if(mIntent.hasExtra("recipeID")) {
                 recipeID = mIntent.getIntExtra("recipeID", 0);
            }
            String[] args = {String.valueOf(recipeID)};
            data = getContentResolver().query(RecipeContract.RecipeIngredientEntry.CONTENT_URI,
                    null,
                    RecipeContract.RecipeIngredientEntry.COLUMN_RECIPE_ID + "=?",
                    args,
                    null);

        }

        @Override
        public void onDestroy() {
            if (data != null) {
                data.close();
                data = null;
            }
        }

        @Override
        public int getCount() {
            return data == null ? 0 : data.getCount();
        }

        @Override
        public RemoteViews getViewAt(int i) {
            RemoteViews views;
            views = new RemoteViews(mContext.getPackageName(), R.layout.baking_app_widget_item_list);
            if (i == AdapterView.INVALID_POSITION || data == null || !data.moveToPosition(i))
                return views;
            String result =
                    data.getString(data.getColumnIndex(RecipeContract.RecipeIngredientEntry.COLUMN_INGREDIENT)) + " "+
                    data.getString(data.getColumnIndex(RecipeContract.RecipeIngredientEntry.COLUMN_QUANTITY)) + " "+
                    data.getString(data.getColumnIndex(RecipeContract.RecipeIngredientEntry.COLUMN_MEASURE));

            views.setTextViewText(R.id.appwidget_list_item_text, result);
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return data.moveToPosition(position) ? data.getLong(0) : position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        mIntent = intent;
        return new MyRemoteViewFactory(this.getApplicationContext(), intent);
    }

}
