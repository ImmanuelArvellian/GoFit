package com.example.gofit.homeMO

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.gofit.R
import com.example.gofit.databinding.ActivityHomeMemberBinding
import com.example.gofit.databinding.ActivityHomeMoBinding
import com.example.gofit.homeMO.presensiInstruktur.presensiInstrukturFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class homeMoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeMoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeMoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firstFragment = homeMoFragment()
        val secondFragment = presensiInstrukturFragment()

        val bundle = intent.extras
        val id_user = bundle?.getString("id_user")

        val fragmentBundle = Bundle()
        fragmentBundle.putString("id_user", id_user)

        val navView: BottomNavigationView = binding.bottomNavigationMo

        navView.setOnNavigationItemSelectedListener {
            if(it.itemId == R.id.nav_home_mo) {
                setCurrentFragment(firstFragment)
            }else if(it.itemId == R.id.nav_presensiInstruktur) {
                secondFragment.arguments = fragmentBundle
                setCurrentFragment(secondFragment)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.containerMo,fragment)
            commit()
        }
    }
}