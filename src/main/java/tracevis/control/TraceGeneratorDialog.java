/*
 * TraceGeneratorDialog.java
 *
 * Author: Pieter Deelen
 * Created: June 13, 2005, 10:00 PM
 *
 */

package tracevis.control;

import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;

import tracevis.model.ProgramOptions;
import tracevis.model.TraceGenerator;
import tracevis.properties.ApplicationProperties;
import tracevis.utilities.SwingWorker;

/**
 * A trace generator dialog.
 * @author Pieter Deelen
 */
class TraceGeneratorDialog extends JDialog {
	/**
	 * Creates new form TraceGeneratorDialog.
	 */
	private TraceGeneratorDialog(ProgramOptions defaultOptions) {
		super((Frame)null, "Generate Trace", true);
		initComponents();

		if (defaultOptions != null) {
			// Initialize fields.
			fromProgramOptions(defaultOptions);
		}
	}

	/**
	 * Shows a trace generator dialog.
	 * @param defaultOptions the program options to show in the dialog.
	 */
	public static ProgramOptions showDialog(ProgramOptions defaultOptions) {
		TraceGeneratorDialog dialog = new TraceGeneratorDialog(defaultOptions);
		dialog.setVisible(true);
		return dialog.toProgramOptions();
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        directoryChooser = new javax.swing.JFileChooser();
        directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        profileChooser = new javax.swing.JFileChooser();
        profileChooser.setFileFilter(new ProfileFilter());
        traceChooser = new javax.swing.JFileChooser();
        classPathChooser = new javax.swing.JFileChooser();
        buttonPanel = new javax.swing.JPanel();
        goButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        optionsPanel = new javax.swing.JPanel();
        profilePanel = new javax.swing.JPanel();
        profileLoadButton = new javax.swing.JButton();
        profileSaveButton = new javax.swing.JButton();
        manualPanel = new javax.swing.JPanel();
        mainClassLabel = new javax.swing.JLabel();
        mainClassText = new javax.swing.JTextField();
        argumentsLabel = new javax.swing.JLabel();
        argumentsText = new javax.swing.JTextField();
        workingDirLabel = new javax.swing.JLabel();
        workingDirText = new javax.swing.JTextField();
        workingDirBrowseButton = new javax.swing.JButton();
        vmOptionsLabel = new javax.swing.JLabel();
        vmOptionsText = new javax.swing.JTextField();
        classPathLabel = new javax.swing.JLabel();
        classPathAddButton = new javax.swing.JButton();
        classPathRemoveButton = new javax.swing.JButton();
        classPathUpButton = new javax.swing.JButton();
        classPathDownButton = new javax.swing.JButton();
        classPathScroller = new javax.swing.JScrollPane();
        classPathList = new javax.swing.JList();
        exclusionPatternsLabel = new javax.swing.JLabel();
        exclusionPatternsScroller = new javax.swing.JScrollPane();
        exclusionPatternsList = new javax.swing.JList();
        exclusionPatternAddButton = new javax.swing.JButton();
        exclusionPatternRemoveButton = new javax.swing.JButton();
        excludeInnerClassesCheckBox = new javax.swing.JCheckBox();
        inclusionPatternsScroller = new javax.swing.JScrollPane();
        inclusionPatternsList = new javax.swing.JList();
        inclusionPatternsLabel = new javax.swing.JLabel();
        inclusionPatternAddButton = new javax.swing.JButton();
        inclusionPatternRemoveButton = new javax.swing.JButton();
        dummyPanel1 = new javax.swing.JPanel();
        savePanel = new javax.swing.JPanel();
        fileNameLabel = new javax.swing.JLabel();
        fileNameText = new javax.swing.JTextField();
        fileNameBrowse = new javax.swing.JButton();
        dummyPanel2 = new javax.swing.JPanel();

        directoryChooser.setFont(new java.awt.Font("Dialog", 0, 10));
        String profilesDirName = ApplicationProperties.getInstance().getProfilesDirectory();
        profileChooser.setCurrentDirectory(new File(profilesDirName));
        String tracesDirName = ApplicationProperties.getInstance().getTracesDirectory();
        traceChooser.setCurrentDirectory(new File(tracesDirName));
        traceChooser.setFileFilter(new TraceFilter());
        classPathChooser.setFileFilter(new ClassPathFilter());
        classPathChooser.setFileSelectionMode(javax.swing.JFileChooser.FILES_AND_DIRECTORIES);

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 10));

        goButton.setText("Go");
        goButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                goButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(goButton);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(cancelButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        getContentPane().add(buttonPanel, gridBagConstraints);

        optionsPanel.setLayout(new java.awt.GridBagLayout());

        profilePanel.setBorder(new javax.swing.border.TitledBorder("Profiles"));
        profileLoadButton.setText("Load Profile...");
        profileLoadButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                profileLoadButtonActionPerformed(evt);
            }
        });

        profilePanel.add(profileLoadButton);

        profileSaveButton.setText("Save Profile...");
        profileSaveButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                profileSaveButtonActionPerformed(evt);
            }
        });

        profilePanel.add(profileSaveButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        optionsPanel.add(profilePanel, gridBagConstraints);

        manualPanel.setLayout(new java.awt.GridBagLayout());

        manualPanel.setBorder(new javax.swing.border.TitledBorder("Manual Settings"));
        mainClassLabel.setText("Main Class:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 2, 5);
        manualPanel.add(mainClassLabel, gridBagConstraints);

        mainClassText.setColumns(25);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 2, 5);
        manualPanel.add(mainClassText, gridBagConstraints);

        argumentsLabel.setText("Arguments:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        manualPanel.add(argumentsLabel, gridBagConstraints);

        argumentsText.setColumns(25);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        manualPanel.add(argumentsText, gridBagConstraints);

        workingDirLabel.setText("Working Directory:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 5, 2, 5);
        manualPanel.add(workingDirLabel, gridBagConstraints);

        workingDirText.setColumns(25);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 5, 2, 5);
        manualPanel.add(workingDirText, gridBagConstraints);

        workingDirBrowseButton.setText("Browse...");
        workingDirBrowseButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                workingDirBrowseButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(12, 5, 2, 5);
        manualPanel.add(workingDirBrowseButton, gridBagConstraints);

        vmOptionsLabel.setText("VM Options:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        manualPanel.add(vmOptionsLabel, gridBagConstraints);

        vmOptionsText.setColumns(25);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        manualPanel.add(vmOptionsText, gridBagConstraints);

        classPathLabel.setText("Class Path:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 5, 2, 0);
        manualPanel.add(classPathLabel, gridBagConstraints);

        classPathAddButton.setText("Add...");
        classPathAddButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                classPathAddButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(12, 5, 2, 5);
        manualPanel.add(classPathAddButton, gridBagConstraints);

        classPathRemoveButton.setText("Remove");
        classPathRemoveButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                classPathRemoveButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        manualPanel.add(classPathRemoveButton, gridBagConstraints);

        classPathUpButton.setText("Move Up");
        classPathUpButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                classPathUpButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        manualPanel.add(classPathUpButton, gridBagConstraints);

        classPathDownButton.setText("Move Down");
        classPathDownButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                classPathDownButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        manualPanel.add(classPathDownButton, gridBagConstraints);

        classPathList.setModel(new DefaultListModel());
        classPathScroller.setViewportView(classPathList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(12, 5, 2, 5);
        manualPanel.add(classPathScroller, gridBagConstraints);

        exclusionPatternsLabel.setText("Exclusion Patterns:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 5, 2, 0);
        manualPanel.add(exclusionPatternsLabel, gridBagConstraints);

        exclusionPatternsList.setModel(new DefaultListModel());
        exclusionPatternsScroller.setViewportView(exclusionPatternsList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 5, 2, 5);
        manualPanel.add(exclusionPatternsScroller, gridBagConstraints);

        exclusionPatternAddButton.setText("Add...");
        exclusionPatternAddButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                exclusionPatternAddButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(12, 5, 2, 5);
        manualPanel.add(exclusionPatternAddButton, gridBagConstraints);

        exclusionPatternRemoveButton.setText("Remove");
        exclusionPatternRemoveButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                exclusionPatternRemoveButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        manualPanel.add(exclusionPatternRemoveButton, gridBagConstraints);

        excludeInnerClassesCheckBox.setText("Exclude Inner Classes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        manualPanel.add(excludeInnerClassesCheckBox, gridBagConstraints);

        inclusionPatternsList.setModel(new DefaultListModel());
        inclusionPatternsScroller.setViewportView(inclusionPatternsList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 5, 2, 5);
        manualPanel.add(inclusionPatternsScroller, gridBagConstraints);

        inclusionPatternsLabel.setText("Inclusion Patterns:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 5, 2, 0);
        manualPanel.add(inclusionPatternsLabel, gridBagConstraints);

        inclusionPatternAddButton.setText("Add...");
        inclusionPatternAddButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                inclusionPatternAddButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(12, 5, 2, 5);
        manualPanel.add(inclusionPatternAddButton, gridBagConstraints);

        inclusionPatternRemoveButton.setText("Remove");
        inclusionPatternRemoveButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                inclusionPatternRemoveButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        manualPanel.add(inclusionPatternRemoveButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        manualPanel.add(dummyPanel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        optionsPanel.add(manualPanel, gridBagConstraints);

        savePanel.setLayout(new java.awt.GridBagLayout());

        savePanel.setBorder(new javax.swing.border.TitledBorder("Save Trace As"));
        fileNameLabel.setText("File Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        savePanel.add(fileNameLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        savePanel.add(fileNameText, gridBagConstraints);

        fileNameBrowse.setText("Browse...");
        fileNameBrowse.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileNameBrowseActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        savePanel.add(fileNameBrowse, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        savePanel.add(dummyPanel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        optionsPanel.add(savePanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(optionsPanel, gridBagConstraints);

        pack();
    }
    // </editor-fold>//GEN-END:initComponents

	private void classPathDownButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_classPathDownButtonActionPerformed
		DefaultListModel model = (DefaultListModel)classPathList.getModel();
		int[] selected = classPathList.getSelectedIndices();

		if (selected.length > 0) {
			if (selected[selected.length - 1] < model.size() - 1) {
				for (int i = selected.length - 1; 0 <= i; i--) {
					Object temp = model.remove(selected[i]);
					model.add(selected[i] + 1, temp);
					selected[i] += 1;
				}

				classPathList.setSelectedIndices(selected);
			}
		}
	}//GEN-LAST:event_classPathDownButtonActionPerformed

	private void classPathUpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_classPathUpButtonActionPerformed
		DefaultListModel model = (DefaultListModel)classPathList.getModel();
		int[] selected = classPathList.getSelectedIndices();

		if (selected.length > 0) {
			if (selected[0] > 0) {
				for (int i = 0; i < selected.length; i++) {
					Object temp = model.remove(selected[i]);
					model.add(selected[i] - 1, temp);
					selected[i] -= 1;
				}

				classPathList.setSelectedIndices(selected);
			}
		}
	}//GEN-LAST:event_classPathUpButtonActionPerformed

	private void classPathRemoveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_classPathRemoveButtonActionPerformed
		DefaultListModel model = (DefaultListModel)classPathList.getModel();
		int[] selectedIndices = classPathList.getSelectedIndices();
		for (int i = 0; i < selectedIndices.length; i++) {
			model.removeElementAt(selectedIndices[i] - i);
		}
	}//GEN-LAST:event_classPathRemoveButtonActionPerformed

	private void classPathAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_classPathAddButtonActionPerformed
		int returnVal = classPathChooser.showDialog(this, "Select");
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = classPathChooser.getSelectedFile();
			DefaultListModel model = (DefaultListModel)classPathList.getModel();
			model.addElement(file.getAbsolutePath());
		}
	}//GEN-LAST:event_classPathAddButtonActionPerformed

	private void fileNameBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileNameBrowseActionPerformed
		int returnVal = traceChooser.showDialog(this, "Select");
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = traceChooser.getSelectedFile();
			fileNameText.setText(file.getPath());
		}
	}//GEN-LAST:event_fileNameBrowseActionPerformed

	private void inclusionPatternAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inclusionPatternAddButtonActionPerformed
		// TODO: Check whether the entered pattern is valid.
		String pattern = JOptionPane.showInputDialog("Enter inclusion pattern.");
		DefaultListModel model = (DefaultListModel)inclusionPatternsList.getModel();
		model.addElement(pattern);
	}//GEN-LAST:event_inclusionPatternAddButtonActionPerformed

	private void inclusionPatternRemoveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inclusionPatternRemoveButtonActionPerformed
		DefaultListModel model = (DefaultListModel)inclusionPatternsList.getModel();
		int[] selectedIndices = inclusionPatternsList.getSelectedIndices();
		for (int i = 0; i < selectedIndices.length; i++) {
			model.removeElementAt(selectedIndices[i] - i);
		}
	}//GEN-LAST:event_inclusionPatternRemoveButtonActionPerformed

	private void exclusionPatternRemoveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exclusionPatternRemoveButtonActionPerformed
		DefaultListModel model = (DefaultListModel)exclusionPatternsList.getModel();
		int[] selectedIndices = exclusionPatternsList.getSelectedIndices();
		for (int i = 0; i < selectedIndices.length; i++) {
			model.removeElementAt(selectedIndices[i] - i);
		}
	}//GEN-LAST:event_exclusionPatternRemoveButtonActionPerformed

	private void exclusionPatternAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exclusionPatternAddButtonActionPerformed
		// TODO: Check whether the entered pattern is valid.
		String pattern = JOptionPane.showInputDialog("Enter exclusion pattern.");
		DefaultListModel model = (DefaultListModel)exclusionPatternsList.getModel();
		model.addElement(pattern);
	}//GEN-LAST:event_exclusionPatternAddButtonActionPerformed

	private void profileSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profileSaveButtonActionPerformed
		// TODO: ask whether to overwrite existing files.
		int returnVal = profileChooser.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = profileChooser.getSelectedFile();
			try {
				ProgramOptions options = toProgramOptions();
				options.save(file);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, "Could not save profile.",
				                              "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}//GEN-LAST:event_profileSaveButtonActionPerformed

	private ProgramOptions toProgramOptions() {
		ProgramOptions options = new ProgramOptions(
			mainClassText.getText(),
			argumentsText.getText(),
			workingDirText.getText(),
			vmOptionsText.getText(),
			getEntries(classPathList),
			getEntries(exclusionPatternsList),
			getEntries(inclusionPatternsList),
			excludeInnerClassesCheckBox.isSelected()
		);

		return options;
	}

	private static List<String> getEntries(JList jlist) {
		DefaultListModel model = (DefaultListModel)jlist.getModel();
		List<String> patterns = new ArrayList<String>(model.size());

		for (int i = 0; i < model.size(); i++) {
			patterns.add((String)model.getElementAt(i));
		}

		return patterns;
	}

	private void profileLoadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profileLoadButtonActionPerformed
		int returnVal = profileChooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = profileChooser.getSelectedFile();
			try {
				ProgramOptions options = ProgramOptions.load(file);
				fromProgramOptions(options);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, "Could not load profile.",
				                             "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}//GEN-LAST:event_profileLoadButtonActionPerformed

	private void fromProgramOptions(ProgramOptions options) {
		mainClassText.setText(options.getMainClass());
		argumentsText.setText(options.getArguments());
		workingDirText.setText(options.getWorkingDirectory());
		vmOptionsText.setText(options.getVMOptions());

		initializeJList(classPathList, options.getClassPath());

		initializeJList(exclusionPatternsList, options.getExclusionPatterns());

		initializeJList(inclusionPatternsList, options.getInclusionPatterns());

		excludeInnerClassesCheckBox.setSelected(options.getExcludeInnerClasses());

	}

	private void initializeJList(JList jlist, List<String> list) {
		DefaultListModel model = (DefaultListModel)jlist.getModel();
		if (list != null) {
			model.clear();
			if (!list.equals("")) {
				for (String entry : list) {
					model.addElement(entry);
				}
			}
		}
	}

	private void workingDirBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_workingDirBrowseButtonActionPerformed
		int returnVal = directoryChooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = directoryChooser.getSelectedFile();
			workingDirText.setText(file.getPath());
		}
	}//GEN-LAST:event_workingDirBrowseButtonActionPerformed

	private void goButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goButtonActionPerformed
		if (fileNameText.getText().equals("")) {
			JOptionPane.showMessageDialog(this, "You need to enter a file name.",
				"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		setVisible(false);

		final ProgramOptions options = toProgramOptions();
		final File file = new File(fileNameText.getText());
		final OutputDialog outputDialog = new OutputDialog(this, true);

		final OutputStream output = outputDialog.getOutputStream();
		final OutputStream error = outputDialog.getErrorStream();

		SwingWorker swingWorker = new SwingWorker() {
			private String message;

			@Override
			public Object construct() {
				message = null;
				try {
					TraceGenerator.generateTrace(options, file, output, error);
				} catch (Exception exc) {
					message = exc.getMessage();
				}
				return null;
			}

			@Override
			public void finished() {
				if (message == null) {
					outputDialog.programFinished();
				} else {
					outputDialog.showError(message);
				}
			}
		};

		swingWorker.start();
		outputDialog.setVisible(true);
		dispose();
	}//GEN-LAST:event_goButtonActionPerformed

	private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
		dispose();
	}//GEN-LAST:event_cancelButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel argumentsLabel;
    private javax.swing.JTextField argumentsText;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton classPathAddButton;
    private javax.swing.JFileChooser classPathChooser;
    private javax.swing.JButton classPathDownButton;
    private javax.swing.JLabel classPathLabel;
    private javax.swing.JList classPathList;
    private javax.swing.JButton classPathRemoveButton;
    private javax.swing.JScrollPane classPathScroller;
    private javax.swing.JButton classPathUpButton;
    private javax.swing.JFileChooser directoryChooser;
    private javax.swing.JPanel dummyPanel1;
    private javax.swing.JPanel dummyPanel2;
    private javax.swing.JCheckBox excludeInnerClassesCheckBox;
    private javax.swing.JButton exclusionPatternAddButton;
    private javax.swing.JButton exclusionPatternRemoveButton;
    private javax.swing.JLabel exclusionPatternsLabel;
    private javax.swing.JList exclusionPatternsList;
    private javax.swing.JScrollPane exclusionPatternsScroller;
    private javax.swing.JButton fileNameBrowse;
    private javax.swing.JLabel fileNameLabel;
    private javax.swing.JTextField fileNameText;
    private javax.swing.JButton goButton;
    private javax.swing.JButton inclusionPatternAddButton;
    private javax.swing.JButton inclusionPatternRemoveButton;
    private javax.swing.JLabel inclusionPatternsLabel;
    private javax.swing.JList inclusionPatternsList;
    private javax.swing.JScrollPane inclusionPatternsScroller;
    private javax.swing.JLabel mainClassLabel;
    private javax.swing.JTextField mainClassText;
    private javax.swing.JPanel manualPanel;
    private javax.swing.JPanel optionsPanel;
    private javax.swing.JFileChooser profileChooser;
    private javax.swing.JButton profileLoadButton;
    private javax.swing.JPanel profilePanel;
    private javax.swing.JButton profileSaveButton;
    private javax.swing.JPanel savePanel;
    private javax.swing.JFileChooser traceChooser;
    private javax.swing.JLabel vmOptionsLabel;
    private javax.swing.JTextField vmOptionsText;
    private javax.swing.JButton workingDirBrowseButton;
    private javax.swing.JLabel workingDirLabel;
    private javax.swing.JTextField workingDirText;
    // End of variables declaration//GEN-END:variables

}
