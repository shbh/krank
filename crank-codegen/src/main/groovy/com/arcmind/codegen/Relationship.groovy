/**
 * 
 */
package com.arcmind.codegen



/**
 * @author richardhightower
 *
 */
public class Relationship{
	String name
	RelationshipType type
	Key key
	JavaClass relatedClass
	
	public String toString() {
		"Relationship(name=${name}, cardinality=${cardinality}, \n key=%{key})"
	}
}
