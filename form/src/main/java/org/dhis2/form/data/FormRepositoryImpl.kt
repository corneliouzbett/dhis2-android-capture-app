package org.dhis2.form.data

import org.dhis2.form.model.ActionType
import org.dhis2.form.model.FieldUiModel
import org.dhis2.form.model.RowAction
import org.dhis2.form.model.StoreResult
import org.dhis2.form.model.ValueStoreResult

class FormRepositoryImpl(
    private val formValueStore: FormValueStore
) : FormRepository {

    private val itemsWithError = mutableListOf<RowAction>()
    private var itemList: List<FieldUiModel> = emptyList()
    private var focusedItem: RowAction? = null

    override fun processUserAction(action: RowAction): StoreResult {
        return when (action.type) {
            ActionType.ON_SAVE -> {
                updateErrorList(action)
                if (action.error != null) {
                    StoreResult(
                        action.id,
                        ValueStoreResult.VALUE_HAS_NOT_CHANGED
                    )

                } else {
                    formValueStore.save(action.id, action.value, action.extraData).blockingSingle()
                }
            }
            ActionType.ON_FOCUS, ActionType.ON_NEXT -> {
                this.focusedItem = action

                StoreResult(
                    action.id,
                    ValueStoreResult.VALUE_HAS_NOT_CHANGED
                )

            }

            ActionType.ON_TEXT_CHANGE -> {
                updateErrorList(action)

                itemList.let { list ->
                    list.find { item ->
                        item.uid == action.id
                    }?.let { item ->
                        itemList = list.updated(
                            list.indexOf(item),
                            item.setValue(action.value)
                        )
                    }
                }
                StoreResult(action.id)
            }
        }
    }

    override fun composeList(list: List<FieldUiModel>?): List<FieldUiModel> {
        list?.let {
            itemList = it
        }
        val listWithErrors = mergeListWithErrorFields(itemList, itemsWithError)
        return setFocusedItem(listWithErrors).toMutableList()
    }

    private fun mergeListWithErrorFields(
        list: List<FieldUiModel>,
        fieldsWithError: MutableList<RowAction>
    ): List<FieldUiModel> {
        return list.map { item ->
            fieldsWithError.find { it.id == item.uid }?.let { action ->
                item.setValue(action.value).setError(action.error)
            } ?: item
        }
    }

    private fun setFocusedItem(list: List<FieldUiModel>) = focusedItem?.let {
        val uid = if (it.type == ActionType.ON_NEXT) {
            getNextItem(it.id)
        } else {
            it.id
        }

        list.find { item ->
            item.uid == uid
        }?.let { item ->
            list.updated(list.indexOf(item), item.setFocus())
        } ?: list
    } ?: list

    private fun getNextItem(currentItemUid: String): String? {
        itemList.let { fields ->
            val oldItem = fields.find { it.uid == currentItemUid }
            val pos = fields.indexOf(oldItem)
            if (pos < fields.size - 1) {
                return fields[pos + 1].uid
            }
        }
        return null
    }

    private fun updateErrorList(action: RowAction) {
        if (action.error != null) {
            if (itemsWithError.find { it.id == action.id } == null) {
                itemsWithError.add(action)
            }
        } else {
            itemsWithError.find { it.id == action.id }?.let {
                itemsWithError.remove(it)
            }
        }
    }

    fun <E> Iterable<E>.updated(index: Int, elem: E): List<E> =
        mapIndexed { i, existing -> if (i == index) elem else existing }
}
