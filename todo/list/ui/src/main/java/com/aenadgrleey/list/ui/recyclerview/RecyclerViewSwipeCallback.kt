package com.aenadgrleey.list.ui.recyclerview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.alpha
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.aenadgrleey.core.ui.resolveColorAttribute
import com.aenadgrleey.list.ui.utils.toPx
import kotlin.math.abs
import com.aenadgrleey.resources.R as CommonR
import com.google.android.material.R as MaterialR


class RecyclerViewSwipeCallback(
    private val context: Context,
    private val onCompleteSwipe: (Int) -> Unit,
    private val onDeleteSwipe: (Int) -> Unit,
) : ItemTouchHelper.Callback() {

    private var coefficientLeft = 0.01F
    private var coefficientRight = 0.01F

    private val leftContainerColor = context.getColor(CommonR.color.greenContainer)
    private val rightContainerColor = context.resolveColorAttribute(MaterialR.attr.colorErrorContainer)

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: ViewHolder,
    ) =
        if (viewHolder.itemViewType == RecyclerViewAdapter.lastItemTag) makeMovementFlags(0, 0)
        else makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)

    override fun onMove(recyclerView: RecyclerView, viewHolder: ViewHolder, target: ViewHolder) = true

    override fun getSwipeThreshold(viewHolder: ViewHolder): Float = 0.1f


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
            val paint = Paint()

            val marginHorizontal = 16.toPx
            val vhHeight = viewHolder.itemView.bottom - viewHolder.itemView.top
            val vhWidth = viewHolder.itemView.width

            if (dX > 0) {
                coefficientLeft = abs(viewHolder.itemView.run { x.div(x + width) * 4 })
                    .let { if (it != 0f && it != 1f) it else coefficientLeft }
                paint.color = leftContainerColor.let { Color.argb((coefficientLeft * it.alpha).toInt(), it.red, it.green, it.blue) }

                val completeIcon = AppCompatResources.getDrawable(context, CommonR.drawable.round_done_24)!!
                    .apply { setTint(context.resolveColorAttribute(MaterialR.attr.colorOutline)) }
                completeIcon.bounds = Rect(
                    marginHorizontal,
                    (viewHolder.itemView.top + vhHeight.div(2) - completeIcon.intrinsicHeight.div(2)),
                    marginHorizontal + completeIcon.intrinsicWidth,
                    viewHolder.itemView.top + vhHeight.div(2) + completeIcon.intrinsicHeight.div(2)
                )
                completeIcon.draw(canvas)

            }

            if (dX < 0) {

                coefficientRight = abs(viewHolder.itemView.run { x.div(x - width) }
                    .let { if (it != 1f && it != 0f) it else coefficientRight })
                paint.color = rightContainerColor.let { Color.argb((coefficientRight * it.alpha).toInt(), it.red, it.green, it.blue) }

                val deleteIcon = AppCompatResources.getDrawable(context, CommonR.drawable.round_delete_outline_24)!!
                    .apply { setTint(context.resolveColorAttribute(MaterialR.attr.colorOnErrorContainer)) }
                deleteIcon.bounds = Rect(
                    vhWidth - deleteIcon.intrinsicWidth - marginHorizontal,
                    (viewHolder.itemView.top + vhHeight.div(2) - deleteIcon.intrinsicHeight.div(2)),
                    vhWidth - marginHorizontal,
                    viewHolder.itemView.top + vhHeight.div(2) + deleteIcon.intrinsicHeight.div(2)
                )
                deleteIcon.draw(canvas)

            }

            val rect = with(viewHolder.itemView) { Rect(0, top, width, bottom) }
            canvas.drawRect(rect, paint)

            val newDx = if (dX > 0) dX / 4 else dX
            super.onChildDraw(
                canvas,
                recyclerView,
                viewHolder,
                newDx,
                dY,
                actionState,
                isCurrentlyActive
            )
        }
    }
}