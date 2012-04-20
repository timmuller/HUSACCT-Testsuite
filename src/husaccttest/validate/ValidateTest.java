package husaccttest.validate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import husacct.common.dto.CategoryDTO;
import husacct.common.dto.RuleTypeDTO;
import husacct.common.dto.ViolationTypeDTO;
import husacct.define.DefineServiceImpl;
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
		assertArrayEquals(new String[]{"pdf","html","xml"}, validate.getExportExtentions());
	}
	@Test
	public void exportViolations()
	{
		//cant test void
	}


	@Test
	public void getCategories()
	{
		CategoryDTO[] dtos = validate.getCategories();		
		assertArrayEquals(new String[]{"legalityofdependency"}, getCategoryStringArray(dtos));	
		assertArrayEquals(new String[]{"IsNotAllowedToUse", "IsAllowedToUse"}, getRuleTypesStringArray(dtos));

		DefineServiceImpl defineService = new DefineServiceImpl();	
		if(defineService.getApplicationDetails().programmingLanguage != null){
			if(defineService.getApplicationDetails().programmingLanguage.isEmpty()){
				assertEquals(0, getViolationTypesStringArray(dtos, "IsNotAllowedToUse").length);
				assertEquals(0, getViolationTypesStringArray(dtos, "IsAllowedToUse").length);
			}			
			else if(defineService.getApplicationDetails().programmingLanguage.equals("Java")){
				assertEquals(9, getViolationTypesStringArray(dtos, "IsNotAllowedToUse").length);
				assertEquals(9, getViolationTypesStringArray(dtos, "IsAllowedToUse").length);
			}			
		}
		else{
			assertEquals(0, getViolationTypesStringArray(dtos, "IsNotAllowedToUse").length);
			assertEquals(0, getViolationTypesStringArray(dtos, "IsAllowedToUse").length);
		}
	}

	private String[] getCategoryStringArray(CategoryDTO[] dtos){
		List<String> categoryList = new ArrayList<String>();
		for(CategoryDTO dto: dtos){
			categoryList.add(dto.getKey());
		}
		return categoryList.toArray(new String[]{});
	}

	private String[] getRuleTypesStringArray(CategoryDTO[] dtos){
		List<String> ruletypeList = new ArrayList<String>();
		for(CategoryDTO cDTO : dtos){
			for(RuleTypeDTO rDTO : cDTO.getRuleTypes()){
				ruletypeList.add(rDTO.getKey());
			}
		}
		return ruletypeList.toArray(new String[]{});
	}

	private String[] getViolationTypesStringArray(CategoryDTO[] dtos, String ruleTypeKey){
		List<String> violationtypeList = new ArrayList<String>();
		for(CategoryDTO cDTO : dtos){
			for(RuleTypeDTO rDTO : cDTO.getRuleTypes()){
				if(rDTO.getKey().equals(ruleTypeKey)){
					for(ViolationTypeDTO vDTO : rDTO.getViolationTypes()){
						violationtypeList.add(vDTO.getKey());
					}
				}
			}
		}
		return violationtypeList.toArray(new String[]{});
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
		for(int i = 0; i < 10001; i++){
			validate.checkConformance();
		}
	}
}
