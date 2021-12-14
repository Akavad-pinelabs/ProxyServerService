package com.pinelabs.proxyServerService.utils;

public class Utils {

	public  static byte[] str2Bcd(String asc) {
		if (asc == null) {
			return null;
		}
		int len = asc.length();
		int mod = len % 2;
		if (mod != 0) {
			asc = "0" + asc;
			len = asc.length();
		}
		if (len >= 2) {
			len /= 2;
		}
		byte[] bbt = new byte[len];
		byte[] abt = asc.getBytes();

		for (int p = 0; p < asc.length() / 2; p++) {
			int j;
			if ((abt[(2 * p)] >= 97) && (abt[(2 * p)] <= 122)) {
				j = abt[(2 * p)] - 97 + 10;
			} else {
				if ((abt[(2 * p)] >= 65) && (abt[(2 * p)] <= 90))
					j = abt[(2 * p)] - 65 + 10;
				else
					j = abt[(2 * p)] - 48;
			}
			int k;
			if ((abt[(2 * p + 1)] >= 97) && (abt[(2 * p + 1)] <= 122)) {
				k = abt[(2 * p + 1)] - 97 + 10;
			} else {
				if ((abt[(2 * p + 1)] >= 65) && (abt[(2 * p + 1)] <= 90))
					k = abt[(2 * p + 1)] - 65 + 10;
				else {
					k = abt[(2 * p + 1)] - 48;
				}
			}
			int a = (j << 4) + k;
			byte b = (byte) a;
			bbt[p] = b;
		}
		return bbt;
	}
    
	public static String bcd2Str(byte[] bytes) {
		StringBuilder temp = new StringBuilder(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			byte left = (byte) ((bytes[i] & 0xf0) >>> 4);
			byte right = (byte) (bytes[i] & 0x0f);
			if (left >= 0x0a && left <= 0x0f) {
				left -= 0x0a;
				left += 'A';
			} else {
				left += '0';
			}

			if (right >= 0x0a && right <= 0x0f) {
				right -= 0x0a;
				right += 'A';
			} else {
				right += '0';
			}

			temp.append(String.format("%c", left));
			temp.append(String.format("%c", right));
		}
		return temp.toString();
	}
}
