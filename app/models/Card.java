package models;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class Card extends Model {

	@ManyToOne
	public Folder folder;

	@ManyToOne
	public User user;

	public String front;
	public String back;

	public Date due;

	private Card() {
	}

	public boolean due() {
		return !due.after(new Date());
	}

	public static Card create(Folder folder, String front, String back) {
		Card card = new Card();
		card.folder = folder;
		card.user = folder.user;
		card.front = front;
		card.back = back;
		card.due = new Date();
		card.save();
		return card;
	}

	public static Card due(User user) {
		return Card.find("user = ?1 and due < ?2", user, new Date()).first();
	}

	public static List<Card> dueAll(User user) {
		return Card.find("user = ?1 and due < ?2", user, new Date()).fetch();
	}

	public static Card due(Folder folder) {
		return Card.find("folder = ?1 and due < ?2", folder, new Date()).first();
	}

	public static List<Card> dueAll(Folder folder) {
		return Card.find("folder = ?1 and due < ?2", folder, new Date()).fetch();
	}

	public static int dueCount(Folder folder) {
		return (int) Card.count("folder = ?1 and due < ?2", folder, new Date());
	}

	public void rateCard(Rating r) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		double addition = 0;
		if (r.equals(Rating.EASY)) {
			addition = user.rating_easy;
		} else if (r.equals(Rating.MEDIUM)) {
			addition = user.rating_medium;
		} else if (r.equals(Rating.HARD)) {
			addition = user.rating_hard;
		}
		cal.add(Calendar.MINUTE, (int) Math.round(61 * addition));
		this.due = cal.getTime();
		this.save();
	}

}
