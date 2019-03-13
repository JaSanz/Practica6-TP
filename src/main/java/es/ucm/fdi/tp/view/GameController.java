package es.ucm.fdi.tp.view;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.GameEvent;

public interface GameController<S extends GameState<S, A>, A extends GameAction<S, A>> {
	
	public enum PlayerMode {MANUAL, RANDOM, SMART};
	
	public void makeManualMove(A a);
	public void makeRandonMove();
	public void makeSmartMove();
	public void restartGame();
	public void stopGame();
	public void changePlayerMode(PlayerMode p);
	public void handleEvent(GameEvent<S, A> e);
	public PlayerMode getPlayerMode();
	public int getPlayerId();
	public void setMaxThreads(int nThreads);
	public void setTimeout(int millis);
	public void interruptThinking();
	public void setView(MessageViewer<S, A> messageViewer, PlayersInfoViewer<S, A> playersInfoViewer, ControlPanel<S, A> controlPanel, RectBoardView<S, A> gameType);
	public void ThinkingInterrupted();
	public void enable();
	public void disable();
	public void addContent(String a);
	public S getState();
	public void makeMove(A action);
	
}
