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

public class ChoosePositionPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7606874881308718123L;
	private JSpinner posMs;
	private JSpinner posSec;
	private JSpinner posMin;
	private JRadioButton rdbtnSpecificPosition;
	private JRadioButton rdbtnCurrentPosition;
	private JCheckBox chckbxBlock;

	/**
	 * Create the panel.
	 */
	public ChoosePositionPanel(int initialMin, int initialSec, int initialMs) {
		
		JLabel lblChooseYourDesired = new JLabel("Choose your desired position:");
		
		rdbtnCurrentPosition = new JRadioButton("Current position");
		rdbtnCurrentPosition.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toggleRadioBtn(true);
			}
		});
		rdbtnCurrentPosition.setSelected(true);
		
		rdbtnSpecificPosition = new JRadioButton("Specific position");
		rdbtnSpecificPosition.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toggleRadioBtn(false);
			}
		});
		
		posMin = new JSpinner();
		posMin.setEnabled(false);
		posMin.setModel(new SpinnerNumberModel(initialMin, 0, 99, 1));
		
		posSec = new JSpinner();
		posSec.setEnabled(false);
		posSec.setModel(new SpinnerNumberModel(initialSec, 0, 99, 1));
		
		posMs = new JSpinner();
		posMs.setEnabled(false);
		posMs.setModel(new SpinnerNumberModel(initialMs, 0, 99, 1));
		
		chckbxBlock = new JCheckBox("Use as a block");
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(rdbtnCurrentPosition, GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
						.addComponent(lblChooseYourDesired, GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
						.addComponent(rdbtnSpecificPosition, GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(21)
							.addComponent(posMin, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(posSec, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(posMs, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE))
						.addComponent(chckbxBlock, GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblChooseYourDesired)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(rdbtnCurrentPosition)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(rdbtnSpecificPosition)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(posMin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(posSec, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(posMs, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chckbxBlock)
					.addContainerGap(11, Short.MAX_VALUE))
		);
		setLayout(groupLayout);
		
	}
	
	public boolean isUseAsBlock(){
		return chckbxBlock.isSelected();
	}
	
	public boolean isUseCurrentPosition(){
		return rdbtnCurrentPosition.isSelected();
	}
	
	public boolean isUseSpecificPosition(){
		return rdbtnSpecificPosition.isSelected();
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
		rdbtnCurrentPosition.setSelected(toggleUpRadioBtn);
		rdbtnSpecificPosition.setSelected(!toggleUpRadioBtn);
		posMin.setEnabled(!toggleUpRadioBtn);
		posSec.setEnabled(!toggleUpRadioBtn);
		posMs.setEnabled(!toggleUpRadioBtn);
	}
}
