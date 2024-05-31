package de.fra_uas.fb2.mobiledevices.bordasvotingmethodapp1

import android.content.Intent
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
import java.util.ArrayList

class VotingActivity : AppCompatActivity() {
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

    private fun generateSeekBars() {
        val bundle = intent.extras
        val votingOptionsList: ArrayList<String>? = bundle?.getStringArrayList("votingOptionsArray")
        val seekBarLinearLayout: LinearLayout = findViewById<LinearLayout>(R.id.seekBarLinearLayout);

        seekBarLinearLayout.removeAllViews()

        for (i in 0 until votingOptionsList!!.size){
            // TextView Generation
            val label = TextView(this)
            label.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            // Set Seekbar to Names in ArrayList
            label.text = votingOptionsList[i]

            // Seekbar Generation
            val seekBar = SeekBar(this)
            seekBar.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            // Initial set of Seekbar
            seekBar.progress = 0;

            seekBarLinearLayout.addView(label)
            seekBarLinearLayout.addView(seekBar)
        }
    }

    // Function to add a single vote
    fun confirmBtn(view: View) {
        shareData();
    }

    // Function to cancel a single vote
    fun cancelBtn(view: View) {
        val intent: Intent = Intent(view.context, MainActivity::class.java).apply {
            putExtra("updatedVoteCount",  (intent.getIntExtra("voteCount", 0)-1))   // Pass the Votes Count back to the User but decreased
        }
        view.context.startActivity(intent)

        finish()
    }
}