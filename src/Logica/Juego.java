package Logica;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Esta clase representa la logica del juego, se encarga de mantener el estado interno del juego.  
 * Incializa el juego mediante un archivo de texto con un formato de 9 filas por 9 columnas, constituido por numeros 
 * separados por un espacio, de no mantener este formato el juego no inicia. El juego consiste en completar las celdas 
 * con los numeros ubicados en las opciones, de forma que respete las reglas de no repetirse en la fila, columna y cuadrante, 
 * de producirse un error, la celda se marca en color rojo, y hasta no colocar un numero correcto en esa celda no puede continuar. 
 * El juego termina cuando el jugador completa todas las celdas.
 *
*/
public class Juego {
	
	private Celda grilla[][];
	private Celda numeros[];
	private int filas;
	private boolean valido;
	private boolean win;
	private int aciertos;
	private int celdasVacias;
	private static Logger logger;
	private boolean archivoValido;
	
	/**
	 * Constructor
	 * @param int eliminar, especifica cuantas celdas van a hacer eliminadas para comenzar el juego.
	*/
	public Juego( int eliminar) {

		if (logger == null){			
			logger = Logger.getLogger(Juego.class.getName());			
			Handler hnd = new ConsoleHandler();
			hnd.setLevel(Level.FINE);			
			logger.addHandler(hnd);			
			logger.setLevel(Level.FINE);			
			Logger rootLogger = logger.getParent();
			for (Handler h : rootLogger.getHandlers()){
				h.setLevel(Level.OFF);
			}
		}
		this.archivoValido = true;
		this.filas = 9;
		this.grilla = new Celda[this.filas][this.filas]; 
		this.numeros = new Celda[10];
		this.valido = true;
		this.win = false;
		this.aciertos = 0;
		this.celdasVacias = eliminar;
		
		//inicializo el panel de numeros
		for(int i =0 ; i< numeros.length ; i++) {
			numeros[i] = new Celda();
			numeros[i].setValor(i);
			numeros[i].actualizar(i);
		}
		
		//inicializo la grilla con Celdas
		for (int i=0; i< this.filas; i++)
			for (int j=0; j<this.filas;j++) {
				grilla[i][j] = new Celda();
				grilla[i][j].setX(i);
				grilla[i][j].setY(j);
			}
		
		this.leerArchivo();
		if (archivoValido) 
			if (this.solucionValida()) {
				this.eliminarCeldas(celdasVacias);
			}
	}
	
	/**
	 * Retorna verdadero si es un archivo valido, falso en caso contrario
	*/
	public boolean archivoValido() {
		return this.archivoValido;
	}
	
	/**
	 * Retorna un elemento de tipo Celda de la grilla, se asume que x e y son validos.
	 * @param x entero que representa una fila de la grilla
	 * @param y entero que representa una columna de la grilla
	*/
	public Celda getCelda(int x, int y) {
		return grilla[x][y];
	}
	
	/**
	 * Retorna un arreglo de elementos de tipo Celda que representa las opciones del usuario para elegir.
	*/
	public Celda[] getNumeros() {
		return this.numeros;
	}
	
	/**
	 * Elimina una cantidad determinada de elementos de la grilla. De manera aleatoria se eligen las celdas de la grilla
	 * y se establece su valor en 0.
	 * @param celdasElim entero que representa las celdas a eliminar.
	 */
	private void eliminarCeldas(int celdasElim) {
		int elim = 0 ;
		Random r = new Random();
		while  (elim < celdasElim) {
			int x = r.nextInt(9);
			int y = r.nextInt(9);
			if (grilla[x][y].getValor() != 0) {
				grilla[x][y].setValor(0);
				grilla[x][y].actualizar(0);
				grilla[x][y].setEditable(true);
				elim++;
			}
		}
	}

	/**
	 * Retorna verdadero si es una solucion valida de sudoku el archivo de texto.
	 * @return true si es un archivo valido, y false si no lo es.
	 */
	private boolean solucionValida() {
		boolean esValido = true;
		int x = 0;
		int y = 0;
		while (esValido && x < 9) {
			y = 0;
			while (esValido && y < 9) {
				if (y < 8) 
					esValido = checkFila(grilla[x][y]);
				if (esValido && x < 8) 
					esValido = checkColumna(grilla[x][y]);
				if (esValido)
					esValido = checkCuadrante(grilla[x][y], getCuadrante(x,y));
				y++;
				}
			x++;
			}
		return esValido;
	}
	
	/**
	 * Retorna verdadero si en el cuadrante no esta la celda que recibo como parametro
	 * @param celda es un objeto de tipo Celda
	 * @param cuadrante es una matriz de objetos de tipo Celda
	 * @return true si no esta celda en el cuadrante, false en caso contrario
	 */
	private boolean checkCuadrante(Celda celda, Celda [][] cuadrante) {
		boolean valido = true;
		int valor = celda.getValor();
		int i = 0;
		int j = 0;
		int cantApariciones = 0 ;
		while (valido && i < 3) {
			j=0;
			while (valido && j < 3) {
				if (valor != 0) {
					if (valor == cuadrante[i][j].getValor()) {
						cantApariciones++;
					}
					if (cantApariciones > 1) {
						valido = false;
					}
				}
				j++;
			}
			i++;
		}
		return valido;
	}

	/**
	 * Retorna una matriz de objetos de tipo Celda, que representan un cuadrante de la grilla.
	 * @param x es un entero que representa un numero de una fila de la grilla
	 * @param y es un entero que representa un numero de una columna de la grilla
	 * @return una matriz de objetos de tipo Celda
	 */
	public Celda[][] getCuadrante(int x, int y) {
		int fila = ((int) x/3) * 3;
		int columna = ((int) y/3) * 3;
		return cuadrante(fila,columna);
	}

	/**
	 * Crea una matriz de objetos de tipo Celda, a partir de la grilla representando una copia de un cuadrante.
	 * @param i es un entero que representa un numero de una fila de la grilla
	 * @param j es un entero que representa un numero de una columna de la grilla
	 * @return
	 */
	private Celda[][] cuadrante(int i, int j) {
		Celda [][] cuadrante = new Celda[(int) Math.sqrt(this.getCantidadFilas())][(int) Math.sqrt(this.getCantidadFilas())];
		
		for (int x= 0; x < 3; x++) 
			for (int y =0; y < 3; y++) {
				cuadrante[x][y]= grilla[i+x][j+y];
			}
		return cuadrante;
	}

	/**
	 * Retorna verdadero si la columna de la grilla no contiene la celda recibida como parametro.
	 * @param celda objeto de tipo Celda
	 * @return true si la columna no tiene ningun valor que sea igual al valor que contiene la celda, false en caso contrario.
	 */
	private boolean checkColumna(Celda celda) {
		boolean valido = true;
		int valor = celda.getValor();
		int x = celda.getX();
		int y = celda.getY();
		int i = 0;
		while (valido && i < 9) {
			if (i != x && valor != 0)
				valido = valor != grilla[i][y].getValor();
			i++;
		}
		return valido;
	}


	/**
	 * Retorna verdadero si la fila de la grilla no contiene la celda recibida como parametro.
	 * @param celda objeto de tipo Celda
	 * @return true si la fila no tiene ningun valor que sea igual al valor que contiene la celda, false en caso contrario.
	 */
	private boolean checkFila(Celda celda) {
		boolean valido = true;
		int valor = celda.getValor();
		int x = celda.getX();
		int y = celda.getY();
		int i = 0;
		while (valido && i < 9) {
			if (i != y && valor != 0)
				valido = valor != grilla[x][i].getValor();
			i++;
		}
		return valido;
	}


	/**
	 * Metodo que lee el archivo de texto para iniciar el estado interno del juego.
	 * Si es un archivo que no respeta el formato de 9 filas por 9 columnas, donde
	 * cada fila contiene 9 digito del 1 al 9 separados por un espacio, entonces cambia 
	 * el valor a false de la variable archivoValido.
	 */
	private void leerArchivo() {
		InputStream in = null;
	    FileReader fr = null;
	    InputStreamReader sr = null ;
	    BufferedReader br = null;
	    Random r = new Random();
	    int random = r.nextInt(2)+1;
	      try {
	    	 in = Juego.class.getClassLoader().getResourceAsStream("archivos/sudoku"+random);
	         sr = new InputStreamReader(in);
	         br = new BufferedReader(sr);

	         // Lectura del archivo
	         String linea = br.readLine();
	         System.out.println("linea "+linea);
	         int contLineas = 0;
	         if (linea.length() != 17) 
	        	 this.archivoValido = false;
	         int x=0;
	         while(linea!=null && archivoValido) {
	        	 contLineas++;
	        	 int y= 0;
	        	 int num = 0;
	        	 while (y < linea.length()) {
	        		 if (y % 2 == 0) { 
	        			 int n = linea.charAt(y)-48;
	        			 if (n > 0 && n < 10) {
	        				 grilla[x][num].setValor(n);
		        			 grilla[x][num].actualizar(n);
		        			 grilla[x][num].setEditable(false);
		        			 num++;
	        			 } else 
	        				 this.archivoValido = false;}
	        		 else 
	        			 if (linea.charAt(y) != ' ') 
	        				 this.archivoValido = false;
	        		 
	        		 y++;
	        	 }
	        	 linea = br.readLine();
	        	 System.out.println("linea "+linea);
	        	 if (linea != null)
		        	 if (linea.length() != 17) 
			        	 this.archivoValido = false;
	        	 x++;
	        	 if (x == 9 && linea != null )
	        		 this.archivoValido = false;
	          }
	         if (contLineas < 9)
	        	 this.archivoValido =false;
	      }
	      catch(Exception e){
	         e.printStackTrace();
	      }finally{
	         try{                    
	            if( fr != null ){   
	               fr.close();     
	            }                  
	         }catch (Exception e2){ 
	            e2.printStackTrace();
	         }
	      }
	}

	/**
	 * Retorna la cantidad de filas de la grilla
	 * @return un entero que representa cuantas filas tiene la grilla
	 */
	public int getCantidadFilas() {
		return this.filas;
	}
	
	/**
	 * Este metodo se encarga de cambiar el estado interno del juego. Dados un objeto de tipo Celda y un entero, 
	 * se cambia el valor de la celda en la grilla que corresponde con el valor requerido. Si no es una jugada valida
	 * para el sudoku, se cambia el estado del juego a false a traves del metodo setValido. Si es una juagda valida
	 * entonces setValido es verdadero, si valor es distinto a cero (representa celda vacia), a la celda le incremento sus valores.
	 * Si la cantidad de valores de la celda es 1, entonces incremento los aciertos, para luego chequear si los aciertos es igual a 
	 * las celdas vacias, de ser asi entonces establezco en verdadero una variable para indicar que gane el juego.
	 * Luego actualizo la celda con el valor recibido.
	 * @param c objeto de tipo Celda
	 * @param valor entero de 0 a 9
	 */
	public void jugar(Celda c, int valor) {
		
		grilla[c.getX()][c.getY()].setValor(valor);
		if (!jugadaValida(c,valor)) {
			setValido(false);
		} else {
			setValido(true);
			if (valor != 0)
				c.addValues();
			if (c.getValues() == 1)
				this.aciertos++;
			if (aciertos == celdasVacias) {
				this.win = true;
			}
		}
		c.actualizar(valor);
	}
	
	/**
	 * Retorna verdadero si el juego terminó
	 * @return true si win es verdadero o false en caso contrario
	 */
	public boolean gameOver() {
		return this.win;
	}

	/**
	 * Cambia el valor de la variable valido. 
	 * @param valido booleano que indica si es valido o no el juego
	 */
	public void setValido(boolean valido) {
		this.valido = valido;
		
	}
	
	/**
	 * Obtiene el valor de la variable valido, que representa el estado del juego
	 * @return true si el juego esta en estado valido, false en caso contrario
	 */
	public boolean esValido() {
		return this.valido;
	}

	/**
	 * Retorna verdadero si el valor que quiero ingresar en el juego, es una jugada valida del sudoku.
	 * Esto es chequear que el valor no este en la fila, columna y cuadrante.
	 * @param celda objeto de tipo Celda
	 * @param valor entero que representa el valor que se quiere ingresar
	 * @return true si es una jugada valida, false en caso contrario
	 */
	private boolean jugadaValida(Celda celda, int valor) {
		int x = celda.getX();
		int y = celda.getY();
		if (checkFila(celda) && 
				checkColumna(celda) &&
				checkCuadrante(celda,getCuadrante(x,y)))
			return true;
		return false;
	}

	/**
	 * Retorna un arreglo de objetos de tipo Celda, representado una fila de la grilla
	 * @param x entero que representa un numero de fila de la grilla
	 * @return array de objetos de tipo Celda
	 */
	public Celda[] getFilas(int x) {
		return this.grilla[x];
	}

	/**
	 * Retorna un arreglo de objetos de tipo Celda, representado una columna de la grilla. 
	 * @param y entero que representa un numero de columna de la grilla
	 * @return array de objetos de tipo Celda
	 */
	public Celda[] getColumna(int y) {
		Celda [] columnas = new Celda[this.getCantidadFilas()];
		for (int i = 0; i < this.getCantidadFilas(); i++)
			columnas[i] = grilla[i][y];
		return columnas;
	}

	
}
