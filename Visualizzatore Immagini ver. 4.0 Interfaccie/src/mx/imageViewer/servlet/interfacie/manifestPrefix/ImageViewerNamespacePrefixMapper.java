package mx.imageviewer.servlet.interfacie.manifestPrefix;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

public class ImageViewerNamespacePrefixMapper extends NamespacePrefixMapper
{

	@Override
	public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix)
	{
    if (namespaceUri.equals("http://www.imageViewer.mx/schema/gestionePagina")) 
      return "mx-pagina";
    else if (namespaceUri.equals("http://www.imageViewer.mx/schema/gestioneLibro")) 
      return "mx-libro";
    else
    {
    	if (suggestion == null && !namespaceUri.equals(""))
    		System.out.println("namespaceUri: "+namespaceUri+ " suggestion: "+suggestion);
    	return suggestion;
    }
	}

}
