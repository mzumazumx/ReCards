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
			card.schedule(Rating.EASY);
			user.stats_easy();
			break;
		case 1:
			card.schedule(Rating.MEDIUM);
			user.stats_medium();
			break;
		case 2:
			card.schedule(Rating.HARD);
			user.stats_hard();
			break;
		default:
			card.schedule(Rating.WRONG);
			user.stats_wrong();
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
