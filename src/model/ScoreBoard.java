package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class ScoreBoard {
	
	private static FileWriter writer;
	private static BufferedWriter bufferedWritter;
	
	private static FileReader reader;
	private static BufferedReader bufferedReader;
	
	private static int[] scoreArray;
	private static String[] scoreStringArray = new String [3];
	private static int count = 0;
	
	public static void writeScore(int points) {
		
		try {
			
			writer = new FileWriter("src/view/resources/Scores.txt", true);
			bufferedWritter = new BufferedWriter(writer);
			bufferedWritter.write("score " + points);
			bufferedWritter.newLine();
			
			bufferedWritter.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static String[] readScore() {
		
		try {
			
			reader = new FileReader("src/view/resources/Scores.txt");
			bufferedReader = new BufferedReader(reader);
			while (bufferedReader.readLine() != null) {
				
				count++;
				
			}
			
			reader.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			
			reader = new FileReader("src/view/resources/Scores.txt");
			bufferedReader = new BufferedReader(reader);
			scoreArray = new int[count];
			String line;
			String[] lineArray;
			int i = 0;
			
			while ((line = bufferedReader.readLine()) != null) {
				
				lineArray = line.split(" ");
				scoreArray[i] = Integer.valueOf(lineArray[1]);
				i++;
				
			}
			
			reader.close();
			
		} catch (IOException | NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Arrays.sort(scoreArray);
		
		for (int j = 0; j < scoreArray.length / 2; j++) {
			
			int temp = scoreArray[j];
			scoreArray[j] = scoreArray[scoreArray.length -j -1];
			scoreArray[scoreArray.length -j -1] = temp;
			
		}
		
		for (int k = 0; k < 3; k++) {
			
			if (scoreArray[k] >= 0 && scoreArray[k] <= 9) {
				
				scoreStringArray[k] = "		            " + scoreArray[k];
				
			} else if (scoreArray[k] >= 10 && scoreArray[k] <= 99) {
				
				scoreStringArray[k] = "		           " + scoreArray[k];
				
			} else if (scoreArray[k] >= 100 && scoreArray[k] <= 999) {
				
				scoreStringArray[k] = "		          " + scoreArray[k];
				
			} else {
				
				scoreStringArray[k] = "		         " + scoreArray[k];
				
			}
			
		}
				
		return scoreStringArray;
		
	}
	
}
