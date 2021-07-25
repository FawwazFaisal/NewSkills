package com.omnisoft.newskills

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.internal.FacebookInitProvider
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.omnisoft.newskills.databinding.FirebaseLoginActivityBinding


class FirebaseLogin : AppCompatActivity(), View.OnClickListener {

    lateinit var bd: FirebaseLoginActivityBinding
    lateinit var googleActivityCallback : ActivityResultLauncher<Intent>
    lateinit var facebookActivityCallback : ActivityResultLauncher<Intent>
    val callbackManager = CallbackManager.Factory.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bd = FirebaseLoginActivityBinding.inflate(layoutInflater)
        setContentView(bd.root)
        setListeners()
    }

    private fun signInGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()
        val client : GoogleSignInClient = GoogleSignIn.getClient(this,gso)
        googleActivityCallback.launch(client.signInIntent)
    }

    private fun signInFacebook(){
        facebookActivityCallback.launch(Intent(this,FacebookActivity::class.java))
    }

    private fun authenticateWithToken(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken!!,null)
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser != null) {
            Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setListeners() {
        bd.btnLogin.setOnClickListener(this)
        bd.btnCreateAccount.setOnClickListener(this)
        bd.btnGoogle.setOnClickListener(this)
        bd.btnSignUp.setOnClickListener(this)
        bd.btnFacebook.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                Toast.makeText(this@FirebaseLogin, "Welcome!", Toast.LENGTH_SHORT).show()
            }

            override fun onCancel() {
                Toast.makeText(this@FirebaseLogin, "Problem logging in", Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: FacebookException?) {
                Toast.makeText(this@FirebaseLogin, "Try again later", Toast.LENGTH_SHORT).show()
            }
        })
        bd.btnFacebook.setPermissions("email","public_profile")
        googleActivityCallback = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode== RESULT_OK){
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                if(task.isSuccessful){
                    authenticateWithToken(task.result.idToken)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== RESULT_OK){
            callbackManager.onActivityResult(0,resultCode,data)
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            bd.btnCreateAccount.id -> {
                if (bd.signUpLayout.isVisible) {
                    bd.signUpLayout.visibility = View.GONE
                    bd.btnCreateAccount.text = "Create Account"
                } else {
                    bd.signUpLayout.visibility = View.VISIBLE
                    bd.btnCreateAccount.text = "Already have an account"
                }
            }
            bd.btnGoogle.id -> {
                signInGoogle()
            }
        }
    }
}