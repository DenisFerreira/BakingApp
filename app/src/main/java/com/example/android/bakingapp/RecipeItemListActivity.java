package com.example.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.example.android.bakingapp.adapters.SimpleItemRecyclerViewAdapter;
import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.data.Step;


import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v4.app.NavUtils.navigateUpFromSameTask;

/**
 * An activity representing a list of RecipeItems. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeItemListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipeitem_list);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if(intent.hasExtra("recipe") ) {
            mRecipe = intent.getParcelableExtra("recipe");
            if(mRecipe == null)
                return;

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            toolbar.setTitle(mRecipe.getName());

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
            // Show the Up button in the action bar.
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }

            if (findViewById(R.id.recipeitem_detail_container) != null) {
                // The detail container view will be present only in the
                // large-screen layouts (res/values-w900dp).
                // If this view is present, then the
                // activity should be in two-pane mode.
                mTwoPane = true;
            }

            View recyclerView = findViewById(R.id.recipeitem_list);
            assert recyclerView != null;
            setupRecyclerView((RecyclerView) recyclerView);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, mRecipe, mTwoPane));
    }
}
