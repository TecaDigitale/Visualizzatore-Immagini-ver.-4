/**
 * 
 */
package mx.imageViewer.implement;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import mx.configuration.Configuration;
import mx.database.ConnectionPool;
import mx.database.MsSqlPool;
import mx.database.table.Column;
import mx.imageViewer.implement.showImage.ShowImage;
import mx.imageViewer.schema.gestionelibro.ReadBook;
import mx.imageViewer.schema.gestioneopera.Libro;
import mx.imageViewer.schema.gestioneopera.Opera;
import mx.imageViewer.schema.gestioneopera.Volume;
import mx.imageViewer.schema.gestionepagina.ImageViewer;
import mx.imageViewer.schema.gestionepagina.ImageViewer.ShowNavigatore;
import mx.imageViewer.schema.gestionepagina.ImageViewer.ShowStru;
import mx.imageViewer.schema.gestionepagina.ImageViewer.Xlimage;
import mx.imageViewer.schema.gestionepagina.ImageViewer.Xlimage.Pagina;
import mx.imageViewer.servlet.interfacie.IImageViewer;
import mx.imageViewer.servlet.interfacie.exception.ImageViewerException;
import mx.teca.archivi.arsbni.TblRelRis;
import mx.teca.archivi.arsbni.Tblris;
import mx.teca.archivi.arsbni.view.ViewListaImg;
import mx.teca.archivi.arsbni.view.ViewTblrisFigli;

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
	private Logger log = Logger.getLogger(ImageViewerTecaDigitale.class);

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
	public ImageViewer initPage(HttpServletRequest request, HttpServletResponse response, String serverName) throws ServletException, IOException
	{
		ImageViewer imageViewer = null;
		String idr = "";

		try
		{
			log.debug("initPage");
			imageViewer = new ImageViewer();
			imageViewer.setTitolo("Visualizzatore Immagini TecaDigitale ver. 4.1");
			isCollezione(request.getParameter("idr"), imageViewer, serverName);
			isStru(request.getParameter("idr"), imageViewer);

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

				pagina.setUrlPage((String)Configuration.get("bncf.xlimage.urlPage", "http://opac.bncf.firenze.sbn.it/php/xlimage/XLImageRV.php?&amp;xsize=500&amp;image=")+
						rs.getString("imgPathName").replace("./STORAGE01/d2/bu/", "./"));
				pagina.setLink((String) Configuration.get("bncf.xlimage.link","javascript:changeImg")+"('"+xlimage.getPagina().size()+"')");
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

	private void isStru(String risIdr, ImageViewer imageViewer){
		ConnectionPool cp = null;
		TblRelRis tblRelRis = null;
		ResultSet rs = null;
		ShowStru show =null;
		boolean costola = false;

		try {
			costola = isCostola(risIdr);
			cp = Configuration.getPool("teca");
			tblRelRis = new TblRelRis(cp);
			tblRelRis.setCampoValue("relRisidrArrivo", risIdr);
			tblRelRis.setCampoValue("tipoRelId", 3);
			rs = tblRelRis.startSelect();
			if (rs.next()){
				show = new ShowStru();
				show.setValue(true);
				show.setIdr(risIdr);
				show.setCostola(costola);
				imageViewer.setShowStru(show);
			}
		} catch (SQLException e) {
			log.error(e);
		} finally {
			try {
				if (rs != null){
					rs.close();
				}
				if (tblRelRis != null){
					tblRelRis.stopSelect();
				}
			} catch (SQLException e) {
				log.error(e);
			}
		}
	}

	private boolean isCostola(String risIdr){
		ViewListaImg view = null;
		ResultSet rs = null;
		boolean result = false;

		try
		{
			view = new ViewListaImg(Configuration.getPool("teca"));
			view.setCampoValue("relrisidr", risIdr);
			view.addWhere(" AND (LOWER(TBLRIS.RISNOTAPUB) like '%costola%' OR LOWER(TBLRIS.RISNOTAPUB) like '%dorso%')");
			rs = view.startSelect();
			result = rs.next();
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
		return result;
	}

	/**
	 * Metodo utilizzato per ricavare se l'opera in oggetto è corrispondente ad una collezione
	 * 
	 * @param ris
	 * @return
	 * @throws ImageViewerException 
	 */
	private void isCollezione(String risIdr, ImageViewer imageViewer, String serverName) throws ImageViewerException{
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
					if (((String) Configuration.get("imageViewer." + serverName + ".externalVolumi",
							     Configuration.get("imageViewer.ALL.externalVolumi", "false"))).equalsIgnoreCase("flase")){
						show = new ShowNavigatore();
						show.setValue(true);
						show.setIdr(risIdr);
						imageViewer.setShowNavigatore(show);
					} else {
						showOpere = true;
					}
				}else if (rs.getString("rislivello").equals("S")){
					if (((String) Configuration.get("imageViewer." + serverName + ".externalVolumi",
						     Configuration.get("imageViewer.ALL.externalVolumi", "false"))).equalsIgnoreCase("flase")){
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
			usage = (String)Configuration.get("imageViewer."+request.getServerName()+".usageImageDefault", 
					Configuration.get("imageViewer.ALL.usageImageDefault", "3"));

		myReadBook = readBook.esegui(request.getParameter("idr"), usage);
		return myReadBook;
	}

	@Override
	public String getFoglioXsl(String serverName) {
		String foglioXsl = null;
		
		foglioXsl = super.getFoglioXsl(serverName);
		if (xlimage){
			foglioXsl = (String) Configuration.get("bncf.xlimage.xsl", xlimage);
		}
		return foglioXsl;
	}

	@Override
	public Opera readCatalogo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		return readCatalogo(request, response, true);
	}

	
	public Opera readCatalogo(HttpServletRequest request,
			HttpServletResponse response, boolean initBook) throws ServletException, IOException {
		ConnectionPool cp = null;
		MsSqlPool msp = null;
		ResultSet rs = null;
		Opera opera = null;
		Libro libro = null;
		Libro gruppo = null;
		int iGruppo = 0;
		Libro mesi = null;
		int iMesi = 0;
		Volume dettaglio = null;
		String titolo = "";
		boolean splitMese =false;
		String piecedt = null;
		String mese = null;
		
		try {
			splitMese =((String) Configuration.get(
					"imageViewer." + request.getServerName()
							+ ".splitMese",
					Configuration.get("imageViewer.ALL"
							+ ".splitMese", "false"))).equalsIgnoreCase("true");
			cp = Configuration.getPool("teca");
			msp = cp.getConn();
			rs = msp.StartSelect("SELECT TBLLEGNOT.tmpautore, " +
					                    "TBLLEGNOT.tmptitolo, " +
					                    "TBLLEGNOTRIS.risidr, " +
					                    "TBLLEGNOTRIS.piecegr, " +
					                    "TBLLEGNOTRIS.piecedt, " +
					                    "TBLLEGNOTRIS.piecein " +
                                   "FROM TBLRELRIS, " +
                                        "TBLLEGNOTRIS, " +
                                        "TBLLEGNOT " +
                                  "WHERE TBLRELRIS.relrisidrarrivo = '"+request.getParameter("idr")+"' AND " +
                                  		"TBLRELRIS.relrisidrpartenza = TBLLEGNOTRIS.risidr AND " +
                                  		"TBLLEGNOTRIS.id_tbllegnot = TBLLEGNOT.id_tbllegnot " +
                               "ORDER BY TBLLEGNOTRIS.piecein;");
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
					mesi = null;
					iMesi = 0;
				}
				
				if (gruppo == null){
					gruppo = new Libro();
					gruppo.setTitolo(rs.getString("piecegr"));
					iGruppo++;
					gruppo.setGenere("item-"+iGruppo);
					mesi = null;
					iMesi = 0;
				} else if (!gruppo.getTitolo().equals(rs.getString("piecegr"))) {
					if (mesi!= null){
						gruppo.getLibro().add(mesi);
					}
					libro.getLibro().add(gruppo);
					gruppo = new Libro();
					gruppo.setTitolo(rs.getString("piecegr"));
					iGruppo++;
					gruppo.setGenere("item-"+iGruppo);
					mesi = null;
					iMesi = 0;
				}
				
				if (splitMese){
					piecedt = rs.getString("piecedt");
					if (piecedt.indexOf(" ")>-1){
						mese = piecedt.substring(0, piecedt.indexOf(" "));
						piecedt = piecedt.substring(piecedt.indexOf(" ")).trim();
						if (mesi == null){
							mesi = new Libro();
							mesi.setTitolo(mese);
							iMesi++;
							mesi.setGenere("item-"+iGruppo+"-"+iMesi);
						} else if (!mesi.getTitolo().equals(mese)) {
							gruppo.getLibro().add(mesi);
							mesi = new Libro();
							mesi.setTitolo(mese);
							iMesi++;
							mesi.setGenere("item-"+iGruppo+"-"+iMesi);
						}
						dettaglio = new Volume();
						dettaglio.setValue(piecedt);
						if (initBook){
							dettaglio.setHref("javascript:parent.initBook('"+rs.getString("risidr")+"');");
						}else{
							dettaglio.setHref(rs.getString("risidr"));
						}
						mesi.getVolume().add(dettaglio);
					} else {
						dettaglio = new Volume();
						dettaglio.setValue(rs.getString("piecedt"));
						if (initBook){
							dettaglio.setHref("javascript:parent.initBook('"+rs.getString("risidr")+"');");
						}else{
							dettaglio.setHref(rs.getString("risidr"));
						}
						gruppo.getVolume().add(dettaglio);
					}
				} else {
					dettaglio = new Volume();
					dettaglio.setValue(rs.getString("piecedt"));
					if (initBook){
						dettaglio.setHref("javascript:parent.initBook('"+rs.getString("risidr")+"');");
					}else{
						dettaglio.setHref(rs.getString("risidr"));
					}
					gruppo.getVolume().add(dettaglio);
				}
			}
			if (libro != null){
				if (gruppo != null) {
					if (mesi!= null){
						gruppo.getLibro().add(mesi);
					}
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

	@Override
	public ImageViewer showCatalogo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ImageViewer imageViewer = null;
		Opera opera = null;
		String idr = "";

		try
		{
			showOpere=true;
			log.debug("initPage");
			imageViewer = new ImageViewer();
			imageViewer.setTitolo("Visualizzatore Immagini TecaDigitale ver. 4.1.0");

			if (request.getParameter("idr")!= null){
				idr = getBookIdr(request.getParameter("idr"));
				imageViewer.setIdr(idr);
			}
			opera = readCatalogo(request, response, false );
			for (Libro libro : opera.getLibro()){
				imageViewer.getLibro().add(libro);
			}
		} catch (ImageViewerException e) {
			imageViewer.setMsgError(e.getMessage());
		}
		return imageViewer;
	}

	/**
	 * 
	 */
	@Override
	public Opera readStru(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Opera opera = null;
		Libro  libro = null;
		Vector<String[]> stru = null;
		boolean costola = false;

		stru = readStru(request.getParameter("idr"), "3");
		if (stru != null){
			opera = new Opera();
			costola = isCostola(request.getParameter("idr"));
			for (int x=0; x<stru.size(); x++){
				libro = new Libro();
				libro.setTitolo(stru.get(x)[1]);
				checkStru(libro, stru.get(x)[0], request.getParameter("idr"), costola);
				opera.getLibro().add(libro);
			}
		}
		return opera;
	}

	private void checkStru(Libro libro, String idr, String idrPadre, boolean costola) throws ServletException{
		Vector<String[]> stru = null;
		Libro  subLibro = null;
		Volume volume = null;
		int idPagina = 0;

		try {
			stru = readStru(idr, "3");
			if (stru != null){
				for (int x=0; x<stru.size(); x++){
					subLibro = new Libro();
					subLibro.setTitolo(stru.get(x)[1]);
					checkStru(subLibro, stru.get(x)[0], idrPadre, costola);
					libro.getLibro().add(subLibro);
				}
			}else{
				stru = readStru(idr, "1");
				if (stru != null){
					for (int x=0; x<stru.size(); x++){
						volume = new Volume();
						volume.setValue(stru.get(x)[1]);
						idPagina = getIdPagina(idrPadre, stru.get(x)[0]);
						if (costola){
							idPagina--;
						}
						volume.setHref("javascript:parent.changePage("+idPagina+");");
						libro.getVolume().add(volume);
					}
				}
			}
		} catch (ServletException e) {
			throw e;
		}
		
	}

	private int getIdPagina(String idrPadre, String idr) throws ServletException{
		TblRelRis table = null;
		ResultSet rs = null;
		int result = 0;

		try {
			table = new TblRelRis(Configuration.getPool("teca"));
			table.setCampoValue("relRisidrPartenza", idr);
			table.setCampoValue("relRisidrArrivo", idrPadre);
			rs = table.startSelect();
			if (rs.next()){
				result = rs.getInt("relRisSequenza");
			}
		} catch (SQLException e) {
			log.error(e);
			throw new ServletException(e.getMessage(), e);
		} finally {
			try {
				if (rs != null){
					rs.close();
				}
				if (table != null){
					table.stopSelect();
				}
			} catch (SQLException e) {
				log.error(e);
				throw new ServletException(e.getMessage(), e);
			}
		}
		return result;
	}

	private Vector<String[]> readStru(String idr, String tipoRelId) throws ServletException{
		ViewTblrisFigli viewTblRisFigli = null;
		ResultSet rs = null;
		Vector<String[]> result =null;
		String[] dati = null;

		try{
			viewTblRisFigli = new ViewTblrisFigli(Configuration.getPool("teca"));
			viewTblRisFigli.setCampoValue("idrp", idr);
			viewTblRisFigli.setCampoValue("tipoRelId", tipoRelId);
			rs = viewTblRisFigli.startSelect();
			while (rs.next()){
				if (result == null){
					result= new Vector<String[]>();
				}
				dati = new String[2];
				dati[0]=rs.getString("idr");
				dati[1]=rs.getString("nota");
				result.add(dati);
			}
		} catch (SQLException e) {
			log.error(e);
			throw new ServletException(e.getMessage(), e);
		} finally {
			try {
				if (rs != null){
					rs.close();
				}
				if (viewTblRisFigli != null){
					viewTblRisFigli.stopSelect();
				}
			} catch (SQLException e) {
				log.error(e);
				throw new ServletException(e.getMessage(), e);
			}
		}
		return result;
	}
}
