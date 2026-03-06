package com.jeremiascortes.flowguide.features.procedure.domain.util

import com.jeremiascortes.flowguide.features.procedure.domain.model.CheckboxNode

fun updateNode(node: CheckboxNode, targetId: Int, newValue: Boolean): CheckboxNode {
    if (node.id == targetId) {
        return node.copy(
            isChecked = newValue,
            children = node.children.map { setAllChildren(it, newValue) }
        )
    }
    return node.copy(children = node.children.map { updateNode(it, targetId, newValue) })
}

fun setAllChildren(node: CheckboxNode, value: Boolean): CheckboxNode {
    return node.copy(
        isChecked = value,
        children = node.children.map { setAllChildren(it, value) }
    )
}