/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.portlet.softwarecatalog.service.persistence;

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.service.persistence.PersistenceExecutionTestListener;
import com.liferay.portal.test.ExecutionTestListeners;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.util.PropsValues;

import com.liferay.portlet.softwarecatalog.NoSuchProductVersionException;
import com.liferay.portlet.softwarecatalog.model.SCProductVersion;
import com.liferay.portlet.softwarecatalog.model.impl.SCProductVersionModelImpl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import java.util.List;

/**
 * @author Brian Wing Shun Chan
 */
@ExecutionTestListeners(listeners =  {
	PersistenceExecutionTestListener.class})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
public class SCProductVersionPersistenceTest {
	@Before
	public void setUp() throws Exception {
		_persistence = (SCProductVersionPersistence)PortalBeanLocatorUtil.locate(SCProductVersionPersistence.class.getName());
	}

	@Test
	public void testCreate() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		SCProductVersion scProductVersion = _persistence.create(pk);

		Assert.assertNotNull(scProductVersion);

		Assert.assertEquals(scProductVersion.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		SCProductVersion newSCProductVersion = addSCProductVersion();

		_persistence.remove(newSCProductVersion);

		SCProductVersion existingSCProductVersion = _persistence.fetchByPrimaryKey(newSCProductVersion.getPrimaryKey());

		Assert.assertNull(existingSCProductVersion);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addSCProductVersion();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		SCProductVersion newSCProductVersion = _persistence.create(pk);

		newSCProductVersion.setCompanyId(ServiceTestUtil.nextLong());

		newSCProductVersion.setUserId(ServiceTestUtil.nextLong());

		newSCProductVersion.setUserName(ServiceTestUtil.randomString());

		newSCProductVersion.setCreateDate(ServiceTestUtil.nextDate());

		newSCProductVersion.setModifiedDate(ServiceTestUtil.nextDate());

		newSCProductVersion.setProductEntryId(ServiceTestUtil.nextLong());

		newSCProductVersion.setVersion(ServiceTestUtil.randomString());

		newSCProductVersion.setChangeLog(ServiceTestUtil.randomString());

		newSCProductVersion.setDownloadPageURL(ServiceTestUtil.randomString());

		newSCProductVersion.setDirectDownloadURL(ServiceTestUtil.randomString());

		newSCProductVersion.setRepoStoreArtifact(ServiceTestUtil.randomBoolean());

		_persistence.update(newSCProductVersion, false);

		SCProductVersion existingSCProductVersion = _persistence.findByPrimaryKey(newSCProductVersion.getPrimaryKey());

		Assert.assertEquals(existingSCProductVersion.getProductVersionId(),
			newSCProductVersion.getProductVersionId());
		Assert.assertEquals(existingSCProductVersion.getCompanyId(),
			newSCProductVersion.getCompanyId());
		Assert.assertEquals(existingSCProductVersion.getUserId(),
			newSCProductVersion.getUserId());
		Assert.assertEquals(existingSCProductVersion.getUserName(),
			newSCProductVersion.getUserName());
		Assert.assertEquals(Time.getShortTimestamp(
				existingSCProductVersion.getCreateDate()),
			Time.getShortTimestamp(newSCProductVersion.getCreateDate()));
		Assert.assertEquals(Time.getShortTimestamp(
				existingSCProductVersion.getModifiedDate()),
			Time.getShortTimestamp(newSCProductVersion.getModifiedDate()));
		Assert.assertEquals(existingSCProductVersion.getProductEntryId(),
			newSCProductVersion.getProductEntryId());
		Assert.assertEquals(existingSCProductVersion.getVersion(),
			newSCProductVersion.getVersion());
		Assert.assertEquals(existingSCProductVersion.getChangeLog(),
			newSCProductVersion.getChangeLog());
		Assert.assertEquals(existingSCProductVersion.getDownloadPageURL(),
			newSCProductVersion.getDownloadPageURL());
		Assert.assertEquals(existingSCProductVersion.getDirectDownloadURL(),
			newSCProductVersion.getDirectDownloadURL());
		Assert.assertEquals(existingSCProductVersion.getRepoStoreArtifact(),
			newSCProductVersion.getRepoStoreArtifact());
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		SCProductVersion newSCProductVersion = addSCProductVersion();

		SCProductVersion existingSCProductVersion = _persistence.findByPrimaryKey(newSCProductVersion.getPrimaryKey());

		Assert.assertEquals(existingSCProductVersion, newSCProductVersion);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail(
				"Missing entity did not throw NoSuchProductVersionException");
		}
		catch (NoSuchProductVersionException nsee) {
		}
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		SCProductVersion newSCProductVersion = addSCProductVersion();

		SCProductVersion existingSCProductVersion = _persistence.fetchByPrimaryKey(newSCProductVersion.getPrimaryKey());

		Assert.assertEquals(existingSCProductVersion, newSCProductVersion);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		SCProductVersion missingSCProductVersion = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingSCProductVersion);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		SCProductVersion newSCProductVersion = addSCProductVersion();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(SCProductVersion.class,
				SCProductVersion.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("productVersionId",
				newSCProductVersion.getProductVersionId()));

		List<SCProductVersion> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		SCProductVersion existingSCProductVersion = result.get(0);

		Assert.assertEquals(existingSCProductVersion, newSCProductVersion);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(SCProductVersion.class,
				SCProductVersion.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("productVersionId",
				ServiceTestUtil.nextLong()));

		List<SCProductVersion> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		SCProductVersion newSCProductVersion = addSCProductVersion();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(SCProductVersion.class,
				SCProductVersion.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property(
				"productVersionId"));

		Object newProductVersionId = newSCProductVersion.getProductVersionId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("productVersionId",
				new Object[] { newProductVersionId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingProductVersionId = result.get(0);

		Assert.assertEquals(existingProductVersionId, newProductVersionId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(SCProductVersion.class,
				SCProductVersion.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property(
				"productVersionId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("productVersionId",
				new Object[] { ServiceTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		if (!PropsValues.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			return;
		}

		SCProductVersion newSCProductVersion = addSCProductVersion();

		_persistence.clearCache();

		SCProductVersionModelImpl existingSCProductVersionModelImpl = (SCProductVersionModelImpl)_persistence.findByPrimaryKey(newSCProductVersion.getPrimaryKey());

		Assert.assertTrue(Validator.equals(
				existingSCProductVersionModelImpl.getDirectDownloadURL(),
				existingSCProductVersionModelImpl.getOriginalDirectDownloadURL()));
	}

	protected SCProductVersion addSCProductVersion() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		SCProductVersion scProductVersion = _persistence.create(pk);

		scProductVersion.setCompanyId(ServiceTestUtil.nextLong());

		scProductVersion.setUserId(ServiceTestUtil.nextLong());

		scProductVersion.setUserName(ServiceTestUtil.randomString());

		scProductVersion.setCreateDate(ServiceTestUtil.nextDate());

		scProductVersion.setModifiedDate(ServiceTestUtil.nextDate());

		scProductVersion.setProductEntryId(ServiceTestUtil.nextLong());

		scProductVersion.setVersion(ServiceTestUtil.randomString());

		scProductVersion.setChangeLog(ServiceTestUtil.randomString());

		scProductVersion.setDownloadPageURL(ServiceTestUtil.randomString());

		scProductVersion.setDirectDownloadURL(ServiceTestUtil.randomString());

		scProductVersion.setRepoStoreArtifact(ServiceTestUtil.randomBoolean());

		_persistence.update(scProductVersion, false);

		return scProductVersion;
	}

	private SCProductVersionPersistence _persistence;
}