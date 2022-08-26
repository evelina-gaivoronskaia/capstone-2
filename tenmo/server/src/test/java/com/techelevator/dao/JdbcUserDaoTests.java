package com.techelevator.dao;


import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class JdbcUserDaoTests extends BaseDaoTests{

    private JdbcUserDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcUserDao(dataSource);
    }

    @Test
    public void createNewUser() {
        boolean userCreated = sut.create("TEST_USER","test_password");
        Assert.assertTrue(userCreated);
        User user = sut.findByUsername("TEST_USER");
        Assert.assertEquals("TEST_USER", user.getUsername());
    }
    @Test
    public void createNewUser_returns_false() {
        boolean userCreated = sut.create(null,"test_password");
        Assert.assertFalse(userCreated);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void findIdByUsername_returns_null_when_username_not_found() {
        User actual = sut.findByUsername("Jane");
    }

    @Test(expected = UsernameNotFoundException.class)
    public void findIdByUsername_returns_null_when_username_empty() {
        User actual = sut.findByUsername("");
    }

    @Test
    public void findIdByUsername_returns_correct_id(){
        int actual = sut.findIdByUsername("bob");
        Assert.assertEquals(1001, actual);
    }

    @Test
    public void findAll_returns_two_users(){
        List<User> actual = sut.findAll();
        Assert.assertEquals(2, actual.size());
    }

    @Test
    public void findByUsername_returns_user(){
        User user = sut.findByUsername("bob");
        int actual = user.getId();
        Assert.assertEquals(1001, actual);
    }

    @Test
    public void findByUsername_returns_user_two(){
        User user = sut.findByUsername("user");
        int actual = user.getId();
        Assert.assertEquals(1002, actual);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void findByUsername_returns_null_when_username_empty() {
        User actual = sut.findByUsername("");
    }

    @Test(expected = UsernameNotFoundException.class)
    public void findByUsername_returns_null_when_username_does_not_exist() {
        User actual = sut.findByUsername("Jill");
    }



}
