package com.carel.supervisor.presentation.AlEvExport;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface IAlEvExport {
	public void startExport(String filename, String language) throws FileNotFoundException;
	public void setTitle(String title);
	public void setColumnName(String[] colNames) throws IOException;
	public void writeRow(String[] row) throws IOException;
	public void endExport() throws IOException;
	
	public String getExtension();
	public void setExtension(String ext);
	
	public String getFileName();
	public void setSiteName(String sitename);
	public void setFilePath(String filePath);
}
