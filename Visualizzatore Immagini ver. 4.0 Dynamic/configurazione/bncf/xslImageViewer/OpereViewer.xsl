<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl"
    xmlns:mx-pagina="http://www.imageViewer.mx/schema/gestionePagina"
    xmlns:mx-opera="http://www.imageViewer.mx/schema/gestioneOpera"
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
            </xsl:text>
            <link rel="stylesheet" type="text/css" href="../navigator/style/yahoo_treeview.css"/>
            <link rel="stylesheet" type="text/css" href="../mx/style/ShowOpere.css"/>
            <xsl:text disable-output-escaping="yes">
              &lt;!--[if IE]&gt;&lt;link rel="stylesheet" href="../mx/style/ShowCatalogo-ie.css" type="text/css"&gt;&lt;![endif]--&gt;
            </xsl:text>
          
            <script type="text/javascript" src="../mx/js/BrowserDetected.js"></script>
            <script type="text/javascript" src="../xlimage/js/stdGraph.js"></script>
            <script type="text/javascript" src="../mx/js/ShowCatalogo.js"></script>
          </head>
          <body onload="init();">
            <div id="Navigator" class="Navigator">
            </div>
            <xsl:call-template name="header"/>
            <br/>
            <div class="clear"></div>
            <div class="menu" id="menu">
            </div>
            <div class="clear"></div>
            <div id="columns">
              <br/>
              <h1>Lista documenti collegatiAAAAAA</h1>
              <xsl:for-each select="mx-opera:libro">
                <h2>Titolo: <xsl:value-of select="@titolo" disable-output-escaping="yes"/><br/>Autore: <xsl:value-of select="@autore" disable-output-escaping="yes"/></h2>
                <xsl:for-each select="mx-opera:libro">
                  <h3><xsl:value-of select="@titolo" disable-output-escaping="yes"/></h3>
                  <table class="showDocumenti">
                  <tr>
                    <th class="link"><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></th>
                    <th class="volume">Volume</th>
                  </tr>
                  	<xsl:for-each select="mx-opera:volume">
                    <tr>
                      <td>
                        <a href="#">
                          <xsl:attribute name="onclick">
                            openExternalOpera('<xsl:value-of select="@mx-opera:href" disable-output-escaping="yes"/>');
                            return false;
                          </xsl:attribute>
                          Visualizza opera
                        </a>
                      </td>
                      <td class="volume"><xsl:value-of select="." disable-output-escaping="yes"/></td>
                    </tr>
                    </xsl:for-each>
                  </table>
                </xsl:for-each>
              </xsl:for-each>
            </div>
            <xsl:call-template name="footer"/>
            <div class="clear"></div>
            <xsl:call-template name="wait"/>
          </body>
      </html>
  </xsl:template>

</xsl:stylesheet>
