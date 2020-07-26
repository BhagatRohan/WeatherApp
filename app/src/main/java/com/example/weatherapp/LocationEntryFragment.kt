package com.example.weatherapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment


class LocationEntryFragment : Fragment() {

    private lateinit var appNavigator: AppNavigator

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is AppNavigator)
            appNavigator = context
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootview = inflater.inflate(R.layout.fragment_location_entry, container, false)

        val submitZipcodeButton: Button = rootview.findViewById(R.id.submitZipcodeButton)
        val zipcodeEditText: EditText = rootview.findViewById(R.id.zipcodeEditText)

        submitZipcodeButton.setOnClickListener {
            if (zipcodeEditText.text.toString().length != 6) {
                Toast.makeText(requireContext(), "Mismatch Zipcode", Toast.LENGTH_SHORT).show()
            } else {
                if (this::appNavigator.isInitialized){
                    appNavigator.navigateToCurrentForecast(zipcodeEditText.text.toString())
                }
            }
        }

        return rootview
    }


}