package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;

import play.data.validation.Email;
import play.data.validation.Password;
import play.db.jpa.Model;

@Entity
public class User extends Model {

	public double rating_easy = 4;
	public double rating_medium = 2;
	public double rating_hard = 1;

	@Email
	public String email;

	@Password
	public String password;

	@OneToMany(mappedBy = "user")
	public List<Folder> folders;

	private User() {
		folders = new ArrayList<Folder>();
	}

	public static User create(String email, String password) {
		User user = new User();
		user.email = email;
		user.password = password;
		user.save();
		return user;
	}

}
