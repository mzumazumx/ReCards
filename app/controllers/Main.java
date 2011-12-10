package controllers;

import models.User;
import play.mvc.Controller;

public class Main extends Controller {
	
	public static void index() {
		User user = User.getCurrent();
		render(user);
	}

}
