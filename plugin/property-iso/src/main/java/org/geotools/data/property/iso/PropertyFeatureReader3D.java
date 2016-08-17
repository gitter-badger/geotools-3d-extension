/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.property.iso;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.logging.Logger;

import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.property.PropertyFeatureReader;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.WKTReader2;
import org.geotools.geometry.text.WKTParser;
import org.geotools.util.Converters;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.GeometryType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
/**
 * Read a property file directly.
 * <p>
 * This implementation does not perform any filtering or processing; it leaves that up to wrappers 
 * to manipulate the content into the format or projection requested by the user.
 * <p>
 * 
 * <p>
 * The content of this file should start with a the property "_" with the value being the typeSpec 
 * describing the featureType. Thereafter each line will should have a FeatureID as the property and
 * the attributes as the value separated by | characters.
 * </p>
 * 
 * <pre>
 * <code>
 * _=id:Integer|name:String|geom:Geometry
 * fid1=1|Jody|<i>well known text</i>
 * fid2=2|Brent|<i>well known text</i>
 * fid3=3|Dave|<i>well known text</i>
 * </code>
 * </pre>
 * 
 * <p>
 * Many values may be represented by a special tag: <code><null></code>. 
 * An empty element: <code>||</code> is interpreted as the empty string:
 * </p>
 * 
 * <pre>
 *  <code>
 *  fid4=4||<null> -> Feature( id=2, name="", geom=null )
 *  </code>
 * </pre>
 * 
 * @author Jody Garnett (LISAsoft)
 * @author Torben Barsballe (Boundless)
 * 
 * @source $URL$
 * @version $Id
 * @since 8.0
 */
public class PropertyFeatureReader3D extends PropertyFeatureReader {
    private static final Logger LOGGER = Logging.getLogger("org.geotools.data.property");
    BufferedReader reader;
    SimpleFeatureType type;
    String line;
    String next;
    String[] text;
    String fid;

    WKTReader2 wktReader;
    
    public PropertyFeatureReader3D(String namespace, File file) throws IOException {
        this(namespace, file, null);
    }

    public PropertyFeatureReader3D(String namespace, File file, GeometryFactory geometryFactory)
            throws IOException {
        super(namespace, file, geometryFactory);
    }
    
    /**
     * Read attribute in position marked by <code>index</code>.
     * 
     * @param index Attribute position to read
     * 
     * @return Value for the attribtue in position <code>index</code>
     * 
     * @throws IOException
     * @throws ArrayIndexOutOfBoundsException
     */
    public Object read(int index) throws IOException, ArrayIndexOutOfBoundsException {
        if (line == null) {
            throw new IOException("No content available - did you remeber to call next?");
        }
        
        AttributeDescriptor attType = type.getDescriptor(index);
        
        String stringValue = null;
        try {
            // read the value
            stringValue = text[index];
        } catch (RuntimeException e1) {
            e1.printStackTrace();
            stringValue = null;
        }
        // check for special <null> flag
        if ("<null>".equals(stringValue)) {
            stringValue = null;
        }
        if (stringValue == null) {
            if (attType.isNillable()) {
                return null; // it was an explicit "<null>"
            }
        }
        Object value = null;
        
        // Use of Converters to convert from String to requested java binding
        if(attType instanceof GeometryDescriptor && stringValue != null && !stringValue.isEmpty()) {
            
            if(stringValue.contains("Solid") || stringValue.contains("Shell")) {
                org.opengis.geometry.Geometry g = new WKTParser(new GeometryBuilder()).parse(text)
            }
            else {
                try {
                    Geometry geometry = wktReader.read(stringValue);
                    value = Converters.convert(geometry, attType.getType().getBinding());
                } catch (ParseException e) {
                    // to be consistent with converters
                    value = null;
                }
            }
        } else {
            value = Converters.convert(stringValue, attType.getType().getBinding());
        }
        
        if (attType.getType() instanceof GeometryType) {
            // this is to be passed on in the geometry objects so the srs name gets encoded
            CoordinateReferenceSystem crs = ((GeometryType) attType.getType())
                    .getCoordinateReferenceSystem();
            if (crs != null) {
                
                //TODO
                // must be geometry, but check anyway
                //if (value != null && value instanceof Geometry) {
                //    ((Geometry) value).getCoordinateReferenceSystem()
                //}
            }
        }
        return value;
    }
    
    void setWKTReader(WKTReader2 wktReader) {
        this.wktReader = wktReader;
    }
}