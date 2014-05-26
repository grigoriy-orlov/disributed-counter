package ru.ares4322.distributedcounter.common.cli;

import com.google.inject.AbstractModule;
import org.apache.commons.io.input.CharSequenceInputStream;
import org.testng.annotations.*;

import javax.inject.Inject;
import java.io.InputStream;
import java.lang.reflect.Field;

import static java.lang.System.setIn;
import static java.nio.charset.Charset.defaultCharset;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

@Guice(modules = {
	CliCommandReaderImplTest.EchoTestModule.class
})
public class CliCommandReaderImplTest{

	@Inject
	private Controllable controllable;

	@Inject
	private CliCommandReader reader;

	private InputStream in;

	@BeforeMethod
	public void setUp() throws Exception {
		reset(controllable);
		in = System.in;
	}

	@AfterMethod
	public void tearDown() throws Exception {
		setIn(in);
	}

	//FIXME reflection is evil
	@Test(dataProvider = "commands")
	public void testRead(String lastCommand, String newCommand, boolean isInvoke, boolean isExit) throws Exception {
		Field field = CliCommandReaderImpl.class.getDeclaredField("lastCommand");
		field.setAccessible(true);
		field.set(reader, lastCommand);
		field.setAccessible(false);

		try (CharSequenceInputStream stream = new CharSequenceInputStream(newCommand + "\n", defaultCharset())) {
			setIn(stream);
		}
		reader.readCommand();

		if (isInvoke) {
			verify(controllable).getClass().getMethod(newCommand).invoke(controllable);
		} else {
			verify(controllable, never()).getClass().getMethod(newCommand).invoke(controllable);
		}
		assertEquals(reader.isExit(), isExit);
	}

	//TODO add more test cases

	@DataProvider
	public Object[][] commands() {
		return new Object[][]{
			{"new", "start", true, false},
			{"new", "stop", false, false},
			{"new", "exit", true, true},
			{"start", "start", false, false},
			{"start", "stop", true, false},
			{"start", "exit", true, true},
			{"stop", "start", true, false},
			{"stop", "stop", false, false},
			{"stop", "exit", true, true},
			{"exit", "start", false, true},
			{"exit", "stop", false, true},
			{"exit", "exit", false, true},
		};
	}


	public static class EchoTestModule extends AbstractModule {

		@Override
		protected void configure() {
			binder().bind(Controllable.class).toInstance(mock(Controllable.class));
		}
	}
}

