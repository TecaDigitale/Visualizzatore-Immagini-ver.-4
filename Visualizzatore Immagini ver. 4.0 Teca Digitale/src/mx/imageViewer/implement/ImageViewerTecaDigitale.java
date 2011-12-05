/**
 * 
 */
package mx.imageViewer.implement;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.configuration.Configuration;
import mx.imageViewer.implement.showImage.ShowImage;
import mx.imageviewer.schema.gestionelibro.ReadBook;
import mx.imageviewer.schema.gestionepagina.ImageViewer;
import mx.imageviewer.servlet.interfacie.IImageViewer;
import mx.log4j.Logger;
import mx.teca.archivi.arsbni.view.ViewTbllegnotTblris;

/**
 * Questa classe viene utilizzata per gestire l'interfaccia verso di 
 * Database Teca per la visualizzazione delle immagini
 * 
 * @author Massimiliano Randazzo
 *
 */
public class ImageViewerTecaDigitale implements IImageViewer
{

	/**
	 * Variabile utilizzata per loggare l'appplicazione
	 */
	private Logger log = new Logger(ImageViewerTecaDigitale.class, "mx.imageViewer.implement");

	/**
	 * Costruttore
	 */
	public ImageViewerTecaDigitale()
	{
		log.debug("Costruttore");
	}

	/**
	 * @see mx.imageviewer.servlet.interfacie.IImageViewer#initPage(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public ImageViewer initPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		ImageViewer imageViewer = null;
		ViewTbllegnotTblris view = null;
		ResultSet rs = null;

		try
		{
			log.debug("initPage");
			imageViewer = new ImageViewer();
			imageViewer.setTitolo("Visualizzatore Immagini TecaDigitale ver. 4.0");
			if (request.getParameter("idr")!= null)
				imageViewer.setIdr(request.getParameter("idr"));

			view = new ViewTbllegnotTblris(Configuration.getPool("teca"));
			view.setCampoValue("risidr", request.getParameter("idr"));
			rs = view.startSelect();
			if (rs.next())
				imageViewer.setShowNavigatore(view.getRecTot()>1);
		}
		catch (SQLException e)
		{
			log.error(e);
		}
		finally
		{
			try
			{
				if (rs != null)
					rs.close();
				if (view != null)
					view.stopSelect();
			}
			catch (SQLException e)
			{
				log.error(e);
			}
		}
		return imageViewer;
	}

	/**
	 * 
	 * @see mx.imageviewer.servlet.interfacie.IImageViewer#showImage(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void showImage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String key = "";
		String[] values = null;

		log.debug("showImage");
		for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();)
		{
			key = e.nextElement();
			values = request.getParameterValues(key);
			for (int x=0; x<values.length; x++)
				log.debug(key+": "+values[x]);
		}
		ShowImage.showImage(request.getParameter("idr"), request, response);
	}
/*
	public void showImage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String key = "";
		String[] values = null;
		String fileImg = "";
		File f = null;
		FileImageInputStream fiis = null;
		int len = 0;
		byte[] bbuf = new byte[1000000]; 

		try
		{
			log.debug("showImage");
			for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();)
			{
				key = e.nextElement();
				values = request.getParameterValues(key);
				for (int x=0; x<values.length; x++)
					log.debug(key+": "+values[x]);
			}
			response.setCharacterEncoding("UTF-8");
			response.setContentType("image/jpeg");
			
			fileImg= "/Volumes/HDDSORGENTI/Backup/discoc/Progetti/Tivoli/Tivoli DVD/Immagini/P000001_SIGI_1691_D1_VIII_12/200";
			fileImg += File.separator;
			fileImg += "TIV_"+ConvertText.mettiZeri(request.getParameter("sequence"), 4)+".jpg";

			log.debug("fileImg: "+fileImg);
			f = new File(fileImg);
			fiis = new FileImageInputStream(f);
			while ((len = fiis.read(bbuf))>-1)
			{
				response.getOutputStream().write(bbuf,0,len);
			}
		}
		catch (IOException e)
		{
			log.error(e);
			throw e;
		}
		finally
		{
			if (fiis != null)
				fiis.close();
		}
	}
*/
	/**
	 * 
	 * @see mx.imageviewer.servlet.interfacie.IImageViewer#readBook(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ReadBook readBook(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		mx.imageViewer.implement.readBook.ReadBook readBook = null;
		ReadBook myReadBook = null;
		String usage = "";

		readBook = new mx.imageViewer.implement.readBook.ReadBook(request);

		if (request.getParameter("usage") != null)
			usage = request.getParameter("usage");
		else
			usage = (String)Configuration.listaParametri.get("imageViewer."+request.getServerName()+".usageImageDefault", 
					Configuration.listaParametri.get("imageViewer.ALL.usageImageDefault", "3"));

		myReadBook = readBook.esegui(request.getParameter("idr"), usage);
		return myReadBook;
	}
}
