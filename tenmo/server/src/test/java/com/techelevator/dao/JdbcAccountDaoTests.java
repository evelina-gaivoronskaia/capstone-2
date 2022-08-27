package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;

public class JdbcAccountDaoTests extends BaseDaoTests{


    JdbcAccountDao sut;
    JdbcUserDao helper;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);
        helper = new JdbcUserDao(jdbcTemplate);
    }

    @Test
    public void requestBalance_returns_1000_given_userId(){
        helper.create("Test_Username", "test_password");
        int testUserId = helper.findIdByUsername("Test_Username");
        BigDecimal expected = new BigDecimal("1000.00");
        BigDecimal actual = sut.requestBalance(testUserId);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void requestBalance_returns_null_given_unknownId(){
        BigDecimal actual = sut.requestBalance(1000);
        Assert.assertNull(actual);
    }


    @Test
    public void getAllAccounts_return_list_ofPopulatedAccounts(){
        List<Account> actual = sut.getAllAccounts();
        Assert.assertEquals(2, actual.size());
    }
}
