package cu.nico.infinity.testapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import c.ContactsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import cu.nico.infinity.testapp.R
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var toolbar : Toolbar
    private lateinit var bottomNavigationView : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window: Window? = window
        if (window != null) {
            window.decorView
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.statusBarColor = getColor(R.color.colorPrimary)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.navigationBarColor = getColor(R.color.colorPrimary)
            }
        }
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        setSupportActionBar(toolbar);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        if (savedInstanceState == null) {
            val fragment = NewsFragment()
            supportFragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.javaClass.simpleName)
                .commit()
        }
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        when (menuItem.itemId) {
            R.id.navigation_news -> {
                val fragment = NewsFragment()
                supportFragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.javaClass.simpleName)
                    .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_contacts -> {

                val fragment = ContactsFragment()
                supportFragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.javaClass.simpleName)
                    .commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }



}
