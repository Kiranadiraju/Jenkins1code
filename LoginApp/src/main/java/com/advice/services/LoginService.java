/**
 *LoginSerrvice is for getting  authenticating user and getting roles from  registrationDAO
 *
 *
 * @author  CJP@venkat
 * @version 1.0
 * @since
 **/
package com.advice.services;

import com.advice.dao.RegistrationDAO;
import com.advice.dos.PasswordRecoveryDO;
import com.advice.dos.RegistrationDO;
import com.advice.dos.SurveyDO;
import com.advice.forms.RecoveryForm;
import com.advice.forms.RegistrationForm;
import com.advice.forms.UserLogInForm;
import java.util.List;
import java.util.Random;
import java.util.Properties;
import java.util.UUID;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**loginApp/loginservice/authenticateUser
 *
 * @author cjp
 */
@RestController
public class LoginService {

    @Autowired
    RegistrationDAO registrationDAO;
    @Autowired
    private JavaMailSender mailSender;

    private static final Logger loginlogger = Logger.getLogger(LoginService.class);

    /**
     * AuthenticateUser for finding authorized user Extract registrationDOList
     * and check user is authorized user or not.
     *
     * @param userLogInForm
     * @return true or false
     * @throws java.lang.Exception
     */
  
    @RequestMapping(value = "/loginservice/authenticateUser", method = RequestMethod.POST)   
  public @ResponseBody boolean authenticateUser(@RequestBody UserLogInForm userLogInForm) throws Exception {
        loginlogger.info("Going to run  LoginService AuthenticateUser method"+userLogInForm.getUserId());
        boolean returnValue = false;
        try {

            String userId = userLogInForm.getUserId();
            String password = userLogInForm.getPassword();
            RegistrationDO registrationDO = registrationDAO.getUser(userId, password);
            //System.out.println("authenticateUser1 : "+!registrationDO.getPass2().equals(userLogInForm.getPassword()));
            if (registrationDO.getUserId().equals(userLogInForm.getUserId()) && registrationDO.getPass2() != userLogInForm.getPassword() && !registrationDO.getUserId().equals(null) && !registrationDO.getPass2().equals(null)) {
                //if (registrationDO.getActive().equals("1")) {
                returnValue = true;
                // }

            }

        } catch (Exception e) {
            loginlogger.error("This is Error message", e);

        }
        loginlogger.info(" LoginService End of AuthenticateUser method");
        return returnValue;
    }
  
    @RequestMapping(value = "/loginservice/isValidUserName",  method = RequestMethod.POST)   
  public @ResponseBody boolean isValidUserName(@RequestBody String userId) {
        
         loginlogger.info("Going to run  LoginService AuthenticateUser method");
        boolean returnValue = false;
        try {
           
            RegistrationDO registrationDO = registrationDAO.getUser(userId);
            if(registrationDO != null)
                return true;
        } catch (Exception e) {
            loginlogger.error("This is Error message", e);

        }
        loginlogger.info(" LoginService End of AuthenticateUser method");
        return false;
        
    }
     
    @RequestMapping(value = "/loginservice/isValidUser",method = RequestMethod.POST)   
  public @ResponseBody boolean isValidUser(@RequestBody UserLogInForm userLogInForm) {
        
         loginlogger.info("Going to run  LoginService isValidUser method");
        boolean returnValue = false;
        try {
           
            RegistrationDO registrationDO = registrationDAO.getUserDetals(userLogInForm.getUserId(),userLogInForm.getPassword());
            if(registrationDO != null)
                return true;
        } catch (Exception e) {
            loginlogger.error("This is Error message", e);

        }
        loginlogger.info(" LoginService End of isValidUser method");
        return false;
        
    }

   @RequestMapping(value = "/loginservice/authenticateActivation",method = RequestMethod.POST)   
  public @ResponseBody boolean authenticateActivation(@RequestBody UserLogInForm userLogInForm) throws Exception {
        loginlogger.info("Going to run  LoginService AuthenticateUser method");
        boolean returnValue = false;
        try {

            String userId = userLogInForm.getUserId();
            String password = userLogInForm.getPassword();
            RegistrationDO registrationDO = registrationDAO.getUser(userId);
            //  System.out.println("authenticateUser1 : "+!registrationDO.getPass2().equals(userLogInForm.getPassword()));
            //if (registrationDO.getPass2().equals(userLogInForm.getPassword())) {
                if (registrationDO.getActive().equals("1")) {
                    returnValue = true;
                }

            //}

        } catch (Exception e) {
            loginlogger.error("This is Error message", e);

        }
        loginlogger.info(" LoginService End of AuthenticateUser method");
        return returnValue;
    }

    /**
     * Admin is for finding admin role
     *
     * @param userLogInForm
     * @return true or false
     * @throws java.lang.Exception
     */
    
    @RequestMapping(value = "/loginservice/getAdminRole",method = RequestMethod.POST)   
  public @ResponseBody boolean getAdminRole(@RequestBody UserLogInForm userLogInForm) throws Exception {
        loginlogger.info("Going to run  LoginService AuthenticateUser method");

        boolean returnValue = false;
        try {

            RegistrationDO registrationDO = registrationDAO.admin(userLogInForm.getUserId(), userLogInForm.getPassword());
            if (registrationDO == null || registrationDO.getRole() == null) {
                returnValue = false;
            } else if (registrationDO.getRole().equals("administrator")) {
                returnValue = true;
            } else {
                returnValue = false;
            }

        } catch (Exception e) {
            loginlogger.error("This is Error message", new Exception("NullPointerException"));
        }
        loginlogger.info("LoginService End of AuthenticateUser method");
        return returnValue;

    }

    /**
     * Generate a CAPTCHA String consisting of random lowercase & uppercase
     * letters, and numbers.
     *
     * @return
     */
    

    @RequestMapping(value = "/loginservice/generateCaptcha",method = RequestMethod.GET)   
  public @ResponseBody String generateCaptcha() {
        
        String CaptchaText = null;
        try {
            Random random = new Random();
            int length = 5;
            StringBuilder captchaStringBuffer = new StringBuilder();
            for (int i = 0; i < length; i++) {
                int captchaNumber = Math.abs(random.nextInt()) % 60;
                
                int charNumber = 0;
                if (captchaNumber < 26) {
                    charNumber = 65 + captchaNumber;
                } else if (captchaNumber < 52) {
                    charNumber = 65 + (captchaNumber - 26);
                } else {
                    charNumber = 48 + (captchaNumber - 52);
                }
                captchaStringBuffer.append((char) charNumber);
            }
            CaptchaText = captchaStringBuffer.toString();
        } catch (Exception e) {

        }
        return CaptchaText;
    }
  
 
 @RequestMapping(value = "/loginservice/getUserName",method = RequestMethod.POST)   
  public @ResponseBody String getUserName(@RequestBody String userId){
         String userName = null;
        try{
            RegistrationDO registrationDO= registrationDAO.getRegistraionDetailsByUserId(userId);
            userName = registrationDO.getFirstName();
        }
        catch(Exception e){
            
        }
        return userName;
    }

  @RequestMapping(value = "/loginservice/getFirstAndLastNameById",method = RequestMethod.POST)   
  public @ResponseBody RegistrationForm getFirstAndLastNameById(@RequestBody String userId) {
        loginlogger.info("Going to run  RegistrationService getNoofAppsAllowed method");
        //RegistrationForm registrationForm = new RegistrationForm();
        RegistrationForm registrationForm = new RegistrationForm();

        try {
            //System.out.println("UID : " + userId);

            RegistrationDO registrationDO = registrationDAO.getRegistraionDetailsByUserId(userId);

            registrationForm.setFirstName(registrationDO.getFirstName());
            registrationForm.setLastName(registrationDO.getLastName());

            loginlogger.info("applicationsCount : " + registrationForm.getFirstName() + " : " + registrationForm.getLastName());//12

        } catch (Exception ex) {
            loginlogger.error("Exception occured when gettingApplications count, failed", ex);
        }
        loginlogger.info(" RegistrationService End of getNoofAppsAllowed method");
        return registrationForm;
    }
      
        @RequestMapping(value = "/loginservice/resendEmail", method = RequestMethod.POST)   
  public @ResponseBody void resendEmail(@RequestBody String userId,@RequestBody  String requestUrl) {
        try {
            RegistrationDO registrationDO = registrationDAO.getRegistraionDetailsByUserId(userId);
            //recipent address
            //registrationDO.setEmailId(registrationForm.getEmailId());
            
             String smtpServer = "gator4041.hostgator.com";
            int port = 465;
            final String userid = "advise@cloudjournee.com";//change accordingly
            final String password = "Cjptech@12";//change accordingly
            String contentType = "text/plain";
           
            String to = registrationDO.getEmailId();//some invalid address
            String bounceAddr = "advise@cloudjournee.com";//change accordingly
           
            Properties props = new Properties();

            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", smtpServer);
            props.put("mail.smtp.port", "465");
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.from", bounceAddr);
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

            Session mailSession = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(userid, password);
                }
            });
            
            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(registrationDO.getEmailId());
            email.setFrom("advise@cloudjournee.com");
            //setting subject for email.
            String subject = "Welcome to CloudJournee - Online Cloud Assessment";
            // requestUrl.delete(requestUrl.indexOf(("/"), requestUrl.indexOf("/")+1), requestUrl.length());
            loginlogger.info("The Modified requestUrl: " + requestUrl);
            // here create automatic generated link wirh random unique key.
            String url = requestUrl + "/activation?active=" + registrationDO.getId();
            String firstName = registrationDO.getFirstName().substring(0, 1).toUpperCase() + registrationDO.getFirstName().substring(1);
            String lastName = registrationDO.getLastName().substring(0, 1).toUpperCase() + registrationDO.getLastName().substring(1);

            //here we create message txt
            String body = "Hello " + firstName + " " + lastName + "\n\nYou have received this email message because you have requested for an online Cloud Assessment from CloudJournee."
                    + "\n\nUserId : " + registrationDO.getUserId() + "" + "\n\nPassword : " + registrationDO.getPass1() + ""
                    + "\n\nClick on the link below to activate your account. \n\n" + url + "\n\nIf you are unable to access the assessment or face any issues during the online assessment," + "\n\nplease contact us at advise@cloudjournee.com" + "\n\nThis is an automatically generated email, please do not reply to this email."
                    + "\n\nNote:- If you did not create this account, please ignore it." + "\n\nThanks," + "\n\nTeam CloudJournee" + "\n\nVisit us at: www.cloudjournee.com";

           // email.setSubject(subject);
           // email.setText(message);
            ///sending mail 
            //mailSender.send(email);//To change body of generated methods, choose Tools | Templates.
             MimeMessage message = new MimeMessage(mailSession);
            // message.addFrom(InternetAddress.parse(from));
            message.setRecipients(Message.RecipientType.TO, to);
            
            message.setSubject(subject);
            message.setContent(body, contentType);
            Transport transport = mailSession.getTransport();

            // System.out.println("Sending ....");
            transport.connect(smtpServer, port, userid, password);
            transport.sendMessage(message,
                    message.getRecipients(Message.RecipientType.TO));
            System.out.println("Sending done ...");
            //here passing registrationDO object registrationDAO through addData method
            transport.close();
        } catch (Exception ex) {
            loginlogger.error("Exception occured when email resent, failed", ex);
        }
    }
            /**
     *
     * @param userLogInForm
     * @return
     * @throws java.lang.Exception
     */
   
    @RequestMapping(value = "/loginservice/getApplications",method = RequestMethod.POST)   
  public @ResponseBody List<SurveyDO> getApplications(@RequestBody UserLogInForm userLogInForm) throws Exception {
        loginlogger.info("Going to run  ReportService getApplications method");
        List<SurveyDO> applicationsList = null;
        try {
            String organizationName = registrationDAO.findOrganizationDetails(userLogInForm.getUserId());
            applicationsList = registrationDAO.findApplication(userLogInForm.getUserId(), organizationName);
        } catch (Exception e) {

        }
        loginlogger.info(" ReportService end of getApplications method");
        return applicationsList;
    }
     
      @RequestMapping(value = "/loginservice/isMailValidate",method = RequestMethod.POST)   
  public @ResponseBody Boolean isMailValidate(@RequestBody String reEmail) {
        Boolean mailStatus = null;

        try {
            mailStatus = registrationDAO.checkEmail(reEmail);
        } catch (Exception ex) {
            loginlogger.error("Exception occured when email is validate or not, failed", ex);
        }
        return mailStatus;
    }
 
 @RequestMapping(value = "/loginservice/getFirstAndLastNameByEmail",method = RequestMethod.POST)   
  public @ResponseBody RegistrationForm getFirstAndLastNameByEmail(@RequestBody String emailId) {
        loginlogger.info("Going to run  RegistrationService getNoofAppsAllowed method");
        //RegistrationForm registrationForm = new RegistrationForm();
        RegistrationForm registrationForm = new RegistrationForm();

        try {
            //System.out.println("UID : " + emailId);

            RegistrationDO registrationDO = registrationDAO.getRegistraionDetailsEmailId(emailId);

            registrationForm.setFirstName(registrationDO.getFirstName());
            registrationForm.setLastName(registrationDO.getLastName());

            loginlogger.info("applicationsCount : " + registrationForm.getFirstName() + " : " + registrationForm.getLastName());//12

        } catch (Exception ex) {
            loginlogger.error("Exception occured when gettingApplications count, failed", ex);
        }
        loginlogger.info(" RegistrationService End of getNoofAppsAllowed method");
        return registrationForm;
    }

    @RequestMapping(value = "/loginservice/findRegistrationByEmailId",method = RequestMethod.POST)   
  public @ResponseBody RegistrationDO findRegistrationByEmailId(@RequestBody String id) {
        loginlogger.info("Going to run  RegistrationService findRegistrationByEmailId method");
        RegistrationDO registrationDO = null;
        try {
            PasswordRecoveryDO passwordRecoveryDO = registrationDAO.findPasswordReset(id);
            registrationDO = registrationDAO.getRegistrationDetailsById(passwordRecoveryDO.getUniqueId());
        } catch (Exception e) {

        }
        loginlogger.info(" RegistrationService End of getNoofAppsAllowed method");
        return registrationDO;
    }

/**
     *
     * @param recoveryForm
     * @param requestUrl
     * @throws Exception
     */
  
    @RequestMapping(value = "/loginservice/forgotPassword",method = RequestMethod.POST)
    public @ResponseBody void forgotPassword(@RequestBody RecoveryForm recoveryForm,@RequestBody  String requestUrl) throws Exception {
        loginlogger.info("Start running  RegistrationService forgotPassword method");
        try {

            PasswordRecoveryDO passwordRecoveryDO = new PasswordRecoveryDO();
            String emailId;
            RegistrationDO registrationDO = null;
            String uniqueKey = UUID.randomUUID().toString();

            String userId = recoveryForm.getUserId();
            System.out.println("UserId is :" + userId);
            if (userId == null || userId.equals("")) {

                emailId = recoveryForm.getReEmail();
                registrationDO = registrationDAO.getRegistraionDetailsEmailId(emailId);
                // uniqueKey = userIdByEmail.getId();
            } else {
                registrationDO = registrationDAO.getRegistraionDetailsByUserId(userId);
                emailId = registrationDO.getEmailId();
                System.out.println("If userId is something : " + emailId);
            }
            
            
            String smtpServer = "gator4041.hostgator.com";
            int port = 465;
            final String userid = "advise@cloudjournee.com";//change accordingly
            final String password = "Cjptech@12";//change accordingly
            String contentType = "text/plain";
           
            String to = registrationDO.getEmailId();//some invalid address
            String bounceAddr = "advise@cloudjournee.com";//change accordingly
           
            Properties props = new Properties();

            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", smtpServer);
            props.put("mail.smtp.port", "465");
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.from", bounceAddr);
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

            Session mailSession = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(userid, password);
                }
            });

           // SimpleMailMessage email = new SimpleMailMessage();
           // email.setTo(emailId);
            //generating random unique key

            String subject = "Welcome to CloudJournee - Online Cloud Assessment-Request for Password Reset";
            String firstName = registrationDO.getFirstName().substring(0, 1).toUpperCase() + registrationDO.getFirstName().substring(1);
            String lastName = registrationDO.getLastName().substring(0, 1).toUpperCase() + registrationDO.getLastName().substring(1);

            // here create automatic generated link wirh random unique key.
            String url = requestUrl + "/PasswordRecovey?resetpassword=" + uniqueKey;
            String body = "Hello " + firstName + " " + lastName + ",\n\nYou have requested to reset your password for your CloudJournee account. Click on the link below to reset your password."
                    + "\n\nUserId: " + registrationDO.getUserId() + "\n\n" + url + "\n\nIf you are unable to access the assessment or face any issues during the online assessment," + "\n\nPlease contact us at advise@cloudjournee.com" + "\n\nThis is an automatically generated email, please do not reply to this email."
                    + "\n\nNote:- If you did not create this account, please ignore it." + "\n\nThanks," + "\n\nTeam CloudJournee" + "\n\nVisit us at: www.cloudjournee.com";
          
             MimeMessage message = new MimeMessage(mailSession);
            // message.addFrom(InternetAddress.parse(from));
            message.setRecipients(Message.RecipientType.TO, to);
            
            message.setSubject(subject);
            message.setContent(body, contentType);
            Transport transport = mailSession.getTransport();

            // System.out.println("Sending ....");
            transport.connect(smtpServer, port, userid, password);
            transport.sendMessage(message,
                    message.getRecipients(Message.RecipientType.TO));
            System.out.println("Sending done ...");
            //here passing registrationDO object registrationDAO through addData method
            transport.close();


        //here we create message txt
//            email.setFrom("advise@cloudjournee.com");
//            email.setSubject(subject);
//            email.setText(message);
//            ///sending mail 
//            mailSender.send(email);
            passwordRecoveryDO.setId(uniqueKey);
            passwordRecoveryDO.setUniqueId(registrationDO.getId());
            passwordRecoveryDO.setReEmail(emailId);
            passwordRecoveryDO.setReset("0");
            registrationDAO.insertRecoveryPassword(passwordRecoveryDO);

//            RegistrationDO registrationDOObject = registrationDAO.getRegistrationDetails(registrationDO);
//
//            if (registrationDOObject != null) {
//
//                registrationDOObject.setPass1(recoveryForm.getRePassword());
//                registrationDOObject.setPass2(recoveryForm.getRePasswordc());
//
//                registrationDAO.updatePassword(registrationDOObject);
//            }
        } catch (Exception e) {
            loginlogger.error("Exception occured when fogetpassword, failed", e);
        }
        loginlogger.info("End of forgotPassword method");
    }
  
    @RequestMapping(value = "/loginservice/resetPassword",method = RequestMethod.POST)
    public @ResponseBody void resetPassword(@RequestBody RecoveryForm recoveryForm) {
        loginlogger.info("Going to run  RegistrationService findOrganization method");

        try {

            PasswordRecoveryDO passwordRecoveryDO = registrationDAO.findPasswordReset(recoveryForm.getId());

            registrationDAO.updatePassword(passwordRecoveryDO.getUniqueId(), recoveryForm.getRePassword(), recoveryForm.getRePasswordc());
        } catch (Exception e) {

        }
        loginlogger.info(" RegistrationService End of findOrganization method");
    }


//    /**
//     *
//     * @param args
//     */
//    public static void main(String[] args) {
////        LoginService loginService=new LoginService();
////        loginService.Admin(userLogInForm);
////        loginService.AuthenticateUser(userLogInForm);
//    }

}
