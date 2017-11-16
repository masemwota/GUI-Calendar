package hw4;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.*;
import javax.swing.event.*;

public class CalendarComponent extends Component implements ChangeListener
{
	//hold calendar component
	private JFrame frame;
	private CalendarModel calModel;
	private GregorianCalendar cal;
	
	private JPanel dayViewPanel; 
	private JTextArea dateText;
	
	private JTextArea monthTextArea;
	private JPanel monthView;
	private JPanel wholeMonthPanel;
	private ArrayList<JTextArea> days;
	
	public CalendarComponent(CalendarModel calModel)
	{
		this.calModel = calModel;
		cal = calModel.getCalendar();
		calModel.load();
		days = new ArrayList<>();
		
		frame = new JFrame("Simple Calendar"); 
		frame.setSize(400, 200);
		//frame.setForeground(Color.blue);
		//frame.setBackground(Color.CYAN);
		frame.setLayout(new BorderLayout());

		JPanel wholeMonthPanel = getMonthView();
		
		
		dayViewPanel = new JPanel();
		//dayViewPanel.setBackground(Color.MAGENTA);
		getDayView();
		
		JPanel buttonsPanel = getButtons(); 
		
		frame.add(wholeMonthPanel, BorderLayout.WEST);
		frame.add(buttonsPanel, BorderLayout.NORTH);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		/*
		 * 
	MouseListeners listeners = new MouseListeners();   
	addMouseListener(listeners);
	addMouseMotionListener(listeners);
		 */
		
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
					}
				});
		
		JButton previousButton = new JButton("<"); 
		previousButton.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
//						int currentDay = cal.get(Calendar.DAY_OF_MONTH);
//						int min = cal.getActualMinimum(Calendar.DAY_OF_MONTH); 
//						System.out.println("min: " + min);
//						
//						//if the 1st of the month, then you are switching to previous month
//						if(currentDay == min)
//						{
//							//set the month to the previous month
//							cal.add(Calendar.MONTH, -1);
//						}
						
						System.out.println("Previous button clicked");
						//GregorianCalendar previous = calModel.getCalendar();
						cal.add(Calendar.DAY_OF_MONTH, -1);
						
						String previousDay = calModel.getDayDescription(cal);
						dateText.setText(previousDay);
						updateMonthView(cal);
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
						
//						int currentDay = cal.get(Calendar.DAY_OF_MONTH);
//						int max = cal.getActualMaximum(Calendar.DAY_OF_MONTH); 
//						System.out.println("max: " + max);
//						
//						//GregorianCalendar next = calModel.getCalendar();
//						
//						//if the 1st of the month, then you are switching to previous month
//						if(currentDay == max)
//						{
//							//set the month to the previous month
//							cal.add(Calendar.MONTH, 1);
//						}
						
						cal.add(Calendar.DAY_OF_MONTH, 1);
						
						String nextDay = calModel.getDayDescription(cal);
						dateText.setText(nextDay);
						updateMonthView(cal);
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
						frame.dispose();
					}
				});
		
		buttonsPanel.add(createButton);
		buttonsPanel.add(previousButton);
		buttonsPanel.add(nextButton);
		buttonsPanel.add(quitButton);
		
		return buttonsPanel;
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
		
		String monthStr = displayMonth(cal);
		System.out.println(monthStr);
		
		monthTextArea = new JTextArea(); 
		monthTextArea.setText(monthStr);
		wholePanel.add(monthTextArea, BorderLayout.NORTH);
		//monthView.add(monthTextArea);
		
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
		for(JTextArea text : days)
		{
			text.removeAll();
		}
		
		//wholeMonthPanel.removeAll();
		//wholeMonthPanel.revalidate();
		wholeMonthPanel = new JPanel();
		frame.remove(wholeMonthPanel);
		cal.set(Calendar.DAY_OF_MONTH, temp.get(Calendar.DAY_OF_MONTH));
		getMonthView();
		//wholeMonthPanel.revalidate();
		//wholeMonthPanel.repaint();
		//frame.add(wholeMonthPanel);
		frame.add(wholeMonthPanel, BorderLayout.WEST);
	}
	
	
	
	
	
	public String displayMonth(GregorianCalendar cal)
	{
		System.out.println(cal.get(Calendar.DAY_OF_MONTH));
		String descriptor = ""; //holds the string for the descriptor which has month and year
		descriptor += CalendarModel.arrayOfMonths[cal.get(GregorianCalendar.MONTH)]; 
		descriptor += " ";
		descriptor += cal.get(GregorianCalendar.YEAR); //year of this view
		descriptor += "\n";
		
		for(int i = 0; i < CalendarModel.tinyDays.length; i++)
		{
			descriptor += CalendarModel.tinyDays[i] + "   "; //the days of the week
		}
		
		//System.out.print(descriptor); 
		JTextArea describeText = new JTextArea(); 
		describeText.setText(descriptor);
		//monthView.add(describeText);

		//create a temporary calendar set the the first day of this month 
		GregorianCalendar temp = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1);
		int firstDay = temp.get(Calendar.DAY_OF_WEEK); //set to the first day (Sunday, Monday, etc) of the month
		int maxDays = temp.getActualMaximum(Calendar.DAY_OF_MONTH); //get the amount of days in this month
		
		days = new ArrayList<>();
				
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
				JTextArea space = new JTextArea(); 
				space.setText("  ");
				space.setSize(0, 0);
				days.add(space);
			}
			
			else //when the days have started
			{
				if(calModel.isTheDay(cal, temp))
				{
					System.out.println("cal: " + cal.get(Calendar.DAY_OF_MONTH)  + " temp: " + temp.get(Calendar.DAY_OF_MONTH));
					//System.out.print("["); 
					//System.out.print(temp.get(Calendar.DAY_OF_MONTH));
					//System.out.print("]");
					
					monthView += "["; 
					monthView += temp.get(Calendar.DAY_OF_MONTH);
					monthView += "] ";
					
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
									day.removeAll();
									System.out.println("Clicked the text area");
									//int date = Integer.parseInt(((JTextArea)e.getSource()).getText());
									int date = Integer.parseInt(day.getText());
									System.out.println(date);
									//System.out.println(day.getText());
				
									cal.set(Calendar.DAY_OF_MONTH, date);
									
									String nextDay = calModel.getDayDescription(cal);
									dateText.setText(nextDay);
									dateText.repaint();
									updateMonthView(cal);
									//frame.repaint();
								}

							    public void mouseDragged(MouseEvent e) 
							    {
							    }
							});
					days.add(day);
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
					
					JTextArea day = new JTextArea();
					day.setText(temp.get(Calendar.DAY_OF_MONTH) + "");
					day.setEditable(false);
					day.setSize(0, 0);
					day.addMouseListener( 
							new MouseAdapter()
							{
								public void mouseClicked(MouseEvent e)
								{
									day.removeAll();
									System.out.println("Clicked the text area");
									//int date = Integer.parseInt(((JTextArea)e.getSource()).getText());
									int date = Integer.parseInt(day.getText());									
									System.out.println(day);
									//System.out.println(day.getText());
				
									cal.set(Calendar.DAY_OF_MONTH, date);
									
									String nextDay = calModel.getDayDescription(cal);
									dateText.setText(nextDay);
									dateText.repaint();
									updateMonthView(cal);
									//frame.repaint();
								}

							    public void mouseDragged(MouseEvent e) 
							    {
							    }
							});
					days.add(day);
					
					monthView += " ";
				}
			}		
		}
		
		//return descriptor + monthView;
		return descriptor;
	}
	
	
	
	
	
	public void getDayView()
	{
		//GregorianCalendar today = calModel.getCalendar();
		String day = calModel.getDayDescription(cal);
		dateText = new JTextArea(day); 
		dayViewPanel.add(dateText);
		
		frame.add(dayViewPanel, BorderLayout.CENTER);
	}
	
	
	
	public void stateChanged(ChangeEvent e)
	{
		//
	}
}
