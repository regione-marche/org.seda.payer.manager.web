<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="move-button[@style='fwd']">
  <input border="0" type="image" name="{@id}" src="{$context}/{$payerpath}/wcf/changeorder/move-down.png" width="9" height="9"/>
</xsl:template>

<xsl:template match="move-button[@style='bwd']">
  <input border="0" type="image" name="{@id}" src="{$context}/{$payerpath}/wcf/changeorder/move-up.png" width="9" height="9"/>
</xsl:template>

<xsl:template match="move-button[@style='cut']">
  <input border="0" type="image" name="{@id}" src="{$context}/{$payerpath}/wcf/changeorder/cut.png" width="9" height="9" title="{@title}"/>
</xsl:template>

<xsl:template match="move-button[@style='uncut']">
  <input border="0" type="image" name="{@id}" src="{$context}/{$payerpath}/wcf/changeorder/uncut.png" width="9" height="9" title="{@title}"/>
</xsl:template>

<xsl:template match="move-button[@style='paste-before']">
  <input border="0" type="image" name="{@id}" src="{$context}/{$payerpath}/wcf/changeorder/paste-before.png" width="9" height="9" title="{@title}"/>
</xsl:template>

<xsl:template match="move-button[@style='paste-after']">
  <input border="0" type="image" name="{@id}" src="{$context}/{$payerpath}/wcf/changeorder/paste-after.png" width="9" height="9" title="{@title}"/>
</xsl:template>

<xsl:template match="move-button">
  <img src="{$context}/{$payerpath}/wcf/changeorder/move-empty.png" width="9" height="9"/>
</xsl:template>

</xsl:stylesheet>