package za.co.castellogovender.android.nfcmessenger.nfc

import android.content.Intent
import android.nfc.NfcAdapter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_hce.*
import za.co.castellogovender.android.nfcmessenger.R

class HCEActivity : AppCompatActivity() {

    private var nfcAdapter: NfcAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hce)
        supportActionBar?.title = "Sender"
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        btn_hce.setOnClickListener{
            showGeneratedKeyPair()
        }
        btn_switchtorecieve_hce.setOnClickListener{
            val intent = Intent(this, ReaderActivity::class.java)
            startActivity(intent)
        }

    }

    private fun showGeneratedKeyPair(){
        HostCardEmulatorService.generateNewKeyPair()
        txt_publickey_hce.text= "Public Key: "+HostCardEmulatorService.myDevice.publickey.encoded
        txt_privatekey_hce.text= "Private Key: "+HostCardEmulatorService.myDevice.publickey.encoded
    }

    override fun onResume() {
        super.onResume()
        if(!HostCardEmulatorService.myDevice.isEmpty){
            txt_publickey_hce.text= "Public Key: "+HostCardEmulatorService.myDevice.publickey.encoded
            txt_privatekey_hce.text= "Private Key: "+HostCardEmulatorService.myDevice.publickey.encoded
        }
        //to suggest to the user that they turn on NFC when they enter the transmit mode
        /*if (!nfcAdapter!!.isEnabled){
            startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
        }*/
    }

    override fun onPause() {
        super.onPause()
        //to suggest to the user that they turn on NFC when they leave the transmit mode
        /*if (nfcAdapter!!.isEnabled){
            startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
        }*/
    }


}
