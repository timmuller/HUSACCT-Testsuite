package husaccttest;

import husaccttest.analyse.TestDomein;
import husaccttest.control.ControllerTest;
import husaccttest.control.ExternalServicesTest;
import husaccttest.define.domain.DefineServiceTests;
import husaccttest.define.domain.MethodTest;
import husaccttest.graphics.GraphicsServiceTest;
import husaccttest.validate.ValidateTest;
import junit.framework.TestSuite;

public class TestAll {

	Class[] testclasses = { TestDomein.class, ControllerTest.class, ExternalServicesTest.class, 
			DefineServiceTests.class, MethodTest.class, GraphicsServiceTest.class, ValidateTest.class};  
	
	TestSuite suite= new TestSuite(testclasses);
	 	 
}
