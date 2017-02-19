package com.github.mob41.soundsche.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.SwingConstants;

public class UIFrame extends JFrame {
	
	private static final String[] col_ident = {"Time", "File-name"};

	private JPanel contentPane;
	private JTable table;
	
	private DefaultTableModel tableModel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIFrame frame = new UIFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public UIFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 871, 541);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel label = new JLabel("00:00:00");
		
		JSlider slider = new JSlider();
		slider.setValue(0);
		
		JLabel label_1 = new JLabel("-00:00:00");
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JButton btnPlay = new JButton("Play");
		
		JButton btnPause = new JButton("Pause");
		
		JButton btnSeekTo = new JButton("Seek to...");
		
		JButton btnLoadProfile = new JButton("Load profile");
		
		JButton btnSaveProfile = new JButton("Save profile");
		
		JSplitPane splitPane = new JSplitPane();
		
		JButton btnAddFile = new JButton("Add file");
		
		JButton btnRemoveFile = new JButton("Remove file");
		
		JButton btnSearchFile = new JButton("Search file...");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(label, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(slider, GroupLayout.DEFAULT_SIZE, 722, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(label_1, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(btnPlay)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnPause)
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
					.addContainerGap(189, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(splitPane, GroupLayout.DEFAULT_SIZE, 835, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(label, GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
						.addComponent(slider, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(label_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnPlay)
						.addComponent(btnPause)
						.addComponent(btnSeekTo)
						.addComponent(btnLoadProfile)
						.addComponent(btnSaveProfile)
						.addComponent(btnAddFile)
						.addComponent(btnRemoveFile)
						.addComponent(btnSearchFile))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(splitPane, GroupLayout.PREFERRED_SIZE, 421, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		
		JScrollPane scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);
		
		tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(col_ident);
		table = new JTable(tableModel);
		table.setPreferredScrollableViewportSize(new Dimension(150, 400));
		scrollPane.setViewportView(table);
		contentPane.setLayout(gl_contentPane);
	}
}
