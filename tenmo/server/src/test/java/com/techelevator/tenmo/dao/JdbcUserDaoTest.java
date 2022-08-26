package com.techelevator.tenmo.dao;

import com.techelevator.dao.BaseDaoTests;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.Assert.*;

public class JdbcUserDaoTest extends BaseDaoTests {



    private static final User USER_1 = new User(1001, "Jill", "password", "ROLE_USER");
    private static final User USER_2 = new User(1002, "Bob", "bob", "ROLE_USER");
    private static final User USER_3 = new User(1003, "Brad", "password", "ROLE_USER");



    private JdbcUserDao sut;
    private User testUser1;

    @Before
    public void setup(){
        sut = new JdbcUserDao(dataSource);
        testUser1 = new User(1004, "Minie", "pass1word", "ROLE_USER");
    }

    @Test(expected = UsernameNotFoundException.class)
    public void findIdByUsername_returns_null_when_username_not_found() {
        User actual = sut.findByUsername("Jane");
    }

    @Test
    public void findAll() {
    }

    @Test
    public void findByUsername() {
    }

    @Test
    public void create() {
    }
}