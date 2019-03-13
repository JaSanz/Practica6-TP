package es.ucm.fdi.tp.launcher;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.swing.*;

import es.ucm.fdi.tp.base.console.ConsolePlayer;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GamePlayer;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.base.player.ConcurrentAiPlayer;
import es.ucm.fdi.tp.base.player.RandomPlayer;
import es.ucm.fdi.tp.mvc.GameTable;
import es.ucm.fdi.tp.ttt.TttState;
import es.ucm.fdi.tp.view.ConsoleController;
import es.ucm.fdi.tp.view.ConsoleView;
import es.ucm.fdi.tp.view.GUIController;
import es.ucm.fdi.tp.view.GUIView;
import es.ucm.fdi.tp.view.GameContainer;
import es.ucm.fdi.tp.view.GameController;
import es.ucm.fdi.tp.view.TttView;
import es.ucm.fdi.tp.view.WasView;
import es.ucm.fdi.tp.was.WolfAndSheepState;

public class Main {
	
	public static String[] nombres = {"Superman","Batman"};
	
	private static WolfAndSheepState wasState = new WolfAndSheepState(8);
	private static TttState tttState = new TttState(3);
	
	/**
	 * Crea el estado inicial de la partida.
	 * @param gameName El nombre del juego. Puede ser was (wolf and sheep) o ttt (tick tack toe). Si no es ninguno de los dos,
	 * muestra un mensaje de error.
	 * @return Devuelve el estado del juego inicial.
	 */
	private static GameTable<?, ?> createGame(String gType) {
		
		GameTable<?, ?> game = null;
		
		if (gType.equalsIgnoreCase("ttt")){
			game = new GameTable<>(tttState);
		}	
		else if (gType.equalsIgnoreCase("was")){
			game = new GameTable<>(wasState);
		}
		else System.out.println("Error: juego no encontrado");
		
		return game;
		// create a game with a GameState depending on the value of gType
	}
	
	/**
	 * Crea los jugadores según el tipo que sean y les dota de nombre. Si no existe el jugador, muestra un mensaje de error.
	 * @param gameName Nombre del juego (ttt ó was)
	 * @param playerType Tipo de jugador. Puede ser aleatorio (rand), inteligente (smart) o jugador de consola (console).
	 * @param playerName Nombre del jugador. Puede ser Superman ó Batman.
	 * @return Devuelve el jugador ya creado.
	 */
	public static GamePlayer createPlayer( String playerType, String playerName){
		Scanner s = new Scanner(System.in);
		GamePlayer player = null;
		
		if (playerType.equalsIgnoreCase("rand")) {
			player = new RandomPlayer(playerName);
		} else if (playerType.equalsIgnoreCase("manual")) {
			player = new ConsolePlayer(playerName,s);
		} else if (playerType.equalsIgnoreCase("smart")) {
			player = new ConcurrentAiPlayer(playerName);
		} else System.out.println("Error: jugador " + playerType + " no definido");
	
		return player;
	
	}
	
	/**
	 * Crea la vista del juego seleccionado
	 * @param gType es el nombre del juego que se desea crear
	 * @return devuelve la vista creada 
	 */
	public static GUIView<?, ?> createGameView(String gType) {
		
		GUIView<?, ?> view = null;
		
		if (gType.equalsIgnoreCase("was")) {
			view = new WasView(wasState); 
		} else if (gType.equalsIgnoreCase("ttt")) {
			view = new TttView(tttState);
		}
			
		return view;
	}
	
	/**
	 * Inicia le juego en modo consola, crea los jugadores, les añade a la partida y lanza la partida 
	 * @param gType el nombre del juego que se desea jugar
	 * @param game el motor del juego
	 * @param playerModes el modo de juego con el que desea jugar cada jugador al iniciarse
	 */
	private static <S extends GameState<S, A>, A extends GameAction<S,A>> void startConsoleMode(String gType, GameTable<S, A> game, String playerModes[]) {
		List<GamePlayer> players = new ArrayList<GamePlayer>();	
		
		GamePlayer player = null;
		player = createPlayer(playerModes[0], nombres[0]);
		players.add(player);
		GamePlayer player1 = null;
		player1 = createPlayer(playerModes[1], nombres[1]);
		players.add(player1);
		
		new ConsoleView<S,A>(game);
		new ConsoleController<S,A>(players,game).run();
	}
	
	/**
	 * Inicializa los juegos en modo GUI, crea a los jugadores, les une a la partida, crea el controlador, crea la vista y lanza el juego
	 * @param gType el nombre del juego que quieres jugar
	 * @param game el tabero del juego seleccionado
	 * @param playerModes el modo de juego de los jugadores con el que se quiere iniciar, en el GUI siempre se inicia en Manual
	 */
	private static <S extends GameState<S, A>, A extends GameAction<S,A>> void startGUIMode(String gType, GameTable<S, A> game, String playerModes[]) {
		for (int i = 0; i < game.getState().getPlayerCount(); i++) {
			final int p = i; 
			GamePlayer p1;
			GamePlayer p2;
			p1 = createPlayer("rand", nombres[0]);
			p2 = createPlayer("smart", nombres[1]);
			p1.join(i);
			p2.join(i);
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						GameController<S, A> gameCtrl = new GUIController<S, A>(p, p1, p2, game);
						GUIView<S,A> guiView = (GUIView<S, A>) createGameView(gType);
						guiView.setGameController(gameCtrl);
						GameContainer<S,A> container = new GameContainer<>(guiView, gameCtrl, game);
						if (p == 1) {
							container.setEnabled(false);
							container.setLocation(container.getWidth(), 0);
						}
					}
				});
			 }
			 catch (InvocationTargetException | InterruptedException e) {
				 System.err.println("Some error occurred while creating the view ...");
				 System.exit(1);
			 }
		}
		SwingUtilities.invokeLater(new Runnable() { public void run() { game.start();} });
	}
	
	/**
	 * En caso de recibir algún tipo de dato incorrecto, se llama a usage para imprimir datos incorrectos
	 */
	private static void usage() {
		System.out.println("Datos incorrectos");
	}

	/**
	 * Método principal del programa. Aquí comienza la ejecución.
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 2) {
			usage();
			System.exit(1);
		}
		GameTable<?, ?> game = createGame(args[0]);
		if (game == null) {
			System.err.println("Invalid game");
			usage();
			System.exit(1);
		}
		String[] otherArgs = Arrays.copyOfRange(args, 2, args.length);
		switch (args[1]) {
			case "console":
				startConsoleMode(args[0],game,otherArgs);
				break;
			case "gui":
				startGUIMode(args[0],game,otherArgs);
				break;
			default:
				System.err.println("Invalid view mode: "+args[1]);
				usage();
				System.exit(1);
		}
	}
	
}