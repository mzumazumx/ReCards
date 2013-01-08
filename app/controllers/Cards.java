package controllers;

import models.Card;
import models.Folder;
import models.Rating;
import models.User;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

@With(Auth.class)
public class Cards extends Controller {

	@Before
	public static void before() {
		User user = Auth.getUser();
		renderArgs.put("u", user);
	}

	public static void index() {
		render();
	}
	
	public static void delete(Long id) {
		Card card = Card.findById(id);
		card.delete();
		Folders.show(card.folder.id);
	}

	public static void create(Long id, String front, String back) {
		Folder folder = Folder.findById(id);
		Card.create(folder, front, back);
		Folders.show(folder.id);
	}

	public static void show(Long id) {
		Card card = Card.findById(id);
		render(card);
	}

	public static void rate(Long id, int difficulty) {
		Card card = Card.findById(id);
		User user = Auth.getUser();
		if (!card.user.equals(user)) {
			flash.error("That card does not belong to you");
			Cards.index();
		}
		switch (difficulty) {
		case 0:
			user.rateCard(card, Rating.EASY);
			break;
		case 1:
			user.rateCard(card, Rating.MEDIUM);
			break;
		case 2:
			user.rateCard(card, Rating.HARD);
			break;
		default:
			user.rateCard(card, Rating.WRONG);
			break;
		}
	}

	public static void save(Long id, String front, String back) {
		Card card = Card.findById(id);
		if (!card.user.equals(Auth.getUser())) {
			flash.error("That card does not belong to you");
			Cards.index();
		}
		card.front = front;
		card.back = back;
		card.save();
		flash.success("Changes saved successfully.");
		Cards.show(card.id);
	}

}
