package com.example.android.bakingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.RecipeItemListActivity;
import com.example.android.bakingapp.data.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lsitec205.ferreira on 22/12/17.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.GridViewHolder>{
    private Context mContext;
    private List<Recipe> mRecipes;

    public void setData(List<Recipe> recipes) {
        mRecipes = recipes;
        notifyDataSetChanged();
    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        int layoutIdForListItem = R.layout.banner_recipe_list;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GridViewHolder holder, int position) {
        final Recipe recipe = mRecipes.get(position);
        if(!recipe.getImage().isEmpty())
            Picasso.with(mContext).load(recipe.getImage())
                .placeholder(R.drawable.ic_placeholder)
                .into(holder.mBannerImage);
        holder.mBannerText.setText(recipe.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RecipeItemListActivity.class);
                intent.putExtra("recipe", recipe);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mRecipes != null)
            return mRecipes.size();
        return 0;
    }

    static class GridViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_banner_recipe_list) ImageView mBannerImage;
        @BindView(R.id.banner_text) TextView mBannerText;

        GridViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
