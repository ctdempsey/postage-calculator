package au.edu.unimelb.comp30022.controllertesting;

import static org.mockito.Mockito.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import android.location.Location;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

/**
 * Created by Cameron on 31-Aug-17.
 */
@RunWith(MockitoJUnitRunner.class)
public class ControllerTest {

    EditText sourcePostCode;
    EditText destPostCode;
    TextView costLabel;
    PostcodeValidator postCodeValidator;
    PostageRateCalculator postageRateCalculator;
    AddressTools addressTools;
    Controller controller;

    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();


    @Before
    public void setUp() throws Exception {
        System.setErr(new PrintStream(errContent));

        UI.addWidget("CALCULATE_BUTTON", new Button());


        sourcePostCode = mock(EditText.class);
        sourcePostCode = new EditText();
        sourcePostCode.setText("3050");
        UI.addWidget("SOURCE_POST_CODE",sourcePostCode);

        destPostCode = mock(EditText.class);
        destPostCode = new EditText();
        destPostCode.setText("3050");
        UI.addWidget("DESTINATION_POST_CODE",destPostCode);

        costLabel = mock(TextView.class);
        costLabel = new TextView();
        UI.addWidget("COST_LABEL",costLabel);

        postCodeValidator = mock(PostcodeValidator.class);
        when(postCodeValidator.isValid(any(String.class))).thenReturn(true);
        when(postCodeValidator.isValid("1")).thenReturn(false);

        postageRateCalculator = mock(PostageRateCalculator.class);
        when(postageRateCalculator.computeCost(any(Location.class), any(Location.class))).thenReturn(5);

        addressTools = mock(AddressTools.class);


        controller = mock(Controller.class);
        controller = new Controller(addressTools, postCodeValidator, postageRateCalculator);

    }

    @After
    public void tearDown() throws Exception {
        System.setErr(null);
    }

    @Test
    public void testButtonPress() {

        sourcePostCode.setText("3050");
        destPostCode.setText("3050");
        controller.calculateButtonPressed();
        assertEquals("", errContent.toString());

        sourcePostCode.setText("1");
        controller.calculateButtonPressed();
        assertEquals("Source post code isn't valid.\r\n", errContent.toString());

        sourcePostCode.setText("3050");
        destPostCode.setText("1");
        controller.calculateButtonPressed();
        assertEquals("Dest post code isn't valid.\r\n", errContent.toString());

        assertEquals("$5", costLabel.getText());

    }

}