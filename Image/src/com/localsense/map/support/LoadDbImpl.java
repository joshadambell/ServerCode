package com.localsense.map.support;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.SneakyThrows;

import org.skife.jdbi.v2.DBI;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.localsense.map.api.DAO;
import com.localsense.map.api.LoadDb;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class LoadDbImpl implements LoadDb
{
  
  private static final String DB_DRIVER = "";
  private static final String DB_CONNECTION = "";
  private static final String DB_USER = "user";
  private static final String DB_PASSWORD = "password";

  private Table<Integer, Integer, List<Integer>> objects = HashBasedTable.create(); 
  
  @Override
  @SneakyThrows
  public void insertIntoDb(Object object)
  {
    
    Map<Integer,Tuple<Integer, Integer>> locations = (HashMap<Integer,Tuple<Integer, Integer>>)object;
    List<PreparedStatement> queryList = new ArrayList<PreparedStatement>();
    Connection dbConnection = null;
    Statement statement = null;
 
    try {
      DAO db = getDbConnection();
      
      PreparedStatement pStatement = dbConnection.prepareStatement("insert into table"
          + "shape_id, x, y values (?,?)");
      for (Map.Entry<Integer, Tuple<Integer, Integer>> pairs : locations.entrySet()) 
      {
        db.insertImageDimesions(0, 0, 0);
          pStatement.setInt(1, (Integer)pairs.getKey());
          pStatement.setInt(2, (Integer)((Tuple)pairs.getValue()).getX());
          pStatement.setInt(3, (Integer)((Tuple)pairs.getValue()).getY());
          queryList.add(pStatement);         
      }
      
      for(PreparedStatement p : queryList)
      {
        p.executeQuery();
        System.out.println("Record is inserted into table!");
      }
      
     
 
    } catch (SQLException e) {
 
      System.out.println(e.getMessage());
 
    } finally {
 
      if (statement != null) {
        statement.close();
      }
 
      if (dbConnection != null) {
        dbConnection.close();
      }
    }  
  }
  

  private static DAO getDbConnection() {

      MysqlDataSource ds = new MysqlDataSource();
      ds.setServerName("localhost");
      ds.setUser("admin");
      DBI dbi = new DBI(ds);
      return dbi.onDemand(DAO.class);
      
  }

}
