package gov.cms.qpp.acceptance;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.BufferedWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;
import org.junit.Test;

import gov.cms.qpp.conversion.encoder.QppOutputEncoder;
import gov.cms.qpp.conversion.model.Node;
import gov.cms.qpp.conversion.parser.QppXmlInputParser;
import gov.cms.qpp.conversion.xml.XmlUtils;

public class AciNumeratorDenominatorRoundTripTest {

	// we currently have a root placeholder node, so the numerator/denominator
	// is indented an extra level
	private static final String EXPECTED = "{\n\t\t600\n}";

	@Test
	public void parseAciNumeratorDenominatorAsNode() throws Exception {
		String xmlFragment = XmlUtils.buildString("<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">",
				"  <observation classCode=\"OBS\" moodCode=\"EVN\">",
				"    <templateId root=\"2.16.840.1.113883.10.20.27.3.3\"/>",
				"    <code code=\"MSRAGG\" codeSystem=\"2.16.840.1.113883.5.4\" codeSystemName=\"ActCode\" displayName=\"rate aggregation\"/>",
				"    <statusCode code=\"completed\"/>", "    <value xsi:type=\"INT\" value=\"600\"/>",
				"    <methodCode code=\"COUNT\" codeSystem=\"2.16.840.1.113883.5.84\" codeSystemName=\"ObservationMethod\" displayName=\"Count\"/>",
				"  </observation>", "</root>");

		Element dom = XmlUtils.stringToDOM(xmlFragment);

		QppXmlInputParser parser = new QppXmlInputParser();
		parser.setDom(dom);

		Node numDenomNode = parser.parse();

		QppOutputEncoder encoder = new QppOutputEncoder();
		List<Node> nodes = new ArrayList<>();
		nodes.add(numDenomNode);
		encoder.setNodes(nodes);

		StringWriter sw = new StringWriter();
		encoder.encode(new BufferedWriter(sw));

		assertThat("expected encoder to return a single number numerator/denominator", sw.toString(), is(EXPECTED));

	}

}
