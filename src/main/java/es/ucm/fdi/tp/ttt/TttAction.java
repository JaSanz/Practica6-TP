package es.ucm.fdi.tp.ttt;

import es.ucm.fdi.tp.base.model.GameAction;

/**
 * An action for TickTackToe.
 */
public class TttAction implements GameAction<TttState, TttAction> {

	private static final long serialVersionUID = -8491198872908329925L;
	
	private int player;
    private int row;
    private int col;

    /**
     * Constructora de cinco parámetros. Inicializa los atributos.
     * @param player Número del jugador.
     * @param row Fila.
     * @param col Columna.
     */
    public TttAction(int player, int row, int col) {
        this.player = player;
        this.row = row;
        this.col = col;
    }

    /**
     * Devuelve el número del jugador.
     */
    public int getPlayerNumber() {
        return player;
    }

    /**
     * Dado un estado, realiza el movimiento y comprueba si alguno de los dos jugadores ha ganado
     * la partida. Si ninguno ha ganado, crea un estado nuevo y sigue jugando. Devuelve el estado que se acaba de crear según
     * las condiciones del estado anterior.
     */
    public TttState applyTo(TttState state) {
        if (player != state.getTurn()) {
            throw new IllegalArgumentException("Not the turn of this player");
        }

        // make move
        int[][] board = state.getBoard();
        board[row][col] = player;

        // update state
        TttState next;
        if (TttState.isWinner(board, state.getTurn())) {
            next = new TttState(state, board, true, state.getTurn());
        } else if (TttState.isDraw(board)) {
            next = new TttState(state, board, true, -1);
        } else {
            next = new TttState(state, board, false, -1);
        }
        return next;
    }

    /**
     * Devuelve la fila.
     */
    public int getRow() {
        return row;
    }

    /**
    * Devuelve la columna.
    */
    public int getCol() {
        return col;
    }

    /**
     * Devuelve los movimientos que un jugador puede realizar.
     */
    public String toString() {
        return "place " + player + " at (" + row + ", " + col + ")";
    }

}
