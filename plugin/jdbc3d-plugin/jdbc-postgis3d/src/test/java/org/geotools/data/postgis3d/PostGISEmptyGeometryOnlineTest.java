/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.jdbc3d.JDBCEmptyGeometryOnlineTest;
import org.geotools.jdbc3d.JDBCEmptyGeometryTestSetup;

/**
 * 
 *
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/plugin/jdbc/jdbc-postgis/src/test/java/org/geotools/data/postgis/PostGISEmptyGeometryTest.java $
 */
public class PostGISEmptyGeometryOnlineTest extends JDBCEmptyGeometryOnlineTest {

    @Override
    protected JDBCEmptyGeometryTestSetup createTestSetup() {
        return new PostGISEmptyGeometryTestSetup(new PostGISTestSetup());
    }

}