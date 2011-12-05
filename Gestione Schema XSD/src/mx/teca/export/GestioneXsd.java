package mx.teca.export;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Marshaller.Listener;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.stream.XMLStreamWriter;
import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import mx.log4j.Logger;

/**
 * Questa classe viene utilizzata per la gestione delle conversioni dei tracciati Xml con le librerie JAXB
 * 
 * @author Massimiliano Randazzo
 *
 */
public class GestioneXsd 
{

	/**
	 * Questa variabile viene utilizzata per loggare l'applicazione
	 */
	private static Logger log = new Logger(GestioneXsd.class, "it.siav.teca.export");
	
	/**
	 * Questo metodo viene utilizzato per leggere un file Xml
	 * 
	 * @param fileXml
	 * @param packageName
	 * @return
	 * @throws JAXBException
	 */
	public static Object readXml(String fileXml, String packageName) throws JAXBException
	{
		Unmarshaller um = null;
		JAXBContext jc = null;
		Object output = null;
		File f = null;

		try
		{
			log.debug("readXml()");

			f = new File(fileXml);
			if (f.exists())
			{
				log.debug("Utente.package: "+packageName);
				jc = JAXBContext.newInstance( packageName );

				um = jc.createUnmarshaller();

				output = um.unmarshal(f);
			
			}
		}
		catch (JAXBException e)
		{
			throw e;
		}
		return output;
	}

	/**
	 * Questo metodo viene utilizzato per leggere un tracciato Xml
	 * 
	 * @param input
	 * @param packageName
	 * @return
	 * @throws JAXBException
	 */
	public static Object readXml(InputStream input, String packageName) throws JAXBException
	{
		Unmarshaller um = null;
		JAXBContext jc = null;
		Object output = null;

		try
		{
			log.debug("readXml()");

			log.debug("Utente.package: "+packageName);
			jc = JAXBContext.newInstance( packageName );

			um = jc.createUnmarshaller();

			output = um.unmarshal(input);
			
		}
		catch (JAXBException e)
		{
			throw e;
		}
		return output;
	}

	/**
	 * Questo metodo viene utilizzato per convertire le informazioni disponibili in una stringa XML
	 * 
	 * @param datiXml
	 * @return
	 * @throws PropertyException
	 * @throws JAXBException
	 * @throws IOException
	 * @throws Exception
	 */
	public static String writeXml(Object datiXml) throws PropertyException, JAXBException, IOException, Exception
	{
		ByteArrayOutputStream baos = null;
		
		baos = (ByteArrayOutputStream) writeOutputStream(datiXml);
		return baos.toString();
	}

	/**
	 * Questo metodo viene utilizzato per convertire le informazioni disponibili 
	 * in una InputStream XML
	 * 
	 * @param datiXml
	 * @return
	 * @throws PropertyException
	 * @throws JAXBException
	 * @throws IOException
	 * @throws Exception
	 */
	public static InputStream writeInputStream(Object datiXml) throws PropertyException, JAXBException, IOException, Exception
	{
		return writeInputStream(datiXml, null);
	}

	/**
	 * Questo metodo viene utilizzato per convertire le informazioni disponibili 
	 * in una InputStream XML
	 * 
	 * @param datiXml
	 * @return
	 * @throws PropertyException
	 * @throws JAXBException
	 * @throws IOException
	 * @throws Exception
	 */
	public static InputStream writeInputStream(Object datiXml, NamespacePrefixMapper namespacePrefixMapper) throws PropertyException, JAXBException, IOException, Exception
	{
		ByteArrayOutputStream baos = null;
		
		baos = (ByteArrayOutputStream) writeOutputStream(datiXml, namespacePrefixMapper);
		log.info(baos.toString());
		return new ByteArrayInputStream(baos.toByteArray());
	}

	/**
	 * Questo metodo viene utilizzato per convertire le informazioni disponibili 
	 * in una OutputStream  XML
	 * 
	 * @param datiXml
	 * @return
	 * @throws PropertyException
	 * @throws JAXBException
	 * @throws IOException
	 * @throws Exception
	 */
	public static OutputStream writeOutputStream(Object datiXml) throws PropertyException, JAXBException, IOException, Exception
	{
		return writeOutputStream(datiXml, null);
	}

	/**
	 * Questo metodo viene utilizzato per convertire le informazioni disponibili 
	 * in una OutputStream  XML
	 * 
	 * @param datiXml
	 * @return
	 * @throws PropertyException
	 * @throws JAXBException
	 * @throws IOException
	 * @throws Exception
	 */
	public static OutputStream writeOutputStream(Object datiXml, NamespacePrefixMapper namespacePrefixMapper) throws PropertyException, JAXBException, IOException, Exception
	{
		Marshaller m = null;
		JAXBContext jc = null;
		ByteArrayOutputStream baos = null;
		
		log.debug("write()");

		log.debug("Utente.package: "+datiXml.getClass().getPackage().getName());
		jc = JAXBContext.newInstance(datiXml.getClass().getPackage().getName());

		log.debug("jc.createUnmarshaller()");
		m = jc.createMarshaller();
		
		if (namespacePrefixMapper != null)
			m.setProperty("com.sun.xml.bind.namespacePrefixMapper", namespacePrefixMapper);

		log.debug("m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);");
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
		
		baos = new ByteArrayOutputStream();
		log.debug("m.marshal( utente, fos )");
		m.marshal( datiXml, baos );
		return baos;
	}

	/**
	 * Questo metodo viene utilizzato per convertire le informazioni disponibili 
	 * in una OutputStream  XML
	 * 
	 * @param datiXml
	 * @return
	 * @throws PropertyException
	 * @throws JAXBException
	 * @throws IOException
	 * @throws Exception
	 */
	public static void write(Object datiXml, OutputStream output) throws PropertyException, JAXBException, IOException, Exception
	{
		write(datiXml, output, null);
	}

	/**
	 * Questo metodo viene utilizzato per convertire le informazioni disponibili 
	 * in una OutputStream  XML
	 * 
	 * @param datiXml
	 * @return
	 * @throws PropertyException
	 * @throws JAXBException
	 * @throws IOException
	 * @throws Exception
	 */
	public static void write(Object datiXml, OutputStream output, NamespacePrefixMapper namespacePrefixMapper) throws PropertyException, JAXBException, IOException, Exception
	{
		Marshaller m = null;
		JAXBContext jc = null;
		
		log.debug("write()");

		log.debug("Utente.package: "+datiXml.getClass().getPackage().getName());
		jc = JAXBContext.newInstance(datiXml.getClass().getPackage().getName());

		log.debug("jc.createUnmarshaller()");
		m = jc.createMarshaller();

		log.debug("m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);");
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
		
		if (namespacePrefixMapper != null)
			m.setProperty("com.sun.xml.bind.namespacePrefixMapper", namespacePrefixMapper);
		
		log.debug("m.marshal( utente, fos )");
		m.marshal( datiXml, output );
	}

	/**
	 * Questo metodo viene utilizzato per scrivere le informazioni disponibili su File
	 * 
	 * @param fileXml
	 * @param datiXml
	 * @throws PropertyException
	 * @throws JAXBException
	 * @throws IOException
	 * @throws Exception
	 */
	public static void writeXml(String fileXml, Object datiXml) throws PropertyException, JAXBException, IOException, Exception
	{
		 writeXml(fileXml, datiXml, null);
	}

	/**
	 * Questo metodo viene utilizzato per scrivere le informazioni disponibili su File
	 * 
	 * @param fileXml
	 * @param datiXml
	 * @throws PropertyException
	 * @throws JAXBException
	 * @throws IOException
	 * @throws Exception
	 */
	public static void writeXml(String fileXml, Object datiXml, String schemaLocation) throws PropertyException, JAXBException, IOException, Exception
	{
		writeXml(fileXml, datiXml, schemaLocation, null, null, null);
	}

	/**
	 * Questo metodo viene utilizzato per scrivere le informazioni disponibili su File
	 * 
	 * @param fileXml
	 * @param datiXml
	 * @throws PropertyException
	 * @throws JAXBException
	 * @throws IOException
	 * @throws Exception
	 */
	public static void writeXml(String fileXml, Object datiXml, String schemaLocation, NamespacePrefixMapper namespacePrefixMapper) throws PropertyException, JAXBException, IOException, Exception
	{
		writeXml(fileXml, datiXml, schemaLocation, null, null, namespacePrefixMapper);
	}

	/**
	 * Questo metodo viene utilizzato per scrivere le informazioni disponibili su File
	 * 
	 * @param fileXml
	 * @param datiXml
	 * @throws PropertyException
	 * @throws JAXBException
	 * @throws IOException
	 * @throws Exception
	 */
	public static void writeXml(String fileXml, Object datiXml, String schemaLocation, XmlAdapter<Object, Object> xmlAdapter) throws PropertyException, JAXBException, IOException, Exception
	{
		 writeXml(fileXml, datiXml, schemaLocation, xmlAdapter, null);
	}

	/**
	 * Questo metodo viene utilizzato per scrivere le informazioni disponibili su File
	 * 
	 * @param fileXml
	 * @param datiXml
	 * @throws PropertyException
	 * @throws JAXBException
	 * @throws IOException
	 * @throws Exception
	 */
	public static void writeXml(String fileXml, Object datiXml, String schemaLocation, XmlAdapter<Object, Object> xmlAdapter, Listener listener) throws PropertyException, JAXBException, IOException, Exception
	{
		writeXml(fileXml, datiXml, schemaLocation, xmlAdapter, listener, null);
		
	}

	/**
	 * Questo metodo viene utilizzato per scrivere le informazioni disponibili su File
	 * 
	 * @param fileXml
	 * @param datiXml
	 * @throws PropertyException
	 * @throws JAXBException
	 * @throws IOException
	 * @throws Exception
	 */
	public static void writeXml(String fileXml, Object datiXml, String schemaLocation, XmlAdapter<Object, Object> xmlAdapter, 
			Listener listener, NamespacePrefixMapper namespacePrefixMapper) throws PropertyException, JAXBException, IOException, Exception
	
	{
		Marshaller m = null;
		JAXBContext jc = null;
		File fOut = null;
		FileOutputStream fos = null;
		
		try
		{
			log.debug("write()");

			log.debug("Utente.package: "+datiXml.getClass().getPackage().getName());
			jc = JAXBContext.newInstance(datiXml.getClass().getPackage().getName());

			log.debug("jc.createUnmarshaller()");
			m = jc.createMarshaller();

			log.debug("m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);");
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
			if (namespacePrefixMapper != null)
				m.setProperty("com.sun.xml.bind.namespacePrefixMapper", namespacePrefixMapper);


			if (listener != null)
				m.setListener(listener);

			if (xmlAdapter != null)
				m.setAdapter(xmlAdapter);

			if (schemaLocation != null)
				m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, schemaLocation);
			log.debug("fileXml: "+fileXml);
			fOut =  new File(fileXml);
			if (fOut.getParentFile() != null && !fOut.getParentFile().exists())
				if (!fOut.getParentFile().mkdirs())
					throw new Exception("Problemi nella creazone della cartella ["+fOut.getParentFile().getAbsolutePath()+"]");

			log.debug("fOut.exists(): "+fOut.exists());

			log.debug("FileOutputStream(fOut)");
			fos = new FileOutputStream(fOut);

			log.debug("m.marshal( utente, fos )");
			m.marshal( datiXml, fos );
		}
		catch (PropertyException e)
		{
			log.error(e);
			throw e;
		}
		catch (JAXBException e)
		{
			log.error(e);
			throw e;
		}
		catch (Exception e)
		{
			log.error(e);
			throw e;
		}
		finally
		{
			try
			{
				if (fos != null)
				{
					fos.flush();
					fos.close();
				}
			}
			catch (IOException e)
			{
				log.error(e);
				throw e;
			}
		}
	}
	

	/**
	 * Questo metodo viene utilizzato per scrivere le informazioni disponibili su File
	 * 
	 * @param fileXml
	 * @param datiXml
	 * @throws PropertyException
	 * @throws JAXBException
	 * @throws IOException
	 * @throws Exception
	 */
	public static void writeXml(XMLStreamWriter xmlStreamWriter, Object datiXml, String schemaLocation) throws PropertyException, JAXBException, IOException, Exception
	{
		Marshaller m = null;
		JAXBContext jc = null;
		
		try
		{
			log.debug("write()");

			log.debug("Utente.package: "+datiXml.getClass().getPackage().getName());
			jc = JAXBContext.newInstance(datiXml.getClass().getPackage().getName());

			log.debug("jc.createUnmarshaller()");
			m = jc.createMarshaller();

			log.debug("m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);");
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);

			if (schemaLocation != null)
				m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, schemaLocation);

			log.debug("m.marshal( utente, fos )");
			m.marshal( datiXml, xmlStreamWriter);
		}
		catch (PropertyException e)
		{
			log.error(e);
			throw e;
		}
		catch (JAXBException e)
		{
			log.error(e);
			throw e;
		}
		catch (Exception e)
		{
			log.error(e);
			throw e;
		}
	}
}
