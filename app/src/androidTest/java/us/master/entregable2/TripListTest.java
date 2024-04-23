package us.master.entregable2;

import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.is;

import android.widget.DatePicker;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;

import us.master.entregable2.entities.Trip;

@RunWith(AndroidJUnit4.class)
public class TripListTest {

    @Rule
    public ActivityTestRule<TripList> mActivityRule = new ActivityTestRule<>(TripList.class);

    @Test
    public void ensureColumnToggleWorks() throws Exception {
        Trip.generateTripList(123456789);

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
    }

    @Test
    public void testFilterActivityStartDate() throws Exception {
        // Generate the trip list
        Trip.generateTripList(123456789);
        LocalDate nextMonth = LocalDate.now().plusMonths(1);

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


        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(20)));
    }

    @Test
    public void testFilterActivityEndDate() throws Exception {
        // Generate the trip list
        Trip.generateTripList(123456789);
        LocalDate nextMonth = LocalDate.now().plusMonths(1);

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

        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(20)));
    }

    @Test
    public void testFilterActivityBothDates() throws Exception {
        // Generate the trip list
        Trip.generateTripList(123456789);
        LocalDate nextMonth = LocalDate.now().plusMonths(1);
        LocalDate nextNextMonth = LocalDate.now().plusMonths(2);

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


        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(20)));
    }
    @Test
    public void testFilterMinPrice() {
        Trip.generateTripList(123456789);

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
    }
    @Test
    public void testFilterMaxPrice() {
        Trip.generateTripList(123456789);

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

        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(20)));
    }
    @Test
    public void testFilterBothPrices() {
        Trip.generateTripList(123456789);

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

        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(20)));
    }
    @Test
    public void testAllFilters() {
        Trip.generateTripList(123456789);
        LocalDate nextMonth = LocalDate.now().plusMonths(1);
        LocalDate nextNextMonth = LocalDate.now().plusMonths(2);

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

        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.recyclerView))
                .check(ViewAssertions.matches(EspressoTestMatchers.withItemCount(20)));
    }
}