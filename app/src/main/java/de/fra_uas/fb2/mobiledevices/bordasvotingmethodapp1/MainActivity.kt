package de.fra_uas.fb2.mobiledevices.bordasvotingmethodapp1

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {

    private lateinit var activityLauncher: ActivityResultLauncher<Intent>
    private lateinit var votingOptions: EditText
    private lateinit var numOptions: EditText
    private lateinit var numOfVotesTxt: TextView
    private var isResetting = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Connect to XML
        numOptions = findViewById<EditText>(R.id.numOptionsEditTxt);
        votingOptions = findViewById<EditText>(R.id.votingOptionsEditTxt);
        numOfVotesTxt = findViewById<TextView>(R.id.numOfVotesTxt);

        // Check number when Focus Changes
        numOptions.setOnFocusChangeListener { view, hasFocus ->
            if(!hasFocus) {
                validateNumOption();
            }
        }

        activityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result ->
            if(result.resultCode == RESULT_OK){
                val extras = result.data?.extras
                if(extras != null) {
                    isResetting = false
                    // Update the Text for the Elements
                    numOfVotesTxt.setText(extras.getInt("updatedVoteCount").toString())
                    numOptions.setText(extras.getString("numOptionsUpdated"))
                    votingOptions.setText(extras.getString("votingOptionsUpdated"))

                    checkUserChangeValidation()
                    isResetting = true
                }
            } else if (result.resultCode == RESULT_CANCELED) {
            }
        }


    }


    // Function for validating the Number Options
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

    // Function for validating the Options
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

    // Reset Function
    private fun reset(message: String) {
        isResetting= false
        votingOptions.setText("");
        numOptions.setText("");
        numOfVotesTxt.text = "0";
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    // Function to check wether the user changes the settings
    private fun checkUserChangeValidation() {
        numOptions.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(numOfVotesTxt.text.toString().toInt() != 0 && isResetting ) {
                    reset("All votes resetted!")
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
        votingOptions.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (numOfVotesTxt.text.toString().toInt() != 0 && isResetting) {
                    reset("All votes resetted!")
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    fun addVote(view: View) {
        // If "Number Of Options" is Empty set 3 into it
        if(numOptions.text.toString().isEmpty()) {
            numOptions.setText("3")
        }

        // Convert Num Options to an Integer to pass onto ArrayList
        val numOptionsNum = numOptions.text.toString().toInt()
        Log.d("ArrayList: ", createVotingOptionsArray(numOptionsNum).toString())

        // Convert List into ArrayList
        val votingOptionsArray: ArrayList<String> = createVotingOptionsArray(numOptionsNum).toCollection(ArrayList<String>())

        // Update Votes Count to 1
        val numOfVotes: Int = numOfVotesTxt.text.toString().toInt()+1


        val intent = Intent(this, VotingActivity::class.java)
        val bundle = Bundle();
        bundle.putInt("voteCount", numOfVotes)
        bundle.putString("numOptions", numOptions.text.toString())
        bundle.putString("votingOptions",  votingOptions.text.toString())
        bundle.putStringArrayList("votingOptionsArray", votingOptionsArray)
        intent.putExtras(bundle)
        activityLauncher.launch(intent)
    }

    fun startOverBtn(view: View) {
        reset("Starting anew!")
    }
}

