package com.example.weatherapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class WeeklyForecastFragment : Fragment() {
    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager
    private val forecastRepository: ForecastRepository = ForecastRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_weekly_forecast, container, false)

        tempDisplaySettingManager = TempDisplaySettingManager(requireContext())

        val forecastList: RecyclerView = view.findViewById(R.id.forecastList)
        forecastList.layoutManager = LinearLayoutManager(requireContext())

        val dailyForecastAdapter = DailyForecastAdapter(tempDisplaySettingManager) {
            showForecastDetails(it)
        }
        forecastList.adapter = dailyForecastAdapter

        forecastRepository.weeklyForecast.observe(viewLifecycleOwner, Observer { forecastItems ->
            dailyForecastAdapter.submitList(forecastItems)
        })

        val locationEntryButton = view.findViewById<FloatingActionButton>(R.id.locationEntryButton)

        locationEntryButton.setOnClickListener {
            val action =
                CurrentForecastFragmentDirections.actionCurrentForecastFragmentToLocationEntryFragment()
            findNavController().navigate(action)
        }

       // val zipcode = requireArguments().getString(CurrentForecastFragment.KEY_ZIPCODE, "") ?: ""

        forecastRepository.loadForecast("")

        return view
    }

    private fun showForecastDetails(dailyForecast: DailyForecast) {
        val action =
            CurrentForecastFragmentDirections.actionCurrentForecastFragmentToForecastDetailsFragment(
                dailyForecast.temperature,
                dailyForecast.description
            )
        findNavController().navigate(action)
    }

    companion object {
        const val KEY_ZIPCODE = "key_zipcode"

        fun newInstance(zipcode: String) : WeeklyForecastFragment{
            val currentForecastFragment = WeeklyForecastFragment()

            val args = Bundle()
            args.putString(KEY_ZIPCODE, zipcode)

            currentForecastFragment.arguments = args

            return currentForecastFragment
        }
    }

}