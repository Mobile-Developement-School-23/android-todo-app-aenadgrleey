package com.aenadgrleey.tobedone.presentation.catalogue.utils

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
import com.aenadgrleey.tobedone.R
import com.aenadgrleey.tobedone.utils.toPx
import kotlin.math.abs


class TodoItemsSwipeCallback(
    private val context: Context,
    private val onCompleteSwipe: (Int) -> Unit,
    private val onDeleteSwipe: (Int) -> Unit
) : ItemTouchHelper.Callback() {

    private var coefficientLeft = 0.01F
    private var coefficientRight = 0.01F

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: ViewHolder
    ) = makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: ViewHolder,
        target: ViewHolder
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
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val rect = Rect(
                0,
                viewHolder.itemView.top,
                viewHolder.itemView.width,
                viewHolder.itemView.bottom
            )
            val paint = Paint()

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
                }

                dX < 0 -> {
                    coefficientRight = abs(
                        viewHolder.itemView.run { x.div(x - width) * 4 }
                            .let { if (it != 1f && it != 0f) it else coefficientRight })
                    val typedValue = TypedValue()
                    context.theme.resolveAttribute(
                        com.google.android.material.R.attr.colorErrorContainer,
                        typedValue,
                        true
                    )
                    val parsedColor = typedValue.data
                    val shownColor = Color.argb(
                        (coefficientRight * parsedColor.alpha).toInt(),
                        parsedColor.red,
                        parsedColor.green,
                        parsedColor.blue
                    )
                    paint.color = shownColor
                }
            }

            canvas.drawRect(rect, paint)

            val typedValue = TypedValue()
            context.theme.resolveAttribute(
                com.google.android.material.R.attr.colorOnErrorContainer,
                typedValue,
                true
            )
            val colorOutline = typedValue.data
            val completeIcon =
                AppCompatResources.getDrawable(context, R.drawable.round_done_24)!!
                    .apply { setTint(colorOutline) }
            val deleteIcon =
                AppCompatResources.getDrawable(context, R.drawable.round_delete_outline_24)!!
                    .apply { setTint(colorOutline) }
            val marginHorizontal = 16.toPx

            val vhHeight = viewHolder.itemView.bottom - viewHolder.itemView.top
            val vhWidth = viewHolder.itemView.width
            completeIcon.bounds = Rect(
                marginHorizontal,
                (viewHolder.itemView.top + vhHeight.div(2) - completeIcon.intrinsicHeight.div(2)),
                marginHorizontal + completeIcon.intrinsicWidth,
                viewHolder.itemView.top + vhHeight.div(2) + completeIcon.intrinsicHeight.div(2)
            )
            deleteIcon.bounds = Rect(
                vhWidth - deleteIcon.intrinsicWidth - marginHorizontal,
                (viewHolder.itemView.top + vhHeight.div(2) - deleteIcon.intrinsicHeight.div(2)),
                vhWidth - marginHorizontal,
                viewHolder.itemView.top + vhHeight.div(2) + deleteIcon.intrinsicHeight.div(2)
            )

            if (dX > 0) completeIcon.draw(canvas) else deleteIcon.draw(canvas)

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

    companion object {
        val width = 1000.toPx
    }

}