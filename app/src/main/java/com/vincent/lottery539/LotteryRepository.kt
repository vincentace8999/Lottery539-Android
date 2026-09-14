package com.vincent.lottery539

import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.Calendar

class LotteryRepository {
    fun fetchLatest(): List<Draw> {
        val calendar = Calendar.getInstance()
        return (0..2).flatMap {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1
            val result = fetchMonth(year, month)
            calendar.add(Calendar.MONTH, -1)
            result
        }
            .distinctBy { it.period }
            .sortedByDescending { it.period }
            .take(20)
    }

    private fun fetchMonth(year: Int, month: Int): List<Draw> {
        val address = "https://api.taiwanlottery.com/TLCAPIWeB/Lottery/Daily539Result" +
            "?period=&month=$year-${month.toString().padStart(2, '0')}&pageSize=31"
        val connection = URL(address).openConnection() as HttpURLConnection
        connection.connectTimeout = 12_000
        connection.readTimeout = 12_000
        connection.setRequestProperty("Accept", "application/json")
        connection.setRequestProperty("User-Agent", "Lottery539-Android/1.0")
        return try {
            if (connection.responseCode !in 200..299) error("資料服務回應 ${connection.responseCode}")
            val root = JSONObject(connection.inputStream.bufferedReader().use { it.readText() })
            val array = root.getJSONObject("content").getJSONArray("daily539Res")
            buildList {
                for (i in 0 until array.length()) {
                    val item = array.getJSONObject(i)
                    val raw = item.get("drawNumberSize")
                    val numbers = when (raw) {
                        is org.json.JSONArray -> (0 until raw.length()).map { raw.getInt(it) }
                        else -> Regex("\\d{1,2}").findAll(raw.toString()).map { it.value.toInt() }.toList()
                    }.filter { it in 1..39 }.take(5)
                    if (numbers.size == 5) add(
                        Draw(item.optString("period"), item.optString("lotteryDate").substringBefore('T'), numbers.sorted())
                    )
                }
            }
        } finally {
            connection.disconnect()
        }
    }
}
