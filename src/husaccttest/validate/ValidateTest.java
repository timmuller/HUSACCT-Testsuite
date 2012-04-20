package husaccttest.validate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import husacct.common.dto.CategoryDTO;
import husacct.common.dto.RuleTypeDTO;
import husacct.common.dto.ViolationTypeDTO;
import husacct.define.DefineServiceImpl;
import husacct.validate.ValidateServiceImpl;
import husacct.validate.abstraction.export.xml.ExportSeverities;
import husacct.validate.abstraction.export.xml.ExportSeveritiesPerTypes;
import husacct.validate.abstraction.export.xml.ExportViolations;
import husacct.validate.domain.validation.Message;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.logicalmodule.LogicalModules;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.DOMBuilder;
import org.jdom2.output.XMLOutputter;
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
		assertArrayEquals(new String[]{"IsNotAllowedToUse", "IsOnlyAllowedToUse","IsOnlyModuleAllowedToUse","IsAllowedToUse", "MustUse","SkipCall","BackCall"}, getRuleTypesStringArray(dtos));


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
	
	public void testImporting() throws URISyntaxException, ParserConfigurationException, SAXException, IOException {
		ClassLoader.getSystemResource("husaccttest/validate/testfile.xml").toURI();
		DocumentBuilderFactory domfactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder dombuilder = domfactory.newDocumentBuilder();
		File file = new File(ClassLoader.getSystemResource("husaccttest/validate/testfile.xml").toURI());
		DOMBuilder domBuilder = new DOMBuilder();
		Document document = domBuilder.build(dombuilder.parse(file));
		
		validate.loadWorkspaceData(document.getRootElement());
		
		
	}
	
	@Test
	public void testExporting() {
		validate.getWorkspaceData();
	}

	@Test
	public void testExportViolations() throws DatatypeConfigurationException {
		TestData data = new TestData();
		ExportViolations exportViolations = new ExportViolations();

		Element element = exportViolations.exportViolations(data.getViolations());
		for (int i = 0; i <data.getViolations().size(); i++) {
			Element violationElement = element.getChildren().get(i);
			Violation violation = data.getViolations().get(i);
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
	@Test
	public void testExportSeverities() {
		TestData data = new TestData();
		ExportSeverities exportViolations = new ExportSeverities();
		Element element = exportViolations.exportSeverities(data.getSeverities());
		for (int i = 0; i <data.getSeverities().size(); i++) {
			Element severityElement = element.getChildren().get(i);
			Severity severity = data.getSeverities().get(i);
			assertEquals(severity.getDefaultName(), severityElement.getChildText("defaultName"));
			assertEquals(severity.getUserName(), severityElement.getChildText("userName"));
			assertEquals(severity.getValue(),Integer.parseInt(severityElement.getChildText("value")));
			assertEquals(severity.getColor(), severityElement.getChildText("color"));
		}
	}
	@Test
	public void testExportSeveritiesPerTypes() {
		TestData data = new TestData();
		ExportSeveritiesPerTypes exportSeveritiesPerTypes = new ExportSeveritiesPerTypes();
		Element element = exportSeveritiesPerTypes.exportSeveritiesPerTypes(data.getSeveritiesPerTypes());
		int i = 0;
		for(Entry<String, Severity> entry : data.getSeveritiesPerTypes().entrySet()) {
			Element severitiesPerTypesElement = element.getChildren().get(i);
			assertEquals(entry.getKey(), severitiesPerTypesElement.getChildText("typeKey"));
			assertEquals(entry.getValue().getValue(), Integer.parseInt(severitiesPerTypesElement.getChildText("value")));
			i++;
		}
	}
}
