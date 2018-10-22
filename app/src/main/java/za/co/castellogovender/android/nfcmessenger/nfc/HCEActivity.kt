package za.co.castellogovender.android.nfcmessenger.nfc

import android.content.Intent
import android.nfc.NfcAdapter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import kotlinx.android.synthetic.main.activity_hce.*
import kotlinx.android.synthetic.main.activity_reader.*
import za.co.castellogovender.android.nfcmessenger.R

class HCEActivity : AppCompatActivity() {

    private var nfcAdapter: NfcAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hce)
        supportActionBar?.title = "Sender"
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        btn_hce.setOnClickListener{
            refreshAPDU()
        }

    }

    private fun refreshAPDU(){
        //txt_hce.append("\nMy Private Key: "+HostCardEmulatorService.myDevice.privatekey)
        //txt_hce.append("\nMy Public Key: "+HostCardEmulatorService.myDevice.publickey)
        if (HostCardEmulatorService.myDevice.sharedsecret!=null){
            if (edt_key_hce.text!=null) {
                txt_hce.text = HostCardEmulatorService.myDevice.encrypt(edt_key_hce.text.toString())
            }
        }
        txt_hce.text = HostCardEmulatorService.encryptext
    }

    override fun onResume() {
        super.onResume()

        /*if (!nfcAdapter!!.isEnabled){
            startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
        }*/
    }

    override fun onPause() {
        super.onPause()
        /*if (nfcAdapter!!.isEnabled){
            startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
        }*/
    }


}
