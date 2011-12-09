package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity
public class Folder extends Model{
	
	public String name;
	
	@OneToMany(mappedBy="folder")
	public List<Card> cards;
	
	@ManyToOne
	public User user;
	
	private Folder() {
		this.cards = new ArrayList<Card>();
	}
	
	public static Folder create(User user, String name) {
		Folder folder = new Folder();
		folder.name = name;
		folder.user = user;
		folder.save();
		user.folders.add(folder);
		user.save();
		return folder;
	}

}
