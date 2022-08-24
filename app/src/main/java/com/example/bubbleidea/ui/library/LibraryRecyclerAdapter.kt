package com.example.bubbleidea.ui.library

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bubbleidea.database.entites.ActivateWord
import com.example.bubbleidea.databinding.WordCardViewBinding

class LibraryRecyclerAdapter (
    private val layoutInflater: LayoutInflater,
    private val clickListener: ActivateWordClickListener
) : ListAdapter<ActivateWord, LibraryRecyclerAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = WordCardViewBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setData(activateWordList: List<ActivateWord>) {
        submitList(activateWordList.toMutableList())
    }

    inner class MyViewHolder(private val binding: WordCardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    val activateWord = getItem(absoluteAdapterPosition)
                    clickListener.onActivateWordClicked(activateWord)
                }
            }
        }

        fun bind(activateWord: ActivateWord) {
            binding.apply {
                wordText.text = activateWord.activateWord
            }
        }
    }

    interface ActivateWordClickListener {
        fun onActivateWordClicked(activateWord: ActivateWord)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<ActivateWord> = object : DiffUtil.ItemCallback<ActivateWord>() {
            override fun areItemsTheSame(oldItem: ActivateWord, newItem: ActivateWord): Boolean {
                return oldItem.activateWordId == newItem.activateWordId
            }

            override fun areContentsTheSame(oldItem: ActivateWord, newItem: ActivateWord): Boolean {
                return oldItem == newItem
            }
        }
    }
}