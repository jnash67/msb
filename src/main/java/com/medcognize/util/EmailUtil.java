package com.medcognize.util;

import com.microtripit.mandrillapp.lutung.MandrillApi;
import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage;
import com.microtripit.mandrillapp.lutung.view.MandrillTemplate;
import lombok.extern.slf4j.Slf4j;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Slf4j
public class EmailUtil implements Serializable {

    private static MandrillApi mandrillApi = null;
    private static String newUserMailbox = "new_user@medcognize.com";
    private static String customerSupportMailbox = "customer_support@medcognize.com";

    public static void sendWelcomeEmail(String to) {
        if (null == mandrillApi) {
            mandrillApi = new MandrillApi("Nlx2jS1r9BvqKBHDeD1_Gw");
        }
        MandrillTemplate template;
        try {
//            template = mandrillApi.templates().info("welcome");
//            System.out.println("Template name " + template.getName());
            //  final HashMap<String, String> templateContent = new HashMap<String, String>();
            // templateContent.put("username", to);

//            List<MandrillMessage.MergeVarBucket> mergeVarBucketList = new ArrayList<>();
//            MandrillMessage.MergeVar[] vars = new MandrillMessage.MergeVar[]{new MandrillMessage.MergeVar
// ("username", to)};
//            MandrillMessage.MergeVarBucket mvb = new MandrillMessage.MergeVarBucket();
//            mvb.setVars(vars);
//            mergeVarBucketList.add(mvb);

            final MandrillMessage m = new MandrillMessage();
            MandrillMessage.MergeVar mv1 = new MandrillMessage.MergeVar("username", to);
            List<MandrillMessage.MergeVar> mergeVars = new ArrayList<>();
            mergeVars.add(mv1);
            m.setGlobalMergeVars(mergeVars);
            m.setSubject("Welcome to Medcognize.com");
            // message.setHtml("<h1>Hi pal!</h2><br />Really, I'm just saying hi!");
            m.setAutoText(true);
            m.setFromEmail(customerSupportMailbox);
            m.setFromName("Customer Support");
            // add recipients
            ArrayList<MandrillMessage.Recipient> recipients = new ArrayList<MandrillMessage.Recipient>();
            MandrillMessage.Recipient recipient = new MandrillMessage.Recipient();
            recipient.setType(MandrillMessage.Recipient.Type.TO);
            recipient.setEmail(to);

            MandrillMessage.Recipient support = new MandrillMessage.Recipient();
            support.setType(MandrillMessage.Recipient.Type.BCC);
            support.setEmail(newUserMailbox);

            recipients.add(recipient);
            recipients.add(support);

            m.setTo(recipients);
            m.setPreserveRecipients(false);
            m.setMerge(true);
            ArrayList<String> tags = new ArrayList<String>();
            tags.add("welcome");
            m.setTags(tags);
            mandrillApi.messages().sendTemplate("Welcome", null, m, null);

        } catch (MandrillApiError mandrillApiError) {
            String errorText = mandrillApiError.getMandrillErrorName() + "\n" + mandrillApiError
                    .getMandrillErrorStatus() + "\n" +
                    mandrillApiError.getMandrillErrorMessage() + "\n" + mandrillApiError.getMandrillErrorAsJson();
            sendWelcomeMessageErrorNotification(to, errorText);
            mandrillApiError.printStackTrace();
        } catch (IOException e) {
            String errorText = "IOException " + "\n" + e.getMessage() + "\n" + e.toString();
            sendWelcomeMessageErrorNotification(to, errorText);
            e.printStackTrace();
        } catch (Exception e) {
            String errorText = "Other exception " + "\n" + e.getMessage() + "\n" + e.toString();
            log.error("Unexpected exception " + errorText);
            sendWelcomeMessageErrorNotification(to, errorText);
            e.printStackTrace();
        }
    }

    public static void sendSimpleEmail(String toEmail, String fromEmail, String subject, String text) {
        String host = "medcognizecom.ipage.com";
        int port = 587;
        String login = customerSupportMailbox;
        String password = "aVl54BAHlKh4kNvyRlie";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props);
        if (null == fromEmail) {
            fromEmail = login;
        }

        try {
            Message message = new MimeMessage(session);
            Address[] a = new Address[1];
            a[0] = new InternetAddress(fromEmail);
            message.setFrom(a[0]);
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));

            message.setSubject(subject);
            message.setText(text);

            Transport transport = session.getTransport("smtp");
            try {
                transport.connect(host, port, login, password);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("connection refused");
            }

            transport.sendMessage(message, a);
            System.out.println("Simple Email Successfully Sent");
        } catch (MessagingException e) {
            log.error("Error " + e.toString() + " sending email to " + toEmail + " from " + fromEmail + " with " +
                    "text " + text);
        }
    }

    public static void sendNewUserNotification(String newUserEmail) {
        String subject = "New User Signup with address " + newUserEmail;
        String text = "New user has signed up with email address " + newUserEmail;
        sendSimpleEmail(newUserMailbox, customerSupportMailbox, subject, text);
    }

    public static void sendWelcomeMessageErrorNotification(String newUserEmail, String errorInfo) {
        String subject = "Problem sending welcome message to new user with address " + newUserEmail;
        sendSimpleEmail(newUserMailbox, customerSupportMailbox, subject, errorInfo);
    }

}