package com.phinma.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.phinma.databinding.ItemResourceBinding
import com.phinma.models.ResourceItem

class ResourcesAdapter(private val resources: List<ResourceItem>) :
    RecyclerView.Adapter<ResourcesAdapter.ResourceViewHolder>() {

    inner class ResourceViewHolder(private val binding: ItemResourceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(resource: ResourceItem) {
            binding.resourceTitle.text = resource.title
            binding.resourceDescription.text = resource.description
            binding.resourceIcon.setImageResource(resource.iconRes)
            
            binding.root.setOnClickListener {
                // Handle resource item click
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceViewHolder {
        val binding = ItemResourceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ResourceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResourceViewHolder, position: Int) {
        holder.bind(resources[position])
    }

    override fun getItemCount() = resources.size
}