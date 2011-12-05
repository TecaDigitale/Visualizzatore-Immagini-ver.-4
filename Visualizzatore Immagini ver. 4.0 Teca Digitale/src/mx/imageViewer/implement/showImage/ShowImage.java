/**
 * 
 */
package mx.imageViewer.implement.showImage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.configuration.Configuration;
import mx.imageViewer.imager.ImageManager;
import mx.imageViewer.imager.exception.ImageException;
import mx.log4j.Logger;
import mx.teca.archivi.arsbni.view.ViewImages;

/**
 * @author massi
 *
 */
public class ShowImage
{

	/**
	 * Variabile utilizzata per loggare l'appplicazione
	 */
	private static Logger log = new Logger(ShowImage.class, "mx.imageViewer.implement");

	/**
	 * 
	 */
	public ShowImage()
	{
	}

	@SuppressWarnings("unchecked")
	public static void showImage(String risIdr, HttpServletRequest request, HttpServletResponse response) throws ServletException
	{
		ImageManager imageManager = null;
		ViewImages view = null;
		ResultSet rs = null;
		String key = "";
		boolean absolutePath = false;
		
		try
		{
			log.debug("risIdr: "+risIdr);
			view = new ViewImages(Configuration.getPool("teca"));
			view.setCampoValue("risIdr", risIdr);
			rs = view.startSelect();
			if (rs.next())
			{
				log.debug("-------------------");
				for (Enumeration<String> e = view.getCampoKeys(); e.hasMoreElements();)
				{
					key = e.nextElement();
					log.debug(key+": "+rs.getString(key));
				}
				absolutePath = ((String)
						Configuration.listaParametri.get("imageViewer."+request.getServerName()+".ftp.absolutePath", 
						              Configuration.listaParametri.get("imageViewer.ALL.ftp.absolutePath", "true"))).
						              equalsIgnoreCase("true");
/*
				connectionActive = ((String)
						Configuration.listaParametri.get("imageViewer."+request.getServerName()+".ftp.connectionActive", 
						              Configuration.listaParametri.get("imageViewer.ALL.ftp.connectionActive", "true"))).
						              equalsIgnoreCase("true");
				timeOut = Integer.parseInt(((String)
						Configuration.listaParametri.get("imageViewer."+request.getServerName()+".ftp.timeout", 
						              Configuration.listaParametri.get("imageViewer.ALL.ftp.timeout", "5000")))
						              );
				ftpLog = ((String)
						Configuration.listaParametri.get("imageViewer."+request.getServerName()+".ftp.timeout", 
						              Configuration.listaParametri.get("imageViewer.ALL.ftp.timeout", "ftpLog")));
*/
				imageManager = new ImageManager();
				imageManager.initialize(
						ImageManager.createURL(rs.getString("hostProt"), rs.getString("hostServerPath"), 
								rs.getString("hostPathDisco"), rs.getString("imgPathName"), rs.getString("hostIp"), 
								rs.getInt("hostPorta"), rs.getString("hostLogin"), rs.getString("hostPsw"), 
								rs.getString("mimeDes"), absolutePath), false);
//				imageManager.resize(1024, 0);
				response.setContentType("image/jpeg");
				imageManager.sendImage(response.getOutputStream());
				/*
				download = new Download(absolutePath, connectionActive, timeOut, ftpLog, response);
				download.download(rs.getString("hostProt"), rs.getString("hostServerPath"), 
						rs.getString("hostPathDisco"), rs.getString("imgPathName"), rs.getString("hostIp"), 
						rs.getInt("hostPorta"), rs.getString("hostLogin"), rs.getString("hostPsw"), 
						rs.getString("mimeDes"));
						*/
			}
			else
			{
				log.error("Non risulta presente l'immagine ["+risIdr+"]");
				throw new ServletException("Non risulta presente l'immagine ["+risIdr+"]");
			}
		}
		catch (SQLException e)
		{
			log.error(e);
		}
		catch (ServletException e)
		{
			throw e;
		}
		/*
		catch (DownloadException e)
		{
			try
			{
				ImgError.imgErr(e.getTitleColor(), e.getTitleMessage(), e.getTestoColor(), e.getTestoMessage(), response.getOutputStream());
			}
			catch (IOException e1)
			{
				log.error(e1);
			}
		}
		*/
		catch (ImageException e)
		{
			log.error(e);
		}
		catch (IOException e)
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
	}
}
