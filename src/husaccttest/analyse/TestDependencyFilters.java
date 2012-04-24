package husaccttest.analyse;

import java.util.HashMap;

import husacct.analyse.AnalyseServiceImpl;
import husacct.common.dto.DependencyDTO;

public class TestDependencyFilters extends TestCaseExtended{

	private AnalyseServiceImpl service;
	
	public void setUp(){
		service = new AnalyseServiceImpl();
	}
	
	
	public void testGetDependenciesBetweenClassTypeExtends(){
		String from = "domain.locationbased.foursquare.History";
		String to = "infrastructure.socialmedia.locationbased.foursquare.HistoryDAO";
		String[] dependencyFilter = {"Extends"};
		int dependenciesExpected = 1;
		
		DependencyDTO[] dependencies = service.getDependencies(from, to, dependencyFilter);
		assertEquals(dependenciesExpected, dependencies.length);
		
		String fromExpected = from;
		String toExpected = to;
		String typeExpected = dependencyFilter[0];
		int linenumberExpected = 10;
		
		HashMap<String, Object> expectedDependency = createDependencyHashmap(
				fromExpected, toExpected, typeExpected, linenumberExpected);
		
		boolean foundDependency = compaireDTOWithValues(expectedDependency, dependencies);
		assertEquals(true, foundDependency);	
	}
	
	public void testGetDependenciesBetweenPackageTypeExtends(){
		String from = "domain.locationbased.foursquare";
		String to = "infrastructure.socialmedia.locationbased.foursquare";
		String[] dependencyFilter = {"Extends"};
		int dependenciesExpected = 3;
		
		DependencyDTO[] dependencies = service.getDependencies(from, to, dependencyFilter);
		assertEquals(dependenciesExpected, dependencies.length);
		
		String historyFromExpected = from + ".History";
		String historyToExpected = to + ".HistoryDAO";
		String historyTypeExpected = dependencyFilter[0];
		int historyLinenumberExpected = 10;
		
		String friendsFromExpected = from + ".Friends";
		String friendsToExpected = to + ".FriendsDAO";
		String friendsTypeExpected = dependencyFilter[0];
		int friendsLinenumberExpected = 10;
		
		String mapFromExpected = from + ".Map";
		String mapToExpected = to + ".IMap";
		String mapTypeExpected = dependencyFilter[0];
		int mapLinenumberExpected = 10;
		
		HashMap<String, Object> historyDependency = createDependencyHashmap(
				historyFromExpected, historyToExpected, historyTypeExpected, historyLinenumberExpected);
		HashMap<String, Object> friendsDependency = createDependencyHashmap(
				friendsFromExpected, friendsToExpected, friendsTypeExpected, friendsLinenumberExpected);
		HashMap<String, Object> mapDependency = createDependencyHashmap(
				mapFromExpected, mapToExpected, mapTypeExpected, mapLinenumberExpected);
		
		boolean foundHistory = compaireDTOWithValues(historyDependency, dependencies);
		boolean foundFriends = compaireDTOWithValues(friendsDependency, dependencies);
		boolean foundMap = compaireDTOWithValues(mapDependency, dependencies);
		
		assertEquals(true, foundHistory);
		assertEquals(true, foundFriends);
		assertEquals(true, foundMap);
	}
	
	public void testGetDependenciesBetweenPackageTypeExtendsAndInvocConstructor(){
		String from = "domain.locationbased.foursquare";
		String to = "infrastructure.socialmedia.locationbased.foursquare";
		String[] dependencyFilter = {"Extends", "InvocConstructor"};
		int dependenciesExpected = 4;
		
		DependencyDTO[] dependencies = service.getDependencies(from, to, dependencyFilter);
		assertEquals(dependenciesExpected, dependencies.length);
		
		String historyFromExpected = from + ".History";
		String historyToExpected = to + ".HistoryDAO";
		String historyTypeExpected = dependencyFilter[0];
		int historyLinenumberExpected = 10;
		
		String friendsFromExpected = from + ".Friends";
		String friendsToExpected = to + ".FriendsDAO";
		String friendsTypeExpected = dependencyFilter[0];
		int friendsLinenumberExpected = 10;
		
		String mapFromExpected = from + ".Map";
		String mapToExpected = to + ".IMap";
		String mapTypeExpected = dependencyFilter[0];
		int mapLinenumberExpected = 10;
		
		String accountFromExpected = from + ".Account";
		String accountToExpected = to + ".AccountDAO";
		String accountTypeExpected = dependencyFilter[1];
		int accountLinenumberExpected = 10;
		
		HashMap<String, Object> historyDependency = createDependencyHashmap(
				historyFromExpected, historyToExpected, historyTypeExpected, historyLinenumberExpected);
		HashMap<String, Object> friendsDependency = createDependencyHashmap(
				friendsFromExpected, friendsToExpected, friendsTypeExpected, friendsLinenumberExpected);
		HashMap<String, Object> mapDependency = createDependencyHashmap(
				mapFromExpected, mapToExpected, mapTypeExpected, mapLinenumberExpected);
		HashMap<String, Object> accountDependency = createDependencyHashmap(
				accountFromExpected, accountToExpected, accountTypeExpected, accountLinenumberExpected);
		
		
		boolean foundHistory = compaireDTOWithValues(historyDependency, dependencies);
		boolean foundFriends = compaireDTOWithValues(friendsDependency, dependencies);
		boolean foundMap = compaireDTOWithValues(mapDependency, dependencies);
		boolean foundAccount = compaireDTOWithValues(accountDependency, dependencies);
		
		assertEquals(true, foundHistory);
		assertEquals(true, foundFriends);
		assertEquals(true, foundMap);
		assertEquals(true, foundAccount);
	}
	
	public void testGetDependenciesFilterWithoutResults(){
		String from = "domain.locationbased.foursquare";
		String to = "infrastructure.socialmedia.locationbased.foursquare";
		String[] dependencyFilter = {"Implements"};
		int dependenciesExpected = 0;
		
		DependencyDTO[] dependencies = service.getDependencies(from, to, dependencyFilter);
		assertEquals(dependenciesExpected, dependencies.length);
	}
	
	public void testGetDependenciesFilterWithNotExistingPackage(){
		String from = "domain.notExisting";
		String to = "infrastructure.socialmedia.locationbased.foursquare";
		String[] dependencyFilter = {"Extends"};
		int dependenciesExpected = 0;
		
		DependencyDTO[] dependencies = service.getDependencies(from, to, dependencyFilter);
		assertEquals(dependenciesExpected, dependencies.length);

		
	}


	
	
	
	
	
	
	
	
	
}

