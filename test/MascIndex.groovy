import org.anc.util.Counter

class MascIndex {

	Map<String,Entry> index = new HashMap<String,Entry>()
	Map<String,Entry> fileNameIndex = [:]
	Map<String,Counter> counts = [:]
		
	public MascIndex() {
	}
	
	void load(String filename) {
		load(new File(filename))
	}
	
	void load(File file) {
		file.eachLine { line ->
			String[] parts = line.split("\t")
			Entry entry = new Entry()
			entry.id = parts[0]
			entry.name = parts[1]
			entry.size = Integer.parseInt(parts[2])
			String[] catref = parts[3].split(" ")
			entry.kind = catref[0]
			entry.genre = catref[1]
			countType(entry.genre, entry.size)
			index.put(entry.id, entry)
			fileNameIndex.put(entry.name, entry)
		}
	}
	
	private void countType(String type, int value) {
		Counter count = counts[type]
		if (count == null) {
			count = new Counter()
			counts[type] = count
		}
		count.add(value)
	}
	
	int size() {
		return index.size()
	}
	
	Entry getByFilename(String filename) {
		return fileNameIndex[filename]
	}
	
	Entry get(String key) {
		return index[key]
	}
	
	String getName(String key) {
		return index[key]?.name
	}
	
	String getKind(String key) {
		return index[key]?.kind 
	}
	
	String getGenre(String key) {
		return index[key]?.genre
	}
	
	int getGenreCount(String genre) {
		return counts[genre]?.count ?: 0
	}
	int getWordCount(String key) {
		return index[key]?.size ?: 0
	}
	
	class Entry {
		String id
		String name
		int size
		String kind
		String genre
	}
}