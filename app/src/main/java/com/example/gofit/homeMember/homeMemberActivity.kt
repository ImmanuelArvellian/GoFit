package com.example.gofit.homeMember

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.gofit.R
import com.example.gofit.databinding.ActivityHomeMemberBinding
import com.example.gofit.homeMember.bookingKelas.bookingKelasFragment
import com.example.gofit.homeMember.profileMember.profileMemberFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class homeMemberActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeMemberBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeMemberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firstFragment = homeMemberFragment()
        val secondFragment = bookingKelasFragment()
        val thirdFragment = profileMemberFragment()

        val bundle = intent.extras
        val id_user = bundle?.getString("id_user")

        val fragmentBundle = Bundle()
        fragmentBundle.putString("id_user", id_user)

        val navView: BottomNavigationView = binding.bottomNavigation

        navView.setOnNavigationItemSelectedListener {
            if(it.itemId == R.id.nav_home_member) {
                setCurrentFragment(firstFragment)
            }else if(it.itemId == R.id.nav_bookKelas) {
                secondFragment.arguments = fragmentBundle
                setCurrentFragment(secondFragment)
            }else if(it.itemId == R.id.nav_profileMember){
                thirdFragment.arguments = fragmentBundle
                setCurrentFragment(thirdFragment)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.containerMember,fragment)
            commit()
        }
    }
}