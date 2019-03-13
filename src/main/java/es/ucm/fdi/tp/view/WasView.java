package es.ucm.fdi.tp.view;

import java.awt.Color;
import java.util.ArrayList;

import es.ucm.fdi.tp.was.WolfAndSheepAction;
import es.ucm.fdi.tp.was.WolfAndSheepState;

public class WasView extends RectBoardView<WolfAndSheepState, WolfAndSheepAction> {

	private static final long serialVersionUID = 1L;
	
	private Integer rowAux;
	private Integer colAux;
	private ArrayList<WolfAndSheepAction> validPositions;
	
	/**
	 * Le pasa el estado inicial a la clase padre, inicializa rowAux y colAux -1, crea un arrayList
	 */
	public WasView(WolfAndSheepState state) {
		super(state);
		this.rowAux = -1;
		this.colAux = -1;
		this.validPositions = new ArrayList<>();
	}

	/**
	 * repinta los colores de las fichas de los jugadores
	 */
	@Override
	public void colorChange(int p, Color color) {
		repaint();
	}
	
	/**
	 * Devuelve el color de un jugador
	 */
	@Override
	protected Color getPlayerColor(int id) {
		if(id == WasView.selected) 
			return this.playersInfoViewer.getPlayerColor(this.gameCtrl.getPlayerId());
		else 
			return this.playersInfoViewer.getPlayerColor(id);
	}

	/**
	 * devuelve el número de columnas del tablero
	 */
	@Override
	protected int getNumCols() {
		int board[][] = state.getBoard();
		return board.length;
	}

	/**
	 * devuelve el número de filas del tablero
	 */
	@Override
	protected int getNumRows() {
		int board[][] = state.getBoard();
		return board.length;
	}

	/**
	 * Devuelve el valor de una casilla indicada por row y col, devuelve 0 para el jugador0, 1 para el jugador1, -1 si está vacía, 2 si es la seleccionada en primer lugar y 3 si es la casilla destino
	 */
	@Override
	protected Integer getPosition(int row, int col) {
		this.validPositions = (ArrayList<WolfAndSheepAction>) this.state.validActions(this.gameCtrl.getPlayerId());
		if (row == this.rowAux && col == this.colAux)
			return selected;
		for (int i = 0; i < this.validPositions.size(); ++i) {
			if (this.rowAux == this.validPositions.get(i).getRowAux() && this.colAux == this.validPositions.get(i).getColAux()
					&& row == this.validPositions.get(i).getRow() && col == this.validPositions.get(i).getCol())
				return destiny;
		}
		if(this.state.at(row, col) == 0)
			return player0;
		else if (this.state.at(row, col) == 1)
			return player1;
		else
			return empty;
	}
	
	/**
	 * Devuelve el color de la casilla solicitada por row y col
	 */
	protected Color getBackgrounds(int row, int col) {
		return (row+col) % 2 == 0 ? Color.LIGHT_GRAY : Color.BLACK;
	}
	
	/**
	 * Llama al enable de la clase padre
	 */
	public void enable() {
		super.enable();
	}
	
	/**
	 * Realiza un movimiento recogiendo la casilla pulsada por primera vez(origen) y la segunda(destino)
	 */
	@Override
	protected void mouseClicked(int row, int col, int clickCount, int mouseButton) {
		if(this.enabled) {
			if(this.rowAux == -1) {
				if((getPosition(row, col) == player0 && this.gameCtrl.getPlayerId() == 0)
						|| (getPosition(row, col) == player1 && this.gameCtrl.getPlayerId() == 1)) {
					this.rowAux = row;
					this.colAux = col;
					this.messageViewer.addContent("~ Has seleccionado [" + row + ", " + col + "]. Pulsa en el destino o ESC para cancelar" + System.getProperty("line.separator"));
					repaint();
				}
				else
					this.messageViewer.addContent("~ Pulsa en una celda válida" + System.getProperty("line.separator"));
			}
			else {
				if(getPosition(row, col) == destiny) {
					WolfAndSheepAction a = new WolfAndSheepAction(this.gameCtrl.getPlayerId(), row, col, this.rowAux, this.colAux);
					this.gameCtrl.makeManualMove(a);
					this.rowAux = -1;
					this.colAux = -1;
				}
				else
					this.messageViewer.addContent("~ Pulsa en una celda válida" + System.getProperty("line.separator"));
			}
		}
		else {
			if (!state.isFinished()) this.messageViewer.addContent("~ No es tu turno" + System.getProperty("line.separator"));
		}
	}
	
	/**
	 * Devuelve el número de pixels de separacion de las casillas del tablero
	 */
	public int getSepPixelss() {
		return 0;
	}

	/**
	 * Actualiza la pantalla si la tecla pulsada es el ESC
	 */
	@Override
	protected void keyPressed(int keyCode) {
		if(keyCode == 27) {
			this.rowAux = -1;
			this.colAux = -1;
			this.messageViewer.addContent("~ Movimiento cancelado" + System.getProperty("line.separator"));
			repaint();
		}
	}
	
	/**
	 * Actualiza el tablero con el nuemmo estado
	 */
	@Override
	public void update(WolfAndSheepState state) {
		super.update(state);
		this.rowAux = -1;
		this.colAux = -1;
		repaint();
	}

}
