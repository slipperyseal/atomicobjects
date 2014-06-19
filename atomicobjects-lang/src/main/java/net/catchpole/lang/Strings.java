package net.catchpole.lang;

//   Copyright 2014 catchpole.net
//
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.

import net.catchpole.io.Files;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.util.Collection;

/**
 */
public final class Strings {
    private Strings() {
    }

    /**
     * Converts the first character of a String to upper case if required.
     */
    public static String sentenceCase(String string) {
        return ((string.length() > 1)
                ? string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase()
                : string.toUpperCase());
    }

    public static String javaCase(String string, boolean firstCharUpperCase) {
        StringBuilder sb = new StringBuilder(string.length());
        int l = string.length();
        boolean nextUpper = false;
        for (int x = 0; x < l; x++) {
            char c = string.charAt(x);
            switch (c) {
                case '_':
                case '-':
                case ' ':
                    nextUpper = true;
                    break;
                default:
                    sb.append(nextUpper || (firstCharUpperCase && sb.length() == 0) ? toUpperCase(c) : toLowerCase(c));
                    nextUpper = false;
                    break;
            }
        }
        return sb.toString();
    }

    public static char toUpperCase(char c) {
        return (char) (((c >= 'a') && (c <= 'z')) ? c + ('A' - 'a') : c);
    }


    public static char toLowerCase(char c) {
        return (char) (((c >= 'A') && (c <= 'Z')) ? c - ('A' - 'a') : c);
    }

    /**
     * Converts the first character of a String to lower case if required.
     */
    public static String javaCase(String string) {
        return ((string.length() > 1)
                ? string.substring(0, 1).toLowerCase() + string.substring(1)
                : string.toLowerCase());
    }

    /**
     * Returns a String containing only Alpha Numeric characters.
     * <p/>
     * <p>eg. <code>Commodore '64'.</code> returns <code>Commodore64</code>
     * <p/>
     * <p>If the String does not contain Alpha Numeric characters the original String reference is returned.
     *
     * @param string
     */
    public static String alphaNum(String string) {
        // return new string or original if no change
        if (containsNonAlphaNum(string)) {
            int l = string.length();
            StringBuilder sb = new StringBuilder(l);

            for (int x = 0; x < l; x++) {
                char c = string.charAt(x);
                if (((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z')) || ((c >= '0') && (c <= '9'))) {
                    sb.append(c);
                }
            }
            string = sb.toString();
        }
        return string;
    }

    /**
     * Method containsNonAlphaNum
     *
     * @param string
     */
    public static boolean containsNonAlphaNum(String string) {
        int l = string.length();

        for (int x = 0; x < l; x++) {
            char c = string.charAt(x);

            if (!(((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z'))
                    || ((c >= '0') && (c <= '9')))) {
                return true;
            }
        }

        return false;
    }

    public static boolean isNumeric(String string) {
        int l = string.length();

        for (int x = 0; x < l; x++) {
            char c = string.charAt(x);

            if (!((c >= '0') && (c <= '9'))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Converts a String into a String array by splitting it on a specified character token.
     * <p/>
     * <p>This method operates differently to StringTokenizer or String.split in that it returns empty records for
     * consecutive tokens. Useful in cases such as parsing comma seperated values where two consecutive commas
     * represents an empty record.
     * <p/>
     * <p>eg.  Splitting commas on <pre>This,, , is the,, way</pre> returns <pre>["This","","","is the","","way"]</pre>
     *
     * @param string
     * @param separator
     */
    public static String[] tokenize(String string, char separator) {
        String[] words = new String[string == null ? 0 : instancesOf(string, separator) + 1];

        if (words.length != 0) {
            int x = 0;
            int start = 0;
            int end;

            while ((end = string.indexOf(separator, start)) != -1) {
                words[x++] = (start == end ? "" : string.substring(start, end).trim());
                start = end + 1;
            }

            words[x] = !(start != string.length()) ? "" : string.substring(start).trim();
        }

        if (words.length == 1 && words[0].length() == 0) {
            return new String[0];
        }

        return words;
    }

    /**
     * Converts a String into a String array by splitting it on a specified character token but ignoring
     * all values which when trimed, are empty.
     * <p/>
     * <p>eg.  Splitting commas on <pre>This,, , is the,, way</pre> returns <pre>["This","is the","way"]</pre>
     *
     * @param string
     * @param separator
     */
    public static String[] tokenizeIgnoreEmpty(String string, char separator) {
        String[] words = new String[string == null ? 0 : instancesOfIgnoreConsecutive(string, separator) + 1];

        if (words.length != 0) {
            int x = 0;
            int start = 0;
            int end;

            while ((end = string.indexOf(separator, start)) != -1) {
                String s = (start == end ? "" : string.substring(start, end).trim());
                // only add to array if not empty
                if (s.length() > 0) {
                    words[x++] = s;
                }
                start = end + 1;
            }

            String s = (start == string.length() ? "" : string.substring(start).trim());
            // only add to array if not empty
            if (s.length() > 0) {
                words[x] = s;
            }
        }

        return words;
    }

    /**
     * Returns a String containing the first word of the String (all characters before the first space).
     *
     * @param string
     */
    public static String firstWord(String string) {
        int i = string.indexOf(' ');
        return (i == -1 ? string : string.substring(0, i));
    }

    public static int instancesOf(String string, char c) {
        if (string == null) {
            throw new NullPointerException();
        }
        int l = string.length();
        int inst = 0;

        for (int x = 0; x < l; x++) {
            if (string.charAt(x) == c) {
                inst++;
            }
        }

        return inst;
    }

    /**
     * Counts the number of character tokens in a specified String.
     *
     * @param string
     * @param separator
     */
    public static int instancesOfIgnoreConsecutive(String string, char separator) {
        if (string == null) {
            throw new NullPointerException();
        }
        int l = string.length();
        int inst = 0;
        int last = separator;

        if (l > 0) {
            // count consecutive separators
            for (int x = 0; x < l; x++) {
                int ch = string.charAt(x);
                if (ch != separator && last == separator) {
                    inst++;
                }
                last = ch;
            }
        }

        return inst;
    }

    /**
     * Method arrayToString
     *
     * @param array
     */
    public static String arrayToString(Object[] array, char separator) {
        StringBuilder sb = new StringBuilder((array.length + 1) * 32);
        for (Object value : array) {
            if (value != null) {
                sb.append(value);
                if (separator != 0) {
                    sb.append(separator);
                }
            }
        }
        return sb.toString();
    }

    public static String onlyPrintable(String input) {
        if (isPrintable(input)) {
            return input;
        }

        int l = input.length();
        StringBuilder sb = new StringBuilder(l);
        for (int x = 0; x < l; x++) {
            char ch = input.charAt(x);
            if (isPrintable(ch)) {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    public static boolean isPrintable(String input) {
        int l = input.length();
        for (int x = 0; x < l; x++) {
            if (!isPrintable(input.charAt(x))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isPrintable(char c) {
        return ((c >= 32 && c <= 126) || (c > 160));
    }

    /**
     * Returns the normal class name for an object or for Array types, the component type and square braces.
     * <p/>
     * <p> e.g For a String array, <pre>String[]</pre>.
     *
     * @param object
     */
    public static String smartClassName(Object object) {
        Class clazz = object.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().getName() + '[' + ']';
        }
        return clazz.getName();
    }

    /**
     * Returns the simple class name for an object or for Array types, the component type and square braces.
     * <p/>
     * <p> e.g For a String array, <pre>String[]</pre>.
     *
     * @param object
     */
    public static String smartSimpleClassName(Object object) {
        Class clazz = object.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().getName() + '[' + ']';
        }
        return clazz.getSimpleName();
    }

    public static String smartToString(Object object) {
        Class clazz = object.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().getName() + '[' + Array.getLength(object) + ']';
        }
        if (object instanceof Collection) {
            Collection collection = (Collection) object;
            return collection.getClass().getInterfaces()[0].getSimpleName() + ' ' + collection.size() + " elements";
        }
        return object.toString();
    }

    public static String[] getArgsOverrideFile(String[] args) {
        File file = new File("args.txt");
        if (file.exists()) {
            try {
                return tokenize(new String(Files.loadFile(file)), ' ');
            } catch (IOException ioe) {
                throw Throw.unchecked(ioe);
            }
        }
        return args;
    }

    /**
     * Removes the suffix from a String if that String ends with the suffix.  Else it returns the original String
     * reference.
     *
     * @param string
     * @param suffix
     */
    public static String removeSuffix(String string, String suffix) {
        if (string.endsWith(suffix)) {
            return string.substring(0, string.length() - suffix.length());
        }
        return string;
    }

    /**
     * Returns the stack trace for a Throwable object as a String.
     *
     * @param throwabubble
     */
    public static String getStackTrace(Throwable throwabubble) {
        StringWriter sw = new StringWriter(512);
        PrintWriter pw = new PrintWriter(sw);
        throwabubble.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    /**
     * Returns true if a String is not null and has a length above zero.
     *
     * @param string
     */
    public static boolean hasValue(String string) {
        return string != null && string.length() > 0;
    }

    /**
     * Returns true if an index in a String array exists and its value is not null and has a length above zero.
     *
     * @param array
     * @param index
     */
    public static boolean hasValue(String[] array, int index) {
        return array != null && array.length > index && hasValue(array[index]);
    }

    /**
     * Returns the passed value if a String is not null and has a length above zero, else returns the
     * default value.
     *
     * @param value
     * @param defaultValue
     */
    public static String hasValueElseDefault(String value, String defaultValue) {
        return hasValue(value) ? value : defaultValue;
    }

    /**
     * Returns the array value if an index in a String array exists and its value is not null and has a length
     * above zero, else returns the default value.
     *
     * @param array
     * @param index
     * @param defaultValue
     */
    public static String hasValueElseDefault(String[] array, int index, String defaultValue) {
        String value = array.length > index ? array[index] : null;
        return hasValue(value) ? value : defaultValue;
    }

    public static String toTabbedList(Collection collection) {
        StringBuilder sb = new StringBuilder(64);
        for (Object item : collection) {
            sb.append('\t');
            sb.append(item);
            sb.append('\r');
            sb.append('\n');
        }
        return sb.toString();
    }
}
