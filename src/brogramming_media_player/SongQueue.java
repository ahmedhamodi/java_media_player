/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brogramming_media_player;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;

/**
 *
 * @author salil
 */
public class SongQueue {
    private long numSongs;
    private JFileChooser fileChooser = new JFileChooser();
    private ArrayList<String> songList = new ArrayList<>();
    File[] dir;
    
    public SongQueue() {
        addSongs();
    }
    
    private void addSongs() {
        File f = new File("music");
        dir = f.listFiles();
//        for (int i = 0; i < dir.length; i++) {
//            System.out.println(dir[i]);
//        }
    }
    
    public String getNextSong(int pos) {
//        System.out.println(dir[pos].toString());
        return dir[pos].getAbsolutePath();
    }
    
    public int getNumFiles() {
        return dir.length;
    }
    
    public int getPos(String fName) {
        int pos = 0;
        for (int i = 0; i < dir.length; i++) {
            if(fName.equals(dir[i].getAbsolutePath())) {
                pos = i;
            }
        }

        return pos;
    }
}
