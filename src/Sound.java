

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {
	static boolean isBGMusicPlaying = false;
	static Clip clip;
	public static void RunBGMusic(String path) {
		try {
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(path));
			clip = AudioSystem.getClip();
			clip.open(inputStream);
			clip.loop(0);
			isBGMusicPlaying = true;
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	public static void StopBGMusic() { 
		clip.stop();
		isBGMusicPlaying = false;
	}
	public static boolean IsBGMusicPlaying() {
        return isBGMusicPlaying;
    }
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
