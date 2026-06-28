package com.aigal.pokedax.ui.list.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aigal.pokedax.ui.theme.PokedaxTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortButton(
    currentSort: String,
    onSortSelected: (String) -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    IconButton(onClick = { showBottomSheet = true }) {
        Icon(Icons.AutoMirrored.Filled.Sort, contentDescription = "Sort")
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = Color.White,
            contentColor = Color.Black
        ) {
            SortBottomSheetContent(
                currentSort = currentSort,
                onSortSelected = {
                    onSortSelected(it)
                    showBottomSheet = false
                }
            )
        }
    }
}

@Composable
fun SortBottomSheetContent(
    currentSort: String,
    onSortSelected: (String) -> Unit
) {
    val sortOptions = listOf(
        "NumberAsc" to "Number (Ascending)",
        "NumberDesc" to "Number (Descending)",
        "NameAsc" to "Name (A-Z)",
        "NameDesc" to "Name (Z-A)"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp)
    ) {
        Text(
            text = "Sort By",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        sortOptions.forEach { (key, label) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSortSelected(key) }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (key == currentSort),
                    onClick = null // Row handles the click
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = label, fontSize = 16.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SortBottomSheetPreview() {
    PokedaxTheme {
        Surface {
            SortBottomSheetContent(currentSort = "NumberAsc", onSortSelected = {})
        }
    }
}
