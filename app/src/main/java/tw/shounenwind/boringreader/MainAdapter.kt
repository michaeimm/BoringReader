package tw.shounenwind.boringreader

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.qtalk.recyclerviewfastscroller.RecyclerViewFastScroller
import org.jetbrains.anko.AnkoContext
import tw.shounenwind.boringreader.MainAdapter.TextViewHolder

class MainAdapter : RecyclerView.Adapter<TextViewHolder>(),
    RecyclerViewFastScroller.OnPopupTextUpdate {

    var data: List<String>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onChange(position: Int): CharSequence {
        return position.toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextViewHolder {
        val rootView = UnitText(parent.context).createView(
            AnkoContext.create(parent.context, parent)
        )
        val lp = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        rootView.layoutParams = lp
        return TextViewHolder(rootView)
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun onBindViewHolder(holder: TextViewHolder, position: Int) {
        holder.textView.text = data?.get(position)
    }

    inner class TextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.text_unit)
    }

}