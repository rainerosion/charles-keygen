package cc.rainss.charleskeygen.utils;

public class RC5 {

    public int[] S = new int[26];

    public int P = -1209970333;

    public int Q = -1640531527;

    public byte[] RC5_DecryptArray(byte[] arrby) {
        byte[] arrby2 = new byte[arrby.length];
        int n = arrby.length;
        int n2 = 0;
        long l = 0L;
        int l1 = 0, l2 = 0;
        for (int i = 0; i < n; i++) {
            if (l < 4L) {
                l1 <<= 8;
                l1 |= arrby[i] & 0xFF;
            } else {
                l2 <<= 8;
                l2 |= arrby[i] & 0xFF;
            }
            l++;
            if (++n2 == 8) {
                int[] ct = { l2, l1 };
                int[] pt = { 0, 0 };
                RC5_DECRYPT(ct, pt);
                arrby2[i - 7] = (byte)(pt[1] >>> 24);
                arrby2[i - 6] = (byte)(pt[1] >>> 16);
                arrby2[i - 5] = (byte)(pt[1] >>> 8);
                arrby2[i - 4] = (byte)pt[1];
                arrby2[i - 3] = (byte)(pt[0] >>> 24);
                arrby2[i - 2] = (byte)(pt[0] >>> 16);
                arrby2[i - 1] = (byte)(pt[0] >>> 8);
                arrby2[i] = (byte)pt[0];
                n2 = 0;
                l = 0L;
                l1 = 0;
                l2 = 0;
            }
        }
        return arrby2;
    }

    public byte[] RC5_EncryptArray(byte[] arrby) {
        byte[] arrby2 = new byte[arrby.length];
        int n = arrby.length;
        int n2 = 0;
        long l = 0L;
        int l1 = 0, l2 = 0;
        for (int i = 0; i < n; i++) {
            if (l < 4L) {
                l1 <<= 8;
                l1 |= arrby[i] & 0xFF;
            } else {
                l2 <<= 8;
                l2 |= arrby[i] & 0xFF;
            }
            l++;
            if (++n2 == 8) {
                int[] pt = { l2, l1 };
                int[] ct = { 0, 0 };
                RC5_ENCRYPT(pt, ct);
                arrby2[i - 7] = (byte)(ct[1] >>> 24);
                arrby2[i - 6] = (byte)(ct[1] >>> 16);
                arrby2[i - 5] = (byte)(ct[1] >>> 8);
                arrby2[i - 4] = (byte)ct[1];
                arrby2[i - 3] = (byte)(ct[0] >>> 24);
                arrby2[i - 2] = (byte)(ct[0] >>> 16);
                arrby2[i - 1] = (byte)(ct[0] >>> 8);
                arrby2[i] = (byte)ct[0];
                n2 = 0;
                l = 0L;
                l1 = 0;
                l2 = 0;
            }
        }
        return arrby2;
    }

    void RC5_ENCRYPT(int[] pt, int[] ct) {
        int A = pt[0] + this.S[0];
        int B = pt[1] + this.S[1];
        for (int i = 1; i <= 12; i++) {
            A = ((A ^ B) << (B & 0x1F) | (A ^ B) >>> 32 - (B & 0x1F)) + this.S[2 * i];
            B = ((B ^ A) << (A & 0x1F) | (B ^ A) >>> 32 - (A & 0x1F)) + this.S[2 * i + 1];
        }
        ct[0] = A;
        ct[1] = B;
    }

    void RC5_DECRYPT(int[] ct, int[] pt) {
        int B = ct[1];
        int A = ct[0];
        for (int i = 12; i > 0; i--) {
            B = (B - this.S[2 * i + 1] >>> (A & 0x1F) | B - this.S[2 * i + 1] << 32 - (A & 0x1F)) ^ A;
            A = (A - this.S[2 * i] >>> (B & 0x1F) | A - this.S[2 * i] << 32 - (B & 0x1F)) ^ B;
        }
        pt[1] = B - this.S[1];
        pt[0] = A - this.S[0];
    }

    void RC5_SETUP(int l, int h) {
        int u = 4;
        int[] L = new int[2];
        L[0] = l;
        L[1] = h;
        int i;
        for (this.S[0] = this.P, i = 1; i < 26; i++) {
            this.S[i] = this.S[i - 1] + this.Q;
        }
        int j;
        for (int k = 0, B = i = j = k = 0, A = B; k < 78; k++, i = (i + 1) % 26, j = (j + 1) % 2) {
            A = this.S[i] = this.S[i] + A + B << 3 | this.S[i] + A + B >>> 29;
            B = L[j] = L[j] + A + B << (A + B & 0x1F) | L[j] + A + B >>> 32 - (A + B & 0x1F);
        }
    }

    public static String hex(int n) {
        return String.format("0x%s", new Object[] { Integer.toHexString(n) }).replace(' ', '0');
    }
}
