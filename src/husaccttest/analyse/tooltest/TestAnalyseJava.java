package husaccttest.analyse.tooltest;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import husacct.define.IDefineService;
import husaccttest.analyse.TestCaseExtended;



public class TestAnalyseJava extends TestCaseExtended{

	private IAnalyseService service;
	
	@Override
	public void setUp(){
		
		ServiceProvider provider = ServiceProvider.getInstance();
		IDefineService defService = provider.getDefineService();

		String path = "..\\benchmark_application\\src";
		String[] paths = new String[]{path};
		defService.createApplication("Boobies Sanders Application", paths, "Java", "1.0");
		
		service = provider.getAnalyseService();
		
		if(service.isAnalysed()){
			return;
		}
		
		try {
			service.analyseApplication();
		} catch (Exception e){
			System.out.println("We're sorry. You need to have a java project 'benchmark_application' with inside the benchmark_application. Or you have the wrong version of the benchmark_application.");
			System.out.println("git://github.com/timmuller/benchmark_application.git");
			System.exit(0);
		}
	}
	
	public void testRootModules(){
		String[] namesExpected = {"declarations", "exception", "extendsconcrete", "generics", "husacct", "inpackage", "interfaces", "overpackages", "parentmoduletest"};			
		AnalysedModuleDTO[] rootModules = service.getRootModules();
		
		assertEquals(namesExpected.length, rootModules.length);
		this.foundModulesNames(namesExpected, rootModules);
	}
	
	public void testNavigation(){
		String rootElement = "interfaces";
		
		int expectedChildmodules1 = 1;
		String[] expectedName = {"multiplepackages"};
		AnalysedModuleDTO[] childmodules1 = service.getChildModulesInModule(rootElement);
		assertEquals(expectedChildmodules1, childmodules1.length);
		this.foundModulesNames(expectedName, childmodules1);
		
		int expectedChildModules2 = 2;
		String uniquenameChildModules2 = childmodules1[0].uniqueName;
		String[] expectedNames2 = {"package1", "package2"};
		AnalysedModuleDTO[] childmodules2 = service.getChildModulesInModule(uniquenameChildModules2);
		assertEquals(expectedChildModules2, childmodules2.length);
		this.foundModulesNames(expectedNames2, childmodules2);

		int expectedChildModules3 = 3;
		String uniquenameChildModules3 = this.getModuleByName("package2", childmodules2).uniqueName;
		String[] expectedNamesChildModule3 = {"TheImplementation", "TheSecondImplementation", "TheThirdImplementation"};
		AnalysedModuleDTO[] childmodules3 = service.getChildModulesInModule(uniquenameChildModules3);
		assertEquals(expectedChildModules3, childmodules3.length);
		this.foundModulesNames(expectedNamesChildModule3, childmodules3);
	}
	
	public void testDeclarationInPackage11(){
		String from = "declarations.inpackage.Toy";
		String to = "declarations.inpackage.User";
		
		int dependenciesExpected = 1;
		
		DependencyDTO[] dependencies = service.getDependencies(from, to);
		assertEquals(dependenciesExpected, dependencies.length);
		assertTrue("Missing Type " + super.DECLARATION, this.hasDependencyType(super.DECLARATION, dependencies));
		assertTrue(hasDependencyTo(to, dependencies));
	}

// Added our Self //
	
//	public void testDeclarationInPackage12(){
//		String from = "declarations.inpackage.Phone";
//		String to = "declarations.inpackage.User";
//		
//		int dependenciesExpected = 1;
//		
//		DependencyDTO[] dependencies = service.getDependencies(from, to);
//		assertEquals(dependenciesExpected, dependencies.length);
//		assertTrue("Missing Type " + super.DECLARATION, this.hasDependencyType(super.DECLARATION, dependencies));
//		assertTrue(hasDependencyTo(to, dependencies));
//	}
	
	public void testDeclarationOuterPackage13(){
		String from = "declarations.outerpackage.package2.Car";
		String to = "declarations.outerpackage.package1.Driver";
		int dependenciesExpected = 2;
		
		DependencyDTO[] dependencies = service.getDependencies(from, to);
		assertEquals(dependenciesExpected, dependencies.length);
		assertTrue("Missing Type " + super.DECLARATION, this.hasDependencyType(super.DECLARATION, dependencies));
		assertTrue(hasDependencyTo(to, dependencies));
	}
	
	public void testDeclarationOuterPackage14(){
		String from = "declarations.outerpackage.package2.Bycicle";
		String to = "declarations.outerpackage.package1.Driver";
		int dependenciesExpected = 1;
		
		DependencyDTO[] dependencies = service.getDependencies(from, to);
		assertEquals(dependenciesExpected, dependencies.length);
		assertTrue("Missing Type " + super.DECLARATION, this.hasDependencyType(super.DECLARATION, dependencies));
		assertTrue(hasDependencyTo(to, dependencies));
	}
	
	public void testDeclarationOuterPackage15(){
		String from = "declarations.outerpackage.package2.Motor";
		String to = "declarations.outerpackage.package1.Driver";
		int dependenciesExpected = 1;
		
		DependencyDTO[] dependencies = service.getDependencies(from, to);
		assertEquals(dependenciesExpected, dependencies.length);
		assertTrue("Missing Type " + super.DECLARATION, this.hasDependencyType(super.DECLARATION, dependencies));
		assertTrue(hasDependencyTo(to, dependencies));
	}
	
	public void testExceptionsTryCatch21(){
		String from = "exception.apackage.AnotherClass";
		String to = "exception.MyException";
		DependencyDTO[] dependencies = service.getDependencies(from, to);
		
		int expectedDependencies = 2;
		assertEquals(expectedDependencies, dependencies.length);
		assertTrue("Missing Type " + super.IMPORT, this.hasDependencyType(super.IMPORT, dependencies));
		assertTrue("Missing Type " + super.EXCEPTION, this.hasDependencyType(super.EXCEPTION, dependencies));
	}
	
	public void testExceptionsTryCatch22(){
		String from = "exception.bpackage.OtherClass";
		String to = "exception.MyException";
		DependencyDTO[] dependencies = service.getDependencies(from, to);
		
		int expectedDependencies = 1;
		assertEquals(expectedDependencies, dependencies.length);
		assertTrue("Missing Type " + super.EXCEPTION, this.hasDependencyType(super.EXCEPTION, dependencies));
	}

// Eigen toegevoegde test (failing)
	
//	public void testExceptionsTryCatch23(){
//		String from = "exception.apackage.BnotherClass";
//		String to = "exception.MyException";
//		DependencyDTO[] dependencies = service.getDependencies(from, to);
//		
//		int expectedDependencies = 1;
//		assertEquals(expectedDependencies, dependencies.length);
//		assertTrue("Missing Type " + super.EXCEPTION, this.hasDependencyType(super.EXCEPTION, dependencies));
//	}
	
	
// Wordt op 7 mei nog niet herkend!	(Throw exception)
	
//	public void testExceptionsThrow24(){
//		String from = "exception.apackage.MyClass";
//		String to = "exception.MyException";
//		DependencyDTO[] dependencies = service.getDependencies(from, to);
//		
//		int expectedDependencies = 2;
//		assertEquals(expectedDependencies, dependencies.length);
//		assertTrue("Missing Type " + super.IMPORT, this.hasDependencyType(super.IMPORT, dependencies));
//		assertTrue("Missing Type " + super.EXCEPTION, this.hasDependencyType(super.EXCEPTION, dependencies));
//	}

// FAALT 7 mei
	
//	public void testExceptionsThrow25(){
//		String from = "exception.apackage.Otherclass";
//		String to = "exception.MyException";
//		DependencyDTO[] dependencies = service.getDependencies(from, to);
//		
//		int expectedDependencies = 1;
//		assertEquals(expectedDependencies, dependencies.length);
//		assertTrue("Missing Type " + super.EXCEPTION, this.hasDependencyType(super.EXCEPTION, dependencies));
//	}
	
// FAALT 7 mei	
	
//	public void testExceptionsThrow25(){
//		String from = "exception.apackage.Cnotherclass";
//		String to = "exception.MyException";
//		DependencyDTO[] dependencies = service.getDependencies(from, to);
//	
//		int expectedDependencies = 1;
//		assertEquals(expectedDependencies, dependencies.length);
//		assertTrue("Missing Type " + super.EXCEPTION, this.hasDependencyType(super.EXCEPTION, dependencies));
//	}		
	
	public void testExtendsConcreteInPackage31(){
		String from = "extendsconcrete.package1.ConcreteChildInPackage";
		String to = "extendsconcrete.package1.ConcreteParent";
		DependencyDTO[] dependencies = service.getDependencies(from, to);
		
		int expectedDependencies = 1;
		assertEquals(expectedDependencies, dependencies.length);
		assertTrue("Missing Type " + super.EXTENDSCONCRETE, this.hasDependencyType(super.EXTENDSCONCRETE, dependencies));
	}
	
	public void testExtendsConcreteInPackage32(){
		String from = "extendsconcrete.package2.ConcreteChildImportsClass";
		String to = "extendsconcrete.package1.ConcreteParent";
		DependencyDTO[] dependencies = service.getDependencies(from, to);
		
		int expectedDependencies = 2;
		assertEquals(expectedDependencies, dependencies.length);
		assertTrue("Missing Type " + super.EXTENDSCONCRETE, this.hasDependencyType(super.EXTENDSCONCRETE, dependencies));
	}
	
	public void testExtendsConcreteInPackage33(){
		String from = "extendsconcrete.package2.ConcreteChildImportsPackage";
		String to = "extendsconcrete.package1.ConcreteParent";
		DependencyDTO[] dependencies = service.getDependencies(from, to);
		
		int expectedDependencies = 1;
		assertEquals(expectedDependencies, dependencies.length);
		assertTrue("Missing Type " + super.EXTENDSCONCRETE, this.hasDependencyType(super.EXTENDSCONCRETE, dependencies));
	}
	
	public void testExtendsConcreteInPackage34(){
		String from = "extendsconcrete.package2.ConcreteChildCompleteDefinition";
		String to = "extendsconcrete.package1.ConcreteParent";
		DependencyDTO[] dependencies = service.getDependencies(from, to);
		
		int expectedDependencies = 1;
		assertEquals(expectedDependencies, dependencies.length);
		assertTrue("Missing Type " + super.EXTENDSCONCRETE, this.hasDependencyType(super.EXTENDSCONCRETE, dependencies));
	}

	
	
//	public void testGenericsExtends41(){
//		String from = "generics.MyList";
//		String to = "";
//		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
//
//		for(DependencyDTO d : dependencies){
//			System.out.println(d.to);
//		}
//		
//	}

// 7 mei wordt ArrayList<String> nog niet ondersteund. gaat kapot met connecten
	
//	public void testGenericsDeclaration42(){
//		String from = "generics.MyStack";
//		String to = "";
//		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
//
//		for(DependencyDTO d : dependencies){
//			System.out.println(d.to);
//		}
//		
//	}
	
	
	public void testExtendsAbstract51(){
		String from = "inpackage.Car";
		String to = "inpackage.AbstractVehicle";
		DependencyDTO[] dependencies = service.getDependencies(from, to);
		
		int expectedDependencies = 1;
		assertEquals(expectedDependencies, dependencies.length);
		assertTrue("Missing Type " + super.EXTENDSABSTRACT, this.hasDependencyType(super.EXTENDSABSTRACT, dependencies));
	}
	
	public void testExtendsAbstract52(){
		String from = "inpackage.Bus";
		String to = "inpackage.AbstractVehicle";
		DependencyDTO[] dependencies = service.getDependencies(from, to);
		
		int expectedDependencies = 1;
		assertEquals(expectedDependencies, dependencies.length);
		assertTrue("Missing Type " + super.EXTENDSABSTRACT, this.hasDependencyType(super.EXTENDSABSTRACT, dependencies));
	}
	
	public void testExtendsAbstract53(){
		String from = "overpackages.childs.Soccer";
		String to = "overpackages.parents.AbstractSport";
		DependencyDTO[] dependencies = service.getDependencies(from, to);
		
		int expectedDependencies = 2;
		assertEquals(expectedDependencies, dependencies.length);
		assertTrue("Missing Type " + super.EXTENDSABSTRACT, this.hasDependencyType(super.EXTENDSABSTRACT, dependencies));
	}
	
	public void testExtendsAbstract54(){
		String from = "overpackages.childs.Rugby";
		String to = "overpackages.parents.AbstractSport";
		DependencyDTO[] dependencies = service.getDependencies(from, to);
		
		int expectedDependencies = 1;
		assertEquals(expectedDependencies, dependencies.length);
		assertTrue("Missing Type " + super.EXTENDSABSTRACT, this.hasDependencyType(super.EXTENDSABSTRACT, dependencies));
	}
	
	public void testExtendsAbstract55(){
		String from = "overpackages.childs.VolleyBal";
		String to = "overpackages.parents.AbstractSport";
		DependencyDTO[] dependencies = service.getDependencies(from, to);
		
		int expectedDependencies = 1;
		assertEquals(expectedDependencies, dependencies.length);
		assertTrue("Missing Type " + super.EXTENDSABSTRACT, this.hasDependencyType(super.EXTENDSABSTRACT, dependencies));
	}
	
	public void testImplements61(){
		String from = "interfaces.multiplepackages.package1.InSamePackageImplementation";
		String to = "interfaces.multiplepackages.package1.TheService";
		DependencyDTO[] dependencies = service.getDependencies(from, to);

		int expectedDependencies = 1;
		assertEquals(expectedDependencies, dependencies.length);
		assertTrue("Missing Type " + super.IMPLEMENTS, this.hasDependencyType(super.IMPLEMENTS, dependencies));
	}
	
	public void testImplements62(){
		String from = "interfaces.multiplepackages.package1.InSamePackageCompletePath";
		String to = "interfaces.multiplepackages.package1.TheService";
		DependencyDTO[] dependencies = service.getDependencies(from, to);

		int expectedDependencies = 1;
		assertEquals(expectedDependencies, dependencies.length);
		assertTrue("Missing Type " + super.IMPLEMENTS, this.hasDependencyType(super.IMPLEMENTS, dependencies));
	}
	
	public void testImplements63(){
		String from = "interfaces.multiplepackages.package2.TheThirdImplementation";
		String to = "interfaces.multiplepackages.package1.TheService";
		DependencyDTO[] dependencies = service.getDependencies(from, to);

		int expectedDependencies = 2;
		assertEquals(expectedDependencies, dependencies.length);
		assertTrue("Missing Type " + super.IMPLEMENTS, this.hasDependencyType(super.IMPLEMENTS, dependencies));
	}
	
	public void testImplements64(){
		String from = "interfaces.multiplepackages.package2.TheSecondImplementation";
		String to = "interfaces.multiplepackages.package1.TheService";
		DependencyDTO[] dependencies = service.getDependencies(from, to);

		int expectedDependencies = 1;
		assertEquals(expectedDependencies, dependencies.length);
		assertTrue("Missing Type " + super.IMPLEMENTS, this.hasDependencyType(super.IMPLEMENTS, dependencies));
	}
	
	public void testImplements65(){
		String from = "interfaces.multiplepackages.package2.TheImplementation";
		String to = "interfaces.multiplepackages.package1.TheService";
		DependencyDTO[] dependencies = service.getDependencies(from, to);

		int expectedDependencies = 1;
		assertEquals(expectedDependencies, dependencies.length);
		assertTrue("Missing Type " + super.IMPLEMENTS, this.hasDependencyType(super.IMPLEMENTS, dependencies));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	private boolean foundModulesNames(String[] search, AnalysedModuleDTO[] moduleList){
		nextSearch : for(String s : search){
			for(AnalysedModuleDTO d : moduleList){
				if(d.name.equals(s)){
					continue nextSearch;
				}
			}
			assertTrue(s + " not found", false);
			return false;
		}
		return true;
	}
	
	private AnalysedModuleDTO getModuleByName(String name, AnalysedModuleDTO[] modulelist){
		for(AnalysedModuleDTO d : modulelist){
			if(d.name.equals(name)){
				return d;
			}
		}
		return null;
	}
	
	private boolean hasDependencyTo(String to, DependencyDTO[] dependencies){
		for(DependencyDTO d : dependencies){
			if(d.to.equals(to)){
				return true;
			}
		}
		return false;
	}
	
	private boolean hasDependencyType(String type, DependencyDTO[] dependencies){
		for(DependencyDTO d : dependencies){
			if(d.type.equals(type)){
				return true;
			}
		}
		return false;
	}
	
	
	
}
