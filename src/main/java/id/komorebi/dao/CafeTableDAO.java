package id.komorebi.dao;

import id.komorebi.model.CafeTable;
import java.sql.SQLException;
import java.util.List;


public interface CafeTableDAO {
    CafeTable findById(int id) throws SQLException;
    List<CafeTable> findAll() throws SQLException;
    int insert(CafeTable table) throws SQLException;
    boolean update(CafeTable table) throws SQLException;
    boolean delete(int id) throws SQLException;
}
