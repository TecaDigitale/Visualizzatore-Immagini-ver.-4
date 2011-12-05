/**
 * 
 */
package mx.imageViewer.implement.readBook;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import mx.configuration.Configuration;
import mx.database.table.Column;
import mx.imageviewer.schema.gestionelibro.DatiBibliografici;
import mx.imageviewer.schema.gestionelibro.Immagini;
import mx.imageviewer.schema.gestionelibro.Immagini.Immagine;
import mx.log4j.Logger;
import mx.teca.archivi.arsbni.view.ViewListaIdrImg;

/**
 * @author massi
 *
 */
public class ReadBook
{

	/**
	 * Variabile utilizzata per loggare l'appplicazione
	 */
	private Logger log = new Logger(ReadBook.class, "mx.imageViewer.implement");

	/**
	 * Questa variabile viene utilizzata per gestire il colloqui da parte del client
	 */
	private HttpServletRequest request = null;

	/**
	 * Costruttore 
	 */
	public ReadBook(HttpServletRequest request)
	{
		this.request = request;
	}

	public mx.imageviewer.schema.gestionelibro.ReadBook esegui(String risIdr, String usage)
	{
		ViewListaIdrImg view = null;
		ResultSet rs = null;
		mx.imageviewer.schema.gestionelibro.ReadBook readBook = null;
		Immagini immagini = null;
		int imgLength = 0;
		int imgWidth = 0;
		
		try
		{
			view = new ViewListaIdrImg(Configuration.getPool("teca"));
			view.setCampoValue("risIdr", risIdr);
			view.setCampoValue("ImgUsage", usage);
			view.getCampo("RelRisSequenza").setOrderBy(Column.ORDERBY_CRES, 1);
			rs = view.startSelect();

			if (view.getRecTot()>0)
			{
				while(rs.next())
				{
					if (readBook == null)
					{
						readBook = new mx.imageviewer.schema.gestionelibro.ReadBook();
						readBook.setDatiBibliografici(initDatiBibliografici(rs));
					}
					if (immagini == null)
					{
						immagini = new Immagini();
						immagini.setNumImg(view.getRecTot());
						immagini.setIsCostola(false);
					}
					if (rs.getString("risNotaPub").toLowerCase().contains("costola"))
						immagini.setIsCostola(true);

					imgLength = (rs.getInt("imgLength")==0?800:rs.getInt("imgLength"));
					imgWidth = (rs.getInt("imgWidth")==0?600:rs.getInt("imgWidth"));
					if (imgLength>imgWidth)
  					immagini.getImmagine().add(
  							addImmagine(
  									rs.getString("RelRisIdrPartenza"), 
  									rs.getInt("RelRisSequenza"), 
  									rs.getString("risNotaPub").toLowerCase().contains("costola"), 
  									rs.getString("risNotaPub"), 
  									imgLength, 
  									imgWidth));
					else
						immagini.getImmagine().add(
								addImmagine(
										rs.getString("RelRisIdrPartenza"), 
										rs.getInt("RelRisSequenza"), 
										rs.getString("risNotaPub").toLowerCase().contains("costola"), 
										rs.getString("risNotaPub"), 
										imgWidth, 
										imgLength));
				}
				if (immagini != null)
					readBook.setImmagini(immagini);
			}
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
		return readBook;
	}

	/**
	 * Questo metodo viene utilizzato per la definizione del nodo Immagine
	 * 
	 * @param id Identificativo dell'immagine
	 * @param sequenza Numero di sequenza dell'immagine
	 * @param nomenclatura Nomenclatura dell'imamgine
	 * @param altezza Altezza dell'imamgine
	 * @param larghezzaLarghezza dell'immgine
	 * @return
	 */
	private Immagine addImmagine(String id, int sequenza, Boolean visPaginaDoppia, String nomenclatura, 
			int altezza, int larghezza)
	{
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
	private DatiBibliografici initDatiBibliografici(ResultSet rs) throws SQLException
	{
		DatiBibliografici datiBibliografici = null;
		String urlDatiBibliografici = null;
		int pos = 0;
		String key = "";

		try
		{
			datiBibliografici = new DatiBibliografici();

			if (rs.getString("tmpAutore") != null && 
					!rs.getString("tmpAutore").trim().equals(""))
				datiBibliografici.setAutore(rs.getString("tmpAutore").trim());

			if (rs.getString("tmpTitolo") != null && 
					!rs.getString("tmpTitolo").trim().equals(""))
				datiBibliografici.setTitolo(rs.getString("tmpTitolo").trim());

			if (rs.getString("tmpNotePubblicazione") != null && 
					!rs.getString("tmpNotePubblicazione").trim().equals(""))
				datiBibliografici.setPubblicazione(rs.getString("tmpNotePubblicazione").trim());

			if ((rs.getString("pieceGr") != null && 
					!rs.getString("pieceGr").trim().equals("")) &&
					(rs.getString("PieceDt") != null && 
							!rs.getString("PieceDt").trim().equals("")))
				datiBibliografici.setUnitaFisica(rs.getString("pieceGr").trim()+" "+rs.getString("PieceDt").trim());

			urlDatiBibliografici = (String)Configuration.listaParametri.get("imageViewer."+request.getServerName()+".urlDatiBibliografici", 
					Configuration.listaParametri.get("imageViewer.ALL.urlDatiBibliografici", ""));

			if (urlDatiBibliografici != null &&
					!urlDatiBibliografici.trim().equals(""))
			{
				urlDatiBibliografici = urlDatiBibliografici.replace("<serverName>", request.getServerName());

				pos = urlDatiBibliografici.indexOf("<");
				while(pos>-1)
				{
					key = urlDatiBibliografici.substring(pos+1);
					pos = key.indexOf(">");
					if (pos>-1)
						key = key.substring(0,pos);
					try
					{
						rs.findColumn(key);
						urlDatiBibliografici = urlDatiBibliografici.replace("<"+key+">", rs.getString(key));
					}
					catch (SQLException e)
					{
						urlDatiBibliografici = urlDatiBibliografici.replace("<"+key+">", "");
					}
					pos = urlDatiBibliografici.indexOf("<");
				}
				datiBibliografici.setUrlDatiBibliografici(urlDatiBibliografici);
			}
		}
		catch (SQLException e)
		{
			log.error(e);
			throw e;
		}

		return datiBibliografici;
	}
}
