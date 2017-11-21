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
		calendarModel.addChangeListener(calendar);
	}
	
	//MODEL -- calendar and the events in hashmap (events.txt)
	//VIEW -- day and month view
	//CONTROLLER -- month view of the calendar and buttons
}
