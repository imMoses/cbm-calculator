package id.cbm.main.cbm_calculator.core.listener // ktlint-disable package-name

interface IPermissionListener {
    fun onPermissionGranted()

    fun onFailed(requestCode:Int, perms: MutableList<String>)
}
