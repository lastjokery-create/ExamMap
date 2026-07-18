package jp.maple.exammap.util

import jp.maple.exammap.model.StudyRecord

object ProgressCalculator {
    /** 進捗: 集計対象の平均正答率。記録なし → -1（未記録）*/
    fun calcProgress(records: List<StudyRecord>): Int {
        val targets = records.filter { it.includedInAggregate }
        if (targets.isEmpty()) return -1
        return targets.map { it.percentage }.average().toInt().coerceIn(0, 100)
    }

    /** 評価A: 実施済み対象の単純平均正答率（calcProgressと同じ）*/
    fun calcEvaluationA(records: List<StudyRecord>): Int = calcProgress(records)

    /** 評価B: 未実施科目を50%として補完した保守的評価 */
    fun calcEvaluationB(records: List<StudyRecord>, totalSubjects: Int): Int {
        val targets = records.filter { it.includedInAggregate }
        if (totalSubjects <= 0) return calcProgress(records)
        val undone = (totalSubjects - targets.size).coerceAtLeast(0)
        val sum = targets.sumOf { it.percentage.toDouble() } + undone * 50.0
        return (sum / totalSubjects).toInt().coerceIn(0, 100)
    }

    /** 判定帯 */
    fun band(value: Int): EvaluationBand = when {
        value < 0  -> EvaluationBand.UNRECORDED
        value < 55 -> EvaluationBand.NEEDS_WORK
        value < 75 -> EvaluationBand.BORDER
        else       -> EvaluationBand.PASS_ZONE
    }
}

enum class EvaluationBand { UNRECORDED, NEEDS_WORK, BORDER, PASS_ZONE }