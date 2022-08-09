package next.dao;

import core.jdbc.JdbcTemplate;
import next.model.User;

import java.util.List;

public class UserDao {

    public void insert(User user) {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.execute(sql, ps -> {
            ps.setString(1, user.getUserId());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getName());
            ps.setString(4, user.getEmail());
        });
    }

    public void update(User user) {
        String sql = "UPDATE USERS SET userId = ?, password = ?, name = ?, email = ? where userId = ?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.execute(sql, ps -> {
            ps.setString(1, user.getUserId());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getName());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getUserId());
        });
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM USERS";
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
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
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        return jdbcTemplate.queryForObject(sql,
            ps -> ps.setString(1, userId),
            rs -> new User(
                rs.getString(1),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4)
            )
        );
    }
}
