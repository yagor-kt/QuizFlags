package com.example.quizflags.ui.stats

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.quizflags.R
import com.example.quizflags.data.local.entity.StatEntity
import com.example.quizflags.ui.theme.QuizFlagsTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Экран истории игр пользователя.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    vm: StatsViewModel,
    onBack: () -> Unit
) {
    val stats by vm.statisticsFlow.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.stats_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
            )
        },
    ) { padding ->
        if (stats.isEmpty()) {
            Text(
                text = stringResource(R.string.stats_empty),
                modifier = Modifier.padding(padding).padding(16.dp),
            )
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(stats, key = { it.id }) { item ->
                    StatCard(item)
                }
            }
        }
    }
}

@Composable
private fun StatCard(item: StatEntity) {
    val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    val dateText = formatter.format(Date(item.dateTime))
    val reasonText = if (item.reason == "lives") {
        stringResource(R.string.reason_lives)
    } else {
        stringResource(R.string.reason_time)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
    ) {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(dateText)
            Text(stringResource(R.string.stats_score, item.score))
            Text(reasonText)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StatsScreenPreview() {
    QuizFlagsTheme {
        StatCard(
            StatEntity(
                id = 1,
                userName = "guest",
                score = 8,
                dateTime = System.currentTimeMillis(),
                reason = "time",
            ),
        )
    }
}