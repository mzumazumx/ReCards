package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import play.db.jpa.Model;
import securesocial.provider.SocialUser;
import securesocial.provider.UserId;
import securesocial.provider.UserService;
import controllers.securesocial.SecureSocial;

@Entity
public class User extends Model {

	public double rating_easy = 24;
	public double rating_medium = 4;
	public double rating_hard = 1;

	@OneToMany(mappedBy = "user")
	public List<Folder> folders;

	@OneToMany(mappedBy = "user")
	public List<StatisticsDay> last30days;

	private UserId socialUserId;

	private User() {
		folders = new ArrayList<Folder>();
		last30days = new ArrayList<StatisticsDay>();
	}

	public static User createUser() {
		User user = new User();
		user.save();
		for (int i = 0; i < 30; i++) {
			StatisticsDay day = new StatisticsDay();
			day.user = user;
			day.day = i;
			day.save();
			user.last30days.add(day);
		}
		user.save();
		return user;
	}

	public static User getCurrent() {
		if (SecureSocial.getCurrentUser() == null)
			return null;
		SocialUser su = SecureSocial.getCurrentUser();
		User found = User.find("bySocialUserId", su.id).first();
		if (found == null) {
			found = createUser();
			found.socialUserId = su.id;
			found.save();
		}
		return found;
	}

	public SocialUser getSocialUser() {
		return UserService.find(socialUserId);
	}

	public void stats_easy() {
		StatisticsDay today = StatisticsDay.find("byDayAndUser", 0, this).first();
		today.easy++;
		today.save();
	}

	public void stats_medium() {
		StatisticsDay today = StatisticsDay.find("byDayAndUser", 0, this).first();
		today.medium++;
		today.save();
	}

	public void stats_hard() {
		StatisticsDay today = StatisticsDay.find("byDayAndUser", 0, this).first();
		today.hard++;
		today.save();
	}

	public void stats_wrong() {
		StatisticsDay today = StatisticsDay.find("byDayAndUser", 0, this).first();
		today.wrong++;
		today.save();
	}

	public void stats_nextDay() {
		StatisticsDay oldDay = StatisticsDay.find("byDayAndUser", 29, this).first();
		last30days.remove(oldDay);
		oldDay.delete();
		for (StatisticsDay day : last30days) {
			day.day++;
			day.save();
		}
		StatisticsDay newDay = new StatisticsDay();
		newDay.user = this;
		newDay.save();
		this.save();
	}

	public List<Map> getStats() {
		List<Map> stats = User.find(
				"select new map(d.easy as easy, d.medium as medium, d.hard as hard, "
						+ "d.wrong as wrong) from StatisticsDay d where d.user = ? order by d.day asc", this).fetch();
		return stats;
	}

	public int stats_getMaxY() {
		try {
			return StatisticsDay.find(
					"select MAX(d.easy+d.medium+d.hard+d.wrong) from StatisticsDay d where d.user = ?", this).first();
		} catch (NullPointerException e) { // thrown when no values are in
			return 1;
		}
	}
}
