package husaccttest.analyse;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import husacct.analyse.AnalyseServiceImpl;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import junit.framework.*;


public class TestDomein extends TestCase{

	private AnalyseServiceImpl service;
	
	public void setUp(){
		service = new AnalyseServiceImpl();
	}
	
	public void testAvailableLanguages(){
		String[] languages = service.getAvailableLanguages();
		assertEquals("Java", languages[0]);
		assertEquals("C#", languages[1]);
	}

	public void testGetModules(){
		AnalysedModuleDTO[] modules = service.getRootModules();		
		assertEquals(2, modules.length);
		assertEquals("domain", modules[0].name);
		assertEquals("infrastructure", modules[1].name);
		assertEquals("domain", modules[0].uniqueName);
		assertEquals("infrastructure", modules[1].uniqueName);
		assertNull(modules[0].subModules);
		assertNull(modules[1].subModules);
	}
	
	public void testGetChildInModule(){
		AnalysedModuleDTO[] modules = service.getChildModulesInModule("domain.locationbased");
		assertEquals(2, modules.length);
		assertEquals("domain.locationbased.latitude", modules[1].uniqueName);
		assertEquals("domain.locationbased.foursquare", modules[0].uniqueName);
		
		
		assertEquals("Account", modules[0].subModules.get(0).name);
		assertEquals("domain.locationbased.foursquare.Account", modules[0].subModules.get(0).uniqueName);
		
		assertEquals("Friends", modules[0].subModules.get(1).name);
		assertEquals("domain.locationbased.foursquare.Friends", modules[0].subModules.get(1).uniqueName);
		
		assertEquals("Map", modules[0].subModules.get(2).name);
		assertEquals("domain.locationbased.foursquare.Map", modules[0].subModules.get(2).uniqueName);
		
		assertEquals("History", modules[0].subModules.get(3).name);
		assertEquals("domain.locationbased.foursquare.History", modules[0].subModules.get(3).uniqueName);
	}
	
	public void testGetChildModulesInDepth(){
		AnalysedModuleDTO[] modules = service.getChildModulesInModule("domain.locationbased", 2);
		
		assertEquals("domain.locationbased.foursquare", modules[0].uniqueName);
		assertEquals("domain.locationbased.foursquare.Account", modules[0].subModules.get(0).uniqueName);
		assertNull(modules[0].subModules.get(0).subModules);
		
	}
	
	public void testGetParentModule(){
		AnalysedModuleDTO module = service.getParentModuleForModule("domain.locationbased.foursquare");
		
		assertEquals("domain.locationbased", module.uniqueName);
				
	}
		
	public void testGetDependencyFrom(){
		DependencyDTO[] dependencies = service.getDependency("domain.locationbased.foursquare.History", "infrastructure.socialmedia.locationbased.foursquare.HistoryDAO");		
		assertEquals(1, dependencies.length);
	}

	
	public void testGetDependencyFromPackageLevel(){
		DependencyDTO[] dependencies = service.getDependency("domain.locationbased.foursquare");		
		assertEquals(4, dependencies.length);
	}
	
	
	

	
	
	public void testGetDependencyFromAndToClasses(){
		String fromPath = "domain.locationbased.foursquare.History";
		String toPath = "infrastructure.socialmedia.locationbased.foursquare.HistoryDAO";
		int totalDependenciesExpected = 1;
		
		DependencyDTO[] dependencies = service.getDependency(fromPath, toPath);
		assertEquals(totalDependenciesExpected, dependencies.length);
		
		String fromPathExpected = fromPath;
		String toPathExpected = toPath;
		String typeExpected = "Extends";
		int linenumberExpected = 10;
		
		HashMap<String, Object> expectedDependency = createDependencyHashmap(
				fromPathExpected, toPathExpected, typeExpected, linenumberExpected);
		
		boolean foundDependency = compaireDTOWithValues(expectedDependency, dependencies);
		assertTrue(foundDependency);
	}
	
	public void testGetDependenciesFromAndToPackages(){
		String fromPath = "domain.locationbased.latitude";
		String toPath = "infrastructure.socialmedia.locationbased.latitude";
		int totalDependenciesExpected = 3;
		
		DependencyDTO[] dependencies = service.getDependency(fromPath, toPath);
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
		assertTrue(foundAccountDependency);
		assertTrue(foundFriendsDependency);
		assertTrue(foundMapDependency);
	}
	
	public void testGetDependenciesFromPackageToClass(){
		String fromPath = "domain.locationbased.latitude";
		String toPath = "infrastructure.socialmedia.locationbased.latitude.IMap";
		int totalDependenciesExpected = 1;
		
		DependencyDTO[] dependencies = service.getDependency(fromPath, toPath);
		
		assertEquals(totalDependenciesExpected, dependencies.length);
		
		String fromPathExpected = fromPath + ".Map";
		String toPathExpected = toPath;
		String typeExpected = "Implements";
		int linenumberExpected = 10;
		
		HashMap<String, Object> expectedDependency = createDependencyHashmap(fromPathExpected, toPathExpected, typeExpected, linenumberExpected);
		boolean foundDependency = compaireDTOWithValues(expectedDependency, dependencies);
		assertTrue(foundDependency);		
	}
	
	public void testGetDependenciesFromAndToWithoutRelation(){
		String fromPath = "domain.locationbased.foursquare.History";
		String toPath = "infrastructure.socialmedia.locationbased.latitude.IMap";
		int totalDependenciesExpected = 0;
		
		DependencyDTO[] dependencies = service.getDependency(fromPath, toPath);
		assertEquals(totalDependenciesExpected, dependencies.length);
		assertNotNull(dependencies);
	}
	
	public void testGetDependencyFromAndToNotExistingValus(){
		String fromPath = "domain.notexisting";
		String toPath = "infrastructure.notexisting";
		int totalDependenciesExpected = 0;
		
		DependencyDTO[] dependencies = service.getDependency(fromPath, toPath);
		assertEquals(totalDependenciesExpected, dependencies.length);
		assertNotNull(dependencies);
	}
	
	public void testGetAllDependenciesOfClass(){
		String fromPath = "domain.locationbased.foursquare.Account";
		int totalDependencies = 1;
		
		DependencyDTO[] dependencies = service.getDependency(fromPath);
		assertEquals(totalDependencies, dependencies.length);
		
		String fromPathExpected = fromPath;
		String toPathExpected = "infrastructure.socialmedia.locationbased.foursquare.AccountDAO";
		String typeExpected = "InvocConstructor";
		int linenumberExpected = 10;
		
		HashMap<String, Object> expectedDependency = createDependencyHashmap(fromPathExpected, toPathExpected, typeExpected, linenumberExpected);
		boolean foundDependency = compaireDTOWithValues(expectedDependency, dependencies);
		assertTrue(foundDependency); 
	}
	
	public void testGetAllDependenciesOfPackage(){
		String fromPath = "domain.locationbased.latitude";
		int totalDependencies = 3;		
		
		DependencyDTO[] dependencies = service.getDependency(fromPath);
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
		assertTrue(accountFoundDependency);
		assertTrue(friendsFoundDependency);
		assertTrue(mapFoundDependency);
	}
	
	public void testGetAvailableLanguages(){
		int totalLanguagesExpected = 2;
		String[] availableLanguages = service.getAvailableLanguages();
		
		assertEquals(totalLanguagesExpected, availableLanguages.length);
		assertTrue(itemExistInArray("Java", availableLanguages));
		assertTrue(itemExistInArray("C#", availableLanguages));
	}
	
	public void testGetRootModules(){
		int totalModulesExpected = 2;
		
		AnalysedModuleDTO[] modules = service.getRootModules();
		assertEquals(totalModulesExpected, modules.length);
		
		String domainNameExpected = "domain";
		String domainUniqueNameExpected = "domain";
		int domainSubmoduleCount = 1;
		String domainTypeExpected = "package";
		
		HashMap<String, Object> accountExpectedDependency = createModuleHashmap(
				domainNameExpected, domainUniqueNameExpected, domainSubmoduleCount, domainTypeExpected);
		
		boolean domainFoundModule = compaireDTOWithValues(accountExpectedDependency, modules);
		assertTrue(domainFoundModule);
		
		
		
		
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
		//moduleHashMap.put("submodules.length", totalSubmodules);
		moduleHashMap.put("type", type);
		
		return moduleHashMap;
	}
	
	
	//Generieke functie om DTO's te vergelijken met een hashmap
	//TODO : onafhankelijk van DTO maken (type DTO)
	private boolean compaireDTOWithValues(Object o, Object[] allDependencies){
		HashMap<String, Object> findingProperties = (HashMap<String, Object>) o;
		
		dependencyloop : for(Object currentDependency : allDependencies){
			keyloop : for(String currentKey : findingProperties.keySet()){
				
				try {
					Class objectPropertyClass = currentDependency.getClass();
					Field objectPropertyField = objectPropertyClass.getDeclaredField(currentKey);
					Object objectPropertyFieldValue = (Object) objectPropertyField.get(currentDependency).toString();
					Object checkingObject = (Object) findingProperties.get(currentKey);
					Object checkingObjectValue = checkingObject.toString();

					
					if(!objectPropertyFieldValue.equals(checkingObjectValue)){
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

