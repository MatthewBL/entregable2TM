package us.master.entregable2;

import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.is;

import android.Manifest;
import android.view.View;
import android.widget.DatePicker;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.FailureHandler;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;

import us.master.entregable2.entities.Trip;
import us.master.entregable2.entities.TripFunctionalities;
import us.master.entregable2.services.FirebaseDatabaseService;

@RunWith(AndroidJUnit4.class)
public class TripListTest {
    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(LoginActivity.class);

    @Rule
    public GrantPermissionRule grantFineLocationPermission = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public GrantPermissionRule grantCoarseLocationPermission = GrantPermissionRule.grant(Manifest.permission.ACCESS_COARSE_LOCATION);

    private static void BeforeTesting() {
        TestFunctionalities.BeforeTesting();
        // Click on the first CardView and check if the correct activity is opened
        Espresso.onView(ViewMatchers.withId(R.id.cardView1))
                .perform(ViewActions.click());
    }

    @Test
    public void ensureColumnToggleWorks() throws Exception {
        BeforeTesting();
        // Check if the RecyclerView's layout manager has a span count of 1
        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withLayoutManagerSpanCount(1)));

        // Find the toggle button and perform a click action on it
        Espresso.onView(withId(R.id.columnasSwitch))
                .perform(ViewActions.click());

        // Check if the RecyclerView's layout manager has a span count of 2
        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withLayoutManagerSpanCount(2)));

        // Perform another click action on the toggle button
        Espresso.onView(withId(R.id.columnasSwitch))
                .perform(ViewActions.click());

        // Check if the RecyclerView's layout manager has a span count of 1
        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withLayoutManagerSpanCount(1)));
        FirebaseDatabaseService.testing = false;
    }

    @Test
    public void testFilterActivityStartDate() throws Exception {
        // Generate the trip list
        BeforeTesting();
        LocalDate nextMonth = LocalDate.now().plusDays(30);

        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(20)));

        // Find the filter button and perform a click action on it
        Espresso.onView(withId(R.id.filtrarTextView))
                .perform(ViewActions.click());

        // Check if the Filter activity is displayed
        Espresso.onView(withId(R.id.filterActivityRootLayout))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Perform a click action on the filter option
        Espresso.onView(withId(R.id.startDateImageView))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.isAssignableFrom(DatePicker.class))
                .perform(PickerActions.setDate(nextMonth.getYear(), nextMonth.getMonthValue(), nextMonth.getDayOfMonth()));
        Espresso.onView(ViewMatchers.withText("Aceptar"))
                .perform(ViewActions.click());

        Espresso.onView(withId(R.id.button3))
                .perform(ViewActions.click());

        Thread.sleep(5000); // waits for 5 seconds

        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(13)));

        // Find the filter button and perform a click action on it
        Espresso.onView(withId(R.id.filtrarTextView))
                .perform(ViewActions.click());

        // Check if the Filter activity is displayed
        Espresso.onView(withId(R.id.filterActivityRootLayout))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Perform a click action on the filter option
        Espresso.onView(withId(R.id.startDateImageView))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("Cancelar"))
                .perform(ViewActions.click());

        Espresso.onView(withId(R.id.button3))
                .perform(ViewActions.click());

        Thread.sleep(5000); // waits for 5 seconds

        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(20)));
        FirebaseDatabaseService.testing = false;
    }

    @Test
    public void testFilterActivityEndDate() throws Exception {
        // Generate the trip list
        BeforeTesting();
        LocalDate nextMonth = LocalDate.now().plusDays(30);

        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(20)));

        // Find the filter button and perform a click action on it
        Espresso.onView(withId(R.id.filtrarTextView))
                .perform(ViewActions.click());

        // Check if the Filter activity is displayed
        Espresso.onView(withId(R.id.filterActivityRootLayout))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Perform a click action on the filter option
        Espresso.onView(withId(R.id.endDateImageView))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.isAssignableFrom(DatePicker.class))
                .perform(PickerActions.setDate(nextMonth.getYear(), nextMonth.getMonthValue(), nextMonth.getDayOfMonth()));
        Espresso.onView(ViewMatchers.withText("Aceptar"))
                .perform(ViewActions.click());

        Espresso.onView(withId(R.id.button3))
                .perform(ViewActions.click());

        Thread.sleep(5000); // waits for 5 seconds

        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(2)));

        // Find the filter button and perform a click action on it
        Espresso.onView(withId(R.id.filtrarTextView))
                .perform(ViewActions.click());

        // Check if the Filter activity is displayed
        Espresso.onView(withId(R.id.filterActivityRootLayout))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Perform a click action on the filter option
        Espresso.onView(withId(R.id.endDateImageView))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("Cancelar"))
                .perform(ViewActions.click());

        Espresso.onView(withId(R.id.button3))
                .perform(ViewActions.click());

        Thread.sleep(5000); // waits for 5 seconds

        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(20)));
        FirebaseDatabaseService.testing = false;
    }

    @Test
    public void testFilterActivityBothDates() throws Exception {
        // Generate the trip list
        BeforeTesting();
        LocalDate nextMonth = LocalDate.now().plusDays(30);
        LocalDate nextNextMonth = LocalDate.now().plusDays(60);

        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(20)));

        // Find the filter button and perform a click action on it
        Espresso.onView(withId(R.id.filtrarTextView))
                .perform(ViewActions.click());

        // Check if the Filter activity is displayed
        Espresso.onView(withId(R.id.filterActivityRootLayout))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Perform a click action on the filter option
        Espresso.onView(withId(R.id.startDateImageView))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.isAssignableFrom(DatePicker.class))
                .perform(PickerActions.setDate(nextMonth.getYear(), nextMonth.getMonthValue(), nextMonth.getDayOfMonth()));
        Espresso.onView(ViewMatchers.withText("Aceptar"))
                .perform(ViewActions.click());
        Espresso.onView(withId(R.id.endDateImageView))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.isAssignableFrom(DatePicker.class))
                .perform(PickerActions.setDate(nextNextMonth.getYear(), nextNextMonth.getMonthValue(), nextNextMonth.getDayOfMonth()));
        Espresso.onView(ViewMatchers.withText("Aceptar"))
                .perform(ViewActions.click());

        Espresso.onView(withId(R.id.button3))
                .perform(ViewActions.click());

        Thread.sleep(5000); // waits for 5 seconds

        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(10)));

        // Find the filter button and perform a click action on it
        Espresso.onView(withId(R.id.filtrarTextView))
                .perform(ViewActions.click());

        // Check if the Filter activity is displayed
        Espresso.onView(withId(R.id.filterActivityRootLayout))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Perform a click action on the filter option
        Espresso.onView(withId(R.id.startDateImageView))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("Cancelar"))
                .perform(ViewActions.click());
        Espresso.onView(withId(R.id.endDateImageView))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("Cancelar"))
                .perform(ViewActions.click());

        Espresso.onView(withId(R.id.button3))
                .perform(ViewActions.click());

        Thread.sleep(5000); // waits for 5 seconds

        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(20)));
        FirebaseDatabaseService.testing = false;
    }

    @Test
    public void testFilterMinPrice() {
        BeforeTesting();

        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(20)));

        // Find the filter button and perform a click action on it
        Espresso.onView(withId(R.id.filtrarTextView))
                .perform(ViewActions.click());

        // Check if the Filter activity is displayed
        Espresso.onView(withId(R.id.filterActivityRootLayout))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(withId(R.id.minPriceSeekBar))
                .perform(EspressoTestMatchers.setSeekBarProgress(200));
        Espresso.onView(withId(R.id.minPriceSeekBar))
                .check(ViewAssertions.matches(EspressoTestMatchers.withSeekBarProgress(200)));

        Espresso.onView(withId(R.id.button3))
                .perform(ViewActions.click());

        try {
            Thread.sleep(5000); // waits for 5 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(14)));

        // Find the filter button and perform a click action on it
        Espresso.onView(withId(R.id.filtrarTextView))
                .perform(ViewActions.click());

        // Check if the Filter activity is displayed
        Espresso.onView(withId(R.id.filterActivityRootLayout))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(withId(R.id.minPriceSeekBar))
                .perform(EspressoTestMatchers.setSeekBarProgress(0));
        Espresso.onView(withId(R.id.minPriceSeekBar))
                .check(ViewAssertions.matches(EspressoTestMatchers.withSeekBarProgress(0)));

        Espresso.onView(withId(R.id.button3))
                .perform(ViewActions.click());

        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(20)));
        FirebaseDatabaseService.testing = false;
    }

    @Test
    public void testFilterMaxPrice() {
        BeforeTesting();

        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(20)));

        // Find the filter button and perform a click action on it
        Espresso.onView(withId(R.id.filtrarTextView))
                .perform(ViewActions.click());

        // Check if the Filter activity is displayed
        Espresso.onView(withId(R.id.filterActivityRootLayout))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(withId(R.id.maxPriceSeekBar))
                .perform(EspressoTestMatchers.setSeekBarProgress(300));
        Espresso.onView(withId(R.id.maxPriceSeekBar))
                .check(ViewAssertions.matches(EspressoTestMatchers.withSeekBarProgress(300)));

        Espresso.onView(withId(R.id.button3))
                .perform(ViewActions.click());

        try {
            Thread.sleep(5000); // waits for 5 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(15)));

        // Find the filter button and perform a click action on it
        Espresso.onView(withId(R.id.filtrarTextView))
                .perform(ViewActions.click());

        // Check if the Filter activity is displayed
        Espresso.onView(withId(R.id.filterActivityRootLayout))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(withId(R.id.maxPriceSeekBar))
                .perform(EspressoTestMatchers.setSeekBarProgress(1000));
        Espresso.onView(withId(R.id.maxPriceSeekBar))
                .check(ViewAssertions.matches(EspressoTestMatchers.withSeekBarProgress(1000)));

        Espresso.onView(withId(R.id.button3))
                .perform(ViewActions.click());

        try {
            Thread.sleep(5000); // waits for 5 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(20)));
        FirebaseDatabaseService.testing = false;
    }
    @Test
    public void testFilterBothPrices() {
        BeforeTesting();

        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(20)));

        // Find the filter button and perform a click action on it
        Espresso.onView(withId(R.id.filtrarTextView))
                .perform(ViewActions.click());

        // Check if the Filter activity is displayed
        Espresso.onView(withId(R.id.filterActivityRootLayout))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(withId(R.id.minPriceSeekBar))
                .perform(EspressoTestMatchers.setSeekBarProgress(200));
        Espresso.onView(withId(R.id.minPriceSeekBar))
                .check(ViewAssertions.matches(EspressoTestMatchers.withSeekBarProgress(200)));
        Espresso.onView(withId(R.id.maxPriceSeekBar))
                .perform(EspressoTestMatchers.setSeekBarProgress(300));
        Espresso.onView(withId(R.id.maxPriceSeekBar))
                .check(ViewAssertions.matches(EspressoTestMatchers.withSeekBarProgress(300)));

        Espresso.onView(withId(R.id.button3))
                .perform(ViewActions.click());

        try {
            Thread.sleep(5000); // waits for 5 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(9)));

        // Find the filter button and perform a click action on it
        Espresso.onView(withId(R.id.filtrarTextView))
                .perform(ViewActions.click());

        // Check if the Filter activity is displayed
        Espresso.onView(withId(R.id.filterActivityRootLayout))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(withId(R.id.minPriceSeekBar))
                .perform(EspressoTestMatchers.setSeekBarProgress(0));
        Espresso.onView(withId(R.id.minPriceSeekBar))
                .check(ViewAssertions.matches(EspressoTestMatchers.withSeekBarProgress(0)));
        Espresso.onView(withId(R.id.maxPriceSeekBar))
                .perform(EspressoTestMatchers.setSeekBarProgress(1000));
        Espresso.onView(withId(R.id.maxPriceSeekBar))
                .check(ViewAssertions.matches(EspressoTestMatchers.withSeekBarProgress(1000)));

        Espresso.onView(withId(R.id.button3))
                .perform(ViewActions.click());

        try {
            Thread.sleep(5000); // waits for 5 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(20)));
        FirebaseDatabaseService.testing = false;
    }

    @Test
    public void testFilterAirportsWithinRange() {
        BeforeTesting();

        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(20)));

        // Find the filter button and perform a click action on it
        Espresso.onView(withId(R.id.filtrarTextView))
                .perform(ViewActions.click());

        // Check if the Filter activity is displayed
        Espresso.onView(withId(R.id.filterActivityRootLayout))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Replace R.id.myCheckBox with the actual ID of your checkbox
        Espresso.onView(withId(R.id.airportCheckBox))
                .perform(ViewActions.click())
                .withFailureHandler(new FailureHandler() {
                    @Override
                    public void handle(Throwable error, Matcher<View> viewMatcher) {
                        throw new AssertionError("Cannot click the checkbox! Perhaps you didn't enable location services?", error);
                    }
                });

        Espresso.onView(withId(R.id.button3))
                .perform(ViewActions.click());

        try {
            Thread.sleep(5000); // waits for 5 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(2)))
                .withFailureHandler(new FailureHandler() {
                    @Override
                    public void handle(Throwable error, Matcher<View> viewMatcher) {
                        throw new AssertionError("Airport count does not match! Remember to enable location services and throw this test from Sevilla!", error);
                    }
                });;

        // Find the filter button and perform a click action on it
        Espresso.onView(withId(R.id.filtrarTextView))
                .perform(ViewActions.click());

        // Check if the Filter activity is displayed
        Espresso.onView(withId(R.id.filterActivityRootLayout))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Replace R.id.myCheckBox with the actual ID of your checkbox
        Espresso.onView(withId(R.id.airportCheckBox)).perform(ViewActions.click());

        Espresso.onView(withId(R.id.button3))
                .perform(ViewActions.click());

        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(20)));
        FirebaseDatabaseService.testing = false;
    }

    @Test
    public void testAllFilters() {
        BeforeTesting();
        LocalDate nextMonth = LocalDate.now().plusDays(30);
        LocalDate nextNextMonth = LocalDate.now().plusDays(60);

        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(20)));

        // Find the filter button and perform a click action on it
        Espresso.onView(withId(R.id.filtrarTextView))
                .perform(ViewActions.click());

        // Check if the Filter activity is displayed
        Espresso.onView(withId(R.id.filterActivityRootLayout))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(withId(R.id.startDateImageView))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.isAssignableFrom(DatePicker.class))
                .perform(PickerActions.setDate(nextMonth.getYear(), nextMonth.getMonthValue(), nextMonth.getDayOfMonth()));
        Espresso.onView(ViewMatchers.withText("Aceptar"))
                .perform(ViewActions.click());
        Espresso.onView(withId(R.id.endDateImageView))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.isAssignableFrom(DatePicker.class))
                .perform(PickerActions.setDate(nextNextMonth.getYear(), nextNextMonth.getMonthValue(), nextNextMonth.getDayOfMonth()));
        Espresso.onView(ViewMatchers.withText("Aceptar"))
                .perform(ViewActions.click());
        Espresso.onView(withId(R.id.minPriceSeekBar))
                .perform(EspressoTestMatchers.setSeekBarProgress(200));
        Espresso.onView(withId(R.id.minPriceSeekBar))
                .check(ViewAssertions.matches(EspressoTestMatchers.withSeekBarProgress(200)));
        Espresso.onView(withId(R.id.maxPriceSeekBar))
                .perform(EspressoTestMatchers.setSeekBarProgress(300));
        Espresso.onView(withId(R.id.maxPriceSeekBar))
                .check(ViewAssertions.matches(EspressoTestMatchers.withSeekBarProgress(300)));

        Espresso.onView(withId(R.id.button3))
                .perform(ViewActions.click());

        try {
            Thread.sleep(5000); // waits for 5 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(3)));

        // Find the filter button and perform a click action on it
        Espresso.onView(withId(R.id.filtrarTextView))
                .perform(ViewActions.click());

        // Check if the Filter activity is displayed
        Espresso.onView(withId(R.id.filterActivityRootLayout))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(withId(R.id.startDateImageView))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("Cancelar"))
                .perform(ViewActions.click());
        Espresso.onView(withId(R.id.endDateImageView))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("Cancelar"))
                .perform(ViewActions.click());
        Espresso.onView(withId(R.id.minPriceSeekBar))
                .perform(EspressoTestMatchers.setSeekBarProgress(0));
        Espresso.onView(withId(R.id.minPriceSeekBar))
                .check(ViewAssertions.matches(EspressoTestMatchers.withSeekBarProgress(0)));
        Espresso.onView(withId(R.id.maxPriceSeekBar))
                .perform(EspressoTestMatchers.setSeekBarProgress(1000));
        Espresso.onView(withId(R.id.maxPriceSeekBar))
                .check(ViewAssertions.matches(EspressoTestMatchers.withSeekBarProgress(1000)));

        Espresso.onView(withId(R.id.button3))
                .perform(ViewActions.click());

        try {
            Thread.sleep(5000); // waits for 5 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(20)));
        FirebaseDatabaseService.testing = false;
    }
}