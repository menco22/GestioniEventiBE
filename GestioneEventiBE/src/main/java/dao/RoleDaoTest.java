package dao;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;

import beans.RoleBean;

public class RoleDaoTest {

	@Test
	public void testGetRole() throws SQLException {
		RoleDao roleDao = new RoleDao();
		ArrayList <RoleBean> roleList = new ArrayList <>();
		String orderBy = "id_role";
		String orderDirection = "asc";
		roleList = roleDao.getRole(orderBy, orderDirection);
		assertEquals(2, roleList.size());
	}

	@Test
	public void testGetRoleById() throws SQLException {
		RoleDao roleDao = new RoleDao();
		//caso id = 0
		RoleBean role1 = roleDao.getRoleById(0);
		assertNull(role1);
		
		// caso id non presente
		RoleBean role2 = roleDao.getRoleById(10);
		assertNull(role2);
		
		//caso id presente
		RoleBean role3  = roleDao.getRoleById(1);
		assertEquals(1, role3.getIdRole());
		assertEquals("admin", role3.getCode());
	}

}
