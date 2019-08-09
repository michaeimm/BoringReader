package tw.shounenwind.boringreader

import android.content.Context
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialog

class ProgressDialog(context: Context) : AppCompatDialog(context) {
    private val content: TextView

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.unit_progress_dialog)
        content = findViewById(R.id.content)!!
    }

    fun setContent(content: String) {
        this.content.text = content
    }

    inline fun show(body: ProgressDialog.() -> Unit): ProgressDialog {
        body()
        show()
        return this
    }
}