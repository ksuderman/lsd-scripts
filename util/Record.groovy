package util

class Record implements Comparable<Record> {
    String key
    int size
    int words
    String category
    String genre

    int compareTo(Record record) {
        return record.size - this.size
    }

    String toString() {
        return "${key}\t${words}\t${size}\t${category}\t${genre}"
    }
}


