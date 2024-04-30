package de.syntax.androidabschluss

import ProfileInfoDialogFragment
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.graphics.Camera
import android.graphics.Color
import android.graphics.Outline
import android.os.Bundle
import android.view.View
import android.view.ViewOutlineProvider
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.view.animation.Transformation
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUiSaveStateControl
import com.bumptech.glide.Glide
import de.syntax.androidabschluss.databinding.ActivityMainBinding
import de.syntax.androidabschluss.ui.authentication.FirestoreViewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: FirestoreViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var userMail: String

    @OptIn(NavigationUiSaveStateControl::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //ADS
        //MobileAds.initialize(this) {}


        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navHost.navController, false)

        binding.profile.setOnClickListener {


            val dialogFragment = ProfileInfoDialogFragment()
            dialogFragment.show(supportFragmentManager, "ProfileInfoDialogFragment")
        }
        binding.logo.setOnClickListener {
            // Property-Animation für die Rotation um 360 Grad entlang der X-Achse
            val rotateXAnimation = PropertyValuesHolder.ofFloat(View.ROTATION_X, 0f, 360f)

            // Property-Animation für die Rotation um 360 Grad entlang der Y-Achse
            //val rotateYAnimation = PropertyValuesHolder.ofFloat(View.ROTATION_Y, 0f, 360f)

            // Dauer der Animation in Millisekunden (hier 3 Sekunden)
            val duration = 5000L

            // Erstelle ein ObjectAnimator, um die Animation auszuführen
            val animator = ObjectAnimator.ofPropertyValuesHolder(binding.logo, rotateXAnimation)

            // Dauer der Animation festlegen
            animator.duration = duration

            // Animation starten
            animator.start()
        }


    }



    class My3DRotationAnimation : Animation() {
        private var centerX: Float = 0f
        private var centerY: Float = 0f
        private val camera = Camera()

        override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) {
            super.initialize(width, height, parentWidth, parentHeight)
            centerX = (width / 2).toFloat()
            centerY = (height / 2).toFloat()
        }

        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            super.applyTransformation(interpolatedTime, t)
            val matrix = t?.matrix ?: return

            // Speichere den aktuellen Status der Matrix
            camera.save()

            // Drehung um die Z-Achse
            camera.rotateY(360 * interpolatedTime)

            // Übertrage die Kameraperspektive in die Matrix
            camera.getMatrix(matrix)

            // Bewege die Kamera zurück
            camera.restore()

            // Setze den Pivotpunkt für die Drehung auf das Zentrum der View
            matrix.preTranslate(-centerX, -centerY)
            matrix.postTranslate(centerX, centerY)
        }

    }

}











