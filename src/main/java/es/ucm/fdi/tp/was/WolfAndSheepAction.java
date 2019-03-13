package es.ucm.fdi.tp.was;

import es.ucm.fdi.tp.base.model.GameAction;

public class WolfAndSheepAction implements GameAction<WolfAndSheepState, WolfAndSheepAction> {

private static final long serialVersionUID = -8491198872908329925L;
	
	private int player;
    private int row;
    private int col;
    private int rowAux;
    private int colAux;

    /**
     * Constructora de cinco parámetros. Inicializa los atributos.
     * @param player Número del jugador (0 para el lobo, 1 para las ovejas).
     * @param row Fila.
     * @param col Columna.
     * @param rowAux Fila auxiliar para borrar la anterior posición.
     * @param colAux Columna auxiliar para borrar la anterior posición.
     */
    public WolfAndSheepAction(int player, int row, int col, int rowAux, int colAux) {
        this.player = player;
        this.row = row;
        this.col = col;
        this.rowAux = rowAux;
        this.colAux = colAux;
    }

    /**
     * Devuelve el número del jugador.
     */
    public int getPlayerNumber() {
        return player;
    }

    /**
     * Dado un estado, realiza el movimiento, borra la anterior posición y comprueba si alguno de los dos jugadores ha ganado
     * la partida. Si ninguno ha ganado, crea un estado nuevo y sigue jugando. Devuelve el estado que se acaba de crear según
     * las condiciones del estado anterior.
     */
    public WolfAndSheepState applyTo(WolfAndSheepState state) {
        if (player != state.getTurn()) {
            throw new IllegalArgumentException("Not the turn of this player");
        }

        // make move
        int[][] board = state.getBoard();
        board[row][col] = player;
        board[rowAux][colAux] = -1;

        // update state
        WolfAndSheepState next;
        if (WolfAndSheepState.isWinner(board, state.getTurn())) {
            next = new WolfAndSheepState(state, board, true, state.getTurn());
        } else if (WolfAndSheepState.sheepNoMovements(board)) {
        	next = new WolfAndSheepState(state, board, true, 0);
        } else {
            next = new WolfAndSheepState(state, board, false, -1);
        }
        return next;
    }

    /**
     * Devuelve la fila.
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Devuelve la columna.
     */
    public int getCol() {
        return this.col;
    }
    
    public int getRowAux() {
    	return this.rowAux;
    }
    
    public int getColAux() {
    	return this.colAux;
    }

    /**
     * Devuelve los movimientos que un jugador puede realizar.
     */
    public String toString() {
    	return "player " + player + " in pos (" + rowAux + ", " +colAux + ") at (" + row + ", " + col + ")";
    }

}
