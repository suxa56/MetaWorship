package uz.suxa.metaworship.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.google.android.material.color.DynamicColors
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import uz.suxa.metaworship.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        DynamicColors.applyToActivityIfAvailable(this)

        if (Firebase.auth.currentUser == null) {
            Firebase.auth.signInAnonymously()
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}