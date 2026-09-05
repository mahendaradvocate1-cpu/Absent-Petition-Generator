package com.example.ui.screens

import android.app.DatePickerDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CourtData
import com.example.model.LegalProvision
import com.example.ui.PetitionViewModel
import com.example.ui.theme.PolishBackground
import com.example.ui.theme.PolishBorder
import com.example.ui.theme.PolishPurpleContainer
import com.example.ui.theme.PolishPurpleDark
import com.example.ui.theme.PolishPurpleLight
import com.example.ui.theme.PolishPurplePrimary
import com.example.ui.theme.PolishSurfaceVariant
import com.example.ui.theme.PolishTextPrimary
import com.example.ui.theme.PolishTextSecondary
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PetitionFormScreen(
    viewModel: PetitionViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val selectedState by viewModel.selectedState.collectAsState()
    val selectedDistrict by viewModel.selectedDistrict.collectAsState()
    val availableCourts by viewModel.availableCourts.collectAsState()
    val selectedCourt by viewModel.selectedCourt.collectAsState()
    val isCustomCourt by viewModel.isCustomCourt.collectAsState()
    val customCourtText by viewModel.customCourtText.collectAsState()
    val mainCaseNo by viewModel.mainCaseNo.collectAsState()
    val legalProvision by viewModel.legalProvision.collectAsState()
    val hearingPurpose by viewModel.hearingPurpose.collectAsState()
    val respondentName by viewModel.respondentName.collectAsState()
    val accusedList by viewModel.accusedList.collectAsState()
    val placeName by viewModel.placeName.collectAsState()
    val filingDate by viewModel.filingDate.collectAsState()
    val advocateAddress by viewModel.advocateAddress.collectAsState()

    var stateExpanded by remember { mutableStateOf(false) }
    var districtExpanded by remember { mutableStateOf(false) }
    var courtExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section 1: Court & Case Information (Professional Polish rounded-3xl container)
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Section Title Banner (M3 Soft Pill style)
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(PolishSurfaceVariant)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Gavel,
                        contentDescription = null,
                        tint = PolishPurplePrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "CASE JURISDICTION & DETAILS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = PolishPurpleDark,
                        letterSpacing = 0.5.sp
                    )
                }

                // State & District Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // State Selector
                    ExposedDropdownMenuBox(
                        expanded = stateExpanded,
                        onExpandedChange = { stateExpanded = !stateExpanded },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = selectedState,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("State") },
                            shape = RoundedCornerShape(12.dp),
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = stateExpanded) },
                            modifier = Modifier
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                .fillMaxWidth()
                                .testTag("state_dropdown")
                        )
                        ExposedDropdownMenu(
                            expanded = stateExpanded,
                            onDismissRequest = { stateExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Telangana") },
                                onClick = {
                                    stateExpanded = false
                                }
                            )
                        }
                    }

                    // District Selector
                    ExposedDropdownMenuBox(
                        expanded = districtExpanded,
                        onExpandedChange = { districtExpanded = !districtExpanded },
                        modifier = Modifier.weight(1.3f)
                    ) {
                        OutlinedTextField(
                            value = selectedDistrict,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("District") },
                            shape = RoundedCornerShape(12.dp),
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = districtExpanded) },
                            modifier = Modifier
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                .fillMaxWidth()
                                .testTag("district_dropdown")
                        )
                        ExposedDropdownMenu(
                            expanded = districtExpanded,
                            onDismissRequest = { districtExpanded = false }
                        ) {
                            CourtData.districts.forEach { dist ->
                                DropdownMenuItem(
                                    text = { Text(dist) },
                                    onClick = {
                                        viewModel.setDistrict(dist)
                                        districtExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Court Selector
                ExposedDropdownMenuBox(
                    expanded = courtExpanded,
                    onExpandedChange = { courtExpanded = !courtExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val displayText = if (isCustomCourt) {
                        "✍️ Custom / Other Court (Type manually)"
                    } else {
                        selectedCourt
                    }

                    OutlinedTextField(
                        value = displayText,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Court Name (from selected District)") },
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = courtExpanded) },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth()
                            .testTag("court_dropdown")
                    )
                    ExposedDropdownMenu(
                        expanded = courtExpanded,
                        onDismissRequest = { courtExpanded = false }
                    ) {
                        availableCourts.forEach { court ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = court,
                                        maxLines = 2,
                                        fontSize = 13.sp
                                    )
                                },
                                onClick = {
                                    viewModel.setCourt(court)
                                    courtExpanded = false
                                }
                            )
                        }
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "✍️ Custom / Other Court (Type manually)",
                                    fontWeight = FontWeight.Bold,
                                    color = PolishPurplePrimary
                                )
                            },
                            onClick = {
                                viewModel.setCourt("CUSTOM")
                                courtExpanded = false
                            }
                        )
                    }
                }

                // Custom Court Input Field
                AnimatedVisibility(visible = isCustomCourt) {
                    OutlinedTextField(
                        value = customCourtText,
                        onValueChange = { viewModel.setCustomCourtText(it) },
                        label = { Text("Enter Custom Court Name") },
                        placeholder = { Text("e.g. IN THE COURT OF THE HON'BLE...") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("custom_court_input")
                    )
                }

                // Main Case No. & Year
                OutlinedTextField(
                    value = mainCaseNo,
                    onValueChange = { viewModel.setMainCaseNo(it) },
                    label = { Text("Main Case No. & Year") },
                    placeholder = { Text("e.g. S.C. POCSO No. 13/2025") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("main_case_no_input")
                )

                // Governing Legal Provision
                Text(
                    text = "Governing Provision:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PolishTextSecondary
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    LegalProvision.entries.forEach { provision ->
                        val isSelected = legalProvision == provision.title
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setLegalProvision(provision.title) },
                            shape = RoundedCornerShape(20.dp),
                            label = {
                                Text(
                                    text = if (provision == LegalProvision.BNSS_355) "Sec. 355 BNSS" else "Sec. 317 Cr.P.C",
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PolishPurpleContainer,
                                selectedLabelColor = PolishPurpleDark,
                                containerColor = PolishSurfaceVariant,
                                labelColor = PolishTextSecondary
                            ),
                            border = null,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("provision_chip_${provision.name}")
                        )
                    }
                }

                // Purpose of Posting Today
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    OutlinedTextField(
                        value = hearingPurpose,
                        onValueChange = { viewModel.setHearingPurpose(it) },
                        label = { Text("Purpose of Posting Today") },
                        placeholder = { Text("his appearance and for Examination") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("hearing_purpose_input")
                    )

                    // Quick Chips for Hearing Purpose
                    Text(
                        text = "Quick suggestions:",
                        fontSize = 11.sp,
                        color = PolishTextSecondary
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        CourtData.commonPurposes.take(4).forEach { purpose ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(PolishSurfaceVariant)
                                    .clickable { viewModel.setHearingPurpose(purpose) }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = purpose,
                                    fontSize = 11.sp,
                                    color = PolishTextPrimary
                                )
                            }
                        }
                    }
                }

                // Complainant / Respondent / State
                OutlinedTextField(
                    value = respondentName,
                    onValueChange = { viewModel.setRespondentName(it) },
                    label = { Text("Complainant / Respondent / State") },
                    placeholder = { Text("e.g. State of T.S., through SHO PS Jainath") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("respondent_name_input")
                )
            }
        }

        // Section 2: Accused Person(s) Details
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(PolishSurfaceVariant)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = PolishPurplePrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "ACCUSED PERSON(S) DETAILS (${accusedList.size})",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = PolishPurpleDark,
                        letterSpacing = 0.5.sp
                    )
                }

                // Accused Item Cards
                accusedList.forEachIndexed { index, accused ->
                    AccusedCard(
                        index = index + 1,
                        accused = accused,
                        canDelete = accusedList.size > 1,
                        onRankChange = { viewModel.updateAccusedRank(accused.id, it) },
                        onNameChange = { viewModel.updateAccusedName(accused.id, it) },
                        onReasonChange = { viewModel.updateAccusedReason(accused.id, it) },
                        onDelete = { viewModel.removeAccused(accused.id) }
                    )
                }

                // Add Another Accused Button (Rounded Polish Container style)
                OutlinedButton(
                    onClick = { viewModel.addAccused() },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = PolishPurplePrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_add_accused")
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Add Another Accused",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Section 3: Filing & Advocate Information
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(PolishSurfaceVariant)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Work,
                        contentDescription = null,
                        tint = PolishPurplePrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "FILING & ADVOCATE INFORMATION",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = PolishPurpleDark,
                        letterSpacing = 0.5.sp
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Place of Filing
                    OutlinedTextField(
                        value = placeName,
                        onValueChange = { viewModel.setPlaceName(it) },
                        label = { Text("Place of Filing") },
                        shape = RoundedCornerShape(12.dp),
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.LocationOn, contentDescription = null)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("place_name_input")
                    )

                    // Filing Date (with DatePickerDialog)
                    OutlinedTextField(
                        value = filingDate,
                        onValueChange = { viewModel.setFilingDate(it) },
                        label = { Text("Filing Date") },
                        shape = RoundedCornerShape(12.dp),
                        leadingIcon = {
                            IconButton(onClick = {
                                val cal = Calendar.getInstance()
                                DatePickerDialog(
                                    context,
                                    { _, year, month, dayOfMonth ->
                                        val formatted = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                                        viewModel.setFilingDate(formatted)
                                    },
                                    cal.get(Calendar.YEAR),
                                    cal.get(Calendar.MONTH),
                                    cal.get(Calendar.DAY_OF_MONTH)
                                ).show()
                            }) {
                                Icon(imageVector = Icons.Default.CalendarToday, contentDescription = "Pick Date")
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("filing_date_input")
                    )
                }

                // Advocate Name & Service Address
                OutlinedTextField(
                    value = advocateAddress,
                    onValueChange = { viewModel.setAdvocateAddress(it) },
                    label = { Text("Advocate Name & Service Address") },
                    shape = RoundedCornerShape(12.dp),
                    minLines = 3,
                    maxLines = 5,
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("advocate_address_input")
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = { viewModel.saveAdvocateProfileAsDefault() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PolishSurfaceVariant,
                            contentColor = PolishPurpleDark
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Save as Default Profile",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // Generate Button (Professional Polish rounded-2xl CTA)
        Button(
            onClick = { viewModel.generatePetition() },
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PolishPurplePrimary,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("btn_generate_petition")
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Generate Absent Petition",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AccusedCard(
    index: Int,
    accused: com.example.model.AccusedPerson,
    canDelete: Boolean,
    onRankChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onReasonChange: (String) -> Unit,
    onDelete: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = PolishSurfaceVariant,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(PolishPurplePrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$index",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "Accused #$index",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = PolishTextPrimary
                    )
                }

                if (canDelete) {
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier
                            .size(28.dp)
                            .testTag("btn_delete_accused_$index")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Accused",
                            tint = Color(0xFFEF4444),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = accused.rank,
                    onValueChange = onRankChange,
                    label = { Text("Rank") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.width(110.dp)
                )

                OutlinedTextField(
                    value = accused.name,
                    onValueChange = onNameChange,
                    label = { Text("Accused Full Name") },
                    placeholder = { Text("Name of Accused") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                OutlinedTextField(
                    value = accused.reason,
                    onValueChange = onReasonChange,
                    label = { Text("Specific Reason for Absence") },
                    placeholder = { Text("e.g. suffering from ill health") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                // Quick Reason suggestions
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    CourtData.commonReasons.take(4).forEach { reason ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color.White)
                                .clickable { onReasonChange(reason) }
                                .padding(horizontal = 9.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = reason,
                                fontSize = 10.5.sp,
                                color = PolishTextPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}
