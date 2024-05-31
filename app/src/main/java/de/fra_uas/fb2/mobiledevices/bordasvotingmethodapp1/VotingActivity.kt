package de.fra_uas.fb2.mobiledevices.bordasvotingmethodapp1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class VotingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voting)

    }
    private fun shareData(){
        val result = Intent()
        val bundle = intent.extras

        result.putExtra("updatedVoteCount", bundle!!.getInt("voteCount"))               // Pass the Votes Count back to the User
        result.putExtra("numOptionsUpdated", bundle.getString("numOptions"))            // Pass numOptions for persistent data
        result.putExtra("votingOptionsUpdated", bundle.getString("votingOptions"))      // Pass votingOptions for persistent data
        setResult(RESULT_OK, result)
        Log.d("Data:", result.toString())
        finish()
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