package com.github.mob41.soundsche.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;

import com.github.mob41.soundsche.TickHandler;
import com.github.mob41.soundsche.Track;
import com.github.mob41.soundsche.TrackManager;
import com.github.mob41.soundsche.TrackPlayerThread;
import com.github.mob41.soundsche.TrackTime;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Dimension;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;

public class UIFrame extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -250094591689347352L;

	private static final String[] col_ident = {"Time", "File-name"};
	
	private final TrackManager mgr;

	private JPanel contentPane;
	private DefaultTableModel tableModel;
	
	private final JFileChooser chooser;
	private JLabel seekRemain;
	private JLabel seekTime;
	private JSlider seeker;
	
	private TrackPlayerThread thread;
	private JButton btnAddFile;
	private JButton btnRemoveFile;
	private JButton btnPlay;
	private JButton btnPause;
	private JButton btnStop;
	private JLabel lblLength;
	private JTable table;
	private JPanel panel_1;
	private JLabel lblSoundschedulerAlphaCopyright;

	/**
	 * Create the frame.
	 */
	public UIFrame(boolean autoPlay, String loadFilePath) {
		setTitle("SoundScheduler");
		mgr = new TrackManager();
		
		chooser = new JFileChooser();
		thread = new TrackPlayerThread(mgr);
		thread.addTickHandler(new TickHandler(){

			@Override
			public void handle() {
				updateUiSeeker();
				
				if (!thread.isRunning()){
					btnPlay.setEnabled(true);
					btnAddFile.setEnabled(true);
					btnRemoveFile.setEnabled(true);
				}
			}
			
		});
		thread.start();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 871, 541);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		seekTime = new JLabel("--:--:--");
		
		seeker = new JSlider();
		seeker.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
				
			}
		});
		seeker.setValue(0);
		
		seekRemain = new JLabel("--:--:--");
		seekRemain.setHorizontalAlignment(SwingConstants.RIGHT);
		
		btnPlay = new JButton("Play");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playMedia();
			}
		});
		
		btnPause = new JButton("Pause");
		btnPause.setEnabled(false);
		btnPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pauseMedia();
			}
		});
		
		JButton btnSeekTo = new JButton("Seek to...");
		btnSeekTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				TrackTime currTime = thread.getOffset();
				currTime = currTime == null ? new TrackTime(0,0,0) : currTime;
				
				SeekPositionPanel panel = new SeekPositionPanel(currTime.getMin(), currTime.getSec(), currTime.getMs());
				int option = JOptionPane.showOptionDialog(UIFrame.this, panel, "Seek to...", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, 0);
			
				if (option == JOptionPane.CLOSED_OPTION || option == JOptionPane.CANCEL_OPTION){
					return;
				}
				
				TrackTime time;
				
				if (panel.isSeekToSelectedTrack()){
					int index = table.getSelectedRow();
					
					if (index == -1){
						JOptionPane.showMessageDialog(UIFrame.this, "No track selected!", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					TrackTime strtt = TrackTime.fromString((String) tableModel.getValueAt(index, 0));
					
					if (strtt == null){
						JOptionPane.showMessageDialog(UIFrame.this, "Error parsing TrackTime from String!", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					time = strtt;
				} else {
					time = new TrackTime(panel.getPosMin(), panel.getPosSec(), panel.getPosMs());
				}
				
				boolean rerun = thread.isRunning();
				
				pauseMedia();
				seekTo(time);
				
				if (rerun){
					playMedia();
				}
				
				updateUiSeeker();
			}
		});
		
		JButton btnLoadProfile = new JButton("Load profile");
		btnLoadProfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int option = JOptionPane.showOptionDialog(UIFrame.this, "Are you sure to load?", "Replacing existing profile", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, 0);
				
				if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.NO_OPTION){
					return;
				}
				
				JFileChooser chooser = new JFileChooser();
				
				chooser.setFileFilter(new FileFilter(){

					@Override
					public boolean accept(File arg0) {
						return arg0.isDirectory() || arg0.getName().endsWith(".ssprofile");
					}

					@Override
					public String getDescription() {
						return ".ssprofile (SoundScheduler Profile)";
					}
					
				});
				
				option = chooser.showOpenDialog(UIFrame.this);
				
				if (option == JFileChooser.CANCEL_OPTION){
					return;
				}
				
				File file = chooser.getSelectedFile();
				
				if (!file.exists()){
					JOptionPane.showMessageDialog(UIFrame.this, "The specified file does not exist", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				try {
					mgr.load(file.getAbsolutePath());
				} catch (IOException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(UIFrame.this, "Load failed:\n" + e1, "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				updateUiTracks();
			}
		});
		
		JButton btnSaveProfile = new JButton("Save profile");
		btnSaveProfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				
				chooser.setSelectedFile(new File("soundsche-profile-" + Calendar.getInstance().getTimeInMillis() + ".ssprofile"));
				
				chooser.setFileFilter(new FileFilter(){

					@Override
					public boolean accept(File arg0) {
						return arg0.isDirectory() || arg0.getName().endsWith(".ssprofile");
					}

					@Override
					public String getDescription() {
						return ".ssprofile (SoundScheduler Profile)";
					}
					
				});
				
				int option = chooser.showSaveDialog(UIFrame.this);
				
				if (option == JFileChooser.CANCEL_OPTION){
					return;
				}
				
				File file = chooser.getSelectedFile();
				
				try {
					mgr.save(file.getAbsolutePath());
				} catch (IOException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(UIFrame.this, "Save failed:\n" + e1, "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		});
		
		JSplitPane splitPane = new JSplitPane();
		
		btnAddFile = new JButton("Add file");
		btnAddFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				TrackTime currTime = thread.getOffset();
				currTime = currTime == null ? new TrackTime(0,0,0) : currTime;
				
				ChoosePositionPanel panel = new ChoosePositionPanel(currTime.getMin(), currTime.getSec(), currTime.getMs());
				int option = JOptionPane.showOptionDialog(UIFrame.this, panel, "Add new track", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, 0);
			
				if (option == JOptionPane.CLOSED_OPTION || option == JOptionPane.CANCEL_OPTION){
					return;
				}
				
				TrackTime time;
				
				if (panel.isUseCurrentPosition()){
					time = currTime;
				} else {
					time = new TrackTime(panel.getPosMin(), panel.getPosSec(), panel.getPosMs());
				}
				
				if (panel.isUseAsBlock()){
					mgr.addTrack(new Track(time, "Block"));
					updateUiTracks();
					return;
				}
				
				option = chooser.showOpenDialog(UIFrame.this);
				
				if (option == JFileChooser.CANCEL_OPTION){
					return;
				}
				
				File file = chooser.getSelectedFile();
				
				if (!file.getName().endsWith(".mp3")){
					option = JOptionPane.showOptionDialog(UIFrame.this,
							"You are adding a file that is not ending with .mp3.\nAre you sure to continue?",
							"Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, 0);
					
					if (option == JOptionPane.CLOSED_OPTION || option == JOptionPane.NO_OPTION){
						return;
					}
				}
				
				Track track = new Track(time, file.getPath());
				mgr.addTrack(track);
				
				updateUiTracks();
			}
		});
		
		btnRemoveFile = new JButton("Remove file");
		btnRemoveFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = table.getSelectedRow();
				
				if (index == -1){
					JOptionPane.showMessageDialog(UIFrame.this, "No track selected", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				String time = (String) tableModel.getValueAt(index, 0);
				String fileName = (String) tableModel.getValueAt(index, 1);
				
				TrackTime tt = TrackTime.fromString(time);
				
				if (tt == null){
					JOptionPane.showMessageDialog(UIFrame.this, "Error parsing TrackTime from string:\n" + time, "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				Track track = new Track(tt, fileName);
				
				boolean result = mgr.removeTrack(track);
				if (!result){
					JOptionPane.showMessageDialog(UIFrame.this, "Error removing track, resulted error in function", "Error", JOptionPane.ERROR_MESSAGE);
				}
				
				updateUiTracks();
			}
		});
		
		JButton btnSearchFile = new JButton("Search file...");
		
		btnStop = new JButton("Stop");
		btnStop.setEnabled(false);
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopMedia();
			}
		});
		
		lblLength = new JLabel("Length: --:--:--");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(splitPane, GroupLayout.DEFAULT_SIZE, 835, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(seekTime, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(seeker, GroupLayout.DEFAULT_SIZE, 722, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(seekRemain, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(btnPlay)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnPause)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnStop)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnSeekTo)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnLoadProfile)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnSaveProfile)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnAddFile)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnRemoveFile)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnSearchFile)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblLength, GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(seekTime, GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
						.addComponent(seeker, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(seekRemain, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnPlay)
							.addComponent(btnPause)
							.addComponent(btnStop)
							.addComponent(btnSeekTo)
							.addComponent(btnLoadProfile)
							.addComponent(btnSaveProfile)
							.addComponent(btnAddFile)
							.addComponent(btnRemoveFile)
							.addComponent(btnSearchFile))
						.addComponent(lblLength))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(splitPane, GroupLayout.PREFERRED_SIZE, 421, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		
		JScrollPane scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);
		
		tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(col_ident);
		table = new JTable(tableModel){
			
			@Override
			public boolean isCellEditable(int row, int col){
				return false;
			}
			
		};
		table.setPreferredScrollableViewportSize(new Dimension(150, 400));
		scrollPane.setViewportView(table);
		
		panel_1 = new JPanel();
		splitPane.setRightComponent(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		lblSoundschedulerAlphaCopyright = new JLabel("SoundScheduler Alpha. Copyright (c) 2017 Anthony Law");
		lblSoundschedulerAlphaCopyright.setFont(new Font("PMingLiU", Font.PLAIN, 14));
		lblSoundschedulerAlphaCopyright.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(lblSoundschedulerAlphaCopyright, BorderLayout.CENTER);
		contentPane.setLayout(gl_contentPane);
		
		seeker.setValueIsAdjusting(true);
		
		if (loadFilePath != null){
			try {
				mgr.load(loadFilePath);
				updateUiTracks();
			} catch (IOException e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(UIFrame.this, "Argument load file failed:\n"+e1, "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		if (autoPlay){
			playMedia();
		}
	}
	
	private void pauseMedia(){
		if (thread.isRunning()){
			thread.pauseMedia();
		}
		
		btnPlay.setEnabled(true);
		btnPause.setEnabled(false);
		btnStop.setEnabled(true);
	}
	
	private void stopMedia(){
		if (thread.isRunning()){
			thread.stopMedia();
		}
		
		thread.setOffset(null);
		updateUiSeeker();
		
		btnPlay.setEnabled(true);
		btnPause.setEnabled(false);
		btnStop.setEnabled(false);
		
		btnAddFile.setEnabled(true);
		btnRemoveFile.setEnabled(true);
		
		setTitle("SoundScheduler");
	}
	
	private void playMedia(){
		if (thread.isRunning()){
			thread.stopMedia();
		}
		
		btnPlay.setEnabled(false);
		btnPause.setEnabled(true);
		btnStop.setEnabled(true);
		
		btnAddFile.setEnabled(false);
		btnRemoveFile.setEnabled(false);
		
		thread.startThread();
	}
	
	public static long calcMs(TrackTime time){
		return time.getMin() * 60 * 1000 +
				time.getSec() * 1000 +
				time.getMs() * 10;
	}
	
	public void updateUiSeeker(){
		Track[] tracks = mgr.getAllTracks();
		
		if (tracks.length == 0){
			return;
		}
		
		TrackTime startTime = thread.getOffset();
		
		if (startTime == null){
			startTime = new TrackTime(0,0,0);
		}
		
		TrackTime lastTime = mgr.getLastTrack().getTrackTime();
		TrackTime minusTime = lastTime.minus(startTime);
		
		seekTime.setText(startTime.toString());
		seekRemain.setText("-" + minusTime.toString());
		
		lblLength.setText("Length: " + lastTime.toString());
		
		long stms = calcMs(startTime);
		long ltms = calcMs(lastTime);
		
		seeker.setMaximum((int) ltms);
		seeker.setValue((int) stms);
		
		Track beforeTrack = mgr.getBeforeTrack(startTime);
		Track nextTrack = mgr.getNextTrack(startTime);
		//System.out.println(nextTrack.getTrackTime().toString());
		if (nextTrack != null){
			String currentTrackStr = beforeTrack != null ? "Current track: " + beforeTrack.getFilePath() : "First track";
			setTitle("SoundScheduler [" + currentTrackStr + "][Next track in " + nextTrack.getTrackTime().minus(startTime).toString() + "]");
		} else {
			setTitle("SoundScheduler [No more tracks][Ending in " + minusTime.toString() + "]");
		}
	}
	
	public void seekTo(TrackTime time){
		thread.setOffset(time);
	}
	
	public void updateUiTracks(){
		Track[] tracks = mgr.getAllTracks();
		
		String[][] strs = new String[tracks.length][2];
		Track track;
		for (int i = 0; i < tracks.length; i++){
			track = tracks[i];
			
			strs[i][0] = track.getTrackTime().toString();
			strs[i][1] = track.getFilePath();
		}
		
		tableModel.setRowCount(0);
		
		for (int i = 0; i < strs.length; i++){
			tableModel.addRow(strs[i]);
		}
	}
}
