package za.co.castellogovender.android.nfcmessenger.nfc

import com.google.android.gms.flags.FlagSource.G

class Utils {



    companion object {

        private val HEX_CHARS = "0123456789ABCDEF"
        fun hexStringToByteArray(data: String) : ByteArray {

            val result = ByteArray(data.length / 2)

            for (i in 0 until data.length step 2) {
                val firstIndex = HEX_CHARS.indexOf(data[i]);
                val secondIndex = HEX_CHARS.indexOf(data[i + 1]);

                val octet = firstIndex.shl(4).or(secondIndex)
                result.set(i.shr(1), octet.toByte())
            }

            return result
        }

        private val HEX_CHARS_ARRAY = "0123456789ABCDEF".toCharArray()
        fun toHex(byteArray: ByteArray) : String {
            val result = StringBuffer()

            byteArray.forEach {
                val octet = it.toInt()
                val firstIndex = (octet and 0xF0).ushr(4)
                val secondIndex = octet and 0x0F
                result.append(HEX_CHARS_ARRAY[firstIndex])
                result.append(HEX_CHARS_ARRAY[secondIndex])
            }

            return result.toString()
        }



        /*fun generateKey(){

        }

        fun E(x:Int, y:Int):Pair<Int,Int>{
            val G = Pair(5,1)
            return G
        }

        fun G2(){
            val s = (((3*G.first*G.first)+2)%17)/((2*G.second)%17)%17

            val x2g = (((s*s)%17) - ((2*G.first)%17))%17
            val y2g = ((s*(G.first-x2g)%17) - G.second)%17

        }
        fun s(x:Int, y:Int):Int{
            return (((3*x*x)+2)%17)/((2*y)%17)%17
        }
        fun x2g(x:Int, s:Int):Int{
            return (((s*s)%17) - ((2*x)%17))%17
        }
        fun y2g(x:Int,y:Int, s:Int, x2g:Int):Int{
            return ((s*(x-x2g)%17) - y)%17
        }*/

    }
}