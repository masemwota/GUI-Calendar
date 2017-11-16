package hw4;
import java.util.*;
import java.io.Serializable;

/**
 * Event class represents one event - has a name, date, start time, and end time
 * @author Marietta Asemwota
 */
public class Event implements Comparable<Event>, Serializable{
	private String title; 
	private GregorianCalendar date; 
	private GregorianCalendar startTime; 
	private GregorianCalendar endTime;
	
	/**
	 * Constructor to create an event without ending time 
	 * 
	 * @param aTitle - the title of the event 
	 * @param aDate - the date of the event 
	 * @param sTime - the starting time of the event
	 */
	public Event(String aTitle, GregorianCalendar aDate, GregorianCalendar sTime)
	{
		title = aTitle; 
		date = aDate;
		startTime = sTime; 
	}
	
	/**
	 * Constructor to create an event with both starting and ending time
	 * @param aTitle - the title of the event 
	 * @param aDate - the date of the event 
	 * @param sTime - the starting time of the event 
	 * @param eTime - the ending time of the event 
	 */
	public Event(String aTitle, GregorianCalendar aDate, GregorianCalendar sTime, GregorianCalendar eTime)
	{
		title = aTitle; 
		date = aDate;
		startTime = sTime; 
		endTime = eTime;
	}
	
	/**
	 * Compare your event to another event 
	 * @param other - the event to compare yours to
	 * @precondition the event must be valid
	 * @return -1, 0, or 1 based on which comes first 
	 */
	public int compareTo(Event other) {
		return this.startTime.compareTo(other.startTime);
	}
	
	/**
	 * Tells if your event comes before the other event 
	 * @param other - the other event to compare
	 * @return true if other does come before your event, otherwise returns false
	 */
	public boolean before (Event other)
	{
		//call compareTo. If this < that -> -1; if this > that -> 1
		int comp = this.compareTo(other); 
		if(comp < 0) //if comp is negative
		{
			return true; //because this comes before that 
		}
		
		else 
		{
			return false;  
		}
	}
	
	/**
	 * Find out if your event (implicit parameter) is after the other event (explicit parameter) 
	 * @param other - the event to compare yours to
	 * @return true if yours comes after, otherwise false
	 */
	public boolean after (Event other)
	{
		//call compareTo. If this < that -> -1; if this > that -> 1
		int comp = this.compareTo(other); 
		if(comp > 0) //if comp is positive
		{
			return true; //because this comes before that 
		}
		
		else 
		{
			return false;  
		}
	}
	
	/**
	 * Determine if your event (implicit param) is equal to another event (explicit param)
	 * @param o - the object to compare your event to 
	 * @precondition the object must actually be an event
	 * @return true if they are equal, false otherwise 
	 */
	public boolean equals(Object o)
	{
		Event other = (Event) o; 
		return this.startTime.equals(other.startTime); 
	}

	/**
	 * Get the title of the event 
	 * @return string - the title of the object
	 */
	public String getTitle()
	{
		return title; 
	}
	
	/**
	 * Get the date of the event
	 * @return the date as a GregorianCalendar
	 */
	public GregorianCalendar getDate()
	{
		return date;
	}
	
	/**
	 * Get the starting time of the event 
	 * @return the start time as a GregorianCalendar
	 */
	public GregorianCalendar getStartTime(){
		return startTime;
	}
	
	/**
	 * Get the ending time of the event 
	 * @return the end time as a GregorianCalendar
	 */
	public GregorianCalendar getEndTime()
	{
		return endTime; 
	}
	
	
	/**
	 * Change the title of the event 
	 * @param aTitle - the new title 
	 * @precondition 'aTitle' must be a valid string
	 * @postcondition the title of the event will be changed to 'aTitle'
	 */
	public void setTitle(String aTitle)
	{
		title = aTitle;
	}
	
	/**
	 * Change the date of the event 
	 * @param aDate - the new date 
	 * @precondition 'aDate' must be a valid GregorianCalendar 
	 * @postcondition the date of the event is changed to 'aDate'
	 */
	public void setDate(GregorianCalendar aDate)
	{
		date = aDate;
	}
	
	/**
	 * Change the starting time of the event 
	 * @param sTime - the new starting time 
	 * @precondition sTime must be a valid GregorianCalendar
	 * @postcondition the start time of the event is changed 
	 */
	public void setStartTime(GregorianCalendar sTime)
	{
		startTime = sTime; 
	}
	
	/**
	 * Change the ending time of the event 
	 * @param eTime - the new ending time 
	 * @precondition eTime must be a valid GregorianCalendar
	 * @postcondition the end time of the event is changed 
	 */
	public void setEndTime(GregorianCalendar eTime)
	{
		endTime = eTime;
	}
}