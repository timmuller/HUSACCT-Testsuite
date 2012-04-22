package husaccttest.analyse;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import husacct.analyse.AnalyseServiceImpl;
import husacct.analyse.IAnalyseService;
import husacct.analyse.presentation.analyseController;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import junit.framework.*;


public class TestDomein extends TestCase{

	private AnalyseServiceImpl service;
	
	public void setUp(){
		service = new AnalyseServiceImpl();
	}
	

	public void testGetDependencyFromAndToClasses(){
		String fromPath = "domain.locationbased.foursquare.History";
		String toPath = "infrastructure.socialmedia.locationbased.foursquare.HistoryDAO";
		int totalDependenciesExpected = 1;
		
		DependencyDTO[] dependencies = service.getDependencies(fromPath, toPath);
		assertEquals(totalDependenciesExpected, dependencies.length);
		
		String fromPathExpected = fromPath;
		String toPathExpected = toPath;
		String typeExpected = "Extends";
		int linenumberExpected = 10;
		
		HashMap<String, Object> expectedDependency = createDependencyHashmap(
				fromPathExpected, toPathExpected, typeExpected, linenumberExpected);
		
		boolean foundDependency = compaireDTOWithValues(expectedDependency, dependencies);
		assertEquals(true, foundDependency);
	}
	
	public void testGetDependenciesFromAndToPackages(){
		String fromPath = "domain.locationbased.latitude";
		String toPath = "infrastructure.socialmedia.locationbased.latitude";
		int totalDependenciesExpected = 3;
		
		DependencyDTO[] dependencies = service.getDependencies(fromPath, toPath);
		assertEquals(totalDependenciesExpected, dependencies.length);
		
		
		
		String accountFromPathExpected = fromPath + ".Account";
		String accountToPathExpected = toPath + ".AccountDAO";
		String accountTypeExpected = "InvocConstructor";
		int accountLinenumberExpected = 11;
		
		String friendsFromPathExpected = fromPath +".Friends";
		String friendsToPathExpected = toPath + ".FriendsDAO";
		String friendsTypeExpected = "Extends";
		int friendsLinenumberExpected = 10;

		String mapFromPathExpected = fromPath + ".Map";
		String mapToPathExpected = toPath + ".IMap";
		String mapTypeExpected = "Implements";
		int mapLinenumberExpected = 10;
		
		HashMap<String, Object> expectedAccountDependency = createDependencyHashmap (
				accountFromPathExpected, accountToPathExpected, accountTypeExpected, accountLinenumberExpected);
		
		HashMap<String, Object> expectedFriendsDependency = createDependencyHashmap(
				friendsFromPathExpected, friendsToPathExpected, friendsTypeExpected, friendsLinenumberExpected);
		
		HashMap<String, Object> expectedMapDependency = createDependencyHashmap(
				mapFromPathExpected, mapToPathExpected, mapTypeExpected, mapLinenumberExpected);
		
		ArrayList<Object> expectedDependencies = new ArrayList<Object>();
		expectedDependencies.add(expectedAccountDependency);
		expectedDependencies.add(expectedFriendsDependency);
		expectedDependencies.add(expectedMapDependency);
		
		boolean foundAccountDependency = compaireDTOWithValues(expectedAccountDependency, dependencies);
		boolean foundFriendsDependency = compaireDTOWithValues(expectedFriendsDependency, dependencies);
		boolean foundMapDependency = compaireDTOWithValues(expectedMapDependency, dependencies);
		assertEquals(true, foundAccountDependency);
		assertEquals(true, foundFriendsDependency);
		assertEquals(true, foundMapDependency);
	}
	
	public void testGetDependenciesFromPackageToClass(){
		String fromPath = "domain.locationbased.latitude";
		String toPath = "infrastructure.socialmedia.locationbased.latitude.IMap";
		int totalDependenciesExpected = 1;
		
		DependencyDTO[] dependencies = service.getDependencies(fromPath, toPath);
		
		assertEquals(totalDependenciesExpected, dependencies.length);
		
		String fromPathExpected = fromPath + ".Map";
		String toPathExpected = toPath;
		String typeExpected = "Implements";
		int linenumberExpected = 10;
		
		HashMap<String, Object> expectedDependency = createDependencyHashmap(fromPathExpected, toPathExpected, typeExpected, linenumberExpected);
		boolean foundDependency = compaireDTOWithValues(expectedDependency, dependencies);
		assertEquals(true, foundDependency);		
	}
	
	public void testGetDependenciesFromAndToWithoutRelation(){
		String fromPath = "domain.locationbased.foursquare.History";
		String toPath = "infrastructure.socialmedia.locationbased.latitude.IMap";
		int totalDependenciesExpected = 0;
		
		DependencyDTO[] dependencies = service.getDependencies(fromPath, toPath);
		assertEquals(totalDependenciesExpected, dependencies.length);
		assertNotNull(dependencies);
	}
	
	public void testGetDependencyFromAndToNotExistingValus(){
		String fromPath = "domain.notexisting";
		String toPath = "infrastructure.notexisting";
		int totalDependenciesExpected = 0;
		
		DependencyDTO[] dependencies = service.getDependencies(fromPath, toPath);
		assertEquals(totalDependenciesExpected, dependencies.length);
		assertNotNull(dependencies);
	}
	
	public void testGetAllDependenciesOfClass(){
		String fromPath = "domain.locationbased.foursquare.Account";
		int totalDependencies = 1;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(fromPath);
		assertEquals(totalDependencies, dependencies.length);
		
		String fromPathExpected = fromPath;
		String toPathExpected = "infrastructure.socialmedia.locationbased.foursquare.AccountDAO";
		String typeExpected = "InvocConstructor";
		int linenumberExpected = 10;
		
		HashMap<String, Object> expectedDependency = createDependencyHashmap(fromPathExpected, toPathExpected, typeExpected, linenumberExpected);
		boolean foundDependency = compaireDTOWithValues(expectedDependency, dependencies);
		assertEquals(true, foundDependency); 
	}
	
	public void testGetDependenciesToClass(){
		String toPath = "infrastructure.socialmedia.locationbased.foursquare.AccountDAO";
		int totalDependencies = 1;
		
		DependencyDTO[] dependencies = service.getDependenciesTo(toPath);
		assertEquals(totalDependencies, dependencies.length);
		
		String fromExpected = "domain.locationbased.foursquare.Account";
		String toExpected = toPath;
		String typeExpected = "InvocConstructor";
		int lineExpected = 10;
		
		HashMap<String, Object> expectedDependency = createDependencyHashmap(
				fromExpected, toExpected, typeExpected, lineExpected);
		
		boolean foundDependency = compaireDTOWithValues(expectedDependency, dependencies);
		assertEquals(true, foundDependency);
	}
	
	public void testGetDependenciesToPackage(){
		String toPath = "infrastructure.socialmedia.locationbased.latitude";
		int totalDependenciesExpected = 3;
				
		DependencyDTO[] dependencies = service.getDependenciesTo(toPath);
		assertEquals(totalDependenciesExpected, dependencies.length);
		
		String accountFromExpected = "domain.locationbased.latitude.Account";
		String accountToExpected = toPath + ".AccountDAO";
		String accountTypeExpected = "InvocConstructor";
		int accountLineExpected = 11;
		
		String friendsFromExpected = "domain.locationbased.latitude.Friends";
		String friendsToExpected = toPath + ".FriendsDAO";
		String friendsTypeExpected = "Extends";
		int friendsLineExpected = 10;
		
		String mapFromExpected  = "domain.locationbased.latitude.Map";
		String mapToExpected = toPath + ".IMap";
		String mapTypeExpected = "Implements";
		int mapLineExpected = 10;
		
		HashMap<String, Object> accountDependency = createDependencyHashmap(accountFromExpected, accountToExpected, accountTypeExpected, accountLineExpected);
		HashMap<String, Object> friendsDependency = createDependencyHashmap(friendsFromExpected, friendsToExpected, friendsTypeExpected, friendsLineExpected);
		HashMap<String, Object> mapDependency = createDependencyHashmap(mapFromExpected, mapToExpected, mapTypeExpected, mapLineExpected);
		
		boolean foundAccount = compaireDTOWithValues(accountDependency, dependencies);
		boolean foundFriends = compaireDTOWithValues(friendsDependency, dependencies);
		boolean foundMap = compaireDTOWithValues(mapDependency, dependencies);
		
		assertEquals(true, foundAccount);
		assertEquals(true, foundFriends);
		assertEquals(true, foundMap);
	}
	
	public void testGetDependencyToUnknown(){
		String toPath = "infrastructure.unknown";
		int totalExpected = 0;
		
		DependencyDTO[] dependencies = service.getDependenciesTo(toPath);
		assertEquals(totalExpected, dependencies.length);
	}
	
	public void testGetAllDependenciesOfPackage(){
		String fromPath = "domain.locationbased.latitude";
		int totalDependencies = 3;		
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(fromPath);
		assertEquals(totalDependencies, dependencies.length);
		
		String accountFromPathExpected = fromPath + ".Account";
		String accountToPathExpected = "infrastructure.socialmedia.locationbased.latitude.AccountDAO";
		String accountTypeExpected = "InvocConstructor";
		int accountLinenumberExpected = 11;
		
		String friendsFromPathExpected = fromPath + ".Friends";
		String friendsToPathExpected = "infrastructure.socialmedia.locationbased.latitude.FriendsDAO";
		String friendsTypeExpected = "Extends";
		int friendsLinenumberExpected = 10;
		
		String mapFromPathExpected = fromPath + ".Map";
		String mapToPathExpected = "infrastructure.socialmedia.locationbased.latitude.IMap";
		String mapTypeExpected = "Implements";
		int mapLinenumberExpected = 10;
		
		
		HashMap<String, Object> accountExpectedDependency = createDependencyHashmap(
				accountFromPathExpected, accountToPathExpected, accountTypeExpected, accountLinenumberExpected);
		HashMap<String, Object> friendsExpectedDependency = createDependencyHashmap(
				friendsFromPathExpected, friendsToPathExpected, friendsTypeExpected, friendsLinenumberExpected);
		HashMap<String, Object> mapExpectedDependency = createDependencyHashmap(
				mapFromPathExpected, mapToPathExpected, mapTypeExpected, mapLinenumberExpected);
		
		boolean accountFoundDependency = compaireDTOWithValues(accountExpectedDependency, dependencies);
		boolean friendsFoundDependency = compaireDTOWithValues(friendsExpectedDependency, dependencies);
		boolean mapFoundDependency = compaireDTOWithValues(mapExpectedDependency, dependencies);
		assertEquals(true, accountFoundDependency);
		assertEquals(true, friendsFoundDependency);
		assertEquals(true, mapFoundDependency);
	}
	
	public void testGetAvailableLanguages(){
		int totalLanguagesExpected = 2;
		String[] availableLanguages = service.getAvailableLanguages();
		
		assertEquals(totalLanguagesExpected, availableLanguages.length);
		assertEquals(true, itemExistInArray("Java", availableLanguages));
		assertEquals(true, itemExistInArray("C#", availableLanguages));
	}
	
	public void testGetRootModules(){
		int totalModulesExpected = 2;
		
		AnalysedModuleDTO[] modules = service.getRootModules();
		assertEquals(totalModulesExpected, modules.length);
		
		String domainNameExpected = "domain";
		String domainUniqueNameExpected = "domain";
		int domainSubmoduleCount = 0;
		String domainTypeExpected = "package";
		
		String infrastructureNameExpected = "infrastructure";
		String infrastructureUniqueNameExpected = "infrastructure";
		int infrastructureSubmoduleCount = 0;
		String infrastructureTypeExpected = "package";
		
		HashMap<String, Object> domainExpectedModule = createModuleHashmap(
				domainNameExpected, domainUniqueNameExpected, domainSubmoduleCount, domainTypeExpected);
		
		HashMap<String, Object> infrastructureExpectedModule = createModuleHashmap(
				infrastructureNameExpected, infrastructureUniqueNameExpected, infrastructureSubmoduleCount, infrastructureTypeExpected);
		
		boolean domainFoundModule = compaireDTOWithValues(domainExpectedModule, modules);
		boolean infrastructureFoundModule = compaireDTOWithValues(infrastructureExpectedModule, modules);
		assertEquals(true, domainFoundModule);
		assertEquals(true, infrastructureFoundModule);
	}
	
	public void testGetChildrenOfPackageModule(){
		int totalModulesExpected = 2;
		String modulesFrom = "domain.locationbased";
		
		AnalysedModuleDTO[] modules = service.getChildModulesInModule(modulesFrom);
		assertEquals(totalModulesExpected, modules.length);
		
		String foursquareNameExpected = "foursquare";
		String foursquareUniqueNameExpected = modulesFrom + ".foursquare";
		int foursquareSubmodulesExpected = 4;
		String foursquareTypeExpected = "package";
		
		String latitudeNameExpected = "latitude";
		String latitudeUniqueNameExpected = modulesFrom + ".latitude";
		int latitudeSubmodulesExpected = 3;
		String latitudeTypeExpected = "package";
		
		HashMap<String, Object> foursquareExpectedModule = createModuleHashmap(
				foursquareNameExpected, foursquareUniqueNameExpected, foursquareSubmodulesExpected, foursquareTypeExpected);
		HashMap<String, Object> latitudeExpectedModule = createModuleHashmap(
				latitudeNameExpected, latitudeUniqueNameExpected, latitudeSubmodulesExpected, latitudeTypeExpected);
		
		boolean foursquareFoundModule = compaireDTOWithValues(foursquareExpectedModule, modules);
		boolean latitudeFoundModule = compaireDTOWithValues(latitudeExpectedModule, modules);
		assertEquals(true, foursquareFoundModule);
		assertEquals(true, latitudeFoundModule);
	}
	
	public void testGetChildrenOfClassModule(){
		int totalModulesExpected = 0;
		String modulesFrom = "domain.locationbased.foursquare.Account";
		
		AnalysedModuleDTO[] modules = service.getChildModulesInModule(modulesFrom);
		assertEquals(totalModulesExpected, modules.length);
	}
	
	public void testGetChildrenOfNotExistingModule(){
		int totalModulesExpected = 0;
		String modulesFrom = "domain.notExisting";
		
		AnalysedModuleDTO[] modules = service.getChildModulesInModule(modulesFrom);
		assertEquals(totalModulesExpected, modules.length);
	}
	
	public void testGetChilderenOfPackageWithDepthOne(){
		int totalModulesExpected = 2;
		String modulesFrom = "domain.locationbased";
		int depth = 1;
		
		AnalysedModuleDTO[] modules = service.getChildModulesInModule(modulesFrom, depth);
		assertEquals(totalModulesExpected, modules.length);
		
		String foursquareNameExpected = "foursquare";
		String foursquareUniqueNameExpected = modulesFrom + "." + foursquareNameExpected;
		int foursquareSubmodulesExpected = 0;
		String foursquareTypeExpected = "package";
		
		String latitudeNameExpected = "latitude";
		String latitudeUniqueNameExpected = modulesFrom + "." + latitudeNameExpected;
		int latitudeSubmodulesExpected = 0;
		String latitudeTypeExpected = "package";
		
		HashMap<String, Object> foursquareExpectedModule = createModuleHashmap(foursquareNameExpected, foursquareUniqueNameExpected, foursquareSubmodulesExpected, foursquareTypeExpected);
		HashMap<String, Object> latitudeExpectedModule = createModuleHashmap(latitudeNameExpected, latitudeUniqueNameExpected, latitudeSubmodulesExpected, latitudeTypeExpected);
		
		boolean foundFoursquare = compaireDTOWithValues(foursquareExpectedModule, modules);
		boolean foundLatitude = compaireDTOWithValues(latitudeExpectedModule, modules);
		assertEquals(true, foundFoursquare);
		assertEquals(true, foundLatitude);
	}
	
	public void testGetChildrenOfPackageWithDepthTwo(){
		int totalModulesExpected = 2;
		String modulesFrom = "domain.locationbased";
		int depth = 2;
		
		AnalysedModuleDTO[] modules = service.getChildModulesInModule(modulesFrom, depth);
		assertEquals(totalModulesExpected, modules.length);
		
		String foursquareNameExpected = "foursquare";
		String foursquareUniqueNameExpected = modulesFrom + "." + foursquareNameExpected;
		int foursquareSubmodulesExpected = 4;
		String foursquareTypeExpected = "package";
		
		String latitudeNameExpected = "latitude";
		String latitudeUniqueNameExpected = modulesFrom + "." + latitudeNameExpected;
		int latitudeSubmodulesExpected = 3;
		String latitudeTypeExpected = "package";
		
		HashMap<String, Object> foursquareExpectedModule = createModuleHashmap(
				foursquareNameExpected, foursquareUniqueNameExpected, foursquareSubmodulesExpected, foursquareTypeExpected);
		HashMap<String, Object> latitudeExpectedModule = createModuleHashmap(
				latitudeNameExpected, latitudeUniqueNameExpected, latitudeSubmodulesExpected, latitudeTypeExpected);
		
		boolean foundFoursquare = compaireDTOWithValues(foursquareExpectedModule, modules);
		boolean foundLatitude = compaireDTOWithValues(latitudeExpectedModule, modules);
		assertEquals(true, foundFoursquare);
		assertEquals(true, foundLatitude);
	}
	
	public void testGetChildrenOfPackageDepthZero(){
		int totalModulesExpected = 1;
		String modulesFrom = "domain";
		int depth = 0;
		
		AnalysedModuleDTO[] modules = service.getChildModulesInModule(modulesFrom, depth);
		assertEquals(totalModulesExpected, modules.length);
		
		String locationbasedNameExpected = "locationbased";
		String locationbasedUniqueNameExpected = modulesFrom + "." + locationbasedNameExpected;
		int locationbasedSubmodulesExpected = 2;
		String locationbasedTypeExpected = "package";
		
		HashMap<String, Object> locationbasedExpectedModule = createModuleHashmap(
				locationbasedNameExpected, locationbasedUniqueNameExpected, locationbasedSubmodulesExpected, locationbasedTypeExpected);
		boolean foundLocationbased = compaireDTOWithValues(locationbasedExpectedModule, modules);
		assertEquals(true, foundLocationbased);
		
		
		List<AnalysedModuleDTO> submodules = modules[0].subModules;
		int totalSubModules = 2;
		assertEquals(totalSubModules, submodules.size());
		
		
		List<AnalysedModuleDTO> foursquaresubmodules = submodules.get(0).subModules;
		int totalFoursquareSubModules = 4;
		assertEquals(totalFoursquareSubModules, foursquaresubmodules.size());
		
		List<AnalysedModuleDTO> latitudesubmodules = submodules.get(1).subModules;
		int totalLatitudeSubModules = 3;
		assertEquals(totalLatitudeSubModules, latitudesubmodules.size());
		
	}
	
	public void testGetChildrenOfClassDepthOne(){
		int totalModulesExpected = 0;
		String modulesFrom = "domain.locationbased.latitude.Account";
		int depth = 1;
	
		AnalysedModuleDTO[] modules = service.getChildModulesInModule(modulesFrom, depth);
		assertEquals(totalModulesExpected, modules.length);
	}
	
	public void testGetChildrenOfClassDepthTwo(){
		int totalModulesExpected = 0;
		String modulesFrom = "domain.locationbased.latitude.Account";
		int depth = 2;
	
		AnalysedModuleDTO[] modules = service.getChildModulesInModule(modulesFrom, depth);
		assertEquals(totalModulesExpected, modules.length);
	}
	
	public void testGetChildrenOfClassDepthZero(){
		int totalModulesExpected = 0;
		String modulesFrom = "domain.locationbased.latitude.Account";
		int depth = 0;
	
		AnalysedModuleDTO[] modules = service.getChildModulesInModule(modulesFrom, depth);
		assertEquals(totalModulesExpected, modules.length);
	}
	
	public void testGetChildrenOfUnknownPackageDepthZero(){
		int totalModulesExpected = 0;
		String modulesFrom = "domain.unknown";
		int depth = 0;
	
		AnalysedModuleDTO[] modules = service.getChildModulesInModule(modulesFrom, depth);
		assertEquals(totalModulesExpected, modules.length);
	}
	
	public void testGetParentOfPackageLevelTwo(){
		String parentFrom = "domain.locationbased";
		AnalysedModuleDTO module = service.getParentModuleForModule(parentFrom);
				
		String nameExpected = "domain";
		String uniqueNameExpected = "domain";
		int submoduleExpected = 1;
		String typeExpected = "package";
		
		assertEquals(nameExpected, module.name);
		assertEquals(uniqueNameExpected, module.uniqueName);
		assertEquals(submoduleExpected, module.subModules.size());
		assertEquals(typeExpected, module.type);
		
		AnalysedModuleDTO locationbasedModule = module.subModules.get(0);
		
		String locationbasedNameExpected = "locationbased";
		String locationbasedUniquenameExpected = "domain.locationbased";
		int locationbasedSubmodulesExpected = 0;
		String locationbasedTypeExpected = "package";
		
		assertEquals(locationbasedNameExpected, locationbasedModule.name);
		assertEquals(locationbasedUniquenameExpected, locationbasedModule.uniqueName);
		assertEquals(locationbasedSubmodulesExpected, locationbasedModule.subModules.size());
		assertEquals(locationbasedTypeExpected, locationbasedModule.type);
		
	}
	
	public void testGetParentOfPackageLevelOne(){
		String parentFrom = "domain";
		AnalysedModuleDTO parentModule = service.getParentModuleForModule(parentFrom);
		
		String nameExpected = "";
		String uniqueNameExpected = "";
		int submodulesExpected = 0;
		String typeExpected = "";
		
		assertEquals(nameExpected, parentModule.name);
		assertEquals(uniqueNameExpected, parentModule.uniqueName);
		assertEquals(submodulesExpected, parentModule.subModules.size());
		assertEquals(typeExpected, parentModule.type);
	}
	
	public void testGetParentOfClassLevelFour(){
		String parentFrom = "domain.locationbased.foursquare.Account";
		AnalysedModuleDTO parentModule = service.getParentModuleForModule(parentFrom);
		
		String nameExpected = "foursquare";
		String uniquenameExpected = "domain.locationbased.foursquare";
		int totalSubmodulesExpected = 4;
		String typeExpected = "package";
		
		assertEquals(nameExpected, parentModule.name);
		assertEquals(uniquenameExpected, parentModule.uniqueName);
		assertEquals(totalSubmodulesExpected, parentModule.subModules.size());
		assertEquals(typeExpected, parentModule.type);
	}
	
	public void testGetParentOfNotExistingPackageLevelTwo(){
		String parentFrom = "domain.notExist";
		AnalysedModuleDTO parentModule = service.getParentModuleForModule(parentFrom);
		
		String nameExpected = "";
		String uniqueNameExpected = "";
		int submodulesExpected = 0;
		String typeExpected = "";
		
		assertEquals(nameExpected, parentModule.name);
		assertEquals(uniqueNameExpected, parentModule.uniqueName);
		assertEquals(submodulesExpected, parentModule.subModules.size());
		assertEquals(typeExpected, parentModule.type);
	}
	
	
	
	private boolean itemExistInArray(Object value, Object[] items){
		for(Object o : items){
			if(o.toString().equals(value.toString())){
				return true;
			}
		}
		return false;
	}
	
	private HashMap<String, Object> createDependencyHashmap(String from, String to, String type, int linenumber){
		HashMap<String, Object> dependencyHashMap = new HashMap<String, Object>();
		
		dependencyHashMap.put("from", from);
		dependencyHashMap.put("to", to);
		dependencyHashMap.put("type", type);
		dependencyHashMap.put("lineNumber", linenumber);
		
		return dependencyHashMap;
	}
	
	private HashMap<String, Object> createModuleHashmap(String name, String uniqueName, int totalSubmodules, String type){
		HashMap<String, Object> moduleHashMap = new HashMap<String, Object>();
			
		moduleHashMap.put("name", name);
		moduleHashMap.put("uniqueName", uniqueName);
		moduleHashMap.put("subModules", totalSubmodules);
		moduleHashMap.put("type", type);
		
		return moduleHashMap;
	}
	
	
	private boolean compaireDTOWithValues(Object o, Object[] allDependencies){

		@SuppressWarnings("unchecked")
		HashMap<String, Object> findingProperties = (HashMap<String, Object>) o;
		
		dependencyloop : for(Object currentDependency : allDependencies){
			for(String currentKey : findingProperties.keySet()){
								
				try {
					Class<? extends Object> objectPropertyClass = currentDependency.getClass();
					Field objectPropertyField = objectPropertyClass.getDeclaredField(currentKey);
														
					Object objectPropertyFieldValueObject = (Object) objectPropertyField.get(currentDependency);
					Object objectPropertyFieldValue;
					
					if(objectPropertyField.getType().getSimpleName().matches("List|Array")){
						if(objectPropertyFieldValueObject == null){
							objectPropertyFieldValue = (Integer) 0;
						} else {
								objectPropertyFieldValue = ((List<?>) objectPropertyFieldValueObject).size();	
						}
					} else {
						objectPropertyFieldValue = objectPropertyFieldValueObject.toString();
					}
					Object checkingObject = (Object) findingProperties.get(currentKey);
					Object checkingObjectValue = checkingObject.toString();
					
					if(!objectPropertyFieldValue.toString().equals(checkingObjectValue.toString())){
						continue dependencyloop;
					}
					
				} catch (Exception e) {
					return false;
				}

			}
			return true;
		}		
		return false;
	}	
}

