package net.kelmer.correostracker.ui.create

import android.app.Activity.RESULT_OK
import android.text.TextUtils
import kotlinx.android.synthetic.main.fragment_create_parcel.*
import net.kelmer.correostracker.ApplicationComponent
import net.kelmer.correostracker.R
import net.kelmer.correostracker.base.BaseFragment
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.ext.observe
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.graphics.BitmapFactory
import timber.log.Timber
import android.provider.MediaStore
import java.io.IOException


/**
 * Created by gabriel on 25/03/2018.
 */
class CreateParcelFragment : BaseFragment<CreateParcelViewModel>() {

    override fun injectDependencies(graph: ApplicationComponent) {
        val component = graph.plus(CreateParcelModule())
        component
                .injectTo(this)
        component
                .injectTo(viewModel)
    }

    override val viewModelClass = CreateParcelViewModel::class.java

    override fun loadUp() {
        create_ok.setOnClickListener {
            if (!TextUtils.isEmpty(parcel_name.text.toString()) && !TextUtils.isEmpty(parcel_code.text.toString())) {
                var localParcelReference = LocalParcelReference(parcel_code.text.toString(), parcel_name.text.toString())
                viewModel.addParcel(localParcelReference)
            }
        }
        viewModel.saveParcelLiveData.observe(this, {
            activity.finish()
        })

        ocr.setOnClickListener {
            scan()
        }
    }

    val PICK_IMAGE = 1

    fun scan() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && null != data) {
            var imageUri: Uri = data.data


            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = context.contentResolver.query(imageUri,
                    filePathColumn, null, null, null)
            cursor.moveToFirst()

            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val picturePath = cursor.getString(columnIndex)
            cursor.close()

            var bmp: Bitmap? = null
            try {
                bmp = getBitmapFromUri(imageUri)
                viewModel.scanImage(bmp)
            } catch (e: IOException) {
                Timber.e("File does not exist!!!")
                // TODO Auto-generated catch block
                e.printStackTrace()
            }

        }
    }


    @Throws(IOException::class)
    private fun getBitmapFromUri(uri: Uri): Bitmap {
        val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "r")
        val fileDescriptor = parcelFileDescriptor.getFileDescriptor()
        val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor.close()
        return image
    }

    override val layoutId: Int = R.layout.fragment_create_parcel
}