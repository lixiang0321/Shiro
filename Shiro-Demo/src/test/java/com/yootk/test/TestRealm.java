package com.yootk.test;

import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.junit.Test;

public class TestRealm {
    @Test
    public void testName() throws Exception {
        Realm realmA = new IniRealm("classpath:shiro.ini") ;
        Realm realmB = new JdbcRealm() ;
        System.out.println(realmA.getName());
        System.out.println(realmB.getName());
    }
}

