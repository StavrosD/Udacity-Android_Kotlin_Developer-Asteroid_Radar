package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.AsteroidListener
import com.udacity.asteroidradar.AsteroidViewModel
import com.udacity.asteroidradar.AsteroidsAdapter
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private val asteroidViewModel: AsteroidViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(this, AsteroidViewModel.Factory(activity.application))[AsteroidViewModel::class.java]

    }

    private val asteroidsAdapter = AsteroidsAdapter(AsteroidListener {
        asteroidID ->  asteroidViewModel.onAsteroidClicked(asteroidID)
    })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentMainBinding.inflate(inflater) //: FragmentMainBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_main,container,false)

        binding.asteroidViewModel = asteroidViewModel
        binding.lifecycleOwner = this

        binding.asteroidRecycler.adapter = asteroidsAdapter

        asteroidViewModel.navigateToAsteroidDetails.observe(viewLifecycleOwner, { asteroid ->
            asteroid?.let {
                this.findNavController().navigate(
                    MainFragmentDirections.actionShowDetail(asteroid))
                asteroidViewModel.onAsteroidDetailsNavigated()
        }
        })

        asteroidViewModel.asteroids.observe(viewLifecycleOwner, {
            it?.let {
                asteroidsAdapter.addHeaderAndSubmitList(it)
            }
        })

        asteroidViewModel.imageOfTheDay.observe(viewLifecycleOwner,{
            it?.let {
                val imageView = binding.activityMainImageOfTheDay
                Picasso.get().load(it.hdurl).fit().centerCrop().into(imageView)
                imageView.contentDescription = it.title

            }
        })
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
