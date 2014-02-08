package com.localsense.map.api;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface DAO
{
  @SqlUpdate("insert into something (id, name) values (:name)")
  void insertImageDimesions(@Bind("name") int x, int y, int id);
  
  
  void close();

}
