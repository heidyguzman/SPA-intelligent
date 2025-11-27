package com.narmocorp.satorispa

import android.content.Context
import android.content.Intent
import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class HceService : HostApduService() {

    companion object {
        val TAG = "HceService"
        val STATUS_SUCCESS = "9000"
        val STATUS_FAILED = "6F00"
        val CLA_NOT_SUPPORTED = "6E00"
        val INS_NOT_SUPPORTED = "6D00"
        val AID = "F0010203040506"
        val SELECT_INS = "A4"
        val DEFAULT_CLA = "00"
        val MIN_APDU_LENGTH = 12
    }

    override fun onDeactivated(reason: Int) {
        Log.d(TAG, "Deactivated: " + reason)
    }

    override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray {
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
        val aidInApdu = hexCommandApdu.substring(10, 10 + AID.length)

        if (AID == aidInApdu) {
            val sharedPreferences = getSharedPreferences("NfcPref", Context.MODE_PRIVATE)
            val nfcUid = sharedPreferences.getString("nfc_uid", null)

            if (nfcUid != null) {
                Log.d(TAG, "Sending NFC UID: $nfcUid")

                // ¡NUEVO! Notifica a la app que el UID se envió
                val intent = Intent("nfc-uid-sent-success")
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)

                // Se envía únicamente el UID, sin el código de estado.
                return Utils.hexStringToByteArray(nfcUid)
            } else {
                Log.w(TAG, "NFC UID not found in SharedPreferences")
                return Utils.hexStringToByteArray(STATUS_FAILED)
            }
        } else {
            return Utils.hexStringToByteArray(STATUS_FAILED)
        }
    }
}

object Utils {
    fun toHex(bytes: ByteArray): String {
        return bytes.joinToString("") { "%02x".format(it) }.uppercase()
    }

    fun hexStringToByteArray(hex: String): ByteArray {
        val len = hex.length
        val data = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            data[i / 2] = ((Character.digit(hex[i], 16) shl 4) + Character.digit(hex[i + 1], 16)).toByte()
            i += 2
        }
        return data
    }
}
