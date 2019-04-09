package com.ximei.tiny.tools;

public class QydmtoHex {

	private String Msgnub;

	public QydmtoHex() {
	}

	public String qydmtohex(String s) {
		if (s.equals("")) {

			Msgnub = "00000000";

		} else {
			String s1 = Long.toHexString(Long.parseLong(s));
			if (s1.length() == 8) {
				String s12 = s1.substring(0, 2);
				String s13 = s1.substring(2, 4);
				String s14 = s1.substring(4, 6);
				Msgnub = (new StringBuilder(String.valueOf(s1.substring(6, 8))))
						.append(s14).append(s13).append(s12).toString();
			}
			if (s1.length() == 7) {
				String s9 = s1.substring(0, 1);
				String s10 = s1.substring(1, 3);
				String s11 = s1.substring(3, 5);
				Msgnub = (new StringBuilder(String.valueOf(s1.substring(5, 7))))
						.append(s11).append(s10).append("0").append(s9)
						.toString();
			}
			if (s1.length() == 6) {
				String s7 = s1.substring(0, 2);
				String s8 = s1.substring(2, 4);
				Msgnub = (new StringBuilder(String.valueOf(s1.substring(4, 6))))
						.append(s8).append(s7).append("00").toString();
			}
			if (s1.length() == 5) {
				String s5 = s1.substring(0, 1);
				String s6 = s1.substring(1, 3);
				Msgnub = (new StringBuilder(String.valueOf(s1.substring(3, 5))))
						.append(s6).append("0").append(s5).append("00")
						.toString();
			}
			if (s1.length() == 4) {
				String s4 = s1.substring(0, 2);
				Msgnub = (new StringBuilder(String.valueOf(s1.substring(2, 4))))
						.append(s4).append("0000").toString();
			}
			if (s1.length() == 3) {
				String s3 = s1.substring(0, 1);
				Msgnub = (new StringBuilder(String.valueOf(s1.substring(1, 3))))
						.append("0").append(s3).append("0000").toString();
			}
			if (s1.length() == 2)
				Msgnub = (new StringBuilder(String.valueOf(s1.substring(0, 2))))
						.append("000000").toString();
			if (s1.length() == 1) {
				String s2 = s1.substring(0, 1);
				Msgnub = (new StringBuilder("0")).append(s2).append("000000")
						.toString();
			}
		}

		return Msgnub.toUpperCase();
	}
}
