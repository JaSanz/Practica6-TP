package es.ucm.fdi.tp.was;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tp.base.model.GameState;

public class WolfAndSheepState extends GameState<WolfAndSheepState, WolfAndSheepAction> {

private static final long serialVersionUID = -2641387354190472377L;
	
	private final int turn;
    private final boolean finished;
    private final int[][] board;
    private final int winner;

    private final int dim;

    final static int EMPTY = -1;

    /**
     * Constructora de un parámetro. Crea el estado inicial del juego.
     * @param dim La dimensión del tablero. Sólo puede ser de 8 casillas por 8 casillas.
     */
    public WolfAndSheepState(int dim) {
        super(2);
        if (dim != 8) {
            throw new IllegalArgumentException("Expected dim to be 8");
        }

        this.dim = dim;
        board = new int[dim][dim];
        for (int i = 0; i < dim; ++i) {
            for (int j = 0; j < dim; ++j) {
            	board[i][j] = EMPTY;
            }
        }
        board[0][1] = 1; board[0][3] = 1; board[0][5] = 1; board[0][7] = 1; //Ponemos las ovejas en el tablero
        board[7][0] = 0; //Ponemos al lobo en el tablero
        this.turn = 0;
        this.winner = -1;
        this.finished = false;
    }
    
    /**
     * Constructora de cuatro parámetros que inicializa los atributos.
     * @param prev Estado anterior de la partida.
     * @param board El tablero actual de la partida.
     * @param finished Indica si la partida ha finalizado.
     * @param winner Indica cuál es el ganador en caso de que la partida haya finalizado.
     */
    public WolfAndSheepState(WolfAndSheepState prev, int[][] board, boolean finished, int winner) {
    	super(2);
    	this.dim = prev.dim;
        this.board = board;
        this.turn = (prev.turn + 1) % 2;
        this.finished = finished;
        this.winner = winner;
    }

    /**
     * Comprueba si la fila y columna dadas se encuentran dentro del rango del tablero y si la posición de esa fila y columna
     * coincide con la casilla a buscar.
     * @param row Fila.
     * @param col Columna.
     * @param busqueda El valor de la casilla.
     * @return Devuelve verdadero si cumple las condiciones, falso en caso contrario.
     */
    public boolean posValida(int row, int col, int busqueda) {
    	return row > -1 && row < 8 && col > -1 && col < 8 && at(row, col) == busqueda;
    }
    
    /**
     * Comprueba si la partida se ha terminado y si la posición a la que se quiere acceder está vacía.
     * @param action
     * @return Dev
     */
    public boolean isValid(WolfAndSheepAction action) {
        if (isFinished()) {
            return false;
        }
        return at(action.getRow(), action.getCol()) == EMPTY;
    }

    /**
     * Comprueba cuáles son las acciones válidas según el jugador al que le toque mover. Almacena las posiciones aceptadas en
     * una lista que, posteriormente, devuelve.
     */
    public List<WolfAndSheepAction> validActions(int playerNumber) {
        ArrayList<WolfAndSheepAction> valid = new ArrayList<>();
        boolean fin = false;
        int sheepCont = 0;
        if (finished) {
            return valid;
        }

        for (int i = 0; i < dim && !fin; i++) {
            for (int j = 0; j < dim && !fin; j++) {
                if (at(i, j) == playerNumber && playerNumber == 0) { //Lobo
                	//Búsqueda alrededor de lobo
                	for (int x = -1; x < 2; x += 2) {
                    	for (int y = -1; y < 2; y += 2) {
                    		if (posValida(i + x, j + y, EMPTY)) valid.add(new WolfAndSheepAction(playerNumber, i + x, j + y, i, j));
                    	}
                    }
                    fin = true;
                }
                else if (at(i, j) == playerNumber && playerNumber == 1) { //Ovejas
                	//Búsqueda alrededor de ovejas
                	for (int x = -1; x < 2; x += 2) {
                		if (posValida(i + 1, j + x, EMPTY)) valid.add(new WolfAndSheepAction(playerNumber, i + 1, j + x, i, j));
                	}
                	++sheepCont;
                	if (sheepCont == 4) fin = true;
                }
            }
        }
    
        return valid;
    }

    //************************MEJORAR EFICIENCIA??
    /**
     * Comprueba si las ovejas tienen movimientros posibles.
     * @param board El tablero actual.
     * @return Devuelve verdadero en caso de que las ovejas se hayan quedado sin movimientos y falso en caso de que tengan
     * todavía algunos posibles.
     */
    public static boolean sheepNoMovements(int[][] board) {
    	int movements = 8;
    	for (int row = 0; row < 8; ++row) {
        	for (int col = 0; col < 8; ++col) {
        		if(board[row][col] == 1) {
        			for (int x = -1; x < 2; x += 2)
        				if (!(col + x > -1 && col + x < 8 && row != 7)) --movements;
        				else if (board[row + 1][col + x] != EMPTY) --movements;
        		}
        	}
        }
        return movements == 0;
    }
    
    /**
     * Comprueba si se ha ganado la partida por parte de alguno de los jugadores. Para ello se le pasa la posición del lobo,
     * y con eso mira si las casillas de alrededor están libres u ocupadas o no válidas.
     * @param board El tablero actual.
     * @param playerNumber El número del jugador.
     * @param row Fila del lobo.
     * @param col Columna del lobo.
     * @return Devuelve verdadero si el lobo ha perdido y falso en caso contrario.
     */
    private static boolean isWinner(int[][] board, int playerNumber, int row, int col) {
    	int sheepNumber = 4;
        int sheepFound = 0;
        //Comprueba las posiciones de alrededor. Busca no válidas y las descarta
        for (int i = -1; i < 2; i += 2) {
        	for (int j = -1; j < 2; j += 2) {
        		if (!(row + i > -1 && row + i < 8 && col + j > -1 && col + j < 8)) --sheepNumber;
        		else if (board[row + i][col + j] == 1) ++sheepFound;
        	}
        }
        return sheepFound == sheepNumber;
    }

    /**
     * Busca al lobo. Si se encuentra en la parte superior del tablero, gana automáticamente. Si no, llama al método para
     * comprobar el número de ovejas de alrededor.
     * @param board El tablero actual.
     * @param playerNumber El número del jugador.
     * @return Devuelve si se ha ganado la partida o no. True si sí, false si no.
     */
    public static boolean isWinner(int[][] board, int playerNumber) {
        boolean won = false;
        for (int x = 0; !won && x < 8; ++x) {
        	for (int y = 0; !won && y < 8; ++y) {
        		if (board[x][y] == 0) {
        			if (x == 0) won = true;
        			else if(isWinner(board, playerNumber, x, y)) won = true;
        		}
        	}
        }
        return won;
    }   
    
    /**
     * Mira qué valor hay en una posición dada del tablero.
     * @param row Fila.
     * @param col Columna.
     * @return Devuelve el valor de la posición de row y col.
     */
    public int at(int row, int col) {
        return board[row][col];
    }

    /**
     * Devuelve en número del jugador al que le toca.
     */
    public int getTurn() {
        return turn;
    }

    /**
     * Devuelve si la partida ha terminado o no. Verdadero si ha acabado, falso en caso contrario.
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Devuelve el número del jugador ganador.
     */
    public int getWinner() {
        return winner;
    }

    /**
     * @return a copy of the board
     */
    public int[][] getBoard() {
        int[][] copy = new int[board.length][];
        for (int i=0; i<board.length; i++) copy[i] = board[i].clone();
        return copy;
    }

    /**
     * Devuelve el dibujo del tablero.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<board.length; i++) {
            sb.append("|");
            for (int j=0; j<board.length; j++) {
                sb.append(board[i][j] == EMPTY ? "   |" : board[i][j] == 0 ? " O |" : " X |");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

	@Override
	public String getGameDescription() {
		return "Wolf and Sheep " + dim + "x" + dim;
	}
	
}
