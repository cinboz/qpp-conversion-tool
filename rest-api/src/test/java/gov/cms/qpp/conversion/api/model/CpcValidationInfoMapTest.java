package gov.cms.qpp.conversion.api.model;

import static com.google.common.truth.Truth.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.amazonaws.util.StringInputStream;

import gov.cms.qpp.test.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CpcValidationInfoMapTest {

	@Mock
	private InputStream mockIns;


	@Test
	void test_loadJsonStream() throws Exception {
		String json = "[" +
			    "   {\r\n" + 
				"		\"apm_entity_id\": \"T1AR0503\",\r\n" + 
				"		\"tin\": \"000333333\",\r\n" + 
				"		\"npi\": \"0333333333\"\r\n" + 
				"	},\r\n" + 
				"	{\r\n" + 
				"		\"apm_entity_id\": \"T1AR0518\",\r\n" + 
				"		\"tin\": \"000444444\",\r\n" + 
				"		\"npi\": \"0444444444\"\r\n" + 
				"	}\r\n" + 
				"]\r\n";
		InputStream jsonStream = new StringInputStream(json);
		
		CpcValidationInfoMap cpc = new CpcValidationInfoMap(jsonStream);
		Map<?,?> map = cpc.getNpiToApmMap();
		
		assertThat(map).isNotNull();
		assertThat(map.size()).isEqualTo(2);
		assertThat(map.get("T1AR0503")).isEqualTo("0333333333");
		assertThat(map.get("T1AR0518")).isEqualTo("0444444444");
	}

	@Test
	void test_loadNullStream() throws Exception {
		
		CpcValidationInfoMap cpc = new CpcValidationInfoMap(null);
		Map<?,?> map = cpc.getNpiToApmMap();
		
		assertThat(map).isNull();
	}


	@Test
	void test_loadNullStream_throwsIOE() throws Exception {
		Mockito.when(mockIns.read()).thenThrow(new IOException());
		
		CpcValidationInfoMap cpc = new CpcValidationInfoMap(mockIns);
		Map<?,?> map = cpc.getNpiToApmMap();
		
		assertThat(map).isNotNull();
		assertThat(map.size()).isEqualTo(0);
	}
}
