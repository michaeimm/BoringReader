package tw.shounenwind.boringreader

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.edit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.android.viewModel


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
            sharedPreferences = defaultSharedPreferences
        }
        setContentView(R.layout.activity_main)
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
                    getFileContent(Uri.parse(savedInstanceState.getString("currentFile")))
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
                    toast(R.string.unsupported_format)
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
            requestCode == 0 && resultCode == Activity.RESULT_OK && data?.data != null -> {
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
        findViewById<RecyclerView>(R.id.hello_world).visibility = View.GONE
        findViewById<RecyclerView>(R.id.list_view).scrollToPosition(lineNumber)
        pd = null
    }

    private suspend fun getFileContent(uri: Uri) = withContext(Dispatchers.IO) {
        currentFile = uri
        lineNumber = sharedPreferences.getInt(uri.toString(), 0)

        viewModel.getFileContent(this@MainActivity, uri)
    }

}
