package com.ximei.tiny.tools;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ToDBFfile {

	public void databasetoDBF(String filepath, String newfilepath,
			String[] qbztlist, String[] qbqllist, String[] cbsjlist,
			String[] flaglist,String[] statelist,String[] jzqflaglist) throws IOException {

		DBFReader reader = new DBFReader(new FileInputStream(filepath));

		reader.setCharactersetName("GBK");

		DBFField fields[] = new DBFField[reader.getFieldCount()];

		for (int i = 0; i < reader.getFieldCount(); i++) {

			fields[i] = reader.getField(i);

		}

		DBFWriter writer = new DBFWriter();
		writer.setCharactersetName("GBK");

		writer.setFields(fields);

		Object[] rowValues;

		for (int i = 0; i < reader.getRecordCount(); i++) {

			Object[] rowData = new Object[reader.getFieldCount()];
			rowValues = reader.nextRecord();

			if (i < qbqllist.length) {
				// 取出所以DBF文件信息
				String qbbh = rowValues[0].toString();
				String qbmy = rowValues[1].toString();
				String cbjl = rowValues[2].toString();
				String zdms = rowValues[3].toString();
				String qbll = qbqllist[i];
				String xcsj = cbsjlist[i];
				String cbsj = cbsjlist[i];
				String qbzt = qbztlist[i];
				String qblx = rowValues[8].toString();
				String yhxm = rowValues[9].toString();
				String scds = rowValues[10].toString();
				String scjb = rowValues[11].toString();
				String cbme = qbztlist[i];
				String pjyq = rowValues[13].toString();
				String filed1 =jzqflaglist[i];
				String filed2 = rowValues[15].toString();
				String filed3 = rowValues[16].toString();
				String filed4 = statelist[i];
				String filed5 = flaglist[i];

				// 一行一行写入DBF文件
				rowData[0] = qbbh;
				rowData[1] = qbmy;
				rowData[2] = cbjl;
				rowData[3] = zdms;
				rowData[4] = qbll;
				rowData[5] = xcsj;
				rowData[6] = cbsj;
				rowData[7] = qbzt;
				rowData[8] = qblx;
				rowData[9] = yhxm;
				rowData[10] = scds;
				rowData[11] = scjb;
				rowData[12] = cbme;
				rowData[13] = pjyq;
				rowData[14] = filed1;
				rowData[15] = filed2;
				rowData[16] = filed3;
				rowData[17] = filed4;
				rowData[18] = filed5;

				writer.addRecord(rowData);
			}else{
				
				String qbbh = rowValues[0].toString();
				String qbmy = rowValues[1].toString();
				String cbjl = rowValues[2].toString();
				String zdms = rowValues[3].toString();
				String qbll = rowValues[4].toString();
				String xcsj = rowValues[5].toString();
				String cbsj = rowValues[6].toString();
				String qbzt = rowValues[7].toString();
				String qblx = rowValues[8].toString();
				String yhxm = rowValues[9].toString();
				String scds = rowValues[10].toString();
				String scjb = rowValues[11].toString();
				String cbme = rowValues[12].toString();
				String pjyq = rowValues[13].toString();
				String filed1 = rowValues[14].toString();
				String filed2 = rowValues[15].toString();
				String filed3 = rowValues[16].toString();
				String filed4 = rowValues[17].toString();
				String filed5 = rowValues[18].toString();

				// 一行一行写入DBF文件
				rowData[0] = qbbh;
				rowData[1] = qbmy;
				rowData[2] = cbjl;
				rowData[3] = zdms;
				rowData[4] = qbll;
				rowData[5] = xcsj;
				rowData[6] = cbsj;
				rowData[7] = qbzt;
				rowData[8] = qblx;
				rowData[9] = yhxm;
				rowData[10] = scds;
				rowData[11] = scjb;
				rowData[12] = cbme;
				rowData[13] = pjyq;
				rowData[14] = filed1;
				rowData[15] = filed2;
				rowData[16] = filed3;
				rowData[17] = filed4;
				rowData[18] = filed5;

				writer.addRecord(rowData);
				
			}

		}

		FileOutputStream fos = new FileOutputStream(newfilepath);
		writer.write(fos);
		fos.close();

	}

}