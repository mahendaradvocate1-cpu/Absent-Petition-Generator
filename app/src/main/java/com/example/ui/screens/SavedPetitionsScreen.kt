package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PetitionData
import com.example.ui.AppScreen
import com.example.ui.PetitionViewModel
import com.example.ui.theme.PolishBorder
import com.example.ui.theme.PolishPurpleContainer
import com.example.ui.theme.PolishPurpleDark
import com.example.ui.theme.PolishPurplePrimary
import com.example.ui.theme.PolishSurfaceVariant
import com.example.ui.theme.PolishTextPrimary
import com.example.ui.theme.PolishTextSecondary

@Composable
fun SavedPetitionsScreen(
    petitions: List<PetitionData>,
    viewModel: PetitionViewModel,
    modifier: Modifier = Modifier
) {
    if (petitions.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(PolishSurfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        tint = PolishPurplePrimary,
                        modifier = Modifier.size(40.dp)
                    )
                }
                Text(
                    text = "No Saved Petitions Yet",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = PolishPurpleDark
                )
                Text(
                    text = "Petitions you generate will automatically be saved here for easy access, re-printing, and sharing.",
                    fontSize = 13.sp,
                    color = PolishTextSecondary,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { viewModel.navigateTo(AppScreen.FORM) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PolishPurplePrimary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.height(48.dp)
                ) {
                    Text("Draft New Petition", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(PolishSurfaceVariant)
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SAVED PETITIONS (${petitions.size})",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = PolishPurpleDark,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Button(
                        onClick = { viewModel.navigateTo(AppScreen.FORM) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PolishPurpleContainer,
                            contentColor = PolishPurpleDark
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Draft New", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            items(petitions, key = { it.id }) { item ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("saved_petition_item_${item.id}")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = item.mainCaseNo,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = PolishPurpleDark,
                                modifier = Modifier.weight(1f)
                            )

                            IconButton(
                                onClick = { viewModel.deleteSavedPetition(item.id) },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = "Delete",
                                    tint = Color(0xFFEF4444),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Text(
                            text = item.courtHeading,
                            fontSize = 12.sp,
                            color = PolishTextSecondary,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = "Accused: ${item.accusedList.joinToString { "${it.rank} (${it.name})" }}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = PolishTextPrimary,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Date: ${item.filingDate} • ${item.district}",
                                fontSize = 11.5.sp,
                                color = PolishTextSecondary
                            )

                            Button(
                                onClick = { viewModel.loadPetition(item) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PolishPurplePrimary,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FolderOpen,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Open Sheet", fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }
    }
}
