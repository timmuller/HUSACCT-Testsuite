package husaccttest.control;

import static org.junit.Assert.assertSame;
import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.define.IDefineService;
import husacct.graphics.IGraphicsService;
import husacct.validate.IValidateService;

import org.junit.Before;
import org.junit.Test;



public class ExternalServicesTest {

	IAnalyseService analyseService;
	IGraphicsService architectureService;
	IDefineService defineService;
	IValidateService validateService;
	
	@Before
	public void prepareServices(){
		ServiceProvider provider = ServiceProvider.getInstance();
		this.analyseService = provider.getAnalyseService();
		this.architectureService = provider.getGraphicsService();
		this.defineService = provider.getDefineService();
		this.validateService = provider.getValidateService();
	}
	
	@Test 
	public void testServiceProvider(){
		ServiceProvider provider1 = ServiceProvider.getInstance();
		ServiceProvider provider2 = ServiceProvider.getInstance();
		assertSame(provider1, provider2);
	}
	
	@Test
	public void testServiceProviderGetAnalyseService(){
		ServiceProvider provider1 = ServiceProvider.getInstance();
		ServiceProvider provider2 = ServiceProvider.getInstance();
		assertSame(provider1.getAnalyseService(), provider2.getAnalyseService());
	}
	
	@Test
	public void testServiceProviderGetGraphicsService(){
		ServiceProvider provider1 = ServiceProvider.getInstance();
		ServiceProvider provider2 = ServiceProvider.getInstance();
		assertSame(provider1.getGraphicsService(), provider2.getGraphicsService());
	}
	
	@Test
	public void testServiceProviderGetDefineService(){
		ServiceProvider provider1 = ServiceProvider.getInstance();
		ServiceProvider provider2 = ServiceProvider.getInstance();
		assertSame(provider1.getDefineService(), provider2.getDefineService());
	}
	
	@Test
	public void testServiceProviderGetValidateService(){
		ServiceProvider provider1 = ServiceProvider.getInstance();
		ServiceProvider provider2 = ServiceProvider.getInstance();
		assertSame(provider1.getValidateService(), provider2.getValidateService());
	}
	
//	@Test
//	public void testAnalyse() {
//		this.analyseService.analyse("path", "Java");
//	}
//
//	@Test
//	public void testAnalyseGetAvailableLanguages(){
//		String[] languages = analyseService.getAvailableLanguages();
//		assertTrue(languages.length>0);
//	}
//	
//	@Test
//	public void testArchitectureGraphicsDrawAnalysedArchitecture(){
//		this.architectureService.drawAnalysedArchitecture();
//	}
//	
//	@Test
//	public void testArchitectureGraphicsDrawAnalysedArchitectureWithViolations(){
//		this.architectureService.drawAnalysedArchitectureWithViolations();
//	}
//	
//	@Test
//	public void testGetArchitectureGUI(){
//		JInternalFrame frame = architectureService.getArchitectureGUI();
//		assertTrue(frame instanceof JInternalFrame);
//	}
//	
//	@Test
//	public void testDefineExportLogicalArchitecture(){
//		this.defineService.exportLogicalArchitecture("path");
//	}
//	
//	@Test
//	public void testDefineExportMappedArchitecture(){
//		this.defineService.exportMappedArchitecture("path");
//	}
//	
//	@Test
//	public void testDefineGui(){
//		JInternalFrame frame = defineService.getDefineGUI();
//		assertTrue(frame instanceof JInternalFrame);
//	}
//	
//	@Test
//	public void testDefineImportLogicalArchitecture(){
//		this.defineService.importLogicalArchitecture("path");
//	}
//	
//	@Test
//	public void testDefineImportMappedArchitecture(){
//		this.defineService.importMappedArchitecture("path");
//	}
//	
//	@Test
//	public void testValidateCheckConformance(){
//		JInternalFrame frame = validateService.checkConformance();
//		assertTrue(frame instanceof JInternalFrame);
//	}
//	
//	@Test
//	public void testValidateExportViolations(){
//		this.validateService.exportViolations("type", "path", "extension");
//	}
//	
//	@Test
//	public void testValidateGetConfigurationGUI(){
//		JInternalFrame frame = validateService.getConfigurationGUI();
//		assertTrue(frame instanceof JInternalFrame);
//	}
//	
//	@Test
//	public void testValidateGetExportExtensions(){
//		String[] extensions = validateService.getExportExtensions();
//		assertTrue(extensions.length>0);
//	}
//	
//	@Test
//	public void testValidateGetViolationsGUI(){
//		JInternalFrame frame = validateService.getViolationsGUI();
//		assertTrue(frame instanceof JInternalFrame);
//	}

}
