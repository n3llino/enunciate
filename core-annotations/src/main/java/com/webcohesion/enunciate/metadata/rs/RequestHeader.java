package com.webcohesion.enunciate.metadata.rs;

/**
 * Documents an expected request header for a resource or resource method.
 * 
 * @author Agnello Staibano
 */
public @interface RequestHeader {

	/**
	 * The name of the request header.
	 * 
	 * @return The name of the request header.
	 */
	String name();

	/**
	 * The type of the request header.
	 * 
	 * @return The type of the request header.
	 */
	String type() default "string";

	/**
	 * The description of the request header.
	 * 
	 * @return The description of the request header.
	 */
	String description();

	/**
	 * The required constraint of the request header.
	 * 
	 * @return The required constraint of the request header.
	 */
	boolean required() default true;
}
