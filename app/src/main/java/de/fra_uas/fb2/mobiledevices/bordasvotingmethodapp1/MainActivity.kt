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
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text


class MainActivity : AppCompatActivity() {

    private lateinit var activityLauncher: ActivityResultLauncher<Intent>
    private lateinit var votingOptions: EditText
    private lateinit var numOptions: EditText
    private lateinit var numOfVotesTxt: TextView
    private lateinit var votingResultSwitch: SwitchCompat
    private lateinit var resultText: TextView
    private lateinit var votingResultsScore: ArrayList<Int>
    private lateinit var votingOptionsArray: ArrayList<String>
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
        votingResultSwitch = findViewById<SwitchCompat>(R.id.votingResultSwitch)
        resultText = findViewById<TextView>(R.id.resultTextTxt)

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

                    // Safely retrieve the scores ArrayList
                    val scores = extras.getIntegerArrayList("scores")
                    if (scores != null) {
                        if (!::votingResultsScore.isInitialized || votingResultsScore.isEmpty()){
                            votingResultsScore = scores
                        }else {
                            votingResultsScore = ArrayList(votingResultsScore.zip(scores) { old, new -> old + new})
                        }
                    }

                    // Set SwitchCompact to unchecked
                    votingResultSwitch.isChecked = false
                    resultText.text = ""

                    checkUserChangeValidation()
                    isResetting = true
                }
            } else if (result.resultCode == RESULT_CANCELED) {
            }
        }

        showResult()
    }

    // Function to show the Results from Voting
    private fun showResult() {
        votingResultSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val resultBuilder = StringBuilder()
                // Determine the maximum score for highlighting
                val maxScore = votingResultsScore.maxOrNull()

                // Iterate over the voting options and their scores
                for ((index, score) in votingResultsScore.withIndex()) {
                    if (index < votingOptionsArray.size) {
                        val label = votingOptionsArray[index]
                        // Append each line with the format "label -> score"
                        // Highlight with stars if it is the maximum score
                        if (score == maxScore) {
                            resultBuilder.append("**** $label -> $score ****\n")
                        } else {
                            resultBuilder.append("$label -> $score\n")
                        }
                    }
                }
                resultText.text = resultBuilder.toString()
            } else {
                resultText.text = ""
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
            return votingOptionsArrayList.take(num);
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
        isResetting = true  // Set to true to prevent trigger from text changes during reset

        // Reset all text fields and scores
        numOfVotesTxt.text = "0"
        votingResultsScore.clear()
        votingOptionsArray.clear()

        // Reset UI elements
        votingResultSwitch.isChecked = false
        resultText.text = ""

        // Show a reset message to the user
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()

        isResetting = false  // Reset complete, allow text changes to trigger
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

        // Convert List into ArrayList
        votingOptionsArray = createVotingOptionsArray(numOptionsNum).toCollection(ArrayList<String>())

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

