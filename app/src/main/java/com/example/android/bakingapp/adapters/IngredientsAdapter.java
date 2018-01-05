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
import com.example.android.bakingapp.UI.RecipeItemListActivity;
import com.example.android.bakingapp.data.Ingredient;
import com.example.android.bakingapp.data.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lsitec205.ferreira on 22/12/17.
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.GridViewHolder>{
    private Context mContext;
    private List<Ingredient> mIngredients;

    public IngredientsAdapter(List<Ingredient> ingredients) {
        mIngredients = ingredients;
        notifyDataSetChanged();
    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        int layoutIdForListItem = R.layout.recipe_info_list;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GridViewHolder holder, int position) {

        holder.mIngredientTextView
                .setText(String.format("%s %f %s",
                        mIngredients.get(position).getIngredient(),
                        mIngredients.get(position).getQuantity(),
                        mIngredients.get(position).getMeasure()));
    }

    @Override
    public int getItemCount() {
        if(mIngredients != null)
            return mIngredients.size();
        return 0;
    }

    static class GridViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recipe_info_ingredient_list_text) TextView mIngredientTextView;

        GridViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
