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
                
                <link rel="stylesheet" type="text/css" href="../mx/style/ReadImages.css"/>
                <xsl:text disable-output-escaping="yes">
		          &lt;!--[if IE]&gt;&lt;link rel="stylesheet" href="../mx/style/ReadImages-ie.css" type="text/css"&gt;&lt;![endif]--&gt;
		        </xsl:text>
                
                <link rel="stylesheet" type="text/css" href="../xlimage/style/stdGraph.css"/>
                <xsl:text disable-output-escaping="yes">
		          &lt;!--[if IE]&gt;&lt;link rel="stylesheet" href="../xlimage/style/stdGraph-ie.css" type="text/css"&gt;&lt;![endif]--&gt;
		        </xsl:text>
                
                <link rel="stylesheet" type="text/css" href="../xlimage/style/dtree.css"/>
                <xsl:text disable-output-escaping="yes">
		          &lt;!--[if IE]&gt;&lt;link rel="stylesheet" href="../xlimage/style/dtree-ie.css" type="text/css"&gt;&lt;![endif]--&gt;
		        </xsl:text>
                
                <script type="text/javascript" src="../xlimage/js/dtree.js"></script>
                <script type="text/javascript" src="../xlimage/js/Resize.js"></script>
                <script type="text/javascript" src="../xlimage/js/stdGraph.js"></script>
            </head>
            <body onload="return initFocus();" onunload="return waitBoxStart();">
                <div class="myBody" id="myBody">
                    <xsl:call-template name="header"/>

                    <xsl:call-template name="footer"/>
                    <div class="zoom" id="zoom">
                        <span id="zoomImg"/>
                    </div>
                    <div class="center" id="center">
                        <div class="column" id="paginaCol">
                            <div class="pagine" id="pagina">
                                <a href="#" onclick="pageFirst();return false;">
                                    <img src="../xlimage/images/first.gif" alt="Prima pagina" title="Prima pagina" border="0"/>
                                </a>
                                <a href="#" onclick="pagePrevious();return false;">
                                    <img src="../xlimage/images/previous.gif" alt="Pagina precedente" title="Pagina precedente" border="0"/>
                                </a>
                                <a href="#" onclick="pageNext();return false;">
                                    <img src="../xlimage/images/next.gif" alt="Pagina successiva" title="Pagina successiva" border="0"/>
                                </a>
                                <a href="#" onclick="pageLast();return false;">
                                    <img src="../xlimage/images/last.gif" alt="Ultima pagina" title="Ultima pagina" border="0"/>
                                </a>
                                <a href="#" onclick="history.back();return false;">
                                    <img src="../xlimage/images/home.gif" alt="Torna alla ricerca" title="Torna alla ricerca" border="0"/>
                                </a>
                                <script type="text/javascript">
                                    var link = '';
                                    isApplet = false;
                                    dPagine = new dTree('dPagine');
                                    dPagine.config.urlWeb='../xlimage/';
                                    dPagine.config.useStatusText=true; 
                                    dPagine.add('0',-1,'&amp;nbsp;&amp;nbsp;<xsl:value-of select="xlimage/@titolo"/>',link);
                                    dPagine.add('IMG',0,'Immagini',link);
                                    
                                    <xsl:for-each select="xlimage/pagina">
                                        link='';
                                        addPage('<xsl:value-of select="@urlPage"/>');
                                        link = "<xsl:value-of select="@link"/>";
                                        dPagine.add('<xsl:value-of select="@key"/>','<xsl:value-of select="@keyPadre"/>','<xsl:value-of select="."/>',link);
                                    </xsl:for-each>
                                    pageAtt=0;
                                    pageTot=1;
                                    document.write(dPagine);
                                    changePage(pageAtt);
                                </script>  
                            </div>
                            <div class="paginaShow" id="paginaShow">
                                <a href="#" onclick="return changeViewCol('pagina', 'Pagine');">
                                    <img src="../xlimage/images/left.gif" alt="Nascondi Pagine" title="Nascondi Pagine" id="paginaImg" border="0"/>
                                </a>
                            </div>
                        </div>
                        <div class="viewer" id="viewer">
                            <iframe name="myApplet" id="myApplet">
                                <xsl:attribute name="src">
                                    <xsl:value-of select="xlimage/pagina[position()=1]/@urlPage"/>
                                </xsl:attribute>
                                E' necessario un browser che supporti iFrame
                            </iframe>
                        </div>
                    </div>
                </div>
                <xsl:call-template name="wait"/>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>