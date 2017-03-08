/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advice.dao.test;

import com.advice.dao.RegistrationDAO;
import com.advice.dos.RegistrationDO;

import com.mongodb.Mongo;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 *
 * @author cjptech
 */
//@Component
public class LoginPageDAOTest {

    private static final String LOCALHOST = "127.0.0.1";
    private static final String DB_NAME = "advice";
    private static final int MONGO_TEST_PORT = 27017;

    private static Mongo mongo;

    private MongoTemplate template;

    @Autowired
    private RegistrationDAO registrationDAO;

    @BeforeClass
    public static void initializeDB() throws IOException {
        mongo = new Mongo(LOCALHOST, MONGO_TEST_PORT);
        mongo.getDB(DB_NAME);
    }

//     @AfterClass
//    public static void shutdownDB() throws InterruptedException {
//        mongo.close();
//        mongoProcess.stop();
//    }
   
    
    @Before
    public void setUp() throws Exception {
        registrationDAO = new RegistrationDAO();
        template = new MongoTemplate(mongo, DB_NAME);
        registrationDAO.setMongoTemplate(template);
    }
    

    @Test
    public void TestInsertUser() throws Exception {

        RegistrationDO registrationDO = new RegistrationDO();
        // registrationDO.setUserId("gshjsghf HI Bye fsfdg");
        // registrationDO.setPass1("bye fsfsd");

        //  registrationDAO.insertUser(registrationDO);
        registrationDO = registrationDAO.getUser("sivaji", "sivaji123");

        System.out.println("regis uname from test class : " + registrationDO.getUserId());
        System.out.println("regis password from test class : " + registrationDO.getPass2());

        assertEquals("sivaji", registrationDO.getUserId());
        assertEquals("sivaji123", registrationDO.getPass2());
               
          
//           // assertTrue("sivaji".equals(registrationDO.getUserId()));
//           // assertNotNull(registrationDO);
//           // assertNotNull(registrationDO);
//
//           // assertEquals("sivajisivaji12", registrationDO);
    }
}

