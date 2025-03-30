package com.phinma.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.phinma.databinding.FragmentResourcesBinding
import com.phinma.models.ResourceItem
import com.phinma.adapters.ResourcesAdapter

class ResourcesFragment : Fragment() {
    private var _binding: FragmentResourcesBinding? = null
    private val binding get() = _binding!!
    private lateinit var resourcesAdapter: ResourcesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResourcesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        resourcesAdapter = ResourcesAdapter(getSampleResources())
        binding.resourcesRecyclerView.apply {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            adapter = resourcesAdapter
        }
    }

    private fun getSampleResources(): List<ResourceItem> {
        return listOf(
            ResourceItem("Student Manual", "Official student handbook", R.drawable.ic_book),
            ResourceItem("Scholarships", "Available financial aids", R.drawable.ic_scholarship),
            ResourceItem("Modality Help", "FLEX & RAD programs", R.drawable.ic_help),
            ResourceItem("Marketing Videos", "School promotions", R.drawable.ic_video)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}