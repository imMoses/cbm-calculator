package id.cbm.main.cbm_calculator.utils

import android.app.DownloadManager
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.io.File
import java.io.FileInputStream

object SavingFileHelper {

    fun savePdfToDownloads(
        context: Context,
        sourceFile: File,
        fileName: String,
    ): Boolean {

        val resolver = context.contentResolver

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName) // File name
            put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf") // MIME type
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS) // Directory
        }

        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

        return if (uri != null) {
            resolver.openOutputStream(uri)?.use { outputStream ->
                try {
//                    outputStream.write(fileContent)
                    FileInputStream(sourceFile).use { inputStream ->
                        inputStream.copyTo(outputStream)
                        Log.w("savePdfToDownlods", "file successfully saved into Download path! | ${fileName}")
                    }
                    true // Successfully saved
                } catch (e: Exception) {
                    e.printStackTrace()
                    false // Failed to save
                }
            } ?: false // Failed to open output stream
        } else {
            false // Failed to create URI
        }
    }

    fun downloadManagerAndSaveFile(context: Context, fileUrl: String, fileName: String, doAction: (Long, DownloadManager) -> Unit) {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val uri = Uri.parse(fileUrl)

        val request = DownloadManager.Request(uri).apply {
            setTitle("Downloading $fileName")
            setDescription("Please wait while the file is being downloaded.")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "$fileName.pdf")
        }

        // Enqueue th edownload request
        val downloadID = downloadManager.enqueue(request)

        // Listen for completion and wait for download completion
        doAction(downloadID, downloadManager)
    }
}