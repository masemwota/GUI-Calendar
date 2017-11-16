package hw4;
import java.util.*;
import java.io.*;
import javax.swing.*;
/**
 * HW 1 Solution 
 * My First Calendar 
 * @author Marietta Asemwota
 * @Date 09/17/2017 
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
	
	
	/**
	 * Constructor for MyCalendar - creates a calendar set to today
	 */
	public CalendarModel()
	{
		myCal = new GregorianCalendar(); //calendar set to today
		myMap = new TreeMap<>(); //creates an empty tree map
		//myCal.set(Calendar.YEAR, 2014);
		//myCal.set(Calendar.MONTH, 2);
	}

	
	/* Private method
	 * Checks if the calendar represents today 
	 * @param cal - the calendar to be compared 
	 * @return true if cal is today, otherwise false
	 */
	private boolean isToday(Calendar cal)
	{
		if(myCal.get(Calendar.YEAR) == cal.get(Calendar.YEAR))
		{
			if(myCal.get(Calendar.MONTH) == cal.get(Calendar.MONTH))
			{
				if(myCal.get(Calendar.DAY_OF_MONTH) == cal.get(Calendar.DAY_OF_MONTH))
				{
						return true; 
				}
			}
		}
		return false; 
	}
	
	
	/**
	 * Check if this is the target day
	 * @param target
	 * @param cal
	 * @return
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
	
	/*
	 * A private method to check if there is at least one event in the TreeMap for the provided calendar
	 * @param cal - the calendar to look for the in the map
	 * @return true if there is at least one event, otherwise return false
	 */
	private boolean eventExists(GregorianCalendar cal)
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
	 * Allows user to view calendar either by month or day
	 */
	public void viewBy()
	{
		GregorianCalendar cal = new GregorianCalendar();
		System.out.println("[D]ay view or [M]onth view ?");
		
		Scanner sc = new Scanner(System.in); 
		String view = "";
		 
		if(sc.hasNextLine())
		{
			view = sc.nextLine();
			
			if(view.equalsIgnoreCase("D"))
				displayDayView(myCal); 
			
			else if (view.equalsIgnoreCase("M"))
				displayMonthEvents(myCal);
		}
		
		System.out.println(" ");
		System.out.println("[P]revious or [N]ext or [M]ain menu ?");
		
		while(sc.hasNextLine())
		{	
			String action = sc.nextLine(); 
			
			if(view.equalsIgnoreCase("D"))
			{
				if (action.equalsIgnoreCase("P"))
				{
					//call day view with previous day
					cal.add(Calendar.DAY_OF_MONTH, -1);
					displayDayView(cal);
				}
				
				else if(action.equalsIgnoreCase("N"))
				{
					//call day view with next day 
					cal.add(Calendar.DAY_OF_MONTH, 1);
					displayDayView(cal);
				}
				
				else 
				{
					return;
				}
			}
			
			else if(view.equalsIgnoreCase("M"))
			{
				if (action.equalsIgnoreCase("P"))
				{
					//call month view with previous month
					cal.add(Calendar.MONTH, -1);
					this.displayMonthEvents(cal);
				}
				
				else if(action.equalsIgnoreCase("N"))
				{
					//call month view with next month
					cal.add(Calendar.MONTH, 1);
					this.displayMonthEvents(cal);
				}
				
				else if (action.equalsIgnoreCase("M"))
				{
					System.out.println("Display main menu");
					return;
				}
				
				else
				{
					System.out.println("Invalid key - please try again");
				}
			}
			
			//prompt user for previous, next, or leave 
			System.out.println(" ");
			System.out.println("[P]revious or [N]ext or [M]ain menu?");
		}	
	}
	
	
	public String getDayDescription(GregorianCalendar cal)
	{
		String day = "";
		day += arrayOfDays[cal.get(Calendar.DAY_OF_WEEK)-1];
		day += " "; 
		//day += arrayShortMonths[cal.get(Calendar.MONTH)];
		day += cal.get(Calendar.MONTH)+1;
		day += "/"; 
		day += cal.get(Calendar.DAY_OF_MONTH);
		//day += "";
		//day += cal.get(Calendar.YEAR);
		
		return day;
	}
	
	/**
	 * Display the day view for the given day
	 * @param cal - the calendar to display the day view for
	 */
	public void displayDayView(GregorianCalendar cal)
	{
		String day = "";
		day += arrayOfDays[cal.get(Calendar.DAY_OF_WEEK)-1];
		day += ", "; 
		day += arrayShortMonths[cal.get(Calendar.MONTH)];
		day += " "; 
		day += cal.get(Calendar.DAY_OF_MONTH);
		day += ", ";
		day += cal.get(Calendar.YEAR);
		
		System.out.println(day);
		
		//check if there are any events for this day and display them
		//create a calendar based on same date but default time of 0 for easy comparison
		cal = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		boolean exists = this.eventExists(cal);
		
		if(exists)
		{
			TreeSet<Event> myTree = myMap.get(cal); //pull down the treeSet
			for(Event e: myTree) 
			{
				System.out.print(e.getTitle() + " "); 
				System.out.print(e.getStartTime().get(Calendar.HOUR_OF_DAY) + ":");
				int sminute = e.getStartTime().get(Calendar.MINUTE); 
				if (sminute == 0)
				{
					System.out.print("00");
				}
				
				else 
				{
					System.out.print(e.getStartTime().get(Calendar.MINUTE) + "");
				}
				
				if((e.getEndTime() != null) && !(e.getEndTime().equals(e.getStartTime())))
				{
					System.out.print(" - ");
					System.out.print(e.getEndTime().get(Calendar.HOUR_OF_DAY) + ":");
					int eminute = e.getEndTime().get(Calendar.MINUTE); 
					if (eminute == 0)
					{
						System.out.print("00");
					}
					
					else 
					{
						System.out.print(e.getEndTime().get(Calendar.MINUTE) +"");
					}
				}
				System.out.println(" ");
			}
			System.out.println(" ");
		}
	}
	
	
	/**
	 * Display the calendar for the current month which is an instance variable
	 */
	public String displayMonth(GregorianCalendar cal)
	{
		String descriptor = ""; //holds the string for the descriptor which has month and year
		descriptor += arrayOfMonths[cal.get(GregorianCalendar.MONTH)]; 
		descriptor += " ";
		descriptor += cal.get(GregorianCalendar.YEAR); //year of this view
		descriptor += "\n";
		
		for(int i = 0; i < tinyDays.length; i++)
		{
			descriptor += tinyDays[i] + "   "; //the days of the week
		}
		
		//System.out.print(descriptor); 

		//create a temporary calendar set the the first day of this month 
		GregorianCalendar temp = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1);
		int firstDay = temp.get(Calendar.DAY_OF_WEEK); //set to the first day (Sunday, Monday, etc) of the month
		int maxDays = temp.getActualMaximum(Calendar.DAY_OF_MONTH); //get the amount of days in this month
		
		String monthView = "";
		for(int j = 1; j < maxDays+firstDay; j++)
		{
			//set the calendar to a different day as it goes along
			temp.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), j - firstDay+1); 
			
			//if there is a new week, enter a new line
			if (j % 7 == 1) 
			{
				//System.out.print("\n");
				monthView += "\n";
			}
			
			if(j < firstDay) //before the days start printing
			{
				//System.out.print("   ");
				monthView += "     ";
			}
			
			else //when the days have started
			{
				if(isTheDay(cal, temp))
				{
					//System.out.print("["); 
					//System.out.print(temp.get(Calendar.DAY_OF_MONTH));
					//System.out.print("]");
					
					monthView += "["; 
					monthView += temp.get(Calendar.DAY_OF_MONTH);
					monthView += "] ";
				}
				
				else //print normally for other days
				{
					if ((temp.get(Calendar.DAY_OF_MONTH) >= 1) && (temp.get(Calendar.DAY_OF_MONTH) <= 9))
					{
						//System.out.print(" ");
						if (j % 7 != 1)
							monthView += "  ";
					}
					
					//System.out.print(temp.get(Calendar.DAY_OF_MONTH));
					//System.out.print(" ");
					
					monthView += temp.get(Calendar.DAY_OF_MONTH); 
					monthView += " ";
				}
			}		
		}
		
		return descriptor + monthView;
	}
	
	
	/**
	 * Display the month calendar which indications of events
	 * Displays brackets on days where there are events 
	 * @param cal - the calendar for which to display the month
	 */
	public void displayMonthEvents(GregorianCalendar cal)
	{
		String descriptor = ""; //holds the string for the descriptor which has month and year
		descriptor += arrayOfMonths[cal.get(GregorianCalendar.MONTH)]; //month of this view
		descriptor += " ";
		descriptor += cal.get(GregorianCalendar.YEAR); //year of this view
		descriptor += "\n";
		
		for(int i = 0; i < arrayShortDays.length; i++)
		{
			descriptor += arrayShortDays[i] + " "; //the days of the week
		}
		
		//System.out.print(descriptor); 

		//create a temporary calendar set the the first day of this month 
		GregorianCalendar temp = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1);
		int firstDay = temp.get(Calendar.DAY_OF_WEEK); //set to the first day (Sunday, Monday, etc) of the month
		int maxDays = temp.getActualMaximum(Calendar.DAY_OF_MONTH); //get the amount of days in this month
		
		String monthView = "";
		for(int j = 1; j < maxDays+firstDay; j++)
		{
			//set the calendar to a different day as it goes along
			temp.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), j - firstDay+1); 
			
			//if there is a new week, enter a new line
			if ((j % 7 == 1)) 
			{
				//System.out.println(" ");
				monthView += " ";
			}
			
			if(j < firstDay) //before the days start printing
			{
				//System.out.print("   ");
				monthView += "   ";
			}
			
			else //when the days have started
			{
				if(this.eventExists(temp)) //if there is an event that day
				{
					//System.out.print("{"); 
					//System.out.print(temp.get(Calendar.DAY_OF_MONTH));
					//System.out.print("}");
					
					monthView += "{"; 
					monthView += temp.get(Calendar.DAY_OF_MONTH);
					monthView += "}";
				}
				
				else //print normally for other days
				{
					if ((temp.get(Calendar.DAY_OF_MONTH) > 1) && (temp.get(Calendar.DAY_OF_MONTH) <= 9))
					{
						//System.out.print(" ");
						monthView += " ";
					}
					
					//System.out.print(temp.get(Calendar.DAY_OF_MONTH));
					//System.out.print(" ");
					
					monthView += temp.get(Calendar.DAY_OF_MONTH);
					monthView += " ";
				}
			}		
		}
	}
	
	
	/**
	 * Create a new event to add to the calendar
	 */
	public void create()
	{
		Scanner scan = new Scanner(System.in); 
		
		System.out.println("Title: ");
		String title = scan.nextLine(); 
		
		System.out.println("Date (MM/DD/YYYY): ");
		//Example: MM/DD/YYYY -- 09/12/2017
		String date = scan.nextLine(); 
		
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
		
		
		System.out.println("Starting time in 24 hour format(HH:MM): "); 
		//sample: 15:30 for 3:30pm
		String sTimeString = scan.nextLine();
		String sHourString = sTimeString.substring(0, 2); 
		int sHour = Integer.parseInt(sHourString);
		
		String sMinString = sTimeString.substring(3, 5); 
		int sMinute = Integer.parseInt(sMinString);
		
		
		GregorianCalendar dateCal = new GregorianCalendar(year, month, day); //to represent date
		GregorianCalendar startTimeCal = new GregorianCalendar(year, month, day, sHour, sMinute); //start time calendar
		
		System.out.println("End time (in 24 hour format - HH:MM) (\"N\" if N/A): ");
		//sample: 15:30 for 3:30pm
		String eTimeString = scan.nextLine(); 
		GregorianCalendar endTimeCal;
		
		if(eTimeString.equalsIgnoreCase("N"))
		{
			//end time calendar is set to the start time if not specified
			endTimeCal = startTimeCal; 
		}
		else 
		{
			String eHourString = eTimeString.substring(0, 2); 
			int eHour = Integer.parseInt(eHourString);
			
			String eMinString = eTimeString.substring(3, 5); 
			int eMinute = Integer.parseInt(eMinString);
			
			endTimeCal = new GregorianCalendar(year, month, day, eHour, eMinute); //end time calendar
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
						return;
					}
					
					//(StartA <= EndB)  and  (EndA >= StartB)
					if((myEvent.getStartTime().before(e.getEndTime())) && (myEvent.getEndTime().after(e.getStartTime())))
					{
						System.out.println("There is an event conflict. Event not created."); 
						return;
					}
					
					if(myEvent.getStartTime().after(myEvent.getEndTime())) //if start time is after end time
					{
						System.out.println("Invalid end time in relation to start time. Event not created."); 
						return;
					}
				}
				myTree.add(myEvent); //add event to existing list 
				myMap.put(dateCal, myTree); //replace the set in the map
				System.out.println("Event Added!\n");
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
		}
	}
	
	
	/*
	 * Take a string of a date and turn it into a calendar 
	 * @param date - the string representation to interpret
	 * @precondition date must be the valid format for a date
	 * @return a GregorianCalendar which is the correct date
	 */
	private GregorianCalendar parseStringtoCal(String date)
	{
		String monthString = date.substring(0, 2); 
		String firstDigit = monthString.substring(0, 1); //first digit of month - either 0 or 1
		String secondDigit = monthString.substring(1, 2); //second digit of month - 0 to 9
		
		int month; //to hold the integer value of the month
		
		if(firstDigit.equals("0"))
		{
			//if it is less than 10
			month = Integer.parseInt(secondDigit); 
		}
		else 
		{
			month = Integer.parseInt(monthString);
		}
			
		String dayString = date.substring(3, 5); 
		int day = Integer.parseInt(dayString); 
		
		String yearString = date.substring(6, date.length());
		int year = Integer.parseInt(yearString);
		
		GregorianCalendar dateCal = new GregorianCalendar(year, month-1, day); //to represent date
		
		return dateCal; 
	}
	
	/**
	 * Method to take the user to a specified date 
	 */
	public void goTo()
	{
		System.out.println("Enter a date in (MM/DD/YYYY) format: "); 
		
		//Example: MM/DD/YYYY -- 09/12/2017
		Scanner in = new Scanner(System.in); 
		String date = in.nextLine(); 
		
		GregorianCalendar dateCal = this.parseStringtoCal(date);
		this.displayDayView(dateCal);
	}
	

	/**
	 * Displays all of the scheduled events in the Calendar. 
	 */
	public void eventList()
	{	
		for (GregorianCalendar tempCal : myMap.keySet())
		{
			this.displayDayView(tempCal);
			System.out.println(" ");
		}
	}
	
	/**
	 * Method for user to delete an event from the calendar
	 * Two ways to delete an event: selected, all
	 * [S] = all the events for selected date are deleted 
	 * [A] = all the events on this calendar will be deleted
	 */
	public void delete()
	{
		Scanner sc = new Scanner(System.in); 
		String method = ""; //the method by which to delete
		
		System.out.println("Delete events for a: [S]elected date or delete [A]ll events");
		if(sc.hasNextLine())
		{
			method = sc.nextLine();
			
			if(method.equalsIgnoreCase("S"))
			{
				System.out.println("Enter a date in (MM/DD/YYYY) format: ");
				String delDate = sc.nextLine();
				GregorianCalendar delCal = this.parseStringtoCal(delDate); 
				myMap.remove(delCal);
				System.out.println("Events deleted for that date");
				this.quit(); //to save the changes to the file
			}
			
			else if (method.equalsIgnoreCase("A"))
			{
				deleteAll(); 
				System.out.println("All events deleted");
			}
		}
	}
	
	
	/**
	 * Method to delete all the events in the calendar - clears both TreeMap and events.txt
	 */
	public void deleteAll()
	{
		//to delete all the events, clear the TreeMap of everything and then clear events.txt
		myMap = null; //so garbageCollector collects the TreeMap with all the events
		myMap = new TreeMap<GregorianCalendar, TreeSet<Event>>(); 
		
		//run the method to save the blank map to events.txt so that it's empty 
		this.quit(); 
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