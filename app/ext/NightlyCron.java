package ext;

import java.util.List;

import models.User;
import play.jobs.Job;
import play.jobs.On;

@On("0 0 0 * * ?")
public class NightlyCron extends Job {
	
	public void doJob() {
        	List<User> allUsers = User.findAll();
        	for (User user : allUsers) {
			user.stats_nextDay();
		}
	}

}
