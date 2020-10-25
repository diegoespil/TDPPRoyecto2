package Logica;

import javax.swing.JLabel;


/**
 * Esta clase representa una celda de la grilla. Su funcion es mantener un valor entero de 0 a 9, y a traves de ciertos metodos
 * hacer representar graficamente ese valor. Ademas cuenta con dos enteros x e y que representan su posicion en la grilla.
 * Mantiene la cantidad de valores que se le asigna, para poder determinar en el juego si es un acierto o no.
 * @author diego
 *
 */
public class Celda {
	private Integer valor;
	private EntidadGrafica imagen;
	private Integer x;
	private Integer y;
	private boolean editable;
	private JLabel label; 
	private int cantValores;
	
	/**
	 * Constructor que inicializa los antributos
	 */
	public Celda() {
		this.valor = null;
		this.x = null;
		this.y = null;
		this.imagen = new EntidadGrafica();
		this.editable = false;
		this.label = null;
		this.cantValores = 0;
	}
	
	/**
	 * Actualiza su imagen a través del valor recibido
	 * @param valor es un entero de 0 a 9
	 */
	public void actualizar(int valor) {
		imagen.actualizar(valor);
	}
	
	/**
	 * Setea el valor con el valor recibido
	 * @param valor entero de 0 a 9
	 */
	public void setValor(int valor) {
		this.valor = valor;
	}

	/**
	 * Retorna el valor de la celda
	 * @return Integer 
	 */
	public Integer getValor() {
		return this.valor;
	}

	/**
	 * Retorna un objecto de tipo EntidadGrafica
	 * @return objecto de tipo EntidadGrafica
	 */
	public EntidadGrafica getEntidadGrafica() {
		// TODO Auto-generated method stub
		return this.imagen;
	}

	/**
	 * Retorna un entero x que representa la posicion x de la celda en la grilla
	 * @return un entero
	 */
	public int getX() {
		// TODO Auto-generated method stub
		return this.x;
	}
	
	/**
	 * Retorna un entero y que representa la posicion y de la celda en la grilla
	 * @return un entero
	 */
	public int getY() {
		// TODO Auto-generated method stub
		return this.y;
	}
	
	/**
	 * Setea el valor de x
	 * @param x es un entero
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * Setea el valor de y
	 * @param y es un entero
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * Retorna un booleano que indica si la celda es editable o no
	 * @return true si es editable, false en caso contrario
	 */
	public boolean getEditable() {
		return this.editable;
	}
	
	/**
	 * Setea la celda si es editable o no
	 * @param edit es un boolean 
	 */
	public void setEditable(boolean edit) {
		this.editable=edit;
	}
	
	/**
	 * Retorna un objeto de tipo JLabel de la celda
	 * @return JLabel
	 */
	public JLabel getLabel() {
		return label;
	}
	
	/**
	 * Setea el label de la celda
	 * @param label de tipo JLabel
	 */
	public void setLabel(JLabel label) {
		this.label = label;
	}
	
	/**
	 * Incrementa la cantidad de valores que se setean a la celda 
	*/
	public void addValues() {
		this.cantValores++;
	}
	
	/**
	 * Retorna la cantidad de valores de la celda
	 * @return un entero 
	 */
	public int getValues() {
		return this.cantValores;
	}
}
