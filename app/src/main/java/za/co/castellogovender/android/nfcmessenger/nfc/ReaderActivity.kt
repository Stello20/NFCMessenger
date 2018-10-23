package za.co.castellogovender.android.nfcmessenger.nfc

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_reader.*
import za.co.castellogovender.android.nfcmessenger.R
import android.R.id.edit
import android.content.SharedPreferences
import android.preference.PreferenceManager



class ReaderActivity : AppCompatActivity(), NfcAdapter.ReaderCallback {

    private var nfcAdapter: NfcAdapter? = null
    private var apduHead = "00A4040007"
    private var identifier = "A0000002471001"
    private var oldResponse = KeyExchangeSec.hexStringToByteArray(apduHead)
    //var response = KeyExchangeSec.hexStringToByteArray(apduHead)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reader)
        supportActionBar?.title = "Reciever"
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        btn_switchtosender_reader.setOnClickListener{
            val intent = Intent(this, HCEActivity::class.java)
            startActivity(intent)
        }
        /*btn_gensharedkey_reader.setOnClickListener{
            if(publickey_reader.text!=null){
                HostCardEmulatorService.myDevice.setReceiverPublicKey(KeyExchangeSec.toPublicKey(KeyExchangeSec.hexStringToByteArray(publickey_reader.text.toString())))
                txt_sharedkey_reader.text = "Shared Secret: "+HostCardEmulatorService.myDevice.sharedSecret
            }
        }*/

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
        //checkDisableReaderNFC()
        val response = isoDep.transceive(KeyExchangeSec.hexStringToByteArray(apduHead+identifier))
        runOnUiThread {
            publickey_reader.text = "Recieved Public Key: "+KeyExchangeSec.bytesToHex(response)
            HostCardEmulatorService.myDevice.setReceiverPublicKey(KeyExchangeSec.toPublicKey(response))
            val sharedKey = KeyExchangeSec.bytesToHex(HostCardEmulatorService.myDevice.sharedSecret)
            txt_sharedkey_reader.text = ("Shared Key: "+sharedKey)

            val preferences = PreferenceManager.getDefaultSharedPreferences(this)
            val editor = preferences.edit()
            editor.putString("NextSharedKey", sharedKey)
            editor.apply()
        }
        //oldResponse= response
        //HostCardEmulatorService.encryptext = HostCardEmulatorService.myDevice.encrypt("hello world")
        isoDep.close()
    }

    fun checkDisableReaderNFC(){
        //does not allow the same APDU request to send consecutively
        /*if (oldResponse==response){
            identifier=""
            apduHead=""
        }*/
    }
}
