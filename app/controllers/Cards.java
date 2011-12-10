package controllers;

import models.Card;
import models.Folder;
import models.Rating;
import models.User;
import play.mvc.Before;
import play.mvc.Controller;

public class Cards extends Controller {

	@Before
	public static void before() {
		User user = User.getCurrent();
		renderArgs.put("user", user);
	}

	public static void index() {
		render();
	}

	public static void create(Long id, String front, String back) {
		Folder folder = Folder.findById(id);
		Card card = Card.create(folder, front, back);
		show(card.id);
	}

	public static void show(Long id) {
		Card card = Card.findById(id);
		render(card);
	}

	public static void rate(Long id, int difficulty) {
		Card card = Card.findById(id);
		switch (difficulty) {
		case 0:
			card.schedule(Rating.EASY);
			break;
		case 1:
			card.schedule(Rating.MEDIUM);
			break;
		case 2:
			card.schedule(Rating.HARD);
			break;
		default:
			card.schedule(Rating.WRONG);
			break;
		}
	}

	public static void save(Long id, String front, String back) {
		Card card = Card.findById(id);
		card.front = front;
		card.back = back;
		card.save();
		flash.success("Changes saved successfully.");
		Cards.show(card.id);
	}

}
