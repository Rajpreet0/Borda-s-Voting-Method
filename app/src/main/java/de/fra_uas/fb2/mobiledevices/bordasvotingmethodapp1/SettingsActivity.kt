package de.fra_uas.fb2.mobiledevices.bordasvotingmethodapp1

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingsActivity : AppCompatActivity() {

    private lateinit var minVotingSettingsEditText: EditText
    private lateinit var maxVotingSettingsEditText: EditText
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sharedPreferences = getSharedPreferences("SettingsSharedPreferences", 0)

        // Connect to XML
        minVotingSettingsEditText = findViewById<EditText>(R.id.minVotingSettingsEditText)
        maxVotingSettingsEditText = findViewById<EditText>(R.id.maxVotingSettingsEditText)

        getMinMaxDate()
    }

    // Function to retrieve Data from SettingsSharedPreferences File
    private fun getMinMaxDate() {
        val minScore: String? = sharedPreferences.getString("minScore", "2")
        val maxScore: String? = sharedPreferences.getString("maxScore", "10")

        // If there is no data write 2 and 10 default values
        if(minScore == null && maxScore == null) {
            minVotingSettingsEditText.setText("2")
            maxVotingSettingsEditText.setText("10")
        } else {
            minVotingSettingsEditText.setText(minScore)
            maxVotingSettingsEditText.setText(maxScore)
        }
    }

    // Function to update Values in SettingsSharedPreferences File
    fun saveSettingsBtn(view: View) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        // Correctly extracting the text from EditText fields
        val minScoreString = minVotingSettingsEditText.text.toString().trim()
        val maxScoreString = maxVotingSettingsEditText.text.toString().trim()

        // Validation for votingOptions Number
        when {
            minScoreString.isEmpty() || maxScoreString.isEmpty() -> {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
                return
            }
            !minScoreString.isDigitsOnly() || !maxScoreString.isDigitsOnly() -> {
                Toast.makeText(this, "Please enter valid numeric values.", Toast.LENGTH_SHORT).show()
                return
            }
            else -> {
                val minScore = minScoreString.toInt()
                val maxScore = maxScoreString.toInt()
                if (minScore >= maxScore) {
                    Toast.makeText(this, "Minimum score must be less than maximum score.", Toast.LENGTH_SHORT).show()
                    return
                }
                if (minScore < 2 || maxScore > 25) {
                    Toast.makeText(this, "Scores must be between 2 and 25.", Toast.LENGTH_SHORT).show()
                    return
                }
            }
        }

        editor.putString("minScore", minScoreString)
        editor.putString("maxScore", maxScoreString)
        editor.apply()

        Toast.makeText(this, "Settings saved !", Toast.LENGTH_SHORT).show()
    }

    fun backBtn(view: View) {
        val intent: Intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}