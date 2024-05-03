package us.master.entregable2;

import android.util.Log;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import us.master.entregable2.entities.TripFunctionalities;
import us.master.entregable2.entities.User;
import us.master.entregable2.services.FirebaseDatabaseService;
import us.master.entregable2.services.UserCallback;

public class TestFunctionalities {
    public static void BeforeTesting(){
        CountDownLatch latch = new CountDownLatch(1);
        FirebaseDatabaseService.testing = true;

        TripFunctionalities.generateTestTripList(123456789, latch);
        TripFunctionalities.readTripData();
        try {
            TestFunctionalities.performLogin();
        }
        catch (InterruptedException e) {
            Log.e("MainActivityTest", "Error performing login");
        }

        FirebaseDatabaseService.getCurrentUser(new UserCallback() {
            @Override
            public void onCallback(User user) {
                if (user != null) {
                    user.setSelectedTrips(new ArrayList<>());
                    user.setBoughtTrips(new ArrayList<>());
                    FirebaseDatabaseService.getServiceInstance().saveUser(user);
                }
            }
        });
    }

    public static void performLogin() throws InterruptedException {
        // Input email
        Espresso.onView(ViewMatchers.withId(R.id.login_email_et))
                .perform(ViewActions.typeText("tmentrega2@gmail.com"), ViewActions.closeSoftKeyboard());

        // Input password
        Espresso.onView(ViewMatchers.withId(R.id.login_pass_et))
                .perform(ViewActions.typeText("holamundo"), ViewActions.closeSoftKeyboard());

        // Click on the login button
        Espresso.onView(ViewMatchers.withId(R.id.login_button_mail))
                .perform(ViewActions.click());

        Thread.sleep(10000); // waits for 10 seconds
    }
}
