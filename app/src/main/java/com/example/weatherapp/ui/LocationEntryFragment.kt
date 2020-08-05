package com.example.weatherapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.repository.Location
import com.example.weatherapp.repository.LocationRepository
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentLocationEntryBinding


class LocationEntryFragment : Fragment() {
    private lateinit var locationRepository: LocationRepository

    private var _binding: FragmentLocationEntryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLocationEntryBinding.inflate(inflater, container, false)

        locationRepository = LocationRepository(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.submitZipcodeButton.setOnClickListener {
            if (binding.zipcodeEditText.text.toString().length != 6) {
                Toast.makeText(requireContext(), "Mismatch Zipcode", Toast.LENGTH_SHORT).show()
            } else {
                locationRepository.saveLocation(Location.Zipcode(binding.zipcodeEditText.text.toString()))
                findNavController().navigateUp()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}