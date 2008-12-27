package com.arcmind.codegen
import java.sql.*;

class JdbcUtils {
    String url
    String driver
    String userName
    String password
    Connection connection
    boolean debug

    def executeScript(String sql) {
    	execute {Connection con ->
    		Statement statement = con.createStatement();
    		try {
    			statement.execute(sql);
    		} finally {
    			statement?.close();
    		}
    	}
    }
    
    def execute(callme) {
        Class.forName(driver)
        try {
            connection = DriverManager.getConnection (url,userName,password)
            callme(connection)
        } finally {
            connection?.close()
            connection=null
        }
    }

    def iterate(ResultSet resultSet, callme) {
        try {
            while (resultSet.next()) {
                callme(resultSet)
            }
        } finally {
            resultSet?.close()
        }

    }
}
