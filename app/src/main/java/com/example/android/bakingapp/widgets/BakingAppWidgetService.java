package com.example.android.bakingapp.widgets;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

/**
 * Created by lsitec205.ferreira on 29/12/17.
 */

public class BakingAppWidgetService extends RemoteViewsService {

    public static final String ACTION_CLICK_RECIPE = "com.example.android.bakingapp.action.click_recipe";

    private class MyRemoteViewService implements RemoteViewsFactory {

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public RemoteViews getViewAt(int i) {
            return null;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 0;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return null;
    }

}
