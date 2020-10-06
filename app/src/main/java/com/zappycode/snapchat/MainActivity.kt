package com.zappycode.snapchat

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import android.R.attr.password
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.widget.Button
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var emailEditText: EditText? = null
    var passwordEditText: EditText? = null
    val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        val mAlertDialogBtn = findViewById<Button>(R.id.buttonAboutDevs)
        buttonAboutDevs.setOnClickListener{
            val mAlertDialog =AlertDialog.Builder(this@MainActivity)
            //dev alert box title
            mAlertDialog.setTitle("Welcome to the Snapchat Clone")
            //dev alert box message
            mAlertDialog.setMessage("This app is developed by "+ "\n" + "Amol Vyavaharkar (MC1958)"
                    + "\n" + "Varsha Kamble (MC1919)"
            )
            mAlertDialog.setIcon(R.mipmap.ic_launcher)
            mAlertDialog.setNeutralButton("Cancel") { dialogInterface: DialogInterface, i: Int ->
                Toast.makeText(this@MainActivity, "Cancel", Toast.LENGTH_SHORT).show()
            }
            mAlertDialog.show()
        }
        if (mAuth.currentUser != null) {
            logIn()
        }
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Are you sure?")
        builder.setMessage("Do you want to close the app?")
        builder.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
            finish()
        }
        builder.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int -> }
        builder.show()
    }

    fun goClicked(view: View) {
        // Check if we can log in the user
        mAuth.signInWithEmailAndPassword(emailEditText?.text.toString(), passwordEditText?.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        logIn()
                    } else {
                        // Sign up the user
                        mAuth.createUserWithEmailAndPassword(emailEditText?.text.toString(), passwordEditText?.text.toString()).addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                FirebaseDatabase.getInstance().getReference().child("users").child(task.result.user.uid).child("email").setValue(emailEditText?.text.toString())
                                logIn()
                            } else {
                                Toast.makeText(this,"Login Failed. Try Again.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
    }

    fun logIn() {
        // Move to next Activity
        val intent = Intent(this, SnapsActivity::class.java)
        startActivity(intent)
    }
}
