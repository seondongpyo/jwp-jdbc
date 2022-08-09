package core.jdbc;

import next.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class JdbcTemplate {

    public void execute(String sql) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            setValues(pstmt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User findByUserId(String sql) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            setValues(pstmt);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String userId = rs.getString(1);
                String password = rs.getString(2);
                String name = rs.getString(3);
                String email = rs.getString(4);
                return new User(userId, password, name, email);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void setValues(PreparedStatement pstmt) throws SQLException;
}
