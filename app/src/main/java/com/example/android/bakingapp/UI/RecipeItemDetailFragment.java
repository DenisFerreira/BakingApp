package com.example.android.bakingapp.UI;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.data.Step;

/**
 * A fragment representing a single RecipeItem detail screen.
 * This fragment is either contained in a {@link RecipeItemListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeItemDetailActivity}
 * on handsets.
 */
public class RecipeItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    private Step mStep;
    private Integer mItemID;
    private Recipe mRecipe;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID) && getArguments().containsKey("recipe")) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mRecipe = getArguments().getParcelable("recipe");
            mItemID = getArguments().getInt(ARG_ITEM_ID, 0);
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if(mItemID > 0) {
                mStep = mRecipe.getSteps().get(mItemID - 1);
                if (appBarLayout != null) {
                    appBarLayout.setTitle(mStep.getShortDescription());
                }
            }else if (appBarLayout != null) {
                appBarLayout.setTitle(mRecipe.getName());
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        if(mItemID == 0) {
            rootView = inflater.inflate(R.layout.recipe_info_detail, container, false);
            ((TextView) rootView.findViewById(R.id.recipe_info_detail))
                    .setText(mRecipe.getServings().toString());
        }else {
            rootView = inflater.inflate(R.layout.recipeitem_detail, container, false);
            // Show the dummy content as text in a TextView.
            if (mStep != null) {
                ((TextView) rootView.findViewById(R.id.recipeitem_detail))
                        .setText(mStep.getDescription());
            }
        }

        return rootView;
    }
}
