package es.ucm.fdi.tp.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableCellRenderer;

import es.ucm.fdi.tp.base.Utils;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;

public class PlayersInfoComp<S extends GameState<S, A>, A extends GameAction<S, A>> extends PlayersInfoViewer<S, A> {

	private static final long serialVersionUID = 1L;

	private Map<Integer, Color> colors; // Line -> Color
	private TableModel tModel;
	
	public PlayersInfoComp() {
		initGUI();
	}
	
	/**
	 * Crea el panel de la tabla con los jugadores y sus colores, y está pendiente de si alguien pulsa sobre la tabla para llamar al colorChooser
	 */
	private void initGUI() {
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		colors = new HashMap<>();

		tModel = new TableModel();
		tModel.getRowCount();
		JTable mainTabla = new JTable(tModel) {
			private static final long serialVersionUID = 1L;

			// THIS IS HOW WE CHANGE THE COLOR OF EACH ROW
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
				Component comp = super.prepareRenderer(renderer, row, col);

				// the color of row 'row' is taken from the colors table, if
				// 'null' setBackground will use the parent component color.
				if(col == 1) {
					comp.setBackground(colors.get(row));
				}
				else {
					comp.setBackground(Color.WHITE);
				}
				comp.setForeground(Color.BLACK);
				return comp;
			}
		};

		mainTabla.setToolTipText("Pulsa para cambiar el color del jugador");
		mainTabla.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int row = mainTabla.rowAtPoint(evt.getPoint());
				int col = mainTabla.columnAtPoint(evt.getPoint());
				if (row >= 0 && col >= 0) {
					changeColor(row);
				}
			}

		});
		
		for (int i = 0; i < this.tModel.getRowCount(); ++i) {
			Color color = Utils.colorsGenerator().next();
			colors.put(i, color);
		}

		mainPanel.add(new JScrollPane(mainTabla));
		
		mainPanel.setOpaque(true);
		mainPanel.setPreferredSize(new Dimension(200, 55));
		
		this.add(mainPanel);
		this.setBorder(new TitledBorder("Colores de jugadores"));
		this.setVisible(true);
	}
	
	/**
	 * abre el cuadro de diálogo que permite cambiar de color al jugador seleccionado
	 * @param row el valor del jugador seleccionado
	 */
	private void changeColor(int row) {
		Color c = JColorChooser.showDialog(this, "Elige color para jugador", null);
		if (c != null) {
			colors.put(row, c);
			notifyObservers(row, colors.get(row));
		}
	}
	
	/**
	 * Devuelve el color del jugador solicitado
	 */
	@Override
	public Color getPlayerColor(int player) {
		return colors.get(player);
	}

	@Override
	public void enable() {}

	@Override
	public void disable() {}

	@Override
	public void update(S state) {}

	@Override
	public void setMessageViewer(MessageViewer<S, A> infoViewer) {}

	@Override
	public void setGameController(GameController<S, A> gameCtrl) {}

}
