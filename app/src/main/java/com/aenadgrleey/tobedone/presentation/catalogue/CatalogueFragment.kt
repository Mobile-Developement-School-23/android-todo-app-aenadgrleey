package com.aenadgrleey.tobedone.presentation.catalogue

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.aenadgrleey.tobedone.databinding.CatalogueFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CatalogueFragment : Fragment() {
    private var binding: CatalogueFragmentBinding? = null
    private val viewModel by viewModels<CatalogueFragmentViewModel>()
    private var adapter: TodoItemsRecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.items.collectLatest {
                    adapter!!.run {
                        todoItems.clear()
                        todoItems.addAll(it)
                        this.notifyDataSetChanged()
                    }

                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CatalogueFragmentBinding.inflate(layoutInflater)

        adapter = TodoItemsRecyclerViewAdapter({}, {})

        with(binding!!.todoList) {
            adapter = this@CatalogueFragment.adapter

            this.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        return binding!!.root
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    companion object {
        const val TAG = "CatalogueFragment"
    }
}