<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:niso="http://www.niso.org/pdfs/DataDict.pdf" xmlns:targetNamespace="http://www.iccu.sbn.it/metaAG1.pdf" xmlns:xlink="http://www.w3.org/TR/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

  <xsl:template match="/" name="stdGraph">
    <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="it" lang="it">
      <head>
        <!-- meta http-equiv="Content-Type" content="text/html; charset=utf-8" / -->
        <meta name="description" content="" />
        <meta name="keywords" content="" />
        <title>
          Archivio Centrale dello Stato - Opac
          <xsl:if test="MxServlet/title">
            - <xsl:value-of select="MxServlet/title"/>
          </xsl:if>
        </title>
        <link rel="stylesheet" type="text/css" href="../style/StdGraph.css"/>
        <xsl:text disable-output-escaping="yes">
          &lt;!--[if IE]&gt;&lt;link rel="stylesheet" href="../style/StdGraph-ie.css" type="text/css"&gt;&lt;![endif]--&gt;
        </xsl:text>
        <link rel="stylesheet" type="text/css" href="../style/Navigatore.css"/>
        <xsl:text disable-output-escaping="yes">
          &lt;!--[if IE]&gt;&lt;link rel="stylesheet" href="../style/Navigatore-ie.css" type="text/css"&gt;&lt;![endif]--&gt;
        </xsl:text>
        <link rel="stylesheet" type="text/css" href="../style/Filtri.css"/>
        <xsl:text disable-output-escaping="yes">
          &lt;!--[if IE]&gt;&lt;link rel="stylesheet" href="../style/Filtri-ie.css" type="text/css"&gt;&lt;![endif]--&gt;
        </xsl:text>
        <xsl:if test="/scheda">
          <link rel="stylesheet" type="text/css" href="../style/schede/scheda.css"/>
          <xsl:text disable-output-escaping="yes">
            &lt;!--[if IE]&gt;&lt;link rel="stylesheet" href="../style/schede/scheda-ie.css" type="text/css"&gt;&lt;![endif]--&gt;
          </xsl:text>
        </xsl:if>
        <xsl:if test="/scheda/rappresentazione">
          <link rel="stylesheet" type="text/css" href="../style/schede/rappresentazione.css"/>
        </xsl:if>
        <xsl:apply-templates select="MxServlet/styleSheets/styleSheet"></xsl:apply-templates>

        <script language="javascript" src="../js/StdGraph.js" type="text/javascript"></script>
        <xsl:if test="/scheda">
          <script language="javascript" src="../js/schede/scheda.js" type="text/javascript"></script>
        </xsl:if>
        <xsl:apply-templates select="MxServlet/javaScripts/javaScript"></xsl:apply-templates>

      </head>
      <body onunload="WaitVisible();">
        <xsl:if test="/MxServlet/home/show">
          <xsl:if test="/MxServlet/home/show/menu">
            <xsl:attribute name="onload">WaitInvisible();placeFocus('<xsl:value-of select="/MxServlet/home/show/menu"/>');</xsl:attribute>
          </xsl:if>
          <xsl:if test="not(/MxServlet/home/show/menu)">
            <xsl:attribute name="onload">WaitInvisible();placeFocus('base');</xsl:attribute>
          </xsl:if>
        </xsl:if>
        <xsl:if test="not(/MxServlet/home/show)"><xsl:attribute name="onload">WaitInvisible();</xsl:attribute></xsl:if>
        <div id="contenitore">
          <div id="header">
            <a href="http://www.acs.beniculturali.it/ " target="_blank"><img src="../images/logoacs.jpg" class="logoacs"/></a>
          </div>
          <div id="center">
            <xsl:apply-templates/>
          </div>
          <div class="version">
            <div class="versionText">
              <xsl:text disable-output-escaping="yes">
              Opac Versione: 3.2 &amp;nbsp;-&amp;nbsp; 26/01/2010
            </xsl:text>
            </div>
            <div class="logosiav"><img src="../images/logo2.gif" class="logosiav"/></div>
            <div class="clear"><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></div>
          </div>
        </div>
        <div id="wait" class="wait">
          Please wait ....
        </div>
      </body>
    </html>
  </xsl:template>

  <xsl:template match="MxServlet/styleSheets/styleSheet">
    
    <xsl:text disable-output-escaping="yes">
    </xsl:text>
    <xsl:if test="@check">
      <xsl:text disable-output-escaping="yes">&lt;!--[if </xsl:text><xsl:value-of select="@check"/><xsl:text disable-output-escaping="yes">]&gt;</xsl:text>
    </xsl:if>
    <link>
      <xsl:attribute name="rel">stylesheet</xsl:attribute>
      <xsl:attribute name="href"><xsl:value-of select="."/></xsl:attribute>
      <xsl:attribute name="type">text/css</xsl:attribute>
    </link>
    <xsl:if test="@check">
      <xsl:text disable-output-escaping="yes">&lt;![endif]--&gt;</xsl:text>
    </xsl:if>
  </xsl:template>

  <xsl:template match="MxServlet/javaScripts/javaScript">
    <xsl:text disable-output-escaping="yes">
    </xsl:text>
    <script>
      <xsl:attribute name="type">text/javascript</xsl:attribute>
      <xsl:attribute name="src"><xsl:value-of select="."/></xsl:attribute>
    </script>
    
  </xsl:template>

  <xsl:template match="title"/>
  <xsl:template match="anagrafica"/>
  <xsl:template match="javaScripts"/>
  <xsl:template match="styleSheets"/>
  <xsl:template match="msgErr"/>
  <xsl:template match="cascadeMenu"/>
  
</xsl:stylesheet>
