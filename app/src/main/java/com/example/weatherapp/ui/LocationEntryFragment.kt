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


class LocationEntryFragment : Fragment() {
    private lateinit var locationRepository: LocationRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootview = inflater.inflate(R.layout.fragment_location_entry, container, false)

        locationRepository =
            LocationRepository(requireContext())

        val submitZipcodeButton: Button = rootview.findViewById(R.id.submitZipcodeButton)
        val zipcodeEditText: EditText = rootview.findViewById(R.id.zipcodeEditText)

        submitZipcodeButton.setOnClickListener {
            if (zipcodeEditText.text.toString().length != 6) {
                Toast.makeText(requireContext(), "Mismatch Zipcode", Toast.LENGTH_SHORT).show()
            } else {
                locationRepository.saveLocation(
                    Location.Zipcode(
                        zipcodeEditText.text.toString()
                    )
                )
                findNavController().navigateUp()
            }
        }

        return rootview
    }


}