/**
 * 
 */
package mx.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;

import mx.database.ConnectionPool;
import mx.log4j.Logger;
import mx.util.MxHashtable;

/**
 * Questa classe viene utilizzata per la gestione dell'apertura deil file comuni e la
 * connessione con il database
 * 
 * @author Massimiliano Randazzo
 *
 */
public class Configuration
{

	/**
	 * Questa variabile viene utilizzata per gestire la lista dei parametri dell'applicazione
	 */
	public static MxHashtable listaParametri = null;

	/**
	 * Questa variabile viene utilizzata per gestire il pool 
	 */
	private static MxHashtable pool = null;

	/**
	 * Questa variabile viene utilizzata per gestire il log dell'applicazione
	 */
	private static Logger log = new Logger(Configuration.class, "mx.configuration");

	/**
	 * Questa variabile viene utilizzata per indicare la connessione di default
	 */
	public static String DATABASEDEF = null;

	/**
	 * Questo metodo viene utilizzato per la inizializzazione dei file di configurazione
	 * 
	 * @param pathProperties Path dove si trovano i file di Configurazione
	 * @param fileConf Lista dei file di configurazione da caricare
	 * @param listaDatabase Lista dei database da aprire
	 */
	public static void init(String pathProperties, String fileConf, String[] listaDatabase)
	{
		init(pathProperties, fileConf, "", listaDatabase);
	}

	/**
	 * Questo metodo viene utilizzato per la inizializzazione dei file di configurazione
	 * 
	 * @param pathProperties Path dove si trovano i file di Configurazione
	 * @param fileConf Lista dei file di configurazione da caricare
	 * @param tipoUtente Lista degli utenti per gli accessi al Database
	 * @param listaDatabase Lista dei database da aprire
	 */
	public static void init(String pathProperties, String fileConf, String tipoUtente, String[] listaDatabase)
	{
		String[] myFileConf = new String[1];
		myFileConf[0] = fileConf;
		init(pathProperties, myFileConf, tipoUtente, listaDatabase);
	}

	/**
	 * Questo metodo viene utilizzato per la inizializzazione dei file di configurazione
	 * 
	 * @param pathProperties Path dove si trovano i file di Configurazione
	 * @param fileConf Lista dei file di configurazione da caricare
	 * @param tipoUtente Lista degli utenti per gli accessi al Database
	 * @param listaDatabase Lista dei database da aprire
	 */
	public static void init(String pathProperties, String fileConf, String[] tipoUtente, String[] listaDatabase)
	{
		String[] myFileConf = new String[1];
		myFileConf[0] = fileConf;
		init(pathProperties, myFileConf, tipoUtente, listaDatabase);
	}

	/**
	 * Questo metodo viene utilizzato per la inizializzazione dei file di configurazione
	 * 
	 * @param pathProperties Path dove si trovano i file di Configurazione
	 * @param fileConf Lista dei file di configurazione da caricare
	 * @param listaDatabase Lista dei database da aprire
	 */
	public static void init(String pathProperties, String[] fileConf, String[] listaDatabase)
	{
		init(pathProperties, fileConf, "", listaDatabase);
	}

	/**
	 * Questo metodo viene utilizzato per la inizializzazione dei file di configurazione
	 * 
	 * @param pathProperties Path dove si trovano i file di Configurazione
	 * @param fileConf Lista dei file di configurazione da caricare
	 * @param tipoUtente Lista degli utenti per gli accessi al Database
	 * @param listaDatabase Lista dei database da aprire
	 */
	public static void init(String pathProperties, String[] fileConf, String tipoUtente, String[] listaDatabase)
	{
		String[] myTipoUtente = new String[1];
		
		myTipoUtente[0] = tipoUtente;
		init(pathProperties, fileConf, myTipoUtente, listaDatabase);
	}

	/**
	 * Questo metodo viene utilizzato per inizializzare per connessioni con il Database
	 * 
	 * @param pathProperties Path dove si trovano i file di Configurazione
	 * @param fileConf Lista dei file di configurazione da caricare
	 * @param tipoUtente Lista degli utenti per gli accessi al Database
	 * @param listaDatabase Lista dei database da aprire
	 */
	public static void init(String pathProperties, String[] fileConf, String[] tipoUtente, String[] listaDatabase)
	{
		for (int x=0; x<fileConf.length; x++)
			initParameter(pathProperties, fileConf[x]);
		
		if (listaDatabase != null && listaDatabase.length>0)
		{
			for (int x=0; x<listaDatabase.length; x++)
				initDatabase(listaDatabase[x], tipoUtente);
		}
	}

	/**
	 * Questo metodo viene utilizzato per inizializzare per connessioni con il Database
	 * 
	 * @param pathProperties Path dove si trovano i file di Configurazione
	 * @param fileConf Lista dei file di configurazione da caricare
	 * @param tipoUtente Lista degli utenti per gli accessi al Database
	 * @param defDatabase Viene utilizzato per indicare la variabile utilizzata per indicare la lista dei Database
	 */
	public static void init(String pathProperties, String fileConf, String defDatabase)
	{
		init(pathProperties, fileConf, "", defDatabase);
	}

	/**
	 * Questo metodo viene utilizzato per inizializzare per connessioni con il Database
	 * 
	 * @param pathProperties Path dove si trovano i file di Configurazione
	 * @param fileConf Lista dei file di configurazione da caricare
	 * @param tipoUtente Lista degli utenti per gli accessi al Database
	 * @param defDatabase Viene utilizzato per indicare la variabile utilizzata per indicare la lista dei Database
	 */
	public static void init(String pathProperties, String[] fileConf, String defDatabase)
	{
		init(pathProperties, fileConf, "", defDatabase);
	}

	/**
	 * Questo metodo viene utilizzato per inizializzare per connessioni con il Database
	 * 
	 * @param pathProperties Path dove si trovano i file di Configurazione
	 * @param fileConf Lista dei file di configurazione da caricare
	 * @param tipoUtente Lista degli utenti per gli accessi al Database
	 * @param defDatabase Viene utilizzato per indicare la variabile utilizzata per indicare la lista dei Database
	 */
	public static void init(String pathProperties, String fileConf, String tipoUtente, String defDatabase)
	{
		String[] myTipoUtente = new String[1];
		String[] myFileConf = new String[1];

		myTipoUtente[0] =tipoUtente;
		myFileConf[0] =fileConf;
		init(pathProperties, myFileConf, myTipoUtente, defDatabase);
	}

	/**
	 * Questo metodo viene utilizzato per inizializzare per connessioni con il Database
	 * 
	 * @param pathProperties Path dove si trovano i file di Configurazione
	 * @param fileConf Lista dei file di configurazione da caricare
	 * @param tipoUtente Lista degli utenti per gli accessi al Database
	 * @param defDatabase Viene utilizzato per indicare la variabile utilizzata per indicare la lista dei Database
	 */
	public static void init(String pathProperties, String[] fileConf, String tipoUtente, String defDatabase)
	{
		String[] myTipoUtente = new String[1];

		myTipoUtente[0] =tipoUtente;
		init(pathProperties, fileConf, myTipoUtente, defDatabase);
	}

	/**
	 * Questo metodo viene utilizzato per inizializzare per connessioni con il Database
	 * 
	 * @param pathProperties Path dove si trovano i file di Configurazione
	 * @param fileConf Lista dei file di configurazione da caricare
	 * @param tipoUtente Lista degli utenti per gli accessi al Database
	 * @param defDatabase Viene utilizzato per indicare la variabile utilizzata per indicare la lista dei Database
	 */
	public static void init(String pathProperties, String[] fileConf, String[] tipoUtente, String defDatabase)
	{
		String[] listaDatabase = null;

		for (int x=0; x<fileConf.length; x++)
			initParameter(pathProperties, fileConf[x]);

		if (!listaParametri.get(defDatabase, "").equals(""))
		{
			listaDatabase =((String)listaParametri.get(defDatabase)).split(",");

			if (listaDatabase != null && listaDatabase.length>0)
			{
				for (int x=0; x<listaDatabase.length; x++)
					initDatabase(listaDatabase[x], tipoUtente);
			}
		}
	}

	/**
	 * Questo metodo viene utilizzato per inizializzare i paramepri del file Gestionale
	 * 
	 * @param pathProperties Path relativa alla posizione dei file di Properties
	 * @param fileConf Nome deil file di configurazione
	 */
	@SuppressWarnings("unchecked")
	private static void initParameter(String pathProperties, String fileConf)
	{
		String fileName = "";
		File f= null;
		Properties prop = null;
		FileInputStream fis = null;
		String key = "";

		try
		{
			fileName = pathProperties+File.separator + fileConf;
			f = new File(fileName);
			if (f.exists())
			{
				prop = new Properties();
				fis = new FileInputStream(f);
				prop.load(fis);

				if (listaParametri == null)
					listaParametri = new MxHashtable();
				for (Enumeration e = prop.keys(); e.hasMoreElements();)
				{
					key = (String) e.nextElement();
					listaParametri.put(key, prop.getProperty(key, ""));
				}
			}
			else
				log.error("Il File " + f.getAbsolutePath()
						+ " non esiste");
		}
		catch (NumberFormatException e)
		{
			log.error(e);
		}
		catch (FileNotFoundException e)
		{
			log.error(e);
		}
		catch (IOException e)
		{
			log.error(e);
		}
		finally
		{
			try
			{
				if (fis != null)
					fis.close();
			}
			catch (IOException e)
			{
				log.error(e);
			}
		}
	}

	/**
	 * Questo metodo vine utilizzato per la inizializzazione dei parametri del Database
	 * 
	 * @param pathProperties Path relativa alla posizione dei file di Properties
	 */
	@SuppressWarnings("unchecked")
	private static void initDatabase(String databaseName, String[] tipoUtente)
	{
		ConnectionPool pool = null;
		String serverName = "";
		String userName = "";
		String password = "";
		String tipoDatabase = "";
		String nomeDatabase = "";
		int numConnessioni = 0;
		String serverPort = "";
		

		try
		{
			if (databaseName != null && !databaseName.equals(""))
			{
				serverName = (String)listaParametri.get("database."+databaseName+".serverName", "");

				userName = (String)listaParametri.get("database."+databaseName+".userName", "");
				for (int x=0; x<tipoUtente.length; x++)
				{
					if (listaParametri.get("database."+databaseName+".userName."+tipoUtente[x])!= null)
					{
						userName = (String)listaParametri.get("database."+databaseName+".userName."+tipoUtente[x]);
						break;
					}
					else if (listaParametri.get("database."+databaseName+".userName"+tipoUtente[x])!= null)
					{
						userName = (String)listaParametri.get("database."+databaseName+".userName"+tipoUtente[x]);
						break;
					}
				}

				password = (String)listaParametri.get("database."+databaseName+".password", "");
				for (int x=0; x<tipoUtente.length; x++)
				{
					if (listaParametri.get("database."+databaseName+".password."+tipoUtente[x])!= null)
					{
						password = (String)listaParametri.get("database."+databaseName+".password."+tipoUtente[x]);
						break;
					}
					else if (listaParametri.get("database."+databaseName+".password"+tipoUtente[x])!= null)
					{
						password = (String)listaParametri.get("database."+databaseName+".password"+tipoUtente[x]);
						break;
					}
					else if (listaParametri.get("database."+databaseName+".pwdName"+tipoUtente[x])!= null)
					{
						password = (String)listaParametri.get("database."+databaseName+".pwdName"+tipoUtente[x]);
						break;
					}
				}
				if (password.equals(""))
					password = (String)listaParametri.get("database."+databaseName+".pwdName", "");

				tipoDatabase = (String)listaParametri.get("database."+databaseName+".tipoDatabase", "");
				if (tipoDatabase.equals(""))
					tipoDatabase = (String)listaParametri.get("database."+databaseName+".tipoDB", "");

				nomeDatabase = (String)listaParametri.get("database."+databaseName+".nomeDatabase", "");
				if (nomeDatabase.equals(""))
					nomeDatabase = (String)listaParametri.get("database."+databaseName+".databaseName", "");

				if (listaParametri.get("database."+databaseName+".numConnessioni", "").equals(""))
					numConnessioni = Integer.parseInt((String)listaParametri.get("database."+databaseName+".numConn", "2"));
				else
					numConnessioni = Integer.parseInt((String)listaParametri.get("database."+databaseName+".numConnessioni", "2"));

				serverPort = (String)listaParametri.get("database."+databaseName+".serverPort", "");
				if (serverPort.equals(""))
					serverPort = (String)listaParametri.get("database."+databaseName+".portNumber", "");
				pool = new ConnectionPool(serverName, userName, password, tipoDatabase, 
						nomeDatabase, numConnessioni, serverPort);

				if (DATABASEDEF == null)
					DATABASEDEF = databaseName;

				if (Configuration.pool==null)
					Configuration.pool = new MxHashtable();
				Configuration.pool.put(databaseName, pool);
			}
		}
		catch (NumberFormatException e)
		{
			log.error(e);
		}
		catch (SQLException e)
		{
			log.error(e);
		}
	}

	/**
	 * Questo metodo viene utilizzato per reperire il Pool di connessioni
	 * 
	 * @param databaseName Nome della connessione
	 * @return Pool di connessione
	 */
	public static ConnectionPool getPool(String databaseName)
	{
		if (pool.get(databaseName)!= null)
			return (ConnectionPool) pool.get(databaseName);
		else
			return null;
	}

	/**
	 * Questo metodo viene utilizzato per reperire il Pool di connessioni
	 * 
	 * @param databaseName Nome della connessione
	 * @return Pool di connessione
	 */
	public static ConnectionPool getPool()
	{
		return getPool(DATABASEDEF);
	}

	/**
	 * Questo metodo viene utilizzato per chiudere tutte le connessioni aperte
	 */
	@SuppressWarnings("unchecked")
	public static void closeConnection()
	{
		String databaseName = null;

		for (Enumeration e = pool.keys(); e.hasMoreElements();)
		{
			databaseName = (String)e.nextElement();
			((ConnectionPool)pool.get(databaseName)).closeAll();
		}
	}
}
