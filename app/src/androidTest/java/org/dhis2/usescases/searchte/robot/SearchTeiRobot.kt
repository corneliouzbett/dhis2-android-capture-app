package org.dhis2.usescases.searchte.robot

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollTo
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.hasSibling
import androidx.test.espresso.matcher.ViewMatchers.withChild
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.dhis2.R
import org.dhis2.common.BaseRobot
import org.dhis2.common.matchers.RecyclerviewMatchers.Companion.atPosition
import org.dhis2.common.matchers.RecyclerviewMatchers.Companion.allElementsHave
import org.dhis2.common.matchers.RecyclerviewMatchers.Companion.hasItem
import org.dhis2.common.viewactions.clickChildViewWithId
import org.dhis2.common.viewactions.openSpinnerPopup
import org.dhis2.common.viewactions.typeChildViewWithId
import org.dhis2.usescases.searchTrackEntity.adapters.SearchTEViewHolder
import org.dhis2.usescases.searchte.entity.DisplayListFieldsUIModel
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not

fun searchTeiRobot(searchTeiRobot: SearchTeiRobot.() -> Unit) {
    SearchTeiRobot().apply {
        searchTeiRobot()
    }
}

class SearchTeiRobot : BaseRobot() {

    fun closeSearchForm () {
        waitToDebounce(2500)
        onView(withId(R.id.close_filter)).perform(click())
    }

    fun clickOnTEI(teiName: String, teiLastName: String) {
        onView(withId(R.id.scrollView)).perform(
            scrollTo<SearchTEViewHolder>(allOf(hasDescendant(withText(teiName)), hasDescendant(withText(teiLastName)))),
            actionOnItem<SearchTEViewHolder>(allOf(hasDescendant(withText(teiName)), hasDescendant(withText(teiLastName))), click())
        )
    }

    fun checkTEIsDelete(teiName: String, teiLastName: String) {
        onView(withId(R.id.scrollView))
            .check(matches(not(hasItem(allOf(hasDescendant(withText(teiName)), hasDescendant(
                withText(teiLastName)))))))
    }

    fun typeAttributeAtPosition(searchWord: String, position:Int) {
        onView(withId(R.id.recyclerView))
            .perform(
                actionOnItemAtPosition<SearchTEViewHolder>(position, typeChildViewWithId(searchWord, R.id.input_editText))
            )
        closeKeyboard()
    }

    fun clickOnDateField() {
        onView(withId(R.id.recyclerView))
            .perform(
                actionOnItemAtPosition<SearchTEViewHolder>(2, clickChildViewWithId(R.id.inputEditText))
            )
    }

    fun selectSpecificDate(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        onView(withId(R.id.widget_datepicker)).perform(PickerActions.setDate(year, monthOfYear, dayOfMonth))
    }

    fun acceptDate() {
        onView(withId(R.id.acceptButton)).perform(click())
    }

    fun clickOnFab() {
        onView(withId(R.id.enrollmentButton)).perform(click())
    }

    fun checkListOfSearchTEI(firstSearchWord: String, secondSearchWord: String) {
        onView(withId(R.id.scrollView))
            .check(matches(allElementsHave(allOf(
                hasDescendant(withText(firstSearchWord)),
                hasDescendant(withText(secondSearchWord))
            ))))
    }

    fun checkFilterCount(filterCount: String) {
        onView(withId(R.id.filterCounterSearch))
            .check(matches(withChild(withText(filterCount))))
    }

    fun checkNoSearchResult(searchWord: String, message: String) {
        onView(withId(R.id.message))
            .check(matches(withText(message)))
    }

    fun clickOnProgramSpinner() {
        onView(withId(R.id.program_spinner)).perform(openSpinnerPopup())
    }

    fun selectAProgram(program: String) {
        onView(allOf(withId(R.id.spinner_text), withText(program)))
            .perform(click())
    }

    fun checkProgramHasChanged(program: String) {
        onView(withId(R.id.spinner_text)).check(matches(withText(program)))
    }

    fun checkFieldsFromDisplayList(displayListFieldsUIModel: DisplayListFieldsUIModel) {
        onView(withId(R.id.showAttributesButton)).perform(click())

        onView(withId(R.id.scrollView))
            .check(matches(
                hasDescendant(allOf(
                    hasDescendant(withText("First name")), hasDescendant(withText(displayListFieldsUIModel.name)),
                    hasDescendant(withText("Last name")), hasDescendant(withText(displayListFieldsUIModel.lastName)),
                    hasDescendant(withText("Email")), hasDescendant(withText(displayListFieldsUIModel.email)),
                    hasDescendant(withText("Date of birth")), hasDescendant(withText(displayListFieldsUIModel.birthday)),
                    hasDescendant(withText("Address")), hasDescendant(withText(displayListFieldsUIModel.address))
                ))
            ))
    }

    fun clickOnOptionMenu() {
        onView(withId(R.id.moreOptions)).perform(click())
    }

    fun clickOnShowMap() {
        onView(withText(R.string.show_in_map)).perform(click())
    }

    fun swipeCarouselToLeft() {
        onView(withId(R.id.map_carousel)).perform(scrollToPosition<RecyclerView.ViewHolder>(3))
    }

    fun checkCarouselTEICardInfo(firstName: String) {
        onView(withId(R.id.map_carousel))
            .check(matches(hasItem(hasDescendant(withText(firstName)))))
    }

    fun clickOnSearchFilter() {
        onView(withId(R.id.search_filter)).perform(click())
    }

    fun clickOnShowMoreFilters(){
        onView(withId(R.id.search_filter_general)).perform(click())
    }

    fun selectAnOrgUnit(orgUnit: String) {
        onView(allOf(withId(R.id.checkbox), hasSibling(withText(orgUnit))))
            .perform(click())
    }

    fun clickOnAcceptButton() {
        onView(withId(R.id.accept_button)).perform(click())
    }
}
