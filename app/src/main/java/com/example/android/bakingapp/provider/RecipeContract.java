package com.example.android.bakingapp.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by lsitec205.ferreira on 29/12/17.
 */

public class RecipeContract {
    public static final String CONTENT_AUTHORITY = "com.example.android.bakingapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_RECIPE = "recipe";

    public static final class RecipeEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_RECIPE)
                .build();
    }
}
