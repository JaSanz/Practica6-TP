package es.ucm.fdi.tp.view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;

public class SmartPanel<S extends GameState<S, A>, A extends GameAction<S, A>> extends JPanel {

	private static final long serialVersionUID = 1L;
	
	boolean enabled;
	
	private GameController<S, A> gameCtrl;

	private JSpinner spinner, ms;
	private JButton stopButton;
	private JLabel picLabel;
	
	/**
	 * Establece un controlador de juego, inicializa las variables y deshabilita la ventana de pensamiento
	 */
	public SmartPanel(GameController<S, A> gameCtrl) {
		this.gameCtrl = gameCtrl;
		this.enabled = true;
		initGUI();
		disableWhileThinking();
	}
	
	/**
	 * Crea los diferentes componentes del panel inteligente
	 */
	private void initGUI() {
		
		//Borde del panel
		this.setBorder(new TitledBorder("Movimientos inteligentes"));
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		//Cerebro
		this.picLabel = new JLabel(new ImageIcon("src/main/resources/brain.png"));
		this.picLabel.setOpaque(true);
		this.picLabel.setBackground(null);
		this.add(this.picLabel);
		
		//Selector de hilos
		this.spinner = new JSpinner(new SpinnerNumberModel(1, 1, Runtime.getRuntime().availableProcessors(), 1));
		this.spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				gameCtrl.setMaxThreads((int) spinner.getValue());
			}
		});
		this.add(spinner);
		
		//Etiqueta de hilos
		this.add(new JLabel("Hilos"));
		
		//Contador
		this.add(new JLabel(new ImageIcon("src/main/resources/timer.png")));
		
		//Selector de tiempo
		this.ms = new JSpinner(new SpinnerNumberModel(500, 500, 5000, 500));
		this.ms.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				gameCtrl.setTimeout((int) ms.getValue());
			}
		});
		this.add(ms);
		this.gameCtrl.setTimeout(500);
		
		//Etiqueta de ms
		this.add(new JLabel("ms."));
		
		//Botón de parar
		this.stopButton = new JButton();
		this.stopButton.setIcon(new ImageIcon("src/main/resources/stop.png"));
		this.stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameCtrl.interruptThinking();
				gameCtrl.enable();
			}
		});
		this.add(this.stopButton);
		
		//Añadimos el panel
		this.setVisible(true);
	}
	
	/**
	 * Habilita el botón de parada mientras está pensando y pone el color del cerebro en amarillo
	 */
	public void enableWhileThinking() {
		this.stopButton.setEnabled(true);
		this.picLabel.setBackground(Color.YELLOW);
	}
	
	/**
	 * Deshabilita el botón de parada y quita el color del cerebro
	 */
	public void disableWhileThinking() {
		this.stopButton.setEnabled(false);
		this.picLabel.setBackground(null);
	}

}
