package za.co.castellogovender.android.nfcmessenger.nfc

import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_reader.*
import za.co.castellogovender.android.nfcmessenger.R

class ReaderActivity : AppCompatActivity(), NfcAdapter.ReaderCallback {

    private var nfcAdapter: NfcAdapter? = null
    private var keyA = "00A4040007"
    private var identifier = "A0000002471001"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reader)
        supportActionBar?.title = "Reciever"
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

    }

    public override fun onResume() {
        super.onResume()
        nfcAdapter?.enableReaderMode(this, this,
            NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
            null)
    }

    public override fun onPause() {
        super.onPause()
        nfcAdapter?.disableReaderMode(this)
    }

    override fun onTagDiscovered(tag: Tag?) {
        val isoDep = IsoDep.get(tag)
        isoDep.connect()
        //keyA = "00A4041110"
        //identifier = "A0000002471001"
        val response = isoDep.transceive(Utils.hexStringToByteArray(keyA+identifier))
        runOnUiThread { textView.append("\nCard Response: "
                + Utils.toHex(response)) }
        isoDep.close()
    }
}
