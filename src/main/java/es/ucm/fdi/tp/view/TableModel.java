package es.ucm.fdi.tp.view;

import javax.swing.table.AbstractTableModel;

public class TableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private String[] columnNames;
	private String[][] data;
	
	/**
	 * Crea el texto que se insertan en la tabla de colores
	 */
	public TableModel() {
		this.columnNames = new String[] {"# Jugador", "Color"};
		this.data = new String[][] {{"0", ""}, {"1", ""}};
	}

	/**
	 * devuelve el nombre de la columna solicitada
	 */
	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}
	
	/**
	 * devuelve la longitud del array de la columnas
	 */
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	/**
	 * devuelve el numero de filas que hay
	 */
	@Override
	public int getRowCount() {
		return data != null ? data.length : 0;
	}

	/**
	 * devuelve el numero del jugador
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			return rowIndex;
		} else {
			return data[rowIndex][columnIndex];
		}
	}

}
