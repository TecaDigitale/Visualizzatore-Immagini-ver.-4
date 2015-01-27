/**
 * 
 */
package mx.imageViewer.imager.test;



import org.apache.log4j.Logger;

import mx.imageViewer.imager.ImageManager;
import mx.imageViewer.imager.exception.ImageException;

/**
 * @author massi
 *
 */
public class ImageManagerTest
{

	/**
	 * Questa variabile viene utilizzata per loggare l'aplicazione
	 */
	private static Logger log = Logger.getLogger(ImageManagerTest.class);

	/**
	 * 
	 */
	public ImageManagerTest()
	{
	}

	public static void main(String[] args)
	{
		ImageManager imageManager = null;
		
		try
		{
			imageManager = new ImageManager();
			imageManager.initialize(ImageManager.createURL("ftp",
					"/volume4/", "/disk6/CD000255/", "MIL0246426/IMG/A0000007.TIF", 
					"192.168.254.67", 21, "utente", "utente", "image/jpeg", false),false); 
//			ftp://utente:utente@192.168.254.67:21/disk6/CD000255/MIL0246426/IMG/A0000007.TIF
//					�"ftp://utente:utente@192.168.254.67:21//disk1/CD000043/bob353x1/a0000222.tif"), false);
//			imageManager.initialize(ImageManager.createURL("ftp://utente:utente@192.168.254.67:21//disk1/CD000043/bob353x1/a0000222.tif"), false);
			imageManager.showInfo();
			System.out.println("-----------------");
			imageManager.resize(Double.parseDouble("1024"), Double.parseDouble("912"));
			imageManager.showInfo();
		}
		catch (ImageException e)
		{
			log.error("Eccezione dell'immagine",e);
		}
	}
}
