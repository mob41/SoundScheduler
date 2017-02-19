package com.github.mob41.soundsche;

import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class Main {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Unable to set look and feel: " + e, "Fatal Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		File vlcNearbyFolder = new File("vlc");
		if (!(vlcNearbyFolder.exists() && vlcNearbyFolder.isDirectory())){
			boolean found = new NativeDiscovery().discover();
	        
			if (!found){
				String input = JOptionPane.showInputDialog(null, "Application cannot find VLC installed in this computer.\nPlease input the exact path of VLC Player:", "Cannot find VLC Player", JOptionPane.WARNING_MESSAGE);
				if (input == null){
					System.exit(0);
				} else if (input.isEmpty()){
					JOptionPane.showMessageDialog(null, "Input cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}
				NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), input);
			}
		} else {
			NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), vlcNearbyFolder.getAbsolutePath());
		}
	}

}
