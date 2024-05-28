package de.fra_uas.fb2.mobiledevices.bordasvotingmethodapp1

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {

    private lateinit var votingOptions: EditText
    private lateinit var numOptions: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Connect to XML
        numOptions = findViewById(R.id.numOptionsEditTxt);
        votingOptions = findViewById(R.id.votingOptionsEditTxt);


        // Check number when Focus Changes
        numOptions.setOnFocusChangeListener { view, hasFocus ->
            if(!hasFocus) {
                validateNumOption();
            }
        }

    }

    // Function for Number Checking
    private fun validateNumOption() {
        val numOptionsText = numOptions.text.toString();

        // If the Field is not Empty then check Condition
        if (numOptionsText.isNotEmpty()){
            val numOptionsNum = numOptionsText.toInt()
            if(numOptionsNum < 2 ) {
                numOptions.setText("2")
            }else if(numOptionsNum > 10) {
                numOptions.setText("10")
            }
        }
    }

    private fun createVotingOptionsArray(num: Int): ArrayList<String> {

        // Putting every Value into an ArrayList
        val votingOptionsArrayList: ArrayList<String> = votingOptions.text.toString()
                                                            .split(",")             // Each element after comma
                                                            .map { it.trim() }                // Trim white spaces
                                                            .take(num)              // Limit array to Options Number
                                                            .toCollection(ArrayList())        // Convert to ArrayList

        return votingOptionsArrayList
    }

    fun addVote(view: View) {

        // If "Number Of Options" is Empty set 3 into it
        if(numOptions.text.toString().isEmpty()) {
            numOptions.setText("3")
        }

        val numOptionsNum = numOptions.text.toString().toInt()


        Log.d("ArrayList: ", createVotingOptionsArray(numOptionsNum).toString())


    }


}