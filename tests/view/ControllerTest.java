package view;

import javafx.embed.swing.JFXPanel;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ControllerTest {
    private final JFXPanel jpanel = new JFXPanel();

    @Test
    public void testGetCurrentLevel() {
        try {
            String value = "src/levels/level2.txt";
            assertEquals(value, Controller.getCurrentLevel(1));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Couldn't fetch the level file");
            System.out.println("Error: Detecting the level file.");
        }
    }


    @Test
    public void testStartNewGame() {
        int testScore = 0;
        int testLevel = 0;
        int testSmallDot = 0;
        int testStep = 0;
        int[] testInitialValues = new int[4];
        testInitialValues[0] = testScore;
        testInitialValues[1] = testLevel;
        testInitialValues[2] = testSmallDot;
        testInitialValues[3] = testStep;
        try {
            assertEquals(testInitialValues[0], Controller.score);
        } catch (Exception e) {
            System.out.println("Error: Starting the game.");
            fail("Error while starting the game");
        }

    }


    @Test
    public void testIsLevelComplete() {
        boolean testGame = false;
        assertEquals(testGame, Controller.levelComplete);
        assertTrue("Game level finished successfully", testGame = true);
    }
}