package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity
public class User extends Model {

	public double rating_easy = 24;
	public double rating_medium = 4;
	public double rating_hard = 1;

	@OneToMany(mappedBy = "user")
	public List<Folder> folders;

	@OneToMany(mappedBy = "user")
	public List<StatisticsDay> last30days;

	public String email;
	public String username;
	public String password;
	
	// email-confirmation code
	public String code;
	public String resetToken;

	private User(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
		//this.code = Long.toHexString(Double.doubleToLongBits(Math.random()));
		folders = new ArrayList<Folder>();
		last30days = new ArrayList<StatisticsDay>();
	}

	public static User createUser(String username, String email, String password) {
		User user = new User(username, email, password);
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
		StatisticsDay oldDay = StatisticsDay.find("select sd from StatisticsDay sd where sd.user = ?1 order by sd.day desc", this).first();
		last30days.remove(oldDay);
		oldDay.delete();
		for (StatisticsDay day : last30days) {
			day.day++;
			day.save();
		}
		StatisticsDay newDay = new StatisticsDay();
		newDay.user = this;
		newDay.save();
		last30days.add(newDay);
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

	@Override
	public String toString() {
		return this.username;
	}
}
