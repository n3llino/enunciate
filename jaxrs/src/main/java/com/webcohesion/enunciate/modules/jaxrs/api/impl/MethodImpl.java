package com.webcohesion.enunciate.modules.jaxrs.api.impl;

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
import com.webcohesion.enunciate.modules.jaxrs.model.ResourceEntityParameter;
import com.webcohesion.enunciate.modules.jaxrs.model.ResourceMethod;
import com.webcohesion.enunciate.modules.jaxrs.model.ResourceParameter;
import com.webcohesion.enunciate.modules.jaxrs.model.ResourceParameterConstraints;
import com.webcohesion.enunciate.modules.jaxrs.model.ResourceRepresentationMetadata;

/**
 * @author Ryan Heaton
 */
public class MethodImpl implements Method {

	private final String httpMethod;
	private final ResourceMethod resourceMethod;
	private final ResourceGroup group;

	public MethodImpl(String httpMethod, ResourceMethod resourceMethod, ResourceGroup group) {
		this.httpMethod = httpMethod;
		this.resourceMethod = resourceMethod;
		this.group = group;
	}

	@Override
	public Resource getResource() {
		return new ResourceImpl(this.resourceMethod, this.group);
	}

	@Override
	public String getLabel() {
		return this.resourceMethod.getLabel() == null ? this.httpMethod : this.resourceMethod.getLabel();
	}

	@Override
	public String getHttpMethod() {
		return this.httpMethod;
	}

	@Override
	public String getSlug() {
		return this.group.getSlug() + "_" + resourceMethod.getSimpleName() + "_" + this.httpMethod;
	}

	@Override
	public String getDescription() {
		return this.resourceMethod.getJavaDoc().toString();
	}

	@Override
	public String getDeprecated() {
		return ElementUtils.findDeprecationMessage(this.resourceMethod);
	}

	@Override
	public String getSince() {
		JavaDoc.JavaDocTagList tags = this.resourceMethod.getJavaDoc().get("since");
		return tags == null ? null : tags.toString();
	}

	@Override
	public String getVersion() {
		JavaDoc.JavaDocTagList tags = this.resourceMethod.getJavaDoc().get("version");
		return tags == null ? null : tags.toString();
	}

	@Override
	public boolean isIncludeDefaultParameterValues() {
		for (ResourceParameter parameter : this.resourceMethod.getResourceParameters()) {
			if (parameter.getDefaultValue() != null) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isIncludeParameterConstraints() {
		for (ResourceParameter parameter : this.resourceMethod.getResourceParameters()) {
			if (parameter.getConstraints() == null || parameter.getConstraints().getType() == null
					|| parameter.getConstraints().getType() != ResourceParameterConstraints.ResourceParameterContraintType.UNBOUND_STRING) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<? extends Parameter> getParameters() {
		Set<ResourceParameter> resourceParams = this.resourceMethod.getResourceParameters();
		ArrayList<Parameter> parameters = new ArrayList<Parameter>(resourceParams.size());
		for (ResourceParameter param : resourceParams) {
			parameters.add(new ParameterImpl(param));
		}
		return parameters;
	}

	@Override
	public Entity getRequestEntity() {
		ResourceEntityParameter entityParameter = this.resourceMethod.getEntityParameter();
		return entityParameter == null ? null : new RequestEntityImpl(this.resourceMethod, entityParameter);
	}

	@Override
	public List<? extends StatusCode> getResponseCodes() {
		return this.resourceMethod.getStatusCodes();
	}

	@Override
	public Entity getResponseEntity() {
		ResourceRepresentationMetadata responseMetadata = this.resourceMethod.getRepresentationMetadata();
		return responseMetadata == null ? null : new ResponseEntityImpl(this.resourceMethod, responseMetadata);
	}

	@Override
	public List<? extends StatusCode> getWarnings() {
		return this.resourceMethod.getWarnings();
	}

	@Override
	public List<? extends Parameter> getResponseHeaders() {
		Map<String, String> responseHeaders = this.resourceMethod.getResponseHeaders();
		ArrayList<Parameter> headerValues = new ArrayList<Parameter>();
		for (Map.Entry<String, String> responseHeader : responseHeaders.entrySet()) {
			headerValues.add(new ResponseHeaderParameterImpl(responseHeader.getKey(), responseHeader.getValue()));
		}
		return headerValues;
	}

	@Override
	public List<? extends Parameter> getRequestHeaders() {
		Map<String, String> requestHeaders = this.resourceMethod.getRequestHeaders();
		ArrayList<Parameter> headerValues = new ArrayList<Parameter>();
		for (Map.Entry<String, String> requestHeader : requestHeaders.entrySet()) {
			headerValues.add(new RequestHeaderParameterImpl(requestHeader.getKey(), requestHeader.getValue()));
		}
		return headerValues;
	}

	@Override
	public Set<String> getSecurityRoles() {
		return this.resourceMethod.getSecurityRoles();
	}

	@Override
	public Map<String, AnnotationMirror> getAnnotations() {
		return this.resourceMethod.getAnnotations();
	}
}
