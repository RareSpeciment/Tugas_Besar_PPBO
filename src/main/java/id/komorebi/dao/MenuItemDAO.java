package id.komorebi.dao;

import id.komorebi.model.MenuItem;
import id.komorebi.model.ReportEntry;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface MenuItemDAO {
    MenuItem findById(int id) throws SQLException;
    List<MenuItem> findAll() throws SQLException;
    List<MenuItem> findAvailable() throws SQLException;
    int insert(MenuItem menu) throws SQLException;
    boolean update(MenuItem menu) throws SQLException;
    boolean delete(int id) throws SQLException;

    // Reporting: top selling items for a date
    List<ReportEntry> getTopSellingItems(LocalDate date) throws SQLException;
}