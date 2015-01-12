/*
 * Copyright (C) 2006-2010 Science and Technology Facilities Council.
 * All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package ot.util;

import java.net.URL;
import java.io.File;
import java.io.BufferedReader;
import java.util.TreeMap;
import java.net.URLEncoder;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.Vector;
import java.io.UnsupportedEncodingException;

// serialising
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Horizons {
    static final String server = "http://ssd.jpl.nasa.gov/";
    static final String script = "horizons_batch.cgi?batch=1";
    private static boolean caching = true;
    private String cacheDirectory = null;
    private static boolean search = false;
    private static Horizons horizons = null;

    private Horizons() {
    }

    public static synchronized Horizons getInstance() {
        if (horizons == null) {
            horizons = new Horizons();
        }

        return horizons;
    }

    public static void main(String args[]) {
        if (args.length == 0) {
            System.exit(-1);
        }

        String inputFileName = args[0];

        if (inputFileName.startsWith("~")) {
            inputFileName = inputFileName.replaceFirst("~",
                    System.getProperty("user.home"));
        }

        Horizons horizon = Horizons.getInstance();
        TreeMap<String, String> treeMap = null;
        treeMap = horizon.resolveFromFile(inputFileName);

        if (treeMap.isEmpty()) {
            treeMap = horizon.resolveName(inputFileName);
        }

        if (treeMap != null) {
            printMap(treeMap);
        }
    }

    private String getFileName(String query) {
        query = query.trim().toUpperCase().replaceAll(File.separator, "_");
        String result = getCacheDirectory() + getVersion() + query + ".map";

        return result;
    }

    private String getVersion() {
        String version = System.getProperty("ot.version");

        if (version == null) {
            version = "";
        }

        return version;
    }

    private String getCacheDirectory() {
        if (cacheDirectory == null) {
            cacheDirectory = System.getProperty("user.home")
                    + File.separator
                    + ".ot" + File.separator;
            File directory = new File(cacheDirectory);

            try {
                // The following should not cause problems as  users should
                // be able to write into their own home directories.
                if (!directory.exists()) {
                    caching = directory.mkdirs();
                }

            } catch (Exception e) {
                caching = false;
                System.out.println("Caching of orbital elements disabled " + e);
            }
        }

        return cacheDirectory;
    }

    @SuppressWarnings("unchecked")
    private TreeMap<String, String> readCache(String query) {
        TreeMap<String, String> treeMap = null;

        if (query != null && !query.trim().equals("")) {
            FileInputStream fileInputStream = null;
            ObjectInputStream objectInputStream = null;
            String fileName = getFileName(query);

            try {
                fileInputStream = new FileInputStream(fileName);
                objectInputStream = new ObjectInputStream(fileInputStream);
                Object tmp = objectInputStream.readObject();

                if (tmp instanceof TreeMap) {
                    TreeMap<String, String> tmpTreeMap =
                            (TreeMap<String, String>) tmp;

                    String cacheTimestamp = tmpTreeMap.get("_CACHE_TIMESTAMP_");

                    if (cacheTimestamp != null) {
                        if (System.currentTimeMillis()
                                - Long.parseLong(cacheTimestamp) < 600000) {
                            tmpTreeMap.remove("_CACHE_TIMESTAMP_");
                            treeMap = tmpTreeMap;
                        }
                    }
                }
                /* we don't care if the map is empty */

            } catch (java.io.FileNotFoundException fnfe) {
                /* we don't care if it is not in the cache */

            } catch (IOException ioe) {
                System.out.println(ioe + " while reading cache file "
                        + fileName);

            } catch (ClassNotFoundException cnfe) {
                /* we already have TreeMap */

            } catch (NumberFormatException e) {
                System.out.println(e
                        + " while reading timestamp from cache file "
                        + fileName);

            } finally {
                try {
                    if (objectInputStream != null) {
                        objectInputStream.close();
                    }

                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }

                } catch (IOException ioe) {
                    System.out.println(ioe + " while closing cache file "
                            + fileName);
                }
            }
        }

        return treeMap;
    }

    private boolean writeCache(TreeMap<String, String> result, String query) {
        // n.b. we still write even if the map is empty
        boolean success = false;

        if (result != null) {
            FileOutputStream fileOutputStream = null;
            ObjectOutputStream objectOutputStream = null;

            if (query != null && !query.trim().equals("")) {
                @SuppressWarnings("unchecked")
                TreeMap<String, String> resultCopy =
                        (TreeMap<String, String>) result.clone();
                resultCopy.put("_CACHE_TIMESTAMP_",
                        Long.toString(System.currentTimeMillis()));

                String fileName = getFileName(query);

                try {
                    fileOutputStream = new FileOutputStream(fileName);
                    objectOutputStream = new ObjectOutputStream(
                            fileOutputStream);
                    objectOutputStream.writeObject(resultCopy);
                    objectOutputStream.flush();
                    fileOutputStream.flush();
                    success = true;

                } catch (IOException ioe) {
                    System.out.println(ioe + " while writing cache");

                } finally {
                    try {
                        if (objectOutputStream != null) {
                            objectOutputStream.close();
                        }

                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        }

                    } catch (IOException ioe) {
                        System.out.println(ioe + " while closing");
                    }
                }
            }
        }

        return success;
    }

    public synchronized Vector<String> searchName(String name) {
        Vector<String> vector = new Vector<String>();

        if (name != null && !name.trim().equals("")) {
            search = true;

            TreeMap<String, String> map = doLookup(name);
            URL lut = URLBuilder(map);

            if (lut != null) {
                vector = connect(lut);
            }

            search = false;
        }

        return vector;
    }

    public TreeMap<String, String> resolveFromFile(String name) {
        TreeMap<String, String> treeMap = new TreeMap<String, String>();

        if (name != null && !name.trim().equals("")) {
            TreeMap<String, String> map;
            map = readInputFile(name);

            URL lut = URLBuilder(map);
            Vector<String> vector = null;

            if (lut != null) {
                vector = connect(lut);

                if (vector != null) {
                    treeMap = parse(vector);
                }
            }
        }

        return treeMap;
    }

    public TreeMap<String, String> resolveName(String name) {
        TreeMap<String, String> treeMap = new TreeMap<String, String>();

        if (name != null && !name.trim().equals("")) {
            if (!name.endsWith(";")) {
                name += ";";
            }

            if (caching) {
                treeMap = readCache(name);
            }

            if (treeMap == null) {
                TreeMap<String, String> map = doLookup(name);

                URL lut = URLBuilder(map);
                Vector<String> vector = null;

                if (lut != null) {
                    vector = connect(lut);

                    if (vector != null) {
                        treeMap = parse(vector);

                        if (caching) {
                            writeCache(treeMap, name);
                        }
                    }
                }
            }
        }

        return treeMap;
    }

    private TreeMap<String, String> doLookup(String name) {
        TreeMap<String, String> treeMap = new TreeMap<String, String>();

        if (name != null && !name.trim().equals("")) {
            treeMap.put("COMMAND", name.trim());
            treeMap.put("MAKE_EPHEM", "YES");
            treeMap.put("TABLE_TYPE", "OBSERVER");
            treeMap.put("R_T_S_ONLY", "YES");

        } else {
            System.out.println("No name given");
        }

        return treeMap;
    }

    public static void printMap(TreeMap<String, String> map) {
        if (map != null) {
            String key, value;

            while (map.size() != 0) {
                key = map.lastKey();
                value = map.remove(key);
                System.out.println(key + " == " + value);
            }
        }
    }

    private TreeMap<String, String> parse(Vector<String> vector) {
        String line;
        TreeMap<String, String> treeMap = new TreeMap<String, String>();
        QuickMatch quickMatch = QuickMatch.getInstance();
        TreeMap<String, String> tmpMap = null;

        while (vector.size() != 0) {
            line = vector.remove(0);

            if (line != null && !line.trim().matches("^No matches found.$")) {
                tmpMap = quickMatch.parseLine(line);

                if (tmpMap != null) {
                    treeMap.putAll(tmpMap);
                }
            }
        }

        return treeMap;
    }

    private Vector<String> connect(URL url) {
        Vector<String> vector = new Vector<String>();

        if (url != null) {
            InputStream stream = null;

            try {
                stream = url.openStream();

                InputStreamReader streamReader = new InputStreamReader(stream);
                BufferedReader buffer = new BufferedReader(streamReader);

                while (!buffer.ready()) {
                }

                String line;
                while ((line = buffer.readLine()) != null) {
                    if (line.trim().matches("\\!\\$\\$SOF")) {
                        break;
                    }

                    vector.add(line);
                }
            } catch (IOException ioe) {
                System.out.println(ioe);
            }

        } else {
            System.out.println("Null URL");
        }

        return vector;
    }

    private URL URLBuilder(TreeMap<String, String> treeMap) {
        URL finalURL = null;
        StringBuffer buffer = new StringBuffer();
        String key, value;

        while (treeMap.size() != 0) {
            key = treeMap.lastKey();
            value = treeMap.remove(key);

            try {
                if (key.trim().equals("COMMAND") && search) {
                    buffer.append("&" + key.trim() + "=" + "'NAME="
                            + URLEncoder.encode(value.trim(), "UTF-8") + "'");
                } else {
                    buffer.append("&" + key.trim() + "="
                            + URLEncoder.encode(value.trim(), "UTF-8"));
                }

            } catch (UnsupportedEncodingException uee) {
                System.out.println("Character encoding not supported" + uee);
                System.exit(-10);
            }
        }

        String url = buffer.toString();
        buffer.delete(0, buffer.length());
        buffer.append(server);
        buffer.append(script);
        buffer.append(url);

        try {
            finalURL = new URL(buffer.toString());
        } catch (MalformedURLException mue) {
            System.out.println(mue);
        }

        return finalURL;
    }

    private TreeMap<String, String> readInputFile(String fileName) {
        String line;
        String[] parts;
        String key, value;
        TreeMap<String, String> treeMap = new TreeMap<String, String>();
        File file = new File(fileName);

        if (file.exists() && file.canRead()) {
            FileReader reader = null;

            try {
                reader = new FileReader(file);
                BufferedReader buffer = new BufferedReader(reader);

                while (!buffer.ready()) {
                }

                while ((line = buffer.readLine()) != null) {
                    parts = line.split("= ");

                    if (parts.length < 2) {
                        continue;
                    }

                    if (parts.length == 2) {
                        key = parts[0].trim();
                        value = parts[1].trim();
                        treeMap.put(key, value);

                    } else {
                        System.out.println("Error ! " + line);

                        break;
                    }
                }

            } catch (FileNotFoundException fnfe) {
                System.out.println(fnfe);

            } catch (IOException ioe) {
                System.out.println(ioe);
            }

        } else {
            System.out.println(fileName + " not available");
        }

        return treeMap;
    }
}
