<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl"
    xmlns:mx-pagina="http://www.imageViewer.mx/schema/gestionePagina"
    exclude-result-prefixes="xs xd"
    version="2.0">
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
    			<link rel="stylesheet" type="text/css" href="../mx/style/ReadImages.css"/>
    			<link rel="stylesheet" type="text/css" href="../navigator/style/yahoo_treeview.css"/>
    			
    		    <script type="text/javascript" src="../mx/js/Sugar.js"></script>
    		    <script type="text/javascript" src="../BookReader/lib/jquery-1.2.6.min.js"></script>
    		    <script type="text/javascript" src="../BookReader/lib/jquery.easing.1.3.js"></script>
    		    <script type="text/javascript" src="../BookReader/js/BookReader.js"></script>
    		    <script type="text/javascript" src="../BookReader/js/dragscrollable.js"></script>
    			<!-- script type="text/javascript" src="../mx/js/ajax/AccessoAjax.js"></script -->
    			<script type="text/javascript" src="../mx/js/ReadImages.js"></script>
    			<script type="text/javascript" src="../mx/js/ImageViewer.js"></script>
    			<script type="text/javascript" src="../navigator/js/yahoo_treeview-min.js"></script>
    			<script type="text/javascript" src="../navigator/js/Navigator.js"></script>
    		</head>
    	    <body>
    	    	<xsl:attribute name="onload">
    	    		<xsl:if test="idr">
    	    			return initBook('<xsl:value-of select="idr"/>');
    	    		</xsl:if>
    	    		<xsl:if test="not(idr)">
    	    			alert('E\' necessario indicare l\'opera da visualizzare');
        			</xsl:if>
    	    	</xsl:attribute>
    	    	<div id="Navigator" class="Navigator">
    	    	</div>
    	        <div id="BookReader" class="BookReader">x</div>
    	    	<div id="wait" class="wait">
    	    		Please wait ....
    	    	</div>
    	    </body>
    	</html>
    </xsl:template>
</xsl:stylesheet>