package us.master.entregable2;

import androidx.annotation.NonNull;
import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.Espresso;

import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;

import android.content.Intent;
import android.util.Log;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

import us.master.entregable2.entities.Trip;
import us.master.entregable2.entities.TripFunctionalities;
import us.master.entregable2.entities.User;
import us.master.entregable2.services.FirebaseDatabaseService;
import us.master.entregable2.services.LocalPreferences;
import us.master.entregable2.services.UserCallback;

import android.Manifest;
import android.view.View;

import androidx.test.rule.GrantPermissionRule;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(LoginActivity.class);

    @Rule
    public GrantPermissionRule grantFineLocationPermission = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public GrantPermissionRule grantCoarseLocationPermission = GrantPermissionRule.grant(Manifest.permission.ACCESS_COARSE_LOCATION);

    @Test
    public void ensureCardViewsArePresent() throws Exception {
        TestFunctionalities.BeforeTesting();
        FirebaseDatabaseService.testing = true;
        // Check if the CardViews are displayed on the screen
        Espresso.onView(ViewMatchers.withId(R.id.cardView1))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.cardView2))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.cardView3))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        FirebaseDatabaseService.testing = false;
    }

    @Test
    public void ensureCardViewClickOpensCorrectActivity() throws Exception {
        TestFunctionalities.BeforeTesting();
        // Click on the first CardView and check if the correct activity is opened
        Espresso.onView(ViewMatchers.withId(R.id.cardView1))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(20)));

        // Go back to MainActivity
        Espresso.pressBack();

        // Click on the second CardView and check if the correct activity is opened
        Espresso.onView(ViewMatchers.withId(R.id.cardView2))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(0)));
        FirebaseDatabaseService.testing = false;
    }

    @Test
    public void testSelectTrip() throws Exception {
        TestFunctionalities.BeforeTesting();
        boolean columnMode = false;
        testSelectedTripStage1(columnMode);
        testSelectedTripStage2(columnMode);
        testSelectedTripStage3(columnMode);
        testSelectedTripStage2(columnMode);
        testSelectedTripStage4(columnMode);
        testSelectedTripStage5(columnMode);
        FirebaseDatabaseService.testing = false;
    }

    @Test
    public void testSelectTripColumns() throws Exception {
        TestFunctionalities.BeforeTesting();
        boolean columnMode = true;
        testSelectedTripStage1(columnMode);
        testSelectedTripStage2(columnMode);
        testSelectedTripStage3(columnMode);
        testSelectedTripStage2(columnMode);
        testSelectedTripStage4(columnMode);
        testSelectedTripStage5(columnMode);
        FirebaseDatabaseService.testing = false;
    }

    private void testSelectedTripStage1(boolean columnMode) {
        Espresso.onView(ViewMatchers.withId(R.id.cardView1))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Column mode
        if (columnMode) {
            Espresso.onView(withId(R.id.columnasSwitch))
                    .perform(ViewActions.click());
        }

        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(20)));

        // Go back to MainActivity
        Espresso.pressBack();

        Espresso.onView(ViewMatchers.withId(R.id.cardView2))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Column mode
        if (columnMode) {
            Espresso.onView(withId(R.id.columnasSwitch))
                    .perform(ViewActions.click());
        }

        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(0)));

        // Go back to MainActivity
        Espresso.pressBack();
    }

    private void testSelectedTripStage2(boolean columnMode) {
        Espresso.onView(ViewMatchers.withId(R.id.cardView1))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Column mode
        if (columnMode) {
            Espresso.onView(withId(R.id.columnasSwitch))
                    .perform(ViewActions.click());
        }

        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(20)));

        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(4, // position is 0-indexed
                        EspressoTestMatchers.clickChildViewWithId(R.id.imageViewRight)));

        // Go back to MainActivity
        Espresso.pressBack();
    }

    private void testSelectedTripStage3(boolean columnMode) {
        Espresso.onView(ViewMatchers.withId(R.id.cardView2))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Column mode
        if (columnMode) {
            Espresso.onView(withId(R.id.columnasSwitch))
                    .perform(ViewActions.click());
        }

        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(1)));

        // Deselect the trip
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, // position is 0-indexed
                        EspressoTestMatchers.clickChildViewWithId(R.id.imageViewRight)));

        try {
            Thread.sleep(1000); // waits for 1 second
        }
        catch (InterruptedException e) {
            Log.e("MainActivityTest", "Error sleeping");
        }

        // Verify that there's no selected trips
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(0)));

        // Go back to MainActivity
        Espresso.pressBack();
    }

    private void testSelectedTripStage4(boolean columnMode) {
        Espresso.onView(ViewMatchers.withId(R.id.cardView2))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Column mode
        if (columnMode) {
            Espresso.onView(withId(R.id.columnasSwitch))
                    .perform(ViewActions.click());
        }

        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(1)));

        // Buy trip
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, // position is 0-indexed
                        EspressoTestMatchers.clickChildViewWithId(R.id.cartIcon)));

        try {
            Thread.sleep(1000); // waits for 1 second
        }
        catch (InterruptedException e) {
            Log.e("MainActivityTest", "Error sleeping");
        }

        // Verify that there's no selected trips
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(0)));

        // Go back to MainActivity
        Espresso.pressBack();
    }

    private void testSelectedTripStage5(boolean columnMode) {
        Espresso.onView(ViewMatchers.withId(R.id.cardView3))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Column mode
        if (columnMode) {
            Espresso.onView(withId(R.id.columnasSwitch))
                    .perform(ViewActions.click());
        }

        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(1)));

        // Verify that there's no selected trips
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(1)));

        // Go back to MainActivity
        Espresso.pressBack();
    }
    @Test
    public void testTripDetails() {
        TestFunctionalities.BeforeTesting();

        Espresso.onView(ViewMatchers.withId(R.id.cardView1))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(20)));

        testTripDetailsCommonMethod();
        FirebaseDatabaseService.testing = false;
    }

    @Test
    public void testTripDetailsColumn() {
        TestFunctionalities.BeforeTesting();

        Espresso.onView(ViewMatchers.withId(R.id.cardView1))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(20)));

        // Column mode
        Espresso.onView(withId(R.id.columnasSwitch))
                .perform(ViewActions.click());

        testTripDetailsCommonMethod();
        FirebaseDatabaseService.testing = false;
    }

    private void testTripDetailsCommonMethod() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;

        // See details of one trip
        Espresso.onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.click()));

        try {
            Thread.sleep(10000); // waits for 10 seconds
        }
        catch (InterruptedException e) {
            Log.e("MainActivityTest", "Error sleeping");
        }

        LocalDate romeDepartureDate = LocalDate.now().plusDays(23);
        LocalDate romeArrivalDate = LocalDate.now().plusDays(37);

        Espresso.onView(withId(R.id.textView5))
                .check(ViewAssertions.matches(withText("Roma")));
        Espresso.onView(withId(R.id.priceValue))
                .check(ViewAssertions.matches(withText("256,20 €")));
        Espresso.onView(withId(R.id.departureDateValue))
                .check(ViewAssertions.matches(withText(romeDepartureDate.format(formatter))));
        Espresso.onView(withId(R.id.arrivalDateValue))
                .check(ViewAssertions.matches(withText(romeArrivalDate.format(formatter))));
        Espresso.onView(withId(R.id.startPointValue))
                .check(ViewAssertions.matches(withText("Madrid")));
        Espresso.onView(withId(R.id.selectedValue))
                .check(ViewAssertions.matches(withTagValue(is((Object) "android.R.drawable.star_big_off"))));

        // Go back to MainActivity
        Espresso.pressBack();

        // Select one trip
        Espresso.onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, // position is 0-indexed
                        EspressoTestMatchers.clickChildViewWithId(R.id.imageViewRight)));

        // See details of one trip
        Espresso.onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, ViewActions.click()));

        try {
            Thread.sleep(10000); // waits for 10 seconds
        }
        catch (InterruptedException e) {
            Log.e("MainActivityTest", "Error sleeping");
        }

        LocalDate tokyoDepartureDate = LocalDate.now().plusDays(37);
        LocalDate tokyoArrivalDate = LocalDate.now().plusDays(54);

        // Verify destination
        Espresso.onView(withId(R.id.textView5))
                .check(ViewAssertions.matches(withText("Tokio")));
        Espresso.onView(withId(R.id.priceValue))
                .check(ViewAssertions.matches(withText("243,79 €")));
        Espresso.onView(withId(R.id.departureDateValue))
                .check(ViewAssertions.matches(withText(tokyoDepartureDate.format(formatter))));
        Espresso.onView(withId(R.id.arrivalDateValue))
                .check(ViewAssertions.matches(withText(tokyoArrivalDate.format(formatter))));
        Espresso.onView(withId(R.id.startPointValue))
                .check(ViewAssertions.matches(withText("Santander")));
        Espresso.onView(withId(R.id.selectedValue))
                .check(ViewAssertions.matches(withTagValue(is((Object) "android.R.drawable.star_big_on"))));
    }
}