/**
 * 
 */
package mx.imageViewer.implement.readBook;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import mx.configuration.Configuration;
import mx.database.MsSqlException;
import mx.database.table.Column;
import mx.imageViewer.imager.ImageManager;
import mx.imageViewer.imager.exception.ImageException;
import mx.imageViewer.schema.gestionelibro.DatiBibliografici;
import mx.imageViewer.schema.gestionelibro.Immagini;
import mx.imageViewer.schema.gestionelibro.Immagini.Immagine;
import mx.teca.archivi.arsbni.TblImg;
import mx.teca.archivi.arsbni.view.ViewListaImg;
import mx.teca.archivi.arsbni.view.ViewTbllegnotTblris;

/**
 * @author massi
 *
 */
public class ReadBook {

	/**
	 * Variabile utilizzata per loggare l'appplicazione
	 */
	private Logger log = Logger.getLogger(ReadBook.class);

	/**
	 * Questa variabile viene utilizzata per gestire il colloqui da parte del client
	 */
	private HttpServletRequest request = null;

	/**
	 * Costruttore
	 */
	public ReadBook(HttpServletRequest request) {
		this.request = request;
	}

	public mx.imageViewer.schema.gestionelibro.ReadBook esegui(String risIdr, String usage) {
		ViewListaImg view = null;
		ResultSet rs = null;
		mx.imageViewer.schema.gestionelibro.ReadBook readBook = null;
		Immagini immagini = null;
		int imgLength = 0;
		int imgWidth = 0;
		ImageManager imageManager = null;

		try {
			view = new ViewListaImg(Configuration.getPool("teca"));
			view.setCampoValue("relrisidr", risIdr);
			view.setCampoValue("ImgUsage", usage);
			view.getCampo("seq").setOrderBy(Column.ORDERBY_CRES, 1);
			rs = view.startSelect();

			readBook = new mx.imageViewer.schema.gestionelibro.ReadBook();
			if (view.getRecTot() > 0) {
				while (rs.next()) {
					if (readBook.getDatiBibliografici() == null) {
						readBook.setDatiBibliografici(initDatiBibliografici(risIdr));
					}
					if (immagini == null) {
						immagini = new Immagini();
						immagini.setNumImg(view.getRecTot());
						immagini.setIsCostola(false);
					}
					if (rs.getString("nota").toLowerCase().contains("costola")
							|| rs.getString("nota").toLowerCase().contains("dorso"))
						immagini.setIsCostola(true);

					if (rs.getString("hostProt").equals("NFS")) {
						try {
							if ((rs.getObject("imgLengthConv") == null || rs.getInt("imgLengthConv") == 0)
									|| (rs.getObject("imgWidthConv") == null || rs.getInt("imgWidthConv") == 0)) {
								imageManager = new ImageManager();
								imageManager.initialize(ImageManager.createURL(rs.getString("hostProt"),
										rs.getString("hostServerPath"), rs.getString("hostPathDisco"),
										rs.getString("imgPathName"), rs.getString("hostIp"), rs.getInt("hostPorta"),
										rs.getString("hostLogin"), rs.getString("hostPsw"), null, true), false);
								imgLength = imageManager.getHeight();
								imgWidth = imageManager.getWidth();
								if (imgLength > 0 && imgWidth > 0) {
									updImgConv(imgLength, imgWidth, rs.getString("idTblImg"));
								} else {

									imgLength = 800;
									imgWidth = 600;
								}
							} else {
								imgLength = (rs.getInt("imgLengthConv") == 0 ? 800 : rs.getInt("imgLengthConv"));
								imgWidth = (rs.getInt("imgWidthConv") == 0 ? 600 : rs.getInt("imgWidthConv"));
							}
						} catch (ImageException e) {
							imgLength = (rs.getInt("imgLength") == 0 ? 800 : rs.getInt("imgLength"));
							imgWidth = (rs.getInt("imgWidth") == 0 ? 600 : rs.getInt("imgWidth"));
						}

					} else {
						imgLength = (rs.getInt("imgLength") == 0 ? 800 : rs.getInt("imgLength"));
						imgWidth = (rs.getInt("imgWidth") == 0 ? 600 : rs.getInt("imgWidth"));
					}
					// if (imgLength>imgWidth)
					immagini.getImmagine()
							.add(addImmagine(rs.getString("RelRisIdrPartenza"), rs.getInt("seq"),
									(rs.getString("nota").toLowerCase().contains("costola")
											|| rs.getString("nota").toLowerCase().contains("dorso")),
									rs.getString("nota"), imgLength, imgWidth));
					// else
					// immagini.getImmagine().add(
					// addImmagine(
					// rs.getString("RelRisIdrPartenza"),
					// rs.getInt("seq"),
					// rs.getString("nota").toLowerCase().contains("costola"),
					// rs.getString("nota"),
					// imgWidth,
					// imgLength));
				}
				if (immagini != null)
					readBook.setImmagini(immagini);
			} else {
				try {
					int iUsage = 0;
					iUsage = Integer.valueOf(usage).intValue();
					if (iUsage > 1) {
						iUsage--;
						usage = Integer.toString(iUsage);
						readBook = esegui(risIdr, usage);
					}
				} catch (NumberFormatException e) {
				}
			}
		} catch (MsSqlException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (view != null)
					view.stopSelect();
			} catch (SQLException e) {
				log.error(e);
			}
		}
		return readBook;
	}

	private void updImgConv(int imgLengthConv, int imgWidthConv, String idTblImg) throws MsSqlException {
		TblImg tblimg = null;

		tblimg = new TblImg(Configuration.getPool("teca"));
		tblimg.setCampoValue("imgLengthConv", imgLengthConv);
		tblimg.setCampoValue("imgWidthConv", imgWidthConv);
		tblimg.setCampoValue("idTblImg", idTblImg);
		tblimg.update();
	}

	/**
	 * Questo metodo viene utilizzato per la definizione del nodo Immagine
	 * 
	 * @param id
	 *            Identificativo dell'immagine
	 * @param sequenza
	 *            Numero di sequenza dell'immagine
	 * @param nomenclatura
	 *            Nomenclatura dell'imamgine
	 * @param altezza
	 *            Altezza dell'imamgine
	 * @param larghezzaLarghezza
	 *            dell'immgine
	 * @return
	 */
	private Immagine addImmagine(String id, int sequenza, Boolean visPaginaDoppia, String nomenclatura, int altezza,
			int larghezza) {
		Immagine immagine = null;

		immagine = new Immagine();
		immagine.setID(id);
		immagine.setSequenza(sequenza);
		immagine.setVisPaginaDoppia(visPaginaDoppia);
		immagine.setNomenclatura(nomenclatura);
		immagine.setAltezza(altezza);
		immagine.setLarghezza(larghezza);
		return immagine;
	}

	/**
	 * Funzione utilizzata per reperire le informazioni Bibliografiche dell'opera
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private DatiBibliografici initDatiBibliografici(String risidr) throws SQLException {
		DatiBibliografici datiBibliografici = null;
		String urlDatiBibliografici = null;
		int pos = 0;
		String key = "";
		ResultSet rs = null;
		ViewTbllegnotTblris view = null;

		try {
			view = new ViewTbllegnotTblris(Configuration.getPool("teca"));
			view.setCampoValue("risidr", risidr);
			rs = view.startSelect();
			datiBibliografici = new DatiBibliografici();

			if (rs.next()) {
				if (rs.getString("autore") != null && !rs.getString("autore").trim().equals(""))
					datiBibliografici.setAutore(rs.getString("autore").trim());

				if (rs.getString("titolo") != null && !rs.getString("titolo").trim().equals(""))
					datiBibliografici.setTitolo(rs.getString("titolo").trim());

				if (rs.getString("notePubblicazione") != null && !rs.getString("notePubblicazione").trim().equals(""))
					datiBibliografici.setPubblicazione(rs.getString("notePubblicazione").trim());

				if ((rs.getString("pieceGr") != null && !rs.getString("pieceGr").trim().equals(""))
						&& (rs.getString("PieceDt") != null && !rs.getString("PieceDt").trim().equals("")))
					datiBibliografici
							.setUnitaFisica(rs.getString("pieceGr").trim() + " " + rs.getString("PieceDt").trim());

				urlDatiBibliografici = (String) Configuration.get(
						"imageViewer." + request.getServerName() + ".urlDatiBibliografici",
						Configuration.get("imageViewer.ALL.urlDatiBibliografici", ""));

				if (urlDatiBibliografici != null && !urlDatiBibliografici.trim().equals("")) {
					urlDatiBibliografici = urlDatiBibliografici.replace("<serverName>", request.getServerName());

					pos = urlDatiBibliografici.indexOf("<");
					while (pos > -1) {
						key = urlDatiBibliografici.substring(pos + 1);
						pos = key.indexOf(">");
						if (pos > -1)
							key = key.substring(0, pos);
						try {
							rs.findColumn(key);
							urlDatiBibliografici = urlDatiBibliografici.replace("<" + key + ">", rs.getString(key));
						} catch (SQLException e) {
							urlDatiBibliografici = urlDatiBibliografici.replace("<" + key + ">", "");
						}
						pos = urlDatiBibliografici.indexOf("<");
					}
					datiBibliografici.setUrlDatiBibliografici(urlDatiBibliografici);
				}
			}
		} catch (SQLException e) {
			log.error(e);
			throw e;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (view != null) {
				view.stopSelect();
			}
		}

		return datiBibliografici;
	}
}
