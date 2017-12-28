package com.example.android.bakingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.RecipeItemDetailActivity;
import com.example.android.bakingapp.RecipeItemDetailFragment;
import com.example.android.bakingapp.RecipeItemListActivity;
import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.data.Step;

import java.util.ListIterator;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lsitec205.ferreira on 27/12/17.
 */

public class SimpleItemRecyclerViewAdapter
        extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

    private final RecipeItemListActivity mParentActivity;
    private final Recipe mRecipe;
    private final boolean mTwoPane;
    private final ListIterator<Step> mStepsIterator;


    public SimpleItemRecyclerViewAdapter(RecipeItemListActivity parent,
                                         Recipe recipe,
                                         boolean twoPane) {
        mRecipe = recipe;
        mParentActivity = parent;
        mTwoPane = twoPane;
        mStepsIterator = mRecipe.getSteps().listIterator();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipeitem_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if(position == 0)
            holder.mContentView.setText(R.string.recipe_ingredients);
        else {
            if(mStepsIterator.hasNext()) {
                final Step step = mStepsIterator.next();
                holder.mIdView.setText(step.getId().toString());
                holder.mContentView.setText(step.getShortDescription());
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTwoPane) {
                    RecipeItemDetailFragment fragment = new RecipeItemDetailFragment();
                    Bundle arguments = new Bundle();
                    arguments.putInt(RecipeItemDetailFragment.ARG_ITEM_ID, position);
                    arguments.putParcelable("recipe", mRecipe);
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.recipeitem_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, RecipeItemDetailActivity.class);
                    intent.putExtra("recipe", mRecipe);
                    intent.putExtra(RecipeItemDetailFragment.ARG_ITEM_ID, position);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mRecipe == null)return 0;
        return mRecipe.getIngredients().size() +1;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.id_text)
        TextView mIdView;
        @BindView(R.id.content) TextView mContentView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
