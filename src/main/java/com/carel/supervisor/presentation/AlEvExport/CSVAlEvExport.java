package com.carel.supervisor.presentation.AlEvExport;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.report.PrinterMgr2;

public class CSVAlEvExport implements IAlEvExport {

	private BufferedWriter fw;
	private String ext;
	private String filename;
	private String filepath;

	public void endExport() throws IOException {
    	fw.flush();
    	fw.close();	
    }

	public void startExport(String nameFile, String language)
			throws FileNotFoundException {
		
		File path = null;
		
		if(filepath!=null)
			path = new File(filepath);
		else
			path = new File(BaseConfig.getCarelPath()+File.separator+PrinterMgr2.getInstance().getSavePath());
			
		
		path.mkdirs();
		
		filename =path.getAbsolutePath()+File.separator+nameFile+"."+ getExtension()  ;
		
		File f = new File(filename);
		fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), Charset.forName("UTF-8")));
	}
	
	public void setColumnName(String[] colNames) throws IOException {
		writeRow(colNames);
	}
	public void writeRow(String[] row) throws IOException {
		for (int i = 0; i < row.length; i++) {
			fw.write(row[i]+";");
		}
		fw.write("\n");
	}

	public String getExtension() {
		return ext;
	}

	public void setExtension(String ext) {
		this.ext=ext;
	}

	public String getFileName() {
		return filename;
	}

	public void setSiteName(String sitename) {
	}

	public void setTitle(String title) {
	}
	
	public void setFilePath(String path)
	{
		filepath = path;
	}

}
