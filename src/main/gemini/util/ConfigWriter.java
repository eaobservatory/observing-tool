/*
 * Copyright (C) 2005-2012 Science and Technology Facilities Council.
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

package gemini.util;

import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.TimeZone;

public class ConfigWriter {
    private String _timeStamp;
    private String shortTimeStamp;
    private int _counter;
    private static ConfigWriter _writer;
    private String _instName = "none";
    private Hashtable<String, String> _lastConfig = null;
    private static int exec_counter = 0;
    private boolean useShortTimeStamp = false;

    private ConfigWriter() {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        SimpleDateFormat longDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");
        SimpleDateFormat shortDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        longDateFormat.setTimeZone(utc);
        shortDateFormat.setTimeZone(utc);

        _timeStamp = longDateFormat.format(new Date())
                        + String.format("%03d", exec_counter++);
        shortTimeStamp = shortDateFormat.format(new Date())
                        + String.format("%03d", exec_counter++);

        exec_counter %= 1000;
        _lastConfig = null;
        _counter = 0;
    }

    public static synchronized ConfigWriter getNewInstance() {
        _writer = null;
        _writer = new ConfigWriter();

        return _writer;
    }

    public static ConfigWriter getCurrentInstance() {
        return _writer;
    }

    public String getCurrentName() {
        return _instName + "_"
                + (useShortTimeStamp ? shortTimeStamp : _timeStamp)
                + "_" + _counter;
    }

    public String getTelFile() {
        return _instName + "_"
                + (useShortTimeStamp ? shortTimeStamp : _timeStamp)
                + ".xml";
    }

    public String getExecName() {
        String execPath = System.getProperty("EXEC_PATH");

        if (execPath == null || execPath.trim().equals("")
                || !(new File(execPath).exists())) {
            execPath = System.getProperty("user.home");
        }

        return (execPath + File.separator
                + _instName + "_"
                + (useShortTimeStamp ? shortTimeStamp : _timeStamp)
                + ".exec");
    }

    public void writeTelFile(String xml) throws IOException {
        synchronized (xml) {
            String confDir = System.getProperty("CONF_PATH");

            if (confDir == null || confDir.equals("")
                    || !(new File(confDir).exists())) {
                confDir = System.getProperty("user.home");
            }

            String fileName = confDir + File.separator + getTelFile();
            BufferedWriter writer =
                    new BufferedWriter(new FileWriter(fileName));

            writer.write(xml);

            writer.flush();
            writer.close();
        }
    }

    public void write(Hashtable<String, String> table) throws IOException {
        synchronized (table) {
            // If the current table is the same as the last, don't bother
            // writing a new one
            if (_lastConfig != null && _lastConfig.equals(table)) {
                return;
            }

            _lastConfig = table;

            // First get the instrument from the hashtable
            _instName = table.get("instrument");

            // The .conf file names were "too long" with the full name
            // of "Michelle" included in them, so we need to omit the
            // millisecond part of the filenames.  Unfortunately we
            // cannot just abbreviate the instrument name as apparently
            // that doesn't work.
            useShortTimeStamp = _instName.equals("Michelle");

            _counter++;

            String confDir = System.getProperty("CONF_PATH");

            if (confDir == null || confDir.equals("")
                    || !(new File(confDir).exists())) {
                confDir = System.getProperty("user.home");
            }

            String fileName = confDir + File.separator + getCurrentName()
                    + ".conf";

            BufferedWriter writer =
                    new BufferedWriter(new FileWriter(fileName));

            write(writer, table);

            writer.flush();
            writer.close();
        }
    }

    private void write(BufferedWriter w, Hashtable<String, String> t)
            throws IOException {
        if (t.containsKey("instrument")) {
            w.write("instrument = " + t.get("instrument"));
            w.newLine();
        }

        Enumeration<String> e = t.keys();

        while (e.hasMoreElements()) {
            String key = e.nextElement();

            if (! key.startsWith("instAper") && ! key.equals("instrument")) {
                w.write(key + " = " + t.get(key));
                w.newLine();
            }
        }
    }
}
