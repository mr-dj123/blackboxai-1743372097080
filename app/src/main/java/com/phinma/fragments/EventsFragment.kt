package com.phinma.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.phinma.databinding.FragmentEventsBinding
import com.phinma.models.Event
import com.phinma.adapters.EventsAdapter

class EventsFragment : Fragment() {
    private var _binding: FragmentEventsBinding? = null
    private val binding get() = _binding!!
    private lateinit var eventsAdapter: EventsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Setup RecyclerView
        eventsAdapter = EventsAdapter(getSampleEvents())
        binding.eventsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = eventsAdapter
        }
    }

    private fun getSampleEvents(): List<Event> {
        return listOf(
            Event("Orientation Day", "Aug 15, 2023", "Main Auditorium"),
            Event("Career Fair", "Sep 5, 2023", "University Grounds"),
            Event("Sports Fest", "Oct 10-12, 2023", "Sports Complex")
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}