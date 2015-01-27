/**
 * 
 */
package mx.imageviewer.servlet.interfacie.exception;

/**
 * Classe utilizzata per la gestione delle eccezioni relative alla visualizzazione 
 * delle immagini
 * @author massi
 *
 */
public class ImageViewerException extends Exception {

	/**
	 * 
	 */
	public ImageViewerException() {
	}

	/**
	 * @param arg0
	 */
	public ImageViewerException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public ImageViewerException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public ImageViewerException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
