package com.example.export

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.model.PetitionData
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.nio.charset.StandardCharsets
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

object DocxExporter {

    fun generateDocxBytes(data: PetitionData): ByteArray {
        val bos = ByteArrayOutputStream()
        ZipOutputStream(bos).use { zos ->
            // 1. [Content_Types].xml
            zos.putNextEntry(ZipEntry("[Content_Types].xml"))
            zos.write(contentTypesXml().toByteArray(StandardCharsets.UTF_8))
            zos.closeEntry()

            // 2. _rels/.rels
            zos.putNextEntry(ZipEntry("_rels/.rels"))
            zos.write(rootRelsXml().toByteArray(StandardCharsets.UTF_8))
            zos.closeEntry()

            // 3. word/_rels/document.xml.rels
            zos.putNextEntry(ZipEntry("word/_rels/document.xml.rels"))
            zos.write(docRelsXml().toByteArray(StandardCharsets.UTF_8))
            zos.closeEntry()

            // 4. word/document.xml
            zos.putNextEntry(ZipEntry("word/document.xml"))
            zos.write(documentXml(data).toByteArray(StandardCharsets.UTF_8))
            zos.closeEntry()
        }
        return bos.toByteArray()
    }

    private fun contentTypesXml(): String {
        return """<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types">
  <Default Extension="rels" ContentType="application/vnd.openxmlformats-package.relationships+xml"/>
  <Default Extension="xml" ContentType="application/xml"/>
  <Override PartName="/word/document.xml" ContentType="application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml"/>
</Types>"""
    }

    private fun rootRelsXml(): String {
        return """<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
  <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument" Target="word/document.xml"/>
</Relationships>"""
    }

    private fun docRelsXml(): String {
        return """<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships"/>"""
    }

    private fun documentXml(d: PetitionData): String {
        val sb = StringBuilder()
        sb.append("""<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<w:document xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main">
  <w:body>
""")

        // Title: ABSENT PETITION (Center, Bold, Underline)
        sb.append(xmlParagraph("ABSENT PETITION", align = "center", bold = true, underline = true, fontSize = 28, spaceAfter = 180))

        // Court Heading
        sb.append(xmlParagraph(d.courtHeading, align = "center", bold = true, fontSize = 24, spaceAfter = 140))

        // Crl.M.P.No with tabs
        sb.append(xmlTabbedParagraph("Crl.M.P.No.", "/${d.filingYear}", align = "center", bold = true, fontSize = 24, spaceAfter = 80))

        // IN
        sb.append(xmlParagraph("IN", align = "center", bold = true, fontSize = 24, spaceAfter = 80))

        // Case No.
        sb.append(xmlParagraph(d.mainCaseNo, align = "center", bold = true, fontSize = 24, spaceAfter = 180))

        // Parties: Between:
        sb.append(xmlParagraph("Between:", align = "left", bold = true, fontSize = 22, spaceAfter = 60))
        d.accusedList.forEach { a ->
            sb.append(xmlParagraph("${a.name} (${a.rank})", align = "left", fontSize = 22, spaceAfter = 40))
        }
        sb.append(xmlParagraph("…${d.petitionerLabel}", align = "right", bold = true, fontSize = 22, spaceAfter = 100))

        // // VERSUS //
        sb.append(xmlParagraph("// VERSUS //", align = "center", bold = true, fontSize = 22, spaceBefore = 80, spaceAfter = 80))

        // Respondent
        sb.append(xmlParagraph(d.respondentName, align = "left", fontSize = 22, spaceAfter = 40))
        sb.append(xmlParagraph("…Respondent / Complainant", align = "right", bold = true, fontSize = 22, spaceAfter = 180))

        // Provision
        sb.append(xmlParagraph("PETITION FILED UNDER ${d.legalProvision}", align = "center", bold = true, underline = true, fontSize = 24, spaceBefore = 120, spaceAfter = 160))

        // Salutation
        sb.append(xmlParagraph("May it please your Honour:", align = "left", bold = true, fontSize = 22, spaceAfter = 100))

        // Paras
        sb.append(xmlParagraph("1.  That the ${d.petitionerRef} ${d.beVerb} Accused in the above said case and same is posted for ${d.pronounRef} ${d.hearingPurpose}.", align = "both", fontSize = 22, spaceAfter = 100))
        sb.append(xmlParagraph("2.  ${d.getReasonSummary()}", align = "both", fontSize = 22, spaceAfter = 100))
        sb.append(xmlParagraph("3.  That the absence of the ${d.petitionerRef} before the Hon’ble Court on today is neither intentional nor wanton, but due to above said bonafide reason only.", align = "both", fontSize = 22, spaceAfter = 160))

        // Prayer
        sb.append(xmlParagraph("P R A Y E R", align = "center", bold = true, fontSize = 24, spaceBefore = 140, spaceAfter = 100))
        sb.append(xmlParagraph("Therefore, it is prayed that the Hon’ble Court may kindly be pleased to allow this Absent Petition and dispense with the personal attendance of the ${d.petitionerRef} on today before the Hon’ble Court in the interest of justice.", align = "both", bold = true, fontSize = 22, indentFirstLine = 720, spaceAfter = 260))

        // Signatures (Two column / side by side via paragraphs)
        sb.append(xmlParagraph("Place: ${d.placeName}.\nDate: ${d.filingDate}", align = "left", fontSize = 22, spaceAfter = 60))
        sb.append(xmlParagraph("// COUNSEL FOR PETITIONER /ACCUSED //", align = "right", bold = true, fontSize = 22, spaceAfter = 300))

        // Page Break for Docket
        sb.append("""  <w:p><w:r><w:br w:type="page"/></w:r></w:p>
""")

        // Docket Section
        sb.append(xmlParagraph("ABSENT PETITION", align = "center", bold = true, fontSize = 24, spaceAfter = 120))
        sb.append(xmlParagraph(d.courtHeading, align = "center", bold = true, fontSize = 22, spaceAfter = 100))
        sb.append(xmlTabbedParagraph("Crl.M.P.No.", "/${d.filingYear}", align = "center", bold = true, fontSize = 22, spaceAfter = 60))
        sb.append(xmlParagraph("IN", align = "center", bold = true, fontSize = 22, spaceAfter = 60))
        sb.append(xmlParagraph(d.mainCaseNo, align = "center", bold = true, fontSize = 22, spaceAfter = 140))

        sb.append(xmlParagraph("Between:", align = "left", bold = true, fontSize = 20, spaceAfter = 40))
        d.accusedList.forEach { a ->
            sb.append(xmlParagraph("${a.name} (${a.rank})", align = "left", fontSize = 20, spaceAfter = 30))
        }
        sb.append(xmlParagraph("…${d.petitionerLabel}", align = "right", bold = true, fontSize = 20, spaceAfter = 60))

        sb.append(xmlParagraph("Versus:", align = "left", bold = true, fontSize = 20, spaceAfter = 30))
        sb.append(xmlParagraph(d.respondentName, align = "left", fontSize = 20, spaceAfter = 30))
        sb.append(xmlParagraph("…Respondent / Complainant", align = "right", bold = true, fontSize = 20, spaceAfter = 120))

        sb.append(xmlParagraph("PETITION FILED UNDER ${d.legalProvision}", align = "center", bold = true, fontSize = 22, spaceAfter = 140))

        sb.append(xmlParagraph("Filed on: ${d.filingDate}\nFiled By: Counsel for Accused", align = "left", fontSize = 20, spaceAfter = 80))
        sb.append(xmlParagraph("ADDRESS FOR SERVICE", align = "right", bold = true, fontSize = 20, spaceAfter = 40))
        d.advocateAddress.split("\n").forEach { line ->
            sb.append(xmlParagraph(line, align = "right", fontSize = 20, spaceAfter = 20))
        }

        // Section properties with standard 1-inch margins
        sb.append("""  <w:sectPr>
    <w:pgSz w:w="11906" w:h="16838"/>
    <w:pgMar w:top="1440" w:right="1440" w:bottom="1440" w:left="1440" w:header="720" w:footer="720" w:gutter="0"/>
  </w:sectPr>
  </w:body>
</w:document>""")

        return sb.toString()
    }

    private fun xmlParagraph(
        text: String,
        align: String = "left",
        bold: Boolean = false,
        underline: Boolean = false,
        fontSize: Int = 22,
        spaceBefore: Int = 0,
        spaceAfter: Int = 100,
        indentFirstLine: Int = 0
    ): String {
        val escaped = text
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")

        val lines = escaped.split("\n")

        val pPr = StringBuilder()
        pPr.append("<w:pPr>")
        pPr.append("<w:jc w:val=\"$align\"/>")
        if (spaceBefore > 0 || spaceAfter > 0) {
            pPr.append("<w:spacing w:before=\"$spaceBefore\" w:after=\"$spaceAfter\"/>")
        }
        if (indentFirstLine > 0) {
            pPr.append("<w:ind w:firstLine=\"$indentFirstLine\"/>")
        }
        pPr.append("<w:rPr>")
        pPr.append("<w:rFonts w:ascii=\"Times New Roman\" w:hAnsi=\"Times New Roman\"/>")
        if (bold) pPr.append("<w:b/>")
        if (underline) pPr.append("<w:u w:val=\"single\"/>")
        pPr.append("<w:sz w:val=\"$fontSize\"/>")
        pPr.append("</w:rPr>")
        pPr.append("</w:pPr>")

        val runs = StringBuilder()
        lines.forEachIndexed { index, line ->
            runs.append("<w:r>")
            runs.append("<w:rPr>")
            runs.append("<w:rFonts w:ascii=\"Times New Roman\" w:hAnsi=\"Times New Roman\"/>")
            if (bold) runs.append("<w:b/>")
            if (underline) runs.append("<w:u w:val=\"single\"/>")
            runs.append("<w:sz w:val=\"$fontSize\"/>")
            runs.append("</w:rPr>")
            runs.append("<w:t xml:space=\"preserve\">$line</w:t>")
            runs.append("</w:r>")
            if (index < lines.size - 1) {
                runs.append("<w:r><w:br/></w:r>")
            }
        }

        return "  <w:p>$pPr$runs</w:p>\n"
    }

    private fun xmlTabbedParagraph(
        prefix: String,
        suffix: String,
        align: String = "center",
        bold: Boolean = true,
        fontSize: Int = 24,
        spaceAfter: Int = 80
    ): String {
        return """  <w:p>
    <w:pPr>
      <w:jc w:val="$align"/>
      <w:spacing w:after="$spaceAfter"/>
      <w:rPr>
        <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
        <w:b/>
        <w:sz w:val="$fontSize"/>
      </w:rPr>
    </w:pPr>
    <w:r>
      <w:rPr>
        <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
        <w:b/>
        <w:sz w:val="$fontSize"/>
      </w:rPr>
      <w:t>$prefix</w:t>
      <w:tab/><w:tab/>
      <w:t>$suffix</w:t>
    </w:r>
  </w:p>
"""
    }

    fun saveDocxToCache(context: Context, data: PetitionData): File {
        val docsDir = File(context.cacheDir, "documents")
        if (!docsDir.exists()) docsDir.mkdirs()

        val safeCase = data.mainCaseNo.replace("[^a-zA-Z0-9]".toRegex(), "_").ifBlank { "Petition" }
        val file = File(docsDir, "Absent_Petition_${safeCase}.docx")
        FileOutputStream(file).use { fos ->
            fos.write(generateDocxBytes(data))
        }
        return file
    }

    fun getFileUri(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }

    fun shareDocx(context: Context, data: PetitionData) {
        try {
            val file = saveDocxToCache(context, data)
            val uri = getFileUri(context, file)

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "Absent Petition - ${data.mainCaseNo}")
                putExtra(Intent.EXTRA_TEXT, "Kindly find attached the Absent Petition for ${data.mainCaseNo}.")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(shareIntent, "Share Absent Petition DOCX"))
        } catch (e: Exception) {
            Toast.makeText(context, "Error sharing DOCX: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun shareViaWhatsApp(context: Context, data: PetitionData) {
        try {
            val text = data.toPlainText()
            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                setPackage("com.whatsapp")
                putExtra(Intent.EXTRA_TEXT, text)
            }
            context.startActivity(sendIntent)
        } catch (e: Exception) {
            // Fallback to any messaging app or web
            try {
                val genericIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, data.toPlainText())
                }
                context.startActivity(Intent.createChooser(genericIntent, "Share via WhatsApp / Messenger"))
            } catch (ex: Exception) {
                Toast.makeText(context, "Unable to launch messaging app", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun shareViaGmail(context: Context, data: PetitionData) {
        try {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                setData(Uri.parse("mailto:"))
                putExtra(Intent.EXTRA_SUBJECT, "Absent Petition - ${data.mainCaseNo} - ${data.accusedList.joinToString { it.rank }}")
                putExtra(Intent.EXTRA_TEXT, data.toPlainText())
            }
            context.startActivity(Intent.createChooser(emailIntent, "Send via Gmail / Email"))
        } catch (e: Exception) {
            Toast.makeText(context, "Error opening email client", Toast.LENGTH_SHORT).show()
        }
    }

    fun printOrSavePdf(context: Context, data: PetitionData) {
        try {
            val webView = WebView(context)
            val html = data.toHtmlDocument()

            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    val printManager = context.getSystemService(Context.PRINT_SERVICE) as? PrintManager
                    if (printManager != null) {
                        val jobName = "Absent_Petition_${data.mainCaseNo.replace(" ", "_")}"
                        val printAdapter = webView.createPrintDocumentAdapter(jobName)
                        val printAttributes = PrintAttributes.Builder()
                            .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                            .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                            .build()
                        printManager.print(jobName, printAdapter, printAttributes)
                    } else {
                        Toast.makeText(context, "Print service not available", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
        } catch (e: Exception) {
            Toast.makeText(context, "Printing error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
