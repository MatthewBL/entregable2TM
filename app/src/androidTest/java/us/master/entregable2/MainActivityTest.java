package us.master.entregable2;

import androidx.test.espresso.Espresso;

import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import us.master.entregable2.entities.Trip;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void ensureCardViewsArePresent() throws Exception {
        // Check if the CardViews are displayed on the screen
        Espresso.onView(ViewMatchers.withId(R.id.cardView1))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.cardView2))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void ensureCardViewClickOpensCorrectActivity() throws Exception {
        Trip.generateTestTripList(123456789);

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
    }
    @Test
    public void testSelectTrip() throws Exception {
        Trip.generateTestTripList(123456789);

        testSelectedTripStage1();

        testSelectedTripStage2();

        testSelectedTripStage3();
    }
    @Test
    public void testSelectTripColumns() throws Exception {
        Trip.generateTestTripList(123456789);

        testSelectedTripStage1();

        // Column mode
        Espresso.onView(withId(R.id.columnasSwitch))
                .perform(ViewActions.click());

        testSelectedTripStage2();

        // Column mode
        Espresso.onView(withId(R.id.columnasSwitch))
                .perform(ViewActions.click());

        testSelectedTripStage3();
    }

    private void testSelectedTripStage1() {
        Espresso.onView(ViewMatchers.withId(R.id.cardView1))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(20)));

        // Go back to MainActivity
        Espresso.pressBack();

        Espresso.onView(ViewMatchers.withId(R.id.cardView2))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(0)));

        // Go back to MainActivity
        Espresso.pressBack();

        Espresso.onView(ViewMatchers.withId(R.id.cardView1))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(20)));
    }

    private void testSelectedTripStage2() {
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(4, // position is 0-indexed
                        EspressoTestMatchers.clickChildViewWithId(R.id.imageViewRight)));

        // Go back to MainActivity
        Espresso.pressBack();

        Espresso.onView(ViewMatchers.withId(R.id.cardView2))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(1)));
    }

    private void testSelectedTripStage3() {
        // Buy trip
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, // position is 0-indexed
                        EspressoTestMatchers.clickChildViewWithId(R.id.cartIcon)));

        // Deselect the trip
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, // position is 0-indexed
                        EspressoTestMatchers.clickChildViewWithId(R.id.imageViewRight)));

        // Verify that there's no selected trips
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(0)));
    }
    @Test
    public void testTripDetails() {
        Trip.generateTestTripList(123456789);

        Espresso.onView(ViewMatchers.withId(R.id.cardView1))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(20)));

        testTripDetailsCommonMethod();
    }

    @Test
    public void testTripDetailsColumn() {
        Trip.generateTestTripList(123456789);

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
    }

    private void testTripDetailsCommonMethod() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM yyyy", Locale.getDefault());

        // See details of one trip
        Espresso.onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.click()));

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