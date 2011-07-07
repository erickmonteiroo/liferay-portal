/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
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

package com.liferay.portlet.journal.model;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.BaseModel;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.GroupedModel;
import com.liferay.portal.service.ServiceContext;

import com.liferay.portlet.expando.model.ExpandoBridge;

import java.io.Serializable;

import java.util.Date;

/**
 * The base model interface for the JournalStructure service. Represents a row in the &quot;JournalStructure&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation {@link com.liferay.portlet.journal.model.impl.JournalStructureModelImpl} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link com.liferay.portlet.journal.model.impl.JournalStructureImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see JournalStructure
 * @see com.liferay.portlet.journal.model.impl.JournalStructureImpl
 * @see com.liferay.portlet.journal.model.impl.JournalStructureModelImpl
 * @generated
 */
public interface JournalStructureModel extends BaseModel<JournalStructure>,
	GroupedModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a journal structure model instance should use the {@link JournalStructure} interface instead.
	 */

	/**
	 * Returns the primary key of this journal structure.
	 *
	 * @return the primary key of this journal structure
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this journal structure.
	 *
	 * @param primaryKey the primary key of this journal structure
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the uuid of this journal structure.
	 *
	 * @return the uuid of this journal structure
	 */
	@AutoEscape
	public String getUuid();

	/**
	 * Sets the uuid of this journal structure.
	 *
	 * @param uuid the uuid of this journal structure
	 */
	public void setUuid(String uuid);

	/**
	 * Returns the ID of this journal structure.
	 *
	 * @return the ID of this journal structure
	 */
	public long getId();

	/**
	 * Sets the ID of this journal structure.
	 *
	 * @param id the ID of this journal structure
	 */
	public void setId(long id);

	/**
	 * Returns the group ID of this journal structure.
	 *
	 * @return the group ID of this journal structure
	 */
	public long getGroupId();

	/**
	 * Sets the group ID of this journal structure.
	 *
	 * @param groupId the group ID of this journal structure
	 */
	public void setGroupId(long groupId);

	/**
	 * Returns the company ID of this journal structure.
	 *
	 * @return the company ID of this journal structure
	 */
	public long getCompanyId();

	/**
	 * Sets the company ID of this journal structure.
	 *
	 * @param companyId the company ID of this journal structure
	 */
	public void setCompanyId(long companyId);

	/**
	 * Returns the user ID of this journal structure.
	 *
	 * @return the user ID of this journal structure
	 */
	public long getUserId();

	/**
	 * Sets the user ID of this journal structure.
	 *
	 * @param userId the user ID of this journal structure
	 */
	public void setUserId(long userId);

	/**
	 * Returns the user uuid of this journal structure.
	 *
	 * @return the user uuid of this journal structure
	 * @throws SystemException if a system exception occurred
	 */
	public String getUserUuid() throws SystemException;

	/**
	 * Sets the user uuid of this journal structure.
	 *
	 * @param userUuid the user uuid of this journal structure
	 */
	public void setUserUuid(String userUuid);

	/**
	 * Returns the user name of this journal structure.
	 *
	 * @return the user name of this journal structure
	 */
	@AutoEscape
	public String getUserName();

	/**
	 * Sets the user name of this journal structure.
	 *
	 * @param userName the user name of this journal structure
	 */
	public void setUserName(String userName);

	/**
	 * Returns the create date of this journal structure.
	 *
	 * @return the create date of this journal structure
	 */
	public Date getCreateDate();

	/**
	 * Sets the create date of this journal structure.
	 *
	 * @param createDate the create date of this journal structure
	 */
	public void setCreateDate(Date createDate);

	/**
	 * Returns the modified date of this journal structure.
	 *
	 * @return the modified date of this journal structure
	 */
	public Date getModifiedDate();

	/**
	 * Sets the modified date of this journal structure.
	 *
	 * @param modifiedDate the modified date of this journal structure
	 */
	public void setModifiedDate(Date modifiedDate);

	/**
	 * Returns the structure ID of this journal structure.
	 *
	 * @return the structure ID of this journal structure
	 */
	public String getStructureId();

	/**
	 * Sets the structure ID of this journal structure.
	 *
	 * @param structureId the structure ID of this journal structure
	 */
	public void setStructureId(String structureId);

	/**
	 * Returns the parent structure ID of this journal structure.
	 *
	 * @return the parent structure ID of this journal structure
	 */
	@AutoEscape
	public String getParentStructureId();

	/**
	 * Sets the parent structure ID of this journal structure.
	 *
	 * @param parentStructureId the parent structure ID of this journal structure
	 */
	public void setParentStructureId(String parentStructureId);

	/**
	 * Returns the name of this journal structure.
	 *
	 * @return the name of this journal structure
	 */
	@AutoEscape
	public String getName();

	/**
	 * Sets the name of this journal structure.
	 *
	 * @param name the name of this journal structure
	 */
	public void setName(String name);

	/**
	 * Returns the description of this journal structure.
	 *
	 * @return the description of this journal structure
	 */
	@AutoEscape
	public String getDescription();

	/**
	 * Sets the description of this journal structure.
	 *
	 * @param description the description of this journal structure
	 */
	public void setDescription(String description);

	/**
	 * Returns the xsd of this journal structure.
	 *
	 * @return the xsd of this journal structure
	 */
	@AutoEscape
	public String getXsd();

	/**
	 * Sets the xsd of this journal structure.
	 *
	 * @param xsd the xsd of this journal structure
	 */
	public void setXsd(String xsd);

	public boolean isNew();

	public void setNew(boolean n);

	public boolean isCachedModel();

	public void setCachedModel(boolean cachedModel);

	public boolean isEscapedModel();

	public void setEscapedModel(boolean escapedModel);

	public Serializable getPrimaryKeyObj();

	public void setPrimaryKeyObj(Serializable primaryKeyObj);

	public ExpandoBridge getExpandoBridge();

	public void setExpandoBridgeAttributes(ServiceContext serviceContext);

	public Object clone();

	public int compareTo(JournalStructure journalStructure);

	public int hashCode();

	public CacheModel<JournalStructure> toCacheModel();

	public JournalStructure toEscapedModel();

	public String toString();

	public String toXmlString();
}