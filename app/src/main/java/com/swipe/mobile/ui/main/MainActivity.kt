package com.swipe.mobile.ui.main

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.swipe.mobile.R
import com.swipe.mobile.base.BaseActivity
import com.swipe.mobile.databinding.ActivityMainBinding
import com.swipe.mobile.ui.main.home.HomeFragment
import com.swipe.mobile.ui.main.myactivities.MyActivitiesFragment
import com.swipe.mobile.ui.main.profile.ProfileFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding =
        ActivityMainBinding::inflate

    override fun setup() {
        val homeFragment = HomeFragment()
        val myActivityFragment = MyActivitiesFragment()
        val profileFragment = ProfileFragment()

        setCurrentFragment(homeFragment)

        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.mi_home -> setCurrentFragment(homeFragment)
                R.id.mi_my_report -> setCurrentFragment(myActivityFragment)
                R.id.mi_profile -> setCurrentFragment(profileFragment)
            }

            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }
}