package com.cymjoe.agencypurchasework

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.appsisle.moudle_user.ui.fragment.UserFragment
import com.cymjoe.moudle_home.home.HomeFragment

class VPAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            else -> UserFragment()
        }

    }

    override fun getItemCount(): Int {
        return 2
    }
}