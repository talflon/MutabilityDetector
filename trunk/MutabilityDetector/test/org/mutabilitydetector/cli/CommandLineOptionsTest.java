/* 
 * Mutability Detector
 *
 * Copyright 2009 Graham Allan
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.mutabilitydetector.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.Test;
import org.mutabilitydetector.cli.CommandLineOptions.ReportMode;



public class CommandLineOptionsTest {

	private CommandLineOptions options;
	private File classListFile;

	@Test
	public void testOptionsCanParseClasspathOption() throws Exception {
		String[] args = makeArgs("-classpath", "fakeClasspath");
		CommandLineOptions options = new CommandLineOptions(args);
		assertEquals("fakeClasspath", options.classpath());
	}
	

	@Test
	public void testToAnalyseRegexCanBeSpecified() throws Exception {
		String[] args = makeArgs("-match", "*.somepackage.*");
		options = new CommandLineOptions(args);
		assertEquals("*.somepackage.*", options.match());
	}
	
	@Test
	public void testVerboseOptionCanBeSetWithShortOpt() throws Exception {
		String[] args = makeArgs("-v");
		options = new CommandLineOptions(args);
		assertEquals(true, options.verbose());
	}
	
	@Test
	public void testVerboseOptionCanBeSetWithLongOpt() throws Exception {
		String[] args = makeArgs("-verbose");
		options = new CommandLineOptions(args);
		assertEquals(true, options.verbose());
	}
	
	@Test
	public void testReportModeCanBeSetToAll() throws Exception {
		String[] args = makeArgs("-report", "all");
		options = new CommandLineOptions(args);
		assertEquals(ReportMode.ALL, options.reportMode());
	}
	
	@Test
	public void testReportModes() throws Exception {
		assertModeAvailable("immutable", ReportMode.IMMUTABLE);
		assertModeAvailable("mutable", ReportMode.MUTABLE);
	}
	
	private void assertModeAvailable(String reportArg, ReportMode reportMode) {
		String[] args = makeArgs("-r", reportArg);
		options = new CommandLineOptions(args);
		assertEquals(reportMode, options.reportMode());
	}
	
	@Test
	public void testReportClassesOptionStoresFile() throws Exception {
		classListFile = new File("someFileName.txt");
		classListFile.createNewFile();
		
		String[] args = makeArgs("-classlist", "someFileName.txt");
		
		options = new CommandLineOptions(args);
		assertEquals(classListFile, options.classListFile());
	}
	
	private void removeTestFile() {
		if(classListFile != null) classListFile.delete();
	}
	
	@Test(expected=CommandLineOptionsException.class)
	public void testThrowsExceptionIfClassListFileIsInvalid() throws Exception {
		options = new CommandLineOptions("-classlist", "");
	}
	
	@Test
	public void testIsUsingClassList() throws Exception {
		options = new CommandLineOptions("-cp", ".");
		assertFalse("Should not be using class list.", options.isUsingClassList());
		
		classListFile = new File("someFileName.txt");
		classListFile.createNewFile();
		options = new CommandLineOptions("-classlist", "someFileName.txt");
		assertTrue("Should be using class list.", options.isUsingClassList());
	}
	
	@Test
	public void testShouldReportErrors() throws Exception {
		options = new CommandLineOptions("-cp", ".");
		assertFalse("By default, errors should not be shown.", options.reportErrors());
		
		options = new CommandLineOptions("-cp", ".", "-e");
		assertTrue("With the '-e' flag, errors should be shown.", options.reportErrors());
	}

	private String[] makeArgs(String... args) {
		return args;
	}
	
	@After
	public void tearDown() {
		removeTestFile();
	}
	
}
