package tw.shounenwind.boringreader

import android.content.Context
import android.content.ContextWrapper
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.*

class UnitText(base: Context) : AnkoComponent<ViewGroup>, ContextWrapper(base) {
    override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui) {
        textView {
            id = R.id.text_unit
            leftPadding = dip(16)
            rightPadding = dip(16)
            topPadding = dip(4)
            bottomPadding = dip(4)
            textSize = 16f
        }
    }

}