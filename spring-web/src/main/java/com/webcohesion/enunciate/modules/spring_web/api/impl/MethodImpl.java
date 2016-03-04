package com.webcohesion.enunciate.modules.spring_web.api.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;

import com.webcohesion.enunciate.api.resources.Entity;
import com.webcohesion.enunciate.api.resources.Method;
import com.webcohesion.enunciate.api.resources.Parameter;
import com.webcohesion.enunciate.api.resources.Resource;
import com.webcohesion.enunciate.api.resources.ResourceGroup;
import com.webcohesion.enunciate.api.resources.StatusCode;
import com.webcohesion.enunciate.javac.decorations.element.ElementUtils;
import com.webcohesion.enunciate.javac.javadoc.JavaDoc;
import com.webcohesion.enunciate.modules.spring_web.model.RequestMapping;
import com.webcohesion.enunciate.modules.spring_web.model.RequestParameter;
import com.webcohesion.enunciate.modules.spring_web.model.ResourceEntityParameter;
import com.webcohesion.enunciate.modules.spring_web.model.ResourceParameterConstraints;
import com.webcohesion.enunciate.modules.spring_web.model.ResourceRepresentationMetadata;

/**
 * @author Ryan Heaton
 */
public class MethodImpl implements Method {

	private final String httpMethod;
	private final RequestMapping requestMapping;
	private final ResourceGroup group;

	public MethodImpl(String httpMethod, RequestMapping requestMapping, ResourceGroup group) {
		this.httpMethod = httpMethod;
		this.requestMapping = requestMapping;
		this.group = group;
	}

	@Override
	public Resource getResource() {
		return new ResourceImpl(this.requestMapping, this.group);
	}

	@Override
	public String getLabel() {
		return this.requestMapping.getLabel() == null ? this.httpMethod : this.requestMapping.getLabel();
	}

	@Override
	public String getHttpMethod() {
		return this.httpMethod;
	}

	@Override
	public String getSlug() {
		return this.group.getSlug() + "_" + requestMapping.getSimpleName() + "_" + this.httpMethod;
	}

	@Override
	public String getDescription() {
		return this.requestMapping.getJavaDoc().toString();
	}

	@Override
	public String getDeprecated() {
		return ElementUtils.findDeprecationMessage(this.requestMapping);
	}

	@Override
	public String getSince() {
		JavaDoc.JavaDocTagList tags = this.requestMapping.getJavaDoc().get("since");
		return tags == null ? null : tags.toString();
	}

	@Override
	public String getVersion() {
		JavaDoc.JavaDocTagList tags = this.requestMapping.getJavaDoc().get("version");
		return tags == null ? null : tags.toString();
	}

	@Override
	public boolean isIncludeDefaultParameterValues() {
		for (RequestParameter parameter : this.requestMapping.getRequestParameters()) {
			if (parameter.getDefaultValue() != null) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isIncludeParameterConstraints() {
		for (RequestParameter parameter : this.requestMapping.getRequestParameters()) {
			if (parameter.getConstraints() == null || parameter.getConstraints().getType() == null
					|| parameter.getConstraints().getType() != ResourceParameterConstraints.ResourceParameterContraintType.UNBOUND_STRING) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<? extends Parameter> getParameters() {
		Set<RequestParameter> resourceParams = this.requestMapping.getRequestParameters();
		ArrayList<Parameter> parameters = new ArrayList<Parameter>(resourceParams.size());
		for (RequestParameter param : resourceParams) {
			parameters.add(new ParameterImpl(param));
		}
		return parameters;
	}

	@Override
	public Entity getRequestEntity() {
		ResourceEntityParameter entityParameter = this.requestMapping.getEntityParameter();
		return entityParameter == null ? null : new RequestEntityImpl(this.requestMapping, entityParameter);
	}

	@Override
	public List<? extends StatusCode> getResponseCodes() {
		return this.requestMapping.getStatusCodes();
	}

	@Override
	public Entity getResponseEntity() {
		ResourceRepresentationMetadata responseMetadata = this.requestMapping.getRepresentationMetadata();
		return responseMetadata == null ? null : new ResponseEntityImpl(this.requestMapping, responseMetadata);
	}

	@Override
	public List<? extends StatusCode> getWarnings() {
		return this.requestMapping.getWarnings();
	}

	@Override
	public List<? extends Parameter> getResponseHeaders() {
		Map<String, String> responseHeaders = this.requestMapping.getResponseHeaders();
		ArrayList<Parameter> headerValues = new ArrayList<Parameter>();
		for (Map.Entry<String, String> responseHeader : responseHeaders.entrySet()) {
			headerValues.add(new ResponseHeaderParameterImpl(responseHeader.getKey(), responseHeader.getValue()));
		}
		return headerValues;
	}

	@Override
	public List<? extends Parameter> getRequestHeaders() {
		Map<String, String> requestHeaders = this.requestMapping.getRequestHeaders();
		ArrayList<Parameter> headerValues = new ArrayList<Parameter>();
		for (Map.Entry<String, String> requestHeader : requestHeaders.entrySet()) {
			headerValues.add(new RequestHeaderParameterImpl(requestHeader.getKey(), requestHeader.getValue()));
		}
		return headerValues;
	}

	@Override
	public Set<String> getSecurityRoles() {
		return this.requestMapping.getSecurityRoles();
	}

	@Override
	public Map<String, AnnotationMirror> getAnnotations() {
		return this.requestMapping.getAnnotations();
	}
}
