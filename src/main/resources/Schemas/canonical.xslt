<xsl:stylesheet version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:lems="http://www.neuroml.org/lems/0.8.0"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

  <xsl:output method="xml" omit-xml-declaration="yes" indent="yes" version="1.0"/>
  
  <xsl:preserve-space elements="*"/>
 
	<xsl:template match="lems:Lems">
		<Lems>
			<xsl:apply-templates/>
		</Lems>
	</xsl:template>

	<xsl:template match="Lems">
		<Lems>
			<xsl:apply-templates/>
		</Lems>
	</xsl:template>

	<!-- Add all the lems labels -->
	<xsl:template match="lems:ComponentType|lems:Target|lems:Unit|lems:Dimension|lems:Assertion|lems:Include|lems:Component|lems:Constant|lems:Parameter|lems:Dynamics|lems:StateVariable|lems:Exposure">
 		<xsl:copy>
 		 <xsl:copy-of select="@*" />
         <xsl:apply-templates/>
        </xsl:copy>
 	</xsl:template>
 
 	<xsl:template match="*">
 		<Component>
 			<xsl:attribute name="type"><xsl:value-of select="local-name()"/></xsl:attribute>
 			<xsl:copy-of select="@*"/>
	 		<xsl:apply-templates mode="incomponent"/>
 		</Component>
 	</xsl:template>
 
 
 	<xsl:template match="*" mode="incomponent">
 		<Component>
 			<xsl:choose>
 				<xsl:when test="@type">
 					<xsl:attribute name="child"><xsl:value-of select="local-name()"/></xsl:attribute>	
 				</xsl:when>
				<xsl:otherwise>
		 			<xsl:attribute name="type"><xsl:value-of select="local-name()"/></xsl:attribute>		
				</xsl:otherwise>
 			</xsl:choose>
 
 			<xsl:copy-of select="@*"/>
	 		<xsl:apply-templates mode="incomponent"/>
 		</Component>
 	</xsl:template>
 
 </xsl:stylesheet>