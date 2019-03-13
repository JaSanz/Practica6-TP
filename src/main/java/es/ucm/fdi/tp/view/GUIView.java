package es.ucm.fdi.tp.view;

import javax.swing.JPanel;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.GameObservable;

public abstract class GUIView<S extends GameState<S, A>, A extends GameAction<S, A>> extends JPanel {

	private static final long serialVersionUID = 1L;
	
	protected MessageViewer<S, A> messageViewer;
	protected PlayersInfoViewer<S, A> playersInfoViewer;
	protected ControlPanel<S, A> controlPanel;
	protected GameController<S, A> gameCtrl;
	protected GameObservable<S, A> game;

	boolean activated;
	
	public GUIView() {
		this.activated = false;
	}
	
	public abstract void enable();
	public abstract void disable();
	public abstract void update(S state);
	public abstract void setMessageViewer(MessageViewer<S, A> infoViewer);
	public abstract void setPlayersInfoViewer(PlayersInfoViewer<S, A> playersInfoViewer);
	public abstract void setGameController(GameController<S, A> gameCtrl);
	
}
