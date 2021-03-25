package sound;

import java.nio.file.Paths;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundManager {
	
	Media bgm = new Media(Paths.get("src/sound/resources/spaceinvaders1.mp3").toUri().toString());
	
	MediaPlayer mediaPlayerBgm = new MediaPlayer(bgm);
	MediaPlayer mediaPlayerMenu;
	MediaPlayer mediaPlayerCoin;
	MediaPlayer mediaPlayerEnemyDead;
	MediaPlayer mediaPlayerExplosion;
	
	boolean backGroundMusic;
	
	public void playBackGroundMusic(boolean music) {
		
		if (music == true) {
			mediaPlayerBgm.setCycleCount(MediaPlayer.INDEFINITE);
			mediaPlayerBgm.play();
			backGroundMusic = true;
		} else if (music == false) {
			mediaPlayerBgm.pause();
			backGroundMusic = false;
		}
		
	}
	
	public boolean isBackGroundMusic() {
		
		return backGroundMusic;
		
	}
	
	public void playMenuOpenMusic() {
		
		Media menuMusic = new Media(Paths.get("src/sound/resources/fastinvader1.mp3").toUri().toString());
		MediaPlayer mediaPlayerMenu = new MediaPlayer(menuMusic);
		mediaPlayerMenu.setAutoPlay(true);
		
	}
	
	public void playCoinCollectMusic() {
		
		Media coin = new Media(Paths.get("src/sound/resources/Pokemon-(Button).mp3").toUri().toString());
		MediaPlayer mediaPlayerCoin = new MediaPlayer(coin);
		mediaPlayerCoin.setAutoPlay(true);
		
	}
	
	public void playEnemyDeadMusic() {
		
		Media enemyDead = new Media(Paths.get("src/sound/resources/Roblox-death-sound.mp3").toUri().toString());
		MediaPlayer mediaPlayerEnemyDead = new MediaPlayer(enemyDead);
		mediaPlayerEnemyDead.play();
		
	}
	
	public void playExplosionMusic() {
		
		Media explosion = new Media(Paths.get("src/sound/resources/explosion.mp3").toUri().toString());
		MediaPlayer mediaPlayerExplosion = new MediaPlayer(explosion);
		mediaPlayerExplosion.play();
		
	}

}
