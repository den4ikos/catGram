package ru.yandex.practicum.catsgram.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.catsgram.dao.UserDao;
import ru.yandex.practicum.catsgram.model.User;

import java.util.Optional;
@Component
public class UserDaoImpl implements UserDao {
    private final JdbcTemplate jdbcTemplate;
    private final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);

    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public Optional<User> findUserByEmail(String login) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from cat_user where email = ?", login);
        if (userRows.next()) {
            log.info("User: {} {}", userRows.getString("id"), userRows.getString("nickname"));
            User user = new User();
            user.setEmail(login);
            return Optional.of(user);
        } else {
            log.info("User with login {} not found.", login);
            return Optional.empty();
        }
    }
}
