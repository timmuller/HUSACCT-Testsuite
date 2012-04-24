package husaccttest;

import husaccttest.analyse.AnalyseTestSuite;
import husaccttest.control.ControlTestSuite;
import husaccttest.define.DefineTestSuite;
import husaccttest.graphics.GraphicsTestSuite;
import husaccttest.validate.ValidateTestSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ 
	AnalyseTestSuite.class, 
	ControlTestSuite.class,
	DefineTestSuite.class, 
	GraphicsTestSuite.class,
	ValidateTestSuite.class,
})
public class TestAll {

}