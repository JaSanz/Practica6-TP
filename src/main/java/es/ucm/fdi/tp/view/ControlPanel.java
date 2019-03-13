package es.ucm.fdi.tp.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.view.GameController.PlayerMode;

public class ControlPanel<S extends GameState<S, A>, A extends GameAction<S, A>> extends JToolBar {

	private static final long serialVersionUID = 1L;
	
	private GameController<S, A> gameCtrl;
	private SmartPanel<S, A> panel;
	
	boolean enabled;
	private String[] modos = {"Manual", "Smart", "Random"};
	
	private JPanel finalPanel;

	private JButton randomButton, smartButton, restartButton, exitButton;
	private JLabel playerMode;
	private JComboBox<String> menu = new JComboBox<>(modos);
	
	public ControlPanel(GameController<S, A> gameCtrl) {
		this.enabled = true;
		this.gameCtrl = gameCtrl;
		finalPanel = new JPanel();
		this.panel = new SmartPanel<S, A>(this.gameCtrl);
		initGUI();
	}
	
	/**
	 * Crea a barra de botones 
	 */
	private void initGUI() {
		JToolBar toolBar = new JToolBar();
		JPanel choosePanel = new JPanel();
		finalPanel.setLayout(new BoxLayout(finalPanel, BoxLayout.Y_AXIS));
		
		toolBar.setOpaque(true);
		
		//Botón para realizar un movimiento aleatorio
		this.randomButton = new JButton();
		this.randomButton.setIcon(new ImageIcon("src/main/resources/dice.png"));
		this.randomButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameCtrl.makeRandonMove();
			}
		});
		toolBar.add(randomButton);
		
		//Botón para realizar un movimiento inteligente
		this.smartButton = new JButton();
		this.smartButton.setIcon(new ImageIcon("src/main/resources/nerd.png"));
		this.smartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameCtrl.makeSmartMove();
			}
		});
		toolBar.add(smartButton);
		
		//Botón para reiniciar la partida
		this.restartButton = new JButton();
		this.restartButton.setIcon(new ImageIcon("src/main/resources/restart.png"));
		this.restartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enableWhileThinking();
				gameCtrl.restartGame();
			}
		});
		toolBar.add(restartButton);
		
		//Botón para salir de la partida
		this.exitButton = new JButton();
		this.exitButton.setIcon(new ImageIcon("src/main/resources/exit.png"));
		this.exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enabled = true;
				if(enabled) {
					int n = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas cerrar la aplicación?",
							"Salir", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
					if (n == JOptionPane.YES_NO_OPTION) {
						gameCtrl.stopGame();
						System.exit(0);
					}
					else enabled = false;
				}
				else enabled = false;
			}
		});
		toolBar.add(exitButton);
		
		playerMode = new JLabel("Modo de jugador:");
		playerMode.setLocation(0, 0);
		choosePanel.add(playerMode);
		
		JComboBox<String> tipo = createTipoJugador();
		choosePanel.add(tipo);
		finalPanel.add(toolBar);
		finalPanel.add(choosePanel);
		
		this.add(finalPanel);
		this.add(panel);
		this.setVisible(true);
	}
	
	/**
	 * Desplegable con los tipos de jugador, al seleccionar uno se cambia el modo de juego de ese jugador al tipo seleccionado
	 */
	public JComboBox<String> createTipoJugador() {
		
		menu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String s = (String) menu.getSelectedItem();
				
				if (s == "Manual") gameCtrl.changePlayerMode(PlayerMode.MANUAL);
				if (s == "Random") gameCtrl.changePlayerMode(PlayerMode.RANDOM);
				if (s == "Smart") gameCtrl.changePlayerMode(PlayerMode.SMART);
				
			}
		});
		
		return menu;
	}
	
	/**
	 * Cambia el modo de juego a manual
	 */
	public void changeToManual() {
		gameCtrl.changePlayerMode(PlayerMode.MANUAL);
		this.menu.setSelectedItem("Manual");
	}

	/**
	 * Habilita los botones 
	 */
	@Override
	public void enable() {
		this.smartButton.setEnabled(true);
		this.randomButton.setEnabled(true);
		this.enabled = true;
	}

	/**
	 * Deshabilita los botones 
	 */
	@Override
	public void disable() {
		this.smartButton.setEnabled(false);
		this.randomButton.setEnabled(false);
		this.enabled = false;
	}
	
	/**
	 * Habilita los botones de jugada inteligente, aleatoria y el menú cuando se ha interrumpido el pensamiento. Además deshabilita los botones de pensamiento
	 */
	public void enableWhileThinking() {
		this.smartButton.setEnabled(true);
		this.randomButton.setEnabled(true);
		this.menu.setEnabled(true);
		this.panel.disableWhileThinking();
	}
	
	/**
	 * Deshabilita los botones de jugada inteligente, aleatoria y el menú cuando se está pensando. Además habilita los botones de pensamiento para poder interrumpir el pensamiento
	 */
	public void disableWhileThinking() {
		this.smartButton.setEnabled(false);
		this.randomButton.setEnabled(false);
		this.menu.setEnabled(false);
		this.panel.enableWhileThinking();
	}

}
