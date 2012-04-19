package husaccttest.control;

import husacct.control.ControlServiceImpl;
import husacct.control.ILocaleChangeListener;

import java.util.Locale;

import junit.framework.TestCase;

import org.junit.Test;



public class ControllerTest extends TestCase {
	
	@Test
	public void testLocaleObserver(){
		ControlServiceImpl service = new ControlServiceImpl();
		service.addLocaleChangeListener(new ILocaleChangeListener() {
			@Override
			public void update(Locale newLocale) {
				assertEquals(newLocale.getLanguage(), "en");
			}
		});
		service.notifyLocaleListeners(Locale.ENGLISH);
	}
	
}
