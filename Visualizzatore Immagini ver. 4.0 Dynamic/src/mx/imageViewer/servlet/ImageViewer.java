package mx.imageViewer.servlet;


import java.io.File;
import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import javax.xml.bind.PropertyException;

import mx.configuration.Configuration;
import mx.converter.ConverterXsl;
import mx.converter.exception.ConvertXslException;
import mx.imageviewer.schema.gestionelibro.ReadBook;
import mx.imageviewer.servlet.interfacie.IImageViewer;
import mx.imageviewer.servlet.interfacie.manifestPrefix.ImageViewerNamespacePrefixMapper;
import mx.log4j.Logger;
import mx.teca.export.GestioneXsd;

/**
 * Servlet implementation class ImageViewer
 */
public class ImageViewer extends HttpServlet implements Servlet {

	/**
	 * Questa variabile viene utilizzata per indicare il Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Questa variabile viene utlizzata per loggare l'applicazione
	 */
	private Logger log = new Logger(ImageViewer.class, "mx.imageViewer.servlet");

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ImageViewer() {
		super();
		log.debug("Costruttore");
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init() throws ServletException {
		String pathProperties = null;
		String[] fileConf = new String[2];

		log.debug("init");
		if (this.getServletContext().getInitParameter("nomeCatalogo") != null
				&& this.getServletContext().getInitParameter("nomeCatalogo")
						.startsWith("file://"))
			pathProperties = this.getServletContext().getInitParameter(
					"nomeCatalogo").replace("file:///", "");
		else {
			if (System.getProperty("catalina.base") != null)
				pathProperties = System.getProperty("catalina.base")
						+ File.separator;
			else
				pathProperties = System.getProperty("jboss.server.home.dir")
						+ File.separator;

			if (this.getServletContext().getInitParameter("nomeCatalogo") == null)
				pathProperties += "conf/teca_digitale";
			else
				pathProperties += this.getServletContext().getInitParameter(
						"nomeCatalogo");
		}

		log.debug("pathProperties: " + pathProperties);

		fileConf[0] = "ImageViewer.properties";
		fileConf[1] = "TecaDatabase.properties";

		log.debug("fileConf: " + fileConf.toString());
		Configuration.init(pathProperties, fileConf, "database");
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		log.debug("destroy");
		Configuration.closeConnection();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		log.debug("doGet");
		esegui(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		log.debug("doPost");
		esegui(request, response);
	}

	/**
	 * Questo metodo viene utilizzato per eseguire le operazioni relative
	 * all'applicativo
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private void esegui(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		mx.imageviewer.schema.gestionepagina.ImageViewer imgViewer = null;
		ReadBook readBook = null;
		IImageViewer imageViewer = null;
		Class myClass = null;
		String metodo = "";
		String azione = "";
		String className = "";
		String fileXsl = "";

		try {
			log.debug("esegui");
			if (request.getParameter("metodo") != null)
				metodo = request.getParameter("metodo");
			else
				metodo = (String) Configuration.listaParametri
						.get("imageViewer.default");
			log.debug("metodo: " + metodo);

			if (request.getParameter("azione") != null)
				azione = request.getParameter("azione");
			else
				azione = "home";
			log.debug("azione: " + azione);

			if (!((String) Configuration.listaParametri.get("imageViewer."
					+ metodo + ".class", "")).equals("")) {
				className = (String) Configuration.listaParametri.get(
						"imageViewer." + metodo + ".class", "");
				log.debug("className: " + className);

				myClass = Class.forName(className);
				imageViewer = (IImageViewer) myClass.newInstance();

				if (azione.equals("home")) {
					imgViewer = imageViewer.initPage(request, response);

					if (imgViewer != null) {
						response.setContentType("text/html; charset=UTF-8");
						response.setCharacterEncoding("UTF-8");

						fileXsl = (String) Configuration.listaParametri.get(
								"imageViewer." + request.getServerName()
										+ ".xsl", Configuration.listaParametri
										.get("imageViewer.ALL.xsl", ""));
						ConverterXsl
								.convertXsl(
										fileXsl,
										GestioneXsd
												.writeInputStream(
														imgViewer,
														new ImageViewerNamespacePrefixMapper()),
										response.getOutputStream());
					} else
						throw new ServletException(
								"Non risulta essere presente le informazioni necessarie per la creazione della pagina principale");
				} else if (azione.equals("readBook")) {
					readBook = imageViewer.readBook(request, response);

					if (readBook != null) {
						response.setContentType("text/xml; charset=UTF-8");
						response.setCharacterEncoding("UTF-8");
						GestioneXsd.write(readBook, response.getOutputStream(),
								new ImageViewerNamespacePrefixMapper());
					} else
						throw new ServletException(
								"Non risulta essere presente le informazioni del libro");
				} else if (azione.equals("showImg")) {
					imageViewer.showImage(request, response);
				} else
					throw new ServletException(
							"L'azione richiesta non \u00E8 prevista da questa applicazione");
			} else
				throw new ServletException(
						"Il metodo richiesto non \u00E8 previsto da questa applicazione");
		} catch (ClassNotFoundException e) {
			log.error(e);
			throw new ServletException(e.getMessage());
		} catch (InstantiationException e) {
			log.error(e);
			throw new ServletException(e.getMessage());
		} catch (IllegalAccessException e) {
			log.error(e);
			throw new ServletException(e.getMessage());
		} catch (PropertyException e) {
			log.error(e);
			throw new ServletException(e.getMessage());
		} catch (ConvertXslException e) {
			log.error(e);
			throw new ServletException(e.getMessage());
		} catch (JAXBException e) {
			log.error(e);
			throw new ServletException(e.getMessage());
		} catch (Exception e) {
			log.error(e);
			throw new ServletException(e.getMessage());
		}
	}
}
