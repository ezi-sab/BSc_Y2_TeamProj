package view;

import java.nio.file.Paths;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundManager {
	
	static Media bgm = new Media(Paths.get("src/view/resources/sounds/spaceinvaders1.mp3").toUri().toString());
	
	static MediaPlayer mediaPlayerBgm = new MediaPlayer(bgm);
	MediaPlayer mediaPlayerMenu;
	MediaPlayer mediaPlayerCoin;
	MediaPlayer mediaPlayerEnemyDead;
	MediaPlayer mediaPlayerExplosion;
	
	static boolean backGroundMusic = true;
	static boolean inGameMusic = true;
	
	public void playBackGroundMusic() {
		
		if (backGroundMusic == true) {
			
			mediaPlayerBgm.setCycleCount(MediaPlayer.INDEFINITE);
			mediaPlayerBgm.play();
			backGroundMusic = true;
			
		} else if (backGroundMusic == false) {
			
			mediaPlayerBgm.pause();
			backGroundMusic = false;
			
		}
		
	}
	
	public void changeBackGroundMusic(boolean music) {
		
		backGroundMusic = music;
		
	}
	
	public boolean isBackGroundMusic() {
		
		return backGroundMusic;
		
	}
	
	public void playMenuOpenMusic() {
		
		Media menuMusic = new Media(Paths.get("src/view/resources/sounds/fastinvader1.mp3").toUri().toString());
		MediaPlayer mediaPlayerMenu = new MediaPlayer(menuMusic);
		mediaPlayerMenu.setAutoPlay(true);
		
	}
	
	public void playCoinCollectMusic() {
		
		if(inGameMusic == true) {
			
			Media coin = new Media(Paths.get("src/view/resources/sounds/Pokemon-(Button).mp3").toUri().toString());
			MediaPlayer mediaPlayerCoin = new MediaPlayer(coin);
			mediaPlayerCoin.setAutoPlay(true);
			
		}
		
	}
	
	public void playEnemyDeadMusic() {
		
		if(inGameMusic == true) {
			
			Media enemyDead = new Media(Paths.get("src/view/resources/sounds/explosion.mp3").toUri().toString());
			MediaPlayer mediaPlayerEnemyDead = new MediaPlayer(enemyDead);
			mediaPlayerEnemyDead.play();
			
		}
		
	}
	
	public void playPlayerDeadMusic() {
		
		if(inGameMusic == true) {
			
			Media playerDead = new Media(Paths.get("src/view/resources/sounds/Roblox-death-sound.mp3").toUri().toString());
			MediaPlayer mediaPlayerExplosion = new MediaPlayer(playerDead);
			mediaPlayerExplosion.play();
			
		}
		
	}
	
	public void changeInGameMusic(boolean music) {
		
		inGameMusic = music;
		
	}
	
	public boolean isInGameMusic() {
		
		return inGameMusic;
	}

}
