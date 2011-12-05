package mx.imageviewer.servlet.interfacie;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.imageviewer.schema.gestionelibro.ReadBook;
import mx.imageviewer.schema.gestionepagina.ImageViewer;

/**
 * Questa interfaccia viene utilizzata per gestire l'interfaccia 
 * con l'archivio che gestisce le immagini da visualizzare
 * 
 * @author Massimiliano Randazzo
 *
 */
public interface IImageViewer
{

	/**
	 * Metodo utilizzato per ricavare le informazioni necessarie per 
	 * disegnare la pagina html che contiene il visualizzatore, l'oggetto XML
	 * che viene generato verrˆ convertito in HTML tramite un foglio Xls
	 * 
	 * @return
	 */
	public abstract ImageViewer initPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

	/**
	 * Metodo utilizzato per inviare l'immagine verso il client
	 * 
	 * @param request Variabile utilizzata per gestire le informazioni provenienti dal client
	 * @param response Variabile utilizzata per gestire le informazoni verso il client
	 */
	public abstract void showImage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

	/**
	 * Metodo utilizzato per reperire le informazioni relative al 
	 * libro da visualizare
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public abstract ReadBook readBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
