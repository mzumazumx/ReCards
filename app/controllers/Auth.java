package controllers;

import org.apache.commons.mail.EmailException;

import notifiers.MailGunHTTPMailer;
import models.User;
import play.cache.Cache;
import play.data.validation.Email;
import play.data.validation.Required;
import play.libs.Codec;
import play.libs.Crypto;
import play.mvc.Before;
import play.mvc.Controller;

public class Auth extends Controller {

	@Before(unless = { "login", "signup", "showConfirm", "logout", "confirm", "forgot", "showReset", "reset" })
	public static void checkaccess() {
		if (session.get("user") == null) {
			flash.error("Access denied.");
			Main.index();
		} else {
			User user = getUser();
			if (user == null || user.code != null) {
				flash.error("Access denied.");
				Main.index();
			} else
				renderArgs.put("user", user);
		}
	}

	static User getUser() {
		User user = null;
		if (session.contains("user"))
			user = User.findById(Long.parseLong(session.get("user")));
		return user;
	}

	public static void logout() {
		session.remove("user");
		flash.success("logout successful.");
		Main.index();
	}

	public static void login(String emailOrUsername, String password) {
		User user = User.find("email", emailOrUsername).first();
		if (user == null)
			user = User.find("username", emailOrUsername).first();
		validation
				.isTrue(user != null && password != null && !"".equals(password)
						&& user.password.equals(Crypto.sign(password))).key("login")
				.message("wrong username or password");
		if (validation.hasErrors()) {
			params.flash();
			validation.keep();
			Main.index();
		}
		session.put("user", user.id);
		flash.success("login successful");
		Cards.index();
	}

	public static void signup(String email, String username, String pw1, String pw2) {
		validation.isTrue(email != null && !"".equals(email)).key("signup").message("please enter your email address");
		validation.isTrue(username != null && !"".equals(username)).key("signup").message("please choose a username");
		validation.isTrue(pw1 != null && !"".equals(pw1)).key("signup").message("please choose a your password");
		validation.isTrue(pw2 != null && !"".equals(pw2)).key("signup").message("please repeat your password");
		validation.equals(pw1, pw2).key("signup").message("your passwords don't match");
		validation.isTrue(pw1.length() >= 5).key("signup").message("minimum length for password is 5");
		User user = User.find("byEmail", email).first();
		validation.isTrue(user == null).key("signup").message("that email address has already signed up");
		User user2 = User.find("byUsername", username).first();
		validation.isTrue(user2 == null).key("signup").message("username already in use");
		if (validation.hasErrors()) {
			params.flash();
			validation.keep();
			Main.index();
		}
		user = User.createUser(username, email, Crypto.sign(pw1));

		try {
			MailGunHTTPMailer.sendConfirm(user);
		} catch (EmailException e) {
			flash.error("Sorry, our Mailserver just failed.");
			Main.index();
		}

		showConfirm(user.id);
	}

	public static void showConfirm(Long id) {
		User user = User.findById(id);
		render(user);
	}

	public static void confirm(Long id, String code) {
		User user = User.findById(id);
		validation.isTrue("confirm", code != null && !"".equals(code)).message("please enter your confirmation code");
		if (user.code == null) {
			validation.addError("confirm", "your account has already been confirmed");
		} else if (!user.code.equals(code)) {
			validation.addError("confirm", "wrong confirmation code");
		}
		if (validation.hasErrors()) {
			params.flash();
			validation.keep();
			showConfirm(user.id);
		}
		user.code = null;
		user.save();

		flash.success("sign up complete. you can now sign in");
		Main.index();
	}

	public static void forgot(String email, String code, String randomID) {
		validation.equals(code, Cache.get(randomID)).key("forgot").message("wrong captcha");
		validation.isTrue("forgot", email != null && !"".equals(email)).message("please enter your email address");
		validation.isTrue("forgot", code != null && !"".equals(code)).message("please enter the captcha code");
		validation.email("forgot", email).message("is that email correct?");

		User user = User.find("byEmail", email).first();
		validation.isTrue("forgot", user != null).message("We don't know that email. Sorry");
		if (validation.hasErrors()) {
			flash.put("showForgot", true);
			params.flash();
			validation.keep();
			Main.index();
		}
		flash.success("We sent you an email with further instructions. Thanks");

		user.resetToken = Long.toHexString(Double.doubleToLongBits(Math.random()));
		user.save();

		try {
			MailGunHTTPMailer.sendReset(user);
		} catch (EmailException e) {
			flash.error("Sorry, our Mailserver just failed.");
			Main.index();
		}

		Main.index();
	}

	public static void showReset(Long id, String token) {
		User userReset = User.findById(id);
		if (userReset == null)
			renderText("user not found");
		render(userReset, token);
	}

	public static void reset(Long id, @Required(message = "please enter your reset token") String token,
			@Required(message = "please choose a new password") String pw1,
			@Required(message = "please repeat your new password") String pw2) {
		validation.equals(pw1, pw2).message("passswords don't match");
		validation.isTrue(pw1.length() >= 5).message("minimum length for password is 5");
		User user = User.findById(id);
		if (user == null)
			renderText("user not found");
		if (user.resetToken == null)
			validation.addError("code", "you already used that code to reset your password");
		if (!user.resetToken.equals(token))
			validation.addError("token", "wrong token");
		if (validation.hasErrors()) {
			params.flash();
			validation.keep();
			showReset(user.id, token);
		}
		user.resetToken = null;
		user.password = Crypto.sign(pw1);
		user.save();

		flash.success("password successfully changed");
		Main.index();
	}
}
