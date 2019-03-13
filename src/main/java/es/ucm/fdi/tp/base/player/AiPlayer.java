package es.ucm.fdi.tp.base.player;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GamePlayer;
import es.ucm.fdi.tp.base.model.GameState;

/**
 * A player that can play any game.
 */
public class AiPlayer implements GamePlayer {

    protected String name;

    protected int playerNumber;
    protected AiAlgorithm algorithm;

    /**
     * Constructora de dos parámetros que inicializa los atributos.
     * @param name Nombre del jugador.
     * @param algorithm Algoritmo usado para realizar los movimientos.
     */
    public AiPlayer(String name, AiAlgorithm algorithm) {
        this.name = name;
        this.algorithm = algorithm;
    }

    /**
     * Introduce en la partida al jugador.
     */
    @Override
    public void join(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    /**
     * Devuelve el número del jugador.
     */
    @Override
    public int getPlayerNumber() {
    	return playerNumber;
    }

    /**
     * Devuelve el nombre del jugador.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Devuelve la acción más óptima según el algoritmo empleado.
     */
	public <S extends GameState<S,A>, A extends GameAction<S,A>> A requestAction(S state) {
        return algorithm.chooseAction(playerNumber, state);
    }
}
