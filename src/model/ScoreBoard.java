package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class ScoreBoard {
	
	private FileWriter writer;
	private BufferedWriter bufferedWritter;
	
	private FileReader reader;
	private BufferedReader bufferedReader;
	
	private static VBox scoreContainer = new VBox();
	
	private int[] scoreArray;
	private String[] scoreStringArray = new String [3];
	private int count = 0;
	
	public void writeScore(int points) {
		
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
	
	public void readScore() {
		
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
						
	}
	
	public void setScoreVBox() {
		
		readScore();
		
		scoreContainer.setLayoutX(150);
		scoreContainer.setLayoutY(150);
		
		Label scoreHeading = new Label("Name			Score ");
		scoreHeading.setUnderline(true);
		Label score1 = new Label("Ship 1" + scoreStringArray[0]);
		Label score2 = new Label("Ship 2" + scoreStringArray[1]);
		Label score3 = new Label("Ship 3" + scoreStringArray[2]);
		scoreHeading.setFont(Font.font("Verdana",20));
		score1.setFont(Font.font("Verdana",20));
		score2.setFont(Font.font("Verdana",20));
		score3.setFont(Font.font("Verdana",20));
		scoreContainer.setBackground(new Background(new BackgroundFill(Color.MEDIUMAQUAMARINE, new CornerRadii(20), new Insets(-20,-20,-20,-20))));
		scoreContainer.getChildren().add(scoreHeading);
		scoreContainer.getChildren().add(score1);
		scoreContainer.getChildren().add(score2);
		scoreContainer.getChildren().add(score3);
		
	}
	
	public void ClearVBox() {
			
		scoreContainer.getChildren().removeAll(scoreContainer.getChildren());		
		
	}
	
	public VBox getScoreVBox() {
		
		return scoreContainer;
		
	}
	
}
