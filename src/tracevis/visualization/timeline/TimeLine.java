/*
 * TimeLine.java
 *
 * Author: Pieter Deelen
 * Created: November 8, 2005, 10:24 AM
 *
 */

package tracevis.visualization.timeline;

import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.Formatter;

import javax.swing.ButtonModel;
import javax.swing.Timer;

import tracevis.model.Program;
import tracevis.model.types.ClassData;
import tracevis.model.types.ProgramListener;
import tracevis.model.types.Range;
import tracevis.visualization.functions.Scale;
import tracevis.visualization.utilities.BrushMultiPickedState;
import tracevis.visualization.utilities.ColorModel;

/**
 * The time line view.
 * @author  Pieter Deelen
 */
public class TimeLine extends javax.swing.JPanel {
	private final Program program;

	private boolean adjustedScrollBar;
	private TimeLineSettings settings;
	private final TimeLineView timeLineView;

	private enum PlayMode {
		PREVIOUS_EVENT,
		NEXT_EVENT,
		NONE
	}

	private PlayMode playMode;
	private final Timer playTimer;

	/**
	 * Creates new form TimeLine.
	 * @param program the program to visualize.
	 * @param pickedState the selection model.
	 */
	public TimeLine(Program program, BrushMultiPickedState pickedState, ColorModel colorModel) {
		initComponents();

		this.program = program;
		program.addListener(new ProgramListenerImpl());

		adjustedScrollBar = false;

		settings = new TimeLineSettings(TimeLineSettings.Mode.SHOW_ACTIVITY,
		                                Scale.LINEAR, 0.5);

		timeLineView = new TimeLineView(program, pickedState, colorModel, settings);
		timeLineView.addListener(new TimeLineViewListenerImpl());
		add(timeLineView, BorderLayout.CENTER);

		scrollBar.addAdjustmentListener(new AdjustmentListenerImpl());

		playMode = PlayMode.NONE;

		playTimer = new Timer(100, new EventPlayer());
		playTimer.setInitialDelay(0);
	}

	private class EventPlayer implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			switch (playMode) {
				case NONE:
					break;
				case PREVIOUS_EVENT:
					program.jumpToPreviousEvent();
					break;
				case NEXT_EVENT:
					program.jumpToNextEvent();
					break;
			}
		}
	}

	private class AdjustmentListenerImpl implements AdjustmentListener {
		@Override
		public void adjustmentValueChanged(AdjustmentEvent e) {
			if (!adjustedScrollBar) {
				Range currentRange = timeLineView.getViewRange();

				long startTime = program.getStartTime();
				long rangeStart = startTime + (long)scrollBar.getValue() * 1000;
				long rangeEnd = rangeStart + currentRange.getEnd() - currentRange.getBegin();

				timeLineView.setViewRange(new Range(rangeStart, rangeEnd));
			} else {
				adjustedScrollBar = false;
			}
		}
	}

	private class TimeLineViewListenerImpl implements TimeLineViewListener {
		@Override
		public void viewRangeChanged() {
			Range viewRange = timeLineView.getViewRange();
			long viewStart = viewRange.getBegin() / 1000;
			long viewEnd = viewRange.getEnd() / 1000;

			long programStart = program.getStartTime() / 1000;
			long programEnd = program.getEndTime() / 1000;

			int scrollRange = (int)(programEnd - programStart);
			int scrollExtent = (int)(viewEnd - viewStart);
			int scrollValue = (int)(viewStart - programStart);

			adjustedScrollBar = true;
			scrollBar.setValues(scrollValue, scrollExtent, 0, scrollRange);
			scrollBar.setUnitIncrement(scrollExtent / 10);
			scrollBar.setBlockIncrement(scrollExtent / 2);

			long startTime = program.getStartTime();
			startTimeLabel.setText(formatTime(viewRange.getBegin() - startTime));
			endTimeLabel.setText(formatTime(viewRange.getEnd() - startTime));
		}
	}

	private class ProgramListenerImpl implements ProgramListener {
		@Override
		public void traceLoaded() {
			long startTimeMicros = program.getStartTime() / 1000;
			long endTimeMicros = program.getEndTime() / 1000;

			int scrollRange = (int)(endTimeMicros - startTimeMicros);

			adjustedScrollBar = true;
			scrollBar.setValues(0, scrollRange, 0, scrollRange);

			startTimeLabel.setText(formatTime(0));
			long viewRange = program.getEndTime() - program.getStartTime();
			endTimeLabel.setText(formatTime(viewRange));

			nextButton.setEnabled(true);
			previousButton.setEnabled(true);
		}

		@Override
		public void traceFiltered() {
			repaint();
		}

		@Override
		public void timeChanged() {
			long startTime = program.getStartTime();
			long metricStartTime = program.getMetricStartTime();
			long currentTime = program.getCurrentTime();

			String metricStartTimeString = formatTime(metricStartTime - startTime);
			String currentTimeString = formatTime(currentTime - startTime);

			metricTimesLabel.setText(metricStartTimeString + "-" + currentTimeString);
		}

		@Override
		public void methodEntered(ClassData caller, ClassData callee) {}
		@Override
		public void methodExited(ClassData caller, ClassData callee) {}
	}

	private String formatTime(long time) {
		long micros = (time / 1000) % 1000;
		long millis = (time / 1000000) % 1000;
		long seconds = time / 1000000000;

		StringBuilder sb = new StringBuilder();
		Formatter formatter = new Formatter(sb);
		formatter.format("%03d.%03d.%03d", seconds, millis, micros);

		return sb.toString();
	}

	/**
	 * Returns the time line settings.
	 */
	public TimeLineSettings getSettings() {
		return settings;
	}

	/**
	 * Set the time line settings.
	 */
	public void setSettings(TimeLineSettings settings) {
		this.settings = settings;
		timeLineView.setSettings(settings);
	}

	/**
	 * Get the view range.
	 */
	public Range getViewRange() {
		return timeLineView.getViewRange();
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        topPanel = new javax.swing.JPanel();
        startTimeLabel = new javax.swing.JLabel();
        metricTimesLabel = new javax.swing.JLabel();
        endTimeLabel = new javax.swing.JLabel();
        toolBar = new javax.swing.JToolBar();
        previousButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();
        linkCheck = new javax.swing.JCheckBox();
        scrollBar = new javax.swing.JScrollBar();

        setLayout(new java.awt.BorderLayout());

        topPanel.setLayout(new java.awt.BorderLayout());

        startTimeLabel.setText("000.000.000");
        topPanel.add(startTimeLabel, java.awt.BorderLayout.WEST);

        metricTimesLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        metricTimesLabel.setText("000.000.000-000.000.000");
        topPanel.add(metricTimesLabel, java.awt.BorderLayout.CENTER);

        endTimeLabel.setText("000.000.000");
        topPanel.add(endTimeLabel, java.awt.BorderLayout.EAST);

        toolBar.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        previousButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tracevis/resources/Continue24-r.gif")));
        previousButton.setBorder(null);
        previousButton.setEnabled(false);
        previousButton.setOpaque(false);
        previousButton.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
                previousButtonStateChanged(evt);
            }
        });

        toolBar.add(previousButton);

        nextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tracevis/resources/Continue24.gif")));
        nextButton.setBorder(null);
        nextButton.setEnabled(false);
        nextButton.setOpaque(false);
        nextButton.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
                nextButtonStateChanged(evt);
            }
        });

        toolBar.add(nextButton);

        linkCheck.setText("Link");
        linkCheck.setOpaque(false);
        linkCheck.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                linkCheckActionPerformed(evt);
            }
        });

        toolBar.add(linkCheck);

        topPanel.add(toolBar, java.awt.BorderLayout.NORTH);

        add(topPanel, java.awt.BorderLayout.NORTH);

        scrollBar.setMaximum(1);
        scrollBar.setOrientation(Adjustable.HORIZONTAL);
        add(scrollBar, java.awt.BorderLayout.SOUTH);

    }// </editor-fold>//GEN-END:initComponents

	private void linkCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_linkCheckActionPerformed
		boolean link = linkCheck.isSelected();
		timeLineView.setLinkIndicators(link);
	}//GEN-LAST:event_linkCheckActionPerformed

	private void previousButtonStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_previousButtonStateChanged
		ButtonModel model = previousButton.getModel();

		if (model.isArmed() && model.isPressed()) {
			playTimer.stop();

			playMode = PlayMode.PREVIOUS_EVENT;

			playTimer.start();
		} else {
			playTimer.stop();
			playMode = PlayMode.NONE;
		}
	}//GEN-LAST:event_previousButtonStateChanged

	private void nextButtonStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_nextButtonStateChanged
		ButtonModel model = nextButton.getModel();

		if (model.isArmed() && model.isPressed()) {
			playTimer.stop();

			playMode = PlayMode.NEXT_EVENT;

			playTimer.start();
		} else {
			playTimer.stop();
			playMode = PlayMode.NONE;
		}
	}//GEN-LAST:event_nextButtonStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel endTimeLabel;
    private javax.swing.JCheckBox linkCheck;
    private javax.swing.JLabel metricTimesLabel;
    private javax.swing.JButton nextButton;
    private javax.swing.JButton previousButton;
    private javax.swing.JScrollBar scrollBar;
    private javax.swing.JLabel startTimeLabel;
    private javax.swing.JToolBar toolBar;
    private javax.swing.JPanel topPanel;
    // End of variables declaration//GEN-END:variables

}
