<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml">
  <xsl:output doctype-system="about:legacy-compat"/>
  <xsl:template match="/page">
    <html>
       <head>
	     <meta charset="utf-8"/>
	     <xsl:value-of select="title"/>
	   </head>
	   <body>
	     <xsl:copy-of select="body"/>
	   </body>
	 </html>
  </xsl:template>
</xsl:stylesheet>