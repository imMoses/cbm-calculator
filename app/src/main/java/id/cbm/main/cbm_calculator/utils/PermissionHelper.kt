package id.cbm.main.cbm_calculator.utils

import android.Manifest
import android.os.Build
import androidx.fragment.app.FragmentActivity
import id.cbm.main.cbm_calculator.core.listener.IPermissionListener
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

object PermissionHelper {

    fun permissionNetworkState(): String {
        return Manifest.permission.ACCESS_NETWORK_STATE
    }

    fun permissionNotification(): String {
        return Manifest.permission.POST_NOTIFICATIONS
    }

    fun permissionMediaAccess(): String {
        return if (Build.VERSION.SDK_INT >= 33) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        }
    }

    fun checkGrantedPermission(
        context: FragmentActivity,
        perms: List<String>,
        requestCode: Int,
        listener: IPermissionListener,
    ) {
        if (EasyPermissions.hasPermissions(
                context,
                *perms.toTypedArray(),
            )
        ) {
            listener.onPermissionGranted()
        } else {
            EasyPermissions.requestPermissions(
                context,
                "Please enable permission!",
                requestCode,
                *perms.toTypedArray(),
            )
        }

        object : EasyPermissions.PermissionCallbacks {
            override fun onRequestPermissionsResult(
                requestCode: Int,
                permissions: Array<out String>,
                grantResults: IntArray,
            ) {
                EasyPermissions.onRequestPermissionsResult(
                    requestCode,
                    permissions,
                    grantResults,
                    this,
                )
            }

            override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
                listener.onPermissionGranted()
            }

            override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
                if (EasyPermissions.somePermissionPermanentlyDenied(context, perms)) {
                    AppSettingsDialog.Builder(context).build().show()
                }

                listener.onFailed(requestCode, perms)
            }
        }
    }
}
