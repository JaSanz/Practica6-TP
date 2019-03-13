package es.ucm.fdi.tp.view;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GamePlayer;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.base.player.ConcurrentAiPlayer;
import es.ucm.fdi.tp.mvc.GameEvent;
import es.ucm.fdi.tp.mvc.GameEvent.EventType;
import es.ucm.fdi.tp.mvc.GameTable;

public class GUIController<S extends GameState<S, A>, A extends GameAction<S, A>> implements GameController<S, A> {

	private GamePlayer randPlayer;
	private ConcurrentAiPlayer aiPlayer;
	private PlayerMode playerMode;
	private SmartThread<S, A> smartThread;
	private ControlPanel<S, A> panel;
	private MessageViewer <S,A> mensajes;
	private RectBoardView<S, A> tablero;
	@SuppressWarnings("unused")
	private PlayersInfoViewer<S, A> info;
	
	private int playerId;
	
	private GameTable<S, A> game;
	
	public GUIController(int playerId, GamePlayer randPlayer, GamePlayer smartPlayer, GameTable<S, A> game) {
		this.playerId = playerId;
		this.randPlayer = randPlayer;
		this.aiPlayer = (ConcurrentAiPlayer) smartPlayer;
		this.game = game;
		this.playerMode = PlayerMode.MANUAL;
	}

	/**
	 * Ejecuta la acción A
	 */
	@Override
	public void makeManualMove(A a) {
		if (a.getPlayerNumber() == this.playerId) game.execute(a);
	}

	/**
	 * Solicita y ejecuta un movimiento aleatorio
	 */
	@Override
	public void makeRandonMove() {
		if (randPlayer.getPlayerNumber() == game.getState().getTurn() && !game.getState().isFinished()) game.execute(randPlayer.requestAction(game.getState()));
	}

	/**
	 *  Solicita y ejecuta un movimiento inteligente
	 */
	@Override
	public void makeSmartMove() {
		if (!game.getState().isFinished() && aiPlayer.getPlayerNumber() == game.getState().getTurn()) {
			this.smartThread = new SmartThread<S, A>(this.aiPlayer, this);
			this.smartThread.start();
		}
	}

	/**
	 * Reinicia el juego
	 */
	@Override
	public void restartGame() {
		if (this.playerMode == PlayerMode.SMART || (!game.getState().isFinished() && aiPlayer.getPlayerNumber() == game.getState().getTurn())) {
			ThinkingInterrupted();
			interruptThinking();
			this.tablero.enable();
		}
		game.start();
	}

	/**
	 * Para el juego
	 */
	@Override
	public void stopGame() {
		game.stop();
	}

	/**
	 * Cambia el modo del jugador, al modo que recibe 
	 */
	@Override
	public void changePlayerMode(PlayerMode mode) {
		this.playerMode = mode;
		decideMakeAutomaticMove();
	}

	/**
	 * Maneja un evento de tipo Info o Change
	 */
	@Override
	public void handleEvent(GameEvent<S, A> e) {
		if ((e.getType() == EventType.Info || e.getType() == EventType.Change) && e.getState().getTurn() == this.playerId) decideMakeAutomaticMove();
	}
	
	/**
	 * Raliza un movimiento aleatorio o inteligente dependiendo del modo de juego en el que esté
	 */
	private void decideMakeAutomaticMove() {
		if (this.playerMode == PlayerMode.SMART)
			makeSmartMove();
		else if (this.playerMode == PlayerMode.RANDOM) 
			makeRandonMove();
	}

	
	@Override
	public PlayerMode getPlayerMode() {
		return this.playerMode;
	}

	@Override
	public int getPlayerId() {
		return this.playerId;
	}
	
	/**
	 * Establede el máximo número de hilos que se pueden usar. No tiene por qué corresponder al máximo número de hilos de tu ordenador
	 */
	@Override
	public void setMaxThreads(int nThreads) {
		this.aiPlayer.setMaxThreads(nThreads);
	}

	/**
	 * Establece el máximo tiempo que la máquina debe estar pensando
	 */
	@Override
	public void setTimeout(int millis) {
		this.aiPlayer.setTimeout(millis);
	}

	/**
	 * Iterrumpe la ejecución de los hilos si estos están pensando
	 */
	@Override
	public void interruptThinking() {
		this.smartThread.interrupt();
	}

	/**
	 * Permite que el controlador tenga los métodos de la vista
	 */
	@Override
	public void setView(MessageViewer<S, A> messageViewer, PlayersInfoViewer<S, A> playersInfoViewer, ControlPanel<S, A> controlPanel, RectBoardView<S, A> gameType) {
		this.mensajes = messageViewer;
		this.info = playersInfoViewer;
		this.panel = controlPanel;
		this.tablero = gameType;
	}

	/**
	 * Cuando se interrumpe un pensamiento, cambia el modo de juego a manual
	 */
	@Override
	public void ThinkingInterrupted() {
		this.panel.changeToManual();
	}

	/**
	 * Habilita los botones necesarios tras acabar de pensar
	 */
	@Override
	public void enable() {
		this.tablero.enable();
		this.panel.enableWhileThinking();
	}

	/**
	 * Deshabilita los botones necesarios cuando empieza a pensar
	 */
	@Override
	public void disable() {
		this.tablero.disable();
		this.panel.disableWhileThinking();
	}
	
	/**
	 * Añade contenido al panel de mensajes
	 */
	@Override
	public void addContent(String a) {
		this.mensajes.addContent(a);
	}
	
	@Override
	public S getState() {
		return this.game.getState();
	}
	
	/**
	 * Ejecuta la accion action
	 */
	@Override
	public void makeMove(A action) {
		game.execute(action);
	}
	
}
