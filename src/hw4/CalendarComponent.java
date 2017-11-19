package hw4;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.*;

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
						System.out.println("Create button clicked");
						createDialog();
					}
				});
		
		JButton previousButton = new JButton("<"); 
		previousButton.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						System.out.println("Previous button clicked");
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
						System.out.println("Next button clicked");
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
						System.out.println("Quit button clicked");
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
	 * Includes a textFieldPanel that stores JTextFields for the title, date, start time, and end time of the event.
	 * A save button adds the event to the TreeMap in the model.
	 */
	public void createDialog() 
	{
		JDialog eventDialog = new JDialog();
		eventDialog.setLayout(new BorderLayout(20, 20));
		eventDialog.setSize(450, 100);
		eventDialog.setTitle("Create event");
		JPanel textFieldPanel = new JPanel();

		// Event Name
		JTextField eventName = new JTextField();
		eventName.setText("Untitled Event");
		Border eventNameBorder = BorderFactory.createLineBorder(Color.BLUE, 1);
		eventName.setBorder(eventNameBorder);

		// Event Date
		JTextField eventDate = new JTextField();
		eventDate.setText(calModel.getDate(myCal));
		Border eventDateBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1);
		eventDate.setBorder(eventDateBorder);
		eventDate.setEditable(false);

		// EventStartTime
		JTextField eventStartTime = new JTextField();
		String time = "";
		//myCal.get(Calendar.HOUR) + ":"
		
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
		eventStartTime.setBorder(eventDateBorder);

		// EventEndTime
		JTextField eventEndTime = new JTextField();
		eventEndTime.setText("11:59PM");
		eventEndTime.setBorder(eventDateBorder);

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
						getDayView();
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
	
	
	
	public JPanel getMonthView()
	{
		JPanel wholePanel = new JPanel();
		wholePanel.setLayout(new BorderLayout());
		
		JPanel thisMonthView = new JPanel(); 
		//monthView.setSize(10, 10);
		thisMonthView.setBackground(Color.white);
		thisMonthView.setLayout(new GridLayout(5, 7));
		//monthView.setBackground(Color.RED);
		
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
	
	
		
	public void updateMonthView(GregorianCalendar temp)
	{
		days = new ArrayList<>();
		
		frame.remove(wholeMonthPanel);
		wholeMonthPanel = new JPanel();
	
		myCal.set(Calendar.DAY_OF_MONTH, temp.get(Calendar.DAY_OF_MONTH));
		getMonthView();
		frame.add(wholeMonthPanel, BorderLayout.WEST);
	}
	
	
	
	
	
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
				space.setText("  ");
				space.setSize(0, 0);
				days.add(space);
			}
			
			else //when the days have started
			{
				if(calModel.isTheDay(cal, temp))
				{
					//System.out.println("Is the day");
					//System.out.println("cal: " + cal.get(Calendar.DAY_OF_MONTH)  + " temp: " + temp.get(Calendar.DAY_OF_MONTH));
					JTextArea day = new JTextArea();
					day.setText(temp.get(Calendar.DAY_OF_MONTH) + "");
					day.setEditable(false);
					day.setSize(0, 0);
					day.setBackground(Color.gray);
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
	
	
	public JPanel getDayView()
	{
		dayViewPanel = new JPanel();
		frame.remove(dayViewPanel);
		dayViewPanel.setLayout(new BorderLayout());
		
		String day = calModel.getDayDescription(myCal);
		dateText.setEditable(false);
		dateText.setText(day);
		dayViewPanel.add(dateText, BorderLayout.NORTH);
		
		String events = calModel.displayDayView(myCal);
		dayEvents.setEditable(false);
		dayEvents.setText(events);
		dayViewPanel.add(dayEvents, BorderLayout.CENTER);
		
		frame.add(dayViewPanel, BorderLayout.CENTER);
		dayViewPanel.repaint();
		//frame.repaint();
		
		return dayViewPanel;
	}
	
	
	
	public void stateChanged(ChangeEvent e)
	{
		//
	}
}
