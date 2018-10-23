package za.co.castellogovender.android.nfcmessenger.nfc

import android.content.Intent
import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log
import za.co.castellogovender.android.nfcmessenger.messages.ChatLogActivity
import za.co.castellogovender.android.nfcmessenger.messages.NewMessageActivity
import java.security.KeyPair

class HostCardEmulatorService: HostApduService() {

    companion object {
        //APDU standardisations
        val TAG = "Host Card Emulator"
        val STATUS_SUCCESS = "9000"
        val STATUS_FAILED = "6F00"
        val CLA_NOT_SUPPORTED = "6E00"
        val INS_NOT_SUPPORTED = "6D00"
        val AID = "A0000002471001"
        val SELECT_INS = "A4"
        val DEFAULT_CLA = "00"
        val MIN_APDU_LENGTH = 12

        var encryptext = " "
        var myDevice:KeyExchangeSec = KeyExchangeSec()

        fun generateNewKeyPair(){
            myDevice = KeyExchangeSec("EC", 256)
        }
    }

    override fun onDeactivated(reason: Int) {
        Log.d(TAG, "Deactivated: " + reason)
    }

    override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray? {
        if (commandApdu != null && !myDevice.isEmpty) {
            return myDevice.publickey.encoded // send public key
        }
        else {
            return KeyExchangeSec.hexStringToByteArray(STATUS_FAILED)
        }
        //Normal handler for APDU requests, can be abstracted for additional usage
        /*
        if (commandApdu == null) {
            return Utils.hexStringToByteArray(STATUS_FAILED)
        }

        val hexCommandApdu = Utils.toHex(commandApdu)
        if (hexCommandApdu.length < MIN_APDU_LENGTH) {
            return Utils.hexStringToByteArray(STATUS_FAILED)
        }

        if (hexCommandApdu.substring(0, 2) != DEFAULT_CLA) {
            return Utils.hexStringToByteArray(CLA_NOT_SUPPORTED)
        }

        if (hexCommandApdu.substring(2, 4) != SELECT_INS) {
            return Utils.hexStringToByteArray(INS_NOT_SUPPORTED)
        }

        if (hexCommandApdu.substring(10, 24) == AID)  {
            return Utils.hexStringToByteArray("")//STATUS_SUCCESS)
        }*/

    }

}
