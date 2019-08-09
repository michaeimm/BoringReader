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
import com.turingtechnologies.materialscrollbar.CustomIndicator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.buffer
import okio.source
import org.apache.commons.lang3.StringUtils
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import org.mozilla.universalchardet.UniversalDetector
import java.io.InputStream
import java.nio.charset.Charset


class MainActivity : BaseActivity() {

    private val adapter = MainAdapter()
    private lateinit var sharedPreferences: SharedPreferences
    private var currentFile: Uri? = null
    private var lineNumber = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GlobalScope.launch(Dispatchers.IO) {
            sharedPreferences = defaultSharedPreferences
        }
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        list_view.layoutManager = LinearLayoutManager(this)
        list_view.adapter = adapter
        dragScrollBar.apply {
            setIndicator(
                CustomIndicator(this@MainActivity),
                true
            )
        }

        val intent = intent
        val intentType = intent.type ?: ""
        if (intent != null
            && (Intent.ACTION_SEND == intent.action || Intent.ACTION_VIEW == intent.action)
        ) {
            if (intentType.startsWith("text/")) {
                val pd = ProgressDialog(this).show {
                    setContent("載入中")
                    setCancelable(false)
                }
                GlobalScope.launch {
                    val content = getFileContent(intent.data!!)
                    bindData(content)
                    withContext(Dispatchers.Main) { pd.dismiss() }
                }
            } else {
                toast("不支援的格式")
            }
        }
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
                startActivityForResult(Intent.createChooser(intent, "Select a TXT file"), 0)
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
                val pd = ProgressDialog(this).show {
                    setContent("載入中")
                    setCancelable(false)
                }
                GlobalScope.launch {
                    val content = getFileContent(data.data!!)
                    bindData(content)
                    withContext(Dispatchers.Main) { pd.dismiss() }
                }
            }
        }
    }

    override fun onPause() {
        currentFile?.also {
            sharedPreferences.edit {
                lineNumber = (list_view.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                putInt(it.toString(), lineNumber)
            }
        }
        super.onPause()
    }

    private suspend fun bindData(data: List<String>) = withContext(Dispatchers.Main) {
        Log.d("binding", "size: " + data.size)
        adapter.data = data
        hello_world.visibility = View.GONE
        list_view.scrollToPosition(lineNumber)
    }

    private suspend fun getFileContent(uri: Uri) = withContext<List<String>>(Dispatchers.IO) {
        Log.d("open", uri.toString())
        var inputStream: InputStream? = null
        try {
            currentFile = uri
            lineNumber = sharedPreferences.getInt(uri.toString(), 0)
            val cR = contentResolver

            inputStream = cR.openInputStream(uri)!!
            val fileSource = inputStream.source()
            val bufferedSource = fileSource.buffer()
            val charset: Charset?
            val detector = UniversalDetector {

            }

            val data = bufferedSource.peek().readByteArray()
            detector.handleData(data, 0, data.size)

            charset = if (detector.detectedCharset == null) {
                charset("US-ASCII")
            } else {
                charset(detector.detectedCharset)
            }

            val result = ArrayList<String>()
            var lastIndex = 0L
            while (true) {
                val lineBreakPosition = bufferedSource.peek().indexOf('\n'.toByte(), lastIndex)
                val str = if (lineBreakPosition == -1L) {
                    bufferedSource.readString(charset)
                } else {
                    bufferedSource.readString(lineBreakPosition - 1, charset).apply {
                        substring(0, length)
                        bufferedSource.skip(1)
                    }
                }
                lastIndex += lineBreakPosition
                if (str.isEmpty() && bufferedSource.peek().indexOf('\n'.toByte(), lastIndex) == -1L) {
                    break
                }
                val strings = StringUtils.split(StringUtils.remove(str, "\r"), '\n')

                result.addAll(strings)
                if (isFinishing) {
                    break
                }
            }
            return@withContext result

        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                toast("發生錯誤，已關閉檔案")
            }
            return@withContext emptyList()
        } finally {
            inputStream?.close()
        }
    }

}
