package controllers;

import java.util.Date;

import models.Card;
import models.Folder;
import models.User;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

@With(Auth.class)
public class Folders extends Controller {
	
	@Before
	public static void before() {
		User user = Auth.getUser();
		renderArgs.put("u", user);
	}
	
	public static void create(String name) {
		Folder.create(Auth.getUser(), name);
		Cards.index();
	}
	
	public static void show(Long id) {
		Folder folder = Folder.findById(id);
		if (!folder.user.equals(Auth.getUser())) {
			flash.error("That folder does not belong to you");
			Cards.index();
		}
		int due = Card.dueCount(folder);
		render(due, folder);
	}
	
	public static void review(Long id) {
		Folder folder = Folder.findById(id);
		if (!folder.user.equals(Auth.getUser())) {
			flash.error("That folder does not belong to you");
			Cards.index();
		}
		Card card = Card.due(folder);
		render(card, folder);
	}
	
	public static void reschedule(Long id) {
		Folder folder =  Folder.findById(id);
		if (!folder.user.equals(Auth.getUser())) {
			flash.error("That folder does not belong to you");
			Cards.index();
		}
		for (Card card : folder.getCards()) {
			card.due = new Date();
			card.save();
		}
		show(folder.id);
	}
}