/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.headless.delivery.dto.v1_0;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.liferay.petra.function.UnsafeSupplier;
import com.liferay.petra.string.StringBundler;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.annotations.annotationTypes.GraphQLName;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

import javax.annotation.Generated;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
@GraphQLName("Value")
@JsonFilter("Liferay.Vulcan")
@XmlRootElement(name = "Value")
public class Value {

	@Schema(description = "The content of this field for simple types.")
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@JsonIgnore
	public void setData(UnsafeSupplier<String, Exception> dataUnsafeSupplier) {
		try {
			data = dataUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected String data;

	@Schema(description = "A ContentDocument element.")
	public ContentDocument getDocument() {
		return document;
	}

	public void setDocument(ContentDocument document) {
		this.document = document;
	}

	@JsonIgnore
	public void setDocument(
		UnsafeSupplier<ContentDocument, Exception> documentUnsafeSupplier) {

		try {
			document = documentUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected ContentDocument document;

	@Schema(description = "A point determined by latitude and longitude.")
	public Geo getGeo() {
		return geo;
	}

	public void setGeo(Geo geo) {
		this.geo = geo;
	}

	@JsonIgnore
	public void setGeo(UnsafeSupplier<Geo, Exception> geoUnsafeSupplier) {
		try {
			geo = geoUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected Geo geo;

	@Schema(description = "A ContentDocument element storing an image file.")
	public ContentDocument getImage() {
		return image;
	}

	public void setImage(ContentDocument image) {
		this.image = image;
	}

	@JsonIgnore
	public void setImage(
		UnsafeSupplier<ContentDocument, Exception> imageUnsafeSupplier) {

		try {
			image = imageUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected ContentDocument image;

	@Schema(description = "A link to a page inside the server.")
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@JsonIgnore
	public void setLink(UnsafeSupplier<String, Exception> linkUnsafeSupplier) {
		try {
			link = linkUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected String link;

	@Schema(description = "A link to a JournalArticle stored in the server.")
	public StructuredContentLink getStructuredContentLink() {
		return structuredContentLink;
	}

	public void setStructuredContentLink(
		StructuredContentLink structuredContentLink) {

		this.structuredContentLink = structuredContentLink;
	}

	@JsonIgnore
	public void setStructuredContentLink(
		UnsafeSupplier<StructuredContentLink, Exception>
			structuredContentLinkUnsafeSupplier) {

		try {
			structuredContentLink = structuredContentLinkUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected StructuredContentLink structuredContentLink;

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof Value)) {
			return false;
		}

		Value value = (Value)object;

		return Objects.equals(toString(), value.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		StringBundler sb = new StringBundler();

		sb.append("{");

		sb.append("\"data\": ");

		if (data == null) {
			sb.append("null");
		}
		else {
			sb.append("\"");
			sb.append(data);
			sb.append("\"");
		}

		sb.append(", ");

		sb.append("\"document\": ");

		if (document == null) {
			sb.append("null");
		}
		else {
			sb.append(document);
		}

		sb.append(", ");

		sb.append("\"geo\": ");

		if (geo == null) {
			sb.append("null");
		}
		else {
			sb.append(geo);
		}

		sb.append(", ");

		sb.append("\"image\": ");

		if (image == null) {
			sb.append("null");
		}
		else {
			sb.append(image);
		}

		sb.append(", ");

		sb.append("\"link\": ");

		if (link == null) {
			sb.append("null");
		}
		else {
			sb.append("\"");
			sb.append(link);
			sb.append("\"");
		}

		sb.append(", ");

		sb.append("\"structuredContentLink\": ");

		if (structuredContentLink == null) {
			sb.append("null");
		}
		else {
			sb.append(structuredContentLink);
		}

		sb.append("}");

		return sb.toString();
	}

}