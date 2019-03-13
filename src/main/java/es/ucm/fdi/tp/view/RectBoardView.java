package es.ucm.fdi.tp.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.extra.jboard.JBoard;
import es.ucm.fdi.tp.extra.jboard.JBoard.Shape;
import es.ucm.fdi.tp.view.GameController.PlayerMode;
import es.ucm.fdi.tp.view.PlayersInfoViewer.PlayersInfoObserver;

public abstract class RectBoardView<S extends GameState<S, A>, A extends GameAction<S, A>> extends GUIView<S, A> implements PlayersInfoObserver {

	private static final long serialVersionUID = 1L;
	
	protected static final Integer empty = -1;
	protected static final Integer player0 = 0;
	protected static final Integer player1 = 1;
	protected static final Integer selected = 2;
	protected static final Integer destiny = 3;
	
	private int numOfRows;
	private int numOfCols;
	protected boolean enabled;
	
	protected GameController<S, A> gameCtrl;
	protected MessageViewer<S, A> infoViewer;
	protected PlayersInfoViewer<S,A> playersInfoViewer;
	protected S state;
	private JBoard jboard;
	
	/**
	 * Coloca el estado inicial en la clase, establece el numero de filas y columnas, y pone el boleano enabled a verdadero;
	 * @param state
	 */
	public RectBoardView(S state) {
		this.state = state;
		this.numOfRows = this.getNumRows();
		this.numOfCols = this.getNumCols();
		this.enabled = true;
		initGUI();
	}

	/**
	 * Crea el tablero
	 */
	private void initGUI() {
		
		this.setLayout(new BorderLayout());
		jboard = new JBoard() {

			private static final long serialVersionUID = 1L;

			@Override
			protected void keyPressed(int keyCode) {
				RectBoardView.this.keyPressed(keyCode);
			}

			@Override
			protected void mouseClicked(int row, int col, int clickCount, int mouseButton) {
				if (gameCtrl.getPlayerMode() == PlayerMode.MANUAL) {
					RectBoardView.this.mouseClicked(row, col, clickCount, mouseButton);
				}
			}
			
			@Override
			protected int getSepPixels() {
				return getSepPixelss();
			}

			@Override
			protected Shape getShape(int player) {
				return getShapes(player);
			}

			@Override
			protected Color getColor(int player) {
				return RectBoardView.this.getPlayerColor(player);
			}

			@Override
			protected Integer getPosition(int row, int col) {
				return RectBoardView.this.getPosition(row, col);
			}

			@Override
			protected Color getBackground(int row, int col) {
				return getBackgrounds(row, col);
			}

			@Override
			protected int getNumRows() {
				return numOfRows;
			}

			@Override
			protected int getNumCols() {
				return numOfCols;
			}
			
		};
		
		this.setPreferredSize(new Dimension(425, 425));
		this.add(jboard, BorderLayout.CENTER);
	}
	
	/**
	 * devuelve la forma del jugador solicitado
	 */
	protected Shape getShapes(int player) {
		return Shape.CIRCLE;
	}
	
	/**
	 * devuelve el color de la posición row, col del tablero 
	 */
	protected Color getBackgrounds(int row, int col) {
		return this.getBackgrounds(row, col);
	}
	
	/**
	 * Devuelve el número de pixels de separación entre las casillas de tablero
	 */
	protected int getSepPixelss() {
		return this.getSepPixelss();
	}
	 
	/**
	 * Devuelve el color del jugador solicitado
	 */
	protected Color getPlayerColor(int id) {
		return this.playersInfoViewer.getPlayerColor(id);
	 }
	 
	protected abstract int getNumCols();
	protected abstract int getNumRows();
	protected abstract Integer getPosition(int row, int col);
	protected abstract void mouseClicked(int row, int col, int clickCount, int mouseButton);
	protected abstract void keyPressed(int keyCode);
	
	public void setGameController(GameController<S, A> gameCtrl) {
		this.gameCtrl = gameCtrl;
	}
	
	public void setMessageViewer(MessageViewer<S, A> messageViewer) {
		this.infoViewer = messageViewer;
	}
	
	/**
	 * añade la clase a la lista de observadores de playersInfoViewer
	 */
	public void setPlayersInfoViewer(PlayersInfoViewer<S, A> playersInfoViewer) {
		this.playersInfoViewer = playersInfoViewer;
		this.playersInfoViewer.addObserver(this);
	}
	
	/**
	 * habilita el tablero
	 */
	public void enable() {
		this.jboard.setEnabled(true);
		this.enabled = true;
	}
	
	/**
	 * deshabilita el tablero
	 */
	public void disable() {
		this.jboard.setEnabled(false);
		this.enabled = false;
	}
	
	/**
	 * Actualiza el estado y repinta el tablero
	 */
	public void update(S state) {
		this.state = state;
		this.jboard.repaint();
		repaint();
	}

}
