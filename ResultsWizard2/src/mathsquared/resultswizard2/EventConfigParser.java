/**
 * 
 */
package mathsquared.resultswizard2;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Provides methods for reading and writing lists of events from and to files.
 * 
 * @author MathSquared
 * 
 */
public class EventConfigParser {

    /**
     * Loads an event file into a {@link Map} from the event's primary name to its {@linkplain Event event object}.
     * 
     * <p>
     * The general format of the file is as described in {@link Properties#load(java.io.Reader)}, with key-value pairs in a line-oriented format.
     * </p>
     * 
     * <p>
     * The file must contain specific keys and no others. For an event named <code>evt</code>, the respective keys are:
     * </p>
     * 
     * <ul>
     * <li><code>evt`<b>otherNames</b></code>: the other names for this event, in {@linkplain SyntaxParser#parseQuotedSyntax(String) quoted syntax}</li>
     * <li><code>evt`<b>indivPlaces</b></code>: the amount of individual places to recognize in this event</li>
     * <li><code>evt`<b>teamPlaces</b></code>: the amount of team places to recognize in this event</li>
     * <li><code>evt`<b>specialHonors</b></code>: the special honors in this event (see below)</li>
     * <li><code>evt`<b>indivSweeps</b></code>: the amount of sweepstakes points to award for each individual place in the event, in order from first to last, as an {@linkplain SyntaxParser#parseIntegerList(String) integer list}</li>
     * <li><code>evt`<b>teamSweeps</b></code>: the amount of sweepstakes points to award for each team place in the event, in order from first to last, as an integer list</li>
     * <li><code>evt`<b>specialSweeps</b></code>: the amount of sweepstakes points to award for special honors in this event (see below)</li>
     * </ul>
     * 
     * <p>
     * The <code>evt`<b>specialHonors</b></code> field must be represented as successive key-value pairs in quoted syntax. Every successive pair of two list elements is taken as a key, then a value; terminal unmatched elements are ignored. For example, the array
     * </p>
     * 
     * <code>"Top Scorer Biology" "1" "Top Scorer Chemistry" "1" "Top Scorer Physics" "1" "Top Speaker" "2" "Terminal Unmatched Element"</code>
     * 
     * <p>
     * is taken as a map from "Top Scorer Biology" to 1, from "Top Scorer Chemistry" to 1, and so on. The "Terminal Unmatched Element" is discarded.
     * </p>
     * 
     * <p>
     * The <code>evt`<b>specialSweeps</b></code> field is represented as with <code>evt`specialHonors</code>, but the values in each key-value pair are taken as integer lists instead of simple integers.
     * </p>
     * 
     * @param read the {@link Reader} representing the property file
     * @return a <code>Map</code> from events' primary names to event objects
     * @throws IOException if an I/O error occurs when reading the data file
     * @throws NumberFormatException if a number in the data file does not parse correctly when one was expected
     * @throws IllegalArgumentException if an invalid value is given for a field; see {@linkplain Event#Event(String, String[], int, int, Map, int[], int[], Map) the <code>Event</code> constructor}
     */
    public static Map<String, Event> load (Reader read) throws IOException {
        Properties loaded = new Properties();
        loaded.load(read);
        Iterator<String> props = loaded.stringPropertyNames().iterator();

        // Make list of only those properties representing event names (i.e. those that don't contain grave characters)
        Set<String> events = new HashSet<String>();
        while (props.hasNext()) {
            String x = props.next();
            if (x.indexOf('`') < 0) {
                events.add(x);
            }
        }

        Map<String, Event> ret = new HashMap<String, Event>();
        for (String x : events) {
            // Check that the property table contains all data fields
            String[] propertyNames = {"otherNames", "indivPlaces", "teamPlaces", "specialHonors", "indivSweeps", "teamSweeps", "specialSweeps"};
            for (String p : propertyNames) {
                String propertyToCheck = x + "`" + p;
                if (!loaded.containsKey(propertyToCheck)) {
                    throw new IllegalArgumentException("File format error: key \"" + propertyToCheck + "\" missing");
                }
            }

            // Begin to retrieve all of the items to instantiate Event
            String[] otherNames = SyntaxParser.parseQuotedSyntax(loaded.getProperty(x + "`otherNames"));
            int indivPlaces = Integer.parseInt(loaded.getProperty(x + "`indivPlaces"));
            int teamPlaces = Integer.parseInt(loaded.getProperty(x + "`teamPlaces"));
            // Special processing for specialHonors done below
            int[] indivSweeps = SyntaxParser.parseIntegerList(loaded.getProperty(x + "`indivSweeps"));
            int[] teamSweeps = SyntaxParser.parseIntegerList(loaded.getProperty(x + "`teamSweeps"));
            // Special processing for specialSweeps done below

            Map<String, String> specialHonorsRaw = SyntaxParser.createPairwiseMap(SyntaxParser.parseQuotedSyntax(loaded.getProperty(x + "`specialHonors")));
            // We multiply size by 2 since default load factor is 0.75, so no more than 3/4 of the capacity can be taken; with capacity equal to twice the size, this passes with flying colors
            Map<String, Integer> specialHonors = new HashMap<String, Integer>(specialHonorsRaw.size() * 2);
            for (Map.Entry<String, String> entry : specialHonorsRaw.entrySet()) {
                specialHonors.put(entry.getKey(), Integer.parseInt(entry.getValue()));
            }

            Map<String, String> specialSweepsRaw = SyntaxParser.createPairwiseMap(SyntaxParser.parseQuotedSyntax(loaded.getProperty(x + "`specialSweeps")));
            // We multiply size by 2 since default load factor is 0.75, so no more than 3/4 of the capacity can be taken; with capacity equal to twice the size, this passes with flying colors
            Map<String, int[]> specialSweeps = new HashMap<String, int[]>(specialSweepsRaw.size() * 2);
            for (Map.Entry<String, String> entry : specialSweepsRaw.entrySet()) {
                specialSweeps.put(entry.getKey(), SyntaxParser.parseIntegerList(entry.getValue()));
            }

            // Finally, add to the Map
            ret.put(x, new Event(x, otherNames, indivPlaces, teamPlaces, specialHonors, indivSweeps, teamSweeps, specialSweeps));
        }

        // All events processed
        return ret;
    }

    /**
     * Loads an event file into a {@link Map} from the event's primary name to its {@linkplain Event event object}.
     * 
     * @param is the {@link InputStream} representing the data file
     * @return a <code>Map</code> from events' primary names to event objects
     * @throws IOException if an I/O error occurs when reading the data file
     * @throws NumberFormatException if a number in the data file does not parse correctly when one was expected
     * @throws IllegalArgumentException if an invalid value is given for a field; see {@linkplain Event#Event(String, String[], int, int, Map, int[], int[], Map) the <code>Event</code> constructor}
     * @see {@link #load(Reader)}
     */
    public static Map<String, Event> load (InputStream is) throws IOException {
        Reader read = new InputStreamReader(is, Charset.forName("ISO-8859-1")); // ISO-8859-1 is required to be supported
        return load(read);
    }
}
