package view;

import org.junit.Test;
import javafx.embed.swing.JFXPanel;

import static org.junit.Assert.*;

public class SoundManagerTest {
    private final JFXPanel jpanel = new JFXPanel();
    @Test
    public void testSetBGMVolumeBeforeGame() {
        try {
            double bgmVolumeBeforeGame = 0.9;
            SoundManager.setBGMVolumeBeforeGame(0.9);
            assertEquals(bgmVolumeBeforeGame, SoundManager.getBGMVolumeBeforeGame(), 0);
        } catch (Exception e) {
            fail("Cannot set the volume");
            System.out.println("desired volume can't be set.");
        }

    }

    @Test
    public void testGetInGameMusicVolume() {
        try {
            double inGameVolume = 0.9;
            SoundManager.setInGameMusicVolume(0.9);
            assertEquals(inGameVolume, SoundManager.getInGameMusicVolume(), 0);
        } catch (Exception e) {
            fail("Cannot set the volume");
            System.out.println("desired volume can't be set.");
        }

    }
}