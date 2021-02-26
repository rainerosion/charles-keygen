package cc.rainss.charleskeygen.utils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;

/**
 * @author rainerosion
 */
public class GenerateSerialNumber {

    private static final String strTitle = "Charles Web Debugging Proxy";


    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] =
                    (byte)((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public String calculateSerial(String name) {
        int serialCheckSum = 1418211210;
        int nameCheckSum = calcNameChecksum(name);
        serialCheckSum ^= nameCheckSum;
        long serial = serialCheckSum;
        serial <<= 32L;
        serial >>>= 32L;
        serial <<= 32L;
        serial |= 0x1CAD6BCL;
        int serialLow = (int)(serial & 0xFFFFFFFFFFFFFFFFL);
        int serialHigh = (int)(serial >>> 32L & 0xFFFFFFFFFFFFFFFFL);
        int[] serialEnc = new int[2];
        serialEnc[0] = serialLow;
        serialEnc[1] = serialHigh;
        int[] serialDec = new int[2];
        RC5 serialDecrypter = new RC5();
        serialDecrypter.RC5_SETUP(-334581843, -1259282228);
        serialDecrypter.RC5_DECRYPT(serialEnc, serialDec);
        long serialDecrypted = (serialDec[1] & 0xFFFFFFFFL) << 32L;
        serialDecrypted |= serialDec[0] & 0xFFFFFFFFL;
        int xorCheckSum = calcXorChecksum(serial);
        String strSerial = Integer.toHexString(xorCheckSum) + Long.toHexString(serialDecrypted);
        strSerial = String.format("%02X", new Object[] { Integer.valueOf(xorCheckSum) }) + String.format("%016X", new Object[] { Long.valueOf(serialDecrypted) });
        return strSerial;
    }

    private final int calcXorChecksum(long l) {
        long l2 = 0L;
        for (int i = 56; i >= 0; i -= 8) {
            l2 ^= l >>> i & 0xFFL;
        }
        return Math.abs((int)(l2 & 0xFFL));
    }

    public int calcNameChecksum(String strName) {
        byte[] byteArrayName = null;
        try {
            byteArrayName = strName.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            System.out.println(ex.toString());
        }
        int nameLength = byteArrayName.length;
        int n = nameLength + 4;
        if (n % 8 != 0) {
            n += 8 - n % 8;
        }
        byte[] arrby4 = new byte[n];
        System.arraycopy(byteArrayName, 0, arrby4, 4, nameLength);
        arrby4[0] = (byte)(nameLength >> 24);
        arrby4[1] = (byte)(nameLength >> 16);
        arrby4[2] = (byte)(nameLength >> 8);
        arrby4[3] = (byte)nameLength;
        RC5 r = new RC5();
        r.RC5_SETUP(1763497072, 2049034577);
        byte[] outputArray = r.RC5_EncryptArray(arrby4);
        int n3 = 0;
        for (byte by : outputArray) {
            n3 ^= by;
            n3 = n3 << 3 | n3 >>> 29;
        }
        return n3;
    }
}
