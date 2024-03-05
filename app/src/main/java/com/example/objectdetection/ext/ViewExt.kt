package com.example.objectdetection.ext

import android.Manifest
import android.content.Context
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission

fun AppCompatActivity.showToast(context: Context = this, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.showToast(context: Context = this.requireContext(), message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}


fun View.hideKeyboard(context: Context = this.context) {
    val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(this.windowToken, 0)
}

fun TextInputLayout.showError(message: String) {
    error = message
    isErrorEnabled = true
}

fun TextInputLayout.hideError() {
    isErrorEnabled = false
}

fun Context.checkPermission(callback: (isGranted: Boolean) -> Unit) {
    TedPermission.create()
        .setPermissionListener(object : PermissionListener {
            override fun onPermissionGranted() {
                callback.invoke(true)
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                callback.invoke(false)
            }
        })
        .setPermissions(*providePermissions())
        .setDeniedMessage("If you reject permission")
        .check()
}

fun providePermissions(): Array<String> {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    } else {
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }
}
