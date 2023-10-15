package com.example.sample23l11paginationswiperefreshloadingelement

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sample23l11paginationswiperefreshloadingelement.databinding.ItemCounterBinding
import com.example.sample23l11paginationswiperefreshloadingelement.databinding.ItemErrorBinding
import com.example.sample23l11paginationswiperefreshloadingelement.databinding.ItemLoadingBinding

sealed class Item {
    data class Counter(val value: Int) : Item()
    object Loading : Item()
    object Error : Item()
}

class CounterAdapter(
    context: Context,
    private val onTryAgainClicked: () -> Unit
) : ListAdapter<Item, RecyclerView.ViewHolder>(DIFF_UTIL) {

    private val layoutInflater = LayoutInflater.from(context)

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is Item.Counter -> TYPE_COUNTER
            Item.Loading -> TYPE_LOADING
            Item.Error -> TYPE_ERROR
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            TYPE_COUNTER -> CounterViewHolder(
                binding = ItemCounterBinding.inflate(layoutInflater, parent, false)
            )
            TYPE_LOADING -> LoadingViewHolder(
                binding = ItemLoadingBinding.inflate(layoutInflater, parent, false)
            )
            TYPE_ERROR -> ErrorViewHolder(
                binding = ItemErrorBinding.inflate(layoutInflater, parent, false),
                onTryAgainClicked = onTryAgainClicked
            )
            else -> {
                error("Unsupported viewType $viewType")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is CounterViewHolder -> {
                if (item !is Item.Counter) return
                holder.bind(item)
            }
        }
    }

    companion object {

        private const val TYPE_COUNTER = 0
        private const val TYPE_LOADING = 1
        private const val TYPE_ERROR = 2

        private val DIFF_UTIL = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem == newItem
            }
        }
    }
}

fun load(lastCounter: Int, itemsToLoad: Int): List<Int> {
    return List(itemsToLoad) {
        lastCounter + 1 + it
    }
}

fun load(lastCounter: Int, itemsToLoad: Int, action: (List<Int>) -> Unit) {
    Handler(Looper.getMainLooper())
        .postDelayed(
            {
                if (lastCounter == 100) {
                    error("emulated error of 100 element")
                }
                action(load(lastCounter, itemsToLoad))
            },
            5000
        )
}

class CounterViewHolder(
    private val binding: ItemCounterBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Item.Counter) {
        binding.textView.text = item.value.toString()
    }

}

class LoadingViewHolder(binding: ItemLoadingBinding) : RecyclerView.ViewHolder(binding.root)

class ErrorViewHolder(
    binding: ItemErrorBinding,
    onTryAgainClicked: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.button.setOnClickListener {
            onTryAgainClicked()
        }
    }
}