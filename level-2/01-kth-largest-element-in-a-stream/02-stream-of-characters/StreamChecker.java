class StreamChecker {

    class TrieNode {
        TrieNode[] children = new TrieNode[26];
        boolean isWord;
    }

    private final TrieNode root;
    private final StringBuilder stream;
    private int maxLength;

    public StreamChecker(String[] words) {
        root = new TrieNode();
        stream = new StringBuilder();

        for (String word : words) {
            maxLength = Math.max(maxLength, word.length());

            TrieNode node = root;

            for (int i = word.length() - 1; i >= 0; i--) {
                int index = word.charAt(i) - 'a';

                if (node.children[index] == null) {
                    node.children[index] = new TrieNode();
                }

                node = node.children[index];
            }

            node.isWord = true;
        }
    }

    public boolean query(char letter) {
        stream.append(letter);

        TrieNode node = root;

        for (int i = stream.length() - 1;
             i >= 0 && stream.length() - i <= maxLength;
             i--) {

            int index = stream.charAt(i) - 'a';

            if (node.children[index] == null) {
                return false;
            }

            node = node.children[index];

            if (node.isWord) {
                return true;
            }
        }

        return false;
    }
}
