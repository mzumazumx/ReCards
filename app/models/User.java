package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import controllers.securesocial.SecureSocial;

import play.data.validation.Email;
import play.data.validation.Password;
import play.data.validation.Required;
import play.db.jpa.Model;
import securesocial.provider.SocialUser;
import securesocial.provider.UserId;
import securesocial.provider.UserService;

@Entity
public class User extends Model {

	public double rating_easy = 4;
	public double rating_medium = 2;
	public double rating_hard = 1;

	@OneToMany(mappedBy = "user")
	public List<Folder> folders;

	private UserId socialUserId;

	private User() {
		folders = new ArrayList<Folder>();
	}

	public static User getCurrent() {
		User found = User.findById(1L);
		if (found == null)
			found = new User().save();
		return found;
		// if (SecureSocial.getCurrentUser() == null)
		// return null;
		// SocialUser su = SecureSocial.getCurrentUser();
		// User found = User.find("bySocialUserId", su.id).first();
		// if (found == null) {
		// found = new User().save();
		// found.socialUserId = su.id;
		// }
		// return found;
	}

	public SocialUser getSocialUser() {
		return UserService.find(socialUserId);
	}

}
