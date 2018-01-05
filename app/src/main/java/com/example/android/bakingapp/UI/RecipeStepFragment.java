package com.example.android.bakingapp.UI;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.data.Step;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a single RecipeItem detail screen.
 * This fragment is either contained in a {@link RecipeItemListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeItemDetailActivity}
 * on handsets.
 */
public class RecipeStepFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    public static final String KEY_RECIPE_ID = "recipe";
    private Recipe mRecipe;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mExoPlayerView;
    private Dialog mFullScreenDialog;
    private long mVideoPosition;
    private boolean isFullscreen;
    private View mRootView;
    private String mVideoURL;

    public static String KEY_VIDEO_POSITION_BUNDLE = "key-video_position-bundle";
    private int mStepListIndex;

    @BindView(R.id.btn_previous_step) Button previousStepButton;
    @BindView(R.id.btn_next_step) Button nextStepButton;
    @BindView(R.id.tv_no_video_available) TextView noVideoAvailableTextView;
    @BindView(R.id.tv_step_description) TextView stepDescriptionTextView;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().getSupportFragmentManager().popBackStack(KEY_RECIPE_ID, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mStepListIndex = savedInstanceState.getInt(ARG_ITEM_ID);
            mRecipe = savedInstanceState.getParcelable(KEY_RECIPE_ID);
            mVideoPosition = savedInstanceState.getLong(KEY_VIDEO_POSITION_BUNDLE);
        } else {
            mStepListIndex = getArguments().getInt(ARG_ITEM_ID);
            mRecipe = getArguments().getParcelable(KEY_RECIPE_ID);
        }
        if(mRecipe != null) {
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mRecipe.getSteps().get(mStepListIndex -1).getShortDescription());
            }
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        isFullscreen = getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        mRootView = inflater.inflate(R.layout.recipe_step, container, false);
        ButterKnife.bind(this, mRootView);
        mExoPlayerView = mRootView.findViewById(R.id.exoplayer_view);

        final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mExoPlayerView.getLayoutParams();

        mFullScreenDialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
                mExoPlayerView.setLayoutParams(params);
                ((LinearLayout) mRootView.findViewById(R.id.ll_step_detail_fragment)).addView(mExoPlayerView, 1);
                isFullscreen = false;
                mFullScreenDialog.dismiss();
                super.onBackPressed();
            }
        };


        if (mRecipe != null) {
            Step mStep = mRecipe.getSteps().get(mStepListIndex - 1);
            stepDescriptionTextView.setText(mStep.getDescription());
            String thumbnailURL = mStep.getThumbnailURL();
            if (thumbnailURL != null && !thumbnailURL.equals("")) {
                final Uri thumbnailUri = Uri.parse(thumbnailURL).buildUpon().build();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Bitmap thumbnail;
                            thumbnail = Picasso.with(getActivity()).load(thumbnailUri).get();
                            mExoPlayerView.setDefaultArtwork(thumbnail);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
            mVideoURL = mStep.getVideoURL();
            if (mVideoURL == null || mVideoURL.isEmpty()) {
                mExoPlayerView.setVisibility(View.GONE);

                noVideoAvailableTextView.setVisibility(View.VISIBLE);
                noVideoAvailableTextView.setGravity(Gravity.CENTER);
            }
            if (!isFullscreen) {

                previousStepButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mStepListIndex > 0) {
                            Bundle bundle = new Bundle();
                            bundle.putParcelable(KEY_RECIPE_ID, mRecipe);
                            bundle.putInt(ARG_ITEM_ID, mStepListIndex - 1);
                            RecipeStepFragment recipeStepDetailFragment = new RecipeStepFragment();
                            recipeStepDetailFragment.setArguments(bundle);
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.recipeitem_detail_container, recipeStepDetailFragment)
                                    .addToBackStack(null)
                                    .commit();
                        } else {
                            Toast.makeText(getActivity(), "You're already on the first step.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                nextStepButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mStepListIndex < mRecipe.getSteps().size() - 1) {
                            Bundle bundle = new Bundle();
                            bundle.putParcelable(KEY_RECIPE_ID, mRecipe);
                            bundle.putInt(ARG_ITEM_ID, mStepListIndex + 1);
                            RecipeStepFragment recipeStepDetailFragment = new RecipeStepFragment();
                            recipeStepDetailFragment.setArguments(bundle);
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.recipeitem_detail_container, recipeStepDetailFragment)
                                    .addToBackStack(null)
                                    .commit();
                        } else {
                            Toast.makeText(getActivity(), "You're already on the last step.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
        return mRootView;
    }


    public void initializePlayer() {
        if (mExoPlayer == null && mVideoURL != null && !mVideoURL.isEmpty()) {
            Uri videoUri = Uri.parse(mVideoURL).buildUpon().build();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter());
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            mExoPlayerView.setPlayer(mExoPlayer);

            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                    Util.getUserAgent(getContext(), "BakingApp"), new DefaultBandwidthMeter());
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource videoSource = new ExtractorMediaSource(videoUri,
                    dataSourceFactory, extractorsFactory, null, null);
            if (isFullscreen) {
                ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
                mFullScreenDialog.addContentView(mExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                mFullScreenDialog.show();
            }
            mExoPlayer.seekTo(mVideoPosition);
            mExoPlayer.prepare(videoSource);
            mExoPlayer.setPlayWhenReady(true);
            mExoPlayerView.hideController();
        }
    }

    @Override
    public void onResume() {
        initializePlayer();
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mRecipe != null) {
            outState.putParcelable(KEY_RECIPE_ID, mRecipe);
            outState.putInt(ARG_ITEM_ID, mStepListIndex);
        }
        if(mExoPlayer!=null)
            outState.putLong(KEY_VIDEO_POSITION_BUNDLE,mExoPlayer.getCurrentPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }
}
