package de.fra_uas.fb2.mobiledevices.bordasvotingmethodapp1

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Collections
import kotlin.collections.ArrayList

class VotingActivity : AppCompatActivity() {

    private lateinit var seekBarValues: ArrayList<Int>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voting)

        generateSeekBars()
    }

    // Function to pass Data to MainActivity
    private fun shareData(cancel: Boolean){
        val result = Intent()
        val bundle = intent.extras

        result.putExtra("numOptionsUpdated", bundle?.getString("numOptions"))            // Pass numOptions for persistent data
        result.putExtra("votingOptionsUpdated", bundle?.getString("votingOptions"))      // Pass votingOptions for persistent data

        if(cancel) {
            Toast.makeText(this, "Vote canceled!", Toast.LENGTH_SHORT).show()
            result.putExtra("updatedVoteCount", bundle!!.getInt("voteCount")-1)    // Pass the Votes Count back to the User decrased
        }else {
            val votingOptionsList = bundle?.getStringArrayList("votingOptionsArray")
            val scores = if (votingOptionsList != null) calculateBordaCount(votingOptionsList) else ArrayList()

            result.putExtra("updatedVoteCount", bundle!!.getInt("voteCount"))            // Pass the Votes Count back to the User
            result.putIntegerArrayListExtra("scores", scores)

        }

        setResult(RESULT_OK, result)
        finish()
    }

    // Function to identify Seekbars which have the same Value
    private fun getNotUnqiueValues(): Set<Int> {
        // Create a map to count occurrences of each seekBar value
        val valueCount = HashMap<Int, Int>()
        seekBarValues.forEach{value ->
            valueCount[value] = valueCount.getOrDefault(value, 0) +1
        }

        return valueCount.filter { it.value > 1 }.keys
    }

    // Function to calculate the Borda Voting Values of SeekBars
    private fun calculateBordaCount(votingOptionsList: ArrayList<String>): ArrayList<Int> {
        val scores = ArrayList<Int>(Collections.nCopies(votingOptionsList.size, 0))
        val sortedIndices = seekBarValues.indices.sortedByDescending { seekBarValues[it] }
        sortedIndices.forEachIndexed { index, position ->
            scores[position] = (votingOptionsList.size - 1 - index)
        }
        return scores
    }

    // Display results of the seek bars
    private fun seekBarResultDisplay(votingOptionsList: ArrayList<String>) {
        val displayLinearLayout: LinearLayout = findViewById<LinearLayout>(R.id.seekBarValueLinearLayout)
        displayLinearLayout.removeAllViews()

        // Calculate Borda scores
        val scores = calculateBordaCount(votingOptionsList)

        // Determine non-unique values
        val nonUniqueValues = getNotUnqiueValues()

        // Iterating through the list of voting options
        for(i in votingOptionsList.indices) {
            val resultTextView = TextView(this)

            resultTextView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )

            val option = votingOptionsList[i]
            val score = scores[i]
            val value = seekBarValues[i]

            // Check if the current value is not unique
            val displayText = if (value in nonUniqueValues) {
                "$option -> <not unique>"
            } else {
                "$option -> $score points"
            }

            // Set the text to display the current value of the seek bar corresponding to each option
            resultTextView.text =  displayText
            // Text Styles
            resultTextView.textSize = 16f
            resultTextView.textAlignment = View.TEXT_ALIGNMENT_CENTER
            resultTextView.setTextColor(Color.BLACK)
            displayLinearLayout.addView(resultTextView)
        }
    }

    // Function to generate SeekBars for each voting option
    private fun generateSeekBars() {
        val bundle = intent.extras
        val votingOptionsList: ArrayList<String>? = bundle?.getStringArrayList("votingOptionsArray")

        // Initializing an ArrayList to store the seek bar values with default 0s for each voting option
        seekBarValues = ArrayList<Int>(Collections.nCopies(votingOptionsList?.size ?: 0, 0))
        val seekBarLinearLayout: LinearLayout = findViewById<LinearLayout>(R.id.seekBarLinearLayout);

        seekBarLinearLayout.removeAllViews()

        // Iterate over each voting Option
        votingOptionsList?.forEachIndexed { index, option ->
            // TextView Generation
            val label = TextView(this)
            label.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            // Set Seekbar to Names in ArrayList
            label.text = option

            // Seekbar Generation
            val seekBar = SeekBar(this)
            seekBar.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            // Setting the initial progress of the seek bar
            seekBar.progress = seekBarValues[index];

            // Listener to handle changes in seek bar
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    // Updating seekbar value
                    seekBarValues[index] = progress
                    // Display the Result of the Seekbar when user changes
                    seekBarResultDisplay(votingOptionsList)
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}

            })


            seekBarLinearLayout.addView(label)
            seekBarLinearLayout.addView(seekBar)
        }
    }

    // Function to add a single vote
    fun confirmBtn(view: View) {
        if(getNotUnqiueValues().isNotEmpty()){
            Toast.makeText(this, "Values are not unique", Toast.LENGTH_SHORT).show()
        }else {
            shareData(false)
        }
    }

    // Function to cancel a single vote
    fun cancelBtn(view: View) {
        shareData(true)
    }
}