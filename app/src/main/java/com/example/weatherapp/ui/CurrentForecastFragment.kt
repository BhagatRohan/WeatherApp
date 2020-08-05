package com.example.weatherapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.databinding.FragmentCurrentForecastBinding
import com.example.weatherapp.repository.ForecastRepository
import com.example.weatherapp.repository.Location
import com.example.weatherapp.repository.LocationRepository
import com.example.weatherapp.utils.TempDisplaySettingManager
import com.example.weatherapp.utils.formatTemperature
import com.example.weatherapp.viewModel.CurrentForecastViewModel
import com.example.weatherapp.viewModel.CurrentForecastViewModelFactory


class CurrentForecastFragment : Fragment() {
    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager
    private val forecastRepository: ForecastRepository = ForecastRepository()
    private lateinit var locationRepository: LocationRepository

    private var _binding: FragmentCurrentForecastBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModelFactory: CurrentForecastViewModelFactory
    private val viewModel: CurrentForecastViewModel by viewModels(
        factoryProducer = { viewModelFactory }
    )


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCurrentForecastBinding.inflate(inflater, container, false)

        viewModelFactory = CurrentForecastViewModelFactory(forecastRepository)
        tempDisplaySettingManager =
            TempDisplaySettingManager(requireContext())
        locationRepository = LocationRepository(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.locationEntryButton.setOnClickListener {
            val action =
                CurrentForecastFragmentDirections.actionCurrentForecastFragmentToLocationEntryFragment()
            findNavController().navigate(action)
        }

        viewModel.currentWeather.observe(viewLifecycleOwner, Observer { weather ->
            binding.emptyText.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
            binding.locationName.visibility = View.VISIBLE
            binding.tempText.visibility = View.VISIBLE

            binding.locationName.text = weather.name
            binding.tempText.text =
                formatTemperature(
                    weather.forecast.temp,
                    tempDisplaySettingManager.getDisplaySetting()
                )
        })

        locationRepository.savedLocation.observe(viewLifecycleOwner, Observer { savedLocation ->
            when (savedLocation) {
                is Location.Zipcode -> {
                    binding.progressBar.visibility = View.VISIBLE
                    viewModel.loadCurrentForecast(savedLocation.zipcode)
                }
            }
        })

    }

}