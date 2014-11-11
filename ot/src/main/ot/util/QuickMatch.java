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

/* Designed to be used in conjunction with ot.util.Horizons */

package ot.util;

import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import gemini.util.MJDUtils;

public class QuickMatch {
    static final String word = "[a-zA-Z]";
    static final String keyValueRegex =
            "\\s*" + word + "+(\\s|\\-)?" + word + "*=\\s*";
    static final String epochRegex = "\\s*\\d+\\.??\\d*?\\s*!{1}=?\\s*\\d{4}-"
            + word + "*-\\d*\\.?\\d*?\\s*\\(" + word + "*\\)\\s*";
    static final String tpRegex = "\\s*\\d{4}-" + word + "*-\\d+\\.?\\d*";
    static final String nameDateRegex = "\\d{4}-" + word + "{3}-\\d{1,2}";
    static final String nameTimeRegex = "\\d{2}:\\d{2}:\\d{2}";
    static final String nameRegex = "^\\s*JPL/HORIZONS\\s+.+\\s+"
            + nameDateRegex + "\\s+" + nameTimeRegex + "\\s*$";
    static final String numberRegex = "\\d*\\.?\\d+((D|E)\\d+)?";
    private static QuickMatch quickmatch;

    Matcher keyValueMatcher;
    static Pattern keyValuePattern;

    static Pattern numberPattern;
    static Matcher numberMatcher;

    static Pattern epochPattern;
    static Matcher epochMatcher;

    static Pattern tpPattern;
    static Matcher tpMatcher;

    static {
        keyValuePattern = Pattern.compile(keyValueRegex);
        numberPattern = Pattern.compile(numberRegex);
        epochPattern = Pattern.compile(epochRegex);
        tpPattern = Pattern.compile(tpRegex);
    }

    public static void main(String[] args) {
        String input = "  EPOCH=  2453800.5 ! 2006-Mar-06.00 (CT)         RMSW= n.a.  ";

        if (args.length != 0) {
            input = args[0];
        }

        QuickMatch match = getInstance();
        TreeMap<String, String> merged = match.parseLine(input);
        printMap(merged);
    }

    public boolean isName(String line) {
        return (line != null && !line.trim().equals("")
                && line.matches(nameRegex));
    }

    public TreeMap<String, String> parseName(String line) {
        TreeMap<String, String> treeMap = new TreeMap<String, String>();

        if (line != null && !line.trim().equals("")) {
            String[] split = line.split(" ");
            String current;
            String output = "";

            for (int index = 0; index < split.length; index++) {
                current = split[index];

                if (current.equals("JPL/HORIZONS")
                        || current.trim().equals("")) {
                    continue;
                }

                if (current.matches(nameDateRegex)
                        || current.matches(nameTimeRegex)) {
                    continue;
                }

                output += (current + " ");
            }

            if (!output.equals("")) {
                treeMap.put("NAME", output.trim());
            }
        }

        return treeMap;
    }

    // Ripped from Horizons for debugging
    public static void printMap(TreeMap<String, String> map) {
        if (map == null) {
            return;
        }

        String key, value;

        while (map.size() != 0) {
            key = map.lastKey();
            value = map.remove(key);
            System.out.println(key + " == " + value);
        }
    }

    private QuickMatch() {
    }

    public static synchronized QuickMatch getInstance() {
        if (quickmatch == null) {
            quickmatch = new QuickMatch();
        }

        return quickmatch;
    }

    public TreeMap<String, String> parseLine(String line) {
        TreeMap<String, String> merged = null;

        if (line != null && !line.trim().equals("")) {
            if (isName(line)) {
                merged = parseName(line);
            } else {
                merged = keyValuePairs(line);
            }
        }

        return merged;
    }

    private TreeMap<String, String> keyValuePairs(String line) {
        TreeMap<String, String> treemap = new TreeMap<String, String>();

        if (line != null && !line.trim().equals("")) {
            line = line.trim();
            keyValueMatcher = keyValuePattern.matcher(line);
            numberMatcher = numberPattern.matcher(line);
            epochMatcher = epochPattern.matcher(line);
            tpMatcher = tpPattern.matcher(line);

            String keyValueGroup;

            while (keyValueMatcher.find()) {
                keyValueGroup = keyValueMatcher.group();
                keyValueGroup = keyValueGroup.replace('=', ' ');
                keyValueGroup = keyValueGroup.trim();
                int end = keyValueMatcher.end();
                Double value = null;
                String match = null;

                if (keyValueGroup.equals("EPOCH") && epochMatcher.find(end)) {
                    match = epochMatcher.group();

                } else if (keyValueGroup.equals("TP") && tpMatcher.find(end)) {
                    match = tpMatcher.group();

                } else if (!keyValueGroup.equals("TP")
                        && numberMatcher.find(end)) {
                    match = numberMatcher.group();
                }

                if (match != null) {
                    value = parseValue(match);
                }

                if (value != null) {
                    treemap.put(keyValueGroup, value.toString());
                }
            }
        }

        return treemap;
    }

    private Double parseValue(String value) {
        Double converted = null;

        if (value != null) {
            if (value.matches(epochRegex)) {
                String[] split = value.split("!");
                value = split[0];
                value = value.trim();

                try {
                    converted = new Double(value);
                } catch (NumberFormatException nfe) {
                }

                converted = MJDUtils.makeMJD(converted);
            } else if (value.matches(tpRegex)) {
                value = value.trim();
                value = value.replace('-', ' ');
                converted = MJDUtils.convertMJD(value);

            } else {
                // See if it's fortranified
                if (value.matches("\\d*\\.?\\d+D\\d+")) {
                    value = value.replace('D', 'E');
                }

                try {
                    converted = new Double(value);
                } catch (NumberFormatException nfe) {
                }
            }
        }

        return converted;
    }
}
