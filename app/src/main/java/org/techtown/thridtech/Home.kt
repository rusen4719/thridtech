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

// 전역 변수로 바인딩 객체 선언
private var mBinding: ActivityHomeBinding? = null
// 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
private val binding get() = mBinding!!

class Home : AppCompatActivity() {
    private val fragmentOne by lazy { chat_room() }
    private val fragmentTwo by lazy { friends() }
    private val fragmentThree by lazy { settings() }
    private val fragments: List<Fragment> = listOf( fragmentOne, fragmentTwo, fragmentThree )


    private val pagerAdapter: MainViewPagerAdapter by lazy { MainViewPagerAdapter(this, fragments) }

    override fun onCreate(savedInstanceState: Bundle?) {
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
        binding.bottomNav.setItemSelected(R.id.chat_room, true)

        binding.bottomNav.run {
            setOnItemSelectedListener {
                val page = when(it) {
                    R.id.chat_room -> 0
                    R.id.friends -> 1
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
                        0 -> R.id.chat_room
                        1 -> R.id.friends
                        2 -> R.id.settings
                        else -> R.id.chat_room
                    }
                    if (binding.bottomNav.getSelectedItemId() != navigation) {
                        binding.bottomNav.setItemSelected(navigation)
                    }
                }
            })
        }
    }

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }

}