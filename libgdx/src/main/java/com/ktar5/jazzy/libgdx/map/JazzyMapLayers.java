package com.ktar5.jazzy.libgdx.map;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.ClassReflection;

import java.util.Iterator;

public class JazzyMapLayers implements Iterable<JazzyMapLayer> {
    private Array<JazzyMapLayer> layers = new Array<JazzyMapLayer>();
    
    /**
     * @param index
     * @return the MapLayer at the specified index
     */
    public JazzyMapLayer get(int index) {
        return layers.get(index);
    }
    
    /**
     * @param name
     * @return the first map having the specified name, if one exists, otherwise null
     */
    public JazzyMapLayer get(String name) {
        for (int i = 0, n = layers.size; i < n; i++) {
            JazzyMapLayer layer = layers.get(i);
            if (name.equals(layer.getName())) {
                return layer;
            }
        }
        return null;
    }
    
    /**
     * Get the index of the map having the specified name, or -1 if no such map exists.
     */
    public int getIndex(String name) {
        return getIndex(get(name));
    }
    
    /**
     * Get the index of the map in the collection, or -1 if no such map exists.
     */
    public int getIndex(JazzyMapLayer layer) {
        return layers.indexOf(layer, true);
    }
    
    /**
     * @return number of layers in the collection
     */
    public int getCount() {
        return layers.size;
    }
    
    /**
     * @param layer map to be added to the set
     */
    public void add(JazzyMapLayer layer) {
        this.layers.add(layer);
    }
    
    /**
     * @param index removes map at index
     */
    public void remove(int index) {
        layers.removeIndex(index);
    }
    
    /**
     * @param layer map to be removed
     */
    public void remove(JazzyMapLayer layer) {
        layers.removeValue(layer, true);
    }
    
    /**
     * @param type
     * @return array with all the layers matching type
     */
    public <T extends JazzyMapLayer> Array<T> getByType(Class<T> type) {
        return getByType(type, new Array<T>());
    }
    
    /**
     * @param type
     * @param fill array to be filled with the matching layers
     * @return array with all the layers matching type
     */
    public <T extends JazzyMapLayer> Array<T> getByType(Class<T> type, Array<T> fill) {
        fill.clear();
        for (int i = 0, n = layers.size; i < n; i++) {
            JazzyMapLayer layer = layers.get(i);
            if (ClassReflection.isInstance(type, layer)) {
                fill.add((T) layer);
            }
        }
        return fill;
    }
    
    /**
     * @return iterator to set of layers
     */
    @Override
    public Iterator<JazzyMapLayer> iterator() {
        return layers.iterator();
    }
    
}