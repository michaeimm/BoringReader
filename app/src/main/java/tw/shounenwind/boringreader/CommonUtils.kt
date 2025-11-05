package tw.shounenwind.boringreader

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

object CommonUtils {
    const val INSET_TOP = 0b0001
    const val INSET_RIGHT = 0b0010
    const val INSET_BOTTOM = 0b0100
    const val INSET_LEFT = 0b1000

    fun View.applyInsets(target: Int) {
        ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
            val realInsets = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
                        or WindowInsetsCompat.Type.ime()
            )
            val top = if (target and INSET_TOP > 0) {
                realInsets.top
            } else {
                paddingTop
            }
            val right = if (target and INSET_RIGHT > 0) {
                realInsets.right
            } else {
                paddingRight
            }
            val bottom = if (target and INSET_BOTTOM > 0) {
                realInsets.bottom
            } else {
                paddingBottom
            }
            val left = if (target and INSET_LEFT > 0) {
                realInsets.left
            } else {
                paddingLeft
            }
            v.updatePadding(left, top, right, bottom)
            insets
        }

    }

    private fun fillIntentArguments(intent: Intent, params: Array<out Pair<String, Any?>>) {
        params.forEach {
            when (val value = it.second) {
                null -> intent.putExtra(it.first, null as java.io.Serializable?)
                is Int -> intent.putExtra(it.first, value)
                is Long -> intent.putExtra(it.first, value)
                is CharSequence -> intent.putExtra(it.first, value)
                is String -> intent.putExtra(it.first, value)
                is Float -> intent.putExtra(it.first, value)
                is Double -> intent.putExtra(it.first, value)
                is Char -> intent.putExtra(it.first, value)
                is Short -> intent.putExtra(it.first, value)
                is Boolean -> intent.putExtra(it.first, value)
                is java.io.Serializable -> intent.putExtra(it.first, value)
                is Bundle -> intent.putExtra(it.first, value)
                is Parcelable -> intent.putExtra(it.first, value)
                is Array<*> -> when {
                    value.isArrayOf<CharSequence>() -> intent.putExtra(it.first, value)
                    value.isArrayOf<String>() -> intent.putExtra(it.first, value)
                    value.isArrayOf<Parcelable>() -> intent.putExtra(it.first, value)
                    else -> throw Exception("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
                }

                is IntArray -> intent.putExtra(it.first, value)
                is LongArray -> intent.putExtra(it.first, value)
                is FloatArray -> intent.putExtra(it.first, value)
                is DoubleArray -> intent.putExtra(it.first, value)
                is CharArray -> intent.putExtra(it.first, value)
                is ShortArray -> intent.putExtra(it.first, value)
                is BooleanArray -> intent.putExtra(it.first, value)
                else -> throw Exception("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
            }
            return@forEach
        }
    }

    fun <T> createIntent(
        ctx: Context,
        clazz: Class<out T>,
        params: Array<out Pair<String, Any?>>
    ): Intent {
        val intent = Intent(ctx, clazz)
        if (params.isNotEmpty()) fillIntentArguments(intent, params)
        return intent
    }

    inline fun <reified T : Any> Context.intentFor(vararg params: Pair<String, Any?>): Intent =
        createIntent(this, T::class.java, params)
}