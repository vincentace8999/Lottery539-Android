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
import androidx.compose.ui.graphics.Brush
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
private val Navy = Color(0xFF17324D)

@Composable
fun LotteryTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(primary = Orange, secondary = Color(0xFF2A9D8F), background = Cream),
        content = content
    )
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
                Card(shape = RoundedCornerShape(24.dp)) {
                    Row(
                        Modifier.fillMaxWidth().background(
                            Brush.horizontalGradient(listOf(Navy, Color(0xFF285B73)))
                        ).padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text("今彩 539 智慧分析", color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
                            Text("台彩最近 20 期資料", color = Color(0xFFD6EAF2))
                        }
                        FilledTonalButton(onClick = vm::refresh, enabled = !state.loading) { Text("更新資料") }
                    }
                }
            }
            if (state.loading) item { LinearProgressIndicator(Modifier.fillMaxWidth()) }
            state.message?.let { message -> item { Text(message, color = MaterialTheme.colorScheme.error) } }
            state.analysis?.let { analysis ->
                item { SuggestedCard("本期冷熱參考", "熱門與冷門號碼交叉選取", analysis.suggested, Orange) }
                item {
                    SuggestedCard(
                        "上期拖尾參考",
                        "依上期 ${analysis.previousNumbers.joinToString("、") { it.toString().padStart(2, '0') }} 的尾數延伸",
                        analysis.tailSuggested,
                        Color(0xFF7B2CBF)
                    )
                }
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
private fun SuggestedCard(title: String, subtitle: String, numbers: List<Int>, accent: Color) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(5.dp, 38.dp).background(accent, RoundedCornerShape(6.dp)))
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            }
            NumberRow(numbers, accent)
        }
    }
}

@Composable
private fun FrequencyCard(title: String, entries: List<Pair<Int, Int>>, color: Color) {
    Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
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
    Card(shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
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
