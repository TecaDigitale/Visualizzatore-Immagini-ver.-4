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

	<xsl:template name="header">
    	<div id="header" class="header">
                <div id="logo" class="logo">
			<img id="logo-cava" border="0" alt="Logo" title="Logo" src="http://www.consiglioveneto.it/crvportal/images/headervenetointernet_1.gif"/>
		</div>
    		<div class="head_text2">
    			<span class="head_text2" id="head_text2"></span>
    		</div>
    		<div class="logoHeader" id="logoHeader">
    			</div>
    	</div>
    </xsl:template>

	<xsl:template name="footer">
    	<div class="footer" id="footer">
    		<div class="footerCenter">
    			<table class="footer" align="center">
    				<tr>
    					<td class="logo1"><span id="logo1"><img border="0" alt="" title="" src="http://www.w3.org/Icons/valid-xhtml10.png"/></span></td>
    					<td id="showVersion" class="version">
    						Versione: <b>4.1.0</b> - <b>30 gennaio 2015</b><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
    					</td>
    					<td rowspan="2" class="help"><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
    						<a href="#" onclick="showHelp();return false;" accesskey="h" title="Help">Non riesci a visualizzare la pagina?</a>
    						<xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
    					</td>
    				</tr>
    			</table>
    		</div>
    	</div>
    </xsl:template>
    
    <xsl:template name="wait">
    	<div id="wait" class="wait">
    		Please wait ....
    	</div>
    </xsl:template>
</xsl:stylesheet>
