package com.app.khavdawala.apputils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.text.InputType
import android.util.Patterns
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.khavdawala.ui.activity.SplashActivity
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.regex.Matcher
import java.util.regex.Pattern

//fun isConnected(context: Context): Boolean {
//    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//        val n = cm.activeNetwork
//        if (n != null) {
//            val nc = cm.getNetworkCapabilities(n)
//            //It will check for both wifi and cellular network
//            return nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(
//                NetworkCapabilities.TRANSPORT_WIFI
//            )
//        }
//        return false
//    } else {
//        val netInfo = cm.activeNetworkInfo
//        return netInfo != null && netInfo.isConnectedOrConnecting
//    }
//}
//
//
//fun setRecyclerViewLayoutManager(recyclerView: RecyclerView, context: Context) {
//    val layoutManager = LinearLayoutManager(context)
//    recyclerView.layoutManager = layoutManager
//    recyclerView.itemAnimator = DefaultItemAnimator()
//    recyclerView.isNestedScrollingEnabled = true
//}
//
//fun setGridLayoutManager(recyclerView: RecyclerView, context: Context, spanCount: Int) {
//    val layoutManager = GridLayoutManager(context, spanCount)
//    recyclerView.layoutManager = layoutManager
//    //recyclerView.itemAnimator = DefaultItemAnimator()
//    recyclerView.isNestedScrollingEnabled = true
//}
//
//fun TextView.showStrikeThrough(show: Boolean) {
//    paintFlags =
//        if (show) paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//        else paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
//}
//
//fun showSnackBar(message: String?, activity: Activity?) {
//    if (null != activity && null != message) {
//        hideKeyboard(activity)
//        Snackbar.make(
//            activity.findViewById(android.R.id.content),
//            message, Snackbar.LENGTH_SHORT
//        ).show()
//    }
//}
//
fun showToast(text: String, context: Context) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}
fun Context.showToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}
//
//fun hideKeyboard(activity: Activity) {
//    val imm =
//        activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
//    //Find the currently focused view, so we can grab the correct window token from it.
//    var view = activity.currentFocus
//    //If no view currently has focus, create a new one, just so we can grab a window token from it
//    if (view == null) {
//        view = View(activity)
//    }
//    imm.hideSoftInputFromWindow(view.windowToken, 0)
//}
//
//fun sessionExpired(application: Application) {
//    //SPreferenceManager.getInstance(application).clearSession()
//    val intent = Intent(application, SplashActivity::class.java)
//    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//    application.startActivity(intent)
////    Handler(application.mainLooper).post {
////        Toast.makeText(
////            application,
////            "Unauthorized! Invalid authentication details or session expired.",
////            Toast.LENGTH_SHORT
////        ).show()
////    }
//}
//
//private var pgDialog: Dialog? = null
//
//fun showProgressDialog(context: Context) {
//    if (pgDialog == null) {
//        pgDialog = getProgressDialog(context)
//        pgDialog?.show()
//    }
//}
//
//fun getProgressDialog(context: Context): Dialog? {
//    val progressDialog = Dialog(context)
//    val view: View =
//        LayoutInflater.from(context).inflate(R.layout.dialog_progress, null, false)
//    progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//    progressDialog.setContentView(view)
//    progressDialog.setCancelable(false)
//    progressDialog.setCanceledOnTouchOutside(false)
//    val window = progressDialog.window
//    if (window != null) {
//        window.setBackgroundDrawable(
//            ContextCompat.getDrawable(
//                context,
//                android.R.color.transparent
//            )
//        )
//        window.setLayout(
//            WindowManager.LayoutParams.MATCH_PARENT,
//            WindowManager.LayoutParams.MATCH_PARENT
//        )
//    }
//    return progressDialog
//}
//
//fun dismissProgressDialog() {
//    if (pgDialog != null) {
//        pgDialog?.dismiss()
//        pgDialog = null
//    }
//}
//
//@SuppressLint("SimpleDateFormat")
//fun formatDate(dateToFormat: String, inputFormat: String, outputFormat: String): String {
//    try {
//        print("Input Date Date is $dateToFormat")
//        val convertedDate: String = SimpleDateFormat(outputFormat)
//            .format(
//                SimpleDateFormat(inputFormat)
//                    .parse(dateToFormat)!!
//            )
//        print("Output Date is $convertedDate")
//
//        //Update Date
//        return convertedDate
//    } catch (e: ParseException) {
//        e.printStackTrace()
//    }
//    return ""
//}
//
//fun openBrowser(context: Context, url: String) {
//    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//    context.startActivity(browserIntent)
//}
//
//fun CharSequence.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
//
//fun String.saveTo(path: String) {
//    URL(this).openStream().use { input ->
//        FileOutputStream(File(path)).use { output ->
//            input.copyTo(output)
//        }
//    }
//}
//
//fun getStorageDir(context: Context, fileName: String): String {
//    //create folder
//    val file: File =
//        File(context.getExternalFilesDir(null)!!.absolutePath + "/patidarsamaj/files")
//    if (!file.mkdirs()) {
//        file.mkdirs()
//    }
//    return file.absolutePath + File.separator + fileName
//}
//
//fun getRootDirPath(context: Context): String? {
//    return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
//        val file: File = ContextCompat.getExternalFilesDirs(
//            context.applicationContext,
//            null
//        )[0]
//        file.absolutePath
//    } else {
//        context.applicationContext.filesDir.absolutePath
//    }
//}
//
//fun shareIntent(text: String, url: String, context: Context) {
//    val fileName: String =
//        url.substring(url.lastIndexOf('/') + 1, url.length)
//
//    val pdfFile = File(getRootDirPath(context) + "/" + fileName)
//    if (pdfFile.exists()) {
//        shareTextAndImage(context, fileName, text)
//    } else {
//        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
//        alertDialogBuilder.setCancelable(false)
//        alertDialogBuilder.setView(R.layout.layout_download_dialog)
//        alertDialogBuilder.create()
//        val alertDialog: AlertDialog = alertDialogBuilder.create()
//        alertDialog.show()
//
//        val tvTitleForShare = alertDialog.findViewById<TextView>(R.id.tvTitleForShare)
//        tvTitleForShare!!.text = context.getString(R.string.sharing)
//
//        val dirPath = getRootDirPath(context)
//
//        PRDownloader.download(
//            url,
//            dirPath, fileName
//        ).build()
//            .setOnStartOrResumeListener {
//                if (!alertDialog.isShowing) {
//                    alertDialog.show()
//                }
//            }
//            .setOnPauseListener {
//
//            }
//            .setOnCancelListener {
//                alertDialog.dismiss()
//            }
//            .setOnProgressListener {
//
//            }
//            .start(object : OnDownloadListener {
//                override fun onDownloadComplete() {
//                    alertDialog.dismiss() // to hide this dialog
//                    println("File download complete")
//                    //showToast("File download successfully", requireContext())
//                    shareTextAndImage(context, fileName, text)
//                }
//
//                override fun onError(error: com.downloader.Error?) {
//                    alertDialog.dismiss() // to hide this dialog
//                    println("PDF download error: ${error.toString()}")
//                    showToast("Sorry! File download failed.", context)
//                }
//            })
//    }
//}
//
//fun shareTextAndImage(context: Context, fileName: String, shareableText: String) {
//    val pdfFile = File(getRootDirPath(context) + "/" + fileName)
//    val fileUri = FileProvider.getUriForFile(
//        context,
//        BuildConfig.APPLICATION_ID,
//        pdfFile
//    )
//    val intent = Intent(Intent.ACTION_SEND)
//    intent.type = "image/png"
//    intent.putExtra(Intent.EXTRA_STREAM, fileUri)
//    intent.putExtra(
//        Intent.EXTRA_TEXT,
//        shareableText + "\n\nઇન્સ્ટોલ કરો પાટીદાર સૌરભ એપ - https://play.google.com/store/apps/details?id=" +
//                BuildConfig.APPLICATION_ID
//    )
//
//    context.startActivity(Intent.createChooser(intent, "Share"))
//}
//
//fun shareIntent(shareableText: String, context: Context) {
//    val txtIntent = Intent(Intent.ACTION_SEND)
//    txtIntent.type = "text/plain"
//    txtIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name))
//    txtIntent.putExtra(
//        Intent.EXTRA_TEXT, shareableText
//                + "\nDownload the app from https://play.google.com/store/apps/details?id=" +
//                BuildConfig.APPLICATION_ID
//    )
//    context.startActivity(Intent.createChooser(txtIntent, "Share"))
//}
//
//fun callIntent(activity: Activity, phoneNumber: String) {
//    if (phoneNumber.contains(",")) {
//        val phoneNumberList = phoneNumber.split(",")
//
//        val phoneNumber1 = phoneNumberList[0]
//        val phoneNumber2 = phoneNumberList[1]
//
//        val builder =
//            AlertDialog.Builder(activity)
//
//        val viewGroup = activity.findViewById<ViewGroup>(R.id.content)
//        val layout: View = LayoutInflater.from(activity)
//            .inflate(R.layout.dialog_two_phone_number, viewGroup, false)
//
//        builder.setView(layout)
//        val alertDialog = builder.create()
//        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//
//        val tvPhoneNumberOne = layout.findViewById(R.id.tvPhoneNumberOne) as TextView
//        val tvPhoneNumberTwo = layout.findViewById(R.id.tvPhoneNumberTwo) as TextView
//
//        tvPhoneNumberOne.text = phoneNumber1
//        tvPhoneNumberTwo.text = phoneNumber2
//
//        tvPhoneNumberOne.setOnClickListener {
//            val intent = Intent(Intent.ACTION_DIAL)
//            intent.data = Uri.parse("tel:$phoneNumber1")
//            activity.startActivity(intent)
//            alertDialog.dismiss()
//        }
//
//        tvPhoneNumberTwo.setOnClickListener {
//            val intent = Intent(Intent.ACTION_DIAL)
//            intent.data = Uri.parse("tel:$phoneNumber2")
//            activity.startActivity(intent)
//            alertDialog.dismiss()
//        }
//
//        alertDialog.show()
//    } else {
//        val intent = Intent(Intent.ACTION_DIAL)
//        intent.data = Uri.parse("tel:$phoneNumber")
//        activity.startActivity(intent)
//    }
//}
//
//fun browserIntent(context: Context, url: String) {
//    var webpage = Uri.parse(url)
//    if (!url.startsWith("http://") && !url.startsWith("https://")) {
//        webpage = Uri.parse("http://$url")
//    }
//    val intent = Intent(Intent.ACTION_VIEW, webpage)
//    if (intent.resolveActivity(context.packageManager) != null) {
//        context.startActivity(intent)
//    }
//}
//
//fun extractYouTubeVideoId(ytUrl: String?): String? {
//    var vId: String? = null
//    val pattern: Pattern = Pattern.compile(
//        "^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
//        Pattern.CASE_INSENSITIVE
//    )
//    val matcher: Matcher = pattern.matcher(ytUrl)
//    if (matcher.matches()) {
//        vId = matcher.group(1)
//    }
//    return vId
//}
//
//fun EditText.setMultiLineCapSentencesAndDoneAction() {
//    imeOptions = EditorInfo.IME_ACTION_DONE
//    setRawInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES or InputType.TYPE_TEXT_FLAG_MULTI_LINE)
//}
//
//fun EditText.setMultiLineCapSentencesAndNextAction() {
//    imeOptions = EditorInfo.IME_ACTION_NEXT
//    setRawInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES or InputType.TYPE_TEXT_FLAG_MULTI_LINE)
//}