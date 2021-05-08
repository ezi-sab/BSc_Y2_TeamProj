package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Pair;

public class ScoreBoard {

	private FileWriter writer;
	private BufferedWriter bufferedWriter;

	private FileReader reader;
	private BufferedReader bufferedReader;

	private static VBox scoreContainer = new VBox();

	private List<Pair<String, Integer>> playerScore = new ArrayList<Pair<String, Integer>>();
	private String[] playerScoreStringArray = new String [3];
	private int count = 0;

	/**
	 * Function that write the inputted names and scores in text files for further display.
	 * @param name Writes the names in the text files using BufferedWriter.
	 * @param points Writes the scores in the text files using BufferedWriter.
	 */
	public void writeScore(String name, int points) {

		try {

			writer = new FileWriter("src/view/resources/Scores.txt", true);
			bufferedWriter = new BufferedWriter(writer);
			bufferedWriter.write(name + " " + points);
			bufferedWriter.newLine();

			bufferedWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Reads the names from the stored text file for displaying in the Scores section.
	 * Reads the scores from the stored text file for displaying in the Scores section.
	 */
	public void readNamesandScores() {

		try {

			reader = new FileReader("src/view/resources/Scores.txt");
			bufferedReader = new BufferedReader(reader);
			while (bufferedReader.readLine() != null) {

				count++;

			}

			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		String[] nameArray = new String[3];
		int[] scoreArray = new int[count];

		try {

			reader = new FileReader("src/view/resources/Scores.txt");
			bufferedReader = new BufferedReader(reader);

			String line;
			String playerName;
			String[] lineArray;

			int i = 0;

			while ((line = bufferedReader.readLine()) != null) {

				lineArray = line.split(" ");
				playerName = lineArray[0];

				if (lineArray.length > 2) {
					for (int k = 1; k < lineArray.length - 1; k++) {
						playerName = playerName + " " + lineArray[k];
					}
				}

				playerScore.add(new Pair<String, Integer>(playerName, Integer.valueOf(lineArray[lineArray.length - 1])));
				scoreArray[i] = Integer.valueOf(lineArray[lineArray.length - 1]);
				i++;

			}

			reader.close();

		} catch (IOException | NumberFormatException e) {
			e.printStackTrace();
		}

		Arrays.sort(scoreArray);

		for (int j = 0; j < scoreArray.length / 2; j++) {

			int temp = scoreArray[j];
			scoreArray[j] = scoreArray[scoreArray.length -j -1];
			scoreArray[scoreArray.length -j -1] = temp;

		}

		int k = 0;


		while(k < 3) {

			for (int l = 0;l < playerScore.size();l++) {
				if (scoreArray[k] == playerScore.get(l).getValue()) {
					nameArray[k] = playerScore.get(l).getKey();
					playerScore.remove(new Pair<String, Integer>(playerScore.get(l).getKey(), playerScore.get(l).getValue()));
					break;
				}
			}


			k++;

		}

		for (int l = 0; l < 3; l++) {

			String name = nameArray[l];
			String score = "" + scoreArray[l];

			if (name.length() > 10) {
				name = nameArray[l].substring(0, 10);
			}


			String alignedName = padRight(name, 10);
			String alignedScore =  padLeft(score, 14);

			playerScoreStringArray[l] = (alignedName + alignedScore);

		}

	}

	/**
	 * A Right indented formatted string is returned without additional spaces.
	 * @param s String that should be formatted and padded in the space accordingly.
	 * @param n number of spaces that should be added to the right.
	 * @return String after formatted and padded without additional spaces.
	 */
	public String padRight(String s, int n) {
		return String.format("%-" + n + "s", s);
	}

	/**
	 * A Left indented formatted string is returned without additional spaces.
	 * @param s String that should be formatted and padded in the space accordingly.
	 * @param n number of spaces that should be added to the left.
	 * @return String after formatted and padded without additional spaces.
	 */
	public String padLeft(String s, int n) {
		return String.format("%" + n + "s", s);
	}

	/**
	 * Sets the Scores Box to display the player in Scores section.
	 */
	public void setScoreVBox() {

		readNamesandScores();

		scoreContainer.getChildren().removeAll(scoreContainer.getChildren());

		scoreContainer.setLayoutX(125);
		scoreContainer.setLayoutY(150);

		Label scoreHeading = new Label("Name			   Score ");
		scoreHeading.setUnderline(true);

		Label score1 = new Label(playerScoreStringArray[0]);
		Label score2 = new Label(playerScoreStringArray[1]);
		Label score3 = new Label(playerScoreStringArray[2]);

		scoreHeading.setFont(Font.font("Verdana",23));
		score1.setFont(Font.font("MONOSPACED", FontWeight.BOLD, 24));
		score2.setFont(Font.font("MONOSPACED", FontWeight.BOLD, 24));
		score3.setFont(Font.font("MONOSPACED", FontWeight.BOLD, 24));

		scoreContainer.setBackground(new Background(new BackgroundFill(Color.MEDIUMAQUAMARINE, new CornerRadii(20), new Insets(-20,-20,-20,-20))));

		scoreContainer.getChildren().add(scoreHeading);
		scoreContainer.getChildren().add(score1);
		scoreContainer.getChildren().add(score2);
		scoreContainer.getChildren().add(score3);

	}

	/**
	 * Gets the VBox scoreContainer.
	 * @return scoreContainer from setScoreVBox()
	 */
	public VBox getScoreVBox() {

		return scoreContainer;

	}


}
