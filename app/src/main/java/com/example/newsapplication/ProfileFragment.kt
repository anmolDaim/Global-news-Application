package com.example.newsapplication

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.newsapplication.DataClass.Data1


class ProfileFragment : Fragment() {
    lateinit var generalCnstraintLayout:ConstraintLayout
    lateinit var darkModeConstraintLayout:ConstraintLayout
    lateinit var textSizeConstraintLayout:ConstraintLayout
    lateinit var cacheConstraintLayout:ConstraintLayout
    lateinit var clearCacheConstraintLayout:ConstraintLayout
    lateinit var shareFriendsConstraintLayout:ConstraintLayout
    lateinit var rateUsConstraintLayout:ConstraintLayout
    lateinit var aboutConstraintLayout:ConstraintLayout
    lateinit var generalImage:ImageView
    lateinit var cacheImage:ImageView
    lateinit var aboutImage:ImageView
    lateinit var toggleDarkModeButton:AppCompatButton

    private var isExpanded = false
    private var ClearExpanded = false
    private var aboutExpanded = false

    lateinit var Register:TextView
    lateinit var profileLogin:TextView

    private var article: Data1.Article? = null

    fun setArticle(article: Data1.Article) {
        this.article = article
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        generalCnstraintLayout=view.findViewById(R.id.generalCnstraintLayout)
        darkModeConstraintLayout=view.findViewById(R.id.darkModeConstraintLayout)
        textSizeConstraintLayout=view.findViewById(R.id.textSizeConstraintLayout)
        cacheConstraintLayout=view.findViewById(R.id.cacheConstraintLayout)
        clearCacheConstraintLayout=view.findViewById(R.id.clearCacheConstraintLayout)
        shareFriendsConstraintLayout=view.findViewById(R.id.shareFriendsConstraintLayout)
        rateUsConstraintLayout=view.findViewById(R.id.rateUsConstraintLayout)
        aboutConstraintLayout=view.findViewById(R.id.aboutConstraintLayout)
        toggleDarkModeButton=view.findViewById(R.id.toggleDarkModeButton)
        cacheImage=view.findViewById(R.id.cacheImage)
        generalImage=view.findViewById(R.id.generalImage)
        aboutImage=view.findViewById(R.id.aboutImage)
        Register=view.findViewById(R.id.Register)
        profileLogin=view.findViewById(R.id.profileLogin)

    return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Register.setOnClickListener {
            val intent = Intent(requireContext(),RegisterActivity::class.java)
            startActivity(intent)
        }
        profileLogin.setOnClickListener {
            val intent = Intent(requireContext(),LoginActivity::class.java)
            startActivity(intent)
        }
        generalCnstraintLayout.setOnClickListener {
            if (isExpanded) {
                // Wrap up
                darkModeConstraintLayout.visibility = View.GONE
                textSizeConstraintLayout.visibility = View.GONE

                generalImage.setImageResource(R.drawable.baseline_keyboard_arrow_down_24)
            } else {
                // Expand
                darkModeConstraintLayout.visibility = View.VISIBLE
                textSizeConstraintLayout.visibility = View.VISIBLE
                generalImage.setImageResource(R.drawable.baseline_keyboard_arrow_up_24)
            }
            isExpanded = !isExpanded
        }
        cacheConstraintLayout.setOnClickListener {
            if (ClearExpanded) {
                // Wrap up
                clearCacheConstraintLayout.visibility = View.GONE

                cacheImage.setImageResource(R.drawable.baseline_keyboard_arrow_down_24)
            } else {
                // Expand
                clearCacheConstraintLayout.visibility = View.VISIBLE
                cacheImage.setImageResource(R.drawable.baseline_keyboard_arrow_up_24)
            }
            ClearExpanded = !ClearExpanded
        }
        clearCacheConstraintLayout.setOnClickListener {
            // Use an Intent to open the specific application settings page
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", requireContext().packageName, null)
            intent.data = uri
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(requireContext(), "Unable to open app settings.", Toast.LENGTH_SHORT).show()
            }
        }
        aboutConstraintLayout.setOnClickListener {
            if (aboutExpanded) {
                // Wrap up
                rateUsConstraintLayout.visibility = View.GONE
                shareFriendsConstraintLayout.visibility = View.GONE

                aboutImage.setImageResource(R.drawable.baseline_keyboard_arrow_down_24)
            } else {
                // Expand
                rateUsConstraintLayout.visibility = View.VISIBLE
                shareFriendsConstraintLayout.visibility = View.VISIBLE
                aboutImage.setImageResource(R.drawable.baseline_keyboard_arrow_up_24)
            }
            aboutExpanded = !aboutExpanded
        }

        rateUsConstraintLayout.setOnClickListener {
            val intent=Intent(requireContext(),RateUsActivity::class.java)
            startActivity(intent)
        }
        shareFriendsConstraintLayout.setOnClickListener {
            shareContent()
        }

        val currentMode = AppCompatDelegate.getDefaultNightMode()
        if (currentMode == AppCompatDelegate.MODE_NIGHT_YES) {
            // Dark mode is enabled
            toggleDarkModeButton.text = "Light"
        } else {
            // Light mode is enabled
            toggleDarkModeButton.text = "Dark"
        }

        // Set click listener for the button
        toggleDarkModeButton.setOnClickListener {
            toggleDarkMode()
        }
//
//        textSizeConstraintLayout.setOnClickListener {
//            showTextSizeDialog()
//        }
    }
//    private fun showTextSizeDialog() {
//        val dialogBuilder = Dialog(requireContext(), R.style.CustomAlertDialogStyle)
//        dialogBuilder.setContentView(R.layout.text_size_layout)
//
//        val radioGroup = dialogBuilder.findViewById<EditText>(R.id.radioGroup)
//        val radioButtonLarge = dialogBuilder.findViewById<EditText>(R.id.radioButtonLarge)
//        val radioButtonMedium = dialogBuilder.findViewById<EditText>(R.id.radioButtonMedium)
//        val radioButtonSmall = dialogBuilder.findViewById<AppCompatButton>(R.id.radioButtonSmall)
//        dialogBuilder.setTitle("Select Text Size")
//
////        val checkedId = radioGroup.checkedRadioButtonId
////        var textSize =13
////        when (checkedId) {
////            radioButtonSmall -> {
////                textSize = 12 // Small text size
////            }
////
////            radioButtonMedium -> {
////                textSize = 15 // Medium text size
////            }
////
////            radioButtonLarge -> {
////                textSize = 18 // Large text size
////            }
////        }
//
//        //article?.content = textSize.toString()
//
//        // Close the dialog
//        dialogBuilder.show()
//    }

    private fun toggleDarkMode() {
        val currentMode = AppCompatDelegate.getDefaultNightMode()
        if (currentMode == AppCompatDelegate.MODE_NIGHT_YES) {
            // Dark mode is currently enabled, disable it
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            toggleDarkModeButton.text = "Dark"
        } else {
            // Dark mode is currently disabled, enable it
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            toggleDarkModeButton.text = "Light"
        }
    }
    private fun shareContent() {
        // Define the text you want to share
        val shareText = "Check out this amazing app!"

        // Create an ACTION_SEND intent
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }

        // Create a chooser intent to let the user select the app to share the content
        val chooserIntent = Intent.createChooser(shareIntent, "Share with...")

        // Try to launch the chooser
        try {
            startActivity(chooserIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(requireContext(), "No application available to share content", Toast.LENGTH_SHORT).show()
        }
    }
}