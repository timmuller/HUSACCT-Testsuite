package husaccttest.control;

import husacct.control.ControlServiceImpl;
import husacct.control.ILocaleChangeListener;

import java.util.Locale;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;



public class ControllerTest extends TestCase {
	
	ControlServiceImpl service;
	
	@Before
	public void setup(){
		 service = new ControlServiceImpl();
	}
	
	@Test
	public void testLocaleObserver(){
		service.addLocaleChangeListener(new ILocaleChangeListener() {
			@Override
			public void update(Locale newLocale) {
				assertEquals(newLocale.getLanguage(), "en");
			}
		});
		service.notifyLocaleListeners(Locale.ENGLISH);
	}
	
	@Test
	public void testDefaultLocale(){
		assertEquals(service.getLocale(), Locale.ENGLISH);
	}
	

	
}
