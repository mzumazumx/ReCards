package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import play.data.validation.Email;
import play.data.validation.Password;
import play.data.validation.Required;
import play.db.jpa.Model;
import securesocial.provider.SocialUser;
import securesocial.provider.UserId;

@Entity
public class CopyOfUser extends Model {

	public double rating_easy = 4;
	public double rating_medium = 2;
	public double rating_hard = 1;

	@Required
	@Email
	public String email;

	@Required
	@Password
	public String password;

	@OneToMany(mappedBy = "user")
	public List<Folder> folders;
	
	public String resetToken;
	public String code;

	private CopyOfUser() {
		folders = new ArrayList<Folder>();
	}

	public static CopyOfUser create(String email, String password) {
		CopyOfUser user = new CopyOfUser();
		user.email = email;
		user.password = password;
		user.save();
		return user;
	}

}
