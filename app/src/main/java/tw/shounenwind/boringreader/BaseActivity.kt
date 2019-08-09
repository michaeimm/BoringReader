package tw.shounenwind.boringreader

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

@SuppressLint("Registered")
open class BaseActivity: AppCompatActivity() {
    protected var mainScope: CoroutineScope? = MainScope()

    override fun onDestroy() {
        mainScope?.cancel()
        mainScope = null
        super.onDestroy()
    }
}