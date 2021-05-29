package cu.nico.infinity.testapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cu.nico.infinity.testapp.R
import kotlinx.coroutines.*
import java.util.ArrayList

class Splash : AppCompatActivity() {

    private var parentJob: Job = Job()
    private val REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1
    private var coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main + parentJob)

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
        setContentView(R.layout.activity_splash)

        parentJob = Job()

        coroutineScope = CoroutineScope(Dispatchers.Main + parentJob)

        checkAndRequestPermissions()

    }

    private fun checkAndRequestPermissions() {
        val readContact =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val writeContact =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)

        val listPermissionsNeeded: MutableList<String> = ArrayList()
        if (readContact != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS)
        }
        if (writeContact != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_CONTACTS)
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toTypedArray(),
                REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS
            )
        } else {
            coroutineScope.async(Dispatchers.Main) {

                delay(1000)
                startActivity(Intent(this@Splash, MainActivity::class.java))

                parentJob.cancelAndJoin()

            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    checkAndRequestPermissions()
                    coroutineScope.async(Dispatchers.Main) {

                        delay(1000)
                        startActivity(Intent(this@Splash, MainActivity::class.java))

                        parentJob.cancelAndJoin()

                    }
                } else {
                    Toast.makeText(this, "Denied Permission", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}