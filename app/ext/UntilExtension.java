package ext;

import java.util.Date;

import play.templates.JavaExtensions;

public class UntilExtension extends JavaExtensions {

	public static String until(Date date) {
		Date now = new Date();
		if (now.after(date)) {
            return "over due";
        }
        long delta = (date.getTime() - now.getTime()) / 1000;
        if (delta < 60) {
            return delta+" second"+(delta!=1?"s":"");
        }
        if (delta < 60 * 60) {
            long minutes = delta / 60;
            return minutes+" minute"+(minutes!=1?"s":"");
        }
        if (delta < 24 * 60 * 60) {
            long hours = delta / (60 * 60);
            return hours+" hour"+(hours!=1?"s":"");
        }
        if (delta < 30 * 24 * 60 * 60) {
            long days = delta / (24 * 60 * 60);
            return days+" day"+(days!=1?"s":"");
        }
        if (delta < 365 * 24 * 60 * 60) {
            long months = delta / (30 * 24 * 60 * 60);
            return months+" month"+(months!=1?"s":"");
        }
        long years = delta / (365 * 24 * 60 * 60);
        return years+" year"+(years!=1?"s":"");
	}
}
