package com.aigal.pokedax.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.aigal.pokedax.R
import com.aigal.pokedax.ui.theme.PokedaxTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTopBar(
    title: String = stringResource(id = R.string.app_name),
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}

@Preview(showBackground = true)
@Composable
fun CommonTopBarPreview() {
    PokedaxTheme {
        CommonTopBar(title = "Pokedax", showBackButton = true)
    }
}
