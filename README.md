# 今彩539 Android 分析 App

以 Kotlin 與 Jetpack Compose 製作，自動讀取台灣彩券資料，統計最近 20 期熱門與冷門號碼，並產生一組娛樂性參考號碼。

## 功能

- 自動抓取本月與前兩個月的今彩539資料
- 最近20期冷、熱門號碼與出現次數
- 顯示最近20期開獎紀錄
- 依冷熱門候選池產生5個參考號碼
- 支援 Android 6.0（API 23）以上

## 執行

1. 使用 Android Studio 開啟本專案。
2. 等待 Gradle 同步完成。
3. 連接 Android 手機並開啟 USB 偵錯，或啟動模擬器。
4. 按 Run 執行。

## 從 GitHub 下載 APK

每次推送到 `main` 分支時，GitHub Actions 會自動測試並編譯 Debug APK。

1. 在 GitHub 專案頁面點選 **Actions**。
2. 開啟最新一次 **Build Android APK** 執行紀錄。
3. 在頁面下方 **Artifacts** 點選 **Lottery539-debug-apk** 下載。
4. 解壓縮後，將 `app-debug.apk` 安裝到 Android 手機。

也可以進入 **Actions → Build Android APK → Run workflow** 手動編譯。

> 歷史統計無法預測隨機開獎結果。本 App 僅供娛樂與資料參考；未滿18歲不得購買彩券。
