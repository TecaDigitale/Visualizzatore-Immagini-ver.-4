<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl"
    xmlns:mx-pagina="http://www.imageViewer.mx/schema/gestionePagina"
    exclude-result-prefixes="xs xd"
    version="2.0">
	<xsl:include href="StdGraph.xsl"/>
    <xd:doc scope="stylesheet">
        <xd:desc>
            <xd:p><xd:b>Created on:</xd:b> Jan 6, 2011</xd:p>
            <xd:p><xd:b>Author:</xd:b> massi</xd:p>
            <xd:p></xd:p>
        </xd:desc>
    </xd:doc>

    <xsl:output method="html" version="4.0" encoding="UTF-8"/>

	<xsl:template match="/mx-pagina:imageViewer">
    	<html>
    		<head>
    			<title><xsl:value-of select="titolo"/></title>

    		    <meta http-equiv="cache-control" content="no-cache"/>
    		    <meta http-equiv="content-language" content="it-IT"/>
    		    <meta http-equiv="expires" content="Mon, 22 Jul 2002 11:12:01 GMT"/>
    		    <meta http-equiv="pragma" content="no-cache"/>
    		    <meta name="robots" content="none"/>
    		    <meta name="googlebot" content="noarchive"/>
    			
    			
    		    <link rel="stylesheet" type="text/css" href="../BookReader/style/BookReader.css"/>
    			<link rel="stylesheet" type="text/css" href="../BookReader/style/BookReaderEmbed.css"/>
                <link rel="stylesheet" type="text/css" href="../BookReader/style/BookReaderLending.css"/>
                <link rel="stylesheet" type="text/css" href="../mx/style/ReadImages.css"/>
    			<xsl:text disable-output-escaping="yes">
		          &lt;!--[if IE]&gt;&lt;link rel="stylesheet" href="../mx/style/ReadImages-ie.css" type="text/css"&gt;&lt;![endif]--&gt;
		          &lt;!--[if IE 6]&gt;&lt;link rel="stylesheet" href="../mx/style/ReadImages-ie6.css" type="text/css"&gt;&lt;![endif]--&gt;
		        </xsl:text>
    			<link rel="stylesheet" type="text/css" href="../navigator/style/yahoo_treeview.css"/>
    			
    		    <script type="text/javascript" src="../mx/js/Sugar.js"></script>
    			<script type="text/javascript" src="../mx/js/BrowserDetected.js"></script>
    			<script type="text/javascript" src="../BookReader/lib/jquery-1.4.2.min.js"></script>
    		    <script type="text/javascript" src="../BookReader/lib/jquery-ui-1.8.5.custom.min.js"></script>
    		    <script type="text/javascript" src="../BookReader/js/dragscrollable.js"></script>
                <script type="text/javascript" src="../BookReader/lib/jquery.colorbox-min.js"></script>
                <script type="text/javascript" src="../BookReader/lib/jquery.ui.ipad.js"></script>
                <script type="text/javascript" src="../BookReader/lib/jquery.bt.min.js"></script>
                <script type="text/javascript" src="../BookReader/js/BookReader.js"></script>
    		    <xsl:text disable-output-escaping="yes">
                    &lt;!--[if IE]&gt;&lt;script type="text/javascript" src="../BookReader/js/BookReader-ie.js"&gt;&lt;/script&gt;&lt;![endif]--&gt;
                </xsl:text>
                <script type="text/javascript" src="../mx/js/ReadImages.js"></script>
    			<script type="text/javascript" src="../mx/js/ImageViewer.js"></script>
    			<script type="text/javascript" src="../navigator/js/yahoo_treeview-min.js"></script>
    			<script type="text/javascript" src="../navigator/js/Navigator.js"></script>
    			<xsl:if test="not(xlimage) and showNavigatore">
    			  <script type="text/javascript">
                    var _url = "http://";
                    _url += location.host;
                    _url += "<xsl:text disable-output-escaping="yes">/ImageViewer/servlet/ImageViewer?azione=readCatalogo&amp;idr=</xsl:text>";
                    _url += "<xsl:value-of select="showNavigatore/@idr"/>";
    			  </script>
    			</xsl:if>
    		    <xsl:if test="not(xlimage) and showStru">
    		      <script type="text/javascript">
                    var _urlStru = "http://";
                    _urlStru += location.host;
                    _urlStru += "<xsl:text disable-output-escaping="yes">/ImageViewer/servlet/ImageViewer?azione=readStru&amp;idr=</xsl:text>";
                    _urlStru += "<xsl:value-of select="showStru/@idr"/>";
    			  </script>
    			</xsl:if>
    		</head>
    	    <body>
    	    	<xsl:attribute name="onload">
    	    		<xsl:if test="idr and not(msgError)">
    	    			return initBook('<xsl:value-of select="idr"/>');
    	    		</xsl:if>
    	    		<xsl:if test="msgError">
    	    			alert('<xsl:value-of select="msgError"/>');
    	    		</xsl:if>
    	    		<xsl:if test="not(idr) and not(msgError)">
    	    			alert('E\' necessario indicare l\'opera da visualizzare');
        			</xsl:if>
    	    	</xsl:attribute>
    	    	<div id="Navigator" class="Navigator">
        		</div>
        		<xsl:call-template name="header"/>
    	    	<br/>
            <div class="clear"></div>
            <div class="menu">
              <a class="menu" accesskey="i" title="Pagina precedente" alt="Pagina precedente" onClick="javascript:history.back(); return false;" href="#">Indietro</a> |
              <a class="menu" accesskey="m" title="Home page" alt="Home page" href="/TecaRicerca/">home</a> |
              <a id="active" class="menu" accesskey="b" title="Visualizza Opera" alt="Visualizza Opera" href="#">Visualizza Opera</a>
              <xsl:if test="not(xlimage) and showNavigatore">
                |
                <a class="menu" accesskey="v" href="#" title="Visualizza l'elenco opere collegate" alt="Visualizza l'elenco opere collegate">
                  <xsl:attribute name="onclick">
                    toggle_sidebar();
                    return false;
                  </xsl:attribute>
                  <!--img src="../mx/icon/Icon-Book.jpg" class="iconBook" id="iconBook"/-->
                  Visualizza Volumi
                </a>
              </xsl:if>
              <xsl:if test="not(xlimage) and showStru">
                |
                <a class="menu" accesskey="s" href="#" 
                   title="Visualizza l'indice dell'opera" 
                   alt="Visualizza l'indice dell'opera">
                  <xsl:attribute name="onclick">
                    toggle_sidebarStru();
                    return false;
                  </xsl:attribute>
                  <!--img src="../mx/icon/Icon-Book.jpg" class="iconBook" id="iconBook"/-->
                  Visualizza Indice
                </a>
              </xsl:if>
            </div>
        <div id="columns">
    	  <div class="cols-wrapper" id="cols-wrapper">
    	    <xsl:if test="not(xlimage) and showNavigatore">
    	      <div id="col-c" class="sidecol">
	    	    <iframe height="100%" width="100%" src="../tree.html"/>
    	      </div>
    	    </xsl:if>
            <xsl:if test="not(xlimage) and showStru">
              <div id="col-c" class="sidecolStru">
                <iframe height="100%" width="100%" src="../treeStru.html"/>
              </div>
            </xsl:if>
            <div id="BookReader" class="BookReader">x</div>
          </div>
        </div>
    	<xsl:call-template name="footer"/>
    	<div class="clear"></div>
        <xsl:call-template name="wait"/>
      </body>
    </html>
  </xsl:template>

</xsl:stylesheet>
