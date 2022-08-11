package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {

    public void execute(String sql, Object... values) {
        execute(sql, new ArgumentPreparedStatementSetter(values));
    }

    public void execute(String sql, PreparedStatementSetter pss) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            pss.setValues(ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... values) {
        return queryForObject(sql, rowMapper, new ArgumentPreparedStatementSetter(values));
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, PreparedStatementSetter pss) {
        List<T> results = query(sql, rowMapper, pss);
        int size = results.size();
        if (size != 1) {
            throw new IncorrectResultSizeDataAccessException(size);
        }
        return results.iterator().next();
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper) {
        return query(sql, rowMapper, ps -> {});
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, PreparedStatementSetter pss) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            pss.setValues(ps);
            return mappingRowToObject(ps, rowMapper);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    private <T> List<T> mappingRowToObject(PreparedStatement ps, RowMapper<T> rowMapper) throws SQLException {
        try (ResultSet rs = ps.executeQuery()) {
            List<T> results = new ArrayList<>();
            while (rs.next()) {
                results.add(rowMapper.mapRow(rs));
            }
            return results;
        }
    }
}
