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
import mx.imageViewer.schema.ImageViewerXsd;
import mx.imageViewer.schema.OperaXsd;
import mx.imageViewer.schema.ReadBookXsd;
import mx.imageViewer.schema.gestionelibro.ReadBook;
import mx.imageViewer.schema.gestioneopera.Opera;
import mx.imageViewer.servlet.interfacie.IImageViewer;
import mx.imageViewer.servlet.interfacie.manifestPrefix.ImageViewerNamespacePrefixMapper;
import mx.randalf.converter.xsl.ConverterXsl;
import mx.randalf.converter.xsl.exception.ConvertXslException;
import mx.randalf.xsd.exception.XsdException;

import org.apache.log4j.Logger;

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
	private Logger log = Logger.getLogger(ImageViewer.class);

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
	private void esegui(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String metodo = "";
		String azione = "";
		String className = "";

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

			if (!((String) Configuration.get("imageViewer."
					+ metodo + ".class", "")).equals("")) {
				className = (String) Configuration.get(
						"imageViewer." + metodo + ".class", "");
				log.debug("className: " + className);

//				myClass = Class.forName(className);
//				imageViewer = (IImageViewer) myClass.newInstance();

				if (azione.equals("home")) {
					showHome(request,response, className);
				} else if (azione.equals("readBook")) {
					bookReader(request, response, className);
				} else if (azione.equals("readCatalogo")) {
					readCatalogo(request, response, className);
				} else if (azione.equals("showCatalogo")) {
					showCatalogo(request, response, className);
				} else if (azione.equals("readStru")) {
					readStru(request, response, className);
				} else if (azione.equals("showImg")) {
					showImg(request, response, className);
				} else
					throw new ServletException(
							"L'azione richiesta non \u00E8 prevista da questa applicazione");
			} else
				throw new ServletException(
						"Il metodo richiesto non \u00E8 previsto da questa applicazione");
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(),e);
			throw new ServletException(e.getMessage());
		} catch (InstantiationException e) {
			log.error(e.getMessage(),e);
			throw new ServletException(e.getMessage());
		} catch (IllegalAccessException e) {
			log.error(e.getMessage(),e);
			throw new ServletException(e.getMessage());
		} catch (PropertyException e) {
			log.error(e.getMessage(),e);
			throw new ServletException(e.getMessage());
		} catch (ConvertXslException e) {
			log.error(e.getMessage(),e);
			throw new ServletException(e.getMessage(),e);
		} catch (JAXBException e) {
			log.error(e.getMessage(),e);
			throw new ServletException(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw new ServletException(e.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	private void showHome(HttpServletRequest request, HttpServletResponse response, String className) 
			throws PropertyException, ClassNotFoundException, InstantiationException, IllegalAccessException,
					ServletException, IOException, ConvertXslException, JAXBException, Exception{
		IImageViewer imageViewer = null;
		Class myClass = null;
		mx.imageViewer.schema.gestionepagina.ImageViewer imgViewer = null;
		String fileXsl = "";
		ImageViewerXsd imageViewerXsd = null;

		try {
			myClass = Class.forName(className);
			imageViewer = (IImageViewer) myClass.newInstance();

			imgViewer = imageViewer.initPage(request, response, request.getServerName());

			if (imageViewer.isShowOpere()){
				showCatalogo(request, response, className);
			} else if (imgViewer != null) {
				response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");

				fileXsl = imageViewer.getFoglioXsl(request.getServerName());
				imageViewerXsd = new ImageViewerXsd();
				ConverterXsl
						.convertXsl(
								fileXsl,
								imageViewerXsd.writeInputStream(imgViewer, new ImageViewerNamespacePrefixMapper()),
								response.getOutputStream());
			} else{
				throw new ServletException(
						"Non risulta essere presente le informazioni necessarie per la creazione della pagina principale");
			}
		} catch (PropertyException e) {
			throw e;
		} catch (ClassNotFoundException e) {
			throw e;
		} catch (InstantiationException e) {
			throw e;
		} catch (IllegalAccessException e) {
			throw e;
		} catch (ServletException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} catch (ConvertXslException e) {
			throw e;
		} catch (JAXBException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@SuppressWarnings("rawtypes")
	private void bookReader(HttpServletRequest request, HttpServletResponse response, String className)
			throws  ClassNotFoundException, InstantiationException, IllegalAccessException,
			ServletException, IOException, XsdException {
		IImageViewer imageViewer = null;
		Class myClass = null;
		ReadBook readBook = null;
		ReadBookXsd readBookXsd = null;

		try {
			myClass = Class.forName(className);
			imageViewer = (IImageViewer) myClass.newInstance();

			imageViewer.initPage(request, response, request.getServerName());

			readBook = imageViewer.readBook(request, response);

			if (readBook != null) {
				response.setContentType("text/xml; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
				readBookXsd = new ReadBookXsd();
				readBookXsd.write(readBook, response.getOutputStream(), 
						new ImageViewerNamespacePrefixMapper(), null, 
						null, null);
			} else{
				throw new ServletException(
						"Non risulta essere presente le informazioni del libro");
			}
		} catch (ClassNotFoundException e) {
			throw e;
		} catch (InstantiationException e) {
			throw e;
		} catch (IllegalAccessException e) {
			throw e;
		} catch (ServletException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} catch (XsdException e) {
			throw e;
		}

	}

	@SuppressWarnings("rawtypes")
	private void readCatalogo(HttpServletRequest request, HttpServletResponse response, String className)
			throws  ClassNotFoundException, InstantiationException, IllegalAccessException,
			ServletException, IOException, XsdException {
		IImageViewer imageViewer = null;
		Class myClass = null;
		OperaXsd operaXsd = null;
		Opera opera= null;

		try {
			myClass = Class.forName(className);
			imageViewer = (IImageViewer) myClass.newInstance();

			opera = imageViewer.readCatalogo(request, response);

			if (opera != null) {
				response.setContentType("text/xml; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
				operaXsd = new OperaXsd();
				operaXsd.write(opera, response.getOutputStream(), null, 
						null, null, null);
			} else {
				throw new ServletException(
						"Non risulta essere presente le informazioni del libro");
			}
		} catch (ClassNotFoundException e) {
			throw e;
		} catch (InstantiationException e) {
			throw e;
		} catch (IllegalAccessException e) {
			throw e;
		} catch (ServletException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} catch (XsdException e) {
			throw e;
		}
	}

	@SuppressWarnings("rawtypes")
	private void showCatalogo(HttpServletRequest request, HttpServletResponse response, String className)
			throws PropertyException, ClassNotFoundException, InstantiationException, IllegalAccessException,
			ServletException, IOException, XsdException, ConvertXslException, JAXBException, Exception{
		IImageViewer imageViewer = null;
		Class myClass = null;
		mx.imageViewer.schema.gestionepagina.ImageViewer imgViewer = null;
		String fileXsl = "";
		ImageViewerXsd imageViewerXsd = null;

		try {
			myClass = Class.forName(className);
			imageViewer = (IImageViewer) myClass.newInstance();
			imgViewer = imageViewer.showCatalogo(request, response);

			if (imgViewer != null) {
				response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");

				imageViewerXsd = new ImageViewerXsd();
				log.debug(imageViewerXsd.write(imgViewer, null, null, null, null));
				fileXsl = imageViewer.getFoglioXsl(request.getServerName());
				ConverterXsl
						.convertXsl(
								fileXsl,
								imageViewerXsd.writeInputStream(imgViewer, 
										new ImageViewerNamespacePrefixMapper()),
								response.getOutputStream());
			} else{
				throw new ServletException(
						"Non risulta essere presente le informazioni del libro");
			}
		} catch (PropertyException e) {
			throw e;
		} catch (ClassNotFoundException e) {
			throw e;
		} catch (InstantiationException e) {
			throw e;
		} catch (IllegalAccessException e) {
			throw e;
		} catch (ServletException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} catch (XsdException e) {
			throw e;
		} catch (ConvertXslException e) {
			throw e;
		} catch (JAXBException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@SuppressWarnings("rawtypes")
	private void readStru(HttpServletRequest request, HttpServletResponse response, String className)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException,
			ServletException, IOException, XsdException{
		IImageViewer imageViewer = null;
		Class myClass = null;
		Opera opera= null;
		OperaXsd operaXsd = null;

		try {
			myClass = Class.forName(className);
			imageViewer = (IImageViewer) myClass.newInstance();
			opera = imageViewer.readStru(request, response);

			if (opera != null) {
				response.setContentType("text/xml; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
				operaXsd = new OperaXsd();
				operaXsd.write(opera, response.getOutputStream(), null, null, null, null);
			} else{
				throw new ServletException(
						"Non risulta essere presente le informazioni del libro");
			}
		} catch (ClassNotFoundException e) {
			throw e;
		} catch (InstantiationException e) {
			throw e;
		} catch (IllegalAccessException e) {
			throw e;
		} catch (ServletException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} catch (XsdException e) {
			throw e;
		}
	}

	@SuppressWarnings("rawtypes")
	private void showImg(HttpServletRequest request, HttpServletResponse response, String className)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException,
			ServletException, IOException{
		IImageViewer imageViewer = null;
		Class myClass = null;

		try {
			myClass = Class.forName(className);
			imageViewer = (IImageViewer) myClass.newInstance();
			imageViewer.showImage(request, response);
		} catch (ClassNotFoundException e) {
			throw e;
		} catch (InstantiationException e) {
			throw e;
		} catch (IllegalAccessException e) {
			throw e;
		} catch (ServletException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}
		
	}
}
