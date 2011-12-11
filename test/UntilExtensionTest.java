import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;

import play.test.UnitTest;
import ext.UntilExtension;


public class UntilExtensionTest extends UnitTest {

	@Test
	public final void textExpired() {
		Date now = new Date();
		Date expired = new Date(now.getTime()-1001*1);
		assertEquals("over due", UntilExtension.until(expired));
	}

	@Test
	public final void testUntilSeconds() {
		Date now = new Date();
		Date in1Second = new Date(now.getTime()+1001*1);
		assertEquals("1 second", UntilExtension.until(in1Second));
		Date in2Seconds = new Date(now.getTime()+1001*2);
		assertEquals("2 seconds", UntilExtension.until(in2Seconds));
	}

	@Test
	public final void testUntilMinutes() {
		Date now = new Date();
		Date in1Minute = new Date(now.getTime()+1001*60*1);
		assertEquals("1 minute", UntilExtension.until(in1Minute));
		Date in2Minutes = new Date(now.getTime()+1001*60*2);
		assertEquals("2 minutes", UntilExtension.until(in2Minutes));
	}

	@Test
	public final void testUntilHours() {
		Date now = new Date();
		Date in1Hour = new Date(now.getTime()+1001*60*60*1);
		assertEquals("1 hour", UntilExtension.until(in1Hour));
		Date in2Hours = new Date(now.getTime()+1001*60*60*2);
		assertEquals("2 hours", UntilExtension.until(in2Hours));
	}

	@Test
	public final void testUntilDays() {
		Date now = new Date();
		Date in1Day = new Date(now.getTime()+1001*60*60*24*1);
		assertEquals("1 day", UntilExtension.until(in1Day));
		Date in2Days = new Date(now.getTime()+1001*60*60*24*2);
		assertEquals("2 days", UntilExtension.until(in2Days));
	}

	@Test
	public final void testUntilMonths() {
		Calendar in1Month = GregorianCalendar.getInstance();
		in1Month.add(Calendar.DATE, 31);
		assertEquals("1 month", UntilExtension.until(in1Month.getTime()));
		Calendar in2Months = GregorianCalendar.getInstance();
		in2Months.add(Calendar.DATE, 2*31);
		assertEquals("2 months", UntilExtension.until(in2Months.getTime()));
	}

	@Test
	public final void testUntilYears() {
		Calendar in1Year = GregorianCalendar.getInstance();
		in1Year.add(Calendar.DATE, 365);
		assertEquals("1 year", UntilExtension.until(in1Year.getTime()));
		Calendar in2Years = GregorianCalendar.getInstance();
		in2Years.add(Calendar.DATE, 2*365);
		assertEquals("2 year", UntilExtension.until(in2Years.getTime()));
	}

}
