package mp2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings("unchecked")
public class MyConcurrentHashmap<K,V> {
	private Object[] maps;
	private int concurrencyCount;
	
	public MyConcurrentHashmap(int concurrencyCount) {
		this.maps = new Object[concurrencyCount];
		this.concurrencyCount = concurrencyCount;
		
		for (int i = 0; i < concurrencyCount; i++) {
			this.maps[i] = new HashMap<K,V>();
		}
	}
	
	// TODO: Implement put()
	// In a thread-safe manner:
	//		Update the V value associated with a given K key.
	// 		To update an object, first lock the particular map segment in which the key lies (hint: use getLockStrip())
	public void put(K key, V val) {
		HashMap<K, V> map = this.getLockStrip(key);

		synchronized(map){
			map.put(key, val);
		}
	}
	
	// TODO: Implement get()
	// In a nonblocking manner:
	//		Return the Optional<V> value for a given K key. Any number of threads can execute get() at once.
	//		If value is null, return Optional.empty();
	public Optional<V> get(K key) {
		V val = null;
		HashMap<K, V> map;

		for (int i = 0; i < this.concurrencyCount; i++) {
			map = ((HashMap<K, V>) this.maps[i]);
			if (map.containsKey(key)) {
				val = map.get(key);
				break;
			}
		}

		return Optional.ofNullable(val);
	}
	
	// Returns lock strip associated with a given Key  
	public HashMap<K,V> getLockStrip(K key) {
		int hash = key.hashCode();
		
		return ((HashMap<K, V>) this.maps[hash % this.concurrencyCount]);
	}
	
	// Returns a snapshot of the union of keys across all maps
	public HashSet<K> keySet() {
		HashSet<K> union = new HashSet<K>();
		
		for (int i = 0; i < concurrencyCount; i++) {
			HashMap<K,V> map = (HashMap<K, V>) this.maps[i];
			
			synchronized(map) {
				union.addAll(map.keySet());
			}
		}
		
		return union;
	}
}
