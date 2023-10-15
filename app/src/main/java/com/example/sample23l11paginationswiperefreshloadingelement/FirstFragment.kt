package com.example.sample23l11paginationswiperefreshloadingelement

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sample23l11paginationswiperefreshloadingelement.databinding.FragmentFirstBinding

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val adapter by lazy {
        CounterAdapter(requireContext()) {
            loadData()
        }
    }
    private var isLoading = false
    private var lastCounter = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentFirstBinding.inflate(inflater, container,false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            val linearLayoutManager = LinearLayoutManager(requireContext())
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.adapter = adapter
            recyclerView.addVerticalSpace()
            recyclerView.addPaginationListener(linearLayoutManager, ITEMS_TO_LOAD) {
                loadData()
            }
            loadData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun loadData() {
        if (isLoading) return
        isLoading = true

        try {
            load(lastCounter, PAGE_SIZE) { items ->
                lastCounter = items.last()
                val recentItems = adapter.currentList.dropLastWhile { it == Item.Loading }
                val newItems = recentItems + items.map { Item.Counter(it) } + Item.Error
                adapter.submitList(newItems)
                isLoading = false
            }
        } catch (e: Throwable) {
            val recentItems = adapter.currentList.dropLastWhile { it == Item.Loading }
            val newItems = recentItems + Item.Error
            adapter.submitList(newItems)
            isLoading = false
        }
    }

    companion object {
        private const val PAGE_SIZE = 50
        private const val ITEMS_TO_LOAD = 15

    }
}

