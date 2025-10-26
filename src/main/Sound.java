package main;

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/**
 * The class is responsible for managing all audio playback within the game.
 */
public class Sound {

    Clip clip;
    URL[] soundURL = new URL[30];
    FloatControl fc;
    int volumeScale = 3;
    float volume;

    /**
     * Constructs a new sound object.
     */
    public Sound() {
        soundURL[0] = getClass().getResource("/res/sound/music.wav");
        soundURL[1] = getClass().getResource("/res/sound/key.wav");
        soundURL[2] = getClass().getResource("/res/sound/door.wav");
        soundURL[3] = getClass().getResource("/res/sound/monsterhit.wav");
        soundURL[4] = getClass().getResource("/res/sound/playerhit.wav");
        soundURL[5] = getClass().getResource("/res/sound/sword.wav");
        soundURL[6] = getClass().getResource("/res/sound/spiritSE.wav");
        soundURL[7] = getClass().getResource("/res/sound/gameover.wav");
        soundURL[8] = getClass().getResource("/res/sound/chest.wav");
        soundURL[9] = getClass().getResource("/res/sound/click.wav");
        soundURL[10] = getClass().getResource("/res/sound/dia0.wav");
        soundURL[11] = getClass().getResource("/res/sound/dia1.wav");
        soundURL[12] = getClass().getResource("/res/sound/dia2.wav");
        soundURL[13] = getClass().getResource("/res/sound/dia3.wav");
    }

    /**
     * Loads a sound file.
     * 
     * @param i the index of the sound file
     */
    public void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
            fc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            checkVolume();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void play() {
        clip.start();
    }

    @SuppressWarnings("static-access")
    public void loop() {
        clip.loop(clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
    }

    /**
     * Adjust the playback volume based on the current volumeScale.
     */
    public void checkVolume() {
        switch (volumeScale) {
            case 0:
                volume = -80f;
                break;
            case 1:
                volume = -20f;
                break;
            case 2:
                volume = -12f;
                break;
            case 3:
                volume = -5f;
                break;
            case 4:
                volume = 1f;
                break;
            case 5:
                volume = 6f;
                break;
            default:
                break;
        }
        fc.setValue(volume);
    }

}
