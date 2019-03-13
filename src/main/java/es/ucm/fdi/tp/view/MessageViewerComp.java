package es.ucm.fdi.tp.view;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;

public class MessageViewerComp<S extends GameState<S, A>, A extends GameAction<S, A>> extends MessageViewer<S, A> {
	
	private static final long serialVersionUID = 1L;

	private JTextArea msgArea;
	
	public MessageViewerComp() {
		initGUI();
	}
	
	/**
	 * Crea el área de texto 
	 */
	private void initGUI() {
		this.msgArea = new JTextArea();
		this.msgArea.setEditable(false);
		this.msgArea.setLineWrap(true);
		this.msgArea.setWrapStyleWord(true);
		
		JScrollPane barra = new JScrollPane(this.msgArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		barra.setPreferredSize(new Dimension(200, 300));

		this.setBorder(new TitledBorder("Estado de juego"));
		this.add(barra);
		this.setVisible(true);
		
	}

	/**
	 * Añade un mensaje al contenido del área de texto
	 */
	@Override
	public void addContent(String msg) {
		msgArea.setText(msgArea.getText() + msg);
	}
	
	public void addContent(int msg) {
		msgArea.setText(msgArea.getText() + msg);
	}

	/**
	 * Borra el contenido del área de testo y añade un mensaje
	 */
	@Override
	public void setContent(String msg) {
		msgArea.setText(msg);
	}

	@Override
	public void enable() {}

	@Override
	public void disable() {}

	@Override
	public void update(S state) {}

	@Override
	public void setMessageViewer(MessageViewer<S, A> infoViewer) {}

	@Override
	public void setPlayersInfoViewer(PlayersInfoViewer<S, A> playersInfoViewer) {}

	@Override
	public void setGameController(GameController<S, A> gameCtrl) {}

}
