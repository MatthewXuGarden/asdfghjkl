package com.carel.supervisor.base.config;

import com.carel.supervisor.base.conversion.*;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import java.io.*;
import java.net.*;
import java.util.*;


public class ResourceLoader extends URLClassLoader
{
    private static final String EMPTY_STR = "";
    private static final String COMMENT_STR = "#";
    private static final String SLASH = "/";
    private static final String CAREL_FILE = "carel.properties";
    private static final String CAREL_DEV_FILE = "carel.developer.properties";
    private static final String DOT_STR = ".properties";
    public static final String CONFIG_ROOT = "carel.config.root";
    public static final String CONFIG_DIR = "carel.config.dir";
    private static Properties properties;
    private static URL configRootUrl;
    private static ResourceLoader resLoaderImpl;
    private static Logger logger = null;
    private transient List configUrls = new ArrayList();

    private ResourceLoader(URL[] urls) throws Exception
    {
        super(urls, ResourceLoader.class.getClassLoader());

        loadPropertyFile(CAREL_FILE);

        for (int i = 0; i < urls.length; i++)
        {
            configUrls.add(urls[i]);
        }
    }

    protected static void init() throws Exception
    {
        List urls = new ArrayList();

        boolean loadFromAltDirs = false;

        String userhome = System.getProperty("user.home");

        if (null != userhome)
        {
            StringBuffer buffer = new StringBuffer(userhome);
            buffer.append(File.separator);
            buffer.append(CAREL_DEV_FILE);

            String fileName = buffer.toString();

            File file = new File(fileName);

            if (file.exists())
            {
                InputStream input = new FileInputStream(file);
                loadProperties(input);
            }
        }

        String propValue = System.getProperty(
                "carel.developer.loadResourcesFromAlternateDirs");

        if ((propValue != null) && propValue.trim().equalsIgnoreCase("true"))
        {
            loadFromAltDirs = true;
        }

        if (loadFromAltDirs)
        {
            if (null != userhome)
            {
                File file = new File(userhome);

                if (file.isDirectory())
                {
                    urls.add(file.toURL());
                }
            }
        }

        propValue = System.getProperty(CONFIG_DIR);

        if (null != propValue)
        {
            File configRootDir = new File(propValue);

            if (configRootDir.isDirectory())
            {
                configRootDir = configRootDir.getCanonicalFile();

                URL oUrl = configRootDir.toURL();
                urls.add(oUrl);
            }
        }

        propValue = System.getProperty(CONFIG_ROOT);

        if (null != propValue)
        {
            File configRootDir = new File(propValue);

            if (configRootDir.isDirectory())
            {
                configRootDir = configRootDir.getCanonicalFile();

                URL configDirUrl = configRootDir.toURL();
                urls.add(configDirUrl);
            }
            else
            {
                /*try
                {*/
                configRootUrl = new URL(propValue);

                /*}
                catch (Throwable e)
                {
                    throw new RuntimeException("[ResourceLoader] Ignoring malformed url formed from \"" + CONFIG_ROOT_PROPERTY +
                                               "\" (" + propValue + ")");
                }*/
            }
        }

        if (loadFromAltDirs)
        {
            propValue = System.getProperty("java.home");

            if (null != propValue)
            {
                File file = new File(propValue);

                if (file.isDirectory())
                {
                    urls.add(file.toURL());
                }
            }

            propValue = System.getProperty("user.dir");

            if (null != propValue)
            {
                File file = new File(propValue);

                if (file.isDirectory())
                {
                    urls.add(file.toURL());
                }
            }
        }

        URL[] localConfigUrls = new URL[urls.size()];

        for (int i = 0; i < urls.size(); i++)
        {
            localConfigUrls[i] = (URL) urls.get(i);
        }

        resLoaderImpl = new ResourceLoader(localConfigUrls);
    }

    public static ResourceLoader getLoader()
    {
        return resLoaderImpl;
    }

    public static URL fromResource(String sResourceName)
        throws ResourceNotFoundException
    {
        URL url = resLoaderImpl.getResource(sResourceName);

        if (null == url)
        {
            url = resLoaderImpl.getResource(sResourceName.concat(DOT_STR));
        }

        if (null == url)
        {
            throw new ResourceNotFoundException("RESOURCE: " + sResourceName);
        }

        return url;
    }

    public static URL fromResource(String sResourceName, String path)
        throws ResourceNotFoundException
    {
        URL url = resLoaderImpl.getResource(sResourceName);

        if (null == url)
        {
            url = resLoaderImpl.getResource(sResourceName.concat(DOT_STR));
        }

        if (null == url)
        {
            try
            {
                File file = new File(path + sResourceName);
                url = file.toURL();
            }
            catch (Exception e)
            {
                throw new ResourceNotFoundException("RESOURCE: " +
                    sResourceName);
            }

            if (null == url)
            {
                throw new ResourceNotFoundException("RESOURCE: " +
                    sResourceName);
            }
        }

        return url;
    }

    public void addConfigRootToList()
    {
        if (null != configRootUrl)
        {
            addUrlToList(configRootUrl);
        }
    }

    public void addUrlToList(URL url)
    {
        if ((null != url) && (!configUrls.contains(url)))
        {
            try
            {
                configUrls.add(url);
                addURL(url);
            }
            catch (Exception e)
            {
                String sMsg =
                    "[ResourceLoader] Ignoring malformed url formed from \"" +
                    CONFIG_ROOT + "\" (" + url.toString() + ")";
                throw new RuntimeException(sMsg);
            }
        }
    }
    
    /*
     * Ritorna una array di URL che rispondono alle seguenti caratteristiche:
     * - siano in una precisa directory;
     * - inizino con un determinato prefisso
     * - abbiano una determinata estensione
     */
    public static URL[] fromResourcePathFiltered(String dir, String extension, String prefix)
        throws Exception
    {
        URL url = ResourceLoader.fromResource(dir);
        URL[] urls = null;
        String urlName = Replacer.replace(url.getFile(), "%20", " ");
        File file = new File(urlName);
        String fileName = "";
        
        if (file.isDirectory())
        {
            File[] files = file.listFiles(new ExtensionFilter(extension));
    
            if (0 < files.length)
            {
                urls = new URL[files.length];
    
                for (int i=0; i<files.length; i++)
                {
                    fileName = files[i].getName();
                    if(fileName != null && fileName.startsWith(prefix))
                        urls[i] = files[i].toURL();
                    else
                        urls[i] = null;
                }
            }
        }
        return urls;
    }
    
    public static URL[] fromResourcePath(String dir, String extension)
        throws Exception
    {
        URL url = ResourceLoader.fromResource(dir);
        URL[] urls = null;
        String urlName = Replacer.replace(url.getFile(), "%20", " ");

        File file = new File(urlName);

        if (file.isDirectory())
        {
            File[] files = file.listFiles(new ExtensionFilter(extension));

            if (0 < files.length)
            {
                urls = new URL[files.length];

                for (int i = 0; i < files.length; i++)
                {
                    urls[i] = files[i].toURL();
                }
            }
        }

        return urls;
    }

    public static URL fileFromResourcePath(String dir, String fileName)
        throws Exception
    {
        URL url = ResourceLoader.fromResource(dir);
        URL urlReturn = null;
        String urlName = Replacer.replace(url.getFile(), "%20", " ");

        File file = new File(urlName);

        if (file.isDirectory())
        {
            File[] files = file.listFiles(new FileNameFilter(fileName));

            if (0 < files.length)
            {
                urlReturn = files[0].toURL();
            }
        }

        return urlReturn;
    }

    public static String[] pathFromResourcePath(String dir)
        throws Exception
    {
        URL url = ResourceLoader.fromResource(dir);
        String urlName = Replacer.replace(url.getFile(), "%20", " ");
        File file = new File(urlName);
        List list = new ArrayList();

        if (file.isDirectory())
        {
            File[] files = file.listFiles();

            if (0 < files.length)
            {
                for (int i = 0; i < files.length; i++)
                {
                    if (files[i].isDirectory())
                    {
                        list.add(files[i].getName());
                    }
                }

                String[] fileName = new String[list.size()];

                return (String[]) list.toArray(fileName);
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }

    public static String getAbsolutePath(String dir) throws Exception
    {
        URL url = ResourceLoader.fromResource(dir);
        String urlName = Replacer.replace(url.getFile(), "%20", " ");
        File file = new File(urlName);

        return file.getAbsolutePath();
    }

    public void addDirToList(String dir) throws Exception
    {
        File file = new File(dir);

        if (file.isDirectory())
        {
            addURL(file.toURL());
        }
    }

    public synchronized String getProperty(String propertyName)
    {
        return getProperties().getProperty(propertyName);
    }

    private static synchronized void loadProperties(InputStream input) throws Exception
    {
        if (null != input)
        {
            Properties newProperties = new Properties();
            newProperties.load(input);

            // carel.developer.properties sono più importanti di carel.properties
            Properties propertiesTmp = getProperties();
            Enumeration keys = newProperties.keys();
            boolean added = false;

            while (keys.hasMoreElements())
            {
                String key = (String) keys.nextElement();

                if (!propertiesTmp.containsKey(key))
                {
                    propertiesTmp.setProperty(key,
                        newProperties.getProperty(key));
                    added = true;
                }
            }

            if (added)
            {
                System.setProperties(propertiesTmp);
            }
        }
    }

    private static Properties getProperties()
    {
        synchronized (ResourceLoader.class) 
        {
    	if (null == properties)
        {
            properties = new Properties(System.getProperties());
        }

        return properties;
        }
    }

    public boolean loadPropertyFile(String propFile)
        throws Exception
    {
        boolean code = false;
        InputStream input = getResourceAsStream(propFile);

        if (null != input)
        {
            loadProperties(input);

            code = true;
        }

        return code;
    }

    public boolean loadListOfJars(String jarListFileName)
        throws JarListException
    {
        try
        {
            URL url = getResource(jarListFileName);

            if (null == url)
            {
                return false;
            }

            InputStream input = getResourceAsStreamAux(url);

            if (null == input)
            {
                return false;
            }

            String baseUrl = url.toExternalForm();
            int index = baseUrl.lastIndexOf(SLASH);

            if (index > 0)
            {
                baseUrl = baseUrl.substring(0, index + 1);
            }

            InputStreamReader inputStreamReader = new InputStreamReader(input);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            while (true)
            {
                String urlName = null;

                try
                {
                    String jarName = bufferedReader.readLine();

                    if (null == jarName)
                    {
                        break;
                    }

                    if (jarName.equals(EMPTY_STR) ||
                            jarName.startsWith(COMMENT_STR))
                    {
                        continue;
                    }

                    urlName = baseUrl.concat(jarName);
                    url = new URL(urlName);
                    addURL(url);
                }
                catch (IOException e)
                {
                    break;
                }
                catch (Exception e)
                {
                    throw new JarListException(e,
                        "Problem loading " + jarListFileName, urlName);
                }
            }
        }
        catch (Exception t)
        {
            throw new JarListException(t, "Problem loading " + jarListFileName,
                jarListFileName);
        }

        return true;
    }

    protected Class loadClass(String name, boolean bResolve)
        throws ClassNotFoundException
    {
        Class className = findLoadedClass(name);

        if (null == className)
        {
            try
            {
                ClassLoader classLoader = Thread.currentThread()
                                                .getContextClassLoader();
                className = classLoader.loadClass(name);
            }
            catch (Exception t)
            {
            	logger = LoggerMgr.getLogger(ResourceLoader.class);
                logger.info("Load class " + name + " 1 TRY ");
            }
        }

        if (null == className)
        {
            try
            {
                className = getParent().loadClass(name);
            }
            catch (Exception t)
            {
            	logger = LoggerMgr.getLogger(ResourceLoader.class);
                logger.info("Load class " + name + " 2 TRY ");
            }
        }

        if (null == className)
        {
            try
            {
                ClassLoader classLoader = getSystemClassLoader();
                className = classLoader.loadClass(name);
            }
            catch (Exception t)
            {
            	logger = LoggerMgr.getLogger(ResourceLoader.class);
                logger.info("Load class " + name + " 3 TRY ");
            }
        }

        if (null == className)
        {
            try
            {
                className = findClass(name);
            }
            catch (Exception t)
            {
            	logger = LoggerMgr.getLogger(ResourceLoader.class);
                logger.info("Load class " + name + " 4 TRY ");
            }
        }

        if (bResolve)
        {
            resolveClass(className);
        }

        return className;
    }

    public Enumeration findConfigResources(String name)
        throws IOException
    {
        Enumeration enumer = super.findResources(name);

        return enumer;
    }

    public Enumeration findResources(String name) throws IOException
    {
        Set resources = null;

        try
        {
            ClassLoader classLoader = Thread.currentThread()
                                            .getContextClassLoader();
            Enumeration enumeration = classLoader.getResources(name);
            Set localResources = loadSetFromEnumeration(null, enumeration);

            if ((localResources != null) && (localResources.size() > 0))
            {
                resources = localResources;
            }
        }
        catch (Exception e)
        {
        	logger = LoggerMgr.getLogger(ResourceLoader.class);
        	logger.info("Find resource " + name + " 1 TRY ");
        }

        try
        {
            ClassLoader classLoader = getParent();
            Enumeration enumeration = classLoader.getResources(name);
            Set localResources = loadSetFromEnumeration(null, enumeration);

            if ((null != localResources) && (localResources.size() > 0))
            {
                if (null == resources)
                {
                    resources = localResources;
                }
                else
                {
                    resources.addAll(localResources);
                }
            }
        }
        catch (Exception t)
        {
        	logger = LoggerMgr.getLogger(ResourceLoader.class);
        	logger.info("Find resource " + name + " 2 TRY ");
        }

        try
        {
            ClassLoader classLoader = getSystemClassLoader();
            Enumeration enumeration = classLoader.getResources(name);
            Set localResources = loadSetFromEnumeration(null, enumeration);

            if ((null != localResources) && (localResources.size() > 0))
            {
                if (null == resources)
                {
                    resources = localResources;
                }
                else
                {
                    resources.addAll(localResources);
                }
            }
        }
        catch (Exception e)
        {
        	logger = LoggerMgr.getLogger(ResourceLoader.class);
        	logger.info("Find resource " + name + " 3 TRY ");
        }

        try
        {
            Enumeration enumeration = findConfigResources(name);
            Set localResources = loadSetFromEnumeration(null, enumeration);

            if ((null != localResources) && (localResources.size() > 0))
            {
                if (null == resources)
                {
                    resources = localResources;
                }
                else
                {
                    resources.addAll(localResources);
                }
            }
        }
        catch (Exception t)
        {
        	logger = LoggerMgr.getLogger(ResourceLoader.class);
        	logger.info("Find resource " + name + " 4 TRY ");
        }

        if (null == resources)
        {
            return null;
        }

        return Collections.enumeration(resources);
    }

    private static Set loadSetFromEnumeration(Set resources,
        Enumeration enumeration)
    {
        Set resourceTmp = resources;

        if (null != enumeration)
        {
            if (null == resourceTmp)
            {
                resourceTmp = new HashSet();
            }

            while (enumeration.hasMoreElements())
            {
                resourceTmp.add(enumeration.nextElement());
            }
        }

        return resourceTmp;
    }

    public URL findResource(String name)
    {
        return getResource(name);
    }

    public URL getResource(String name)
    {
        URL url = null;

        if (null == url)
        {
            try
            {
                ClassLoader classLoader = Thread.currentThread()
                                                .getContextClassLoader();
                url = classLoader.getResource(name);
            }
            catch (Exception t)
            {
            	logger = LoggerMgr.getLogger(ResourceLoader.class);
                logger.info("Get class " + name + " 1 TRY ");
            }
        }

        if (null == url)
        {
            try
            {
                url = getParent().getResource(name);
            }
            catch (Exception t)
            {
            	logger = LoggerMgr.getLogger(ResourceLoader.class);
                logger.info("Get class " + name + " 2 TRY ");
            }
        }

        if (null == url)
        {
            try
            {
                url = getSystemClassLoader().getResource(name);
            }
            catch (Exception t)
            {
            	logger = LoggerMgr.getLogger(ResourceLoader.class);
                logger.info("Get class " + name + " 3 TRY ");
            }
        }

        if (null == url)
        {
            try
            {
                url = super.findResource(name);
            }
            catch (Exception t)
            {
            	logger = LoggerMgr.getLogger(ResourceLoader.class);
                logger.info("Get class " + name + " 4 TRY ");
            }
        }

        return url;
    }

    private InputStream getResourceAsStreamAux(URL url)
    {
        if (null == url)
        {
            return null;
        }

        try
        {
            return url.openStream();
        }
        catch (IOException e)
        {
            return null;
        }
    }

    public InputStream getResourceAsStream(String name)
    {
        URL url = getResource(name);

        return getResourceAsStreamAux(url);
    }

    public static class JarListException extends Exception
    {
        private String jarFileName = null;

        JarListException(Throwable e, String msg, String jarFileNamePar)
        {
            super(msg);
            jarFileName = jarFileNamePar;
        }

        public String getJarFileName()
        {
            return jarFileName;
        }
    }
}
