package net.kelmer.correostracker.ocr

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import com.googlecode.tesseract.android.TessBaseAPI
import java.io.File
import android.os.Environment.getExternalStorageDirectory
import timber.log.Timber
import java.io.FileOutputStream
import java.io.IOException


class TessOCR(val context: Context) {

    private val tess : TessBaseAPI by lazy { TessBaseAPI() }


    private val DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/TesseractSample/"
    private val TESSDATA = "tessdata"


    init {

        prepareTesseract();
        tess.init(DATA_PATH, "eng")
    }

    private fun prepareTesseract() {
        try {
            prepareDirectory(DATA_PATH + TESSDATA)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        copyTessDataFiles(TESSDATA)
    }


    /**
     * Prepare directory on external storage
     *
     * @param path
     * @throws Exception
     */
    private fun prepareDirectory(path: String) {

        val dir = File(path)
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Timber.e("ERROR: Creation of directory $path failed, check does Android Manifest have permission to write to external storage.")
            }
        } else {
            Timber.i( "Created directory $path")
        }
    }

    fun scanImage(image : Bitmap) : String {
        tess.setImage(image)

        return tess.utF8Text
    }

    fun scanImage(file: File) : String {
        tess.setImage(file)

        return tess.utF8Text
    }

    /**
     * Copy tessdata files (located on assets/tessdata) to destination directory
     *
     * @param path - name of directory with .traineddata files
     */
    private fun copyTessDataFiles(path: String) {
        try {
            val fileList = context.assets.list(path)

            for (fileName in fileList) {

                // open file within the assets folder
                // if it is not already there copy it to the sdcard
                val pathToDataFile = "$DATA_PATH$path/$fileName"
                if (!File(pathToDataFile).exists()) {

                    val input = context.assets.open("$path/$fileName")

                    val out = FileOutputStream(pathToDataFile)

                    // Transfer bytes from in to out
                    val buf = ByteArray(1024)
                    var len: Int

                    len = input.read(buf)
                    while (len > 0) {
                        out.write(buf, 0, len)
                        len = input.read(buf)
                    }
                    input.close()
                    out.close()

                    Timber.d("Copied " + fileName + "to tessdata")
                }
            }
        } catch (e: IOException) {
            Timber.e("Unable to copy files to tessdata " + e.toString())
        }

    }
}