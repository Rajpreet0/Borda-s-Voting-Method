package de.fra_uas.fb2.mobiledevices.bordasvotingmethodapp1

import android.content.Intent
import android.os.Bundle
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

    // Function to add a single vote
    fun confirmBtn(view: View) {
        val intent: Intent = Intent(view.context, MainActivity::class.java).apply {
            putExtra("updatedVoteCount",  intent.getIntExtra("voteCount", 0))       // Pass the Votes Count back to the User
        }
        view.context.startActivity(intent)

        finish()
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