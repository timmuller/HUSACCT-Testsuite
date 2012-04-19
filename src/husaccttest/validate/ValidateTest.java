package husaccttest.validate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import husacct.validate.ValidateServiceImpl;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class ValidateTest {

	private ValidateServiceImpl validate;

	@Before
	public void setup()
	{
		validate = new ValidateServiceImpl();
	}
	@Test
	public void getExportExtentions()
	{
		assertArrayEquals(new String[]{"pdf","xml","html"}, validate.getExportExtentions());
	}
	@Test
	public void exportViolations()
	{
		//cant test void
	}
	@Test
	public void getCategories()
	{
		assertEquals("legalityofdependency", validate.getCategories()[0].getKey());
		assertEquals("IsNotAllowedToUse", validate.getCategories()[0].getRuleTypes()[0].getKey());
		assertEquals("InvocMethod", validate.getCategories()[0].getRuleTypes()[0].getViolationTypes()[0].getKey());
	}
	@Test
	public void getViolations()
	{
		validate.checkConformance();
		assertEquals("domain.locationbased.foursquare.Account", validate.getViolations("DomainLayer", "Infrastructure")[0].getFromClasspath());
		assertEquals("infrastructure.socialmedia.locationbased.foursquare.AccountDAO", validate.getViolations("DomainLayer", "Infrastructure")[0].getToClasspath());
		assertEquals("InvocConstructor", validate.getViolations("DomainLayer", "Infrastructure")[0].getViolationType().getKey());
	}
	
	@Ignore
	@Test
	public void checkConformancePerformance(){
		for(int i = 0; i < 100001; i++){
			validate.checkConformance();
		}
	}
}
