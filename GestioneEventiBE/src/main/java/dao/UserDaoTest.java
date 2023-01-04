package dao;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;

import beans.ProfileBean;
import beans.UserBean;

public class UserDaoTest {

	@Test
	public void testGetUserByUsername() throws SQLException {
		UserDao userDao = new UserDao();
		// caso username nullo
		UserBean user1 = userDao.getUserByUsername(null);
		assertNull(user1);
		
		// caso username non presente
		UserBean user2 = userDao.getUserByUsername("pippo");
		assertNull(user2);
		
		// caso username presente
		UserBean user3 = userDao.getUserByUsername("pro22");
		assertEquals(19, user3.getIdUser());
		assertEquals("Piera", user3.getName());
		assertEquals("Treghini", user3.getSurname());
		assertEquals("hbuhg@gmail.com", user3.getEmail());
		assertEquals("pro22", user3.getUsername());
		assertEquals("pro22", user3.getPassword());
		assertEquals(2, user3.getIdRole());
	}

	@Test
	public void testGetUserById() throws SQLException {
		UserDao userDao = new UserDao();
		// caso id nullo
		UserBean user1 = userDao.getUserById(0);
		assertNull(user1);
		
		// caso id non presente
		UserBean user2 = userDao.getUserById(30);
		assertNull(user2);
		
		// caso id presente
		UserBean user3 = userDao.getUserById(19);
		assertEquals(19, user3.getIdUser());
		assertEquals("Piera", user3.getName());
		assertEquals("Treghini", user3.getSurname());
		assertEquals("hbuhg@gmail.com", user3.getEmail());
		assertEquals("pro22", user3.getUsername());
		assertEquals("pro22", user3.getPassword());
		assertEquals(2, user3.getIdRole());
	}

	@Test
	public void testGetUserList() throws SQLException {
		UserDao userDao = new UserDao();
		ArrayList <UserBean> usersList = new ArrayList <>();
		String orderBy = "id_user";
		String orderDirection = "asc";
		usersList = userDao.getUserList(orderBy, orderDirection);
		assertEquals(8, usersList.size());
	}

	@Test
	public void testGetUsernameAndRole() throws SQLException {
		UserDao userDao = new UserDao();
		ProfileBean user = userDao.getUsernameAndRole(19);
		assertEquals("pro22", user.getUsername());
		assertEquals(2, user.getIdRole());
		
	}

	@Test
	public void testGetUser() throws SQLException {
		UserDao userDao = new UserDao();
		UserBean user1 = userDao.getUser(null, null);
		assertNull(user1);
		
		UserBean user2 = userDao.getUser("pino", "pino13");
		assertNull(user2);
		
		UserBean user3 = userDao.getUser("pro22", "pro22");
		assertEquals(19, user3.getIdUser());
		assertEquals("Piera", user3.getName());
		assertEquals("Treghini", user3.getSurname());
		assertEquals("hbuhg@gmail.com", user3.getEmail());
		assertEquals("pro22", user3.getUsername());
		assertEquals("pro22", user3.getPassword());
		assertEquals(2, user3.getIdRole());
	}

}
