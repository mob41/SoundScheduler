package com.github.mob41.soundsche.ui;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;

public class SeekPositionPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7606874881308718123L;
	private JSpinner posMs;
	private JSpinner posSec;
	private JSpinner posMin;
	private JRadioButton rdbtnSeekToSelected;
	private JRadioButton rdbtnSeekToSpecific;

	/**
	 * Create the panel.
	 */
	public SeekPositionPanel(int initialMin, int initialSec, int initialMs) {
		
		posMin = new JSpinner();
		posMin.setModel(new SpinnerNumberModel(initialMin, 0, 99, 1));
		
		posSec = new JSpinner();
		posSec.setModel(new SpinnerNumberModel(initialSec, 0, 99, 1));
		
		posMs = new JSpinner();
		posMs.setModel(new SpinnerNumberModel(initialMs, 0, 99, 1));
		
		rdbtnSeekToSelected = new JRadioButton("Seek to selected track");
		rdbtnSeekToSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toggleRadioBtn(false);
			}
		});
		
		rdbtnSeekToSpecific = new JRadioButton("Seek to specific position");
		rdbtnSeekToSpecific.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				toggleRadioBtn(true);
			}
		});
		rdbtnSeekToSpecific.setSelected(true);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(36)
							.addComponent(posMin, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(posSec, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(posMs, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(rdbtnSeekToSelected, GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(rdbtnSeekToSpecific, GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(rdbtnSeekToSpecific)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(posMin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(posSec, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(posMs, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(rdbtnSeekToSelected)
					.addContainerGap(27, Short.MAX_VALUE))
		);
		setLayout(groupLayout);
		
	}
	
	public boolean isSeekToSelectedTrack(){
		return rdbtnSeekToSelected.isSelected();
	}
	
	public boolean isSeekToSpecificPosition(){
		return rdbtnSeekToSpecific.isSelected();
	}
	
	public int getPosMin(){
		return (int) posMin.getValue();
	}
	
	public int getPosSec(){
		return (int) posSec.getValue();
	}
	
	public int getPosMs(){
		return (int) posMs.getValue();
	}
	
	private void toggleRadioBtn(boolean toggleUpRadioBtn){
		rdbtnSeekToSpecific.setSelected(toggleUpRadioBtn);
		rdbtnSeekToSelected.setSelected(!toggleUpRadioBtn);
		posMin.setEnabled(!toggleUpRadioBtn);
		posSec.setEnabled(!toggleUpRadioBtn);
		posMs.setEnabled(!toggleUpRadioBtn);
	}
}
