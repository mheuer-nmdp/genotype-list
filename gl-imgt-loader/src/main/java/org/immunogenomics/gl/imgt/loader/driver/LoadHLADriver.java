/*

    gl-imgt-loader  IMGT/HLA database loader for the gl project.
    Copyright (c) 2012-2014 National Marrow Donor Program (NMDP)

    This library is free software; you can redistribute it and/or modify it
    under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation; either version 3 of the License, or (at
    your option) any later version.

    This library is distributed in the hope that it will be useful, but WITHOUT
    ANY WARRANTY; with out even the implied warranty of MERCHANTABILITY or
    FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
    License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this library;  if not, write to the Free Software Foundation,
    Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA.

    > http://www.fsf.org/licensing/licenses/lgpl.html
    > http://www.opensource.org/licenses/lgpl-license.php

 */
package org.immunogenomics.gl.imgt.loader.driver;

import java.io.File;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.immunogenomics.gl.imgt.loader.processor.AlleleSetDataProcessor;
import org.immunogenomics.gl.imgt.xml.model.hla.Alleles;
import org.immunogenomics.gl.imgt.xml.parser.XmlUnmarshaller;

/**
 * @author Adrienne N. Walts (awalts) <awalts@nmdp.org> Operational
 *         Bioinformatics, National Marrow Donor Program
 *
 */
public class LoadHLADriver {

	private static final Logger LOGGER = Logger.getLogger("mainEventLogger."
			+ LoadHLADriver.class);

	static {
		DOMConfigurator.configure("src/main/resources/log4j.xml");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String pathname = System.getProperty("user.dir");
		String hlaDatabasePath =
				System.getProperty(LoaderConstants.HLA_DATABASE_PATH_SYS_PROP);

		if (hlaDatabasePath != null && !hlaDatabasePath.trim().equals("")) {
			pathname = hlaDatabasePath.trim();
			if (hlaDatabasePath.endsWith(File.separator)) {
				pathname = pathname.substring(0, pathname.length() - 1);
			}
		}

		File xml = new File(pathname + File.separator + LoaderConstants.HLA_DATABASE_FILENAME);

		Date start = null;
		Alleles alleles = null;
		XmlUnmarshaller x = new XmlUnmarshaller();
		AlleleSetDataProcessor alleleSetProcessor = new AlleleSetDataProcessor();

		LOGGER.info("LoadHLADriver.main:: Started loading the hla database from the XML file: "
				+ xml.getPath());

		try {
			start = new Date(System.currentTimeMillis());

			// unmarshal the xml
			alleles = x.unmarshalAlleles(xml);

			// insert the alleles
			alleleSetProcessor.insertAlleleSetBatch(alleles.getAllele());
		} catch (Exception e) {
			LOGGER.error("LoadHLADriver.main:: Exception: " + e);
		}

		LOGGER.info("LoadHLADriver.main:: Finished loading the hla database.");
		LOGGER.info("Start time was: " + start);
		LOGGER.info("end was: " + new Date(System.currentTimeMillis()));

	}

}
