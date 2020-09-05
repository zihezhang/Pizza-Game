/*
 * Name: Dina Bernstein & Zihe Zhang
 * Class: Grade 11 AP Computer Science
 * Teacher: Mr. Benum
 * Description: This game is a cooking game where the user will be given an order for a pizza with specific toppings, and they must make as many pizzas as possible in a short time. A page also displays instructions and the top 6 high scores.
 */

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;
import java.text.NumberFormat;
import java.util.Scanner;

import net.miginfocom.swing.MigLayout;

import java.util.Timer;
import java.util.TimerTask;

public class BurgerExclamationMark
{
	//frame, where everything all the components of the game will be stored
	final static JFrame frame= new JFrame("PIZZA");
	
	//size of the frame of the whole game
	final static int WIDTH = 800;
	final static int HEIGHT = 560;

	//size of each food button
	final static int IMAGEWIDTH = 90;
	final static int IMAGEHEIGHT = 90; 
	final static int TITLEWIDTH = 375;
	final static int TITLEHEIGHT = 375; 
	
	//sets buttons for ingredients players can pick
	final static JButton doughButton = new JButton(new ImageIcon(new ImageIcon("dough.png").getImage().getScaledInstance(IMAGEHEIGHT, IMAGEWIDTH, Image.SCALE_DEFAULT)));
	final static JButton cheeseButton = new JButton(new ImageIcon(new ImageIcon("cheese.png").getImage().getScaledInstance(IMAGEHEIGHT, IMAGEWIDTH, Image.SCALE_DEFAULT)));
	final static JButton sauceButton = new JButton(new ImageIcon(new ImageIcon("sauce.png").getImage().getScaledInstance(IMAGEHEIGHT, IMAGEWIDTH, Image.SCALE_DEFAULT)));
	final static JButton pepperButton = new JButton(new ImageIcon(new ImageIcon("pepper.png").getImage().getScaledInstance(IMAGEHEIGHT, IMAGEWIDTH, Image.SCALE_DEFAULT)));
	final static JButton pepperoniButton = new JButton(new ImageIcon(new ImageIcon("pepperoni.png").getImage().getScaledInstance(IMAGEHEIGHT, IMAGEWIDTH, Image.SCALE_DEFAULT)));
	final static JButton mushroomButton = new JButton(new ImageIcon(new ImageIcon("mushrooms.png").getImage().getScaledInstance(IMAGEHEIGHT, IMAGEWIDTH, Image.SCALE_DEFAULT)));
	
	//sets button for the general buttons used in the game
	final static JButton sendButton = new JButton("Send");
	final static JButton startButton = new JButton("Start");
	final static JButton instructionButton = new JButton("Instructions");
	final static JButton highScoreButton = new JButton("High Scores"); 
	
	//creating different jpanels for the game with gridbadlayout
	final static JPanel introPanel = new JPanel(new GridBagLayout()); 
	final static JPanel instructionPanel = new JPanel(new GridBagLayout());
	final static JPanel highscorePanel = new JPanel(new GridBagLayout());
	final static JPanel gameOverPanel = new JPanel(new GridBagLayout());
	final static JPanel highScoresPanel = new JPanel(new GridBagLayout());

	//creating jpanels for other parts of the game with miglayout
	final static JPanel gamePanel = new JPanel(new MigLayout("insets 10"));
	
	//some fonts used
	final static Font startButtonFont = new Font("Serif", Font.BOLD, 20);
	final static Font buttonFont = new Font("", Font.BOLD, 15);
	
	//sets labels used in the game
	final static JLabel scoreLabel = new JLabel("Score: ");
	final static JLabel timeLabel = new JLabel("Time: ");
	final static JLabel endScoreLabel = new JLabel("");
	
	final static JLabel orderText = new JLabel();
	final static JLabel orderResponse = new JLabel("Starting in ...");
	final static JLabel pizzaTray = new JLabel(new ImageIcon(new ImageIcon("pizza tray.png").getImage().getScaledInstance(500,350, Image.SCALE_DEFAULT)));
	final static JLabel titlePicture = new JLabel(new ImageIcon(new ImageIcon("title.png").getImage().getScaledInstance(TITLEHEIGHT, TITLEWIDTH, Image.SCALE_DEFAULT)));
	
	//Creates the constants to be used in the game
	//Holds the length of the high score list
	final static int HIGH_SCORE_LIST_LENGTH = 6;
	//Holds the length of the pizza array
	final static int PIZZA_ARRAY_LENGTH = 6;
	//Holds the olds that a topping will be generated (if a random number between 0 and 1 is above this constant, a topping is added)
	final static double TOPPING_ODDS = 0.6;
		
	//generates array for the pizza you have to make, and the pizza you are making. An array is always 6 ints long, each int corresponds to a different element of the pizza
	//If that element is there, the array for that index is set to 1, if not it is set to 0
	//The indexes in the array correspond to [dough,sauce,cheese,peppers,pepperoni,mushrooms]
	final static int[] pizzaOrder = generatePizza();  
	final static int[] pizza = new int[PIZZA_ARRAY_LENGTH];

	//Generates variable that will hold user's score
	final static double[] score = {0};
	
	//Generates variable that will track how long it takes to make a pizza
	final static int[] pizzaTime = {0};
	
	//Number format object used to format scores as costs
	final static NumberFormat money = NumberFormat.getCurrencyInstance();

	//Two fonts that will be used for text on various parts of the page
	static Font titleFont;
	static Font textFont;
	
	//The blue background colour
	final static Color BACKGROUND_COLOUR = new Color(66, 88, 178); 
	
	//These arrays will hold a list of the names with high scores, and the actual high scores, respectively
	static String[] namesList = new String[HIGH_SCORE_LIST_LENGTH];
	static double[] scoresList = new double[HIGH_SCORE_LIST_LENGTH];
	
	//Imports the text file containing high scores
	static String fileName = "HighScores.txt";
	
	//These arrays will hold the JLabels that display the text of the names with high scores and the actual high scores
	static JLabel[] nameLabels = new JLabel[HIGH_SCORE_LIST_LENGTH];
	static JLabel[] scoreLabels = new JLabel[HIGH_SCORE_LIST_LENGTH];

	public static void main(String[] args) throws InterruptedException
	{
		//Imports the two fonts
		try 
		{
			titleFont = Font.createFont(Font.TRUETYPE_FONT, new File("Exo2-SemiBold.ttf")).deriveFont(30f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Exo2-SemiBold.ttf")));
		} 
		catch(IOException | FontFormatException e) 
		{
			System.out.print(e.getStackTrace());
		}
		try 
		{
			textFont = Font.createFont(Font.TRUETYPE_FONT, new File("Exo2-Regular.ttf")).deriveFont(20f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Exo2-Regular.ttf")));
		} 
		catch(IOException | FontFormatException e) 
		{
			System.out.print(e.getStackTrace());
		}
		//Runs the home page method
		homePage();
	}

	/**
	 * The whole home page and game page are created using this method. It also sets up some features for other panels
	 * pre: program started to run
	 * post: user pressed on start, instructions, highscore button or exited the program
	 */
	public static void homePage() 
	{
		//sets up grid bag layout
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;

		//creating buttons with no background or outline so that it is just a text or a image showing
		doughButton.setBorderPainted(false);
		doughButton.setFocusPainted(false);
		doughButton.setContentAreaFilled(false);
		cheeseButton.setBorderPainted(false);
		cheeseButton.setFocusPainted(false);
		cheeseButton.setContentAreaFilled(false);
		sauceButton.setBorderPainted(false);
		sauceButton.setFocusPainted(false);
		sauceButton.setContentAreaFilled(false);
		pepperButton.setBorderPainted(false);
		pepperButton.setFocusPainted(false);
		pepperButton.setContentAreaFilled(false);
		pepperoniButton.setBorderPainted(false);
		pepperoniButton.setFocusPainted(false);
		pepperoniButton.setContentAreaFilled(false);
		mushroomButton.setBorderPainted(false);	
		mushroomButton.setFocusPainted(false);
		mushroomButton.setContentAreaFilled(false);
		sendButton.setBorderPainted(false);
		sendButton.setFocusPainted(false);
		sendButton.setContentAreaFilled(false);
		startButton.setBorderPainted(false);
		startButton.setFocusPainted(false);
		startButton.setContentAreaFilled(false);
		instructionButton.setBorderPainted(false);
		instructionButton.setFocusPainted(false);
		instructionButton.setContentAreaFilled(false);
		highScoreButton.setBorderPainted(false);
		highScoreButton.setFocusPainted(false);
		highScoreButton.setContentAreaFilled(false);
		
		//sets the colour and the fonts of different labels and buttons
		gamePanel.setBackground(Color.WHITE); 
		gameOverPanel.setBackground(Color.BLACK);
		introPanel.setBackground(BACKGROUND_COLOUR);
		startButton.setForeground(Color.WHITE);
		instructionButton.setForeground(Color.WHITE);
		highScoreButton.setForeground(Color.WHITE);
		scoreLabel.setFont(textFont);
		timeLabel.setFont(textFont);
		endScoreLabel.setFont(textFont);
		sendButton.setFont(textFont);
		instructionButton.setFont(textFont);
		highScoreButton.setFont(textFont);
		startButton.setFont(startButtonFont);

		//makes all ingredient buttons unclickable so that you are forced to wait until the game loads
		doughButton.setEnabled(false);
		cheeseButton.setEnabled(false);
		sauceButton.setEnabled(false);
		pepperButton.setEnabled(false);
		mushroomButton.setEnabled(false);
		pepperoniButton.setEnabled(false);
		sendButton.setEnabled(false);
	
		//Sets every value in the pizza array to equal 0
		for (int i = 0; i < pizza.length; i++)
		{
			pizza[i] = 0;
		}
		//Adds functions to each of the buttons
		instructionButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//Calls the instruction page method, opening the instruction page
				frame.add(instructionPanel);
				instructionsPage(); 
			}
		});
		//Opens the high scores screen when the high score button is pressed
		highScoreButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				frame.add(highScoresPanel);
				openHighScoreScreen(); 
			}
		});
		startButton.addActionListener(new ActionListener() //Starts playing the game when the start button is pressed
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{

				introPanel.setVisible(false);
				frame.add(gamePanel);
				gamePanel.setVisible(true);
				playGame();
			}
		});

		//Pressing an ingredient button does three things: it disables that button (so it can't be repressed) and sets the next button to enabled,
		//It adds a 1 to its set index in the array, so the pizza now has that ingredient
		//It updates the pizza image using a method
		doughButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				sauceButton.setEnabled(true);
				doughButton.setEnabled(false);
				pizza[0] = 1;
				setPizzaImage(pizza,pizzaTray);
			}
		});
		sauceButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				sauceButton.setEnabled(false);
				cheeseButton.setEnabled(true);
				pizza[1] = 1;
				setPizzaImage(pizza,pizzaTray);
			}
		});
		cheeseButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				cheeseButton.setEnabled(false);
				mushroomButton.setEnabled(true);
				pepperoniButton.setEnabled(true);
				pepperButton.setEnabled(true);
				pizza[2] = 1;
				setPizzaImage(pizza, pizzaTray);
			}
		});
		pepperButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				pepperButton.setEnabled(false);
				pizza[3] = 1;
				setPizzaImage(pizza, pizzaTray);
			}
		});
		pepperoniButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				pepperoniButton.setEnabled(false);
				pizza[4] = 1;
				setPizzaImage(pizza, pizzaTray);
			}
		});
		mushroomButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				mushroomButton.setEnabled(false);
				pizza[5] = 1;
				setPizzaImage(pizza, pizzaTray);
			}
		});
		
		//sendButton checks if the pizzas are made correctly, resets buttons, creates a new pizza to make and adds score
		sendButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (compareArrays(pizza,pizzaOrder))
				{
					orderResponse.setText("Thanks!");
				}
				
				else
				{
					orderResponse.setText("That's not what I want. ");
				}
				doughButton.setEnabled(true);
				cheeseButton.setEnabled(false);
				sauceButton.setEnabled(false);
				pepperButton.setEnabled(false);
				mushroomButton.setEnabled(false);
				pepperoniButton.setEnabled(false);
				score[0]+=findPizzaCost(pizza,pizzaOrder,pizzaTime[0]);
				pizzaTime[0] = 0;
				//This generates a new pizza order, holds it in a variable temp, then transfers all the values in it to the pizzaOrder array
				int[] temp = generatePizza(); 
				for (int i = 0; i < pizzaOrder.length; i++)
				{
					pizzaOrder[i] = temp[i];
				}
				//This sets every index in the pizza array to equal 0, meaning that the pizza now has no ingredients
				for (int i = 0; i < pizza.length; i++)
				{
					pizza[i] = 0;
				}
				setPizzaImage(pizza, pizzaTray);
				//This uses a method to convert the array order into a readable sentence, and sets it as a JLabel for the user to know what pizza to make
				orderText.setText(arrayToOrder(pizzaOrder));
			}
		});

		//add the buttons and labels to the game panel using MigLayout
		gamePanel.add(scoreLabel, "cell 0 0 4 1");
		gamePanel.add(timeLabel, "cell 0 1 4 1");
		gamePanel.add(pizzaTray, "cell 0 2 5 2");
		gamePanel.add(orderText, "cell 4 2 2 1");
		gamePanel.add(orderResponse, "cell 4 1 2 1");
		gamePanel.add(sendButton, "cell 4 3, center");
		gamePanel.add(doughButton, "cell 0 4");
		gamePanel.add(sauceButton, "cell 1 4");
		gamePanel.add(cheeseButton, "cell 2 4");
		gamePanel.add(pepperButton, "cell 3 4");
		gamePanel.add(pepperoniButton, "cell 4 4");
		gamePanel.add(mushroomButton, "cell 5 4");

		//layout for the home screen panel
		introPanel.add(titlePicture, c);
		c.gridy++;
		introPanel.add(startButton, c);
		c.gridy++;
		introPanel.add(instructionButton, c);
		c.gridy++;
		introPanel.add(highScoreButton,c);
	
		//adding all the panels to the frame and only setting introPanel to visible
		frame.add(gamePanel);
		gamePanel.setVisible(false);
		frame.add(highscorePanel);
		highscorePanel.setVisible(false);
		highscorePanel.setBackground(BACKGROUND_COLOUR);
		gameOverPanel.setVisible(false);
		frame.add(introPanel);
		
		//sets up frame details	
		frame.setSize(WIDTH, HEIGHT);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);

	}
	/**
	 * Generates a random pizza for the player to make
	 * pre: game must have started
	 * post: generates 1 of the 7 pizza combinations
	 */
	public static int[] generatePizza() 
	{
		//Creates a temporary array of the same length as the pizzaorder array
		int[] temp = new int[PIZZA_ARRAY_LENGTH];
		//Sets the first three values of the array to 1, meaning that the first three ingredients (dough, sauce, and cheese) are always present
		for (int i = 0; i < 3; i++)
		{
			temp[i] = 1;
		}
		//Generates a random number, and if it is above the constant number, a topping is added. This is repeated for all three toppings
		for (int i = 3; i < PIZZA_ARRAY_LENGTH; i++)
		{
			if (Math.random()>=TOPPING_ODDS)
			{
				temp[i]=1;
			}
		}
		//Returns the created array
		return temp;
	}
	/**
	 * boolean that returns true if two arrays are identical
	 * pre: both arrays must be the same length
	 * post: true will be returned if arrays are the same, false if not
	 */
	public static boolean compareArrays(int[] arrayOne, int[] arrayTwo)
	{
		//A temporary boolean value is created
		boolean temp = true;
		//This loop goes through each index and sets the boolean to false if any index in the two arrays are different
		for (int i = 0; i < arrayOne.length; i++)
		{
			if (arrayOne[i]!=arrayTwo[i])
			{
				temp = false;
			}
		}
		//Will return true if the loop didn't find any differences, returns false if the arrays are different
		return temp;
	}
	/**
	 * Creates the prompt text according to the random pizza generated. This will create the text to ask the player to make a certain type of pizza
	 * pre: an expected pizza has been generated
	 * post: returns text prompting reader to make a pizza with specific toppings
	 */
	public static String arrayToOrder(int[] array)
	{
		//Uses HTML formatting tags
		String text = "<html> I want a pizza with";
		//Counter will hold the amount of toppings the pizza has
		int toppings = 0;
		//Displays the name of a topping if that index in the array is 1 (meaning that the topping is present)
		if (array[3]==1)
		{
			text += "<br>-Peppers</br> ";
			toppings++;
		}
		if (array[4]==1)
		{
			text+="<br>-Pepperoni</br> ";
			toppings++;
		}
		if (array[5]==1)
		{
			text+="<br>-Mushrooms</br> ";
			toppings++;
		}
		//States that the pizza needs no toppings if the toppings counter is at 0
		if (toppings == 0)
		{
			text+=" no toppings";
		}
		text+="</html>";
		//Returns the text that was created
		return text;
	}
	/**
	 * returns the amount of money being earned in the game
	 * pre: order and pizza are the same length, all ints in each array have a value of 0 or 1
	 * post: returns either the value of earned by making the pizza
	 */
	public static double findPizzaCost(int[] order, int[] pizza, int pizzaTime)
	{
		//This will store the amount of money the pizza made
		double cost = 0;
		//Holds the amount of time the pizza took to make, in seconds
		int sec = pizzaTime/1000;
		//If the pizza matches the order, the player gets $5 + $1.50 for each topping
		if (compareArrays(order,pizza))
		{
			cost+= 5;
			for (int i = 3; i <= 5; i++)
			{
				if(order[i]==1)
				{
					cost+=1.5;
				}
			}
			//If somehow, someone takes less than one second to make the pizza, they get a $5 bonus
			if (sec == 0)
			{
				cost+=5;
			}
			//Otherwise, a function calculates the amount of bonus money the player gets for speed
			else
			{
				cost+= ((double)(5/sec));
			}
		}
		//If the user makes the wrong pizza they lose $3
		else
		{
			cost-=3;
		}
		//Outputs the code
		return cost;
	}
	/**
	 * Displays the graphics of the game so that the picture of the corresponding pizza appears when user presses desired ingredients
	 * pre: game has started and pizza has been prompted
	 * post: a image should be shown when user presses the ingredient buttons
	 */
	public static void setPizzaImage(int[] pizza, JLabel picture)
	{
		//An array for each pizza combination is created. The indices in the arrays correspond to dough, sauce, cheese, peppers, pepperoni, and mushrooms respectively. A 1 means that that ingredient is present while a 0 means it is not
		int[] empty = {0,0,0,0,0,0};
		int[] pizzaDough = {1,0,0,0,0,0};
		int[] pizzaSauce = {1,1,0,0,0,0};
		int[] pizzaCheese = {1,1,1,0,0,0};
		int[] pepper = {1,1,1,1,0,0};
		int[] pepperoni = {1,1,1,0,1,0};
		int[] mushroom = {1,1,1,0,0,1};
		int[] pepperPepperoni = {1,1,1,1,1,0};
		int[] pepperMushroom = {1,1,1,1,0,1};
		int[] mushroomPepperoni = {1,1,1,0,1,1};
		int[] allToppings = {1,1,1,1,1,1};
		//Each pizza combination sets a different image of a (partially complete) pizza
		if (compareArrays(pizza,empty))
		{
			picture.setIcon(new ImageIcon(new ImageIcon("pizza tray.png").getImage().getScaledInstance(500,350, Image.SCALE_DEFAULT)));
		}
		if (compareArrays(pizza, pizzaDough))
		{
			picture.setIcon(new ImageIcon(new ImageIcon("pizza dough.png").getImage().getScaledInstance(500,350, Image.SCALE_DEFAULT)));
		}
		if (compareArrays(pizza, pizzaSauce))
		{
			picture.setIcon(new ImageIcon(new ImageIcon("pizza sauce.png").getImage().getScaledInstance(500,350, Image.SCALE_DEFAULT)));
		}
		if (compareArrays(pizza,pizzaCheese))
		{ 
			picture.setIcon(new ImageIcon(new ImageIcon("pizza cheese.png").getImage().getScaledInstance(500,350, Image.SCALE_DEFAULT)));
		}
		if (compareArrays(pizza,pepper))
		{
			picture.setIcon(new ImageIcon(new ImageIcon("pizza pepper.png").getImage().getScaledInstance(500,350, Image.SCALE_DEFAULT)));
		}
		if (compareArrays(pizza,pepperoni))
		{
			picture.setIcon(new ImageIcon(new ImageIcon("pizza pepperoni.png").getImage().getScaledInstance(500,350, Image.SCALE_DEFAULT)));
		}
		if (compareArrays(pizza,mushroom))
		{
			picture.setIcon(new ImageIcon(new ImageIcon("pizza mushroom.png").getImage().getScaledInstance(500,350, Image.SCALE_DEFAULT)));
		}
		if (compareArrays(pizza,pepperPepperoni))
		{
			picture.setIcon(new ImageIcon(new ImageIcon("pizza pepper, pepperoni.png").getImage().getScaledInstance(500,350, Image.SCALE_DEFAULT)));
		}
		if (compareArrays(pizza,pepperMushroom))
		{
			picture.setIcon(new ImageIcon(new ImageIcon("pizza pepper, mushroom.png").getImage().getScaledInstance(500,350, Image.SCALE_DEFAULT)));
		}
		if (compareArrays(pizza,mushroomPepperoni))
		{
			picture.setIcon(new ImageIcon(new ImageIcon("pizza pepperoni, mushroom.png").getImage().getScaledInstance(500,350, Image.SCALE_DEFAULT)));
		}
		if (compareArrays(pizza,allToppings))
		{
			picture.setIcon(new ImageIcon(new ImageIcon("pizza pepper, pepperoni, mushroom.png").getImage().getScaledInstance(500,350, Image.SCALE_DEFAULT)));
		}
	}
	/**
	 * This generates the instructions panel
	 * pre: instructions button has been triggered
	 * post: gives you a nice instructions page with everything in it
	 */
	public static void instructionsPage() 
	{
		//Creates a null scanner for holding the reader file
		Scanner sc = null;
		
		//generates a gridbaglayout constraint to contain all the text in a organised fashion
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.insets =new Insets(5, 5, 5, 5);
		
		//imports the instructions text file
        File instruction = new File("instructions.txt");
        
        //read the instruction file
        try 
        {
        	sc = new Scanner(instruction);
        } 
        catch (FileNotFoundException e) 
        {
        	System.out.println("File not found");
        	System.err.println("FileNotFoundException: " + e.getMessage());
        }
        //creates a string array to contain the different lines of text
        String line[] = new String[7];
        int counter = 0; 
        //reads the file one line at a time and adds each line to an index in the array
        while (sc.hasNextLine()) 
        {
             	
      		line[counter] = sc.nextLine();
      		counter++;
        }
        sc.close();
		//creates JLabels to contain each row of text
		final JLabel[] instructionLabels = new JLabel[7];
		for (int i = 0; i < 7; i++)
		{
			instructionLabels[i] = new JLabel(line[i]);
			instructionLabels[i].setForeground(Color.WHITE);
			instructionLabels[i].setFont(titleFont);
		}
		//creates a back button
		final JButton backButton = new JButton("BACK");
		backButton.setFocusPainted(false);
		backButton.setContentAreaFilled(false);
		backButton.setForeground(Color.WHITE);
		backButton.setFont(buttonFont);
		
		//adds function to the back button. It will take you back to the home screen when you press on it
		backButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				highscorePanel.setVisible(false);
				instructionPanel.setVisible(false);
				introPanel.setVisible(true);	
			}
		});
		
		//Adds each instruction label to the screen
		for (int i = 0; i < 7; i++)
		{
			instructionPanel.add(instructionLabels[i], c);
			c.gridy++;
		}
		//Adds more padding so the backButton will be farther from the text
		c.insets =new Insets(20, 20, 20, 20); 
		instructionPanel.add(backButton, c);
		
		//Adds the frame to the instructions panel and sets it visible
		frame.add(instructionPanel);
		instructionPanel.setVisible(false);
		highScoresPanel.setVisible(false);
		gameOverPanel.setVisible(false);
		instructionPanel.setBackground(BACKGROUND_COLOUR);
		instructionPanel.setVisible(true);
		introPanel.setVisible(false);
	}
	/**
	 * Keeps track of the timer and the score of the game
	 * pre: user triggered the start button
	 * post: score saved and when timer goes to zero, the end game method is called
	 */
	public static void playGame() 
	{
		//This counter increases every millisecond, allowing events to be scheduled at precise times		
		final int[] counter = {0};
		//Creates a new timer and a new timeertask, which is the running of the game
		final Timer t = new Timer();
		int m = 50;
		TimerTask game = new TimerTask()
		{
			@Override
		    public void run() 
			{
				//Says Starting in x seconds for 3 seconds before the game starts
				if (counter[0]<4*m)
				{
					orderResponse.setText("Starting in ... " + (3-(counter[0]/m)));
				}
				//Enables all the buttons and sets a pizzaorder, so the game can begin
				if (counter[0]==(4*m))
				{
					sendButton.setEnabled(true);
					doughButton.setEnabled(true);
					orderResponse.setText(" ");
					orderText.setText(arrayToOrder(pizzaOrder));
				}
				//Allows the user to play for 60 seconds, while a timer counts down to 0
				if (counter[0]>=(4*m) && counter[0]<(64*m))
				{
					timeLabel.setText("Time: "+(64-(counter[0]/m)));
					scoreLabel.setText("Score: " + money.format(score[0]));
				}
				//After 60 seconds, the screen switches to the game-ending screen
				if (counter[0]>=(64*m) && counter[0]<(64*m+1))
				{
					endGame();
				}

				//Counter is incremented by one each time
				counter[0]++;	
			}	
		};
		//A timer with no delay is scheduled to go through its action every millisecond
		t.schedule(game, 0, 1); 
	}
	/**
	 * Removes the game panel and displays the game over panel
	 * pre: time ran out
	 * post: user gets to see their end score and have option to go to home screen
	 */
	public static void endGame() 
	{
		//Creates a custom gridbaglayout		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		//displays everything in the end screen
		JLabel endScreenLabel = new JLabel("GAME OVER");
		endScreenLabel.setFont(titleFont);
		endScreenLabel.setForeground(Color.WHITE);
		gamePanel.setVisible(false);
		frame.add(gameOverPanel);
		gameOverPanel.setVisible(true);
		gameOverPanel.add(endScreenLabel, c);
		c.gridy++;
		gameOverPanel.add(endScoreLabel, c);
		
		endScoreLabel.setText("Score: " + money.format(score[0]));
		endScoreLabel.setFont(textFont);
		endScoreLabel.setForeground(Color.WHITE);
		fileToArray();
		arrangeScores();
		c.gridy++;
		c.insets = new Insets(10,10,10,10);
		addHighScore(getPlayer(), score[0]); //dialog box wont stop coming
		arrangeScores();
		writeHighScoresToFile();
		updateLabels();	
	}
	/**
	 *Opens the highscore screen with all the contents in it
	 *pre: highscore button has been triggered
	 *post: user gets to see the highscores
	 */
	public static void openHighScoreScreen()
	{
		//Creates a JLabel which will say leaderboard		
		final JLabel leaderboard = new JLabel("Leaderboard");
		//Clears all elements on the screen, so repeatedly visiting this page won’t cause errors
		highScoresPanel.removeAll();
		//Convert the text file of high scores into a names and score array with corresponding valus
		fileToArray();
		//Arranges the scores and name arrays in order, and writes these new scores to the textfile
		arrangeScores();
		writeHighScoresToFile();	
		//Updates the labels with the text of the name and score
		updateLabels();
		//Creates a button that can send the user back to the homescreen
		final JButton backButton = new JButton("BACK");
		backButton.setFocusPainted(false);
		backButton.setContentAreaFilled(false);
		backButton.setForeground(Color.WHITE);
		backButton.setFont(buttonFont);
		//adds function to the back button. It will take you back to the home screen when you press on it
		backButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				highScoresPanel.setVisible(false);
				instructionPanel.setVisible(false);
				gameOverPanel.setVisible(false);
				frame.remove(instructionPanel);
				introPanel.setVisible(true);	
			}
		});
		//Sets gridBagConstraints for elements on the screen, then adds the title
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(15, 15, 15, 15);	
		c.gridwidth = 2;
		leaderboard.setForeground(Color.WHITE);
		leaderboard.setFont(titleFont);
		highScoresPanel.add(leaderboard, c);
		//Updates gridbag constraints for the names and scores
		c.gridy++;
		c.gridwidth = 1;
		c.insets = new Insets(5,5,5,5);
		//Goes through every label in the score and name array
		for (int i = 0; i < HIGH_SCORE_LIST_LENGTH; i++)
		{
			//Adds name labels to the panel
			c.gridx = 0;
			nameLabels[i].setForeground(Color.WHITE);
			nameLabels[i].setFont(textFont);
			highScoresPanel.add(nameLabels[i], c);
			//Adds the score labels to the panel
			c.gridx = 1;
			scoreLabels[i].setForeground(Color.WHITE);
			scoreLabels[i].setFont(textFont);
			highScoresPanel.add(scoreLabels[i], c);
			c.gridy++;
		}
		//Changes grid bag contraints again, then adds the backbutton
		c.gridx = 0;
		c.gridwidth = 2;
		c.insets =new Insets(15, 15, 15, 15);
		highScoresPanel.add(backButton, c);
		
		//Sets every panel except for the high scores panel to invisible and adds the highscorespanel
		introPanel.setVisible(false);
		gameOverPanel.setVisible(false);
		instructionPanel.setVisible(false);
		frame.add(highScoresPanel);
		highScoresPanel.setVisible(true);
		highScoresPanel.setBackground(BACKGROUND_COLOUR);
		frame.add(highScoresPanel);
		
		//Sets the values of the frame
		frame.setSize(800,560);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);	
		
	}

	/**
	*Creates a dialogue box that asks for name and returns it
	*pre: game has ended
	*post: returns a name
	*/
	public static String getPlayer() 
	{
		//Asks the user to enter their name and returns it as a string
		String name = JOptionPane.showInputDialog(null, "Enter your name: ");
		return name;
	}
	/**
	*Sorts contents of the file into 2 different arrays
	*pre: there is a file, there are less than 12 lines in the file
	*post: one array is full of name, and the other is full of scores
	*/
	public static void fileToArray()
	{
		String line = null;
		try 
		{
			//Creates specific objects for file reading
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			int counter = 0;
			//Uses the modulus function to place half of the line values into the name array, and half into the scores array
			while((line = bufferedReader.readLine()) != null && counter < 12) 
			{
				if (counter%2==0)
				{
					namesList[counter/2] = line;
				}
				else
				{
					//Double.parseInt converts the string in the text file into a double, or floating point number
					scoresList[counter/2] = Double.parseDouble(line);
				}
				counter++;
			}   
			bufferedReader.close();         
		}
		//Catches any exceptions or possible mistakes with filereading
		catch(FileNotFoundException ex) 
		{
			ex.printStackTrace();              
		}
		catch(IOException ex)
		{
			ex.printStackTrace();                  
		}
	}
	/**
	*Writes the score and the name to the highscore file
	*pre: there is a file to write on
	*post: name and score recorded on file
	*/
	public static void writeHighScoresToFile()
	{
		try 
		{
			//Creates file writing objects
			FileWriter fileWriter = new FileWriter(fileName);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			//Goes through each point in the array and adds each one as a new line into the text file
			for (int i = 0; i < scoresList.length && i < namesList.length; i++)
			{
				bufferedWriter.write(namesList[i]);
				bufferedWriter.newLine();
				bufferedWriter.write(Double.toString(scoresList[i]));
				bufferedWriter.newLine();
			}

			bufferedWriter.close();
		}
		//Catches any problems that occur during file writing
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	/**
	*arranges scores so that highest is at the top, and lowest is at the bottom
	*pre: there are scores
	*post: names are all sorted
	*/
	public static void arrangeScores()
	{		
		//Goes through each position in the array
		for (int i = 0; i <HIGH_SCORE_LIST_LENGTH; i++)
		{
			//Starts searching from the value right after the position number, and replaces it with the position number if it is larger. 
			for (int j = i+1; j < HIGH_SCORE_LIST_LENGTH; j++)
			{
				if (scoresList[j]>scoresList[i])
				{
					//A temp stores the value of the earlier position, then the second position replaces the early position, and the temp (or value of early position) replaces the second position
					double tempScore = scoresList[i];
					String tempName = namesList[i];
					scoresList[i] = scoresList[j];
					namesList[i] = namesList[j];
					scoresList[j] = tempScore;
					namesList[j] = tempName;
				}
			}
		}
	}
	/**
	* add new highscores to arrays
	* pre: the list must be in order, with the lowest value at the end
	* post: name and score stored to arrays if the list is not full
	*/
	public static void addHighScore(String name, double score)
	{
		//Temporarily sets the listFull variable to true
        boolean listFull = true;
		for (int i = 0; i < HIGH_SCORE_LIST_LENGTH; i++)
		{
			//If a score isn’t added (scores are originally 0), it means the list is not full, so the variable is set to false. The score and name is also added in the section
			if (scoresList[i]==0)
			{
				scoresList[i] = score;
				namesList[i] = name;
				listFull = false;
				break;
			}
		}
		//If the list is full, the new score is only added if it is higher than at least the lowest (or last) value
		if (listFull)
		{
			if (score > scoresList[HIGH_SCORE_LIST_LENGTH-1])
			{
				scoresList[scoresList.length-1] = score;
				namesList[scoresList.length-1] = name;
			}
		}
	}
	/**
	*Updates the leaderboard labels to display updated organised scores
	*pre: has to be a name with a new high score
	*post: labels are updated
	*/
	public static void updateLabels()
	{
		//Goes through each label in both label arrays and sets their values to be the same as the values in the main arrays. (So the name label 1, would have the first name in the name array)
		for (int i = 0; i < HIGH_SCORE_LIST_LENGTH; i++)
		{
			//generates labels
			nameLabels[i] = new JLabel(namesList[i]);
			scoreLabels[i] = new JLabel(money.format(scoresList[i]));
		}
	}
}

