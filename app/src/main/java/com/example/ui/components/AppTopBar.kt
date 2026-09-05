package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen
import com.example.ui.theme.PolishBackground
import com.example.ui.theme.PolishBorder
import com.example.ui.theme.PolishPurpleContainer
import com.example.ui.theme.PolishPurpleDark
import com.example.ui.theme.PolishPurpleLight
import com.example.ui.theme.PolishPurplePrimary
import com.example.ui.theme.PolishSurfaceVariant
import com.example.ui.theme.PolishTextPrimary
import com.example.ui.theme.PolishTextSecondary

@Composable
fun AppTopBar(
    currentScreen: AppScreen,
    savedCount: Int,
    onNavigate: (AppScreen) -> Unit
) {
    Surface(
        color = PolishBackground,
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    // Circular icon container matching Professional Polish design (w-12 h-12 rounded-full bg-[#f3edf7])
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(PolishSurfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = "Legal Document Icon",
                            tint = PolishPurplePrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Absent Petition",
                            color = PolishTextPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                        Text(
                            text = "LegalScribe Court Draftsman",
                            color = PolishTextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Professional Polish Avatar / Status Badge (#6750A4 rounded-full border-2 border-white text-white font-bold)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(PolishPurpleContainer)
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "BNSS / CrPC",
                        color = PolishPurpleDark,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Navigation Tabs with Professional Polish Pill Styling
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = currentScreen == AppScreen.FORM,
                    onClick = { onNavigate(AppScreen.FORM) },
                    label = { Text("Draft Form", fontWeight = FontWeight.Medium) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = PolishPurpleContainer,
                        selectedLabelColor = PolishPurpleDark,
                        selectedLeadingIconColor = PolishPurpleDark,
                        containerColor = PolishSurfaceVariant,
                        labelColor = PolishTextSecondary,
                        iconColor = PolishTextSecondary
                    ),
                    border = null,
                    modifier = Modifier.testTag("tab_draft_form")
                )

                FilterChip(
                    selected = currentScreen == AppScreen.HISTORY,
                    onClick = { onNavigate(AppScreen.HISTORY) },
                    label = {
                        Text(
                            if (savedCount > 0) "Saved ($savedCount)" else "History",
                            fontWeight = FontWeight.Medium
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = PolishPurpleContainer,
                        selectedLabelColor = PolishPurpleDark,
                        selectedLeadingIconColor = PolishPurpleDark,
                        containerColor = PolishSurfaceVariant,
                        labelColor = PolishTextSecondary,
                        iconColor = PolishTextSecondary
                    ),
                    border = null,
                    modifier = Modifier.testTag("tab_saved_history")
                )

                if (currentScreen == AppScreen.PREVIEW) {
                    FilterChip(
                        selected = true,
                        onClick = { /* already in preview */ },
                        label = { Text("Court Sheet", fontWeight = FontWeight.SemiBold) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Description,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PolishPurplePrimary,
                            selectedLabelColor = Color.White,
                            selectedLeadingIconColor = Color.White
                        ),
                        border = null,
                        modifier = Modifier.testTag("tab_court_sheet")
                    )
                }
            }
        }
    }
}
