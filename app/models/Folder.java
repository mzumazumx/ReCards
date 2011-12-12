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
	
	@ManyToOne
	public User user;
	
	private Folder() {
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
	
	public List<Card> getCards() {
		return Card.find("folder = ? order by due asc", this).fetch();
	}

}
