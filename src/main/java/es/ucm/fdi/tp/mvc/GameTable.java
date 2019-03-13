package es.ucm.fdi.tp.mvc;

import java.util.ArrayList;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameError;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.GameEvent.EventType;

/**
 * An event-driven game engine.
 * Keeps a list of players and a state, and notifies observers
 * of any changes to the game.
 */
public class GameTable<S extends GameState<S, A>, A extends GameAction<S, A>> implements GameObservable<S, A> {

	private S inicial;
	private S actual;
	private GameError error;
	private boolean partidaIniciada;
	private boolean parado;
	private ArrayList<GameObserver<S, A>> observadores;
	
	/**
	 * Se encarga de dar los valores iniciales y de crear la lista de observadores 
	 * @param initState
	 */
    public GameTable(S initState) {
    	this.inicial = initState;
    	this.actual = initState;
    	this.partidaIniciada = false;
    	this.parado = false;
    	this.observadores = new ArrayList<GameObserver<S,A>>();
    }
    
    /**
     * Inicia o reinica el juego
     */
    public void start() {
    	this.actual = this.inicial;
    	
    	if(parado) parado = false;
    	if (!partidaIniciada) notifyObservers(new GameEvent<S, A>(EventType.Start, null, this.actual, null, "~ Juego iniciado"));
    	else notifyObservers(new GameEvent<S, A>(EventType.Start, null, this.actual, null, "~ Juego reiniciado"));
    	 
    	this.partidaIniciada = true;
    }
    
    /**
     * Para el juego, si estaba parado muestra un error
     */
    public void stop() {
    	if (!parado) {
    		notifyObservers(new GameEvent<S, A>(EventType.Stop, null, this.actual, null, "~ Juego parado"));
    		this.parado = true;
    	}
    	else {
    		this.error = new GameError("~ El juego ya habia sido parado con anterioridad");
    		notifyObservers(new GameEvent<S, A>(EventType.Error, null, this.actual, this.error, "~ Error. Juego ya parado"));
    		throw this.error;
    	}
    }
    
    /**
     * Ejecuta una acción, en caso de que no se pueda llevar a cabo lanza una excepción 
     * @param action acción que se quiere llevar a cabo
     */
    public void execute(A action) {
        try {
        	S auxiliar;
        	auxiliar = action.applyTo(this.actual);
        	if (auxiliar == null || !this.partidaIniciada || parado) throw new GameError("~ La accion no se ha podido llevar a cabo");
        	this.actual = auxiliar;
        	notifyObservers(new GameEvent<S, A>(EventType.Change, action, this.actual, null, "~ La accion se ha llevado a cabo correctamente"));
        }
        catch (Exception gameError) {
        	notifyObservers(new GameEvent<S, A>(EventType.Error, null, this.actual, this.error, gameError.getMessage()));
        }
    }
    
    public S getState() {
    	return actual;
    }

    /**
     * Añade un observador a la lista 
     */
    public void addObserver(GameObserver<S, A> o) {
    	observadores.add(o);
    }
    
    /**
     * elimina un observador de la lista
     */
    public void removeObserver(GameObserver<S, A> o) {
    	observadores.remove(o);
    }
    
    /**
     * notifica a todos los observadores, suscritos a la lista de un evento
     * @param evento
     */
    private void notifyObservers(GameEvent<S, A> evento) {
    	for(int i = 0; i < observadores.size(); ++i) observadores.get(i).notifyEvent(evento);
    }
    
}
