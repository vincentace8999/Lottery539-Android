package com.vincent.lottery539

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { LotteryTheme { LotteryApp() } }
    }
}

private val Orange = Color(0xFFE85D04)
private val Cream = Color(0xFFFFF8F1)

@Composable
fun LotteryTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = lightColorScheme(primary = Orange, background = Cream), content = content)
}

@Composable
fun LotteryApp(vm: MainViewModel = viewModel()) {
    val state by vm.state.collectAsStateWithLifecycle()
    Scaffold(containerColor = Cream) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("今彩539分析", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                        Text("依台彩最近20期資料統計", color = Color.Gray)
                    }
                    Button(onClick = vm::refresh, enabled = !state.loading) { Text("更新") }
                }
            }
            if (state.loading) item { LinearProgressIndicator(Modifier.fillMaxWidth()) }
            state.message?.let { message -> item { Text(message, color = MaterialTheme.colorScheme.error) } }
            state.analysis?.let { analysis ->
                item { SuggestedCard(analysis.suggested) }
                item { FrequencyCard("熱門號碼", analysis.hot, Color(0xFFD62828)) }
                item { FrequencyCard("冷門號碼", analysis.cold, Color(0xFF457B9D)) }
                item { Text("最近開獎紀錄", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) }
            }
            items(state.draws) { DrawRow(it) }
            item {
                Text(
                    "提醒：歷史統計無法預測隨機開獎結果，號碼僅供娛樂參考。未滿18歲不得購買彩券。",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun SuggestedCard(numbers: List<Int>) {
    Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE6CC)), shape = RoundedCornerShape(20.dp)) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("本期參考組合", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            NumberRow(numbers, Orange)
        }
    }
}

@Composable
private fun FrequencyCard(title: String, entries: List<Pair<Int, Int>>, color: Color) {
    Card(shape = RoundedCornerShape(18.dp)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                entries.take(8).forEach { (number, count) ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Ball(number, color, 34)
                        Text("${count}次", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}

@Composable
private fun DrawRow(draw: Draw) {
    Card(shape = RoundedCornerShape(14.dp)) {
        Row(Modifier.fillMaxWidth().padding(13.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("第 ${draw.period} 期", fontWeight = FontWeight.SemiBold)
                Text(draw.date, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            NumberRow(draw.numbers, Color(0xFF2A9D8F), 32)
        }
    }
}

@Composable
private fun NumberRow(numbers: List<Int>, color: Color, size: Int = 46) {
    Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) { numbers.forEach { Ball(it, color, size) } }
}

@Composable
private fun Ball(number: Int, color: Color, size: Int) {
    Box(
        Modifier.size(size.dp).background(color, CircleShape),
        contentAlignment = Alignment.Center
    ) { Text(number.toString().padStart(2, '0'), color = Color.White, fontWeight = FontWeight.Bold) }
}
