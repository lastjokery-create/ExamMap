package jp.maple.exammap.model

enum class RecordSortOrder(val label: String) {
    DATE_DESC("新しい順"),
    DATE_ASC("古い順"),
    PERCENT_DESC("正答率が高い順"),
    PERCENT_ASC("正答率が低い順")
}
