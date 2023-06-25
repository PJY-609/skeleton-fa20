package bearmaps.proj2d;

import bearmaps.proj2ab.KDTree;
import bearmaps.proj2ab.Point;
import bearmaps.proj2ab.PointSet;
import bearmaps.proj2ab.WeirdPointSet;
import bearmaps.proj2c.streetmap.StreetMapGraph;
import bearmaps.proj2c.streetmap.Node;


import java.util.*;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 *
 * @author Alan Yao, Josh Hug, ________
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {
    private final PointSet pointSet;
    private final Map<Point, Node> pointsToNodes;
    private final Map<String, List<Node>> locNamesToNodes;

    private final Trie trie;

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);

        List<Node> nodes = this.getNodes();
        pointsToNodes = new HashMap<>();
        List<Point> points = new LinkedList<>();
        locNamesToNodes = new HashMap<>();
        List<String> nodeNames = new LinkedList<>();
        for (Node node: nodes) {
            if (node.name() != null) {
                // initialize locNamesToNodes for `public List<Map<String, Object>> getLocations(String locationName)`
                String cleanName = cleanString(node.name());
                List<Node> nodesWithCleanName;
                if (locNamesToNodes.containsKey(cleanName)) {
                    nodesWithCleanName = locNamesToNodes.get(cleanName);
                } else {
                    nodesWithCleanName = new LinkedList<>();
                }
                nodesWithCleanName.add(node);
                locNamesToNodes.put(cleanName, nodesWithCleanName);

                nodeNames.add(cleanName);
            }

            // initialize pointsToNodes for public long closest(double lon, double lat)
            if (!neighbors(node.id()).isEmpty()) {
                Point point = new Point(node.lon(), node.lat());
                pointsToNodes.put(point, node);
                points.add(point);
            }
        }

        trie = new Trie(nodeNames);
        pointSet = new KDTree(points);
    }


    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        Point point = pointSet.nearest(lon, lat);
        Node node = pointsToNodes.get(point);
        return node.id();
    }


    /**
     * For Project Part III (gold points)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        if (prefix == null || prefix.isEmpty()) {
            return new LinkedList<>();
        }

        return trie.search(prefix);
    }

    /**
     * For Project Part III (gold points)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        String cleanName = cleanString(locationName);

        if (!locNamesToNodes.containsKey(cleanName)) {
            return new LinkedList<>();
        }

        List<Map<String, Object>> results = new LinkedList<>();

        List<Node> nodes = locNamesToNodes.get(cleanName);
        for (Node node: nodes) {
            Map<String, Object> info = new HashMap<>();
            info.put("lat", node.lat());
            info.put("lon", node.lon());
            info.put("name", node.name());
            info.put("id", node.id());
            results.add(info);
        }

        return results;
    }


    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

}
