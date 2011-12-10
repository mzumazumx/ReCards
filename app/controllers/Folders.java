package controllers;

import models.Card;
import models.Folder;
import models.User;
import play.mvc.Before;
import play.mvc.Controller;

public class Folders extends Controller {
	
	@Before
	public static void before() {
		User user = User.getCurrent();
		renderArgs.put("user", user);
	}
	
	public static void create(String name) {
		Folder.create(User.getCurrent(), name);
		Cards.index();
	}
	
	public static void show(Long id) {
		Folder folder = Folder.findById(id);
		int due = Card.dueCount(folder);
		render(due, folder);
	}
	
	public static void review(Long id) {
		Folder folder = Folder.findById(id);
		Card card = Card.due(folder);
		render(card, folder);
	}
	
}