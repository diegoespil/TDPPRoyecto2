package Logica;

import javax.swing.ImageIcon;

/**
 * Esta clase representa una imagen en una celda. Contiene un array de imagenes las cuales son cargadas por el juego.
 * @author diego
 *
 */
public class EntidadGrafica {

	private ImageIcon image;
	private String [] images;
	
	/**
	 * Constructor que inicializa los atributos
	 */
	public EntidadGrafica() {
		String path = "/resources/img/";
		this.image = new ImageIcon();
		this.images = new String[] {path+"vacio.png",path+"1.png",path+"2.png",path+"3.png",path+"4.png",path+"5.png",path+"6.png",path+"7.png",path+"8.png",path+"9.png", path+"0.png", path+"two-dots.png"};
	}
	
	/**
	 * Actualiza la imagen segun el valor recibido
	 * @param indice es un entero de 0 a 11
	 */
	public void actualizar(int indice) {
		ImageIcon imageIcon = new ImageIcon(this.getClass().getResource(this.images[indice]));
		this.image.setImage(imageIcon.getImage());
	}
	
	/**
	 * Retorna un objecto de tipo ImageIcon
	 * @return un objecto de tipo ImageIcon
	 */
	public ImageIcon getGrafico() {
		return this.image;
	}
	
	
}

