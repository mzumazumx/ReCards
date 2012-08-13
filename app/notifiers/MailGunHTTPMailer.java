package notifiers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import javax.mail.internet.InternetAddress;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;

import models.User;
import play.Logger;
import play.Play;
import play.classloading.enhancers.LocalvariablesNamesEnhancer.LocalVariablesNamesTracer;
import play.exceptions.MailException;
import play.exceptions.TemplateNotFoundException;
import play.libs.Mail;
import play.mvc.Router;
import play.templates.Template;
import play.templates.TemplateLoader;

import sun.misc.BASE64Encoder;

public class MailGunHTTPMailer {

	private static void send(String content, String subject, String to) throws IOException {
		String apiKey = Play.configuration.getProperty("mailgun.apiKey");

		String data = URLEncoder.encode("from", "UTF-8") + "="
				+ URLEncoder.encode("Recards <recards@mzumazumx.de>", "UTF-8");
		data += "&" + URLEncoder.encode("to", "UTF-8") + "=" + URLEncoder.encode(to, "UTF-8");
		data += "&" + URLEncoder.encode("subject", "UTF-8") + "=" + URLEncoder.encode(subject, "UTF-8");
		data += "&" + URLEncoder.encode("html", "UTF-8") + "=" + URLEncoder.encode(content, "UTF-8");

		String encoding = new BASE64Encoder().encode(("api:" + apiKey).getBytes());
		URL url = new URL("https://api.mailgun.net/v2/recards.mailgun.org/messages");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setRequestProperty("Authorization", "Basic " + encoding);

		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(data);
		wr.flush();
		wr.close();

		Logger.info("Sending E-Mail to " + to);
		Logger.info("Subject: " + subject);
		Logger.info("Content: " + content);

		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		while ((line = rd.readLine()) != null) {
			Logger.info(line);
		}
		rd.close();
	}

	private static void sendPlay(String recipient, String subject, String templateName,
			Map<String, Object> templateHtmlBinding) throws EmailException {
		Template templateHtml = TemplateLoader.load("MailGunHTTPMailer/" + templateName + ".html");
		String bodyHtml = templateHtml.render(templateHtmlBinding);

		try {
			send(bodyHtml, subject, recipient);
		} catch (IOException e) {
			throw new MailException(e.getMessage());
		}
	}

	public static void sendConfirm(User user) throws EmailException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", user.id);
		map.put("code", user.code);
		String link = Router.getFullUrl("Auth.confirm", map);

		Map<String, Object> templateHtmlBinding = new HashMap<String, Object>();
		templateHtmlBinding.put("link", link);
		templateHtmlBinding.put("user", user);

		sendPlay(user.email, "Your signup at reCards", "sendConfirm", templateHtmlBinding);
	}

	public static void sendReset(User user) throws EmailException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", user.id);
		map.put("token", user.resetToken);
		String link = Router.getFullUrl("Auth.showReset", map);

		Map<String, Object> templateHtmlBinding = new HashMap<String, Object>();
		templateHtmlBinding.put("link", link);
		templateHtmlBinding.put("user", user);

		sendPlay(user.email, "Reset your password at reCards", "sendReset", templateHtmlBinding);
	}
}
