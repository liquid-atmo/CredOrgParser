package application;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import application.config.SOAPConnector;
import application.entity.Form101;
import application.entity.Organisation;
import application.service.Form101Service;
import application.service.OrganisationService;
import hello.wsdl.Data101FullResponse;
import hello.wsdl.EnumBICXMLResponse;

 
@SpringBootApplication
public class SpringBootSoapClientApplication {
	
	@Autowired
    private OrganisationService organisationService;
	
	@Autowired
    private Form101Service form101Service;
	
	// количество организаций, по которым надо парсить формы-101 задаётся в application.properties
	@Value("${my.numberOfOrganisations}")
	int numberOfOrganisations;
			
	// по каким счетам, валюте и проч. парсим выписки
	@Value("${my.indId}")
	int indId;
		 	
	// дата с: год
	@Value("${my.year.from}")
	int yearFrom;	
	// дата с: месяц
	@Value("${my.month.from}")
	int monthFrom;
	// дата с: год
	@Value("${my.day.from}")
	int dayFrom;
	// дата по: год
	@Value("${my.year.to}")
	int yearTo;
	// дата по: месяц
	@Value("${my.month.to}")
	int monthTo;	
	// дата с: год
	@Value("${my.day.to}")
	int dayTo;

	
    public static void main(String[] args) {
        SpringApplication.run(SpringBootSoapClientApplication.class, args).close();
        System.out.println("Done!");
            }
    
    @Bean
    CommandLineRunner lookup(SOAPConnector soapConnector) {
        return args -> { 
            if(args.length>0){
              System.out.println("No arguments needed");
            }
                  
           // запрашиваем все БИКи кредитных организаций
           EnumBICXMLResponse response = soapConnector.getEnumBICResult();
           
           List<Object> list = response.getEnumBICXMLResult().getContent();
           // получаем Document из List<Object>
           // есть ли иной способ получить Document, покрасивее ??
           Document doc = ((Element)list.get(0)).getOwnerDocument(); 
          // сохраняем в файл 
           writeToFile(transformToString(doc), "response.txt");
           
           // парсить будем при помощи xPath
           XPath xPath =  XPathFactory.newInstance().newXPath();
              
           // парсим БИКи
           NodeList nodeListBICs = extractNodeList(doc, xPath, "//BIC/BIC");
          
           // парсим названия
           NodeList nodeListNAMEs = extractNodeList(doc, xPath, "//BIC/NM");
           
           // парсим внутренний номер. Нужен, чтобы по нему парсить выписки формы 101
           NodeList nodeListRNs = extractNodeList(doc, xPath, "//BIC/RN");  
           
           // сохраняем в бд БИКи, названия, внутренние номера RN для каждой организации
           for (int i = 0; i < nodeListBICs.getLength(); i++) {
        	   
        	   Organisation tempOrganisation = new Organisation();
        	   
        	   Node tempNode = nodeListBICs.item(i);
        	   tempOrganisation.setBic(tempNode.getTextContent());
        	   
        	   tempNode = nodeListNAMEs.item(i);
        	   tempOrganisation.setName(tempNode.getTextContent());
        	   
        	   tempNode = nodeListRNs.item(i);
        	   tempOrganisation.setRn(tempNode.getTextContent());
        	   
        	   organisationService.save(tempOrganisation);
        	  
        	}
        	
            // задаём, для скольких организаций парсить  формы-101. Задаётся в application.properties
           
           XMLGregorianCalendar dateFrom = getDateFrom();
           XMLGregorianCalendar dateTo = getDateTo();
         
           for (int i = 1; i <= numberOfOrganisations; i++) {
               Organisation tempOrganisation = organisationService.findById(i);
           
               int internalNumber = Integer.valueOf(tempOrganisation.getRn());
           
               Data101FullResponse response1 = soapConnector.getData101FullResponse(
        		   internalNumber, indId, dateFrom, dateTo);
          
               Object content = response1.getData101FullResult().getAny();
               doc = ((Element)content).getOwnerDocument(); 
  
           
           // вытаскиваем из ДОМа нужные нам поля при помощи XPath
           NodeList nodeListDt = extractNodeList(doc, xPath, "//FDF/dt");
           NodeList nodeListPln = extractNodeList(doc, xPath, "//FDF/pln");
           NodeList nodeListAp = extractNodeList(doc, xPath, "//FDF/ap");
           NodeList nodeListVr = extractNodeList(doc, xPath, "//FDF/vr");
           NodeList nodeListVv = extractNodeList(doc, xPath, "//FDF/vv");
           NodeList nodeListVitg = extractNodeList(doc, xPath, "//FDF/vitg");
           NodeList nodeListOra = extractNodeList(doc, xPath, "//FDF/ora");
           NodeList nodeListOva = extractNodeList(doc, xPath, "//FDF/ova");
           NodeList nodeListOitga = extractNodeList(doc, xPath, "//FDF/oitga");
           NodeList nodeListOrp = extractNodeList(doc, xPath, "//FDF/orp");
           NodeList nodeListOvp = extractNodeList(doc, xPath, "//FDF/ovp");
           NodeList nodeListOitgp = extractNodeList(doc, xPath, "//FDF/oitgp");
           NodeList nodeListIr = extractNodeList(doc, xPath, "//FDF/ir");
           NodeList nodeListIv = extractNodeList(doc, xPath, "//FDF/iv");
           NodeList nodeListIitg = extractNodeList(doc, xPath, "//FDF/iitg");
        
           // сохраняем их в бд
           
           SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+HH:mm");

           for (int j = 0; j < nodeListIitg.getLength(); j++) {
        	   Form101 tempForm101 = new Form101();
        	   tempForm101.setOrganisation(tempOrganisation);
        	   
        	   Node tempNode = nodeListDt.item(j);
        	   Date date = convertStringToDate(format, tempNode);
        	   tempForm101.setFormDate(date);
        	  
        	   tempNode = nodeListPln.item(j);
        	   tempForm101.setPln(tempNode.getTextContent());
        	   
        	   tempNode = nodeListAp.item(j);
        	   tempForm101.setAp(tempNode.getTextContent());
        	   
        	   tempNode = nodeListVr.item(j);
        	   tempForm101.setVr(tempNode.getTextContent());
        	   
        	   tempNode = nodeListVv.item(j);
        	   tempForm101.setVv(tempNode.getTextContent());
        	   
        	   tempNode = nodeListVitg.item(j);
        	   tempForm101.setVitg(tempNode.getTextContent());
        	   
        	   tempNode = nodeListOra.item(j);
        	   tempForm101.setOra(tempNode.getTextContent());
        	   
        	   tempNode = nodeListOva.item(j);
        	   tempForm101.setOva(tempNode.getTextContent());
        	   
        	   tempNode = nodeListOitga.item(j);
        	   tempForm101.setOitga(tempNode.getTextContent());
        	   
        	   tempNode = nodeListOrp.item(j);
        	   tempForm101.setOrp(tempNode.getTextContent());
        	   
        	   tempNode = nodeListOvp.item(j);
        	   tempForm101.setOvp(tempNode.getTextContent());
        	   
        	   tempNode = nodeListOitgp.item(j);
        	   tempForm101.setOitgp(tempNode.getTextContent());
        	   
        	   tempNode = nodeListIr.item(j);
        	   tempForm101.setIr(tempNode.getTextContent());
        	   
        	   tempNode = nodeListIv.item(j);
        	   tempForm101.setIv(tempNode.getTextContent());
        	   
        	   tempNode = nodeListIitg.item(j);
        	   tempForm101.setIitg(tempNode.getTextContent());
        	   
        	   form101Service.save(tempForm101);
        	}
           
           int result = form101Service.findByOrganisationId(i).size();
           System.out.println("Credit Organisation " + tempOrganisation.getName() + 
        		   " has "  + result + " Date101Forms");
           }
             
        };
        
    }

	private NodeList extractNodeList(Document doc, XPath xPath, String expression) throws XPathExpressionException {	        
           NodeList result = (NodeList) xPath.compile(expression).evaluate(
              doc, XPathConstants.NODESET);
		return result;
	}

	private XMLGregorianCalendar getDateTo() throws DatatypeConfigurationException {
		XMLGregorianCalendar dateTo = DatatypeFactory.newInstance().newXMLGregorianCalendar
				(new GregorianCalendar(yearTo, monthTo, dayTo));
		return dateTo;
	}

	private XMLGregorianCalendar getDateFrom() throws DatatypeConfigurationException {
		XMLGregorianCalendar dateFrom = DatatypeFactory
			   .newInstance().newXMLGregorianCalendar(new GregorianCalendar
					   (yearFrom, monthFrom, dayFrom));
		return dateFrom;
	}

	private java.util.Date convertStringToDate(SimpleDateFormat format, Node nNode) {
		
		    java.util.Date utilDate = null;
			try {
				utilDate = format.parse(nNode.getTextContent());
			} catch (DOMException e) {
				
				e.printStackTrace();
			} catch (ParseException e) {
			
				e.printStackTrace();
			}
		
		return utilDate;	
	}


	private StringWriter transformToString(Document doc) throws TransformerFactoryConfigurationError {
		try {
		       StringWriter sw = new StringWriter();
		       TransformerFactory tf = TransformerFactory.newInstance();
		       Transformer transformer = tf.newTransformer();
		       transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		       transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		       transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		       transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

		       transformer.transform(new DOMSource(doc), new StreamResult(sw));      
		       return sw;
		      
		   } catch (Exception ex) {
		       throw new RuntimeException("Error converting to String", ex);
		   }
	}

	private void writeToFile(StringWriter sw, String path) throws IOException {
		// write the result to a file
		   String content = sw.toString();
		   Path path_ = Paths.get(path);
		
		try (
		    final BufferedWriter writer = Files.newBufferedWriter(path_,
		        StandardCharsets.UTF_8, StandardOpenOption.CREATE);
		) {
		    writer.write(content);
		    writer.flush();
		}
	}
    
}
