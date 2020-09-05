import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;

import javax.swing.*;
import java.text.NumberFormat;
import java.util.Scanner;

import net.miginfocom.swing.MigLayout;

public class HighScoresPage
{
	final static int HIGH_SCORE_LIST_LENGTH = 6;
	final static Color BACKGROUND_COLOUR = new Color(66, 88, 178);
	static String[] namesList = new String[HIGH_SCORE_LIST_LENGTH];
	static double[] scoresList = new double[HIGH_SCORE_LIST_LENGTH];
	static String fileName = "HighScores.txt";
	static boolean listFull = false;
	static int listLength = 0;
	static JLabel[] nameLabels = new JLabel[HIGH_SCORE_LIST_LENGTH];
	static JLabel[] scoreLabels = new JLabel[HIGH_SCORE_LIST_LENGTH];
	static NumberFormat money = NumberFormat.getCurrencyInstance();
	
	public static void main(String[] args) 
	{
		JFrame frame = new JFrame("your");
		JPanel highScoresPanel = new JPanel(new MigLayout("insets 50"));
		JLabel leaderboard = new JLabel("Leaderboard");
		fileToArray();
		//arrangeScores();
		//addHighScore(getPlayer(), 110); TODO
		arrangeScores();
		writeHighScoresToFile();	
		updateLabels();
		highScoresPanel.add(leaderboard, "cell 0 0 2 1");
		for (int i = 0; i < HIGH_SCORE_LIST_LENGTH; i++)
		{
			highScoresPanel.add(nameLabels[i], "cell 0 "+ i+1);
			highScoresPanel.add(scoreLabels[i], "cell 1 "+ i+1);
		}
		highScoresPanel.setVisible(true);
		highScoresPanel.setBackground(BACKGROUND_COLOUR);
		frame.add(highScoresPanel);
		frame.setSize(800,560);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);	
	}
	public static String getPlayer() 
	{
		String name = JOptionPane.showInputDialog(null, "Enter your name: ");
		return name;
	}
	public static void fileToArray()
	{
		String line = null;
		try 
		{
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			int counter = 0;
			while((line = bufferedReader.readLine()) != null && counter < 12) 
			{
				if (counter%2==0)
				{
					namesList[counter/2] = line;
				}
				else
				{
					scoresList[counter/2] = Double.parseDouble(line);
				}
				counter++;
			}   
			bufferedReader.close();         
		}
		catch(FileNotFoundException ex) 
		{
			ex.printStackTrace();              
		}
		catch(IOException ex)
		{
			ex.printStackTrace();                  
		}
	}
	public static void writeHighScoresToFile()
	{
		try 
		{
			FileWriter fileWriter = new FileWriter(fileName);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			for (int i = 0; i < scoresList.length && i < namesList.length; i++)
			{
				bufferedWriter.write(namesList[i]);
				bufferedWriter.newLine();
				bufferedWriter.write(Double.toString(scoresList[i]));
				bufferedWriter.newLine();
			}

			bufferedWriter.close();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	public static void arrangeScores()
	{		
		for (int i = 0; i <HIGH_SCORE_LIST_LENGTH; i++)
		{
			for (int j = i+1; j < HIGH_SCORE_LIST_LENGTH; j++)
			{
				if (scoresList[j]>scoresList[i])
				{
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
	public static void addHighScore(String name, int score)
	{
		listFull = true;
		for (int i = 0; i < HIGH_SCORE_LIST_LENGTH; i++)
		{
			if (scoresList[i]==0)
			{
				scoresList[i] = score;
				namesList[i] = name;
				listFull = false;
				break;
			}
		}
		if (listFull)
		{
			if (score > scoresList[HIGH_SCORE_LIST_LENGTH-1])
			{
				scoresList[scoresList.length-1] = score;
				namesList[scoresList.length-1] = name;
			}
		}
	}
	public static void updateLabels()
	{
		for (int i = 0; i < HIGH_SCORE_LIST_LENGTH; i++)
		{
			nameLabels[i] = new JLabel(namesList[i]);
			scoreLabels[i] = new JLabel(money.format(scoresList[i]));
		}
	}

}
