package notifiers;

import java.util.HashMap;
import java.util.Map;

import models.User;
import play.Logger;
import play.mvc.Mailer;
import play.mvc.Router;

public class Mailing extends Mailer {

	public static boolean sendConfirm(User user) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", user.id);
		map.put("code", user.code);
		String link = Router.getFullUrl("Auth.confirm", map);
		
		setSubject("reCards > Sign Up");
		setFrom("recards@mzumazumx.de");
		addRecipient(user.email);
		send(user, link);
		Logger.info("sent registration-email to " + user);
		return true;
	}

	public static boolean sendReset(User user) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", user.id);
		map.put("token", user.resetToken);
		String link = Router.getFullUrl("Auth.showReset", map);
		
		setSubject("reCards > reset password");
		setFrom("recards@mzumazumx.de");
		addRecipient(user.email);
		send(user, link);
		Logger.info("sent reset-email to " + user);
		return true;
	}

}
