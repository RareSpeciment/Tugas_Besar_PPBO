package id.komorebi.dao;

import id.komorebi.model.ActivityLog;
import java.sql.SQLException;
import java.util.List;

public interface ActivityLogDAO {
    int insert(ActivityLog log) throws SQLException;
    List<ActivityLog> findAll() throws SQLException;
    List<ActivityLog> findByUserId(int userId) throws SQLException;
}