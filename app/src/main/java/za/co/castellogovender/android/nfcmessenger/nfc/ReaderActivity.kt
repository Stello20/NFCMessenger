package za.co.castellogovender.android.nfcmessenger.nfc

import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_reader.*
import za.co.castellogovender.android.nfcmessenger.R

class ReaderActivity : AppCompatActivity(), NfcAdapter.ReaderCallback {

    private var nfcAdapter: NfcAdapter? = null
    private var apduHead = "00A4040007"
    private var identifier = "A0000002471001"
    private var oldResponse = Utils.hexStringToByteArray(apduHead)
    var response = Utils.hexStringToByteArray(apduHead)

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
        if (oldResponse==response){
            identifier=""
            apduHead=""
        }
        response = isoDep.transceive(Utils.hexStringToByteArray(apduHead+identifier))
        runOnUiThread { textView.append("\nCard Response: "
                + Utils.toHex(response))
            Toast.makeText(this,"Recieved", Toast.LENGTH_SHORT).show()}
        oldResponse= response
        HostCardEmulatorService.myDevice.setReceiverPublicKey(KeyExchangeSec.toPublicKey(response))
        HostCardEmulatorService.encryptext = HostCardEmulatorService.myDevice.encrypt("hello world")
        isoDep.close()
    }
}
