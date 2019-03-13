package es.ucm.fdi.tp.view;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.GameEvent;
import es.ucm.fdi.tp.mvc.GameObserver;
import es.ucm.fdi.tp.mvc.GameEvent.EventType;
import es.ucm.fdi.tp.mvc.GameObservable;

public class ConsoleView<S extends GameState<S,A>, A extends GameAction<S,A>> implements GameObserver<S,A> {
	
	public ConsoleView(GameObservable<S,A> gameTable) {
		gameTable.addObserver(this);
	}
	
	/**
	 * Notifica al usuario el estado del tablero y si ha terminado o no la partida, mostrando el resultado.
	 */
	@Override
	public void notifyEvent(GameEvent<S, A> e) {
		S currentState = e.getState();
		System.out.println(e.toString());
		if (e.getType() == EventType.Start) System.out.println("Initial state:\n" + currentState);
		else System.out.println("After action:\n" + currentState);
		
		if (currentState.isFinished()) {
			// game over
			String endText = "The game ended: ";
			int winner = currentState.getWinner();
			if (winner == -1) {
				endText += "draw!";
			} else {
				endText += "player " + (winner + 1) + " won!";
			}
			System.out.println(endText);
		}
	}
	
}

