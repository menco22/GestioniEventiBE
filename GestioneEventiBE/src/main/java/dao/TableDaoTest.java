package dao;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;

import beans.TableBean;

public class TableDaoTest {

	@Test
	public void testGetTables() throws SQLException {
		TableDao tableDao = new TableDao();
		ArrayList <TableBean> tableList = new ArrayList <>();
		String orderBy = "id_table";
		String orderDirection = "asc";
		tableList = tableDao.getTables(orderBy, orderDirection);
		assertEquals(2, tableList.size());
	}

	@Test
	public void testGetTableById() throws SQLException {
		TableDao tableDao = new TableDao();
		// caso id = 0
		TableBean table1 = tableDao.getTableById(0);
		assertNull(table1);
		
		// caso id non presente
		TableBean table2 = tableDao.getTableById(10);
		assertNull(table2);
		
		// caso id presente
		TableBean table3 = tableDao.getTableById(2);
		assertEquals(2, table3.getIdTable());
		assertEquals(3, table3.getTableCapacity());
		assertEquals(4, table3.getIdEvent());
		}

}
