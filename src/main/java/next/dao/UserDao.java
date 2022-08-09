package next.dao;

import core.jdbc.JdbcTemplate;
import next.model.User;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class UserDao {

    public void insert(User user) {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            public void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, user.getUserId());
                pstmt.setString(2, user.getPassword());
                pstmt.setString(3, user.getName());
                pstmt.setString(4, user.getEmail());
            }
        };
        jdbcTemplate.execute(sql);
    }

    public void update(User user) {
        String sql = "UPDATE USERS SET userId = ?, password = ?, name = ?, email = ? where userId = ?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            public void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, user.getUserId());
                pstmt.setString(2, user.getPassword());
                pstmt.setString(3, user.getName());
                pstmt.setString(4, user.getEmail());
                pstmt.setString(5, user.getUserId());
            }
        };
        jdbcTemplate.execute(sql);
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM USERS";
        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            protected void setValues(PreparedStatement pstmt) {
                //
            }
        };

        return jdbcTemplate.query(sql, rs ->
            new User(
                rs.getString(1),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4)
            )
        );
    }

    public User findByUserId(String userId) {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userId = ?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            protected void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, userId);
            }
        };

        return jdbcTemplate.queryForObject(sql, rs ->
            new User(
                rs.getString(1),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4)
            )
        );
    }
}
