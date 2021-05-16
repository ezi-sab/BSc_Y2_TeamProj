package view;

import javafx.embed.swing.JFXPanel;
import org.junit.Test;

import static org.junit.Assert.*;

public class ShipModelTest {
    private JFXPanel jpanel = new JFXPanel();

    @Test
    public void testSetGameGrid() {
        ShipModel.CellValue testCellValue = ShipModel.CellValue.EMPTY;
        ShipModel.CellValue [][]  testGameGrid = new ShipModel.CellValue[2][2];
        for (int i = 0; i < 2; i++) {
            for(int j = 0; j < 2 ; j++) {
                testGameGrid[i][j] = ShipModel.CellValue.EMPTY;
            }
        }
        ShipModel.setGameGrid(testGameGrid);

        assertEquals(testCellValue, ShipModel.getGameGrid()[1][1]);
    }
}