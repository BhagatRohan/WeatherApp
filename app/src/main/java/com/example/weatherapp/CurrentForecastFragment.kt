package com.example.weatherapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_current_forecast.*


class CurrentForecastFragment : Fragment() {
    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager
    private val forecastRepository: ForecastRepository = ForecastRepository()
    private lateinit var locationRepository: LocationRepository


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_current_forecast, container, false)

        val locationName = view.findViewById<TextView>(R.id.location_name)
        val tempText = view.findViewById<TextView>(R.id.temp_text)

        tempDisplaySettingManager = TempDisplaySettingManager(requireContext())
        locationRepository = LocationRepository(requireContext())

        forecastRepository.currentWeather.observe(viewLifecycleOwner, Observer  { weather ->
            locationName.text = weather.name
            tempText.text = formatTemperature(weather.forecast.temp, tempDisplaySettingManager.getDisplaySetting())
        })

        val locationEntryButton = view.findViewById<FloatingActionButton>(R.id.locationEntryButton)

        locationEntryButton.setOnClickListener {
            val action =
                CurrentForecastFragmentDirections.actionCurrentForecastFragmentToLocationEntryFragment()
            findNavController().navigate(action)
        }

      //  val zipcode = requireArguments().getString(KEY_ZIPCODE, "") ?: ""

        locationRepository.savedLocation.observe(viewLifecycleOwner, Observer<Location> {savedLocation ->
            when(savedLocation){
                is Location.Zipcode -> forecastRepository.loadCurrentForecast(savedLocation.zipcode)
            }
        })

        return view
    }

    companion object {
        const val KEY_ZIPCODE = "key_zipcode"

        fun newInstance(zipcode: String) : CurrentForecastFragment{
            val currentForecastFragment = CurrentForecastFragment()

            val args = Bundle()
            args.putString(KEY_ZIPCODE, zipcode)

            currentForecastFragment.arguments = args

            return currentForecastFragment
        }
    }

}