/**
 * 
 */
package mx.imageViewer.imager;

import java.awt.RenderingHints;
import java.awt.image.renderable.ParameterBlock;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.GregorianCalendar;

import javax.imageio.ImageIO;
import javax.media.jai.EnumeratedParameter;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;

import org.apache.log4j.Logger;

import mx.imageViewer.IVConfiguration;
import mx.imageViewer.imager.exception.ImageException;

import com.sun.media.jai.util.SunTileCache;

/**
 * @author massi
 *
 */
public class ImageManager
{

	/**
	 * Questa variabile viene utilizzata per loggare l'aplicazione
	 */
	private static Logger log = Logger.getLogger(ImageManager.class);

	private PlanarImage sourceImageJAI = null;

	private URL url = null;

	/**
	 * 
	 */
	public ImageManager()
	{
	}

	/**
	 * Metodo utlizzato per inizializzare l'immagine
	 * 
	 * @param url
	 * @param isJpeg2000
	 */
	public void initialize(URL url, boolean isJpeg2000)
	{
		GregorianCalendar gcStart = null;
		GregorianCalendar gcStop = null;

		this.url = url;

		gcStart = new GregorianCalendar();
		sourceImageJAI = JAI.create((isJpeg2000?IVConfiguration.IMAGE_READER:IVConfiguration.URL), url);
		gcStop  = new GregorianCalendar();
		log.info("initialize URL: "+this.url.getFile()+" t.: "+(gcStop.getTimeInMillis()-gcStart.getTimeInMillis()));
	}

	/*
	private boolean isTiff()
	{
		boolean ris = false;
		if (sourceImageJAI.getProperty("tiff_directory")!= null)
		{
			if (sourceImageJAI.getProperty("tiff_directory").getClass().getName().equals(TIFFDirectory.class.getName()))
				ris = true;
		}
		return ris;
	}
	*/

	public int getHeight(){
		int height = 0;
		try {
			height = sourceImageJAI.getHeight();
		} catch (Exception e) {
			height = 0;
		}
		return height;
	}

	public int getWidth(){
		int width = 0;
		
		try {
			width = sourceImageJAI.getWidth();
		} catch (Exception e) {
			width = 0;
		}
		return width;
	}
	
	public void resize(double height, double width)
	{
		ParameterBlock param = null;
		double z = 0.0;
		float f;
		
		param = new ParameterBlock();
		param.addSource(sourceImageJAI);
		
		z =height/sourceImageJAI.getHeight();

		f = Float.valueOf(Double.toString(z)).floatValue();
		param.add(f); // The xScale
		param.add(f); // The yScale
		param.add(0.0F); // The x translation
		param.add(0.0F); 

		RenderingHints hints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);           
		sourceImageJAI = JAI.create("scale", param, hints);

	}

	public void sendImage(OutputStream output) 
	{
		GregorianCalendar gcStart = null;
		GregorianCalendar gcStop = null;
		try
		{
			gcStart = new GregorianCalendar();
			ImageIO.write(sourceImageJAI, "JPEG", output);
			gcStop  = new GregorianCalendar();
			log.info("sendImage URL: "+this.url.getFile()+" t.: "+(gcStop.getTimeInMillis()-gcStart.getTimeInMillis()));
		}
		catch (IOException e)
		{
			log.error(e);
		}
		catch (RuntimeException e)
		{
			throw e;
		}
		finally
		{
			sourceImageJAI.dispose();
		}
	}

	public void showInfo()
	{
		log.debug("Show Info");
		log.debug("Bounds:");
		log.debug("\theight: "+sourceImageJAI.getBounds().height);
		log.debug("\twidth: "+sourceImageJAI.getBounds().width);
		log.debug("\tx: "+sourceImageJAI.getBounds().x);
		log.debug("\ty: "+sourceImageJAI.getBounds().y);
		log.debug("\tgetCenterX: "+sourceImageJAI.getBounds().getCenterX());
		log.debug("\tgetCenterY: "+sourceImageJAI.getBounds().getCenterY());
		log.debug("\tgetHeight: "+sourceImageJAI.getBounds().getHeight());
		log.debug("\tgetMaxX: "+sourceImageJAI.getBounds().getMaxX());
		log.debug("\tgetMaxY: "+sourceImageJAI.getBounds().getMaxY());
		log.debug("\tgetMinX: "+sourceImageJAI.getBounds().getMinX());
		log.debug("\tgetMinY: "+sourceImageJAI.getBounds().getMinY());
		log.debug("\tgetWidth: "+sourceImageJAI.getBounds().getWidth());
		log.debug("\tgetX: "+sourceImageJAI.getBounds().getX());
		log.debug("\tgetY: "+sourceImageJAI.getBounds().getY());
		log.debug("\tgetSize: "+sourceImageJAI.getBounds().getSize());
		log.debug("Parameter:");
		String[] st = sourceImageJAI.getPropertyNames();
		for (int x=0; x<st.length; x++)
		{
			log.debug("\t"+st[x]+": "+sourceImageJAI.getProperty(st[x]).getClass().getName()+" - "+sourceImageJAI.getProperty(st[x]));
		}
		SunTileCache stc = (SunTileCache) sourceImageJAI.getProperty("tile_cache");
		log.debug("tile_cache:");
		log.debug("\t"+"getCachedObject"+": "+stc.getCachedObject());
		log.debug("\t"+"getCacheHitCount"+": "+stc.getCacheHitCount());
		log.debug("\t"+"getCacheMemoryUsed"+": "+stc.getCacheMemoryUsed());
		log.debug("\t"+"getCacheMissCount"+": "+stc.getCacheMissCount());
		log.debug("\t"+"getCacheTileCount"+": "+stc.getCacheTileCount());
		log.debug("\t"+"getMemoryCapacity"+": "+stc.getMemoryCapacity());
		log.debug("\t"+"getMemoryThreshold"+": "+stc.getMemoryThreshold());
		log.debug("\t"+"getTileCapacity"+": "+stc.getTileCapacity());
		log.debug("\t"+"getTileComparator"+": "+stc.getTileComparator());
		EnumeratedParameter[] e = SunTileCache.getCachedTileActions();
		for (int x=0; x<e.length; x++)
			log.debug("\t"+"getCachedTileActions"+": "+e[x].getName()+" - "+e[x].getValue());
//		ImageIO.write(sourceImageJAI, "JPEG", output);
	}

	public static URL createURL(String hostProt, String hostServerPath, String hostPathDisco, String fileDig, String hostIp, int hostPorta, 
			String hostLogin, String hostPsw, String mimeType, boolean absolutePath) throws ImageException
	{
		String pathSource = "";
		String fileSource = "";
		String urlString =  "";
		
		log.debug("absolutePath: "+absolutePath);
		log.debug("hostProt: " + hostProt);
		log.debug("hostServerPath: " + hostServerPath);
		log.debug("hostPathDisco: " + hostPathDisco);
		log.debug("fileDig: " + fileDig);
		log.debug("hostIp: " + hostIp);
		log.debug("hostPorta: " + hostPorta);
		log.debug("hostLogin: " + hostLogin);
		log.debug("hostPsw: " + hostPsw);
		
		
		pathSource = "";
		if (absolutePath || hostProt.equalsIgnoreCase("NFS"))
		{
			pathSource = hostServerPath;
			if (pathSource.trim().length()>0)
			{
				if (pathSource.startsWith("./"))
					pathSource = pathSource.substring(1);
				else if (!pathSource.startsWith("/"))
					pathSource = "/"+pathSource;
				if (!pathSource.endsWith("/"))
					pathSource += "/";
			}
			else
				pathSource = "/";
		}
		else
			pathSource = "/";

		if (!hostPathDisco.trim().equals(""))
		{
			if (hostPathDisco.trim().startsWith("./"))
				pathSource += hostPathDisco.trim().substring(2);
			else if (hostPathDisco.trim().startsWith("/"))
				pathSource += hostPathDisco.trim().substring(1);
			else
				pathSource += hostPathDisco.trim();

			if (!pathSource.endsWith("/"))
				pathSource += "/";
		}

		pathSource = pathSource.replace("\\", "/");

		fileSource = fileDig.trim();
		fileSource = fileSource.replace("\\", "/");

		if (fileSource.startsWith("./"))
			fileSource = fileSource.substring(2);
		
		if (hostProt.equalsIgnoreCase("nfs"))
			urlString = "file://"+pathSource+fileSource;
		else
			urlString = "ftp://"+hostLogin+":"+hostPsw+"@"+hostIp+":"+hostPorta+pathSource+fileSource;
		log.debug("urlString: "+urlString);
		return createURL(urlString);
	}
	// method that creates an url from a given string
	public static URL createURL(String urlString) throws ImageException 
	{
		URL outputURL = null;
		
		try 
		{
			log.info("url: "+urlString);
			
	   		URI uri = new URI(urlString.replace(" ", "%20"));
	   		if (uri.getScheme() == null || // nfs
	   				IVConfiguration.FILE_SCHEME.equals(uri.getScheme()) || // ftp 
	   				IVConfiguration.FTP_SCHEME.equals(uri.getScheme()) || // ftp 
	   				IVConfiguration.SFTP_SCHEME.equals(uri.getScheme())) // sftp
	   			outputURL = new URL(urlString);
	   		else
	   			outputURL = new URL(uri.getScheme(), uri.getHost(), uri.getPort(), uri.getPath()); // http
	   		
		} 
		catch (URISyntaxException use) 
		{
			log.error(use.getMessage(), use);
			throw new ImageException("La stringa URL non ha una syntax URI valida!", use);
		}
		catch (MalformedURLException e) 
		{
			log.error(e.getMessage(), e);
			throw new ImageException("La stringa URL: " + urlString + " non e formalmente corretta!", e);
		}
		catch (Exception e) 
		{
			log.error(e.getMessage(), e);
			throw new ImageException(IVConfiguration.GENERIC_ERROR_MESSAGE, e);
		}
		
		return outputURL;
	}
}
