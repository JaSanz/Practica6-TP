package es.ucm.fdi.tp.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.GameEvent;
import es.ucm.fdi.tp.mvc.GameObservable;
import es.ucm.fdi.tp.mvc.GameObserver;

public class GameContainer<S extends GameState<S, A>, A extends GameAction<S, A>> extends JFrame implements GameObserver<S, A> {

	private static final long serialVersionUID = 1L;
	
	private boolean enabled;
	private boolean ini;
	private static int frameTurn;
	
	private GUIView<S, A> gameView;
	
	/**
	 * Constructora, da título a la ventana, inicializa las variables, establece los componentes y añade los observadores
	 * @param gameView vista del juego
	 * @param gameCtrl controlador del juego
	 * @param game el motor del juego
	 */
	public GameContainer(GUIView<S, A> gameView, GameController<S, A> gameCtrl, GameObservable<S, A> game) {
		super("Práctica 5");
		this.enabled = true;
		this.ini = true;
		GameContainer.frameTurn = 0;
		this.gameView = gameView;
		this.gameView.messageViewer = new MessageViewerComp<S, A>();
		this.gameView.playersInfoViewer = new PlayersInfoComp<S, A>();
		this.gameView.controlPanel = new ControlPanel<S, A>(gameCtrl);
		this.gameView.gameCtrl = gameCtrl;
		this.gameView.gameCtrl.setView(this.gameView.messageViewer, this.gameView.playersInfoViewer, this.gameView.controlPanel, (RectBoardView<S, A>) this.gameView);
		initGUI();
		this.gameView.game = game;
		this.gameView.game.addObserver(this);
		this.gameView.setPlayersInfoViewer(this.gameView.playersInfoViewer);
	}
	
	/**
	 * Se encarga de meter en la ventana los componentes visuales de manera ordenada.
	 */
	private void initGUI() {
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		JPanel tableroPanel = new JPanel();
		
		panel.add(gameView.messageViewer, BorderLayout.NORTH);
		panel.add(this.gameView.playersInfoViewer, BorderLayout.CENTER);
		
		tableroPanel.add(gameView);
		
		this.setLayout(new BorderLayout(5, 5));
		
		this.add(this.gameView.controlPanel, BorderLayout.PAGE_START);
		this.add(tableroPanel, BorderLayout.CENTER);
		this.add(panel, BorderLayout.LINE_END);
		
		this.setVisible(true);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.pack();
		
		repaint();
		
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public boolean getEnabled() {
		return this.enabled;
	}

	/**
	 * Notifica de los eventos del juego
	 */
	@Override
	public void notifyEvent(GameEvent<S, A> e) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {handleEvent(e); }
		});
	}
	
	/**
	 * Maneja el evento que le llega 
	 * @param e evento ocurrido en el juego
	 */
	public void handleEvent(GameEvent<S, A> e) {
		switch(e.getType()) {
		case Change:
			if(e.getState().isFinished()) {
				if(e.getState().getWinner() == -1) {
					this.gameView.messageViewer.addContent("~ ¡Empate! :_(" + System.getProperty("line.separator"));
					if(e.getState().getTurn() == this.gameView.gameCtrl.getPlayerId()) this.gameView.gameCtrl.stopGame();
				}
				else if (e.getState().getWinner() == this.gameView.gameCtrl.getPlayerId()) {
					this.gameView.messageViewer.addContent("~ ¡Enhorabuena, has ganado! :D" + System.getProperty("line.separator"));
					this.gameView.gameCtrl.stopGame();
				}
				else 
					this.gameView.messageViewer.addContent("~ Lo sentimos, has perdido :_( " + System.getProperty("line.separator"));
			}
			else {
				if (e.getState().getTurn() == this.gameView.gameCtrl.getPlayerId()) this.gameView.messageViewer.addContent("~ Tu turno" + System.getProperty("line.separator"));
				else this.gameView.messageViewer.addContent("~ Turno del otro jugador" + System.getProperty("line.separator"));
			}
			if (this.enabled) disable();
			else enable();
			break;
		case Error:
			this.gameView.messageViewer.addContent(e.toString() + System.getProperty("line.separator"));
			break;
		case Info:
			this.gameView.messageViewer.addContent(e.toString() + System.getProperty("line.separator"));
			break;
		case Start:
			this.gameView.messageViewer.setContent(e.toString() + System.getProperty("line.separator"));
			if (e.getState().getTurn() == this.gameView.gameCtrl.getPlayerId()) 
				this.gameView.messageViewer.addContent("~ Tu turno" + System.getProperty("line.separator"));
			else 
				this.gameView.messageViewer.addContent("~ Turno del otro jugador" + System.getProperty("line.separator"));
			//Comprobamos si es la jugada inicial de la ejecución
			if (this.ini) {
				if (!this.enabled) disable();
				ini = false;
				break;
			}
			else {
				//Cambiamos las ventanas si el frameTurn no coincide
				if (frameTurn == 0 && !this.enabled) enable();
				else if (frameTurn == 1 && this.enabled) disable();
			}
			this.gameView.controlPanel.changeToManual(); //Cambia el modo de jugador a manual y pone la etiqueta adecuada
			break;
		case Stop:
			this.gameView.messageViewer.addContent(e.toString() + System.getProperty("line.separator"));
			disable();
			break;
		default:
			this.gameView.messageViewer.addContent(e.toString() + System.getProperty("line.separator"));
			break;
		}
		changeFrameTurn();
		update(e.getState());
		SwingUtilities.invokeLater(new Runnable() {
			public void run() { gameView.gameCtrl.handleEvent(e);}
		});
	}
	
	/**
	 * Cambia el valor de frameTurn dependiendo de qué ventana es la que está activa
	 */
	public void changeFrameTurn() {
		if (GameContainer.frameTurn == 0) 
			GameContainer.frameTurn = 1;
		else GameContainer.frameTurn = 0;
	}

	/**
	 * LLama al enable de los componentes que se tienen que poner en enable y cambia el valor del boleano a verdadero
	 */
	@Override
	public void enable() {
		this.gameView.controlPanel.enable();
		this.gameView.enable();
		this.enabled = true;
	}

	/**
	 * LLama al disable de los componentes que se tienen que poner en disable y cambia el valor del boleano a falso
	 */
	@Override
	public void disable() {
		this.gameView.controlPanel.disable();
		this.gameView.disable();
		this.enabled = false;
	}
	
	/**
	 * LLama a todos los updates de las clases que extienden a gameView
	 */
	public void update(S state) {
		this.gameView.playersInfoViewer.update(state);
		this.gameView.update(state);
	}

}
