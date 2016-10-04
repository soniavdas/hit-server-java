package com.hiveTown.test.dao;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.ClassRule;
import org.junit.rules.ExternalResource;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations="classpath:applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Suite.SuiteClasses( {
        TestUserDao.class,
        TestCommunityDao.class
        /* , Add more test classes here separated by commas*/
} )
public class DaoSuite{

    // This is a static field.  Per the ClassRule documentation,
    // to use a ClassRule we need a field that is "public, static,
    // and a subtype of of TestRule."
    // See http://junit.czweb.org/apidocs/org/junit/ClassRule.html
    @ClassRule
    public static ExternalResource testRule = new ExternalResource(){
        @Override
        protected void before() throws Throwable{
            Logger.getLogger("com.hiveTown.test").log(Level.ALL, "Inside RuleSuite::ExternalResource::before.");
 
        };

        @Override
        protected void after(){
            // Nothing to do here in this case.
            Logger.getLogger("com.hiveTown.test").log(Level.ALL, "Inside RuleSuite::ExternalResource::after.");
        };
    };
}