package brogramming_media_player;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.*; //.JFrame, .JLabel, .JPanel, etc
import java.awt.event.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Ahmed, Connor, and Salil
 */
public class MediaPlayerGUI extends MouseAdapter implements ActionListener, ChangeListener {

    JFrame frame;
    JLabel timeStamp = new JLabel();
    JPanel mainCP, contentPane0, contentPane1, contentPane2, contentPane3, contentPane4;
    JButton playSong, nextSong, prevSong, shuffle, selectSong; //add functionality for reset/generateLife
    JSlider volumeControl = new JSlider(0, 200, 100);
    JComboBox<String> speedControl = new JComboBox<>();
    AudioProgress duration = new AudioProgress();
    private int volume = 100, speed = 1000, mSpeed = 1;
    private String min = "0", sec = "00", songMin = "0", songSec = "00";
    private int calcMin = 0, calcSec = 0, calcSongMin = 0, calcSongSec = 0;
    private static Timer time;
    private int timeInMS, songTIS;
    private double timeInSeconds;
    private String dir = "";
    private int songQ = 0;
    private SongQueue songQueue = new SongQueue();
    private boolean shuffleOn = false;

    Chooser choose = new Chooser();
    Audio player = new Audio();
    Runner songThread = new Runner();

    /**
     * Constructor pre: none post: none
     */
    public MediaPlayerGUI() {
        /* Create and set up the frame */
        frame = new JFrame("Media Player");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /* Create the main content pane */
        mainCP = new JPanel();
        mainCP.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainCP.setBackground(Color.gray);
        mainCP.setLayout(new BoxLayout(mainCP, BoxLayout.PAGE_AXIS));

        /* Create the supporting content panes:
        cp0 = time stamp
        cp1 = visual
        cp2 = prev, play and next song buttons
        cp3 = shuffle and select song buttons
        cp4 = slider for volume, combo box for speed
        */
        contentPane0 = new JPanel();
        contentPane0.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        contentPane1 = new JPanel();
        contentPane1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        contentPane2 = new JPanel();
        contentPane2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane2.setLayout(new FlowLayout());

        contentPane3 = new JPanel();
        contentPane3.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane3.setLayout(new FlowLayout());

        contentPane4 = new JPanel();
        contentPane4.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane4.setLayout(new FlowLayout());

        /* Creates  a JLabel that displays the time of the song */
        timeStamp.setText("0:00 / 0:00");
        contentPane0.add(timeStamp);
        
        /*Slider to adjust song time*/
//        contentPane0.add(duration);

        /* create and add the buttons for user interactions */
        prevSong = new JButton("Previous Song");
        prevSong.addActionListener(this);
        prevSong.setActionCommand("prevSong");
        prevSong.setPreferredSize(new Dimension(120, 20));
        prevSong.setBackground(Color.cyan);
        prevSong.setForeground(Color.blue);
        contentPane2.add(prevSong);

        playSong = new JButton("Play");
        playSong.addActionListener(this);
        playSong.setActionCommand("play");
        playSong.setPreferredSize(new Dimension(120, 20));
        playSong.setBackground(Color.cyan);
        playSong.setForeground(Color.blue);
        contentPane2.add(playSong);

        nextSong = new JButton("Next Song");
        nextSong.addActionListener(this);
        nextSong.setActionCommand("nextSong");
        nextSong.setPreferredSize(new Dimension(120, 20));
        nextSong.setBackground(Color.cyan);
        nextSong.setForeground(Color.blue);
        contentPane2.add(nextSong);

        shuffle = new JButton("Shuffle = Off");
        shuffle.addActionListener(this);
        shuffle.setActionCommand("shuffle");
        shuffle.setPreferredSize(new Dimension(120, 20));
        shuffle.setBackground(Color.green);
        shuffle.setForeground(Color.black);
        contentPane3.add(shuffle);

        selectSong = new JButton("Select Song");
        selectSong.addActionListener(this);
        selectSong.setActionCommand("selectSong");
        selectSong.setPreferredSize(new Dimension(120, 20));
        selectSong.setBackground(Color.green);
        selectSong.setForeground(Color.black);
        contentPane3.add(selectSong);

        /* Create slider for changing volume of video */
        volumeControl.setMajorTickSpacing(50);
        volumeControl.setMinorTickSpacing(25);
        volumeControl.setPaintTicks(true);
        volumeControl.setPaintLabels(true);
        volumeControl.setSnapToTicks(true);
        volumeControl.addChangeListener(this);
        volumeControl.setForeground(Color.black);
        volumeControl.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        contentPane4.add(volumeControl);
        
        /* Create combobox for speed control*/
        speedControl.addItem("0.5x");
        speedControl.addItem("1.0x");
        speedControl.addItem("1.5x");
        speedControl.addItem("2.0x");
        speedControl.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 0));
        speedControl.setSelectedIndex(1);
        contentPane4.add(speedControl);
        
        /* Set up timer for tracking the location in the song */
        time = new Timer(mSpeed, new gameTimer());
        time.stop();

        /* Add content panes to the main content pane */
        mainCP.add(contentPane0);
        mainCP.add(contentPane1);
        mainCP.add(contentPane2);
        mainCP.add(contentPane3);
        mainCP.add(contentPane4);

        /* Add content pane to frame */
        frame.setContentPane(mainCP);

        /* Size and then display the frame. */
//        frame.setSize(900, 900);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

    public static void runGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);

        MediaPlayerGUI project = new MediaPlayerGUI();
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(MediaPlayerGUI::runGUI);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
     public void actionPerformed(ActionEvent e) {
        int temp = 0;
        String eventName = e.getActionCommand();
        switch (eventName) {
            case "play":
                timeInSeconds = 0;
                timeStamp.setText("0:00 / 0:00");
                if(songQueue.getNumFiles() > 0) {
                    time.start();
                    playSong.setActionCommand("pause");
                    playSong.setText("Pause");
                    if (!dir.equals("")) {
                        songThread.start();
                    } else {
                        if(shuffleOn) {
                            do {
                                temp = (int)(Math.random() * (songQueue.getNumFiles())); //generate number between 1 and 6
//                                System.out.println(temp);
                            }while (temp == songQ);

                            songQ = temp;
                        }
                        player.setFilePath(songQueue.getNextSong(songQ));
                        if (songThread.isAlive()) {
                            songThread.stop();
                        }
                        songThread = new Runner();
                        songThread.setAudioPlayer(player);
                        songThread.start();

                        time.start();
                        playSong.setActionCommand("pause");
                        playSong.setText("Pause");
                    }
                }
                break;
            case "pause":
                time.stop();
                playSong.setActionCommand("resume");
                playSong.setText("Resume");
                songThread.suspend();
                break;
            case "resume":
                time.start();
                playSong.setActionCommand("pause");
                playSong.setText("Pause");
                songThread.resume();
                break;
            case "nextSong":
                timeInSeconds = 0;
                timeStamp.setText("0:00 / 0:00");
                //go forward 1 song from directory
                if(songQueue.getNumFiles() > 0) {
                    time.stop();
                    if(shuffleOn) {
                        do {
                            temp = (int)(Math.random() * (songQueue.getNumFiles())); //generate number between 1 and 6
                        }while (temp == songQ);

                        songQ = temp;
                    } else {
                        songQ += 1;
                    }

                    if(songQ >= songQueue.getNumFiles()) {
                        songQ = 0;
                    }
                    player.setFilePath(songQueue.getNextSong(songQ));
    //                System.out.println(player.getFile());
                    if (songThread.isAlive()) {
                        songThread.stop();
                        songThread = new Runner();
                    } else {
                        songThread = new Runner();
                    }
                    songThread.setAudioPlayer(player);
                    songThread.start();

                    time.start();
                    playSong.setActionCommand("pause");
                    playSong.setText("Pause");
                }
                break;
            case "prevSong":
                timeInSeconds = 0;
                timeStamp.setText("0:00 / 0:00");
                //go back 1 song from directory
                if(songQueue.getNumFiles() > 0) {
                    time.stop();
                    if(shuffleOn) {
                        do {
                        temp = (int)(Math.random() * (songQueue.getNumFiles())); //generate number between 1 and 6
                      }while (temp == songQ);
                        songQ = temp;
                    } else {
                        songQ -= 1;
                    }
                    if(songQ < 0) {
                        songQ = songQueue.getNumFiles() - 1;
                    }
                    player.setFilePath(songQueue.getNextSong(songQ));
                    if (songThread.isAlive()) {
                        songThread.stop();
                        songThread = new Runner();
                    } else {
                        songThread = new Runner();
                    }
                    songThread.setAudioPlayer(player);
                    songThread.start();

                    time.start();
                    playSong.setActionCommand("pause");
                    playSong.setText("Pause");
                }
                break;
            case "shuffle":
                //choose new song (shuffle/randomize it)
                if(songQueue.getNumFiles() > 1) {
                    if(shuffleOn) {
                        shuffleOn = false;
                        shuffle.setText("Shuffle = Off");
                    } else {
                        shuffleOn = true;
                        shuffle.setText("Shuffle = On");
                    }
                } else {
                    shuffle.setText("Shuffle = Off");
                    System.out.println("Not enough songs to shuffle.");
                }
                break;
            case "selectSong":
                dir = choose.choose();
//                System.out.println("dir: " + dir);
                player.setFilePath(dir);
                timeInSeconds = 0;
                timeStamp.setText("0:00 / 0:00");
                time.stop();
                //set songQ position
                songQ = songQueue.getPos(player.getFile().toString());
                if (songThread.isAlive()) {
                    songThread.stop();
                    songThread = new Runner();
                } else {
                    songThread = new Runner();
                }
                songThread.setAudioPlayer(player);
                songThread.start();

                time.start();
                playSong.setActionCommand("pause");
                playSong.setText("Pause");
                break;
            default:
                break;
        }
    }

    /**
     * Slider listener that checks when the speed has changed and updates the
     * timer to the new rate of change (frames per second) in milliseconds pre:
     * none post: none
     *
     * @param e
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        volume = volumeControl.getValue();
    }

    class gameTimer implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (songThread.isAlive()) {
                
                /* KNOWN ISSUE:
                 * When song is paused, and restarted, the second count loses
                 * any milliseconds it had stored. Therefore, in theory, if
                 * all pauses were timed, you could delay a song by almost a
                 * second per pause.
                 * PROOF:
                 * 3:35 w interruptions |||| 3:47 wout interruptions
                 * ^^ end of song time values based on the code below
                 */
                timeInMS ++;
                
                timeInSeconds = timeInMS / 515;

                calcMin = (int)timeInSeconds/60;
                calcSec = (int)timeInSeconds%60;

                min = Integer.toString(calcMin);
                if (calcSec < 10) {
                    sec = "0" + Integer.toString(calcSec);
                } else {
                    sec = Integer.toString(calcSec);
                }
                
//                try {
//                    AudioFile audioFile = AudioFileIO.read(player.getFile());
//                    duration = audioFile.getAudioHeader().getTrackLength();
//                } catch (NoPlayerException e) {
//                    e.printStackTrace();
//                }

                songTIS = (int)player.getSoundTime();
                calcSongMin = songTIS / 60;
                calcSongSec = songTIS % 60;
                songMin = Integer.toString(calcSongMin);
                if (calcSongSec < 10) {
                    songSec = "0" + Integer.toString(calcSongSec);
                } else {
                    songSec = Integer.toString(calcSongSec);
                }
                
                timeStamp.setText(min + ":" + sec + " / " + songMin + ":" + songSec);
            }
        }
    }
}
