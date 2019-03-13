package es.ucm.fdi.tp.view;

import java.awt.Color;
import java.util.ArrayList;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;

public abstract class PlayersInfoViewer<S extends GameState<S, A>, A extends GameAction<S, A>> extends GUIView<S, A> {

	private static final long serialVersionUID = 1L;
	
	protected ArrayList<PlayersInfoObserver> observers;
	
	public PlayersInfoViewer() {
		this.observers = new ArrayList<>();
	}
	
	public abstract Color getPlayerColor(int p);
	
	public interface PlayersInfoObserver {
		public void colorChange(int p, Color color);
	}
	
	public void setPlayersInfoViewer(PlayersInfoViewer<S, A> playersInfoViewer) {}
	
	public void addObserver(PlayersInfoObserver o) {
		observers.add(o);
	}
	
	protected void notifyObservers(int p, Color c) {
		for(PlayersInfoObserver o : observers) {
			o.colorChange(p, c);
		}
	}
	
}
