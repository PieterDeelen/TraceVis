/*
 * MethodsTableModel.java
 *
 * Author: Pieter Deelen
 * Created: April 3, 2006, 10:44 AM
 *
 */

package tracevis.visualization.detail;

import javax.swing.table.AbstractTableModel;

/**
 * A model for a table which shows methods.
 * @author Pieter Deelen
 */

public abstract class MethodsTableModel extends AbstractTableModel {
	protected abstract int getMethodCount();
	protected abstract String getMethodName(int rowIndex);
	protected abstract int getMethodCallCount(int rowIndex);

	@Override
	public int getRowCount() {
		return getMethodCount();
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
			case 0:
				return getMethodName(rowIndex);
			case 1:
				return getMethodCallCount(rowIndex);
			default:
				throw new RuntimeException();
		}
	}

	@Override
	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
			case 0:
				return "Name";
			case 1:
				return "Count";
			default:
				throw new RuntimeException();
		}
	}

	@Override
	public Class getColumnClass(int columnIndex) {
		switch (columnIndex) {
			case 0:
				return String.class;
			case 1:
				return Integer.class;
			default:
				throw new RuntimeException();
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}
}
