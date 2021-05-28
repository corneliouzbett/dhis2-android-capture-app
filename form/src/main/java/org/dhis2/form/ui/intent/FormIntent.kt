package org.dhis2.form.ui.intent

import org.dhis2.form.mvi.MviIntent

sealed class FormIntent : MviIntent {
    data class OpenYearMonthDayAgeCalendar(
        val uid: String,
        val year: Int,
        val month: Int,
        val day: Int
    ) : FormIntent()

    data class OpenCustomAgeCalendar(val uid: String, val label: String) : FormIntent()
    data class SelectDateFromAgeCalendar(val uid: String, val date: String?) : FormIntent()
    data class ClearDateFromAgeCalendar(val uid: String) : FormIntent()
}