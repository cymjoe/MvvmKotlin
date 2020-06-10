package com.cymjoe.lib_base.entity

import android.view.View
import androidx.databinding.ViewDataBinding

open class NoDataBinding protected constructor(
    bindingComponent: Any?,
    root: View?,
    localFieldCount: Int
) : ViewDataBinding(bindingComponent, root, localFieldCount) {
    override fun onFieldChange(
        localFieldId: Int,
        `object`: Any,
        fieldId: Int
    ): Boolean {
        return false
    }

    override fun setVariable(variableId: Int, value: Any?): Boolean {
        return false
    }

    override fun executeBindings() {}
    override fun invalidateAll() {}
    override fun hasPendingBindings(): Boolean {
        return false
    }
}