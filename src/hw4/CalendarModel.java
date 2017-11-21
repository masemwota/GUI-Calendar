package hw4;
import java.util.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
/**
 * Model for the Calendar
 * Holds the calendar as well as the events
 */

enum MONTHS
{
	January, February, March, April, May, June, July, August, September, October, November, December;
}
enum SHORTMONTHS
{
	Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec;
}
enum DAYS
{
	Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday ;
}
enum SHORTDAYS 
{
	Su, Mo, Tu, We, Th, Fr, Sa;
}


/**
 * This class contains MyCalendar which holds days as well as events
 */
public class CalendarModel {
	final static MONTHS[] arrayOfMonths = MONTHS.values();
	final static DAYS[] arrayOfDays = DAYS.values();
	final static SHORTDAYS[] arrayShortDays = SHORTDAYS.values();
	final static SHORTMONTHS[] arrayShortMonths = SHORTMONTHS.values();
	final static String [] tinyDays = {"S", "M", "T", "W", "T", "F", "S"};
	
	private GregorianCalendar myCal;
	private TreeMap<GregorianCalendar, TreeSet<Event>> myMap; 
	private ArrayList<ChangeListener> listeners; 
	
	
	/**
	 * Constructor for MyCalendar - creates a calendar set to today
	 */
	public CalendarModel()
	{
		myCal = new GregorianCalendar(); //calendar set to today
		myMap = new TreeMap<>(); //creates an empty tree map
		listeners = new ArrayList<>();
		
		//sample date
		//myCal.set(Calendar.MONTH, 2);
		//myCal.set(Calendar.YEAR, 2014);
	}

	/**
	 * Accessor method to get private instance variable
	 * @return the tree map of events
	 */
	public TreeMap<GregorianCalendar, TreeSet<Event>> getEvents()
	{
		return myMap;
	}
	
	/**
	 * Add a change listener to the list of change listeners
	 * @param l - the change listener to add
	 */
	public void addChangeListener(ChangeListener l)
	{
		listeners.add(l);
	}
	
	/**
	 * Check if this is the target day
	 * @param target - the calendar to check for
	 * @param cal - the calendar to verify
	 * @return true if the calendar matched the target; false otherwise
	 */
	public boolean isTheDay(GregorianCalendar target, GregorianCalendar cal)
	{
		if(target.get(Calendar.YEAR) == cal.get(Calendar.YEAR))
		{
			if(target.get(Calendar.MONTH) == cal.get(Calendar.MONTH))
			{
				if(target.get(Calendar.DAY_OF_MONTH) == cal.get(Calendar.DAY_OF_MONTH))
				{
						return true; 
				}
			}
		}
		return false; 
	}
	
	/**
	 * A private method to check if there is at least one event in the TreeMap for the provided calendar
	 * @param cal - the calendar to look for the in the map
	 * @return true if there is at least one event, otherwise return false
	 */
	public boolean eventExists(GregorianCalendar cal)
	{
		// create a calendar based on same date but default time of 0 for easy comparison
		GregorianCalendar dCal = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
	
		boolean exist = false; // if day already exists in the map - meaning events exist

		for (GregorianCalendar tempCal : myMap.keySet()) 
		{
			// if date already exits = already at least one event
			if (tempCal.getTime().equals(dCal.getTime())) 	
			{
				exist = true;
			}
		}
		return exist; 
	}
	
	/**
	 * Loads events.txt to populate the calendar
	 * If there is no such file because it's the first run, the user is notified
	 */
	public void load()
	{
		try 
		{
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("events.txt"));
			TreeMap<GregorianCalendar, TreeSet<Event>> oldMap = (TreeMap<GregorianCalendar, TreeSet<Event>>) in.readObject();
			in.close(); 
			myMap = oldMap;
			System.out.println("Events loaded!");
		}
		catch(FileNotFoundException x)
		{
			System.out.println("File error: " + x.getMessage());
			System.out.println("This is the first run of the program");
		}
		catch(IOException x)
		{
			System.out.println("IO Exception: " + x.getMessage());
			System.out.println("This is the first run of the program");
		}
		catch(ClassNotFoundException x)
		{
			System.out.println("Class Exception: " + x.getMessage());
			System.out.println("This is the first run of the program");
		}
	}
	
	
	/**
	 * Get the data to describe a day
	 * @param cal - the day to describe
	 * @return day represented as day of week, day, and month
	 */
	public String getDayDescription(GregorianCalendar cal)
	{
		String day = "";
		day += arrayOfDays[cal.get(Calendar.DAY_OF_WEEK)-1];
		day += " "; 
		day += cal.get(Calendar.MONTH)+1;
		day += "/"; 
		day += cal.get(Calendar.DAY_OF_MONTH);

		return day;
	}
	
	
	/**
	 * Get the date of the given calendar
	 * @param cal - the day to describe
	 * @return traditional date format M/D/Y
	 */
	public String getDate(GregorianCalendar cal)
	{
		String day = "";
		day += cal.get(Calendar.MONTH)+1;
		day += "/"; 
		day += cal.get(Calendar.DAY_OF_MONTH);
		day += "/"; 
		day += cal.get(Calendar.YEAR);

		return day;
	}
		
	
	/**
	 * Create an event using the provided strings 
	 * @param titleString - the title of the event
	 * @param date - the date of the event 
	 * @param startString - the start time 
	 * @param endString - the end time 
	 * @return true if the event was successfully created; false otherwise
	 */
	public boolean createWithStrings(String titleString, String date, String startString, String endString)
	{
		String title = titleString; 
		
		String monthString = date.substring(0, 2); 
		String firstDigit = monthString.substring(0, 1); //first digit of month - either 0 or 1
		String secondDigit = monthString.substring(1, 2); //second digit of month - 0 to 9
		
		int month; //to hold the integer value of the month
		
		if(firstDigit.equals("0"))
		{
			//if it is less than 10
			month = Integer.parseInt(secondDigit)-1; 
		}
		else 
		{
			month = Integer.parseInt(monthString)-1;
		}
			
		String dayString = date.substring(3, 5); 
		int day = Integer.parseInt(dayString); 
		
		String yearString = date.substring(6, date.length());
		int year = Integer.parseInt(yearString);
		
		//starting time
		String sHourString = startString.substring(0, 2); 
		int sHour = Integer.parseInt(sHourString);
		
		String sMinString = startString.substring(3, 5); 
		int sMinute = Integer.parseInt(sMinString);
		
		
		GregorianCalendar dateCal = new GregorianCalendar(year, month, day); //to represent date
		GregorianCalendar startTimeCal = new GregorianCalendar(year, month, day, sHour, sMinute); //start time calendar
		
		
		String speriod = startString.substring(5, startString.length());
		if(speriod.equalsIgnoreCase("AM"))
		{
			startTimeCal.set(Calendar.AM_PM, Calendar.AM);
		}
		
		else
		{
			startTimeCal.set(Calendar.AM_PM, Calendar.PM);
		}
		
		
		//ending time
		GregorianCalendar endTimeCal;
		
		String eHourString = endString.substring(0, 2); 
		int eHour = Integer.parseInt(eHourString);

		String eMinString = endString.substring(3, 5); 
		int eMinute = Integer.parseInt(eMinString);

		endTimeCal = new GregorianCalendar(year, month, day, eHour, eMinute); //end time calendar
	
		String eperiod = endString.substring(5, startString.length());
		if(eperiod.equalsIgnoreCase("AM"))
		{
			endTimeCal.set(Calendar.AM_PM, Calendar.AM);
		}
		
		else
		{
			endTimeCal.set(Calendar.AM_PM, Calendar.PM);
		}
		
		Event myEvent = new Event(title, dateCal, startTimeCal, endTimeCal);
		
		//check if this event conflicts with any other events 
		boolean exist = false; //if day already exists in the map
		
		for (GregorianCalendar tempCal : myMap.keySet())
		{
			//for each calendar that is in the set of keys, search for the date you want to add
			if(tempCal.equals(dateCal)) //if date already exits = already at least one event
			{
				exist = true; 
				TreeSet<Event> myTree = myMap.get(dateCal); 
				
				for(Event e : myTree)
				{
					if(e.equals(myEvent)) 
					{
						System.out.println("Event already exists - not created"); 
						return false;
					}
					
					//(StartA <= EndB)  and  (EndA >= StartB)
					if((myEvent.getStartTime().before(e.getEndTime())) && (myEvent.getEndTime().after(e.getStartTime())))
					{
						System.out.println("There is an event conflict. Event not created."); 
						return false;
					}
					
					if(myEvent.getStartTime().after(myEvent.getEndTime())) //if start time is after end time
					{
						System.out.println("Invalid end time in relation to start time. Event not created."); 
						return false;
					}
				}
				
				myTree.add(myEvent); //add event to existing list 
				myMap.put(dateCal, myTree); //replace the set in the map
				System.out.println("Event Added!\n");
				
				//after event is created, notify the listeners 
				for(ChangeListener l : listeners)
				{
					ChangeEvent e = new ChangeEvent(this);
					l.stateChanged(e);
				}
				
				return true;
			}
		}
		
		if(exist == false)
		{
			//calendar doesn't already exist in map
			//put the key and value in the map
			TreeSet<Event> myTree = new TreeSet<>(); 
			
			myTree.add(myEvent); 
			myMap.put(dateCal, myTree); 
			System.out.println("Event Added!\n");
			
			//after event is created, notify the listeners 
			for(ChangeListener l : listeners)
			{
				ChangeEvent e = new ChangeEvent(this);
				l.stateChanged(e);
			}
			return true;
		}
		
		return true;
	}
	
	
	/**
	 * Quit method saves the TreeMap to a text file - events.txt
	 */
	public void quit()
	{
		try
		{
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("events.txt"));
			out.writeObject(myMap);
			out.close(); 
			System.out.println("File saved!");
		}
		
		catch (IOException x)
		{
			System.out.println("Error: " + x.getMessage());
		}
	}
	
	public GregorianCalendar getCalendar()
	{
		return myCal;
	}
	
}