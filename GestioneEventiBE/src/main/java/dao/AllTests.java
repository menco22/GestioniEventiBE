package dao;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ BookingDaoTest.class, EventDaoTest.class, FeedbackDaoTest.class, LocationDaoTest.class,
		LocationTypeDaoTest.class, RoleDaoTest.class, TableDaoTest.class, UserDaoTest.class })
public class AllTests {

}
