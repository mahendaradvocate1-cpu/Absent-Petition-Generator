package com.example.model

import java.util.UUID

data class AccusedPerson(
    val id: String = UUID.randomUUID().toString(),
    val rank: String = "A1",
    val name: String = "",
    val reason: String = "suffering from ill health"
)

enum class LegalProvision(val title: String, val actLabel: String) {
    BNSS_355("SECTION 355 OF B.N.S.S", "Section 355 of Bharatiya Nagarik Suraksha Sanhita, 2023"),
    CRPC_317("SECTION 317 OF Cr.P.C", "Section 317 of Code of Criminal Procedure, 1973")
}

data class PetitionData(
    val id: String = UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val state: String = "Telangana",
    val district: String = "Adilabad",
    val courtHeading: String = "IN THE COURT OF THE HON’BLE FASTRACK SPECIAL JUDGE FOR TRIAL OF POCSO CASES, AT: ADILABAD",
    val isCustomCourt: Boolean = false,
    val mainCaseNo: String = "S.C. POCSO No. 13/2025",
    val filingYear: Int = 2026,
    val legalProvision: String = "SECTION 355 OF B.N.S.S",
    val hearingPurpose: String = "his appearance and for Examination",
    val respondentName: String = "State of T.S., through SHO PS Jainath",
    val accusedList: List<AccusedPerson> = listOf(
        AccusedPerson(rank = "A1", name = "Ravi Rakade", reason = "suffering from ill health"),
        AccusedPerson(rank = "A2", name = "Vinod Borkar", reason = "suffering from ill health")
    ),
    val placeName: String = "Adilabad",
    val filingDate: String = "",
    val advocateAddress: String = "MAHENDAR B, ADVOCATE\nH.No. 9-46/5, Lane No.7, Teachers Colony, Adilabad\nCell No: 8501002211"
) {
    val isMultiAccused: Boolean get() = accusedList.size > 1
    val petitionerLabel: String get() = if (isMultiAccused) "Petitioners / Accused" else "Petitioner / Accused"
    val petitionerRef: String get() = if (isMultiAccused) "Petitioners" else "Petitioner"
    val pronounRef: String get() = if (isMultiAccused) "their" else "his"
    val beVerb: String get() = if (isMultiAccused) "are" else "is"

    val crlMpDisplay: String get() = "Crl.M.P.No.                /$filingYear"
    val crlMpTabbed: String get() = "Crl.M.P.No.\t\t/$filingYear"

    fun getReasonSummary(): String {
        return if (accusedList.isEmpty()) {
            "That the Petitioner is unable to attend before the Hon’ble Court on today as he is suffering from ill health."
        } else if (accusedList.size == 1) {
            val single = accusedList[0]
            "That the Petitioner is unable to attend before the Hon’ble Court on today as he is ${single.reason.ifBlank { "suffering from ill health" }}."
        } else {
            val itemized = accusedList.joinToString(separator = "; and ") {
                "${it.rank.ifBlank { "Accused" }} (${it.name.ifBlank { "Name Unspecified" }}) is ${it.reason.ifBlank { "suffering from ill health" }}"
            }
            "That the Petitioners are unable to attend before the Hon’ble Court on today due to bonafide reasons: $itemized."
        }
    }

    fun toPlainText(): String {
        val accusedText = accusedList.joinToString("\n") { "${it.name} (${it.rank})" }
        val advText = advocateAddress

        return """
ABSENT PETITION

$courtHeading
$crlMpTabbed
IN
$mainCaseNo

Between:
$accusedText
…$petitionerLabel
// VERSUS //
$respondentName
…Respondent / Complainant

PETITION FILED UNDER $legalProvision

May it please your Honour:
1. That the $petitionerRef $beVerb Accused in the above said case and same is posted for $pronounRef $hearingPurpose.
2. ${getReasonSummary()}
3. That the absence of the $petitionerRef before the Hon’ble Court on today is neither intentional nor wanton, but due to above said bonafide reason only.

P R A Y E R
Therefore, it is prayed that the Hon’ble Court may kindly be pleased to allow this Absent Petition and dispense with the personal attendance of the $petitionerRef on today before the Hon’ble Court in the interest of justice.

Place: $placeName.
Date: $filingDate
// COUNSEL FOR PETITIONER /ACCUSED //

--------------------------------------------------
DOCKET (BACK FILING SHEET)
--------------------------------------------------
ABSENT PETITION
$courtHeading
$crlMpTabbed
IN
$mainCaseNo

Between:
$accusedText
…$petitionerLabel
Versus:
$respondentName
…Respondent / Complainant

PETITION FILED UNDER $legalProvision

Filed on: $filingDate
Filed By: Counsel for Accused

ADDRESS FOR SERVICE:
$advText
""".trimIndent()
    }

    fun toHtmlDocument(): String {
        val accusedLinesHtml = accusedList.joinToString("") {
            "<div>${escapeHtml(it.name)} (${escapeHtml(it.rank)})</div>"
        }
        val advLinesHtml = escapeHtml(advocateAddress).replace("\n", "<br/>")

        return """
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Absent Petition - $mainCaseNo</title>
<style>
  @page {
    size: A4 portrait;
    margin: 25mm 20mm 20mm 20mm;
  }
  body {
    font-family: 'Times New Roman', Times, serif;
    font-size: 14pt;
    line-height: 1.55;
    color: #000;
    margin: 0;
    padding: 0;
    background: #fff;
  }
  .heading {
    text-align: center;
    font-weight: bold;
    font-size: 16pt;
    text-decoration: underline;
    margin-bottom: 12pt;
    letter-spacing: 1px;
  }
  .court-title {
    text-align: center;
    font-weight: bold;
    font-size: 13pt;
    margin-bottom: 10pt;
    text-transform: uppercase;
  }
  .case-no {
    text-align: center;
    font-weight: bold;
    margin: 4pt 0;
  }
  .parties-table {
    width: 100%;
    margin-top: 14pt;
    margin-bottom: 14pt;
    border-collapse: collapse;
  }
  .parties-table td {
    vertical-align: top;
    font-size: 13.5pt;
  }
  .provision {
    text-align: center;
    font-weight: bold;
    text-decoration: underline;
    text-transform: uppercase;
    margin: 16pt 0 12pt 0;
    font-size: 13.5pt;
  }
  .salutation {
    font-weight: bold;
    margin-bottom: 10pt;
  }
  .para {
    text-align: justify;
    margin-bottom: 10pt;
    display: flex;
  }
  .para-num {
    min-width: 26pt;
    font-weight: bold;
  }
  .para-body {
    flex: 1;
    text-align: justify;
  }
  .prayer-head {
    text-align: center;
    font-weight: bold;
    letter-spacing: 4px;
    margin: 18pt 0 10pt 0;
  }
  .prayer-body {
    text-align: justify;
    text-indent: 30pt;
    margin-bottom: 24pt;
  }
  .signatures-table {
    width: 100%;
    margin-top: 20pt;
  }
  .docket-section {
    page-break-before: always;
    border-top: 2px dashed #666;
    padding-top: 20pt;
    margin-top: 24pt;
  }
</style>
</head>
<body>
  <div class="heading">ABSENT PETITION</div>
  <div class="court-title">${escapeHtml(courtHeading)}</div>
  <div class="case-no">${escapeHtml(crlMpDisplay)}</div>
  <div class="case-no">IN</div>
  <div class="case-no">${escapeHtml(mainCaseNo)}</div>

  <table class="parties-table">
    <tr>
      <td style="width: 44%;">
        <strong>Between:</strong><br/>
        $accusedLinesHtml
        <strong>…$petitionerLabel</strong>
      </td>
      <td style="width: 12%; text-align: center; vertical-align: middle;">
        <strong>// VERSUS //</strong>
      </td>
      <td style="width: 44%; text-align: right;">
        ${escapeHtml(respondentName)}<br/>
        <strong>…Respondent / Complainant</strong>
      </td>
    </tr>
  </table>

  <div class="provision">PETITION FILED UNDER ${escapeHtml(legalProvision)}</div>

  <div class="salutation">May it please your Honour:</div>

  <div class="para">
    <div class="para-num">1.</div>
    <div class="para-body">That the $petitionerRef $beVerb Accused in the above said case and same is posted for $pronounRef ${escapeHtml(hearingPurpose)}.</div>
  </div>

  <div class="para">
    <div class="para-num">2.</div>
    <div class="para-body">${escapeHtml(getReasonSummary())}</div>
  </div>

  <div class="para">
    <div class="para-num">3.</div>
    <div class="para-body">That the absence of the $petitionerRef before the Hon’ble Court on today is neither intentional nor wanton, but due to above said bonafide reason only.</div>
  </div>

  <div class="prayer-head">P R A Y E R</div>
  <div class="prayer-body">
    Therefore, it is prayed that <strong>the Hon’ble Court may kindly be pleased to allow this Absent Petition and dispense with the personal attendance of the $petitionerRef on today before the Hon’ble Court in the interest of justice.</strong>
  </div>

  <table class="signatures-table">
    <tr>
      <td>
        Place: ${escapeHtml(placeName)}.<br/>
        Date: ${escapeHtml(filingDate)}
      </td>
      <td style="text-align: right;">
        <strong>// COUNSEL FOR PETITIONER /ACCUSED //</strong>
      </td>
    </tr>
  </table>

  <div class="docket-section">
    <div class="heading" style="font-size: 14pt; margin-bottom: 8pt;">ABSENT PETITION</div>
    <div class="court-title" style="font-size: 12pt;">${escapeHtml(courtHeading)}</div>
    <div class="case-no" style="font-size: 12pt;">${escapeHtml(crlMpDisplay)}</div>
    <div class="case-no" style="font-size: 12pt;">IN</div>
    <div class="case-no" style="font-size: 12pt;">${escapeHtml(mainCaseNo)}</div>

    <table class="parties-table" style="font-size: 12pt;">
      <tr>
        <td style="width: 48%;">
          <strong>Between:</strong><br/>
          $accusedLinesHtml
          <strong>…$petitionerLabel</strong>
        </td>
        <td style="width: 48%; text-align: right;">
          <strong>Versus:</strong><br/>
          ${escapeHtml(respondentName)}<br/>
          <strong>…Respondent / Complainant</strong>
        </td>
      </tr>
    </table>

    <div class="provision" style="font-size: 12pt;">PETITION FILED UNDER ${escapeHtml(legalProvision)}</div>

    <table style="width: 100%; border-top: 1px solid #000; padding-top: 10pt; font-size: 12pt;">
      <tr>
        <td style="vertical-align: top;">
          <strong>Filed on:</strong> ${escapeHtml(filingDate)}<br/>
          <strong>Filed By:</strong> Counsel for Accused
        </td>
        <td style="text-align: right; vertical-align: top;">
          <strong>ADDRESS FOR SERVICE</strong><br/>
          $advLinesHtml
        </td>
      </tr>
    </table>
  </div>
</body>
</html>
""".trimIndent()
    }

    private fun escapeHtml(text: String): String {
        return text
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;")
    }
}
