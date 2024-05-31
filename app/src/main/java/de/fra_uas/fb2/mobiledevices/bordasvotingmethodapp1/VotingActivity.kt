package de.fra_uas.fb2.mobiledevices.bordasvotingmethodapp1

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Collections
import kotlin.collections.ArrayList

class VotingActivity : AppCompatActivity() {

    private lateinit var seekBarValues: ArrayList<Int>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voting)

        generateSeekBars()
    }
    private fun shareData(){
        val result = Intent()
        val bundle = intent.extras

        result.putExtra("updatedVoteCount", bundle!!.getInt("voteCount"))               // Pass the Votes Count back to the User
        result.putExtra("numOptionsUpdated", bundle.getString("numOptions"))            // Pass numOptions for persistent data
        result.putExtra("votingOptionsUpdated", bundle.getString("votingOptions"))      // Pass votingOptions for persistent data

        setResult(RESULT_OK, result)
        finish()
    }

    //TODO: ArrayList is not cut off

    // Display results of the seek bars
    private fun seekBarResultDisplay(votingOptionsList: ArrayList<String>) {
        val displayLinearLayout: LinearLayout = findViewById<LinearLayout>(R.id.seekBarValueLinearLayout)


        displayLinearLayout.removeAllViews()

        // Iterating through the list list of voting options
        for(i in votingOptionsList.indices) {
            val resultTextView = TextView(this)

            resultTextView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            // Set the text to display the current value of the seek bar corresponding to each option
            resultTextView.text = "${votingOptionsList[i]} -> ${seekBarValues[i]}"
            // Text Styles
            resultTextView.textSize = 16f
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
        shareData();
    }

    // Function to cancel a single vote
    // TODO: Fix the Cancel Button
    fun cancelBtn(view: View) {
        val intent: Intent = Intent(view.context, MainActivity::class.java).apply {
            putExtra("updatedVoteCount",  (intent.getIntExtra("voteCount", 0)-1))   // Pass the Votes Count back to the User but decreased
        }
        view.context.startActivity(intent)

        finish()
    }
}