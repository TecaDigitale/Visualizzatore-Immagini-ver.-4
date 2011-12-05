/**
 * 
 */
package mx.imageViewer.imager.exception;

/**
 * @author massi
 *
 */
public class ImageException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -830301948257608132L;

	/**
	 * @param message
	 */
	public ImageException(String message)
	{
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ImageException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
