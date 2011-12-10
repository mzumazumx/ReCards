package controllers;

import models.User;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;
import controllers.securesocial.SecureSocial;

@With(SecureSocial.class)
public class Cards extends Controller {
	
	@Before
	public static void before() {
		User user = User.getCurrent();
		renderArgs.put("user", user);
	}
	
	public static void index() {
		render();
	}

}
