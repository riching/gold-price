package me.riching.goldprice.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(MailService.class);

	private static final String FROM = "suolunzhidiao@163.com";
	private static final String[] TO = { "18888823527@163.com", "284020437@qq.com" };
	@Autowired
	private JavaMailSender javaMailSender;

	private ExecutorService executorService = Executors.newSingleThreadExecutor();

	public void sendHtmlMail(final String subject, final String htmlText) {
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				MimeMessage message = javaMailSender.createMimeMessage();
				MimeMessageHelper helper;
				try {
					helper = new MimeMessageHelper(message, true, "utf-8");
					helper.setTo(TO);
					helper.setFrom(FROM, "索伦");
					helper.setSubject(subject);
					helper.setText(htmlText, true);

					javaMailSender.send(message);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		});

	}
}
