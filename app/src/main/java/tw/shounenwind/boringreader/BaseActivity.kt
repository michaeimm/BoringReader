package tw.shounenwind.boringreader

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {
    protected var mainScope: CoroutineScope? = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.enableEdgeToEdge(window)
    }

    override fun onDestroy() {
        mainScope?.cancel()
        mainScope = null
        super.onDestroy()
    }
}