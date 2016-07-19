/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.postgis3d;

import org.geotools.jdbc3d.JDBCDataStoreAPIOnlineTest;
import org.geotools.jdbc3d.JDBCDataStoreAPITestSetup;

/**
 * 
 *
 * @source $URL$
 */
public class PostgisDataStoreAPIOnlineTest extends JDBCDataStoreAPIOnlineTest {

    @Override
    protected JDBCDataStoreAPITestSetup createTestSetup() {
        return new PostgisDataStoreAPITestSetup(new PostGISTestSetup());
    }

    @Override
    public void testGetFeatureWriterConcurrency() throws Exception {
        // postgis will lock indefinitely, won't throw an exception
    }
}