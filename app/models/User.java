package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity
public class User extends Model {

	public double rating_easy = 4;
	public double rating_medium = 2;
	public double rating_hard = 1;
	
	public String email;
	
	@OneToMany(mappedBy="user")
	public List<Folder> folders;
	
	private User() {
		folders = new ArrayList<Folder>();
	}

	public static User create(String email) {
		User user = new User();
		user.email = email;
		user.save();
		return user;
	}

}
