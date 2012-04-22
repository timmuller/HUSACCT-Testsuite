package husaccttest.validate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import husacct.common.dto.CategoryDTO;
import husacct.common.dto.RuleTypeDTO;
import husacct.common.dto.ViolationTypeDTO;
import husacct.define.DefineServiceImpl;
import husacct.validate.ValidateServiceImpl;
import husacct.validate.domain.validation.Message;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.logicalmodule.LogicalModules;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.DOMBuilder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

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
		
		final String [] currentRuletypes = new String[]{"IsNotAllowedToUse", "IsOnlyAllowedToUse","IsOnlyModuleAllowedToUse","IsAllowedToUse", "MustUse","SkipCall","BackCall"};
		assertArrayEquals(currentRuletypes, getRuleTypesStringArray(dtos));
		
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
	public void isValidatedBeforeValidation(){
		assertFalse(validate.isValidated());
	}

	@Test
	public void getViolations()
	{
		validate.checkConformance();
		assertEquals("domain.locationbased.foursquare.Account", validate.getViolations("DomainLayer", "Infrastructure")[0].getFromClasspath());
		assertEquals("infrastructure.socialmedia.locationbased.foursquare.AccountDAO", validate.getViolations("DomainLayer", "Infrastructure")[0].getToClasspath());
		assertEquals("InvocConstructor", validate.getViolations("DomainLayer", "Infrastructure")[0].getViolationType().getKey());
	}
	
	@Test
	public void isValidatedAfterValidation(){
		validate.checkConformance();
		assertFalse(validate.isValidated());
	}

	@Ignore
	@Test
	public void checkConformancePerformance(){
		for(int i = 0; i < 10001; i++){
			validate.checkConformance();
		}
	}
	
	public void testImporting() throws URISyntaxException, ParserConfigurationException, SAXException, IOException, DatatypeConfigurationException {
		ClassLoader.getSystemResource("husaccttest/validate/testfile.xml").toURI();
		DocumentBuilderFactory domfactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder dombuilder = domfactory.newDocumentBuilder();
		File file = new File(ClassLoader.getSystemResource("husaccttest/validate/testfile.xml").toURI());
		DOMBuilder domBuilder = new DOMBuilder();
		Document document = domBuilder.build(dombuilder.parse(file));
		validate.loadWorkspaceData(document.getRootElement());
		checkViolationsTheSameAsViolationsElement(validate.getConfiguration().getAllViolations(), document.getRootElement().getChild("violations"));
		checkSeveritiesTheSameAsSeveritiesElement(validate.getConfiguration().getAllSeverities(), document.getRootElement().getChild("severities"));
		checkSeveritiesPerTypesPerProgrammingLanguagesTheSameAsSeveritiesPerTypesPerProgrammingLanguagesElement(validate.getConfiguration().getAllSeveritiesPerTypesPerProgrammingLanguages(), document.getRootElement().getChild("severitiesPerTypesPerProgrammingLanguages"));
	}
	
	public void checkViolationsTheSameAsViolationsElement(List<Violation> violations, Element violationsElement) throws DatatypeConfigurationException {
		for(int i = 0; i < violationsElement.getChildren().size(); i++) {
			Element violationElement = violationsElement.getChildren().get(i);
			Violation violation = violations.get(i);
			checkViolationTheSameAsViolationElement(violationElement, violation);
		}
	}
	
	public void checkSeveritiesTheSameAsSeveritiesElement(List<Severity> severities, Element severitiesElement) {
		for(int i = 0; i < severitiesElement.getChildren().size(); i++) {
			Element severityElement = severitiesElement.getChildren().get(i);
			Severity severity = severities.get(i);
			checkSeverityTheSameAsSeverityElement(severity, severityElement);
		}
	}
	
	public void checkSeveritiesPerTypesPerProgrammingLanguagesTheSameAsSeveritiesPerTypesPerProgrammingLanguagesElement(HashMap<String, HashMap<String, Severity>> severitiesPerTypesPerProgrammingLanguages, Element severitiesPerTypesPerProgrammingLanguagesElement) {
		assertEquals(severitiesPerTypesPerProgrammingLanguages.size(), severitiesPerTypesPerProgrammingLanguagesElement.getChildren().size());
		for(Entry<String, HashMap<String, Severity>> severityPerTypePerProgrammingLanguage : severitiesPerTypesPerProgrammingLanguages.entrySet()) {
			for(Element severityPerTypePerProgrammingLanguageElement : severitiesPerTypesPerProgrammingLanguagesElement.getChildren()) {
				if(severityPerTypePerProgrammingLanguageElement.getAttribute("language").getValue().equals(severityPerTypePerProgrammingLanguage.getKey())) {
					checkSeverityPerTypePerProgrammingLanguageTheSameAsSeverityPerTypePerProgrammingLanguageElement(severityPerTypePerProgrammingLanguage, severityPerTypePerProgrammingLanguageElement);
				}
			}
		}
	}
	
	public void checkViolationTheSameAsViolationElement(Element violationElement, Violation violation) throws DatatypeConfigurationException {
		assertEquals(violation.getLinenumber(), Integer.parseInt(violationElement.getChildText("lineNumber")));
		assertEquals(violation.getSeverityValue(), Integer.parseInt(violationElement.getChildText("severityValue")));
		assertEquals(violation.getRuletypeKey(), violationElement.getChildText("ruletypeKey"));
		assertEquals(violation.getClassPathFrom(), violationElement.getChildText("classPathFrom"));
		assertEquals(violation.getClassPathTo(), violationElement.getChildText("classPathTo"));
		checkLogicalModulesTheSameAsLogicalModulesElement(violationElement.getChild("logicalModules"), violation.getLogicalModules());
		checkMessageTheSameAsMessageElement(violationElement.getChild("message"), violation.getMessage());
		assertEquals(violation.isIndirect(), Boolean.parseBoolean(violationElement.getChildText("isIndirect")));
		assertEquals(DatatypeFactory.newInstance().newXMLGregorianCalendar((GregorianCalendar)violation.getOccured()), DatatypeFactory.newInstance().newXMLGregorianCalendar(violationElement.getChildText("occured")));
	}
	//TODO create a assertFound in case a key doesnt get found.
	public void checkSeverityTheSameAsSeverityElement(Severity severity, Element severityElement) {
		assertEquals(severity.getDefaultName(), severityElement.getChildText("defaultName"));
		assertEquals(severity.getUserName(), severityElement.getChildText("userName"));
		assertEquals(severity.getValue(),Integer.parseInt(severityElement.getChildText("value")));
		assertEquals(severity.getColor(), new Color(Integer.parseInt(severityElement.getChildText("color"))));
	}
	//TODO create a assertFound in case a key doesnt get found.
	public void checkSeverityPerTypePerProgrammingLanguageTheSameAsSeverityPerTypePerProgrammingLanguageElement(Entry<String, HashMap<String, Severity>> severityPerTypePerProgrammingLanguage, Element severityPerTypePerProgrammingLanguageElement) {
		assertEquals(severityPerTypePerProgrammingLanguageElement.getChildren().size(), severityPerTypePerProgrammingLanguage.getValue().size());
		for(Entry<String, Severity> severityPerType : severityPerTypePerProgrammingLanguage.getValue().entrySet()) {
			for(Element severityPerTypeElement : severityPerTypePerProgrammingLanguageElement.getChildren()) {
				if(severityPerTypeElement.getChildText("typeKey").equals(severityPerType.getKey())) {
					assertEquals(Integer.parseInt(severityPerTypeElement.getChildText("value")), severityPerType.getValue().getValue());
				}
			}
		}
	}
	
	@Test
	public void testExportingAndImporting() throws URISyntaxException, ParserConfigurationException, SAXException, IOException, DatatypeConfigurationException {
		testImporting();
		checkViolationsTheSameAsViolationsElement(validate.getConfiguration().getAllViolations(), validate.getWorkspaceData().getChild("violations"));
		checkSeveritiesTheSameAsSeveritiesElement(validate.getConfiguration().getAllSeverities(), validate.getWorkspaceData().getChild("severities"));
		checkSeveritiesPerTypesPerProgrammingLanguagesTheSameAsSeveritiesPerTypesPerProgrammingLanguagesElement(validate.getConfiguration().getAllSeveritiesPerTypesPerProgrammingLanguages(), validate.getWorkspaceData().getChild("severitiesPerTypesPerProgrammingLanguages"));
	}

	public void checkLogicalModulesTheSameAsLogicalModulesElement(Element logicalModulesElement, LogicalModules logicalModiles) {
		assertEquals(logicalModiles.getLogicalModuleFrom().getLogicalModulePath(), logicalModulesElement.getChild("logicalModuleFrom").getChildText("logicalModulePath"));
		assertEquals(logicalModiles.getLogicalModuleFrom().getLogicalModuleType(), logicalModulesElement.getChild("logicalModuleFrom").getChildText("logicalModuleType"));
		assertEquals(logicalModiles.getLogicalModuleTo().getLogicalModulePath(), logicalModulesElement.getChild("logicalModuleTo").getChildText("logicalModulePath"));
		assertEquals(logicalModiles.getLogicalModuleTo().getLogicalModuleType(), logicalModulesElement.getChild("logicalModuleTo").getChildText("logicalModuleType"));
	}

	public void checkMessageTheSameAsMessageElement(Element messageElement, Message message) {
		checkLogicalModulesTheSameAsLogicalModulesElement(messageElement.getChild("logicalModules"), message.getLogicalModules());
		assertEquals(message.getRuleKey(), messageElement.getChildText("ruleKey"));
		for(int i = 0; i < message.getViolationTypeKeys().size(); i++) {
			assertEquals(message.getViolationTypeKeys().get(i), messageElement.getChild("violationTypeKeys").getChildren().get(i).getText());
		}
		for(int i = 0; i < message.getExceptionMessage().size(); i++){
			checkMessageTheSameAsMessageElement(messageElement.getChild("exceptionMessages").getChildren().get(i), message.getExceptionMessage().get(i));
		}
	}

}
