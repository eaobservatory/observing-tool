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

import java.io.File;
import java.util.TreeMap;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.StringTokenizer;

// serialising
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Horizons {
    private static boolean caching = true;
    private String cacheDirectory = null;
    private static Horizons horizons = null;

    private Horizons() {
    }

    public static synchronized Horizons getInstance() {
        if (horizons == null) {
            horizons = new Horizons();
        }

        return horizons;
    }

    private String getFileName(String query) {
        query = query.trim().toUpperCase().replace(File.separator, "_");
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

        return success;
    }

    public TreeMap<String, String> resolveName(String name)
            throws HorizonsApi.HorizonsApiException, HorizonsNonUniqueException {
        if ((name == null) || name.trim().equals("")) {
            throw new HorizonsApi.HorizonsApiException("No object name specified.");
        }

        TreeMap<String, String> treeMap = null;

        if (caching) {
            treeMap = readCache(name);
        }

        if (treeMap == null) {
            String result = HorizonsApi.requestObjData(name.trim());

            treeMap = parse(result);

            if (treeMap == null) {
                throw new HorizonsNonUniqueException(result);
            }

            String value = treeMap.get("NAME");

            if (value == null || value.trim().equals("")) {
                throw new HorizonsNonUniqueException(result);
            }

            if (caching) {
                writeCache(treeMap, name);
            }
        }

        return treeMap;
    }

    private TreeMap<String, String> parse(String result) {
        String line;
        TreeMap<String, String> treeMap = new TreeMap<String, String>();
        QuickMatch quickMatch = QuickMatch.getInstance();
        TreeMap<String, String> tmpMap = null;

        StringTokenizer st = new StringTokenizer(result, "\n\r");
        while (st.hasMoreTokens()) {
            line = st.nextToken();

            if (line != null && !line.trim().matches("^No matches found.$")) {
                tmpMap = quickMatch.parseLine(line);

                if (tmpMap != null) {
                    treeMap.putAll(tmpMap);
                }
            }
        }

        return treeMap;
    }

    public static class HorizonsNonUniqueException extends Exception {
        private String searchResults;

        public HorizonsNonUniqueException(String searchResults) {
            super("Search did not return a unique result");
            this.searchResults = searchResults;
        }

        public String getSearchResults() {
            return searchResults;
        }
    }
}
