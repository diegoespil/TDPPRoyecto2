package Logica;

import java.util.TimerTask;
import Gui.GuiGame;
 /**
  * Esta clase representa el tiempo del juego, cada segundo se refleja internamente con su imagen,
  * al heredar de la clase TimerTask, ejecuta una cierta tarea que simula un reloj. Mantiene el estado 
  * del reloj, mediante tres enteros que representan los segundos, minutos y horas. Cada vez que se ejecuta la tarea,
  * se incrementa un segundo, actualizando su imagen, y asi sucesivamente con los minutos y las horas.
  * @author diego
  *
  */
public class TimerGame extends TimerTask {

	private int seconds;
	private int minutes;
	private int hours;
	
	private int contSeconds;
	private int contMinutes;
	private int contHours;
	
	private EntidadGrafica []reloj;
	private GuiGame gui;
	
	/**
	 * Constructor de la clase que inicializa los atributos.
	 * @param gui objecto de tipo GuiGame 
	 */
	public TimerGame(GuiGame gui) {
		this.gui = gui;
		this.seconds = 0;
		this.minutes = 0;
		this.hours = 0;
		this.contSeconds = 0;
		this.contMinutes = 0;
		this.contHours = 0;
		reloj = new EntidadGrafica[8];
		for (int i=0; i < reloj.length; i++) {
			reloj[i] = new EntidadGrafica();
			if ( i  == 2 || i == 5) {
				reloj[i].actualizar(11);
			} else
				reloj[i].actualizar(10);
		}
	}
	
	/**
	 * Metodo sobrescrito de la clase TimerTask, para ejecutar una cierta tarea
	 */
    @Override
    public void run() {
    	gui.actualizarReloj(reloj);
    	incrementSeconds();
    }

    /**
     * Metodo para incrementar segundos, y actualizar las imagenes de los segundos
     * Si llega a 60 segundos, incrementa los minutos
     */
	private void incrementSeconds() {
		this.contSeconds++;
		this.seconds++;
		if (this.contSeconds == 10) {
			if (this.seconds != 60) {
				reloj[6].actualizar(seconds/10);
				reloj[7].actualizar(10);//Cero
				
			} else {
				reloj[6].actualizar(10);
				reloj[7].actualizar(10);//Cero
				this.seconds = 0;
				incrementsMinutes();
			}			
			this.contSeconds = 0;
			
		} else {
			reloj[7].actualizar(this.contSeconds);
		}
	}
	
	/**
	 * Metodo que incrementa los minutos, y actualiza la imagen de los minutos. 
	 * Si llega a 60 minutos, incrementa las horas.
	 */
	private void incrementsMinutes() {
		this.contMinutes++;
		this.minutes++;
		if (this.contMinutes == 10) {
			if (this.minutes != 60) {
				reloj[4].actualizar(10);//Cero
				reloj[3].actualizar(minutes/10);
				
			} else {
				reloj[4].actualizar(10);//cero
				reloj[3].actualizar(10);//cero
				this.minutes = 0;
				incrementsHours();
			}			
			this.contMinutes = 0;
			
		} else {
			reloj[4].actualizar(this.contMinutes);
		}
		
	}

	/**
	 * Metodo que incrementa las horas, y actualiza la imagen de las horas.
	 * Si llega a 99 horas, vuelve a cero las horas, minutos y segundos.
	 */
	private void incrementsHours() {
		this.contHours++;
		this.hours++;
		if (this.contHours == 10) {
			if (this.hours != 100) {
				reloj[1].actualizar(10);//Cero
				reloj[0].actualizar(hours/10);
				
			} else {
				reset();
			}			
			this.contHours = 0;
		} else {
			reloj[1].actualizar(this.contHours);
		}
	}
	
	/**
	 * Metodo que retorna un array de objectos de tipo EntidadGrafica
	 * @return array de objectos de tipo de EntidadGrafica
	 */
	public EntidadGrafica [] getReloj() {
		return this.reloj;
	}
	
	/**
	 * Metodo que reseta el reloj a cero.
	 */
	public void reset() {
		reloj[0].actualizar(10);//cero
		reloj[1].actualizar(10);//cero
		reloj[3].actualizar(10);//cero
		reloj[4].actualizar(10);//cero
		reloj[6].actualizar(10);//cero
		reloj[7].actualizar(10);//cero
		this.seconds = 0;
		this.contMinutes = 0;
		this.contHours = 0;
	}

}
