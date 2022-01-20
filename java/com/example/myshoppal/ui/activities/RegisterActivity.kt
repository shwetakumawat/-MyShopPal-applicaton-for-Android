package com.example.myshoppal.ui.activities

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.example.myshoppal.R
import com.example.myshoppal.models.User
import com.example.myshoppal.firestore.FirestoreClass
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_login.et_email as et_email1
import kotlinx.android.synthetic.main.activity_login.et_password as et_password1

class RegisterActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        setUpActionBar()

        tv_login.setOnClickListener {
            onBackPressed()
        }

        btn_register.setOnClickListener{
            registerUser()
        }
    }

    private fun setUpActionBar() {

        setSupportActionBar(toolbar_register_activity)

        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow)
        }

        toolbar_register_activity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun validateRegisterDetails(): Boolean {
        return when {
            TextUtils.isEmpty(et_first_name.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Please enter first name.", true)
                false
            }
            TextUtils.isEmpty(et_last_name.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Please enter last name.", true)
                false
            }
            TextUtils.isEmpty(et_email.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Please enter an email id.", true)
                false
            }
            TextUtils.isEmpty(et_password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Please enter a password.", true)
                false
            }
            TextUtils.isEmpty(et_confirm_password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Please enter confirm password.", true)
                false
            }
            et_password.text.toString()
                .trim { it <= ' ' } != et_confirm_password.text.toString()
                .trim { it <= ' ' } -> {
                showErrorSnackBar("Password and confirm password does not match", true)
                false
            }
            !cb_terms_and_condition.isChecked -> {
                showErrorSnackBar("Please agree terms and conditions.", true)
                false
            }
            else -> {
                //showErrorSnackBar("Registration successfull.", false)
                true
            }

        }
    }

    private fun registerUser() {

        if (validateRegisterDetails()) {

            showProgressDialog(getString(R.string.please_wait))

            val email: String = et_email.text.toString().trim { it <= ' ' }
            val password: String = et_password.text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->

                        if (task.isSuccessful) {

                            val firebaseUser: FirebaseUser = task.result!!.user!!

                            val user = User(
                                firebaseUser.uid,
                                et_first_name.text.toString().trim { it <= ' ' },
                                et_last_name.text.toString().trim { it <= ' ' },
                                et_email.text.toString().trim { it <= ' ' }
                            )

                            FirestoreClass().registerUser(this@RegisterActivity, user)

                            //FirebaseAuth.getInstance().signOut()
                            //finish()

                        } else {
                            hideProgressDialog()
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    })
        }
    }

    fun userRegistrationSuccess() {
        hideProgressDialog()
        Toast.makeText(this@RegisterActivity, "You are registered successfully", Toast.LENGTH_SHORT)
            .show()
    }


}
