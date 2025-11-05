package tw.shounenwind.boringreader

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.edit
import androidx.core.net.toUri
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import tw.shounenwind.boringreader.CommonUtils.INSET_LEFT
import tw.shounenwind.boringreader.CommonUtils.INSET_RIGHT
import tw.shounenwind.boringreader.CommonUtils.INSET_TOP
import tw.shounenwind.boringreader.CommonUtils.applyInsets
import tw.shounenwind.boringreader.CommonUtils.intentFor


class MainActivity : BaseActivity() {

    private val adapter = MainAdapter()
    private lateinit var sharedPreferences: SharedPreferences
    private var currentFile: Uri? = null
    private var lineNumber = 0
    private val viewModel: MainViewModel by viewModel()
    private var pd: ProgressDialog? = null
        set(value) {
            field?.dismiss()
            field = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainScope?.launch(Dispatchers.IO) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
        }
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.root_view).applyInsets(INSET_LEFT + INSET_RIGHT + INSET_TOP)
        setSupportActionBar(findViewById(R.id.toolbar))
        findViewById<RecyclerView>(R.id.list_view).apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = adapter
        }

        val intent = intent
        val intentType = intent.type ?: ""
        if (viewModel.lines.value?.isNotEmpty() == true) {
            if (savedInstanceState?.getString("currentFile") != null) {
                pd = ProgressDialog(this).show {
                    setContent(getString(R.string.loading))
                    setCancelable(false)
                }
                mainScope?.launch {
                    getFileContent(savedInstanceState.getString("currentFile")!!.toUri())
                }
            } else if (intent != null
                && (Intent.ACTION_SEND == intent.action || Intent.ACTION_VIEW == intent.action)
            ) {
                if (intentType.startsWith("text/")) {
                    pd = ProgressDialog(this).show {
                        setContent(getString(R.string.loading))
                        setCancelable(false)
                    }
                    mainScope?.launch {
                        getFileContent(intent.data!!)
                    }
                } else {
                    Toast.makeText(this, R.string.unsupported_format, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.lines.observe(this) {
            bindData(it)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("currentFile", currentFile.toString())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_open -> {
                val intent = Intent()
                    .setType("text/*")
                    .setAction(Intent.ACTION_GET_CONTENT)
                startActivityForResult(
                    Intent.createChooser(
                        intent,
                        getString(R.string.select_a_text_file)
                    ), 0
                )
                true
            }

            R.id.action_license -> {

                startActivity(intentFor<LicenseActivity>())
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            requestCode == 0 && resultCode == RESULT_OK && data?.data != null -> {
                pd = ProgressDialog(this).show {
                    setContent(getString(R.string.loading))
                    setCancelable(false)
                }
                mainScope?.launch {
                    getFileContent(data.data!!)
                }
            }
        }
    }

    override fun onPause() {
        currentFile?.also {
            sharedPreferences.edit {
                lineNumber = (findViewById<RecyclerView>(R.id.list_view)
                    .layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                putInt(it.toString(), lineNumber)
            }
        }
        super.onPause()
    }

    private fun bindData(data: List<String>) {
        Log.d("binding", "size: " + data.size)
        adapter.data = data
        findViewById<TextView>(R.id.hello_world).visibility = View.GONE
        findViewById<RecyclerView>(R.id.list_view).scrollToPosition(lineNumber)
        pd = null
    }

    private suspend fun getFileContent(uri: Uri) = withContext(Dispatchers.IO) {
        currentFile = uri
        lineNumber = sharedPreferences.getInt(uri.toString(), 0)

        viewModel.getFileContent(this@MainActivity, uri)
    }

}
