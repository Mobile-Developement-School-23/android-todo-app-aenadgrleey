package com.aenadgrleey.list.ui.recyclerview

import android.os.Build
import android.view.HapticFeedbackConstants
import androidx.recyclerview.widget.RecyclerView
import com.aenadgrleey.todo.list.ui.databinding.TodoListLastItemBinding

class RecyclerViewLastItemViewHolder(binding: TodoListLastItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private val mainView = binding.root
    fun onBind(onClick: () -> Unit) {
        mainView.setOnClickListener {
            it.performHapticFeedback(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) HapticFeedbackConstants.CONFIRM
                else HapticFeedbackConstants.CONTEXT_CLICK
            )
            onClick()
        }
    }
}