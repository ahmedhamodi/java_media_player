/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brogramming_media_player;

/**
 *
 * @author rsweet
 */
public class Runner extends Thread {

    Audio audio = new Audio();

    public Runner() {

    }

    public void setAudioPlayer(Audio selected) {
        audio = selected;
    }

    @Override
    public void run() {
        audio.playSound();
    }

}
