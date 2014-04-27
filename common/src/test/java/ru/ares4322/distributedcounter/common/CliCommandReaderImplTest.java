package ru.ares4322.distributedcounter.common;

import com.google.inject.AbstractModule;
import org.apache.commons.io.input.CharSequenceInputStream;
import org.mockito.Mockito;
import org.testng.annotations.*;

import javax.inject.Inject;
import java.io.InputStream;

import static java.lang.System.setIn;
import static java.nio.charset.Charset.defaultCharset;

@Guice(modules = {
	CliCommandReaderImplTest.EchoTestModule.class
})
public class CliCommandReaderImplTest {

	@Inject
	private Controllable controllable;

	@Inject
	private CliCommandReader reader;

	private InputStream in;


	@BeforeMethod
	public void setUp() throws Exception {
		Mockito.reset(controllable);
		in = System.in;
	}

	@AfterMethod
	public void tearDown() throws Exception {
		setIn(in);
	}

	@Test(dataProvider = "commands")
	public void testRead(String command) throws Exception {
		try (CharSequenceInputStream stream = new CharSequenceInputStream(command, defaultCharset())) {
			setIn(stream);
		}

		reader.read();
		Mockito.verify(controllable).getClass().getMethod(command).invoke(controllable);
	}

	//TODO add more test cases

	@DataProvider
	public Object[][] commands() {
		return new Object[][]{
			{"start"},
			{"stop"},
			{"exit"}
		};
	}


	public static class EchoTestModule extends AbstractModule {

		@Override
		protected void configure() {
			binder().bind(Controllable.class).toInstance(Mockito.mock(Controllable.class));
		}
	}
}

