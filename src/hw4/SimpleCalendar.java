package hw4;
import java.awt.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class SimpleCalendar 
{
	public static void main(String [] args)
	{
		CalendarModel calendarModel = new CalendarModel();
		CalendarComponent calendar = new CalendarComponent(calendarModel);
		//calendarModel.displayMonth();
		
		
		//String aTitle, GregorianCalendar aDate, GregorianCalendar sTime
		GregorianCalendar today = new GregorianCalendar();
		today.set(Calendar.HOUR, 19);
		Event event1 = new Event("Show", today, today);
		//calendarModel.quit();
	}
	
	//MODEL -- calendar and the events (events.txt)
	//VIEW -- day and month view
	//CONTROLLER -- month view of the calendar and buttons
}
