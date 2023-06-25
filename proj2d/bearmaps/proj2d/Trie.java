package bearmaps.proj2d;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Trie {
    private Node root;
    private class Node {
        char val;
        HashMap<Character, Node> children;
        String leafContent;

        Node(char c, String content) {
            val = c;
            leafContent = content;
            children = new HashMap<>();
        }

        Node(char c) {
            this(c, "");
        }

        void addChild(char c, Node node) {
            children.put(c, node);
        }

        Node getChild(char c) {
            return children.getOrDefault(c, null);
        }

    }

    public Trie(List<String> strings) {
        root = new Node('\0');
        for (String str: strings) {
            buildTrieHelper(root, str, 0);
        }
    }

    private void buildTrieHelper(Node node, String str, int k) {
        if (str == null || k > str.length()) {
            return;
        } else if (k == str.length()) {
            node.addChild('\0', new Node('\0', str));
            return;
        }

        char c = str.charAt(k);
        Node next = node.getChild(c);
        if (next == null) {
            next = new Node(c);
            node.addChild(c, next);
        }

        buildTrieHelper(next, str, k + 1);
    }

    public List<String> search(String prefix) {
        List<String> matched = new LinkedList<>();
        searchHelper(root, prefix, -1, matched);
        return matched;
    }

    private void searchHelper(Node node, String prefix, int k, List<String> matched) {
        if (node == null || prefix == null || (0 <= k && k < prefix.length() && node.val != prefix.charAt(k))) {
            return;
        }

        if (k >= prefix.length() && !node.leafContent.isEmpty()) {
            matched.add(node.leafContent);
        }

        for (char c: node.children.keySet()) {
            Node child = node.getChild(c);
            searchHelper(child, prefix, k + 1, matched);
        }
    }
}
