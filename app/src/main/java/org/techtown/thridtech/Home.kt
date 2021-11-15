package org.techtown.thridtech

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.techtown.thridtech.databinding.ActivityCreateAccountBinding
import org.techtown.thridtech.databinding.ActivityHomeBinding
import org.techtown.thridtech.databinding.FragmentChatRoomBinding

private var mBinding: ActivityHomeBinding? = null
private val binding get() = mBinding!!

class Home : AppCompatActivity() {
    private val fragmentTwo by lazy { chat_room() }
    private val fragmentOne by lazy { friends() }
    private val fragmentThree by lazy { settings() }
    private val fragments: List<Fragment> = listOf( fragmentOne, fragmentTwo, fragmentThree )

    private val pagerAdapter: MainViewPagerAdapter by lazy { MainViewPagerAdapter(this, fragments) }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Thridtech)
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_home)
        mBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewPager()
        initNavigationBar()
    }

    inner class MainViewPagerAdapter(activity: AppCompatActivity, private val fragments: List<Fragment>) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = fragments.size
        override fun createFragment(position: Int): Fragment = fragments[position]
    }


    private fun initNavigationBar() {
        binding.bottomNav.setItemSelected(R.id.friends, true)

        binding.bottomNav.run {
            setOnItemSelectedListener {
                val page = when(it) {
                    R.id.friends -> 0
                    R.id.chat_room -> 1
                    R.id.settings -> 2
                    else -> 0
                }
                if (page != binding.container.currentItem) {
                    binding.container.currentItem = page
                }
                true
            }
        }
    }

    private fun initViewPager() {
        binding.container.run {
            adapter = pagerAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    val navigation = when(position) {
                        0 -> R.id.friends
                        1 -> R.id.chat_room
                        2 -> R.id.settings
                        else -> R.id.friends
                    }
                    if (binding.bottomNav.getSelectedItemId() != navigation) {
                        binding.bottomNav.setItemSelected(navigation)
                    }
                }
            })
        }
    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }

}