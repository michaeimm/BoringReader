package tw.shounenwind.boringreader

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import okio.buffer
import okio.source
import org.jetbrains.anko.toast
import org.mozilla.universalchardet.UniversalDetector
import java.nio.charset.Charset

class MainViewModel : ViewModel() {

    private val _lines = MutableLiveData<List<String>>()
    val lines: LiveData<List<String>> = _lines

    suspend fun getFileContent(mContext: Context, uri: Uri) = withContext(Dispatchers.IO) {
        Log.d("open", uri.toString())
        try {
            val cR = mContext.contentResolver

            cR.openInputStream(uri)!!.use { inputStream ->
                val fileSource = inputStream.source()
                val bufferedSource = fileSource.buffer()
                val detector = UniversalDetector {

                }

                val data = bufferedSource.peek().readByteArray()
                detector.handleData(data, 0, data.size)

                val charset = if (detector.detectedCharset == null) {
                    withContext(Dispatchers.Main) {
                        mContext.toast(R.string.unknown_charset)
                    }
                    Charset.defaultCharset()
                } else {
                    charset(detector.detectedCharset)
                }

                Log.d("charset", charset.name())

                val result = ArrayList<String>()
                while (true) {
                    val lineBreakPosition = bufferedSource.peek().indexOf('\n'.toByte())
                    val str = when {
                        lineBreakPosition < 0 -> {
                            bufferedSource.readString(charset)
                        }
                        lineBreakPosition == 0L -> {
                            bufferedSource.skip(1)
                            " "
                        }
                        else -> {
                            bufferedSource.readString(lineBreakPosition, charset).apply {
                                substring(0, length)
                                bufferedSource.skip(1)
                            }
                        }
                    }

                    if (str.isEmpty() && bufferedSource.peek().indexOf('\n'.toByte()) == -1L) {
                        break
                    }

                    result.add(str)

                    if (!isActive) {
                        break
                    }
                }
                _lines.postValue(result)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                mContext.toast(R.string.error_and_close)
            }
        }
    }
}