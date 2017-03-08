/**
 *The RegistrationDAO used to create collection of survey .
 * here we also do crud registration.
 *
 * @author CJP@venkat
 * @version 1.0
 * @since
 */
package com.advice.dao;

import com.advice.dos.PasswordRecoveryDO;
import com.advice.dos.RegistrationDO;
import com.advice.dos.SurveyDO;
import com.advice.encryptionAlgorithams.EncryptionDecryption;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;
import java.beans.IntrospectionException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

/**
 * @Repository annotation, importing the DAOs into the DI container and also
 * this annotation makes the unchecked exceptions (thrown from DAO methods)
 * eligible for translation into Spring DataAccessException. indicates that an
 * annotated class is a "Repository" (or "DAO").
 *
 * A class thus annotated is eligible for Spring DataAccessException
 * translation. The annotated class is also clarified as to its role in the
 * overall application architecture for the purpose of tools, aspects, etc.
 *
 * this annotation also serves as a specialization of @Component, allowing for
 * implementation classes to be auto detected through class path scanning
 * @Autowired Marks a constructor, field, setter method or configuration method
 * as to be auto wired by Spring 's dependency injection facilities.
 * @author cjp
 */
@Repository
public class RegistrationDAO {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private EncryptionDecryption encryptionDecryption;

    private static final String COLLECTION_NAME = "registration";

    private static final Logger registrationDAOLogger = Logger.getLogger(RegistrationDAO.class);

    /**
     *
     * addData method is for creating collections , inserting and updating
     * collections. check collection is Exist or not. If collection not exists
     * create new Collection of RegistrationDO Generate dynamic Id with create
     * collection object Insert data registrationDO with "registration
     * collection" *
     *
     * @param registrationDO
     * @throws Exception
     */
    public void insertUser(RegistrationDO registrationDO) throws Exception {
        registrationDAOLogger.info("Start running insertUser method in RegistrationDAO");
        try {

            if (!mongoTemplate.collectionExists(RegistrationDO.class)) {
                mongoTemplate.createCollection(RegistrationDO.class);
            }
            // registrationDO.setId(UUID.randomUUID().toString());
            // EncryptDecrypt.encrypt(registrationDO, registrationDO);
            //Object abcd =
            encryptionDecryption.encryptObject(registrationDO);
            // registrationDO=sealed;
            mongoTemplate.insert(registrationDO, COLLECTION_NAME);

        } catch (DataAccessException dae) {
            registrationDAOLogger.error("DataAccessException in insertUser method of RegistrationDAO", dae);
            throw dae;
        } catch (MongoException me) {
            registrationDAOLogger.error("MongoException in insertUser method of RegistrationDAO", me);
            throw me;
        } catch (InvalidKeyException | IllegalBlockSizeException | NoSuchAlgorithmException | NoSuchPaddingException | IOException e) {
            registrationDAOLogger.error("Exception in insertUser method of RegistrationDAO", e);
            throw e;
        }
        registrationDAOLogger.info("End running insertUser method in RegistrationDAO");
    }

    /**
     * getuserId method is finding userId is existing or not
     *
     *
     * @param userId
     * @return
     * @throws Exception
     */
    public Boolean getUserId(String userId) throws Exception {
      
        registrationDAOLogger.info("Start running getUserId method in RegistrationDAO");
        RegistrationDO registrationDO;
        boolean returnValue = false;
        try {
            userId = encryptionDecryption.encrypt(userId);
            Query query = new Query();
            query.addCriteria(Criteria.where("userId").is(userId));
            registrationDO = mongoTemplate.findOne(query, RegistrationDO.class, COLLECTION_NAME);

            //encryptionDecryption.decryptObject(registrationDO);
            if (registrationDO != null) {
                encryptionDecryption.decryptObject(registrationDO);
                returnValue = true;
            }
        } catch (DataAccessException dae) {
            registrationDAOLogger.error("DataAccessException in getUserId method of RegistrationDAO", dae);
            throw dae;
        } catch (MongoException me) {
            registrationDAOLogger.error("MongoException in getUserId method of RegistrationDAO", me);
            throw me;
        } catch (Exception e) {
            registrationDAOLogger.error("Exception in getUserId method of RegistrationDAO", e);
            throw e;
        }
        registrationDAOLogger.info("End of getUserId method in RegistrationDAO");
        return returnValue;
    }
    /**
     * getRegistraionData is finding registering data based on userId
     *
     *
     * @param userId
     * @return
     * @throws Exception
     */
    public RegistrationDO getRegistraionDetailsByUserId(String userId) throws Exception {
        registrationDAOLogger.info("Start running getRegistraionData method in RegistrationDAO");
        RegistrationDO registrationDO = null;
        try {
            String usersId = encryptionDecryption.encrypt(userId);
            Query query = new Query();
            query.addCriteria(Criteria.where("userId").is(usersId));
            registrationDO = mongoTemplate.findOne(query, RegistrationDO.class, COLLECTION_NAME);
            encryptionDecryption.decryptObject(registrationDO);
        } catch (DataAccessException dae) {
            registrationDAOLogger.error("DataAccessException in getRegistraionData method of RegistrationDAO", dae);
            throw dae;
        } catch (MongoException me) {
            registrationDAOLogger.error("MongoException in getRegistraionData method of RegistrationDAO", me);
            throw me;
        } catch (Exception e) {
            registrationDAOLogger.error("Exception in getRegistraionData method of RegistrationDAO", e);
            throw e;
        }
        registrationDAOLogger.info("End of getRegistraionData method in RegistrationDAO");
        return registrationDO;
    }

    public RegistrationDO getRegistraionDetailsEmailId(String emailId) throws Exception {
        registrationDAOLogger.info("Start running getRegistraionData method in RegistrationDAO");
        RegistrationDO registrationDO = null;
        try {
            String emailsId = encryptionDecryption.encrypt(emailId);
            Query query = new Query();
            query.addCriteria(Criteria.where("emailId").is(emailsId));
            registrationDO = mongoTemplate.findOne(query, RegistrationDO.class, COLLECTION_NAME);
            encryptionDecryption.decryptObject(registrationDO);
        } catch (DataAccessException dae) {
            registrationDAOLogger.error("DataAccessException in getRegistraionData method of RegistrationDAO", dae);
            throw dae;
        } catch (MongoException me) {
            registrationDAOLogger.error("MongoException in getRegistraionData method of RegistrationDAO", me);
            throw me;
        } catch (Exception e) {
            registrationDAOLogger.error("Exception in getRegistraionData method of RegistrationDAO", e);
            throw e;
        }
        registrationDAOLogger.info("End of getRegistraionData method in RegistrationDAO");
        return registrationDO;
    }

    /**
     * getRegistraionData is finding registering data based on userId
     *
     *
     * @param registrationDO
     * @return
     * @throws Exception
     */
    public RegistrationDO getRegistrationDetails(RegistrationDO registrationDO) throws Exception {
        registrationDAOLogger.info("Start running  RegistrationDAO getRegistraionData method");
        RegistrationDO registrationDOObject = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("userId").is(registrationDO.getUserId()));
            query.addCriteria(Criteria.where("emailId").is(registrationDO.getEmailId()));
            query.addCriteria(Criteria.where("phone").is(registrationDO.getPhone()));
            registrationDOObject = mongoTemplate.findOne(query, RegistrationDO.class, COLLECTION_NAME);

        } catch (DataAccessException dae) {
            registrationDAOLogger.error("DataAccessException in getRegistrationDetails method of RegistrationDAO", dae);
            throw dae;
        } catch (MongoException me) {
            registrationDAOLogger.error("MongoException in getRegistrationDetails method of RegistrationDAO", me);
            throw me;
        } catch (Exception e) {
            registrationDAOLogger.error("Exception in getRegistrationDetails method of RegistrationDAO", e);
            throw e;
        }
        registrationDAOLogger.info("End in RegistrationDAO class of getRegistrationDetails method");
        return registrationDOObject;
    }

    /**
     * updateActive method is for updating activation based userid *
     *
     * @param userId
     * @return
     * @throws Exception
     */
    public Boolean updateActive(String userId) throws Exception {
        registrationDAOLogger.info("Going to run  RegistrationDAO updateActive method");

        boolean returnValue = false;
        try {
            userId = encryptionDecryption.encrypt(userId);
            Query query = new Query();
            query.addCriteria(Criteria.where("userId").is(userId));
            int active = 1;
            //  active=encryptionDecryption.encrypt(active);
            Update update = new Update();
            update.set("active", active);
            WriteResult data = mongoTemplate.updateFirst(query, update, RegistrationDO.class, COLLECTION_NAME);
            if (data != null) {
                returnValue = true;
            }
        } catch (DataAccessException dae) {
            registrationDAOLogger.error("This is Error message", dae);
            throw dae;
        } catch (MongoException me) {
            registrationDAOLogger.error("This is Error message", me);
            throw me;
        } catch (Exception e) {
            registrationDAOLogger.error("This is Error message", e);
            throw e;
        }

        registrationDAOLogger.info("Going to run  RegistrationDAO updateActive method");
        return returnValue;
    }

    /**
     *
     * @return @throws Exception
     */
    public RegistrationDO userDefault() throws Exception {
        registrationDAOLogger.info("Going to run  LoginDAO listData method");
        RegistrationDO registrationDO = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("RegistrationpageId").is(1));
            registrationDO = mongoTemplate.findOne(query, RegistrationDO.class, "registrationInput");

        } catch (DataAccessException dae) {
            registrationDAOLogger.error("This is Error message", dae);
            throw dae;
        } catch (MongoException me) {
            registrationDAOLogger.error("This is Error message", me);
            throw me;
        } catch (Exception e) {
            registrationDAOLogger.error("This is Error message", e);
            throw e;
        }
        registrationDAOLogger.info(" LoginDAO End of  listData method");
        return registrationDO;
    }

    /**
     *
     * @param userId
     * @param password
     * @return
     * @throws Exception
     */
    public RegistrationDO getUser(String userId) throws Exception {
        registrationDAOLogger.info("Going to run  LoginDAO listData method");
        RegistrationDO registrationDO = null;
        try {
            String encrypteduserId = encryptionDecryption.encrypt(userId);
            //password = encryptionDecryption.encrypt(password);

            Query queryForFinding = new Query();

            queryForFinding.addCriteria(Criteria.where("userId").is(encrypteduserId));
            //queryForFinding.addCriteria(Criteria.where("pass2").is(password));
           

            registrationDO = mongoTemplate.findOne(queryForFinding, RegistrationDO.class, COLLECTION_NAME);
            if (registrationDO != null) {
                encryptionDecryption.decryptObject(registrationDO);
            }

        } catch (DataAccessException dae) {
            registrationDAOLogger.error("This is Error message", dae);
            throw dae;
        } catch (MongoException me) {
            registrationDAOLogger.error("This is Error message", me);
            throw me;
        } catch (Exception e) {
            registrationDAOLogger.error("This is Error message", e);
            throw e;
        }
        registrationDAOLogger.info(" LoginDAO End of listData method");
        return registrationDO;
    }

    /**
     *
     * @param userId
     * @param password
     * @return
     * @throws Exception
     */
    public RegistrationDO getUser(String userId, String password) throws Exception {
        registrationDAOLogger.info("Going to run  LoginDAO listData method");
        RegistrationDO registrationDO = null;
        try {
            //userId = encryptionDecryption.encrypt(userId);
          //  password = encryptionDecryption.encrypt(password);

            Query queryForFinding = new Query();

            queryForFinding.addCriteria(Criteria.where("userId").is(userId));
            queryForFinding.addCriteria(Criteria.where("pass2").is(password));
           

            registrationDO = mongoTemplate.findOne(queryForFinding, RegistrationDO.class, COLLECTION_NAME);

           // encryptionDecryption.decryptObject(registrationDO);

        } catch (DataAccessException dae) {
            registrationDAOLogger.error("This is Error message", dae);
            throw dae;
        } catch (MongoException me) {
            registrationDAOLogger.error("This is Error message", me);
            throw me;
        } catch (Exception e) {
            registrationDAOLogger.error("This is Error message", e);
            throw e;
        }
        registrationDAOLogger.info(" LoginDAO End of listData method");
        return registrationDO;
    }

    /**
     * admin is find users matches userId and password from registration
     * collection.
     *
     *
     * @param userId
     * @param password
     * @return
     * @throws Exception
     */
    public RegistrationDO admin(String userId, String password) throws Exception {
        registrationDAOLogger.info("Going to run  LoginDAO admin method");
        RegistrationDO registrationDO = null;
        try {
            Query query = new Query();

            query.addCriteria(Criteria.where("userId").is(userId));
            query.addCriteria(Criteria.where("pass2").is(password));

            registrationDO = mongoTemplate.findOne(query, RegistrationDO.class, COLLECTION_NAME);

        } catch (DataAccessException dae) {
            registrationDAOLogger.error("This is Error message", dae);
            throw dae;
        } catch (MongoException me) {
            registrationDAOLogger.error("This is Error message", me);
            throw me;
        } catch (Exception e) {
            registrationDAOLogger.error("This is Error message", e);
            throw e;
        }
        registrationDAOLogger.info(" LoginDAO end of admin method");
        return registrationDO;
    }

    

    /**
     *
     * @param registrationDO
     * @throws Exception
     */
    public void updatePassword(RegistrationDO registrationDO) throws Exception {
        registrationDAOLogger.info("Going to run  LoginDAO updatePassword method");
        try {
            encryptionDecryption.encryptObject(registrationDO);
            Query queryForUpdate = new Query();
            queryForUpdate.addCriteria(Criteria.where("_id").is(registrationDO.getId()));

            mongoTemplate.find(queryForUpdate, RegistrationDO.class, COLLECTION_NAME);
            Update update = new Update();

            update.set("pass1", registrationDO.getPass1());
            update.set("pass2", registrationDO.getPass2());
            mongoTemplate.updateFirst(queryForUpdate, update, COLLECTION_NAME);
        } catch (DataAccessException dae) {
            registrationDAOLogger.error("This is Error message", dae);
            throw dae;
        } catch (MongoException me) {
            registrationDAOLogger.error("This is Error message", me);
            throw me;
        } catch (InvalidKeyException | IllegalBlockSizeException | NoSuchAlgorithmException | NoSuchPaddingException | IOException e) {
            registrationDAOLogger.error("This is Error message", e);
            throw e;
        }
        registrationDAOLogger.info("  LoginDAO End of updatePassword method");
    }

    /**
     *
     * @param registrationDO
     * @throws Exception
     */
    public void updateOrganizationDetails(RegistrationDO registrationDO) throws Exception {
        registrationDAOLogger.info("Going to run  LoginDAO updateOrganizationDetails method");
        try {
            String usersId = encryptionDecryption.encrypt(registrationDO.getUserId());
            Query queryForId = new Query();
            queryForId.addCriteria(Criteria.where("userId").is(usersId));
            RegistrationDO registrationDOforId = mongoTemplate.findOne(queryForId, RegistrationDO.class, COLLECTION_NAME);
            //encryptionDecryption.decryptObject(registrationDO);
            //String id = encryptionDecryption.encrypt(registrationDOforId.getId());
            Query queryForUpdate = new Query();
            queryForUpdate.addCriteria(Criteria.where("_id").is(registrationDOforId.getId()));
            mongoTemplate.find(queryForUpdate, RegistrationDO.class, COLLECTION_NAME);
            Update update = new Update();
            encryptionDecryption.encryptObject(registrationDO);
            update.set("organizationEmailId", registrationDO.getOrganizationEmailId());
            update.set("organizationEmployees", registrationDO.getOrganizationEmployees());
            update.set("organizationFirst", registrationDO.getOrganizationFirst());
            update.set("revenue", registrationDO.getRevenue());
            update.set("location", registrationDO.getLocation());

            mongoTemplate.updateFirst(queryForUpdate, update, COLLECTION_NAME);

        } catch (DataAccessException dae) {
            registrationDAOLogger.error("This is Error message", dae);
            throw dae;
        } catch (MongoException me) {
            registrationDAOLogger.error("This is Error message", me);
            throw me;
        } catch (InvalidKeyException | IllegalBlockSizeException | NoSuchAlgorithmException | NoSuchPaddingException | IOException e) {
            registrationDAOLogger.error("This is Error message", e);
            throw e;
        }
        registrationDAOLogger.info(" LoginDAO End of updateOrganizationDetails method");
    }

    /**
     *
     * @param userId
     * @return
     * @throws java.lang.Exception
     */
    public String findOrganizationDetails(String userId) throws Exception {
        registrationDAOLogger.info("Going to run  RegistrationDAO getUserId method");
        RegistrationDO registrationDO;
        String organizationName = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("userId").is(userId));
            registrationDO = mongoTemplate.findOne(query, RegistrationDO.class, COLLECTION_NAME);
            organizationName = registrationDO.getOrganazationName();
        } catch (DataAccessException dae) {
            registrationDAOLogger.error("This is Error message", dae);
            throw dae;
        } catch (MongoException me) {
            registrationDAOLogger.error("This is Error message", me);
            throw me;
        } catch (Exception e) {
            registrationDAOLogger.error("This is Error message", e);
            throw e;
        }
        registrationDAOLogger.info("End of RegistrationDAO getUserId method");
        return organizationName;
    }

    public void activateRegistraionById(String id) throws Exception {
        registrationDAOLogger.info("Going to run  RegistrationDAO getUserId method");

        try {
            id = encryptionDecryption.encrypt(id);
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(id));

            Update update = new Update();
            String active = encryptionDecryption.encrypt("1");
            update.set("active", active);

            mongoTemplate.updateFirst(query, update, COLLECTION_NAME);
        } catch (DataAccessException dae) {
            registrationDAOLogger.error("This is Error message", dae);
            throw dae;
        } catch (MongoException me) {
            registrationDAOLogger.error("This is Error message", me);
            throw me;
        } catch (Exception e) {
            registrationDAOLogger.error("This is Error message", e);
            throw e;
        }

    }

    public void insertRecoveryPassword(PasswordRecoveryDO passwordRecoveryDO) throws Exception {
        registrationDAOLogger.info("Start running insertRecoveryPassword method in RegistrationDAO");
        try {

            if (!mongoTemplate.collectionExists(RegistrationDO.class)) {
                mongoTemplate.createCollection(RegistrationDO.class);
            }
            // registrationDO.setId(UUID.randomUUID().toString());
            // encryptionDecryption.encryptObject(passwordRecoveryDO);
            //Object abcd =
            encryptionDecryption.encryptObject(passwordRecoveryDO);
            // registrationDO=sealed;
            mongoTemplate.insert(passwordRecoveryDO, "passwordreset");

        } catch (DataAccessException dae) {
            registrationDAOLogger.error("DataAccessException in insertUser method of RegistrationDAO", dae);
            throw dae;
        } catch (MongoException me) {
            registrationDAOLogger.error("MongoException in insertUser method of RegistrationDAO", me);
            throw me;
        }
        registrationDAOLogger.info("End running insertUser method in RegistrationDAO");
    }

    public boolean activatePasswordByUniquekey(String uniqueId) throws Exception {
        registrationDAOLogger.info("Going to run  RegistrationDAO activatePaawordByUniquekey method");

        try {
            uniqueId = encryptionDecryption.encrypt(uniqueId);
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(uniqueId));

            PasswordRecoveryDO passwordRecoveryDO = mongoTemplate.findOne(query, PasswordRecoveryDO.class, "passwordreset");
            encryptionDecryption.decryptObject(passwordRecoveryDO);
            if (passwordRecoveryDO.getReset().equals("0")) {
                Update update = new Update();
                String active = encryptionDecryption.encrypt("1");
                update.set("reset", active);

                mongoTemplate.updateFirst(query, update, "passwordreset");
                return true;
            } else {
                return false;
            }

        } catch (DataAccessException dae) {
            registrationDAOLogger.error("This is Error message", dae);
            throw dae;
        } catch (MongoException me) {
            registrationDAOLogger.error("This is Error message", me);
            throw me;
        } catch (Exception e) {
            registrationDAOLogger.error("This is Error message", e);
            throw e;
        }
    }

    public PasswordRecoveryDO findPasswordReset(String id) {
        registrationDAOLogger.info("Start running findPasswordReset method in RegistrationDAO");
        try {
            id = encryptionDecryption.encrypt(id);
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(id));

            PasswordRecoveryDO passwordRecoveryDO = mongoTemplate.findOne(query, PasswordRecoveryDO.class, "passwordreset");
            encryptionDecryption.decryptObject(passwordRecoveryDO);
            return passwordRecoveryDO;
        } catch (IllegalBlockSizeException ex) {
            java.util.logging.Logger.getLogger(RegistrationDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            java.util.logging.Logger.getLogger(RegistrationDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            java.util.logging.Logger.getLogger(RegistrationDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            java.util.logging.Logger.getLogger(RegistrationDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            java.util.logging.Logger.getLogger(RegistrationDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IntrospectionException ex) {
            java.util.logging.Logger.getLogger(RegistrationDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RegistrationDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidAlgorithmParameterException ex) {
            java.util.logging.Logger.getLogger(RegistrationDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            java.util.logging.Logger.getLogger(RegistrationDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(RegistrationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void updatePassword(String uniqueId, String rePassword, String rePasswordc) throws IOException, InvalidKeyException, IllegalBlockSizeException, NoSuchAlgorithmException, NoSuchPaddingException {
        registrationDAOLogger.info("Going to run  RegistrationDAO updatePassword method");
        try {
            uniqueId = encryptionDecryption.encrypt(uniqueId);
            rePassword = encryptionDecryption.encrypt(rePassword);
            rePasswordc = encryptionDecryption.encrypt(rePasswordc);
            Query queryForId = new Query();
            queryForId.addCriteria(Criteria.where("_id").is(uniqueId));
            Update update = new Update();
            // encryptionDecryption.encryptObject(registrationDO);
            update.set("pass1", rePassword);
            update.set("pass2", rePasswordc);

            mongoTemplate.updateFirst(queryForId, update, COLLECTION_NAME);

        } catch (DataAccessException dae) {
            registrationDAOLogger.error("This is Error message", dae);
            throw dae;
        } catch (MongoException me) {
            registrationDAOLogger.error("This is Error message", me);
            throw me;
        } catch (InvalidKeyException | IllegalBlockSizeException | NoSuchAlgorithmException | NoSuchPaddingException | IOException e) {
            registrationDAOLogger.error("This is Error message", e);
            throw e;
        } catch (InvalidAlgorithmParameterException ex) {
            java.util.logging.Logger.getLogger(RegistrationDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            java.util.logging.Logger.getLogger(RegistrationDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(RegistrationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        registrationDAOLogger.info(" RegistrationDAO End of updatePassword method");
    }

    /**
     * checkEmail method is finding userId is existing or not
     *
     *
     * @param userId
     * @return
     * @throws Exception
     */
    public Boolean checkEmail(String emailId) throws Exception {
        registrationDAOLogger.info("Start running getUserId method in RegistrationDAO");
        RegistrationDO registrationDO;
        boolean returnValue = false;
        try {
            emailId = encryptionDecryption.encrypt(emailId);
            Query query = new Query();
            query.addCriteria(Criteria.where("emailId").is(emailId));
            registrationDO = mongoTemplate.findOne(query, RegistrationDO.class, COLLECTION_NAME);

            //encryptionDecryption.decryptObject(registrationDO);
            if (registrationDO != null) {
                encryptionDecryption.decryptObject(registrationDO);
                returnValue = true;
            }
        } catch (DataAccessException dae) {
            registrationDAOLogger.error("DataAccessException in getUserId method of RegistrationDAO", dae);
            throw dae;
        } catch (MongoException me) {
            registrationDAOLogger.error("MongoException in getUserId method of RegistrationDAO", me);
            throw me;
        } catch (Exception e) {
            registrationDAOLogger.error("Exception in getUserId method of RegistrationDAO", e);
            throw e;
        }
        registrationDAOLogger.info("End of getUserId method in RegistrationDAO");
        return returnValue;
    }

    public RegistrationDO getOrganizationName(String organizationName) {
        
         registrationDAOLogger.info("Going to run  LoginDAO listData method");
        RegistrationDO registrationDO = null;
        try {
            String organizationFirst = encryptionDecryption.encrypt(organizationName);
            //password = encryptionDecryption.encrypt(password);

            Query queryForFinding = new Query();

            queryForFinding.addCriteria(Criteria.where("organizationFirst").is(organizationFirst));
            //queryForFinding.addCriteria(Criteria.where("pass2").is(password));
           

            registrationDO = mongoTemplate.findOne(queryForFinding, RegistrationDO.class, COLLECTION_NAME);
            if (registrationDO != null) {
                encryptionDecryption.decryptObject(registrationDO);
            }

        } catch (DataAccessException dae) {
            registrationDAOLogger.error("This is Error message", dae);
           /// throw dae;
        } catch (MongoException me) {
            registrationDAOLogger.error("This is Error message", me);
           // throw me;
        } catch (Exception e) {
            registrationDAOLogger.error("This is Error message", e);
            //throw e;
        }
        registrationDAOLogger.info(" LoginDAO End of listData method");
        return registrationDO;
    }

    public Boolean checkOrganizationEmail(String reEmail) {
       registrationDAOLogger.info("Start running getUserId method in RegistrationDAO");
        RegistrationDO registrationDO;
        boolean returnValue = false;
        try {
            String organizationEmailId = encryptionDecryption.encrypt(reEmail);
            Query query = new Query();
            query.addCriteria(Criteria.where("organizationEmailId").is(organizationEmailId));
            registrationDO = mongoTemplate.findOne(query, RegistrationDO.class, COLLECTION_NAME);

            //encryptionDecryption.decryptObject(registrationDO);
            if (registrationDO != null) {
                encryptionDecryption.decryptObject(registrationDO);
                returnValue = true;
            }
        } catch (DataAccessException dae) {
            registrationDAOLogger.error("DataAccessException in getUserId method of RegistrationDAO", dae);
            throw dae;
        } catch (MongoException me) {
            registrationDAOLogger.error("MongoException in getUserId method of RegistrationDAO", me);
            throw me;
        } catch (Exception e) {
            registrationDAOLogger.error("Exception in getUserId method of RegistrationDAO", e);
            //throw e;
        }
        registrationDAOLogger.info("End of getUserId method in RegistrationDAO");
        return returnValue;
    }

    public RegistrationDO findEmailId(String userId) {
         registrationDAOLogger.info("Going to run  LoginDAO listData method");
        RegistrationDO registrationDO = null;
        try {
            String organizationFirst = encryptionDecryption.encrypt(userId);
            //password = encryptionDecryption.encrypt(password);

            Query queryForFinding = new Query();

            queryForFinding.addCriteria(Criteria.where("userId").is(userId));
            //queryForFinding.addCriteria(Criteria.where("pass2").is(password));
           

            registrationDO = mongoTemplate.findOne(queryForFinding, RegistrationDO.class, COLLECTION_NAME);
            if (registrationDO != null) {
                encryptionDecryption.decryptObject(registrationDO);
            }

        } catch (DataAccessException dae) {
            registrationDAOLogger.error("This is Error message", dae);
           /// throw dae;
        } catch (MongoException me) {
            registrationDAOLogger.error("This is Error message", me);
           // throw me;
        } catch (Exception e) {
            registrationDAOLogger.error("This is Error message", e);
            //throw e;
        }
        registrationDAOLogger.info(" LoginDAO End of listData method");
        return registrationDO;//To change body of generated methods, choose Tools | Templates.
    }

    public void upadteEmail(String userId, String emailId) {
       registrationDAOLogger.info("Going to run  RegistrationDAO updatePassword method");
        try {
           // uniqueId = encryptionDecryption.encrypt(uniqueId);
           // rePassword = encryptionDecryption.encrypt(rePassword);
           // rePasswordc = encryptionDecryption.encrypt(rePasswordc);
            Query queryForId = new Query();
            queryForId.addCriteria(Criteria.where("userId").is(userId));
            Update update = new Update();
            // encryptionDecryption.encryptObject(registrationDO);
            update.set("emailId",  emailId);
           

            mongoTemplate.updateFirst(queryForId, update, COLLECTION_NAME);

        } catch (DataAccessException dae) {
            registrationDAOLogger.error("This is Error message", dae);
            throw dae;
        } catch (MongoException me) {
            registrationDAOLogger.error("This is Error message", me);
            throw me;
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(RegistrationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        registrationDAOLogger.info(" RegistrationDAO End of updatePassword method");
    }

    public RegistrationDO getRegistrationDetailsById(String id) {
         registrationDAOLogger.info("Going to run  RegistrationDAO updatePassword method");
         RegistrationDO registrationDO = null;
         try {
           // uniqueId = encryptionDecryption.encrypt(uniqueId);
           // rePassword = encryptionDecryption.encrypt(rePassword);
           // rePasswordc = encryptionDecryption.encrypt(rePasswordc);
            Query queryForId = new Query();
            queryForId.addCriteria(Criteria.where("_id").is(id));
           
           

           registrationDO= mongoTemplate.findOne(queryForId, RegistrationDO.class, COLLECTION_NAME);

        } catch (DataAccessException dae) {
            registrationDAOLogger.error("This is Error message", dae);
            throw dae;
        } catch (MongoException me) {
            registrationDAOLogger.error("This is Error message", me);
            throw me;
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(RegistrationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        registrationDAOLogger.info(" RegistrationDAO End of updatePassword method");
        return registrationDO;
    }
     public RegistrationDO getUserDetals(String userId,String password) throws Exception {
        registrationDAOLogger.info("Going to run RegistrationDAO getUserDetals method");
        RegistrationDO registrationDO = null;
        try {
            String encrypteduserId = encryptionDecryption.encrypt(userId);
            String encryptedpassword = encryptionDecryption.encrypt(password);
            //password = encryptionDecryption.encrypt(password);

            Query queryForFinding = new Query();

            queryForFinding.addCriteria(Criteria.where("userId").is(encrypteduserId));
            queryForFinding.addCriteria(Criteria.where("pass1").is(encryptedpassword));
           

            registrationDO = mongoTemplate.findOne(queryForFinding, RegistrationDO.class, COLLECTION_NAME);
            if (registrationDO != null) {
                encryptionDecryption.decryptObject(registrationDO);
            }

        } catch (DataAccessException dae) {
            registrationDAOLogger.error("This is Error message", dae);
            throw dae;
        } catch (MongoException me) {
            registrationDAOLogger.error("This is Error message", me);
            throw me;
        } catch (Exception e) {
            registrationDAOLogger.error("This is Error message", e);
            throw e;
        }
        registrationDAOLogger.info(" LoginDAO End of listData method");
        return registrationDO;
    }
      /**
     * findApp method is find all application using userId
     *
     * @param userId
     * @param organizationName
     * @return
     * @throws java.lang.Exception
     */
    public List<SurveyDO> findApplication(String userId, String organizationName) throws Exception {
        registrationDAOLogger.info("Going to run  StartAdviceDAO findApp method");
        List<SurveyDO> surveyDOhasedList = new ArrayList<>();

        try {

            userId = encryptionDecryption.encrypt(userId);

            Query query = new Query();

            query.addCriteria(Criteria.where("userId").is(userId));
            query.addCriteria(Criteria.where("application").not().regex("CommonForOrganization"));
            List<SurveyDO> surveyDOList = mongoTemplate.find(query, SurveyDO.class, "survey");

            for (SurveyDO surveyDOObject : surveyDOList) {
                encryptionDecryption.decryptObject(surveyDOObject);

                surveyDOhasedList.add(surveyDOObject);
            }
            encryptionDecryption.decrypt(userId);
        } catch (DataAccessException dae) {
            registrationDAOLogger.error("This is Error message", dae);
            throw dae;
        } catch (MongoException me) {
            registrationDAOLogger.error("This is Error message", me);
            throw me;
        } catch (Exception e) {
            registrationDAOLogger.error("This is Error message", e);
            throw e;
        }
        registrationDAOLogger.info(" StartAdviceDAO End of findApp method");

        return surveyDOhasedList;
    }
//  /**
//     *
//     * @param args
//     */
//    public static void main(String[] args) {
//        RegistrationDAO registrationDAO = new RegistrationDAO();
////        registrationDAO.addActivateData(activeDO);
////        registrationDAO.addData(registrationDO);
////        registrationDAO.admin(user);
////        registrationDAO.getRegistraionData(COLLECTION_NAME);
////        registrationDAO.getUserId(COLLECTION_NAME);
//
//        //RegistrationDO abc = registrationDAO.listData(userLogInForm);
////        registrationDAO.retrieveActivatePage(COLLECTION_NAME);
////        registrationDAO.updateActive(COLLECTION_NAME);
//    }

   public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    
}
