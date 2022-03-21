package pong;

import java.io.BufferedInputStream;
import java.io.InputStream;
import javax.sound.sampled.*;


/**
 * Manages game sounds.
 */
public class Sound {
		
	private static String name;
				
	public static void play(int soundCode) {
		new Thread(new Runnable() {
			public void run() {
				try {
					// obtains a clip to play a sound  
					Clip clip = AudioSystem.getClip();
					if (soundCode == 0) {
						name = "hit_paddle.wav";
					} else if (soundCode == 1) {
						name = "hit_obstacle.wav";
					} else if (soundCode == 2){
						name = "point_marked.wav";
					}
					//add buffer for mark/reset support
					if (Sound.class.getResourceAsStream(name) == null) {
						System.out.println("didnt find out");
					}
					InputStream bufferedInput = new BufferedInputStream(Sound.class.getResourceAsStream(name));
					// specifies the desired audio data to inputStream
					AudioInputStream inputStream = AudioSystem.getAudioInputStream(bufferedInput);
					// opens the clip with the format and audio data present in the provided audio inputStream
					clip.open(inputStream); 
					clip.start(); 
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start();
	}
}
