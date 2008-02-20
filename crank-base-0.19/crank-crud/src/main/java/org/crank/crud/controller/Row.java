package org.crank.crud.controller;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

@SuppressWarnings("serial")
class MagicMap implements Map<String, Object>, Serializable {
    private BeanWrapper beanWrapper;
    private BeanWrapper thisWrapper;
    private Map<String, Object> map = new HashMap<String, Object>();
    
    public MagicMap (Object object){
        init(object);
    }

    public void init (Object object){
        beanWrapper = new BeanWrapperImpl(object);
        thisWrapper = new BeanWrapperImpl(this);
    }

    public MagicMap (){
        
    }

    
    public void clear() {
    }

    public boolean containsKey( Object key ) {
        throw new UnsupportedOperationException();
    }

    public boolean containsValue( Object arg0 ) {
        throw new UnsupportedOperationException();
    }

    public Set<java.util.Map.Entry<String, Object>> entrySet() {
        throw new UnsupportedOperationException();
    }

    public Object get( Object oKey ) {
        try {
            String key = (String) oKey;
            if (beanWrapper.isReadableProperty(key)) {
            	return beanWrapper.getPropertyValue( key );
            } else if (thisWrapper.isReadableProperty(key)){
            	return thisWrapper.getPropertyValue( key ) ;
            } else {
            	return this.map.get(oKey);
            }
        } catch (org.springframework.beans.NullValueInNestedPathException nvinpe) {
            return null;
        } catch (org.springframework.beans.NotReadablePropertyException nrpe) {
        	return null;
        }
    }

    public boolean isEmpty() {
        return false;
    }

    public Set<String> keySet() {
        throw new UnsupportedOperationException();
    }

    public Object put( String oKey, Object value ) {
        String key = (String) oKey;
        if (beanWrapper.isWritableProperty(key)) {
        	beanWrapper.setPropertyValue( key, value );
        } else if (thisWrapper.isWritableProperty(key)){
        	thisWrapper.setPropertyValue( key, value );
        } else {
        	return map.put(oKey, value);
        }
        return null;
    }

    public void putAll( Map<? extends String, ? extends Object> arg0 ) {
        throw new UnsupportedOperationException();        
    }

    public Object remove( Object arg0 ) {
        throw new UnsupportedOperationException();
        
    }

    public int size() {
        throw new UnsupportedOperationException();
    }

    public Collection<Object> values() {
        throw new UnsupportedOperationException();
    }

    
}

@SuppressWarnings("serial")
public class Row extends MagicMap implements Serializable {
    private boolean selected;
    private Object object;
    
    public Row (Object object) {
    	super(object);
    	this.object = object;
    }
    
    public Row () {
    	
    }

    @Deprecated
    public Map<String, Object> getMap() {
        return this;
    }

    public Object getObject() {
        return object;
    }

    public void setObject( Object object ) {
        this.object = object;
        super.init(object);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected( boolean selected ) {
        this.selected = selected;
    }

    public String toString() {
    	return String.format("row ((%s) selected=%s)", object, selected);
    }
}
