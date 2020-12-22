package org.covid.inventory;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.junit.jupiter.api.Test;
import org.covid.inventory.controller.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class InventoryApplicationTests {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
    private DataSource dataSource;
	
	@Test
	public void test1() throws SQLException {
		assertThat(dataSource.getClass().getName())
        .isEqualTo("org.apache.tomcat.jdbc.pool.DataSource");
		System.out.println("123");
		logger.debug("Max Idle: " + dataSource.getMaxIdle());
		logger.debug("Max Active: " + dataSource.getMaxIdle());
		logger.debug("Active: " + dataSource.getNumActive());
		logger.debug("Idle: " + dataSource.getNumIdle());
	}
}
