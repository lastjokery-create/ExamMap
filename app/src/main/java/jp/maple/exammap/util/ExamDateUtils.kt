package jp.maple.exammap.util

import jp.maple.exammap.model.ExamDatePrecision
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit

object ExamDateUtils {

    private val exactFormatter =
        DateTimeFormatter.ofPattern("yyyy/MM/dd")

    private val monthFormatter =
        DateTimeFormatter.ofPattern("yyyy/MM")

    fun displayDateText(
        examDate: String,
        precision: ExamDatePrecision
    ): String {
        if (precision == ExamDatePrecision.UNDECIDED) {
            return "日程未定"
        }

        return try {
            when (precision) {
                ExamDatePrecision.EXACT -> {
                    val date = LocalDate.parse(
                        examDate,
                        exactFormatter
                    )

                    date.format(exactFormatter)
                }

                ExamDatePrecision.MONTH_ONLY -> {
                    val yearMonth = YearMonth.parse(
                        examDate,
                        monthFormatter
                    )

                    "${yearMonth.year}年${yearMonth.monthValue}月"
                }

                ExamDatePrecision.EARLY_MONTH -> {
                    val yearMonth = YearMonth.parse(
                        examDate,
                        monthFormatter
                    )

                    "${yearMonth.year}年${yearMonth.monthValue}月上旬"
                }

                ExamDatePrecision.MID_MONTH -> {
                    val yearMonth = YearMonth.parse(
                        examDate,
                        monthFormatter
                    )

                    "${yearMonth.year}年${yearMonth.monthValue}月中旬"
                }

                ExamDatePrecision.LATE_MONTH -> {
                    val yearMonth = YearMonth.parse(
                        examDate,
                        monthFormatter
                    )

                    "${yearMonth.year}年${yearMonth.monthValue}月下旬"
                }

                ExamDatePrecision.UNDECIDED -> {
                    "日程未定"
                }
            }
        } catch (_: DateTimeParseException) {
            "日付未入力"
        }
    }

    fun remainingDaysText(
        examDate: String,
        precision: ExamDatePrecision
    ): String {
        val targetDate = representativeDateOrNull(
            examDate = examDate,
            precision = precision
        ) ?: return ""

        val remainingDays = ChronoUnit.DAYS.between(
            LocalDate.now(),
            targetDate
        )

        return when {
            remainingDays > 0 -> "残り${remainingDays}日"
            remainingDays == 0L -> "本日"
            else -> "試験終了"
        }
    }

    fun representativeDateOrNull(
        examDate: String,
        precision: ExamDatePrecision
    ): LocalDate? {
        return try {
            when (precision) {
                ExamDatePrecision.EXACT -> {
                    LocalDate.parse(
                        examDate,
                        exactFormatter
                    )
                }

                ExamDatePrecision.MONTH_ONLY -> {
                    YearMonth.parse(
                        examDate,
                        monthFormatter
                    ).atDay(15)
                }

                ExamDatePrecision.EARLY_MONTH -> {
                    YearMonth.parse(
                        examDate,
                        monthFormatter
                    ).atDay(5)
                }

                ExamDatePrecision.MID_MONTH -> {
                    YearMonth.parse(
                        examDate,
                        monthFormatter
                    ).atDay(15)
                }

                ExamDatePrecision.LATE_MONTH -> {
                    val yearMonth = YearMonth.parse(
                        examDate,
                        monthFormatter
                    )

                    yearMonth.atDay(
                        minOf(25, yearMonth.lengthOfMonth())
                    )
                }

                ExamDatePrecision.UNDECIDED -> {
                    null
                }
            }
        } catch (_: DateTimeParseException) {
            null
        }
    }
}