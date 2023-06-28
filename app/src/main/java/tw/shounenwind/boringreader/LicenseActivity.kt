package tw.shounenwind.boringreader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LicenseActivity : BaseActivity() {
    private val listAdapter by lazy {
        ArrayAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        screenPrepare()
    }

    private fun screenPrepare() {
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = RecyclerView.VERTICAL
        findViewById<RecyclerView>(R.id.list_view).apply {
            this.layoutManager = layoutManager
            adapter = listAdapter
        }

        setLicenses()
    }

    private fun setLicenses() {
        mainScope?.launch(Dispatchers.IO) {
            val licenses = ArrayList<License>().apply {
                add(license("Okio") {
                    "Copyright 2013 Square, Inc.\n" +
                            "\n" +
                            "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                            "you may not use this file except in compliance with the License.\n" +
                            "You may obtain a copy of the License at\n" +
                            "\n" +
                            "   http://www.apache.org/licenses/LICENSE-2.0\n" +
                            "\n" +
                            "Unless required by applicable law or agreed to in writing, software\n" +
                            "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                            "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                            "See the License for the specific language governing permissions and\n" +
                            "limitations under the License.\n"
                })
                add(license("Material") {
                    "Copyright 2015 Rey Pham.\n" +
                            "\n" +
                            "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                            "you may not use this file except in compliance with the License.\n" +
                            "You may obtain a copy of the License at\n" +
                            "\n" +
                            "   http://www.apache.org/licenses/LICENSE-2.0\n" +
                            "\n" +
                            "Unless required by applicable law or agreed to in writing, software\n" +
                            "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                            "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                            "See the License for the specific language governing permissions and\n" +
                            "limitations under the License."
                })
                add(license("Material Scroll Bar") {
                    "Copyright 2016-2017 Turing Technologies, an unincorporated orginisation of Wynne Plaga.\n" +
                            "\n" +
                            "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                            "you may not use this file except in compliance with the License.\n" +
                            "You may obtain a copy of the License at\n" +
                            "\n" +
                            "   http://www.apache.org/licenses/LICENSE-2.0\n" +
                            "\n" +
                            "Unless required by applicable law or agreed to in writing, software\n" +
                            "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                            "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                            "See the License for the specific language governing permissions and\n" +
                            "limitations under the License.\n" +
                            "\n" +
                            "This licensing is applicable to all code offered as part of this\n" +
                            "repository, which can be identified by the lisence notice preceding\n" +
                            "the content AND/OR by its inclusion in a package starting with \"com.\n" +
                            "turingtechnologies.materialscrollbar\"."
                })
                add(license("RecyclerView-FastScroller") {
                    "Copyright 2018 Quiph Media Pvt Ltd\n" +
                            "\n" +
                            "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                            "you may not use this file except in compliance with the License.\n" +
                            "You may obtain a copy of the License at\n" +
                            "\n" +
                            "http://www.apache.org/licenses/LICENSE-2.0\n" +
                            "\n" +
                            "Unless required by applicable law or agreed to in writing, software\n" +
                            "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                            "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                            "See the License for the specific language governing permissions and\n" +
                            "limitations under the License."
                })
                add(license("Launcher 3") {
                    " Copyright (C) 2010 The Android Open Source Project\n" +
                            "\n" +
                            "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                            "you may not use this file except in compliance with the License.\n" +
                            "You may obtain a copy of the License at\n" +
                            "\n" +
                            "   http://www.apache.org/licenses/LICENSE-2.0\n" +
                            "\n" +
                            "Unless required by applicable law or agreed to in writing, software\n" +
                            "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                            "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                            "See the License for the specific language governing permissions and\n" +
                            "limitations under the License."
                })
                add(license("anko") {
                    "Copyright 2016 JetBrains s.r.o.\n" +
                            " \n" +
                            "  Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                            "  you may not use this file except in compliance with the License.\n" +
                            "  You may obtain a copy of the License at\n" +
                            " \n" +
                            "  http://www.apache.org/licenses/LICENSE-2.0\n" +
                            " \n" +
                            "  Unless required by applicable law or agreed to in writing, software\n" +
                            "  distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                            "  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                            "  See the License for the specific language governing permissions and\n" +
                            "  limitations under the License."
                })
                add(license("kotlinx.coroutines") {
                    "Copyright 2016-2018 JetBrains s.r.o.\n" +
                            "\n" +
                            "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                            "you may not use this file except in compliance with the License.\n" +
                            "You may obtain a copy of the License at\n" +
                            "\n" +
                            "http://www.apache.org/licenses/LICENSE-2.0\n" +
                            "\n" +
                            "Unless required by applicable law or agreed to in writing, software\n" +
                            "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                            "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                            "See the License for the specific language governing permissions and\n" +
                            "limitations under the License."
                })
                add(license("juniversalchardet") {
                    "/* ***** BEGIN LICENSE BLOCK *****\n" +
                            " * Version: MPL 1.1/GPL 2.0/LGPL 2.1\n" +
                            " *\n" +
                            " * The contents of this file are subject to the Mozilla Public License Version\n" +
                            " * 1.1 (the \"License\"); you may not use this file except in compliance with\n" +
                            " * the License. You may obtain a copy of the License at\n" +
                            " * http://www.mozilla.org/MPL/\n" +
                            " *\n" +
                            " * Software distributed under the License is distributed on an \"AS IS\" basis,\n" +
                            " * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License\n" +
                            " * for the specific language governing rights and limitations under the\n" +
                            " * License.\n" +
                            " *\n" +
                            " * The Original Code is mozilla.org code.\n" +
                            " *\n" +
                            " * The Initial Developer of the Original Code is\n" +
                            " * Netscape Communications Corporation.\n" +
                            " * Portions created by the Initial Developer are Copyright (C) 1998\n" +
                            " * the Initial Developer. All Rights Reserved.\n" +
                            " *\n" +
                            " * Contributor(s):\n" +
                            " *   Kohei TAKETA <k-tak@void.in> (Java port)\n" +
                            " *\n" +
                            " * Alternatively, the contents of this file may be used under the terms of\n" +
                            " * either the GNU General Public License Version 2 or later (the \"GPL\"), or\n" +
                            " * the GNU Lesser General Public License Version 2.1 or later (the \"LGPL\"),\n" +
                            " * in which case the provisions of the GPL or the LGPL are applicable instead\n" +
                            " * of those above. If you wish to allow use of your version of this file only\n" +
                            " * under the terms of either the GPL or the LGPL, and not to allow others to\n" +
                            " * use your version of this file under the terms of the MPL, indicate your\n" +
                            " * decision by deleting the provisions above and replace them with the notice\n" +
                            " * and other provisions required by the GPL or the LGPL. If you do not delete\n" +
                            " * the provisions above, a recipient may use your version of this file under\n" +
                            " * the terms of any one of the MPL, the GPL or the LGPL.\n" +
                            " *\n" +
                            " * ***** END LICENSE BLOCK ***** */"
                })
                add(license("koin") {
                    "Copyright 2017-2020 the original author or authors.\n" +
                            "\n" +
                            " Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                            "you may not use this file except in compliance with the License.\n" +
                            "You may obtain a copy of the License at\n" +
                            "\n" +
                            "     http://www.apache.org/licenses/LICENSE-2.0\n" +
                            "\n" +
                            "Unless required by applicable law or agreed to in writing, software\n" +
                            "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                            "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                            "See the License for the specific language governing permissions and\n" +
                            "limitations under the License.\n"
                })
            }
            withContext(Dispatchers.Main) { listAdapter.setData(licenses) }
        }

    }

    override fun finish() {
        super.finish()
        listAdapter.setData(ArrayList(0))
    }

    private inner class License(val title: String, val content: String)

    private inline fun license(title: String, content: () -> String): License {
        return License(title, content())
    }

    private inner class ArrayAdapter : RecyclerView.Adapter<ArrayAdapter.ListViewHolder>() {

        var data: ArrayList<License>

        init {
            this.data = ArrayList()
        }

        fun setData(data: List<License>) {
            this.data = ArrayList(data.size)
            this.data.addAll(data)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
            val rootView = LayoutInflater.from(parent.context).inflate(
                    R.layout.unit_license,
                    parent,
                    false
            )
            val lp = RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            )
            rootView.layoutParams = lp
            return ListViewHolder(rootView)
        }

        override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
            holder.title.text = data[position].title
            holder.content.text = data[position].content
        }


        override fun getItemCount(): Int {
            return data.size
        }

        inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val title = itemView.findViewById<TextView>(R.id.title)!!
            val content = itemView.findViewById<TextView>(R.id.content)!!
        }
    }
}