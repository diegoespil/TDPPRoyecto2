package Gui;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Logica.Celda;
import Logica.EntidadGrafica;
import Logica.Juego;
import Logica.TimerGame;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

/**
 * 
 * Esta clase representa la interfaz grafica del juego. Consta de un panel para la grilla del sudoku, otro para el reloj,
 * y otro para las opciones del juego. Al empezar el juego, primero debe seleccionar una celda de la grilla, si no lanza un 
 * cartel de advertencia. Una vez seleecionada una celda, debe seleccionar una opcion, si es una jugada valida coloca la opcion
 * en la celda seleccionada, si no coloca la opcion en otro color. Si fue una juagada invalida, debe corregir ese error para seguir
 * avanzando en el juego. Al seleccionar una celda, se marcan las celdas que estan en esa fila, columna y cuadrante. 
 * @author diego
 *
 */
@SuppressWarnings("serial")
public class GuiGame extends JFrame{

	private JPanel contentPane;
	private JPanel panelSudoku;
	private JPanel panelNumeros;
	private JPanel panelReloj;
	private JLabel labelMarcado;
	private Celda celdaAnterior;
	private Celda [] fila;
	private Celda [][] cuadrante;
	private Celda [] columna;
	private Juego juego;
	private Timer timer;
	private TimerGame task;
	private static GuiGame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new GuiGame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GuiGame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100,100,800,600);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		panelSudoku = new JPanel();
		panelSudoku.setLayout(new GridLayout(9, 9, 0, 0));
		inicializarPanelSudoku();
		
		panelNumeros = new JPanel();
		panelNumeros.setBackground(Color.WHITE);
		panelNumeros.setLayout(new GridLayout(5, 2, 0, 0));
		
		
		panelReloj = new JPanel();
		panelReloj.setBackground(null);
		panelReloj.setLayout(new GridLayout(1, 6, 0, 0));
		inicializoPanelReloj();
		
		JLabel lblOpciones =  new JLabel("Opciones");
		lblOpciones.setForeground(Color.BLACK);
		lblOpciones.setFont(new Font("Dialog", Font.BOLD, 14));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(panelSudoku, GroupLayout.PREFERRED_SIZE, 460, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addComponent(panelNumeros, GroupLayout.PREFERRED_SIZE, 275, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
							.addComponent(lblOpciones, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(panelReloj, GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)))
					.addGap(25))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(panelReloj, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)
							.addGap(9)
							.addComponent(lblOpciones, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panelNumeros, GroupLayout.PREFERRED_SIZE, 309, GroupLayout.PREFERRED_SIZE)
							.addGap(0, 0, Short.MAX_VALUE))
						.addComponent(panelSudoku, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 505, GroupLayout.PREFERRED_SIZE))
					.addGap(55))
		);
		contentPane.setLayout(gl_contentPane);
		
		iniciarJuego(40);
		inicializoPanelNumeros();
		iniciarTimer();	
	}
	
	/**
	 * Inicializa el panel del reloj, creando JLabels y añadiendolas.
	 */
	private void inicializoPanelReloj() {
		for (int i= 0;i < 8; i++) {
			panelReloj.add(new JLabel());
		}
	}
	
	/**
	 * Inicializa el panel del sudoku, creando JLabels, añadiendo bordes de acuerdo a su ubicacion
	 * Los bordes que delimitan cuadrantes son remarcados mas fuertes. 
	 */
	private void inicializarPanelSudoku() {
		for (int i=0; i<9;i++)
			for (int j=0; j< 9; j++) {
				JLabel label = new JLabel();
				setBorder(label,i,j);
				panelSudoku.add(label);			
			}
	}
	
	/**
	 * Este metodo crea una instancia de la clase Juego con una determinada cantidad de  celdas  eliminar.
	 * Si el juego tiene un archivo valido, entonces comienza a crear JLabels por cada celda, y a colocarlas en
	 * el panel del juego. Dependiendo su valor, cambia graficamente para diferenciar de las celdas vacias a las 
	 * que no son editables.
	 * @param eliminar
	 */
	private void iniciarJuego(int eliminar) {
		
		labelMarcado = null;
		celdaAnterior = null;
		fila= null;
		cuadrante = null;
		columna = null;
		juego= new Juego(eliminar);
		if (juego.archivoValido()) {
			int componente = 0;
			for (int i=0; i<juego.getCantidadFilas();i++)
				for (int j=0; j< juego.getCantidadFilas(); j++) {
					Celda c = juego.getCelda(i,j);
					System.out.println("Valor "+c.getValor());
					ImageIcon grafico = c.getEntidadGrafica().getGrafico();
					JLabel label = (JLabel) panelSudoku.getComponent(componente);
					c.setLabel(label);
					label.addComponentListener(new ComponentAdapter() {
						@Override
						public void componentResized(ComponentEvent e) {
							reDimensionar(label, grafico);
							label.setIcon(grafico);
						}
					});
					if (c.getEditable()) {
						label.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								if (juego.esValido()) {
									if (celdaAnterior != null) {
										int x = celdaAnterior.getX();
										int y = celdaAnterior.getY();
										setBorder(labelMarcado,x,y);
										desmarcarCeldas(fila, columna, cuadrante);
									}
									labelMarcado = label;
									celdaAnterior = c;
									label.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
									fila = juego.getFilas(c.getX());
									columna = juego.getColumna(c.getY());
									cuadrante = juego.getCuadrante(c.getX(),c.getY());
									marcarCeldas(fila, columna, cuadrante);
								}
							}						
						});
					} else {
						label.setOpaque(true);
						label.setBackground(Color.GRAY);
					}
					componente++;
				}	
		} else {
			JOptionPane.showMessageDialog(null, "Error de archivo.");
			try {
				frame.dispose();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
			
			
	}
	
	/**
	 * Este metodo inicia una instancia de la clase Timer, luego de la clase TimerGame
	 * A travez de los servicios del Timer programo una tarea que se ejecuta cada 1 segundo, despues de que pase un segundo.
	 */
	private void iniciarTimer() {
		timer = new Timer();
		task = new TimerGame(this);
		timer.schedule(task, 1000, 1000);
	}
	
	/**
	 * Metodo que actualiza el reloj.
	 * @param reloj
	 */
	public void actualizarReloj(EntidadGrafica [] reloj) {
		for (int i = 0; i < reloj.length; i++) {			
			ImageIcon grafico = reloj[i].getGrafico();
			JLabel label = (JLabel) panelReloj.getComponent(i);
			label.setIcon(grafico);
			label.repaint();
		}
			
	}
	
	/**
	 * Metodo que marca las celdas de una fila, columna y cuadrante.
	 * @param fila es un array de objectos de tipo Celda
	 * @param columna es un array de objetos de tipo Celda
	 * @param cuadrante es una matriz de objetos de tipo Celda
	 */
	private void marcarCeldas(Celda[] fila, Celda[] columna, Celda[][] cuadrante) {
		for (int i = 0; i < fila.length ; i++) {
			fila[i].getLabel().setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
			columna[i].getLabel().setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
		}
		for (int i = 0 ; i < cuadrante.length; i++)
			for (int j = 0; j < cuadrante.length; j++)
				cuadrante[i][j].getLabel().setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
	}
	
	/**
	 * Metodo que desmarca las celdas de una fila, columna y cuadrante.
	 * @param fila es un array de objectos de tipo Celda
	 * @param columna es un array de objectos de tipo Celda
	 * @param cuadrante es una matriz de objetos de tipo Celda
	 */
	private void desmarcarCeldas(Celda[] fila, Celda[] columna, Celda[][] cuadrante) {
		for (int i = 0; i < fila.length ; i++) {
			setBorder(fila[i].getLabel(),fila[i].getX(),fila[i].getY());
			setBorder(columna[i].getLabel(),columna[i].getX(),columna[i].getY());
		}
		for (int i = 0 ; i < cuadrante.length; i++)
			for (int j = 0; j < cuadrante.length; j++)
				setBorder(cuadrante[i][j].getLabel(),cuadrante[i][j].getX(),cuadrante[i][j].getY());
	}
	
	/**
	 * Metodo que crea bordes a los labels, de acuerdo a en que posicion en la grilla estan los marcas de una 
	 * forma u otra. para marcar los cuadrantes.
	 * @param label objeto de tipo JLabel
	 * @param i entero entre 0 y 8
	 * @param j entero entre 0 y 8
	 */
	private void setBorder(JLabel label, int i, int j) {
		if ((i==2 && j==2) || (i==2 && j==5) || (i==5 && j==2) || (i== 5 && j==5)){
			label.setBorder(BorderFactory.createMatteBorder(1,1,5,5,Color.BLACK));
		} else 
			if (i==2 || i==5) {
				label.setBorder(BorderFactory.createMatteBorder(1,1,5,1,Color.BLACK));
			}else 
				if (j==2 || j==5){
					label.setBorder(BorderFactory.createMatteBorder(1,1,1,5,Color.BLACK));
				} else
					label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
	}

	/**
	 * Metodo que inicializa el panel de opciones. Le solicita al juego el array de Celdas que representan los numeros,
	 * Por cada celda, crea un boton con su imagen. Al momento de accionar alguno de los botones, le envia un mensaje a
	 * la instancia de la clase Juego, y si fue una jugada coloca el valor en la celda, si no coloca el valor y le cambia
	 * el color a rojo, indicando una juaga invalida. En cada jugada valida, le cosulta al juego si gano o no, si gano 
	 * muestra un mensaje que gano y luego le permite volver a jugar o salir del juego.
	 */
	private void inicializoPanelNumeros() {
		Celda [] numeros = juego.getNumeros();
		for (int i= 0; i< numeros.length; i++) {
			ImageIcon grafico = numeros[i].getEntidadGrafica().getGrafico();
			JButton button = new JButton(grafico);
			Integer numero = i;
			this.panelNumeros.add(button);
			button.addMouseListener(new MouseListener() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					Celda celdaSeleccionada = celdaSelected();
					if (celdaSeleccionada != null) {
						juego.jugar(celdaSeleccionada,numero);
						if (juego.esValido()) {
							fila = juego.getFilas(celdaSeleccionada.getX());
							columna = juego.getColumna(celdaSeleccionada.getY());
							cuadrante = juego.getCuadrante(celdaSeleccionada.getX(),celdaSeleccionada.getY());
							desmarcarCeldas(fila,columna,cuadrante);
							celdaAnterior = null;
							JLabel label = celdaSeleccionada.getLabel();
							label.setOpaque(false);
							label.setBackground(null);
							label.repaint();
							reDimensionar(label,celdaSeleccionada.getEntidadGrafica().getGrafico());
							if (juego.gameOver()) {
								timer.cancel();
								JOptionPane.showMessageDialog(null, "Sudoku resuelto.");
								int resp = JOptionPane.showConfirmDialog(null, "Quieres jugar de nuevo?", "Sudoku", JOptionPane.YES_NO_OPTION);
								switch (resp) {
									case 0:{
										task.reset();
										limpiarTablero();
										inicializarPanelSudoku();
										iniciarJuego(40);
										revalidate();
										repaint();
										iniciarTimer();
										
										break;
									}
									case 1:{
											System.exit(0);
										break;
									}
								}
							}
						} else {
							JLabel label = celdaSeleccionada.getLabel();
							label.setOpaque(true);
							label.setBackground(Color.RED);
							label.repaint();
							reDimensionar(label,celdaSeleccionada.getEntidadGrafica().getGrafico());
						} 
					} else {
						JOptionPane.showMessageDialog(null, "Por favor, selecciona una celda.");
					}
					//if (labelMarcado != null)
						//labelMarcado.repaint();
					
					
				}

				@Override
				public void mouseEntered(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseExited(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mousePressed(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseReleased(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				
				
			});
		}
	}
	
	/**
	 * Este metodo elimina todos los componentes del panel sudoku. Se utiliza luego de que el jugador gane el juego
	 * y quiera jugar de nuevo.
	 */
	private void limpiarTablero() {
		panelSudoku.removeAll();
		panelSudoku.updateUI();
	}
	
	/**
	 * Devuelve un objeto de tipo Celda, que fue seleccionado del panel.
	 * @return objeto de tipo Celda
	 */
	private Celda celdaSelected() {
		return celdaAnterior;
	}

	/**
	 * Este metodo redimensiona los JLabels.
	 * @param label objeto de tipo JLabel
	 * @param grafico objeto de tipo ImageIcon
	 */
	private void reDimensionar(JLabel label, ImageIcon grafico) {
		Image image = grafico.getImage();
		if (image != null) {  
			Image newimg = image.getScaledInstance(label.getWidth(), label.getHeight(),  java.awt.Image.SCALE_SMOOTH);
			grafico.setImage(newimg);
			label.repaint();
		}
	}
}
