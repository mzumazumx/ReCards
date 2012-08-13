package controllers;

import models.User;
import play.cache.Cache;
import play.libs.Codec;
import play.libs.Images;
import play.mvc.Controller;

public class Main extends Controller {
	
	public static void index() {
		User user = Auth.getUser();
		String randomID = Codec.UUID();
		render(user, randomID);
	}
	
	public static void captcha(String id) {
		Images.Captcha captcha = Images.captcha();
		String code = captcha.getText("#ff6c00");
		Cache.set(id, code, "10mn");
		renderBinary(captcha);
	}

}
