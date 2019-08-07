package com.uzoebere.gasify.distributors

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.parse.*
import com.uzoebere.gasify.R
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_payment_request.*
import java.util.HashMap

class PaymentRequest : AppCompatActivity() {

    var currentBal = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_request)

        val textBalance = findViewById<TextView>(R.id.textBalance)
        val textAmount = findViewById<EditText>(R.id.textAmount)
        val textBank = findViewById<EditText>(R.id.textBank)
        val textAccount = findViewById<EditText>(R.id.textAccount)

        val currentUser = ParseUser.getCurrentUser()
        val refCode = currentUser.getString("referralCode")
        val query = ParseQuery.getQuery<ParseObject>("Distributors")
        // Retrieve the object by id
        query.getInBackground(refCode) { entity, e ->

            if (e == null) {
            //    entity.put("currentBalance", finalCurrentBalance)
             //   entity.saveInBackground()
                currentBal = entity.get("currentBalance") as Int
            }
            val mText = "Current Balance: N" + currentBal
            val mBSpannableString = SpannableString(mText)
            val mBold = StyleSpan(Typeface.BOLD)
            mBSpannableString.setSpan(mBold, 17, mText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            textBalance.text = mBSpannableString
        }


        buttonSubmit.setOnClickListener{

            val amount1 = textAmount.text.toString()
            val accountName = textBank.text.toString()
            val accountNo = textAccount.text.toString()

            var validationError = false
            val validationErrorMessage = StringBuilder("Please, insert ")

            if (amount1.isEmpty()) {
                //validationError = true
                validationErrorMessage.append("the amount")
                return@setOnClickListener
            }

            if (accountName.isEmpty() ) {
                if (validationError) {
                    validationErrorMessage.append(" and ")
                }
                validationError = true
                validationErrorMessage.append("your account name")
            }

            if (accountNo.isEmpty()) {
                if (validationError) {
                    validationErrorMessage.append(" and ")
                }
                validationError = true
                validationErrorMessage.append("your account number")
            }
           if (java.lang.Double.parseDouble(amount1) > currentBal ) {
                if (validationError) {
                    validationErrorMessage.append(" and ")
                }
                validationError = true
                validationErrorMessage.append("an amount lower or equal to your current balance")
            }
            validationErrorMessage.append(".")

            if (validationError) {
                Toasty.warning(this@PaymentRequest, validationErrorMessage.toString(), Toast.LENGTH_LONG, true).show()
             //   progressBar5.visibility = View.GONE
                return@setOnClickListener
            }
            val amount = java.lang.Double.parseDouble(textAmount.text.toString())
            val newBalance = currentBal - amount

            val newPayment = ParseObject("PaymentRequests")
            newPayment.put("distObjectId", currentUser)
            newPayment.put("Distributor", currentUser.objectId)
            newPayment.put("amount", amount)
            newPayment.put("approvalStatus", "Pending")
            newPayment.put("paymentStatus", "Pending")
            newPayment.put("currentBalance", newBalance)
            newPayment.put("bankName", accountName)
            newPayment.put("accountNumber", accountNo)
            newPayment.saveInBackground { e ->
                if (e == null) {
                    // Show toast to display message sent and close the activity
                    Toast.makeText(this@PaymentRequest, "Your Request Has Been Sent.", Toast.LENGTH_SHORT).show()
                    // Send an email to the admin

                    val fromEmailAddress = currentUser.email
                    val toEmailAddress = "kingfrankhenshaw@gmail.com"
                    val emailSubject = "New Distributor Payment Request"
                    val emailBody =
                        "A new payment request has been made. Kindly find details about it below.\nKind Regards. \n\nCustomer Name: " + currentUser.getString("name") + "\nAccount Name: " + accountName +
                                "\nAccount Number: " + accountNo + "\nAmount: N" + amount

                    val params = HashMap<String, String>()
                    params["fromEmail"] = fromEmailAddress
                    params["toEmail"] = toEmailAddress
                    params["subject"] = emailSubject
                    params["body"] = emailBody

                    ParseCloud.callFunctionInBackground("sendEMail", params,
                        FunctionCallback<Any> { _, exc ->
                            if (exc == null) {
                                // The function executed, but still has to check the response
                             //   Toast.makeText(this@PaymentRequest, "Order Received Successfully. Check your inbox", Toast.LENGTH_SHORT).show()
                            } else {
                                // Something went wrong
                             //   Toast.makeText(this@PaymentRequest, "Request Not Sent.", Toast.LENGTH_SHORT).show()
                            }
                        })

                    // Send an email to the distributor
                    val fromEmailAddress1 = "kingfrankhenshaw@gmail.com"
                    val toEmailAddress1 = currentUser.email
                    val emailSubject1 = "Payment Request Received"
                    val emailBody1 = "Dear " + currentUser.getString("name") +
                            ",\n\nYour request has been received and will be sorted within 2-3 days. Kindly take this email as a confirmation of your request.\n\nKind Regards.\nThe Gasify Team"


                    val params1 = HashMap<String, String>()
                    params1["fromEmail"] = fromEmailAddress1
                    params1["toEmail"] = toEmailAddress1
                    params1["subject"] = emailSubject1
                    params1["body"] = emailBody1

                    ParseCloud.callFunctionInBackground("sendEMail", params1,
                        FunctionCallback<Any> { _, exc ->
                            if (exc == null) {
                                // The function executed, but still has to check the response
                                Toasty.success(this@PaymentRequest, "Request Sent Successfully. Check your inbox", Toast.LENGTH_SHORT, true).show()
                            } else {
                                // Something went wrong
                                Toasty.error(this@PaymentRequest, "Request Not Sent.", Toast.LENGTH_SHORT, true).show()
                            }
                        })
                } else {
                    // Show toast for the user to try again
                    Toasty.error(this@PaymentRequest, "Oops! There seems to be a network problem. Please try again.", Toast.LENGTH_SHORT).show()
                    return@saveInBackground
                }

            }
            finish()
        }

        closeImageView.setOnClickListener{
            finish()
        }



    }


}


