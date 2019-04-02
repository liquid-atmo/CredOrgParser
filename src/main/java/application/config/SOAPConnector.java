package application.config;
import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import hello.wsdl.BicToRegNumber;
import hello.wsdl.BicToRegNumberResponse;
import hello.wsdl.Data101Full;
import hello.wsdl.Data101FullResponse;
import hello.wsdl.EnumBICXML;
import hello.wsdl.EnumBICXMLResponse;
import hello.wsdl.GetDatesForF101;
import hello.wsdl.GetDatesForF101Response;

public class SOAPConnector extends WebServiceGatewaySupport {
 
	
	public EnumBICXMLResponse getEnumBICResult() {
       
		EnumBICXML request = new EnumBICXML();

		EnumBICXMLResponse response = (EnumBICXMLResponse)
				getWebServiceTemplate()
				.marshalSendAndReceive("http://www.cbr.ru/CreditInfoWebServ/CreditOrgInfo.asmx", request,
						new SoapActionCallback(
								"http://web.cbr.ru/EnumBIC_XML"));

		return response;
	}

	
	public Data101FullResponse getData101FullResponse(int credOrgNumber, int indId,
			XMLGregorianCalendar dateFrom, XMLGregorianCalendar dateTo) {
		
		Data101Full request = new Data101Full();
		request.setCredorgNumber(credOrgNumber);
		request.setIndCode(indId);
		request.setDateFrom(dateFrom);
		request.setDateTo(dateTo);
		Data101FullResponse response = (Data101FullResponse)
				getWebServiceTemplate()
				.marshalSendAndReceive("http://www.cbr.ru/CreditInfoWebServ/CreditOrgInfo.asmx", request,
						new SoapActionCallback(
								"http://web.cbr.ru/Data101Full"));
	
		return response;
	
		
	}

}