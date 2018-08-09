package supervisor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.SiteInfo;
import com.carel.supervisor.dataaccess.dataconfig.SiteInfoList;
import com.carel.supervisor.device.DeviceStatusMgr;

public class SRVLDataTransfer extends HttpServlet
{

	private static final String CHECK = "CHECK";
	private static final String AUTH = "AUTH";
	private static final String GETFILES = "getfiles";
	private static final String GETFILELIST = "getfilelist";
	private static final String SETIPS = "dialin";
	private static final String CHUNKS = "chunks";

	private static final String FILEPATH = "C:\\Carel\\PlantVisorPRO\\scheduler\\send";
	private static final String PROPERTIESFILE = "C:\\Carel\\PlantVisorPRO\\scheduler\\conf";
	private boolean nomorefiles = false;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		int x = 0;
		x = x;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String filepath = FILEPATH;
		Properties p = new Properties();
		String zippedfilename = "";
		try
		{
			p.load(new FileInputStream(PROPERTIESFILE));
			filepath = System.getenv("PVPRO_HOME") + "/" + p.getProperty("sourcepath");
		} catch (Exception e)
		{
			filepath = FILEPATH;
		}
		Logger logger = LoggerMgr.getLogger(this.getClass());
		Map parms = request.getParameterMap();
		String cmd = ((String[]) parms.get("cmd"))[0];
		if (CHECK.equalsIgnoreCase(cmd))
		{
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.print("OK");
			out.flush();
			out.close();
		}
		if (AUTH.equalsIgnoreCase(cmd))
		{
			PrintWriter out = response.getWriter();
			StringBuffer sb = new StringBuffer();
			SiteInfo si = null;
			String status = "1";
			try
			{
				si = SiteInfoList.retrieveSiteById(1);
			} catch (Exception e)
			{
				logger.error(e);
			}

			try
			{
				if (DeviceStatusMgr.getInstance().existAlarm())
					status = "2";
			} catch (Exception e)
			{
				logger.error(e);
			}
			sb.append("<response for='auth'>");
			sb.append("<user><![CDATA[" + ((si != null) ? si.getCode() : "") + "]]></user>");
			sb.append("<pass><![CDATA[" + ((si != null) ? si.getPassword() : "") + "]]></pass>");
			sb.append("<type><![CDATA[" + ((si != null) ? si.getType() : "") + "]]></type>");
			sb.append("<status><![CDATA[" + status + "]]></status>");
			sb.append("</response>");
			try
			{
				out.write(sb.toString());
			} catch (Exception e)
			{
				logger.error(e);
			}
			out.flush();
			out.close();
		}
		if (GETFILELIST.equalsIgnoreCase(cmd))
		{
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			File f = new File(filepath);
			if (f.isDirectory())
			{
				File[] flist = f.listFiles(new PatternFileFilter());
				if (flist.length > 0)
				{
					for (int i = 0; i < flist.length - 1; i++)
					{
						out.write(flist[i].getName() + "$");
					}
					//out.write(flist[flist.length - 1].getName()); //salto ultimo file (in progress)
				}
			}
			out.write("");
			out.flush();
			out.close();
		}
		if (GETFILES.equalsIgnoreCase(cmd))
		{
			// parsing requested files
			String filelist = ((String[]) parms.get("files"))[0];
			StringTokenizer st = new StringTokenizer(filelist, "$");
			ArrayList<File> lf = new ArrayList<File>();
			for (int x = 0; st.hasMoreTokens(); x++)
			{
				lf.add(new File(FILEPATH + "\\" + st.nextToken()));
			}
			if (lf.size() > 0)
			{
				// >0 files -> zip and send
				Date d = new Date(System.currentTimeMillis());
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd-HHmm");
				zippedfilename = df.format(d) + ".zip";

				boolean preservefiles = Boolean.parseBoolean(((String[]) parms.get("preservefiles"))[0]);
				if(new File(filepath).listFiles(new PatternFileFilter()).length<2)
				{
					nomorefiles = true;
				}
				createZip(new File(filepath + "\\" + zippedfilename), lf.toArray(new File[lf.size()]), preservefiles);

				File zipfile = new File(filepath + "\\" + zippedfilename);
				FileInputStream fis = new FileInputStream(zipfile);
				byte[] bf = new byte[4096];
				int len;
				response.setContentType("application/zip");
				OutputStream out1 = response.getOutputStream();
				while ((len = fis.read(bf)) > 0)
				{
					out1.write(bf, 0, len);
				}
				out1.flush();
				out1.close();
				fis.close();
				if(preservefiles)
				{
					Iterator<File> i = lf.iterator();
					while(i.hasNext())
					{
						File fin = i.next();
						File newfile = fin;
						while ((newfile = new File(newfile.toString() + ".sent")).exists());
						fin.renameTo(newfile);
					}
				}else
				{
					Iterator<File> i = lf.iterator();
					while(i.hasNext())
					{
						i.next().delete();
					}
					zipfile.delete();
				}
			} else
			{
				response.setContentType("text/plain");
				OutputStream out1 = response.getOutputStream();
				out1.write("".getBytes());
				out1.flush();
				out1.close();
			}
		}
		if (SETIPS.equalsIgnoreCase(cmd))
		{
			try
			{
				Socket s = new Socket(InetAddress.getLocalHost(), 10001);
				OutputStream sos = s.getOutputStream();
				String cmdd = "setips;" + ((String[]) parms.get("local"))[0] + ";" + ((String[]) parms.get("remote"))[0];
				sos.write(cmdd.getBytes());
				sos.flush();
				sos.close();
			} catch (Exception e)
			{
				logger.error(e);
				e.printStackTrace();
			}
		}
		if (CHUNKS.equalsIgnoreCase(cmd))
		{
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			File f = new File(filepath);
			if (nomorefiles)
			{
				out.write("0");
			} else 
			{
				if (f.isDirectory())
				{
					File[] flist = f.listFiles(new PatternFileFilter());
					out.write("" + (flist.length-1));
				} else
				{
					out.write("0");
				}
			}
			out.write("");
			out.flush();
			out.close();
			nomorefiles = false;
		}

	}

	public void createZip(File zipfilename, File[] contents, boolean preservefiles)
	{
		if (contents.length == 0)
			return;
		try
		{
			BufferedInputStream origin = null;
			FileOutputStream dest = new FileOutputStream(zipfilename);
			CheckedOutputStream checksum = new CheckedOutputStream(dest, new Adler32());
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(checksum));
			// out.setMethod(ZipOutputStream.DEFLATED);
			byte data[] = new byte[4096];
			// get a list of files from current directory
			for (int i = 0; i < contents.length; i++)
			{
				FileInputStream fi = new FileInputStream(contents[i]);
				origin = new BufferedInputStream(fi, 4096);
				ZipEntry entry = new ZipEntry(contents[i].getName());
				out.putNextEntry(entry);
				int count;
				while ((count = origin.read(data, 0, 4096)) != -1)
				{
					out.write(data, 0, count);
				}
				origin.close();
			}
			out.close();
		} catch (FileNotFoundException e)
		{
			LoggerMgr.getLogger(this.getClass()).error(e);
			e.printStackTrace();
		} catch (IOException e)
		{
			LoggerMgr.getLogger(this.getClass()).error(e);
			e.printStackTrace();
		}
	}

	private class PatternFileFilter implements FileFilter
	{
		public boolean accept(File f)
		{
			// if (!f.getName().endsWith(".zip") &&
			// !f.getName().endsWith(".sent") && f.isFile())
			if (Pattern.compile("\\d{8}-\\d{10}.txt").matcher(f.getName()).matches())
				return true;
			else
				return false;
		}
	}
}
