package husaccttest.define.domain;

import husacct.define.domain.AppliedRule;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.module.Layer;
import husacct.define.domain.module.Module;

public class MethodTest {
	/**
	 * @param args
	 */	
	public static void main(String[] args) {
		
		//SET UP
		//SET UP
		//SET UP
		SoftwareArchitecture sA = new SoftwareArchitecture("Test architecture", "This architecture is used for testing purposes");
		
		Module module1 = new Module("Module 1", "This is module 1");
		Module module2 = new Module("Module 2", "This is module 2");
		Module module3 = new Module("Module 3", "This is module 3");

		Module layer1 = new Layer("Layer 1", "This is layer 1", 1);
		Module layer2 = new Layer("Layer 2", "This is layer 2", 2);

		Module subModule1 = new Module("Sub Module 1", "This is a submodule");
		Module subModule11 = new Module("Sub Module 11", "This is a submodule");
		Module subModule2 = new Module("Sub Module 2", "This is a submodule");		
		Module subModule3 = new Module("Sub Module 3", "This is a submodule");		

		AppliedRule rule1 = new AppliedRule("IsNotAllowedToUse", "Test", new String[1],
				"prefix", "suffix", module1,
				null);
		AppliedRule rule2 = new AppliedRule("IsNotAllowedToUse", "Test", new String[1],
				"prefix", "suffix", module1,
				null);
		AppliedRule rule3 = new AppliedRule("IsNotAllowedToUse", "Test", new String[1],
				"prefix", "suffix", module1,
				null);

		AppliedRule exception1 = new AppliedRule("IsNotAllowedToUse", "Test", new String[1],
				"prefix", "suffix", module1,
				null);
		AppliedRule exception2 = new AppliedRule("IsNotAllowedToUse", "Test", new String[1],
				"prefix", "suffix", module1,
				null);
		AppliedRule exception3 = new AppliedRule("IsNotAllowedToUse", "Test", new String[1],
				"prefix", "suffix", module1,
				null);
		
		//TODO: Test SoftwareUnitDefinitions
		module1.addSubModule(subModule1);
		module1.addSubModule(subModule11);
		module2.addSubModule(subModule2);
		module3.addSubModule(subModule3);
		
		rule1.addException(exception1);
		rule1.addException(exception1);
		rule1.addException(exception2);
		rule1.addException(exception2);
		rule1.addException(exception3);
		
		layer1.addSubModule(module1);
		layer2.addSubModule(module2);
		
		sA.addModule(layer1);
		sA.addModule(layer2);
		sA.addModule(module3);
		
		sA.addAppliedRule(rule1);
		sA.addAppliedRule(rule2);
		sA.addAppliedRule(rule3);
		
		
		//TESTS
		//TESTS
		//TESTS
		Module module = sA.getModuleByLogicalPath("Layer 1.Module 1.Sub Module 11");
		
		System.out.println("Before removing: ");
		System.out.println("\n" + "This is : " + sA.getName()+ "\n" + "Its description is: " + sA.getDescription());
		
		System.out.println("\n" + "The amount of submodules in Module 1: " + module1.getSubModules().size());
		System.out.println("The amount of modules in the Software Architecture: " + sA.getModules().size());		
		System.out.println("The amount of applied rules within this Software Architecture: " + sA.getAppliedRules().size());

		
		System.out.println("Layer 1 is of type: " + layer1.getType());
		System.out.println("Rule 1 has " + rule1.getExceptions().size() + " exceptions");
		
		sA.removeModule(module3);
		sA.removeModule(layer2);
		module1.removeSubModule(subModule3);
		sA.removeAppliedRule(rule3.getId());
		rule1.removeException(exception3);
		
		
		System.out.println("\n" + "After removing: ");
		System.out.println("\n" + "This is : " + sA.getName()+ "\n" + "Its description is: " + sA.getDescription());
		
		System.out.println("\n" + "The amount of submodules in Module 1: " + module1.getSubModules().size());
		System.out.println("The amount of modules in the Software Architecture: " + sA.getModules().size());		
		System.out.println("The amount of applied rules within this Software Architecture: " + sA.getAppliedRules().size());

		
		System.out.println("Layer 1 is of type: " + layer1.getType());
		System.out.println("Rule 1 has " + rule1.getExceptions().size() + " exceptions");

		
		
	}

}
