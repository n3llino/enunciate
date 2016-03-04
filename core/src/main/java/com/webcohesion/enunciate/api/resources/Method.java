package com.webcohesion.enunciate.api.resources;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;

/**
 * @author Ryan Heaton
 */
public interface Method {

	Resource getResource();

	String getLabel();

	String getHttpMethod();

	String getSlug();

	String getDescription();

	String getDeprecated();

	String getSince();

	String getVersion();

	boolean isIncludeDefaultParameterValues();

	List<? extends Parameter> getParameters();

	boolean isIncludeParameterConstraints();

	Entity getRequestEntity();

	List<? extends StatusCode> getResponseCodes();

	Entity getResponseEntity();

	List<? extends StatusCode> getWarnings();

	List<? extends Parameter> getResponseHeaders();

	List<? extends Parameter> getRequestHeaders();

	Set<String> getSecurityRoles();

	Map<String, AnnotationMirror> getAnnotations();
}
