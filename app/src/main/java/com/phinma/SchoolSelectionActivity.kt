package com.phinma

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.phinma.adapters.SchoolsAdapter
import com.phinma.databinding.ActivitySchoolSelectionBinding
import com.phinma.models.School

class SchoolSelectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySchoolSelectionBinding
    private lateinit var schoolsAdapter: SchoolsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySchoolSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        schoolsAdapter = SchoolsAdapter(getSchools()) { school ->
            if (school.isActive) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        binding.schoolsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@SchoolSelectionActivity)
            adapter = schoolsAdapter
        }
    }

    private fun getSchools(): List<School> {
        return listOf(
            School(1, "PHINMA University of Pangasinan College Urdaneta", R.drawable.ic_school_upc, true),
            School(2, "PHINMA Cagayan de Oro College", R.drawable.ic_school_coc, false),
            School(3, "PHINMA Araullo University", R.drawable.ic_school_au, false),
            School(4, "PHINMA University of Iloilo", R.drawable.ic_school_ui, false),
            School(5, "PHINMA University of Pangasinan", R.drawable.ic_school_up, false),
            School(6, "PHINMA Rizal College of Laguna", R.drawable.ic_school_rcl, false),
            School(7, "PHINMA Saint Jude College", R.drawable.ic_school_sjc, false),
            School(8, "PHINMA Southwestern University", R.drawable.ic_school_swu, false),
            School(9, "PHINMA Union College of Laguna", R.drawable.ic_school_ucl, false),
            School(10, "PHINMA Davao Doctors College", R.drawable.ic_school_ddc, false)
        )
    }

    private fun setupClickListeners() {
        binding.btnContinue.setOnClickListener {
            // Handle continue button click
        }
    }
}