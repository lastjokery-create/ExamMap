package jp.maple.exammap.ui.theme

import androidx.compose.ui.graphics.Color

// ブランドカラー（オレンジ系）
val OrangeMain    = Color(0xFFFF8A00)  // メインオレンジ
val OrangeDark    = Color(0xFFE65100)  // 濃いオレンジ
val OrangeMedium  = Color(0xFFFF6D00)  // やや濃い
val OrangeLight   = Color(0xFFFFAB40)  // 明るいオレンジ
val OrangePale    = Color(0xFFFFD180)  // 薄いオレンジ

// テキスト・ベース
val ColorDark     = Color(0xFF111111)
val ColorWhite    = Color(0xFFFFFFFF)
val ColorGrayBg   = Color(0xFFF5F5F5)
val ColorBorder   = Color(0xFFE6E6E6)
val ColorGrayText = Color(0xFF888888)

// 試験カード 5色ローテーション（アクセントカラー）
val AccentColors = listOf(
    Color(0xFFFF8A00),  // 0: メインオレンジ
    Color(0xFFFF6D00),  // 1: やや濃い
    Color(0xFFFFAB40),  // 2: 明るい
    Color(0xFFFFD180),  // 3: 薄い
    Color(0xFFE65100)   // 4: 濃い
)

// バッジ背景（薄色）
val AccentBadgeBg = listOf(
    Color(0xFFFFF3E0),
    Color(0xFFFBE9E7),
    Color(0xFFFFF8E1),
    Color(0xFFFFF9C4),
    Color(0xFFFCE4EC)
)