package hw4;
import java.awt.*;

public class SimpleCalendar 
{
	public static void main(String [] args)
	{
		CalendarModel calendarModel = new CalendarModel();
		CalendarComponent calendar = new CalendarComponent(calendarModel);
		//calendarModel.displayMonth();
	}
	
	//MODEL -- calendar and the events (events.txt)
	//VIEW -- day and month view
	//CONTROLLER -- month view of the calendar and buttons
}
