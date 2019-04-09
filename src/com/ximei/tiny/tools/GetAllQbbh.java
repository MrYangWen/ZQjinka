package com.ximei.tiny.tools;

public class GetAllQbbh {

	// private String qbbh;
	public String GetQbbh(String qbbh, int flag) {
		if (flag == 8) {
			if (qbbh.length() == 8) {

				return qbbh;
			} else {

				String[] ss = { "00000000", "0000000", "000000", "00000",
						"0000", "000", "00", "0" };
				qbbh = ss[qbbh.length()] + qbbh;
				return qbbh;

			}
		}
		if (flag == 6) {
			if (qbbh.length() == 6) {

				return qbbh;
			} else {

				String[] ss = { "000000", "00000", "0000", "000", "00", "0" };
				qbbh = ss[qbbh.length()] + qbbh;
				return qbbh;

			}

		}
		return null;

	}

}
