package org.covid.inventory.service.impl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);
	
	@Autowired
    private JavaMailSender sender;
	
	public String sendMail(String emailId, Integer orderNo,String textMessage) {
	        MimeMessage message = sender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message);
	
	        try {
	            helper.setTo(emailId);
	            helper.setText(textMessage, true);
	            helper.setSubject("Shop with social distance : Order No "+orderNo);
	            sender.send(message);
	        } catch (MessagingException e) {
	            e.printStackTrace();
	            LOGGER.info("Error while sending mail .. to : " +emailId);
	            return "Error while sending mail ..";
	        }
	        LOGGER.info("Mail Sent Success! to : " +emailId);
	        return "Mail Sent Success!";
	    }
}

