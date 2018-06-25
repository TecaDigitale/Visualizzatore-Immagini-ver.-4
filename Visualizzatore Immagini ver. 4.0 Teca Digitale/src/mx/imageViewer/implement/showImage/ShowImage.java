/**
 * 
 */
package mx.imageViewer.implement.showImage;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import mx.configuration.Configuration;
import mx.imageViewer.imager.ImageManager;
import mx.imageViewer.imager.exception.ImageException;
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
	private static Logger log = Logger.getLogger(ShowImage.class);

	/**
	 * 
	 */
	public ShowImage()
	{
	}

	public static void showImage(String risIdr, HttpServletRequest request, HttpServletResponse response) throws ServletException
	{
		ImageManager imageManager = null;
		ViewImages view = null;
		ResultSet rs = null;
		String usage = "";
		boolean absolutePath = false;
		boolean forceUsage = false;
		DatiImmagini datiImmagini = null;
		
		try
		{
			log.debug("risIdr: "+risIdr);
			view = new ViewImages(Configuration.getPool("teca"));

			if (request.getParameter("usage") != null){
				forceUsage = true;
				view.setCampoValue("imgUsage", request.getParameter("usage"));
			}
			else
				usage = (String)Configuration.get("imageViewer."+request.getServerName()+".usageImageDefault", 
						Configuration.get("imageViewer.ALL.usageImageDefault", "3"));
			view.setCampoValue("risIdr", risIdr);
//			System.out.println("risIdr: "+risIdr);
			rs = view.startSelect();
			while(rs.next()){
				if (checkIp(rs.getString("imgUsage"), request) ||
					rs.getString("imgUsage").equals(usage) ||
					forceUsage){
					datiImmagini = new DatiImmagini(rs);
					break;
				}
			}

			if (datiImmagini != null)
			{
				absolutePath = ((String)
						Configuration.get("imageViewer."+request.getServerName()+".ftp.absolutePath", 
						              Configuration.get("imageViewer.ALL.ftp.absolutePath", "true"))).
						              equalsIgnoreCase("true");
				imageManager = new ImageManager();
				imageManager.initialize(
						ImageManager.createURL(datiImmagini.getHostProt(), datiImmagini.getHostServerPath(), 
								datiImmagini.getHostPathDisco(), datiImmagini.getImgPathName(), datiImmagini.getHostIp(), 
								datiImmagini.getHostPorta(), datiImmagini.getHostLogin(), datiImmagini.getHostPsw(), 
								datiImmagini.getMimeDes(), absolutePath), false);
				response.setContentType("image/jpeg");
				imageManager.sendImage(response.getOutputStream());
			}
			else
			{
				log.info("Non risulta presente l'immagine ["+risIdr+"]");
				errorImage(request,response);
//				throw new ServletException("Non risulta presente l'immagine ["+risIdr+"]");
			}
		}
		catch (SQLException e)
		{
			log.error(e);
			errorImage(request,response);
			throw new ServletException(e);
		}
		catch (ServletException e)
		{
			errorImage(request,response);
			throw e;
		}
		catch (ImageException e)
		{
			log.error(e);
			errorImage(request,response);
//			throw new ServletException(e);
		}
		catch (IOException e)
		{
			log.error(e);
			errorImage(request,response);
			throw new ServletException(e);
		}
		catch (RuntimeException e)
		{
			errorImage(request,response);
//			throw new ServletException(e);
		}catch (Exception e)
		{
			log.error(e.getMessage(), e);
			errorImage(request,response);
//			throw new ServletException(e);
		}		finally
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

	private static void errorImage(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		String errorImg = "";
		File f = null;
		ImageManager imageManager = null;
		
		try {
			errorImg = ((String)
					Configuration.get("imageViewer."+request.getServerName()+".imageNotFound", 
				              Configuration.get("imageViewer.ALL.imageNotFound", "")));
			if (errorImg != null &&
					!errorImg.trim().equals("")){
				f = new File(errorImg);
				if (f.exists()){
					imageManager = new ImageManager();
					imageManager.initialize(
							ImageManager.createURL("file://"+f.getAbsolutePath()), false);
					response.setContentType("image/jpeg");
					imageManager.sendImage(response.getOutputStream());
				}
			}
		} catch (ImageException e) {
			log.error(e);
			throw new ServletException(e);
		} catch (IOException e) {
			log.error(e);
			throw new ServletException(e);
		}
	}
	private static  boolean checkIp(String usage, HttpServletRequest request){
		String ipCheck = null;
		String[] st = null;
		String[] st2 = null;
		String[] remoteAddr = null;
		boolean result = false;

		ipCheck = (String)Configuration.get("imageViewer."+request.getServerName()+".usage."+usage, 
				Configuration.get("imageViewer.ALL.usage."+usage, ""));

		if (ipCheck != null && !ipCheck.equals("")){
			remoteAddr = request.getRemoteAddr().split("\\.");
			st = ipCheck.split(",");
			for (int x=0; x<st.length; x++){
				st2 = st[x].split("\\.");
				if ((st2[0].equals("*") ||st2[0].equals(remoteAddr[0])) && 
					(st2[1].equals("*") ||st2[1].equals(remoteAddr[1])) && 
					(st2[2].equals("*") ||st2[2].equals(remoteAddr[2])) && 
					(st2[3].equals("*") ||st2[3].equals(remoteAddr[3]))){
					result = true;
				}
			}
		}
		return result;
	}
}

/**
 * Classe utilizzata per la gestione delle informazioni relative hai dati dell'immagine
 * 
 * @author massi
 *
 */
class DatiImmagini {

	/**
	 * Protocollo per il collegamento con lo storage FTP/NTFS
	 */
	String hostProt=null;

	/**
	 * Path relativa allo storage
	 */
	String hostServerPath=null;

	/**
	 * Definizione del disco relativo allo storage
	 */
	String hostPathDisco=null;

	/**
	 * Path della singola immagine
	 */
	String imgPathName=null;

	/**
	 * Indirizzo IP dello storage
	 */
	String hostIp=null;

	/**
	 * Porta dello storage relativo al protocollo FTP
	 */
	int hostPorta=-1;

	/**
	 * Utente per il collegamento allo storage
	 */
	String hostLogin= null;

	/**
	 * Password per il collegamento allo storage
	 */
	String hostPsw=null;

	/**
	 * Formato dell'oggetto digitale
	 */
	String mimeDes=null;

	/**
	 * 
	 * @param rs Record relativo al  risultato della ricerca
	 * @throws SQLException 
	 */
	public DatiImmagini(ResultSet rs) throws SQLException{
		this.hostProt=rs.getString("hostProt");
		this.hostServerPath=rs.getString("hostServerPath");
		this.hostPathDisco=rs.getString("hostPathDisco");
		this.imgPathName=rs.getString("imgPathName");
		this.hostIp=rs.getString("hostIp");
		this.hostPorta=rs.getInt("hostPorta");
		this.hostLogin= rs.getString("hostLogin");
		this.hostPsw=rs.getString("hostPsw");
		this.mimeDes=rs.getString("mimeDes");
	}

	public String getHostProt() {
		return hostProt;
	}

	public String getHostServerPath() {
		return hostServerPath;
	}

	public String getHostPathDisco() {
		return hostPathDisco;
	}

	public String getImgPathName() {
		return imgPathName;
	}

	public String getHostIp() {
		return hostIp;
	}

	public int getHostPorta() {
		return hostPorta;
	}

	public String getHostLogin() {
		return hostLogin;
	}

	public String getHostPsw() {
		return hostPsw;
	}

	public String getMimeDes() {
		return mimeDes;
	}
}
