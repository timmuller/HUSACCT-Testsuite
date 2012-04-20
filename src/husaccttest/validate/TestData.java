package husaccttest.validate;



import husacct.validate.domain.validation.Message;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.logicalmodule.LogicalModule;
import husacct.validate.domain.validation.logicalmodule.LogicalModules;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class TestData {
	
	private List<Violation> violations;
	private List<Severity> severities;
	private HashMap<String, Severity> severitiesPerTypes;
	
	public TestData() {
		severitiesPerTypes = new HashMap<String, Severity>();
		violations = new ArrayList<Violation>();
		severities = new ArrayList<Severity>();
		LogicalModule lm2 = new LogicalModule("module path", "module type");
		LogicalModules logicalModules = new LogicalModules(lm2,lm2);
		
		List<Message> exceptionMessage = new ArrayList<Message>();
		List<Message> exceptionMessage2 = new ArrayList<Message>();
		
		List<String> violationTypeKeys = new ArrayList<String>();
		violationTypeKeys.add("key1");
		violationTypeKeys.add("key2");
		violationTypeKeys.add("key3");
		violationTypeKeys.add("key4");
		violationTypeKeys.add("key5");

		Message rootMessage2 =  new Message(logicalModules, "rulekey", violationTypeKeys);
		Message rootMessage3 =  new Message(logicalModules, "rulekey", violationTypeKeys, exceptionMessage2);
		exceptionMessage.add(rootMessage3);
		exceptionMessage.add(rootMessage2);
		exceptionMessage2.add(rootMessage2);

		Message rootMessage =  new Message(logicalModules, "rulekey", violationTypeKeys, exceptionMessage);
		
		
		Violation v1 = new Violation();
		v1.setViolationtypeKey("Implements");
		v1.setIndirect(false);
		v1.setLinenumber(101);
		v1.setMessage(rootMessage);
		v1.setRuletypeKey("IsAllowedToUse");
		v1.setSeverityValue(2);
		v1.setClassPathFrom("m1.p1.c1");
		v1.setClassPathTo("hu.sacct.validate.Validate");
		v1.setOccured(Calendar.getInstance());
		v1.setLogicalModules(logicalModules);
		

		Violation v2 = new Violation();
		v2.setViolationtypeKey("Implements");
		v2.setIndirect(true);
		v2.setLinenumber(50);
		v2.setMessage(rootMessage);
		v2.setRuletypeKey("IsNotAllowedToUse");
		v2.setSeverityValue(1);
		v2.setClassPathFrom("Module.module");
		v2.setClassPathTo("module.module2");
		v2.setOccured(Calendar.getInstance());
		v2.setLogicalModules(logicalModules);

		violations.add(v1);
		violations.add(v2);
		
		Severity s = new Severity();
		s.setColor("FFFFF");
		s.setValue(1);
		s.setDefaultName("High");
		s.setUserName("Heijmen");
		
		Severity s2 = new Severity();
		s2.setColor("FFFFF");
		s2.setValue(1);
		s2.setDefaultName("High");
		s2.setUserName("Heijmen");
		severities.add(s);
		severities.add(s2);
		
		severitiesPerTypes.put("type1", s);
		severitiesPerTypes.put("type2", s2);
		severitiesPerTypes.put("type3", s);
		severitiesPerTypes.put("type4", s2);
	}
	
	public List<Violation> getViolations() {
		return violations;
	}
	
	public List<Severity> getSeverities() {
		return severities;
	}

	public HashMap<String, Severity> getSeveritiesPerTypes() {
		return severitiesPerTypes;
	}
}