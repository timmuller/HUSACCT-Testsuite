package husaccttest.analyse;

import husacct.analyse.AnalyseServiceImpl;
import husacct.analyse.domain.ModelCreationService;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import junit.framework.TestCase;

public abstract class TestCaseExtended extends TestCase{
	

	protected AnalyseServiceImpl service;
	private ModelCreationService famix;
	
	public void setUp(){
		service = new AnalyseServiceImpl();
		famix = new FamixCreationServiceImpl();
			
		fillFamixAsStub();
	}
	
	public boolean itemExistInArray(Object value, Object[] items){
		for(Object o : items){
			if(o.toString().equals(value.toString())){
				return true;
			}
		}
		return false;
	}
	
	public HashMap<String, Object> createDependencyHashmap(String from, String to, String type, int linenumber){
		HashMap<String, Object> dependencyHashMap = new HashMap<String, Object>();
		
		dependencyHashMap.put("from", from);
		dependencyHashMap.put("to", to);
		dependencyHashMap.put("type", type);
		dependencyHashMap.put("lineNumber", linenumber);
		
		return dependencyHashMap;
	}
	
	public HashMap<String, Object> createModuleHashmap(String name, String uniqueName, int totalSubmodules, String type){
		HashMap<String, Object> moduleHashMap = new HashMap<String, Object>();
			
		moduleHashMap.put("name", name);
		moduleHashMap.put("uniqueName", uniqueName);
		moduleHashMap.put("subModules", totalSubmodules);
		moduleHashMap.put("type", type);
		
		return moduleHashMap;
	}
	
	public boolean compaireDTOWithValues(Object o, Object[] allDependencies){

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
					
					System.out.println(objectPropertyField.getName()+ " - " + objectPropertyFieldValue.toString() + " - " + checkingObjectValue.toString());
					
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
	
	private void fillFamixAsStub(){
		famix.createPackage("domain", "", "domain");
		famix.createPackage("domain.locationbased", "domain", "locationbased");
		famix.createPackage("domain.locationbased.latitude", "domain.locationbased", "latitude");
		famix.createPackage("domain.locationbased.foursquare", "domain.locationbased", "foursquare");
		famix.createPackage("infrastructure", "", "infrastructure");
		famix.createPackage("infrastructure.socialmedia", "infrastructure", "socialmedia");
		famix.createPackage("infrastructure.socialmedia.locationbased", "infrastructure.socialmedia", "locationbased");
		famix.createPackage("infrastructure.socialmedia.locationbased.latitude", "infrastructure.socialmedia.locationbased", "latitude");
		famix.createPackage("infrastructure.socialmedia.locationbased.foursquare", "infrastructure.socialmedia.locationbased", "foursquare");
		
		famix.createClass("domain.locationbased.foursquare.Account", "Account", "domain.locationbased.foursquare", false, false);
		famix.createClass("domain.locationbased.foursquare.Friends", "Friends", "domain.locationbased.foursquare", false, false, "infrastructure.socialmedia.locationbased.foursquare.FriendsDAO");
		famix.createClass("domain.locationbased.foursquare.Map", "Map", "domain.locationbased.foursquare", false, false, "infrastructure.socialmedia.locationbased.foursquare.IMap");
		famix.createClass("domain.locationbased.foursquare.History", "History", "domain.locationbased.foursquare", false, false, "infrastructure.socialmedia.locationbased.foursquare.HistoryDAO");
		
		famix.createClass("domain.locationbased.latitude.Account", "Account", "domain.locationbased.latitude", false, false);
		famix.createClass("domain.locationbased.latitude.Friends", "Friends", "domain.locationbased.latitude", false, false, "infrastructure.socialmedia.locationbased.latitude.FriendsDAO");
		famix.createClass("domain.locationbased.latitude.Map", "Map", "domain.locationbased.latitude", false, false, "infrastructure.socialmedia.locationbased.latitude.IMap");
		
		famix.createClass("infrastructure.socialmedia.locationbased.foursquare.AccountDAO", "AccountDAO", "infrastructure.socialmedia.locationbased.foursquare", false, false);
		famix.createClass("infrastructure.socialmedia.locationbased.foursquare.FriendsDAO", "FriendsDAO", "infrastructure.socialmedia.locationbased.foursquare", true, false);
		famix.createClass("infrastructure.socialmedia.locationbased.foursquare.HistoryDAO", "HistoryDAO", "infrastructure.socialmedia.locationbased.foursquare", false, false);
		famix.createClass("infrastructure.socialmedia.locationbased.foursquare.IMap", "IMap", "infrastructure.socialmedia.locationbased.foursquare", false, false);
		
		famix.createClass("infrastructure.socialmedia.locationbased.latitude.AccountDAO", "AccountDAO", "infrastructure.socialmedia.locationbased.latitude", false, false);
		famix.createClass("infrastructure.socialmedia.locationbased.latitude.FriendsDAO", "FriendsDAO", "infrastructure.socialmedia.locationbased.latitude", true, false);
		famix.createClass("infrastructure.socialmedia.locationbased.latitude.IMap", "IMap", "infrastructure.socialmedia.locationbased.latitude", false, false);
		
		famix.createInheritanceDefinition("domain.locationbased.foursquare.Friends", "infrastructure.socialmedia.locationbased.foursquare.FriendsDAO", 10);
		famix.createInheritanceDefinition("domain.locationbased.foursquare.Map", "infrastructure.socialmedia.locationbased.foursquare.IMap", 10);
		famix.createInheritanceDefinition("domain.locationbased.foursquare.History", "infrastructure.socialmedia.locationbased.foursquare.HistoryDAO", 10);
		famix.createInheritanceDefinition("domain.locationbased.latitude.Friends", "infrastructure.socialmedia.locationbased.latitude.FriendsDAO", 10);
		famix.createImplementsDefinition("domain.locationbased.latitude.Map", "infrastructure.socialmedia.locationbased.latitude.IMap", 10);
		famix.createAttribute(false, "private", "domain.locationbased.foursquare.Account", "infrastructure.socialmedia.locationbased.foursquare.AccountDAO", "Account", "domain.locationbased.foursquare.Account", 11);
		famix.createAttribute(false, "private", "domain.locationbased.latitude.Account", "infrastructure.socialmedia.locationbased.latitude.AccountDAO", "Account", "domain.locationbased.latitude.Account", 10);
		
		famix.connectDependencies();
		
		//System.out.println(famix.represent());
	}
	
}
