package id.cbm.main.cbm_calculator.utils

import android.app.DownloadManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import id.cbm.main.cbm_calculator.ui.engineer.form.RequestFormActivity
import java.io.File
import java.io.FileInputStream
import java.lang.Exception

object SavingFileHelper {

    fun downloadManagerAndSaveFile(context: Context, fileUrl: String, fileName: String, doAction: (Long, DownloadManager) -> Unit) {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val uri = Uri.parse(fileUrl)

        val request = DownloadManager.Request(uri).apply {
            setTitle("Downloading $fileName")
            setDescription("Please wait while the file is being downloaded.")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        }

        // Enqueue th edownload request
        val downloadID = downloadManager.enqueue(request)

        // Listen for completion and wait for download completion
        doAction(downloadID, downloadManager)
    }

    fun openChooserPdf(context: Context, filePdf: File) {
        if (!filePdf.exists()) {
            Toast.makeText(context, "File Pdf not exist!", Toast.LENGTH_SHORT).show()
            Log.e(RequestFormActivity::class.simpleName, "moses_check File Pdf not exist! ${filePdf.path}")
            return
        }

        val uri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            filePdf,
        )

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Grant read permission
        }

        val chooserIntent = Intent.createChooser(intent, "Open PDF with")

        try {
            context.startActivity(chooserIntent)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "intent chooser err: ${e.message}", Toast.LENGTH_SHORT).show()

            // Optionally, redirect the user to the Play Store
            val storeIntent = Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pdf+viewer"))
            if (storeIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(storeIntent)
            }
        }
    }
}