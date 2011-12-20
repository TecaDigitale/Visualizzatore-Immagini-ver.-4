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
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.RestoreAction;

import mx.configuration.Configuration;
import mx.database.ConnectionPool;
import mx.database.MsSqlPool;
import mx.database.table.Column;
import mx.imageViewer.implement.showImage.ShowImage;
import mx.imageviewer.schema.gestionelibro.ReadBook;
import mx.imageviewer.schema.gestioneopera.Libro;
import mx.imageviewer.schema.gestioneopera.Opera;
import mx.imageviewer.schema.gestioneopera.Volume;
import mx.imageviewer.schema.gestionepagina.ImageViewer;
import mx.imageviewer.schema.gestionepagina.ImageViewer.ShowNavigatore;
import mx.imageviewer.schema.gestionepagina.ImageViewer.Xlimage;
import mx.imageviewer.schema.gestionepagina.ImageViewer.Xlimage.Pagina;
import mx.imageviewer.servlet.interfacie.IImageViewer;
import mx.imageviewer.servlet.interfacie.exception.ImageViewerException;
import mx.log4j.Logger;
import mx.teca.archivi.arsbni.TblRelRis;
import mx.teca.archivi.arsbni.Tblris;
import mx.teca.archivi.arsbni.view.ViewListaImg;

/**
 * Questa classe viene utilizzata per gestire l'interfaccia verso di 
 * Database Teca per la visualizzazione delle immagini
 * 
 * @author Massimiliano Randazzo
 *
 */
public class ImageViewerTecaDigitale extends IImageViewer
{

	/**
	 * Variabile utilizzata per loggare l'appplicazione
	 */
	private Logger log = new Logger(ImageViewerTecaDigitale.class, "mx.imageViewer.implement");

	/**
	 * Indica se l'immagine contiene Carte geografiche
	 */
	private boolean xlimage=false;

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
		String idr = "";

		try
		{
			log.debug("initPage");
			imageViewer = new ImageViewer();
			imageViewer.setTitolo("Visualizzatore Immagini TecaDigitale ver. 4.0");
			isCollezione(request.getParameter("idr"), imageViewer);

			if (request.getParameter("idr")!= null){
				idr = getBookIdr(request.getParameter("idr"));
				imageViewer.setIdr(idr);
				imageViewer.setXlimage(checkXlimage(idr));
			}
		} catch (ImageViewerException e) {
			imageViewer.setMsgError(e.getMessage());
		}
		return imageViewer;
	}
 
	private Xlimage checkXlimage(String idr) throws ServletException{
		Xlimage xlimage = null;
		Pagina pagina = null;
		Tblris table = null;
		ResultSet rs2 = null;
		ViewListaImg view = null;
		ResultSet rs = null;

		try {
			table = new Tblris(Configuration.getPool("teca"));
			table.setCampoValue("risidr", idr);
			rs2 = table.startSelect();
			view = new ViewListaImg(Configuration.getPool("teca"));
			view.setCampoValue("relrisidr", idr);
			view.setCampoValue("ImgUsage", "5");
			view.getCampo("seq").setOrderBy(Column.ORDERBY_CRES, 1);
			rs = view.startSelect();
			while(rs.next()){
				this.xlimage=true;
				if (xlimage == null){
					xlimage = new Xlimage();
					if (rs2.next()){
						xlimage.setTitolo(rs2.getString("risNotaPub"));
					}
				}
				pagina = new Pagina();

				pagina.setUrlPage((String)Configuration.listaParametri.get("bncf.xlimage.urlPage", "http://opac.bncf.firenze.sbn.it/php/xlimage/XLImageRV.php?&amp;xsize=500&amp;image=")+
						rs.getString("imgPathName"));
				pagina.setLink((String) Configuration.listaParametri.get("bncf.xlimage.link","javascript:changeImg")+"('"+xlimage.getPagina().size()+"')");
				pagina.setKey("IMG."+xlimage.getPagina().size());
				pagina.setKeyPadre("IMG");
				pagina.setValue(rs.getString("nota"));
				xlimage.getPagina().add(pagina);
			}
		} catch (SQLException e) {
			log.error(e);
			throw new ServletException(e);
		} finally {
			try {
				if (rs != null){
					rs.close();
				}
				if (rs2 != null){
					rs2.close();
				}
				if (view != null){
					view.stopSelect();
				}
				if (table != null){
					table.stopSelect();
				}
			} catch (SQLException e) {
				log.error(e);
				throw new ServletException(e);
			}
		}
		return xlimage;
	}

	/**
	 * Metodo utilizzato per ricavare l'identificativo del Libro
	 * @param risIdr
	 * @return
	 * @throws ImageViewerException 
	 */
	private String getBookIdr(String risIdr) throws ImageViewerException{
		ConnectionPool cp = null;
		MsSqlPool msp = null;
		ResultSet rs = null;
		String result = null;
		String newRisIdr = null;
		boolean trovato = false;
		boolean lavorato = false;

		try {
			cp = Configuration.getPool("teca");
			msp = cp.getConn();
			rs = msp.StartSelect("Select TBLRELRIS.tiporelid, " +
					                    "TBLRIS.* " +
					               "from TBLRELRIS, " +
					                    "TBLRIS " +
					              "where TBLRELRIS.relrisidrarrivo='"+risIdr+"' and " +
					              		"TBLRIS.risidr=TBLRELRIS.relrisidrpartenza " +
					           "order by TBLRELRIS.relrissequenza desc");
			while (rs.next()){
				lavorato=true;
				if (rs.getString("rislivello").equals("P")){
					trovato = true;
					result = risIdr;
					break;
				}else if (newRisIdr == null){
					newRisIdr = rs.getString("risIdr");
				}
			}
			
			if (!lavorato){
				throw new ImageViewerException("Non risultano presenti collegamenti con le immagini");
			}
		} catch (SQLException e) {
			log.error(e);
			throw new ImageViewerException(e.getMessage(), e);
		} catch (ImageViewerException e) {
			throw e;
		} catch (Exception e) {
			log.error(e);
			throw new ImageViewerException(e.getMessage(), e);
		}finally{
			try {
				if (rs != null){
					rs.close();
				}
				if (msp != null){
					msp.StopSelect();
					cp.releaseConn(msp);
				}
			} catch (SQLException e) {
				log.error(e);
				throw new ImageViewerException(e.getMessage(), e);
			}finally{
				if (!trovato){
					result = getBookIdr(newRisIdr);
				}
			}
		}
		return result;
	}

	/**
	 * Metodo utilizzato per ricavare se l'opera in oggetto è corrispondente ad una collezione
	 * 
	 * @param ris
	 * @return
	 * @throws ImageViewerException 
	 */
	private void isCollezione(String risIdr, ImageViewer imageViewer) throws ImageViewerException{
		ConnectionPool cp = null;
		Tblris tblRis = null;
		TblRelRis tblRelRis = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		ShowNavigatore show = null;
		
		try {
			cp = Configuration.getPool("teca");
			tblRis = new Tblris(cp);
			tblRis.setCampoValue("risidr", risIdr);
			rs = tblRis.startSelect();
			if (rs.next()){
				if (rs.getString("rislivello").equals("C")){
					show = new ShowNavigatore();
					show.setValue(true);
					show.setIdr(risIdr);
					imageViewer.setShowNavigatore(show);
				}else if (rs.getString("rislivello").equals("S")){
					tblRelRis = new TblRelRis(cp);
					tblRelRis.setCampoValue("relRisidrPartenza", risIdr);
					tblRelRis.setCampoValue("tipoRelId", 2);
					rs2 = tblRelRis.startSelect();
					if (rs2.next()){
						show = new ShowNavigatore();
						show.setValue(true);
						show.setIdr(rs2.getString("relRisidrArrivo"));
						imageViewer.setShowNavigatore(show);
					}
				}
			}else{
				throw new ImageViewerException("Non risulta presente l'oggetto in base dati");
			}
		} catch (SQLException e) {
			log.error(e);
		} catch (ImageViewerException e) {
			throw e;
		} finally {
			try {
				if (rs != null){
					rs.close();
				}
				if (rs2 != null){
					rs2.close();
				}
				if (tblRelRis != null){
					tblRelRis.stopSelect();
				}
				if (tblRis != null){
					tblRis.stopSelect();
				}
			} catch (SQLException e) {
				log.error(e);
			}
		}
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

//	public void showImage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
//	{
//		String key = "";
//		String[] values = null;
//		String fileImg = "";
//		File f = null;
//		FileImageInputStream fiis = null;
//		int len = 0;
//		byte[] bbuf = new byte[1000000]; 
//
//		try
//		{
//			log.debug("showImage");
//			for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();)
//			{
//				key = e.nextElement();
//				values = request.getParameterValues(key);
//				for (int x=0; x<values.length; x++)
//					log.debug(key+": "+values[x]);
//			}
//			response.setCharacterEncoding("UTF-8");
//			response.setContentType("image/jpeg");
//			
//			fileImg= "/Volumes/HDDSORGENTI/Backup/discoc/Progetti/Tivoli/Tivoli DVD/Immagini/P000001_SIGI_1691_D1_VIII_12/200";
//			fileImg += File.separator;
//			fileImg += "TIV_"+ConvertText.mettiZeri(request.getParameter("sequence"), 4)+".jpg";
//
//			log.debug("fileImg: "+fileImg);
//			f = new File(fileImg);
//			fiis = new FileImageInputStream(f);
//			while ((len = fiis.read(bbuf))>-1)
//			{
//				response.getOutputStream().write(bbuf,0,len);
//			}
//		}
//		catch (IOException e)
//		{
//			log.error(e);
//			throw e;
//		}
//		finally
//		{
//			if (fiis != null)
//				fiis.close();
//		}
//	}

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

	@Override
	public String getFoglioXsl(String serverName) {
		String foglioXsl = null;
		
		foglioXsl = super.getFoglioXsl(serverName);
		if (xlimage){
			foglioXsl = (String) Configuration.listaParametri.get("bncf.xlimage.xsl", xlimage);
		}
		return foglioXsl;
	}

	@Override
	public Opera readCatalogo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ConnectionPool cp = null;
		MsSqlPool msp = null;
		ResultSet rs = null;
		Opera opera = null;
		Libro libro = null;
		Libro gruppo = null;
		Volume dettaglio = null;
		String titolo = "";

		try {
			cp = Configuration.getPool("teca");
			msp = cp.getConn();
			rs = msp.StartSelect("select tbllegnot.tmpautore, "+
										"tbllegnot.tmptitolo, "+
										"tbllegnotris.risidr, "+
										"tbllegnotris.piecegr, "+
										"tbllegnotris.piecedt, "+
										"tbllegnotris.piecein "+
								   "from tblrelris, "+
								   		"tbllegnotris, "+
								   		"tbllegnot "+
								  "where tblrelris.relrisidrarrivo='"+request.getParameter("idr")+"' AND "+
								  		"tblrelris.relrisidrpartenza=tbllegnotris.risidr AND "+
								  		"tbllegnotris.id_tbllegnot=tbllegnot.id_tbllegnot "+
							   "order by tbllegnotris.piecein;");
			while (rs.next()){
				if (opera==null){
					opera = new Opera();
				}
				if (rs.getString("tmptitolo")== null){
					titolo = "Titolo";
				}else{
					titolo = rs.getString("tmptitolo");
				}
				if (libro == null){
					libro = new Libro();

					if (rs.getString("tmpautore") != null){
						libro.setAutore(rs.getString("tmpautore"));
					}
					libro.setTitolo(titolo);
				} else if ((libro.getTitolo() != null && !libro.getTitolo().equals(titolo)) ||
						   (libro.getAutore() != null && !libro.getAutore().equals(rs.getString("tmpautore")))){

					if (gruppo != null) {
						libro.getLibro().add(gruppo);
					}

					opera.getLibro().add(libro);
					libro = new Libro();

					if (rs.getString("tmpautore") != null){
						libro.setAutore(rs.getString("tmpautore"));
					}
					libro.setTitolo(titolo);
					gruppo = null;
				}
				
				if (gruppo == null){
					gruppo = new Libro();
					gruppo.setTitolo(rs.getString("piecegr"));
				} else if (!gruppo.getTitolo().equals(rs.getString("piecegr"))) {
					libro.getLibro().add(gruppo);
					gruppo = new Libro();
					gruppo.setTitolo(rs.getString("piecegr"));
				}
				
				dettaglio = new Volume();
				dettaglio.setValue(rs.getString("piecedt"));
				dettaglio.setHref("javascript:parent.initBook('"+rs.getString("risidr")+"');");
				gruppo.getVolume().add(dettaglio);
			}
			if (libro != null){
				if (gruppo != null) {
					libro.getLibro().add(gruppo);
				}
				opera.getLibro().add(libro);
			}
		} catch (SQLException e) {
			log.error(e);
			throw new ServletException(e.getMessage(), e);
		} catch (ServletException e) {
			throw e;
		} catch (Exception e) {
			log.error(e);
			throw new ServletException(e.getMessage(), e);
		}finally{
			try {
				if (rs != null){
					rs.close();
				}
				if (msp != null){
					msp.StopSelect();
					cp.releaseConn(msp);
				}
			} catch (SQLException e) {
				log.error(e);
				throw new ServletException(e.getMessage(), e);
			}finally{
//				if (opera == null){
//					result = getBookIdr(newRisIdr);
//				}
			}
		}
		return opera;
	}
}
