package ArtCode.hometask2.Hashmap;

import java.util.*;

/**
 * Created by Lazorenko on 23.04.2015.
 */
public class MyHashMap <K,V> implements Map<K,V> {
    private Entry<K, V>[] table;
    private double loadFactor = 0.75;
    private static final int initialCapacity = 15;
    private int currentCapacity = initialCapacity;
    private int size = 0;
    private Set<K> keyS = new LinkedHashSet<>();
    private Collection<V> vals = new LinkedHashSet<>();
    private Set<Object> entS = new LinkedHashSet<>();

    public MyHashMap() {
        this.table = new Entry[currentCapacity];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        for (Object o : keyS) {
            if (o.equals(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        for (Object o : values()) {
            if (o.equals(value)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public V get(Object key) {
        int hash = key.hashCode();
        int position = hash % currentCapacity;
        V forRet=null;

        //Realisation for first position

        while (table[position] != null) {
            if (table[position].key.equals(key)) {
                forRet = (V) table[position].value;
                return forRet;
            }
            table[position] = table[position].next;
        }
        return forRet;
    }

    @Override
    public V put(K key, V value) {
        int hash = key.hashCode();
        int position = hash % currentCapacity;

        //Check for necessity of increasing size
        //Implementing effects of loadfactor via resize of table and transfer
        if (size + 1 > (int) (loadFactor * currentCapacity)) {
            int newCapacity = currentCapacity * 2;

            //Declaration of new field to write our new table in
            Entry[] newTable = new Entry[newCapacity];
            //Refreshing link on table, currentCapacity and size
            table = newTable;
            currentCapacity=newCapacity;
            size = 0;
            //Rehashing table using keySet
            for (Object o: keyS){
                put((K)o, get(o));
            }
        }

        //Adding a new null entry
        if (key == null) {
            //Scenario for empty space
            if (table[0] == null) {
                table[0] = new Entry(key, value);
                //Actions on related methods and lists
                size++;
                keyS.add(key);
                vals.add(value);
                Object Entry = value + "=" + key;
                entS.add(Entry);
                //Scenario for occupied space
            } else {
                Entry iterate = table[0];
                if (iterate.key.equals(key)){
                    V forRet = (V)iterate.value;
                    iterate.value=value;
                    //deals with other lists
                    values().remove(forRet);
                    values().add((V) iterate.value);
                    Object entry = value+"="+key;
                    Object oldEntry = forRet+"="+key;
                    entS.remove(oldEntry);
                    entS.add(entry);
                    return forRet;
                }
                //searching for equal keys in other positions. When not found, places new K,V
                while (iterate.next != null) {
                    if (iterate.key.equals(key)) {
                        V forRet = (V) iterate.value;
                        iterate.value = value;
                        //deals with other lists
                        values().remove(forRet);
                        values().add((V) iterate.value);
                        Object entry = value+"="+key;
                        Object oldEntry = forRet+"="+key;
                        entS.remove(oldEntry);
                        entS.add(entry);
                        return forRet;
                    }
                    iterate = iterate.next;
                }
                iterate.next = new Entry(key, value);
                //Other lists
                keyS.add(key);
                vals.add(value);
                Object entry = value+"="+key;
                entS.add(entry);
                size++;
            }
        }

        //Adding a new non-null entry
        //Scenario for empty space
        if (table[position] == null) {
            table[position] = new Entry(key, value);
            //Actions on related methods and lists
            size++;
            keyS.add(key);
            vals.add(value);
            Object entry = value + "=" + key;
            entS.add(entry);
            //Scenario for occupied space
        } else {
            Entry iter = table[position];
            //case of equal keys on 1st position
            if (iter.key.equals(key)){
                V forRet = (V)iter.value;
                iter.value=value;
                //deals with other lists
                values().remove(forRet);
                values().add((V) iter.value);
                Object entry = value+"="+key;
                Object oldEntry = forRet+"="+key;
                entS.remove(oldEntry);
                entS.add(entry);
                return forRet;
            }
            //searching for equal keys in other positions. When not found, places new K,V
            while (iter.next != null) {
                if (iter.key.equals(key)) {
                    V forRet = (V) iter.value;
                    iter.value = value;
                    //deals with other lists
                    values().remove(forRet);
                    values().add((V) iter.value);
                    Object entry = value+"="+key;
                    Object oldEntry = forRet+"="+key;
                    entS.remove(oldEntry);
                    entS.add(entry);
                    return forRet;
                }
                iter = iter.next;
            }
            iter.next = new Entry(key, value);
            //Other lists
            keyS.add(key);
            vals.add(value);
            Object entry = value+"="+key;
            entS.add(entry);
            size++;
        }
        return null;
    }

    @Override
    public V remove(Object key) {
        int hash = key.hashCode();
        int position = hash % currentCapacity;

        while (table[position] != null) {
            if (table[position].key.equals(key)) {
                keyS.remove(table[position].key);
                vals.remove(table[position].value);
                Object entry = table[position].value+"="+table[position].key;
                entS.remove(entry);
                size--;
                table[position].value=null;
                table[position].key=null;
            }
            table[position] = table[position].next;
        }

        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for(Object o:m.keySet()){
            put((K) o, m.get(o));
        }
    }

    @Override
    public void clear() {
        for (Object o:keyS){
            remove(o);
        }
    }

    @Override
    public Set<K> keySet() {
        return keyS;
    }

    @Override
    public Collection<V> values() {
        return vals;
    }

    @Override
    public Set entrySet() {
        return entS;
    }

    private class Entry<K, V> {

        K key;
        V value;
        Entry next;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Entry other = (Entry) obj;
            if (key != other.key)
                return false;
            if (value != other.value)
                return false;
            return true;
        }
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + Integer.parseInt((String)key);
            result = prime * result + Integer.parseInt((String)value);
            return result;
        }
    }
}
