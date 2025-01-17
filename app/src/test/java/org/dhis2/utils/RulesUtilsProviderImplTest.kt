package org.dhis2.utils

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import java.util.ArrayList
import org.dhis2.data.forms.dataentry.fields.FieldViewModelFactory
import org.dhis2.data.forms.dataentry.fields.FieldViewModelFactoryImpl
import org.dhis2.form.model.FieldUiModel
import org.hisp.dhis.android.core.D2
import org.hisp.dhis.android.core.common.ObjectStyle
import org.hisp.dhis.android.core.common.ValueType
import org.hisp.dhis.rules.models.RuleActionAssign
import org.hisp.dhis.rules.models.RuleActionDisplayKeyValuePair
import org.hisp.dhis.rules.models.RuleActionDisplayText
import org.hisp.dhis.rules.models.RuleActionErrorOnCompletion
import org.hisp.dhis.rules.models.RuleActionHideField
import org.hisp.dhis.rules.models.RuleActionHideOption
import org.hisp.dhis.rules.models.RuleActionHideOptionGroup
import org.hisp.dhis.rules.models.RuleActionHideProgramStage
import org.hisp.dhis.rules.models.RuleActionHideSection
import org.hisp.dhis.rules.models.RuleActionSetMandatoryField
import org.hisp.dhis.rules.models.RuleActionShowError
import org.hisp.dhis.rules.models.RuleActionShowOptionGroup
import org.hisp.dhis.rules.models.RuleActionShowWarning
import org.hisp.dhis.rules.models.RuleActionWarningOnCompletion
import org.hisp.dhis.rules.models.RuleEffect
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class RulesUtilsProviderImplTest {

    private lateinit var ruleUtils: RulesUtilsProvider
    private lateinit var testFieldViewModels: MutableMap<String, FieldUiModel>
    private lateinit var fieldFactory: FieldViewModelFactory
    private val d2: D2 = Mockito.mock(D2::class.java, Mockito.RETURNS_DEEP_STUBS)

    private val actionCallbacks: RulesActionCallbacks = mock()

    private val testRuleEffects = ArrayList<RuleEffect>()

    @Before
    fun setUp() {
        ruleUtils = RulesUtilsProviderImpl(d2)
        fieldFactory = FieldViewModelFactoryImpl(
            ValueType.values().map { it to it.name }.toMap(),
            false
        )
        testFieldViewModels = getTestingFieldViewModels().associateBy { it.uid }.toMutableMap()
    }

    private fun getTestingFieldViewModels(): MutableList<FieldUiModel> {
        return arrayListOf(
            randomFieldViewModel("uid1", ValueType.TEXT, "section1", null),
            randomFieldViewModel("uid2", ValueType.TEXT, "section1"),
            randomFieldViewModel("uid3", ValueType.TEXT, "section2"),
            randomFieldViewModel("uid4", ValueType.TEXT, "section2"),
            randomFieldViewModel("uid5", ValueType.TEXT, "section2"),
            randomFieldViewModel("uid6", ValueType.TEXT, "section3"),
            randomFieldViewModel("uid7", ValueType.TEXT, "section3")
        )
    }

    private fun randomFieldViewModel(
        uid: String,
        valueType: ValueType,
        section: String,
        value: String? = "test"
    ): FieldUiModel {
        return fieldFactory.create(
            uid,
            "label",
            valueType,
            false,
            null,
            value,
            section,
            null,
            true,
            null,
            null,
            null,
            null,
            ObjectStyle.builder().build(),
            "",
            null,
            null,
            null
        )
    }

    @Test
    fun `Should update fieldViewModel with a warning message`() {
        val testingUid = "uid1"

        testRuleEffects.add(
            RuleEffect.create(
                "ruleUid",
                RuleActionShowWarning.create("content", "action_data", testingUid),
                "data"
            )
        )

        ruleUtils.applyRuleEffects(
            testFieldViewModels,
            Result.success(testRuleEffects),
            actionCallbacks
        )

        Assert.assertNotNull(testFieldViewModels["uid1"]!!.warning)
        Assert.assertEquals(testFieldViewModels["uid1"]!!.warning, "content data")
    }

    @Test
    fun `Should update fieldViewModel with error message`() {
        val testingUid = "uid1"

        testRuleEffects.add(
            RuleEffect.create(
                "ruleUid",
                RuleActionShowError.create("content", "action_data", testingUid),
                "data"
            )
        )

        val testModel = testFieldViewModels[testingUid]

        ruleUtils.applyRuleEffects(
            testFieldViewModels,
            Result.success(testRuleEffects),
            actionCallbacks
        )

        Assert.assertNotNull(testFieldViewModels[testingUid]!!.error)
        Assert.assertEquals(testFieldViewModels[testingUid]!!.error, "content data")
        verify(actionCallbacks, times(1)).setShowError(
            testRuleEffects[0].ruleAction() as RuleActionShowError,
            testModel
        )
    }

    @Test
    fun `Should remove field from list`() {
        val testingUid = "uid3"
        testRuleEffects.add(
            RuleEffect.create(
                "ruleUid",
                RuleActionHideField.create("content", testingUid),
                "data"
            )
        )

        ruleUtils.applyRuleEffects(
            testFieldViewModels,
            Result.success(testRuleEffects),
            actionCallbacks
        )

        Assert.assertFalse(testFieldViewModels.contains(testingUid))
        verify(actionCallbacks, times(1)).save(testingUid, null)
    }

    @Test
    fun `RuleActionDisplayText Should not add new DisplayViewModel`() {
        testRuleEffects.add(
            RuleEffect.create(
                "ruleUid",
                RuleActionDisplayText.createForFeedback("content", "action data"),
                "data"
            )
        )

        val testFieldViewModelSize = testFieldViewModels.size

        ruleUtils.applyRuleEffects(
            testFieldViewModels,
            Result.success(testRuleEffects),
            actionCallbacks
        )

        Assert.assertTrue(testFieldViewModels.size == testFieldViewModelSize)
        Assert.assertTrue(!testFieldViewModels.containsKey("content"))
    }

    @Test
    fun `RuleActionDisplayKeyValuePair should not add new DisplayViewModel`() {
        testRuleEffects.add(
            RuleEffect.create(
                "ruleUid",
                RuleActionDisplayKeyValuePair.createForIndicators("content", "action data"),
                "data"
            )
        )

        val testFieldViewModelSize = testFieldViewModels.size

        ruleUtils.applyRuleEffects(
            testFieldViewModels,
            Result.success(testRuleEffects),
            actionCallbacks
        )

        Assert.assertTrue(testFieldViewModels.size == testFieldViewModelSize)
        Assert.assertTrue(!testFieldViewModels.containsKey("content"))
    }

    @Test
    fun `RuleActionHideSection should remove all fieldViewModel from a given section`() {
        val testingSectionUid = "section2"
        testRuleEffects.add(
            RuleEffect.create(
                "ruleUid",
                RuleActionHideSection.create(testingSectionUid),
                "data"
            )
        )

        val mandatoryFieldUid = "uid3"
        testFieldViewModels.apply {
            put(mandatoryFieldUid, get(mandatoryFieldUid)!!.setFieldMandatory())
        }

        ruleUtils.applyRuleEffects(
            testFieldViewModels,
            Result.success(testRuleEffects),
            actionCallbacks
        )

        assertTrue(testFieldViewModels[mandatoryFieldUid] != null)
        assertTrue(testFieldViewModels["uid4"] == null)
        assertTrue(testFieldViewModels["uid5"] == null)
    }

    @Test
    fun `RuleActionAssign should set a value to a given field without value`() {
        val testingUid = "uid1"

        testRuleEffects.add(
            RuleEffect.create(
                "ruleUid",
                RuleActionAssign.create("content", "data", testingUid),
                "data"
            )
        )

        ruleUtils.applyRuleEffects(
            testFieldViewModels,
            Result.success(testRuleEffects),
            actionCallbacks
        )

        verify(actionCallbacks, times(1)).save(testingUid, "data")
        Assert.assertTrue(testFieldViewModels[testingUid]!!.value.equals("data"))
        Assert.assertTrue(!testFieldViewModels[testingUid]!!.editable)
    }

    @Test
    fun `RuleActionAssign should set a value to a given field with value`() {
        val testingUid = "uid2"
        val testingUid2 = "uid3"

        testRuleEffects.add(
            RuleEffect.create(
                "ruleUid1",
                RuleActionAssign.create("content", "data", testingUid),
                "data"
            )
        )
        testRuleEffects.add(
            RuleEffect.create(
                "ruleUid2",
                RuleActionAssign.create("content", "data", testingUid2),
                "test"
            )
        )

        ruleUtils.applyRuleEffects(
            testFieldViewModels,
            Result.success(testRuleEffects),
            actionCallbacks
        )

        verify(actionCallbacks, times(1)).save(testingUid, "data")
        verify(actionCallbacks, times(0)).save(testingUid2, "test")
        Assert.assertTrue(testFieldViewModels[testingUid]!!.value.equals("data"))
        Assert.assertTrue(testFieldViewModels[testingUid2]!!.value.equals("test"))
        Assert.assertTrue(!testFieldViewModels[testingUid]!!.editable)
        Assert.assertTrue(!testFieldViewModels[testingUid]!!.editable)
    }

    @Test
    fun `RuleActionAssign should set a value to calculated value`() {
        testRuleEffects.add(
            RuleEffect.create(
                "ruleUid",
                RuleActionAssign.create("content", "data", null),
                "data"
            )
        )

        ruleUtils.applyRuleEffects(
            testFieldViewModels,
            Result.success(testRuleEffects),
            actionCallbacks
        )

        verify(actionCallbacks, times(0)).save(any(), any())
    }

    @Test
    fun `RuleActionSetMandatory should mark field as mandatory`() {
        val testingUid = "uid2"

        testRuleEffects.add(
            RuleEffect.create(
                "ruleUid",
                RuleActionSetMandatoryField.create(testingUid),
                "data"
            )
        )

        ruleUtils.applyRuleEffects(
            testFieldViewModels,
            Result.success(testRuleEffects),
            actionCallbacks
        )

        Assert.assertTrue(testFieldViewModels[testingUid]!!.mandatory)
    }

    @Test
    fun `RuleActionWarningOnCompletion should set warning to field and allow completion`() {
        val testingUid = "uid1"

        testRuleEffects.add(
            RuleEffect.create(
                "ruleUid",
                RuleActionWarningOnCompletion.create("content", "action_data", testingUid),
                "data"
            )
        )

        ruleUtils.applyRuleEffects(
            testFieldViewModels,
            Result.success(testRuleEffects),
            actionCallbacks
        )

        Assert.assertEquals(testFieldViewModels[testingUid]!!.warning, "content data")
        verify(actionCallbacks, times(1)).setMessageOnComplete("content", true)
    }

    @Test
    fun `RuleActionErrorOnCompletion should set warning to field and not allow completion`() {
        val testingUid = "uid1"

        testRuleEffects.add(
            RuleEffect.create(
                "ruleUid",
                RuleActionErrorOnCompletion.create("content", "action_data", testingUid),
                "data"
            )
        )

        ruleUtils.applyRuleEffects(
            testFieldViewModels,
            Result.success(testRuleEffects),
            actionCallbacks
        )

        Assert.assertEquals(testFieldViewModels[testingUid]!!.warning, "content data")
        verify(actionCallbacks, times(1)).setMessageOnComplete("content", false)
    }

    @Test
    fun `RuleActionHideProgramStage should execute callback action`() {
        val testingUid = "stageUid"
        testRuleEffects.add(
            RuleEffect.create(
                "ruleUid",
                RuleActionHideProgramStage.create(testingUid),
                "data"
            )
        )

        ruleUtils.applyRuleEffects(
            testFieldViewModels,
            Result.success(testRuleEffects),
            actionCallbacks
        )

        verify(actionCallbacks, times(1)).setHideProgramStage(testingUid)
    }

    @Test
    fun `RuleActionHideProgramStage should remove stage from possible selections`() {
    }

    @Test
    fun `RuleActionHideOption should execute callback action`() {
        testRuleEffects.add(
            RuleEffect.create(
                "ruleUid",
                RuleActionHideOption.create("content", "optionUid", "field"),
                "data"
            )
        )

        ruleUtils.applyRuleEffects(
            testFieldViewModels,
            Result.success(testRuleEffects),
            actionCallbacks
        )

        verify(actionCallbacks, times(1)).setOptionToHide("optionUid", "field")
    }

    @Test
    fun `RuleActionHideOptionGroup should execute callback action`() {
        testRuleEffects.add(
            RuleEffect.create(
                "ruleUid",
                RuleActionHideOptionGroup.create("content", "optionGroupUid", "field"),
                "data"
            )
        )

        ruleUtils.applyRuleEffects(
            testFieldViewModels,
            Result.success(testRuleEffects),
            actionCallbacks
        )

        verify(actionCallbacks, times(1)).setOptionGroupToHide("optionGroupUid", true, "field")
    }

    @Test
    fun `RuleActionShowOptionGroup should execute callback action`() {
        testRuleEffects.add(
            RuleEffect.create(
                "ruleUid",
                RuleActionShowOptionGroup.create("content", "optionGroupUid", "field"),
                "data"
            )
        )

        ruleUtils.applyRuleEffects(
            testFieldViewModels,
            Result.success(testRuleEffects),
            actionCallbacks
        )

        verify(actionCallbacks, times(1)).setOptionGroupToHide("optionGroupUid", false, "field")
    }
}
