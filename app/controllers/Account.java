package controllers;

import notifiers.Mailing;
import models.User;
import play.data.validation.Email;
import play.data.validation.Required;
import play.libs.Crypto;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

@With(Auth.class)
public class Account extends Controller {

	public static void saveDifficulties(double easy, double medium, double hard) {
		validation.isTrue(easy > medium && medium > hard).message("Easy > Medium > Hard");
		if (validation.hasErrors()) {
			params.flash();
			validation.keep();
			Cards.index();
		}
		User user = Auth.getUser();
		user.rating_easy = easy;
		user.rating_medium = medium;
		user.rating_hard = hard;
		user.save();
		flash.success("Settings successfully saved.");
		Cards.index();
	}
	
}
