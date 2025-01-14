package id.cbm.main.cbm_calculator.data.local

import android.content.Context
import android.content.SharedPreferences
import android.os.StrictMode
import android.util.Log
import com.google.gson.GsonBuilder
import java.lang.Exception

class LocalDataController {

    companion object {
        private val cacheDb: HashMap<String, SharedPreferences> = hashMapOf()

        fun checkCacheDB(context: Context, sharedPref: String?): SharedPreferences {
            sharedPref ?: return context.getSharedPreferences(
                sharedPref,
                Context.MODE_PRIVATE,
            )

            return cacheDb.getOrPut(sharedPref) {
                context.getSharedPreferences(sharedPref, Context.MODE_PRIVATE)
            }
        }

        fun<T> allowReads(block: () -> T): T {
            val oldPolicy = StrictMode.allowThreadDiskReads()
            try {
                return block()
            } finally {
//                StrictMode.setThreadPolicy(oldPolicy)
            }
        }
    }

    class General {

        fun putString(context: Context, key: String?, data: String?, sharedPref: String?) {
            try {
                val _sharedPreferences = checkCacheDB(context, sharedPref)
                val _editor = _sharedPreferences.edit()
                _editor.putString(key, data)
                _editor.apply()
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(LocalDataController::class.simpleName, "error General.putString: ${e.message}")
            }
        }

        fun getString(context: Context, key: String?, sharedPref: String?): String? {
            try {
                val _sharedPref = checkCacheDB(context, sharedPref)
                return _sharedPref.getString(key, "")
            } catch (e: Exception) {
                e.printStackTrace()
                return ""
            }
        }

        fun<T> putModel(context: Context, key: String?, objData: T, sharedPref: String?) {
            val builder = GsonBuilder()
            builder.serializeNulls()
            val mGson = builder.create()

            try {
                val data = mGson.toJson(objData)
                val sharedPref = checkCacheDB(context, sharedPref)
                val editor = sharedPref.edit()
                editor.putString(key, data)
                editor.apply()
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(LocalDataController::class.simpleName, "error General.putModel: ${e.message}")
            }
        }

        fun<T> getModel(context: Context, key: String?, objType: Class<T>, sharedPref: String?): T? {
            val builder = GsonBuilder()
            builder.serializeNulls()
            val mGson = builder.create()

            context?.let { safeCtx ->
                try {

                    val sharedPref = checkCacheDB(safeCtx, sharedPref)
                    val raw = sharedPref.getString(key, "")
                    return if (raw == "") {
                        null
                    } else {
                        mGson.fromJson(raw, objType)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e(LocalDataController::class.simpleName, "error General.getModel : ${e.message}")
                }
            }

            return null
        }

        fun resetLocalDB(context: Context, sharedPref: String?, key: String?) {
            allowReads {
                val _sharedPref = checkCacheDB(context = context, sharedPref = sharedPref)
                val _editor = _sharedPref.edit()
                _editor.remove(key)
                _editor.apply()
            }
        }
    }
}
