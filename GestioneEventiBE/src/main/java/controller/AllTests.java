package controller;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AuthenticationControllerTest.class, BookingControllerTest.class, EventControllerTest.class,
		FeedbackControllerTest.class, LocationControllerTest.class, LocationTypeControllerTest.class,
		LogoutControllerTest.class, RegistrationControllerTest.class, RoleControllerTest.class,
		TableControllerTest.class, UserControllerTest.class, UserProfileControllerTest.class })
public class AllTests {

}
