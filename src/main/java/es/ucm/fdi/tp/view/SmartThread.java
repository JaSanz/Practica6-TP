package es.ucm.fdi.tp.view;

import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.base.player.ConcurrentAiPlayer;

public class SmartThread<S extends GameState<S, A>, A extends GameAction<S, A>> extends Thread {

	private ConcurrentAiPlayer aiPlayer;
	private GameController<S, A> gameCtrl;
	
	public SmartThread(ConcurrentAiPlayer aiPlayer, GameController<S, A> gameCtrl) {
		this.aiPlayer = aiPlayer;
		this.gameCtrl = gameCtrl;
	}
	
	/**
	 * Ejecuta el hilo
	 */
	public void run() {
		SwingUtilities.invokeLater(new Runnable() {
			
			//Deshabilita el panel de botones e imprime el mensaje correspondiente
			@Override
			public void run() {
				gameCtrl.disable();
				gameCtrl.addContent("~ Pensando..." + System.getProperty("line.separator"));
			}
		});
		
		long horaInicio = System.nanoTime();
		
		//Solicitamos una acción
		A action = aiPlayer.requestAction(this.gameCtrl.getState());
		
		long horaFin = System.nanoTime();
		final long duracion = (horaFin - horaInicio) / 1000000;
		
		if(action != null) {
			SwingUtilities.invokeLater(new Runnable() {
				
				//Si la acción se ha recibido correctamente movemos, habilitamos el panel de botones y mostramos el mensaje
				@Override
				public void run() {
					gameCtrl.addContent("~ " + aiPlayer.getEvaluationCount() + " nodos en " + duracion + " ms (" + aiPlayer.getEvaluationCount() / duracion + " n/ms) value = " + aiPlayer.getValue() + System.getProperty("line.separator"));
					gameCtrl.makeMove(action);
					gameCtrl.enable();
				}
			});
		}
		else {
			SwingUtilities.invokeLater(new Runnable() {
				
				//Si la acción no se ha recibido correctamente detenemos el pensamiento y mostramos el mensaje
				@Override
				public void run() {
					gameCtrl.ThinkingInterrupted();
					gameCtrl.addContent("~ Pensamiento detenido" + System.getProperty("line.separator"));
				}
			});
		}
	}

}
