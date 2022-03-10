<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:pwcwc="http://pwc.t-systems.com/pdm/windchill_v02" xmlns:dmc="http://pwc.t-systems.com/datamodel/common">
<!-- 
#
# Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
#
# See the AUTHORS file(s) distributed with this work for additional
# information regarding authorship.
#
# See the LICENSE file(s) distributed with this work for
# additional information regarding license terms.
#
# Sample digital twin / asset administration shell transformed out of an xml file
-->
	<xsl:output method="text" encoding="UTF-8"  omit-xml-declaration="yes" indent="no" media-type="application/json" />
	<xsl:strip-space elements="*" />
    
	<xsl:param name="ADAPTER_URL"/>
	<xsl:param name="CONNECTOR_URL"/>

	<xsl:template match="/">
      <xsl:text>{
  "description": [
    {
      "language": "en",
      "text": "The shell for a brake system"
    }
  ],
  "globalAssetId": {
    "value": [
      "urn:twin:com.tsystems#3c7556f7-6956-4360-8036-d03e5a79c3c8"
    ]
  },
  "idShort": "brake_dt_2019_snr.asm",
  "identification": "3c7556f7-6956-4360-8036-d03e5a79c3c8",
  "specificAssetIds": [
        {
            "key": "http://pwc.t-systems.com/datamodel/common",
            "value": "0000000251"
        },
        {
            "key": "urn:VR:wt.part.WTPart#",
            "value": "25054146@nis11c130.epdm-d.edm.dsh.de"
        }
  ],
  "submodelDescriptors": [
    {
      "description": [
        {
          "language": "en",
          "text": "Catena-X Traceability Aspect Implementation"
        }
      ],
      "idShort": "bom-aspect",
      "identification": "brake-traceability",
      "semanticId": {
        "value": [
          "urn:bamm:com.catenaX:0.0.1#Traceability"
        ]
      },
      "endpoints": [
        {
          "interface": "</xsl:text><xsl:value-of select="$CONNECTOR_URL"/><xsl:text>",
          "protocolInformation": {
            "endpointAddress": "</xsl:text><xsl:value-of select="$ADAPTER_URL"/><xsl:text>/offer-windchill/shells/3c7556f7-6956-4360-8036-d03e5a79c3c8/aas/brake-traceability",
            "endpointProtocol": "AAS/SUBMODEL",
            "endpointProtocolVersion": "1.0RC02"
          }
        }
      ]
    },
    {
      "description": [
        {
          "language": "en",
          "text": "Catena-X Material Aspect Implementation"
        }
      ],
      "idShort": "material-aspect",
      "identification": "brake-material",
      "semanticId": {
        "value": [
          "urn:bamm:com.catenaX:0.0.1#Material"
        ]
      },
      "endpoints": [
        {
          "interface": "</xsl:text><xsl:value-of select="$CONNECTOR_URL"/><xsl:text>",
          "protocolInformation": {
            "endpointAddress": "</xsl:text><xsl:value-of select="$ADAPTER_URL"/><xsl:text>/offer-windchill/shells/3c7556f7-6956-4360-8036-d03e5a79c3c8/aas/brake-material",
            "endpointProtocol": "AAS/SUBMODEL",
            "endpointProtocolVersion": "1.0RC02"
          }
        }
      ]
    }
  ]
}
</xsl:text>
    </xsl:template>

</xsl:stylesheet>