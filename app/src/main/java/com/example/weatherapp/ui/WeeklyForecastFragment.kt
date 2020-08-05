package com.example.weatherapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.adapter.DailyForecastAdapter
import com.example.weatherapp.databinding.FragmentWeeklyForecastBinding
import com.example.weatherapp.models.DailyForecast
import com.example.weatherapp.repository.ForecastRepository
import com.example.weatherapp.repository.Location
import com.example.weatherapp.repository.LocationRepository
import com.example.weatherapp.utils.TempDisplaySettingManager
import com.example.weatherapp.viewModel.WeeklyForecastViewModel
import com.example.weatherapp.viewModel.WeeklyForecastViewModelFactory


class WeeklyForecastFragment : Fragment() {
    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager
    private val forecastRepository: ForecastRepository = ForecastRepository()
    private lateinit var locationRepository: LocationRepository

    private var _binding: FragmentWeeklyForecastBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModelFactory: WeeklyForecastViewModelFactory
    private val viewModel: WeeklyForecastViewModel by viewModels(
        factoryProducer = { viewModelFactory }
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentWeeklyForecastBinding.inflate(inflater, container, false)

        tempDisplaySettingManager = TempDisplaySettingManager(requireContext())
        locationRepository = LocationRepository(requireContext())
        viewModelFactory = WeeklyForecastViewModelFactory(forecastRepository)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.locationEntryButton.setOnClickListener {
            val action = CurrentForecastFragmentDirections.actionCurrentForecastFragmentToLocationEntryFragment()
            findNavController().navigate(action)
        }

        binding.forecastList.layoutManager = LinearLayoutManager(requireContext())
        val dailyForecastAdapter = DailyForecastAdapter(tempDisplaySettingManager) {
                showForecastDetails(it)
        }

        binding.forecastList.adapter = dailyForecastAdapter

        viewModel.weeklyWeather.observe(viewLifecycleOwner, Observer { weeklyForecast ->
            binding.emptyText.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
            dailyForecastAdapter.submitList(weeklyForecast.daily)
        })

        locationRepository.savedLocation.observe(viewLifecycleOwner, Observer { savedLocation ->
            when (savedLocation) {
                is Location.Zipcode -> {
                    binding.progressBar.visibility = View.VISIBLE
                    viewModel.loadWeeklyForecast(savedLocation.zipcode)
                }
            }
        })
    }

    private fun showForecastDetails(dailyForecast: DailyForecast) {
        val temp = dailyForecast.temp.max
        val description = dailyForecast.weather[0].description
        val date = dailyForecast.date
        val icon = dailyForecast.weather[0].icon

        val action = WeeklyForecastFragmentDirections.actionWeeklyForecastFragmentToForecastDetailsFragment(
                temp,
                description,
                date,
                icon
            )
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}