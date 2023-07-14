package com.aenadgrleey.todolist.ui.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.TypedValue
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.alpha
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.aenadgrleey.resources.R
import kotlin.math.abs


class TodoItemsSwipeCallback(
    private val context: Context,
    private val onCompleteSwipe: (Int) -> Unit,
    private val onDeleteSwipe: (Int) -> Unit,
) : ItemTouchHelper.Callback() {

    private var coefficientLeft = 0.01F
    private var coefficientRight = 0.01F

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: ViewHolder,
    ) =
        if ((recyclerView.adapter as TodoItemsRecyclerViewAdapter).getItemViewType(viewHolder.adapterPosition) == TodoItemsRecyclerViewAdapter.lastItemTag)
            makeMovementFlags(0, 0)
        else makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: ViewHolder,
        target: ViewHolder,
    ) = true

    override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
        if (direction == ItemTouchHelper.RIGHT) onCompleteSwipe(viewHolder.adapterPosition)
        else onDeleteSwipe(viewHolder.adapterPosition)
    }

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean,
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val rect = Rect(
                0,
                viewHolder.itemView.top,
                viewHolder.itemView.width,
                viewHolder.itemView.bottom
            )
            val paint = Paint()
            val typedValue = TypedValue()
            context.theme.resolveAttribute(
                com.google.android.material.R.attr.colorOnErrorContainer,
                typedValue,
                true
            )
            val colorOutline = typedValue.data
            val marginHorizontal = 16.toPx
            val vhHeight = viewHolder.itemView.bottom - viewHolder.itemView.top
            val vhWidth = viewHolder.itemView.width

            when {
                dX > 0 -> {
                    coefficientLeft =
                        viewHolder.itemView.run { x.div(x + width) * 4 }
                            .let { if (it != 0f && it != 1f) it else coefficientLeft }
                    val parsedColor = context.getColor(R.color.greenContainer)
                    val shownColor = Color.argb(
                        (coefficientLeft * parsedColor.alpha).toInt(),
                        parsedColor.red,
                        parsedColor.green,
                        parsedColor.blue
                    )
                    paint.color = shownColor
                    val completeIcon =
                        AppCompatResources.getDrawable(context, R.drawable.round_done_24)!!
                            .apply { setTint(colorOutline) }
                    completeIcon.bounds = Rect(
                        marginHorizontal,
                        (viewHolder.itemView.top + vhHeight.div(2) - completeIcon.intrinsicHeight.div(2)),
                        marginHorizontal + completeIcon.intrinsicWidth,
                        viewHolder.itemView.top + vhHeight.div(2) + completeIcon.intrinsicHeight.div(2)
                    )
                    completeIcon.draw(canvas)
                }

                dX < 0 -> {
                    coefficientRight = abs(
                        viewHolder.itemView.run { x.div(x - width) * 4 }
                            .let { if (it != 1f && it != 0f) it else coefficientRight })
                    val typedValueErrorColor = TypedValue()
                    context.theme.resolveAttribute(
                        com.google.android.material.R.attr.colorErrorContainer,
                        typedValueErrorColor,
                        true
                    )
                    val parsedColor = typedValueErrorColor.data
                    val shownColor = Color.argb(
                        (coefficientRight * parsedColor.alpha).toInt(),
                        parsedColor.red,
                        parsedColor.green,
                        parsedColor.blue
                    )
                    paint.color = shownColor
                    val deleteIcon =
                        AppCompatResources.getDrawable(context, R.drawable.round_delete_outline_24)!!
                            .apply { setTint(colorOutline) }
                    deleteIcon.bounds = Rect(
                        vhWidth - deleteIcon.intrinsicWidth - marginHorizontal,
                        (viewHolder.itemView.top + vhHeight.div(2) - deleteIcon.intrinsicHeight.div(2)),
                        vhWidth - marginHorizontal,
                        viewHolder.itemView.top + vhHeight.div(2) + deleteIcon.intrinsicHeight.div(2)
                    )
                    deleteIcon.draw(canvas)
                }
            }

            canvas.drawRect(rect, paint)

            super.onChildDraw(
                canvas,
                recyclerView,
                viewHolder,
                dX / 4,
                dY,
                actionState,
                isCurrentlyActive
            )
        }
    }
}