package es.ucm.fdi.tp.view;

import java.util.List;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GamePlayer;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.GameTable;

public class ConsoleController<S extends GameState<S,A>, A extends GameAction<S,A>> implements Runnable {
	
	private List<GamePlayer> players;
	private GameTable<S, A> game;
	
	public ConsoleController(List<GamePlayer> players, GameTable<S,A> game) {
		this.players = players;
		this.game = game;
	}
	
	/**
	 * Ejecuta el juego, en modo consola, mientras no haya terminado, solicitando y  ejecutando las acciones
	 */
	public void run() {
		int playerCount = 0;
		S currentState;
		
		for (GamePlayer p : players) {
			p.join(playerCount++); // welcome each player, and assign playerNumber
		}
		this.game.start();
		currentState = this.game.getState();

		while (!currentState.isFinished()) {
			// request move
			A action = players.get(currentState.getTurn()).requestAction(currentState);
			this.game.execute(action);
			// apply move
			currentState = this.game.getState();
		}
		
	}
	
}

