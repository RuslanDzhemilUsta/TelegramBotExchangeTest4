package osman.app.telegrambottest4.servise.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import osman.app.telegrambottest4.client.CbrClient;
import osman.app.telegrambottest4.exception.ServiceException;
import osman.app.telegrambottest4.servise.ExchangeRatesServise;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

@Service
public class ExchangeRatesServiceImpl implements ExchangeRatesServise {

    private static final String USD_XPATH = "//Valute[CharCode='USD']/Value";
    private static final String EUR_XPATH = "//Valute[CharCode='EUR']/Value";
    private static final String LIRA_XPATH = "//Valute[CharCode='TRY']/Value";
    private static final String UAH_XPATH = "//Valute[CharCode='UAH']/Value";
    private static final String UZS_XPATH = "//Valute[CharCode='UZS']/Value";

    @Autowired
    private CbrClient client;


    @Override
    public String getUSDExchangeRate() throws ServiceException {
        var xml = client.getCurrencyRatesXML();
        return extractCurrencyValueFromXML(xml, USD_XPATH);

    }

    @Override
    public String getEURExchangeRate() throws ServiceException {
        var xml = client.getCurrencyRatesXML();
        return extractCurrencyValueFromXML(xml, EUR_XPATH);
    }

    @Override
    public String getLIRAExchangeRate() throws ServiceException {
        var xml = client.getCurrencyRatesXML();
        return extractCurrencyValueFromXML(xml, LIRA_XPATH);
    }

    @Override
    public String getUAHExchangeRate() throws ServiceException {
        var xml = client.getCurrencyRatesXML();
        return extractCurrencyValueFromXML(xml, UAH_XPATH);
    }

    @Override
    public String getUZSExchangeRate() throws ServiceException {
        var xml = client.getCurrencyRatesXML();
        return extractCurrencyValueFromXML(xml, UZS_XPATH);
    }

    private static String extractCurrencyValueFromXML(String xml, String xpathExpression) throws ServiceException {
        try {
            InputSource source = new InputSource(new StringReader(xml));
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(source);

            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();
            return xpath.evaluate(xpathExpression, document);
        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e) {
            throw new ServiceException("Ошибка при извлечении значения из XML", e);
        }
    }

}
