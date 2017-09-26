package com.odan.common.utils;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class Mailer {

	static Properties properties = new Properties();

	static Properties mailServerProperties;
	static Session getMailSession;
	static MimeMessage generateMailMessage;
	static Configuration cfg;

	public Mailer() throws IOException {
//		cfg = new Configuration(Configuration.VERSION_2_3_22);
//		cfg.setDirectoryForTemplateLoading(new File("/email_template"));
//		cfg.setDefaultEncoding("UTF-8");
//		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
//		cfg.setLogTemplateExceptions(false);
	}

	public static void send(List<String> toList, String subject, String message)
			throws AddressException, MessagingException {

		// Step1
		mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.port", "587");
		mailServerProperties.put("mail.smtp.auth", "true");
		mailServerProperties.put("mail.smtp.starttls.enable", "true");

		// Step2
		getMailSession = Session.getDefaultInstance(mailServerProperties, null);
		generateMailMessage = new MimeMessage(getMailSession);
		for (String to : toList) {
			generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		}
		generateMailMessage.setSubject(subject);
		String emailBody = message;
		generateMailMessage.setContent(emailBody, "text/html");

		// Step3
		Transport transport = getMailSession.getTransport("smtp");
		transport.connect("smtp.gmail.com", "jawaidgadiwala@gmail.com", "rtnbjnyjnkpougcu");
		transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
		transport.close();

	}

	public static void send(String to, String subject, String message) throws AddressException, MessagingException {
		ArrayList<String> toList = new ArrayList<String>();
		toList.add(to);
		send(toList, subject, message);
	}
	
	public static void send(String to, String subject, String template, HashMap<String, Object> data) {
		loadTemplate(template, data);
	}

	public static void loadTemplate(String template, HashMap<String, Object> data) {
		try {
			Template temp = cfg.getTemplate(template);

			Writer out = new OutputStreamWriter(System.out);
			temp.process(data, out);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}