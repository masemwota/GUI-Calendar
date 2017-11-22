package hw4;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.*;

import hw4.Event;


/**
 * View and Controller class of pattern 
 * Calendar Component holds both month view and day view 
 * @author Marietta Asemwota
 *
 */
public class CalendarComponent extends Component implements ChangeListener
{
	private CalendarModel calModel;
	private GregorianCalendar myCal;
	
	//hold calendar component
	private JFrame frame;
	
	private JPanel dayViewPanel; 
	private JTextArea dateText;
	private JTextArea dayEvents;
	
	private JTextArea monthTextArea;
	private JPanel wholeMonthPanel;
	
	private ArrayList<JTextArea> days;
	
	
	/**
	 * Constructor takes a model class
	 * @param calModel - a calendar model class
	 */
	public CalendarComponent(CalendarModel calModel)
	{
		this.calModel = calModel;
		myCal = calModel.getCalendar();
		calModel.load();
	
		days = new ArrayList<>();
		
		frame = new JFrame("Simple Calendar"); 
		frame.setSize(400, 200);
		frame.setLayout(new BorderLayout());
		
		dateText = new JTextArea(); 
		dayEvents = new JTextArea();
		dayViewPanel = getDayView();
		
		wholeMonthPanel = getMonthView();
		
		JPanel buttonsPanel = getButtons(); 
		
		frame.add(dayViewPanel, BorderLayout.CENTER);
		frame.add(wholeMonthPanel, BorderLayout.WEST);
		frame.add(buttonsPanel, BorderLayout.NORTH);
		
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	/**
	 * Create and initialize the buttons
	 * @return a panel with all the buttons
	 */
	public JPanel getButtons()
	{
		JPanel buttonsPanel = new JPanel(); 
		
		JButton createButton = new JButton("CREATE"); 
		createButton.setBackground(new Color(210, 0, 0));
		createButton.setOpaque(true);
		createButton.setBorderPainted(false);
		createButton.setForeground(Color.WHITE);
		createButton.setFont(new Font("Oswald", Font.BOLD, 12));
		
		createButton.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						//System.out.println("Create button clicked");
						createDialog();
					}
				});
		
		JButton previousButton = new JButton("<"); 
		previousButton.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						//System.out.println("Previous button clicked");
						myCal.add(Calendar.DAY_OF_MONTH, -1);

						updateMonthView(myCal);
						getDayView();
						repaint();
					}
				});
		
		JButton nextButton = new JButton(">");
		nextButton.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{	
						//System.out.println("Next button clicked");
						myCal.add(Calendar.DAY_OF_MONTH, 1);
						
						updateMonthView(myCal);
						getDayView();
						repaint();
					}
				});
		
		JButton quitButton = new JButton("Quit");
		quitButton.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						//System.out.println("Quit button clicked");
						calModel.quit();
						//setVisible(false);
						frame.setVisible(false);
						frame.dispose();
						System.exit(0);
						//dispose();
					}
				});
		
		buttonsPanel.add(createButton);
		buttonsPanel.add(previousButton);
		buttonsPanel.add(nextButton);
		buttonsPanel.add(quitButton);
		
		return buttonsPanel;
	}
	
	
	
	/**
	 * The entire dialog for creating an event
	 * Holds the text fields and calls the appropriate methods to create the event
	 */
	public void createDialog() 
	{
		JDialog eventDialog = new JDialog();
		eventDialog.setLayout(new BorderLayout(20, 20));
		eventDialog.setSize(450, 100);
		eventDialog.setTitle("Create event");
		JPanel textFieldPanel = new JPanel();

		JTextField eventName = new JTextField();
		eventName.setText("Untitled Event");
		Border nameBorder = BorderFactory.createLineBorder(Color.RED, 1);
		eventName.setBorder(nameBorder);

		JTextField eventDate = new JTextField();
		eventDate.setText(calModel.getDate(myCal));
		Border dateBorder = BorderFactory.createLineBorder(Color.PINK, 1);
		eventDate.setBorder(dateBorder);
		eventDate.setEditable(false);

		JTextField eventStartTime = new JTextField();
		String time = "";
		
		int hour = myCal.get(Calendar.HOUR);
		if(hour < 10)
			time += "0" + hour + ":";
		else
			time += hour + ":";
		
		int minute = myCal.get(Calendar.MINUTE);
		if (minute < 10) {
			time += "0" + minute;
		} else
			time += myCal.get(Calendar.MINUTE);
		
		if(myCal.get(Calendar.AM_PM) == Calendar.PM)
		{
			time += "PM";
		}
		
		else 
		{
			time += "AM";
		}

		eventStartTime.setText(time);
		eventStartTime.setBorder(dateBorder);

		// EventEndTime
		JTextField eventEndTime = new JTextField();
		eventEndTime.setText("11:59PM");
		eventEndTime.setBorder(dateBorder);

		// Save
		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						String nameString = eventName.getText();
						String dateString = eventDate.getText();
						String startString = eventStartTime.getText();
						String endString = eventEndTime.getText();
						boolean succeeded = calModel.createWithStrings(nameString, dateString, startString, endString);
						if (!succeeded)
						{
							JOptionPane.showMessageDialog(null, "There is a time conflict \nEvent Not Created");
						}
						repaint();
						eventDialog.dispose();
						//getDayView();
					}
				});

		// Adds The Text Fields
		textFieldPanel.add(eventDate);
		textFieldPanel.add(eventStartTime);
		textFieldPanel.add(eventEndTime);
		textFieldPanel.add(saveButton);
		// Add All Components to the JDialog
		eventDialog.add(eventName, BorderLayout.NORTH);
		eventDialog.add(textFieldPanel, BorderLayout.CENTER);

		// eventDialog.pack();
		eventDialog.setVisible(true);
	}
	
	
	/**
	 * Get the month view of the calendar
	 * @return a panel with all the components for the month
	 */
	public JPanel getMonthView()
	{
		JPanel wholePanel = new JPanel();
		wholePanel.setLayout(new BorderLayout());
		
		JPanel thisMonthView = new JPanel(); 
		thisMonthView.setBackground(Color.white);
		//int weeks = myCal.getActualMaximum(Calendar.WEEK_OF_MONTH);
		thisMonthView.setLayout(new GridLayout(0, 7));
		
		String monthStr = displayMonth(myCal);
		
		monthTextArea = new JTextArea(); 
		monthTextArea.setText(monthStr);
		wholePanel.add(monthTextArea, BorderLayout.NORTH);
		
		for(JTextArea text : days)
		{
			thisMonthView.add(text); 
		}
		
		wholePanel.add(thisMonthView, BorderLayout.CENTER);
		wholeMonthPanel = wholePanel;
		return wholePanel;
	}
	
	
	/**
	 * Updates the month view after changing to a different date or any other change
	 * @param temp - the new date to display
	 */
	public void updateMonthView(GregorianCalendar temp)
	{
		days = new ArrayList<>();
		
		frame.remove(wholeMonthPanel);
		wholeMonthPanel = new JPanel();
	
		myCal.set(Calendar.DAY_OF_MONTH, temp.get(Calendar.DAY_OF_MONTH));
		getMonthView();
		frame.add(wholeMonthPanel, BorderLayout.WEST);
	}
	
	
	/**
	 * Creates the actual days in the month and makes them click-able 
	 * @param cal - the day to display as the current day
	 * @return a string with the description at the top
	 */
	public String displayMonth(GregorianCalendar cal)
	{
		String descriptor = ""; //holds the string for the descriptor which has month and year
		descriptor += CalendarModel.arrayOfMonths[cal.get(GregorianCalendar.MONTH)]; 
		descriptor += " ";
		descriptor += cal.get(GregorianCalendar.YEAR); //year of this view
		descriptor += "\n";
		
		for(int i = 0; i < CalendarModel.tinyDays.length; i++)
		{
			descriptor += CalendarModel.tinyDays[i] + "   "; //the days of the week
		}
		
		JTextArea describeText = new JTextArea(); 
		describeText.setText(descriptor);

		//create a temporary calendar set the the first day of this month 
		GregorianCalendar temp = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1);
		int firstDay = temp.get(Calendar.DAY_OF_WEEK); //set to the first day (Sunday, Monday, etc) of the month
		int maxDays = temp.getActualMaximum(Calendar.DAY_OF_MONTH); //get the amount of days in this month
		
		days = new ArrayList<>();
		
		for(int j = 1; j < maxDays+firstDay; j++)
		{
			//set the calendar to a different day as it goes along
			temp.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), j - firstDay+1); 
				
			if(j < firstDay) //before the days start printing
			{
				JTextArea space = new JTextArea(); 
				space.setText(" ");
				space.setSize(0, 0);
				space.setEditable(false);
				days.add(space);
			}
			
			else //when the days have started
			{
				if(calModel.isTheDay(cal, temp))
				{
					//System.out.println("Is the day");
					//System.out.println("cal: " + cal.get(Calendar.DAY_OF_MONTH)  + " temp: " + temp.get(Calendar.DAY_OF_MONTH));
					JTextArea day = new JTextArea();
					day.setText("" + temp.get(Calendar.DAY_OF_MONTH));
					day.setEditable(false);
					day.setSize(0, 0);
					day.setBackground(Color.gray);
					day.addMouseListener( 
							new MouseAdapter()
							{
								public void mouseClicked(MouseEvent e)
								{
									//String realDate = day.getText().substring(1);
									int date = Integer.parseInt(day.getText());
									//int date = Integer.parseInt(realDate);
									cal.set(Calendar.DAY_OF_MONTH, date);
									updateMonthView(cal);
									getDayView();
									repaint();
								}
							});
					days.add(day);
				}
				
				else //print normally for other days
				{
					JTextArea day = new JTextArea();
					day.setText(temp.get(Calendar.DAY_OF_MONTH) + "");
					day.setEditable(false);
					day.setSize(0, 0);
					day.addMouseListener( 
							new MouseAdapter()
							{
								public void mouseClicked(MouseEvent e)
								{
									int date = Integer.parseInt(day.getText());
									cal.set(Calendar.DAY_OF_MONTH, date);
									
									updateMonthView(cal);
									getDayView();
									repaint();
								}
							});
					days.add(day);
				}
			}		
		}
		return descriptor;
	}
	
	
	/**
	 * Display the day view for the given day
	 * @param cal - the calendar to display the day view for
	 */
	public String displayDayView(GregorianCalendar cal)
	{
		String day = "";
		day += CalendarModel.arrayOfDays[cal.get(Calendar.DAY_OF_WEEK)-1];
		day += ", "; 
		day += CalendarModel.arrayShortMonths[cal.get(Calendar.MONTH)];
		day += " "; 
		day += cal.get(Calendar.DAY_OF_MONTH);
		day += ", ";
		day += cal.get(Calendar.YEAR);
		
		//System.out.println(day);
		
		//check if there are any events for this day and display them
		//create a calendar based on same date but default time of 0 for easy comparison
		cal = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		boolean exists = calModel.eventExists(cal);
		
		String dayEvents = "";
		if(exists)
		{
			TreeMap<GregorianCalendar, TreeSet<Event>> theMap = calModel.getEvents();
			TreeSet<Event> myTree = theMap.get(cal); //pull down the treeSet
			for(Event e: myTree) 
			{
				//System.out.print(e.getTitle() + " "); 
				//dayEvents += e.getTitle() + " ";
				
				if(e.getStartTime().get(Calendar.HOUR_OF_DAY) == 12)
					dayEvents += "12:";
				else 
				{
					//System.out.print(e.getStartTime().get(Calendar.HOUR) + ":");
					dayEvents += e.getStartTime().get(Calendar.HOUR) + ":";
				}
				
				int sminute = e.getStartTime().get(Calendar.MINUTE); 
				if (sminute == 0)
				{
					//System.out.print("00");
					dayEvents += "00";
				}
				
				else 
				{
					//System.out.print(e.getStartTime().get(Calendar.MINUTE) + "");
					dayEvents += e.getStartTime().get(Calendar.MINUTE) + "";
				}
				
				if(e.getStartTime().get(Calendar.AM_PM) == Calendar.PM)
				{
					dayEvents += "PM";
				}
				
				else 
				{
					dayEvents += "AM";
				}
				
				if((e.getEndTime() != null) && !(e.getEndTime().equals(e.getStartTime())))
				{
					//System.out.print(" - ");
					dayEvents += " - ";
					
					if(e.getEndTime().get(Calendar.HOUR_OF_DAY) == 12)
						dayEvents += "12:";
					
					else 
					{
						//System.out.print(e.getEndTime().get(Calendar.HOUR) + ":");
						dayEvents += e.getEndTime().get(Calendar.HOUR) + ":";
					}
					
					int eminute = e.getEndTime().get(Calendar.MINUTE); 
					if (eminute == 0)
					{
						//System.out.print("00");
						dayEvents += "00";
					}
					
					else 
					{
						//System.out.print(e.getEndTime().get(Calendar.MINUTE) +"");
						dayEvents += e.getEndTime().get(Calendar.MINUTE) +"";
					}
					
					if(e.getEndTime().get(Calendar.AM_PM) == Calendar.PM)
					{
						dayEvents += "PM";
					}
					
					else 
					{
						dayEvents += "AM";
					}
					
				}
				//System.out.print(": " + e.getTitle() + " "); 
				dayEvents += ": " + e.getTitle() + " ";
				
				//System.out.println(" ");
				dayEvents += " \n";
			}
			//System.out.println(" ");
			dayEvents += " \n";
		}
		
		return dayEvents;
	}
	
	
	/**
	 * Get the gui part of the day view 
	 * @return a panel with all the parts to display a day view
	 */
	public JPanel getDayView()
	{
		dayViewPanel = new JPanel();
		frame.remove(dayViewPanel);
		dayViewPanel.setLayout(new BorderLayout());
		
		String day = calModel.getDayDescription(myCal);
		dateText.setEditable(false);
		dateText.setText(day);
		dayViewPanel.add(dateText, BorderLayout.NORTH);
		
		String events = displayDayView(myCal);
		dayEvents.setEditable(false);
		dayEvents.setText(events);
		dayViewPanel.add(dayEvents, BorderLayout.CENTER);
		
		frame.add(dayViewPanel, BorderLayout.CENTER);
		dayViewPanel.repaint();
		//frame.repaint();
		
		return dayViewPanel;
	}
	
	/**
	 * When the state is changed - an event is created, display the event
	 */
	public void stateChanged(ChangeEvent e)
	{
		//System.out.println("State changed");
		getDayView();
		repaint();
	}
}
