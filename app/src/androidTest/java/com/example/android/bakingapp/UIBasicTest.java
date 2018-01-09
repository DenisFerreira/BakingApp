package com.example.android.bakingapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakingapp.UI.RecipeListActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.espresso.contrib.RecyclerViewActions;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by lsitec205.ferreira on 09/01/18.
 */

@RunWith(AndroidJUnit4.class)
public class UIBasicTest {
    private static final String RECIPE_NAME = "Brownies";
    @Rule public ActivityTestRule<RecipeListActivity> mActivityTestRule
            = new ActivityTestRule<RecipeListActivity>(RecipeListActivity.class);

    @Test
    public void checkRecyclerViewRecipeText() {
        onView(withId(R.id.recipes_rc)).perform(RecyclerViewActions.scrollToPosition(2));
        onView(withText("Yellow Cake")).check(matches(isDisplayed()));
    }

    @Test
    public void checkDetailActivityStep() {
        onView(withId(R.id.recipes_rc)).perform(RecyclerViewActions.actionOnItemAtPosition(1,click()));
        onView(withId(R.id.recipeitem_list)).check(matches(isDisplayed()));
    }
}
