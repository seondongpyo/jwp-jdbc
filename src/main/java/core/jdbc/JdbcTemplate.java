package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {

    public void execute(String sql, PreparedStatementSetter pss) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            pss.setValues(ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException();
        }
    }

    public <T> List<T> query(String sql, PreparedStatementSetter pss, RowMapper<T> rowMapper) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            pss.setValues(ps);
            ResultSet rs = ps.executeQuery();
            List<T> results = new ArrayList<>();
            while (rs.next()) {
                results.add(rowMapper.mapRow(rs));
            }
            return results;
        } catch (SQLException e) {
            throw new DataAccessException();
        }
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper) {
        return query(sql, ps -> {}, rowMapper);
    }

    public <T> T queryForObject(String sql, PreparedStatementSetter pss, RowMapper<T> rowMapper) {
        List<T> results = query(sql, pss, rowMapper);
        if (results.size() != 1) {
            throw new DataAccessException();
        }
        return results.get(0);
    }
}
