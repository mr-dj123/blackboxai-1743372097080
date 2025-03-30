package com.phinma.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.phinma.databinding.FragmentProfileBinding
import com.phinma.models.UserProfile

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Load sample user profile
        val userProfile = getSampleProfile()
        with(binding) {
            tvName.text = userProfile.name
            tvEmail.text = userProfile.email
            tvSchool.text = userProfile.school
            tvStudentId.text = userProfile.studentId
            tvDepartment.text = userProfile.department
        }
    }

    private fun getSampleProfile(): UserProfile {
        return UserProfile(
            name = "Juan Dela Cruz",
            email = "juan.delacruz@phinma.edu.ph",
            school = "PHINMA University of Pangasinan College Urdaneta",
            studentId = "2023-12345",
            department = "College of Computer Studies"
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}