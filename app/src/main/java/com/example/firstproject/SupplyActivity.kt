package com.example.firstproject

import android.os.Bundle
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.response.*
import kotlinx.android.synthetic.main.supply.*
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.os.Build
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.zarinpal.ewallets.purchase.ZarinPal
import kotlinx.android.synthetic.main.pay.*

class SupplyActivity : AppCompatActivity(){
    var price : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.supply)


        textView12.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    priceText.text = "" + (textView12.text.toString().toInt() * price) + " تومان "
                } catch (e: Exception) {
                    priceText.text = "0"
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        val intent = intent
        var carName = intent.getStringExtra("carName")
        var creationTime = intent.getStringExtra("creationTime")
        var carModel = intent.getStringExtra("carModel")
        var brandName = intent.getStringExtra("brandName")
        var carChassis = intent.getStringExtra("carChassis")
        var partName = intent.getStringExtra("partName")
        price = intent.getIntExtra("price", 0)
        priceText.text = "" + price + " تومان "

        textView2.setText(carName)
        textView3.setText(creationTime.split(" ")[0])
        textView4.setText(carModel)
        textView5.setText(brandName)
        textView6.setText(carChassis)
        textView13.setText(partName)

        var data = intent.data

        ZarinPal.getPurchase(this).verificationPayment(
            data
        ) { isPaymentSuccess, refID, paymentRequest ->
            if (isPaymentSuccess) {
                Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "unSuccessful", Toast.LENGTH_SHORT).show();
            }
        }

    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public fun purchase(v: View){
        if(textView12.text.toString().trim().equals("") || textView12.text.toString().toInt() <= 0 ){
            var toast = Toast.makeText(this,"لطفا تعداد را به درستی مشخص کنید",Toast.LENGTH_SHORT)
            toast.view.background = getDrawable(R.drawable.warning_toast)
            toast.show()
            return
        }
        if (textView10.text.toString().trim().equals("") || textView8.text.toString().trim().equals("") || textView11.text.toString().trim().equals("")){
            var toast = Toast.makeText(this,"لطفا فیلد های ستاره دار را پر نمایید",Toast.LENGTH_SHORT)
            toast.view.background = getDrawable(R.drawable.warning_toast)
            toast.show()
            return
        }

        if (textView13.text.toString().trim().equals("")){
            var toast = Toast.makeText(this,"فیلد کالای درخواستی نمی تواند خالی باشد",Toast.LENGTH_SHORT)
            toast.view.background = getDrawable(R.drawable.warning_toast)
            toast.show()
        }

        popup()

    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun popup(){
        val li = LayoutInflater.from(this)
        val promptsView: View = li.inflate(R.layout.pay, null)

        val alertDialogBuilder = AlertDialog.Builder(
            this
        )

        // set prompts.xml to alertdialog builder

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView)

        val rg = promptsView
            .findViewById<View>(R.id.radioGroup) as RadioGroup
        val txt = promptsView
            .findViewById<View>(R.id.address) as EditText
        val rb = promptsView
            .findViewById<View>(R.id.radioButton2) as RadioButton
        val payButton = promptsView
            .findViewById<View>(R.id.pay_btn) as Button


        val alertDialog = alertDialogBuilder.create()

        // show it
        alertDialog.show()
    }

    fun payment(amount: Long) {
        var purchase = ZarinPal.getPurchase(this)
        var payment = ZarinPal.getPaymentRequest()
        payment.merchantID = "*************"
        payment.amount = amount
        Log.e(amount.toString(), "amount")
        payment.description = "تست برنامه"
        payment.setCallbackURL("return://zarinpalpayment")

        purchase.startPayment(payment) { status, authority, paymentGatewayUri, intent ->
            if (status == 100) {
                startActivity(intent)
            } else {
//                Toast.makeText(this, "خطا در ایجاد درخواست", Toast.LENGTH_SHORT).show()
                Toast.makeText(this, authority, Toast.LENGTH_SHORT).show()
            }
        }
    }

    inner class Request(var context: Context): AsyncTask<String, String, String>(){
        var pd = ProgressDialog(context)
        override fun onPreExecute() {
            pd.show()
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: String?): String {
            payment(textView12.text.toString().toLong() * price.toLong())
            pd.dismiss()
            return ""
        }


    }
}
