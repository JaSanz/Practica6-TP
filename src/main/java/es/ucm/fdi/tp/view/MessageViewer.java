package es.ucm.fdi.tp.view;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;

public abstract class MessageViewer<S extends GameState<S, A>, A extends GameAction<S, A>> extends GUIView<S, A> {

	private static final long serialVersionUID = 1L;

	public MessageViewer() {}
	
	public void setMessageViewer(MessageViewer<S, A> infoViewer) {}
	
	public abstract void addContent(String msg);
	public abstract void setContent(String msg);

}
