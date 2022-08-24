package com.example.bubbleidea.ui.own_ideas_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bubbleidea.database.entites.OwnIdea
import com.example.bubbleidea.databinding.OwnIdeaCardViewBinding

class OwnIdeasRecyclerAdapter(
    private val layoutInflater: LayoutInflater,
    private val clickListener: OwnIdeaClickListener
) : ListAdapter<OwnIdea, OwnIdeasRecyclerAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = OwnIdeaCardViewBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setData(ounIdeasList: List<OwnIdea>) {
        submitList(ounIdeasList.toMutableList())
    }

    inner class MyViewHolder(private val binding: OwnIdeaCardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val ownIdea = getItem(adapterPosition)
                    clickListener.onOwnIdeaClicked(ownIdea)
                }
            }
        }

        fun bind(ownIdea: OwnIdea) {
            binding.apply {
                previewOwnIdea.text = ownIdea.ownIdea
            }
        }
    }

    interface OwnIdeaClickListener {
        fun onOwnIdeaClicked(ownIdea: OwnIdea)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<OwnIdea> = object : DiffUtil.ItemCallback<OwnIdea>() {
            override fun areItemsTheSame(oldItem: OwnIdea, newItem: OwnIdea): Boolean {
                return oldItem.ownIdeaId == newItem.ownIdeaId
            }

            override fun areContentsTheSame(oldItem: OwnIdea, newItem: OwnIdea): Boolean {
                return oldItem == newItem
            }
        }
    }
}