/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brogramming_media_player;

import javax.sound.sampled.*;
import java.io.*;
import javazoom.jl.player.Player;
import java.io.FileInputStream;
import javazoom.jl.decoder.JavaLayerException;

/**
 *
 * @author salil
 */
public class Audio {

    private String filePath;
    private File fileName;
    private InputStream in;
    private static final int BUFFER_SIZE = 4096;

    public Audio() {

    }

    public void setFilePath(String fP) {
        filePath = fP;
        fileName = new File(filePath);
    }

    public File getFile() {
        return fileName;

    }

    public void playSound() {
        if (fileName.toString().endsWith(".wav")) {
            try {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(fileName);
                AudioFormat format = audioStream.getFormat();

                DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

                SourceDataLine audioLine = (SourceDataLine) AudioSystem.getLine(info);

                audioLine.open(format);

                audioLine.start();

                byte[] bytesBuffer = new byte[BUFFER_SIZE];
                int bytesRead = -1;

                while ((bytesRead = audioStream.read(bytesBuffer)) != -1) {
                    audioLine.write(bytesBuffer, 0, bytesRead);
                }

                audioLine.drain();
                audioLine.close();
                audioStream.close();
            } catch (UnsupportedAudioFileException e) {
                System.out.println("");
            } catch (IOException e) {
                System.err.println(e.getMessage());
                System.out.println("IO Exception");
            } catch (LineUnavailableException e) {
                System.err.println(e.getMessage());
                System.out.println("Line unavailable");
            } catch (NullPointerException e) {
                System.err.println("Null Pointer Exception");
            }
        } else if (fileName.toString().endsWith(".mp3")) {
            try {
                FileInputStream file = new FileInputStream(fileName);
                Player playMP3 = new Player(file);
                playMP3.play();
            } catch (FileNotFoundException e) {

            } catch (JavaLayerException e) {

            }
        }
    }

    public void stopSound() {

    }

    public float getSoundTime() {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(fileName);
            AudioFormat format = audioStream.getFormat();
            long audioFileLength = fileName.length();
            System.out.println("Audio File Length: " + audioFileLength);
            int frameSize = format.getFrameSize();
            System.out.println("Frame Size: " + frameSize);
            float frameRate = format.getFrameRate();
            System.out.println("Frame Rate: " + frameRate);
            float durationInSeconds = (audioFileLength / (frameSize * frameRate));
            return durationInSeconds;
        } catch (UnsupportedAudioFileException e) {
            System.err.println("Unsupported Audio File");
        } catch (IOException e) {
            System.err.println("IO Exception Occured.");
        }
        float error = 0;
        return error;
    }
}
