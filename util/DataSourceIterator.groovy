package util

import org.lappsgrid.api.*
import org.lappsgrid.client.DataSourceClient
import org.lappsgrid.discrimination.*

class DataSourceIterator implements Iterator<Data> {
	DataSourceClient client
	String[] keys
	int start
	int index
	int end
	
	public DataSourceIterator(DataSourceClient client) {
		this(client, 0)
	}
	
	public DataSourceIterator(DataSourceClient client, int start) {
		this.client = client
		this.keys = client.list()
		this.index = start
		this.start = start
		this.end = keys.size()
	}
	
	public DataSourceIterator(DataSourceClient client, int start, int end) {
		this(client, start)
		this.end = end
	}
	
	Data next() {
		if (index < end) {
			return client.get(keys[index++])
		}
		return new Data(Types.ERROR, "Length exceeded during iteration.")
	}
	
	boolean hasNext() {
		return index < end
	}
	
	void remove() {
		throw new UnsupportedOperationException("MascIterator objects do not support the remove operation.")
	}
	
	int size() {
		return end - start
	}
	
	void reset() {
		index = start
	}
	
	String key() {
		if (index < end) {
			return keys[index]
		}
		return null
	}
	
	void skip() {
		++index
	}
}