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

    private fun createVotingOptionsArray(num: Int): List<String> {


        val rawInput = votingOptions.text.toString()

        val votingOptionsArrayList: MutableList<String> = if (rawInput.isBlank()) {
            mutableListOf() // Create an empty MutableList if the input is blank
        } else {
            rawInput.split(",")         // Splits the strings into substrings
                    .map { it.trim() }             // Removes leading and trailing whitespaces on every item in the list
                    .filter { it.isNotEmpty() }    // Filter Function removes any empty strings from the list
                    .toMutableList()               // Conversion to MutableList
        }

        // Getting the Size of the Array
        val votingOptionsArrayListSize: Int = votingOptionsArrayList.size


        if(votingOptionsArrayListSize > num) {
            // Take only as much as the User specified
            votingOptionsArrayList.take(num);
        } else if(votingOptionsArrayListSize < num) {
            // Fill the Array with "Option i", when less Options are specified

            for (i in votingOptionsArrayListSize until num) {
                votingOptionsArrayList.add("Option ${i + 1}")
            }
        }

        return votingOptionsArrayList;
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