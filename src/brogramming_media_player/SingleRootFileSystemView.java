/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brogramming_media_player;

import java.io.File;
import java.io.IOException;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author rsweet
 */
public class SingleRootFileSystemView extends FileSystemView {

    File root;
    File[] roots = new File[1];

    public SingleRootFileSystemView(File path) {
        super();
        try {
            root = path.getCanonicalFile();
            roots[0] = root;
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        if (!root.isDirectory()) {
            root.mkdir();
        }
    }

    @Override
    public File createNewFolder(File containingDir) {
        File folder = new File(containingDir, "music");
        folder.mkdir();
        return folder;
    }

    @Override
    public File getDefaultDirectory() {
        return root;
    }

    @Override
    public File getHomeDirectory() {
        return root;
    }

    @Override
    public File[] getRoots() {
        return roots;
    }
}
