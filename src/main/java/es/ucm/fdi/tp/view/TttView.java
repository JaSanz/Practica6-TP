package es.ucm.fdi.tp.view;

import java.awt.Color;

import es.ucm.fdi.tp.ttt.TttAction;
import es.ucm.fdi.tp.ttt.TttState;

public class TttView extends RectBoardView<TttState, TttAction> {
	
	public TttView(TttState state) {
		super(state);
	}

	private static final long serialVersionUID = 1L;
	
	/**
	 * llama al enable de la clase padre
	 */
	public void enable() {
		super.enable();
	}

	/**
	 * repinta los colores de las fichas de los jugadores
	 */
	@Override
	public void colorChange(int p, Color color) {
		repaint();
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
	 * Devuelve el valor de la casilla (row, col) ( 0 para jugador0, 1 para jugador1 y -1 si está vacía)
	 */
	@Override
	protected Integer getPosition(int row, int col) {
		return state.at(row, col);
	}
	
	/**
	 * devuelve el color de la casilla solicitada por row y col
	 */
	public Color getBackgrounds(int row, int col) {
		return Color.LIGHT_GRAY;
	}
	
	/**
	 * devuelve el número de pixels de separacion de las casillas del tablero
	 */
	public int getSepPixelss() {
		return 2;
	}

	/**
	 * Lleva a cabo una acción si la casilla pulsada está vacía y el juego no ha terminado
	 */
	@Override
	protected void mouseClicked(int row, int col, int clickCount, int mouseButton) {
		if(this.enabled) {
			if(getPosition(row, col) == empty) {
				if (!state.isFinished()) {
					TttAction a = new TttAction(gameCtrl.getPlayerId(), row, col);
					if (state.isValid(a)) gameCtrl.makeManualMove(a);
				}
			}
		}
		else {
			if (!state.isFinished()) this.messageViewer.addContent("~ No es tu turno" + System.getProperty("line.separator"));
		}
	}

	@Override
	protected void keyPressed(int keyCode) {}
	
	
}
