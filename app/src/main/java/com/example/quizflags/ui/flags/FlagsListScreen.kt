package com.example.quizflags.ui.flags

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.quizflags.R
import com.example.quizflags.data.local.entity.FlagEntity
import com.example.quizflags.ui.theme.QuizFlagsTheme

// Экран со списком всех флагов (галерея).
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlagsListScreen(
    vm: FlagsViewModel,
    onBack: () -> Unit
) {
    val flags by vm.flagsFlow.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.flags_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
            )
        },
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(padding),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp),
        ) {
            items(flags, key = { it.id }) { flag ->
                FlagCell(flag)
            }
        }
    }
}

@Composable
private fun FlagCell(flag: FlagEntity) {
    val fallbackPainter = rememberVectorPainter(Icons.Default.LocationOn)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AsyncImage(
            model = "file:///android_asset/flags/${flag.imageName}.png",
            contentDescription = flag.name,
            fallback = fallbackPainter,
            error = fallbackPainter,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.5f),
        )
        Text(
            text = flag.name,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FlagsListScreenPreview() {
    QuizFlagsTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            FlagCell(FlagEntity(id = 1, name = "Россия", imageName = "ru"))
            FlagCell(FlagEntity(id = 2, name = "США", imageName = "us"))
        }
    }
}