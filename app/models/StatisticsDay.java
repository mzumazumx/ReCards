package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class StatisticsDay extends Model {

	@ManyToOne
	public User user;
	
	public int easy = 0;
	public int medium = 0;
	public int hard = 0;
	public int wrong = 0;
	
	public int day = 0;

}
