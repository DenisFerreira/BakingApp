package com.example.android.bakingapp.UI;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapters.IngredientsAdapter;
import com.example.android.bakingapp.data.Ingredient;
import com.example.android.bakingapp.data.Recipe;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a single RecipeItem detail screen.
 * This fragment is either contained in a {@link RecipeItemListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeItemDetailActivity}
 * on handsets.
 */
public class RecipeInfoFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    private Recipe mRecipe;
    @BindView(R.id.recipe_info_image) ImageView mInfoImageView;
    @BindView(R.id.recipe_info_name) TextView mInfoName;
    @BindView(R.id.recipe_info_servings) TextView mInfoServings;
    @BindView(R.id.recipe_info_list)
    RecyclerView mInfoListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID) && getArguments().containsKey("recipe")) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mRecipe = getArguments().getParcelable("recipe");
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mRecipe.getName());
            }

        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;

        rootView = inflater.inflate(R.layout.recipe_info, container, false);

        ButterKnife.bind(this, rootView);
        mInfoName.setText(mRecipe.getName());
        mInfoServings.setText(String.format("%d %s",
                mRecipe.getServings(),
                getResources().getString(R.string.portions_label)));
        if(!mRecipe.getImage().isEmpty())
            Picasso.with(getContext()).load(mRecipe.getImage())
                    .placeholder(R.drawable.ic_placeholder)
                    .into(mInfoImageView);
        else
            mInfoImageView.setImageDrawable(getContext().getResources()
                    .getDrawable(R.drawable.ic_no_image_recipe, null));
        IngredientsAdapter adapter = new IngredientsAdapter(mRecipe.getIngredients());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mInfoListView.setLayoutManager(layoutManager);
        mInfoListView.setHasFixedSize(true);
        mInfoListView.setAdapter(adapter);

        return rootView;
    }
}
