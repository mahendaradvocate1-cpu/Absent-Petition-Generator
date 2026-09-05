package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.export.DocxExporter
import com.example.model.PetitionData
import com.example.ui.AppScreen
import com.example.ui.PetitionViewModel
import com.example.ui.theme.DocxBlue
import com.example.ui.theme.GmailRed
import com.example.ui.theme.PolishBorder
import com.example.ui.theme.PolishPurpleContainer
import com.example.ui.theme.PolishPurpleDark
import com.example.ui.theme.PolishPurplePrimary
import com.example.ui.theme.PolishSurfaceVariant
import com.example.ui.theme.PolishTextPrimary
import com.example.ui.theme.PolishTextSecondary
import com.example.ui.theme.PrintEmerald
import com.example.ui.theme.WhatsAppGreen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PetitionPreviewScreen(
    petition: PetitionData,
    viewModel: PetitionViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Action Bar (Professional Polish rounded-3xl container)
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(PolishSurfaceVariant)
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ACTIONS & EXPORT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = PolishPurpleDark,
                        letterSpacing = 0.5.sp
                    )
                }

                // Row of primary action buttons
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Back to Edit Form
                    Button(
                        onClick = { viewModel.navigateTo(AppScreen.FORM) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PolishPurpleContainer,
                            contentColor = PolishPurpleDark
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.testTag("btn_back_to_edit")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Edit Form", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }

                    // Download / Share DOCX
                    Button(
                        onClick = { DocxExporter.shareDocx(context, petition) },
                        colors = ButtonDefaults.buttonColors(containerColor = DocxBlue),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.testTag("btn_export_docx")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("DOCX File", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }

                    // Print / Save PDF
                    Button(
                        onClick = { DocxExporter.printOrSavePdf(context, petition) },
                        colors = ButtonDefaults.buttonColors(containerColor = PrintEmerald),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.testTag("btn_print_pdf")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Print,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Print / PDF", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }

                    // WhatsApp
                    Button(
                        onClick = { DocxExporter.shareViaWhatsApp(context, petition) },
                        colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreen),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.testTag("btn_whatsapp_share")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("WhatsApp", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }

                    // Gmail
                    Button(
                        onClick = { DocxExporter.shareViaGmail(context, petition) },
                        colors = ButtonDefaults.buttonColors(containerColor = GmailRed),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.testTag("btn_gmail_share")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Gmail", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }

                    // Copy Text
                    OutlinedButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Absent Petition", petition.toPlainText())
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Petition copied to clipboard", Toast.LENGTH_SHORT).show()
                        },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.testTag("btn_copy_petition")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Copy Text", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        // Court Document Sheet
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, PolishBorder, RoundedCornerShape(16.dp))
                .testTag("legal_document_card")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 28.dp)
            ) {
                // Main Heading: ABSENT PETITION
                Text(
                    text = "ABSENT PETITION",
                    fontFamily = FontFamily.Serif,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                    textAlign = TextAlign.Center,
                    letterSpacing = 2.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Court Name
                Text(
                    text = petition.courtHeading,
                    fontFamily = FontFamily.Serif,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Crl.M.P.No.                /Year
                Text(
                    text = petition.crlMpDisplay,
                    fontFamily = FontFamily.Serif,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "IN",
                    fontFamily = FontFamily.Serif,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = petition.mainCaseNo,
                    fontFamily = FontFamily.Serif,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Parties: Between / Versus
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Left: Accused list
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Between:",
                            fontFamily = FontFamily.Serif,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                        petition.accusedList.forEach { a ->
                            Text(
                                text = "${a.name} (${a.rank})",
                                fontFamily = FontFamily.Serif,
                                fontSize = 13.sp
                            )
                        }
                        Text(
                            text = "…${petition.petitionerLabel}",
                            fontFamily = FontFamily.Serif,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Center
                    Text(
                        text = "// VERSUS //",
                        fontFamily = FontFamily.Serif,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    // Right: Respondent
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = petition.respondentName,
                            fontFamily = FontFamily.Serif,
                            fontSize = 13.sp,
                            textAlign = TextAlign.End
                        )
                        Text(
                            text = "…Respondent / Complainant",
                            fontFamily = FontFamily.Serif,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Provision Heading
                Text(
                    text = "PETITION FILED UNDER ${petition.legalProvision}",
                    fontFamily = FontFamily.Serif,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "May it please your Honour:",
                    fontFamily = FontFamily.Serif,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Paragraph 1
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "1. ",
                        fontFamily = FontFamily.Serif,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "That the ${petition.petitionerRef} ${petition.beVerb} Accused in the above said case and same is posted for ${petition.pronounRef} ${petition.hearingPurpose}.",
                        fontFamily = FontFamily.Serif,
                        fontSize = 13.5.sp,
                        lineHeight = 20.sp,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Paragraph 2
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "2. ",
                        fontFamily = FontFamily.Serif,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = petition.getReasonSummary(),
                        fontFamily = FontFamily.Serif,
                        fontSize = 13.5.sp,
                        lineHeight = 20.sp,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Paragraph 3
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "3. ",
                        fontFamily = FontFamily.Serif,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "That the absence of the ${petition.petitionerRef} before the Hon’ble Court on today is neither intentional nor wanton, but due to above said bonafide reason only.",
                        fontFamily = FontFamily.Serif,
                        fontSize = 13.5.sp,
                        lineHeight = 20.sp,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Prayer Title
                Text(
                    text = "P R A Y E R",
                    fontFamily = FontFamily.Serif,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    letterSpacing = 4.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Prayer Body
                Text(
                    text = "        Therefore, it is prayed that the Hon’ble Court may kindly be pleased to allow this Absent Petition and dispense with the personal attendance of the ${petition.petitionerRef} on today before the Hon’ble Court in the interest of justice.",
                    fontFamily = FontFamily.Serif,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 21.sp,
                    textAlign = TextAlign.Justify
                )

                Spacer(modifier = Modifier.height(30.dp))

                // Signatures
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = "Place: ${petition.placeName}.",
                            fontFamily = FontFamily.Serif,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Date: ${petition.filingDate}",
                            fontFamily = FontFamily.Serif,
                            fontSize = 13.sp
                        )
                    }

                    Text(
                        text = "// COUNSEL FOR PETITIONER /ACCUSED //",
                        fontFamily = FontFamily.Serif,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Docket Section (Perforated cut divider)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .drawBehind {
                            drawLine(
                                color = Color(0xFF64748B),
                                start = Offset(0f, 0f),
                                end = Offset(size.width, 0f),
                                strokeWidth = 2f,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f)
                            )
                        }
                        .padding(top = 24.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "DOCKET (BACK FILING SHEET)",
                            fontFamily = FontFamily.Serif,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = PolishTextSecondary,
                            textAlign = TextAlign.Center,
                            letterSpacing = 1.sp,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "ABSENT PETITION",
                            fontFamily = FontFamily.Serif,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = petition.courtHeading,
                            fontFamily = FontFamily.Serif,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "${petition.crlMpDisplay}   IN   ${petition.mainCaseNo}",
                            fontFamily = FontFamily.Serif,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Between:",
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                petition.accusedList.forEach { a ->
                                    Text(
                                        text = "${a.name} (${a.rank})",
                                        fontFamily = FontFamily.Serif,
                                        fontSize = 11.5.sp
                                    )
                                }
                                Text(
                                    text = "…${petition.petitionerLabel}",
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Column(
                                horizontalAlignment = Alignment.End,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "Versus:",
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = petition.respondentName,
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 11.5.sp,
                                    textAlign = TextAlign.End
                                )
                                Text(
                                    text = "…Respondent / Complainant",
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.End
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "PETITION FILED UNDER ${petition.legalProvision}",
                            fontFamily = FontFamily.Serif,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(thickness = 1.dp, color = Color.Black)
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Filed on: ${petition.filingDate}",
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "Filed By: Counsel for Accused",
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "ADDRESS FOR SERVICE",
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                petition.advocateAddress.split("\n").forEach { line ->
                                    Text(
                                        text = line,
                                        fontFamily = FontFamily.Serif,
                                        fontSize = 11.sp,
                                        textAlign = TextAlign.End
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
