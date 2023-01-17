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
		assertEquals(4, tableList.size());
	}

	@Test
	public void testGetTablesByEvent() throws SQLException {
		TableDao tableDao = new TableDao();
		ArrayList <TableBean> tableList1 = new ArrayList <>();
		String orderBy = "id_table";
		String orderDirection = "asc";
		//caso id event presente
		tableList1 = tableDao.getTablesByEvent(orderBy, orderDirection,6);
		assertEquals(1, tableList1.size());
		// caso id = 0
		ArrayList <TableBean> tableList2 = new ArrayList <>();
		tableList2 = tableDao.getTablesByEvent(orderBy, orderDirection,0);
		assertNull(tableList2);
		//caso id non presente
		ArrayList <TableBean> tableList3 = new ArrayList <>();
		tableList3 = tableDao.getTablesByEvent(orderBy, orderDirection,200);
		assertEquals(0,tableList3.size());
	} 
	
	@Test
	public void testGetTableById() throws SQLException {
		TableDao tableDao = new TableDao();
		// caso id = 0
		TableBean table1 = tableDao.getTableById(0);
		assertNull(table1);
		
		// caso id non presente
		TableBean table2 = tableDao.getTableById(150);
		assertNull(table2);
		
		// caso id presente
		TableBean table3 = tableDao.getTableById(3);
		assertEquals(3, table3.getIdTable());
		assertEquals(6, table3.getTableCapacity());
		assertEquals(6, table3.getIdEvent());
		}

}
