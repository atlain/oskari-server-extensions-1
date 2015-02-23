package eu.elf.license;

import eu.elf.license.model.*;
import org.w3c.dom.*;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.Locator2Impl;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Refactored from QueryHandler
 */
public class LicenseParser {

    public static List<LicenseModelGroup> parseListOfLicensesAsLicenseModelGroupList(String xml) throws Exception {

        try {
            Document xmlDoc = createXMLDocumentFromString(xml);
            NodeList productGroupElementList = xmlDoc.getElementsByTagNameNS("http://www.conterra.de/xcpf/1.1", "productGroup");

            return createLicenseModelGroupList(productGroupElementList);

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Creates List of LicenseModelGroup objects
     *
     * @param productGroupElementList - List of <ns:productGroup> elements
     * @return list of LicenseModelGroup objects
     */
    private static List<LicenseModelGroup> createLicenseModelGroupList(NodeList productGroupElementList) {
        List<LicenseModelGroup> lmgList = new ArrayList<LicenseModelGroup>();

        for (int i = 0; i < productGroupElementList.getLength(); i++) {
            LicenseModelGroup tempLMG = new LicenseModelGroup();
            Boolean isAccountGroup = false;

            Element productGroupElement = (Element)productGroupElementList.item(i);

            NamedNodeMap productGroupElementAttributeMap = productGroupElement.getAttributes();

            for (int j = 0; j < productGroupElementAttributeMap.getLength(); j++) {
                Attr attrs = (Attr) productGroupElementAttributeMap.item(j);

                if (attrs.getNodeName().equals("id") ) {
                    if (attrs.getNodeValue().equals("ACCOUNTING_SUMMARY_GROUP")) { // Skip element with id="ACCOUNTING_SUMMARY_GROUP" -  do not add to the list
                        isAccountGroup = true;
                    }

                    if (attrs.getNodeName() != null) {
                        tempLMG.setId(attrs.getNodeValue());
                    }
                }
                if (attrs.getNodeName().equals("name") ) {
                    if (attrs.getNodeName() != null) {
                        tempLMG.setUrl(attrs.getNodeValue());
                    }
                }
            }

            if (isAccountGroup == true) {
                continue; // Skip element with id="ACCOUNTING_SUMMARY_GROUP" -  do not add to the list
            }

            Element abstractElement = (Element)productGroupElement.getElementsByTagNameNS("http://www.conterra.de/xcpf/1.1", "abstract").item(0);
            if (abstractElement != null) {
                tempLMG.setDescription(abstractElement.getTextContent());
            }

            Element titleElement = (Element)productGroupElement.getElementsByTagNameNS("http://www.conterra.de/xcpf/1.1", "title").item(0);
            if (titleElement != null) {
                tempLMG.setName(titleElement.getTextContent());
            }



            NodeList productElementList = productGroupElement.getElementsByTagNameNS("http://www.conterra.de/xcpf/1.1", "product");
            tempLMG.setLicenseModels(createLicenseModelList(productElementList));

            lmgList.add(tempLMG);
        }

        return lmgList;
    }


    /**
     * Creates list of LicenseModel elements
     *
     * @param productElementList - NodeList of <ns:product> elements:
     * @return List of LicenseModel objects
     */
    private static List<LicenseModel> createLicenseModelList(NodeList productElementList) {
        List<LicenseModel> lmList = new ArrayList<LicenseModel>();

        for (int i = 0; i < productElementList.getLength(); i++) {
            LicenseModel tempLM = new LicenseModel();
            Boolean isRestricted = true;

            Element productElement = (Element)productElementList.item(i);

            NamedNodeMap productElementAttributeMap = productElement.getAttributes();

            for (int j = 0; j < productElementAttributeMap.getLength(); j++) {
                Attr attrs = (Attr) productElementAttributeMap.item(j);

                if (attrs.getNodeName().equals("id") ) {
                    if (attrs.getNodeName() != null) {
                        tempLM.setId(attrs.getNodeValue());
                    }
                }

            }

            Element titleElement = (Element)productElement.getElementsByTagNameNS("http://www.conterra.de/xcpf/1.1", "title").item(0);
            if (titleElement != null) {
                tempLM.setName(titleElement.getTextContent());
            }

            Element abstractElement = (Element)productElement.getElementsByTagNameNS("http://www.conterra.de/xcpf/1.1", "abstract").item(0);
            if (abstractElement != null) {
                tempLM.setDescription(abstractElement.getTextContent());
            }

            Element calculationElement = (Element)productElement.getElementsByTagNameNS("http://www.conterra.de/xcpf/1.1", "calculation").item(0);
            Element declarationListElement = (Element)calculationElement.getElementsByTagNameNS("http://www.conterra.de/xcpf/1.1", "declarationList").item(0);
            Element predefinedParametersElement = (Element)declarationListElement.getElementsByTagNameNS("http://www.conterra.de/xcpf/1.1", "predefinedParameters").item(0);

            NodeList predefinedParametersParameterElementList = predefinedParametersElement.getElementsByTagNameNS("http://www.conterra.de/xcpf/1.1", "parameter");

            for (int k = 0; k < predefinedParametersParameterElementList.getLength(); k++) {
                Element parameterElement = (Element)predefinedParametersParameterElementList.item(k);

                NamedNodeMap parameterElementAttributeMap = parameterElement.getAttributes();

                for (int l = 0; l < parameterElementAttributeMap.getLength(); l++) {
                    Attr attrs = (Attr) parameterElementAttributeMap.item(l);

                    if (attrs.getNodeName().equals("name") ) {
                        if (attrs.getNodeName() != null) {
                            if (attrs.getNodeValue().equals("ALL_ROLES")) {
                                Element valueElement = (Element)parameterElement.getElementsByTagNameNS("http://www.conterra.de/xcpf/1.1", "value").item(0);

                                if (valueElement.getTextContent().equals("false")) {
                                    isRestricted = true;
                                }
                            }
                        }
                    }

                    if (attrs.getNodeName().equals("name") ) {
                        if (attrs.getNodeName() != null) {
                            if (attrs.getNodeValue().equals("ALLOWED_ROLES")) {
                                NodeList valueElementList = parameterElement.getElementsByTagNameNS("http://www.conterra.de/xcpf/1.1", "value");

                                for (int m = 0; m < valueElementList.getLength(); m++) {
                                    tempLM.addtRole(valueElementList.item(m).getTextContent());
                                }

                            }
                        }
                    }

                }


            }

            tempLM.setRestricted(isRestricted);

            tempLM.setParams(createLicenseModelParamList(declarationListElement));

            //ArrayList<LicenseParam> list = new ArrayList<LicenseParam>();
            //list = (ArrayList<LicenseParam>) tempLM.getParams();
            //for (int v = 0; v < tempLM.getParams().size(); v++) {
            //	System.out.println("paramlist.length "+list.get(v).getName());
            //}

            lmList.add(tempLM);
        }

        return lmList;
    }
    /**
     * Creates list of license model parameters
     *
     * @param declarationListElement - XML element <x:declarationList>
     * @return List of LicenseParam objects
     */
    private static List<LicenseParam> createLicenseModelParamList(Element declarationListElement) {
        List<LicenseParam> paramList = new ArrayList<LicenseParam>();

        // Predefined Parameters - create as LicenseParamDisplay objects
        Element predefinedParametersElement = (Element)declarationListElement.getElementsByTagNameNS("http://www.conterra.de/xcpf/1.1", "predefinedParameters").item(0);
        NodeList predefinedParametersParameterElementList = predefinedParametersElement.getElementsByTagNameNS("http://www.conterra.de/xcpf/1.1", "parameter");

        for (int n = 0; n < predefinedParametersParameterElementList.getLength(); n++) {
            Element parameterElement = (Element)predefinedParametersParameterElementList.item(n);

            LicenseParamDisplay displayParam = createLicenseParamDisplay(parameterElement, "predefinedParameter");
            paramList.add(displayParam);

        }

        // Configuration Parameters - might be LicenseParamDisplay || LicenseParamBln || LicenseParamEnum || LicenseParamInt || LicenseParamText
        Element configurationParametersElement = (Element)declarationListElement.getElementsByTagNameNS("http://www.conterra.de/xcpf/1.1", "configurationParameters").item(0);
        NodeList configurationParametersParameterElementList = configurationParametersElement.getElementsByTagNameNS("http://www.conterra.de/xcpf/1.1", "parameter");

        for (int n = 0; n < configurationParametersParameterElementList.getLength(); n++) {
            Element parameterElement = (Element)configurationParametersParameterElementList.item(n);

            LicenseParam lp = null;
            Boolean isStringType = false;
            Boolean multiAttributeExists = false;
            Boolean optionalAttributeExists = false;

            NamedNodeMap parameterElementAttributeMap = parameterElement.getAttributes();

            // Create appropriate subclass object based on the "type" and "multi" attributes
            for (int o = 0; o < parameterElementAttributeMap.getLength(); o++) {
                Attr attrs = (Attr) parameterElementAttributeMap.item(o);

                if (attrs.getNodeName().equals("type") ) {
                    if (attrs.getNodeValue().equals("real")) {
                        lp = createLicenseParamInt(parameterElement, "configurationParameter");
                        paramList.add(lp);
                        //System.out.println("lp - name "+lpInt.getName());
                    }
                    else if (attrs.getNodeValue().equals("string")) {
                        isStringType = true;
                    }
                    else if (attrs.getNodeValue().equals("boolean")) {
                        lp = createLicenseParamBln(parameterElement, "configurationParameter");
                        paramList.add(lp);
                    }

                }
                if (attrs.getNodeName().equals("multi")) {
                    multiAttributeExists = true;
                }
                if (attrs.getNodeName().equals("optional")) {
                    optionalAttributeExists = true;
                }

            }

            if (isStringType == true) {
                if (multiAttributeExists == true) {
                    if (optionalAttributeExists == true) {
                        // What does optional attribute signify????
                    }
                    else {
                        lp = createLicenseParamEnum(parameterElement, "configurationParameter");
                        paramList.add(lp);
                    }
                }
                else {
                    lp = createLicenseParamText(parameterElement, "configurationParameter");
                    paramList.add(lp);
                }
            }

        }

        return paramList;
    }

    /**
     * Creates LicenseParamBln object
     *
     * @param parameterElement
     * @param parameterClass - parameter class (predefinedParameter || precalculatedParameter || referencedParameter || resultParameter || configurationParameter)
     * @return
     */
    private static LicenseParamBln createLicenseParamBln(Element parameterElement, String parameterClass) {
        LicenseParamBln lpbln = new LicenseParamBln();
        lpbln.setParameterClass(parameterClass);

        NamedNodeMap parameterElementAttributeMap = parameterElement.getAttributes();

        for (int i = 0; i < parameterElementAttributeMap.getLength(); i++) {
            Attr attrs = (Attr) parameterElementAttributeMap.item(i);

            if (attrs.getNodeName().equals("name") ) {
                lpbln.setName(attrs.getNodeValue());
            }

        }

        Element parameterTitleElement = (Element)parameterElement.getElementsByTagNameNS("http://www.conterra.de/xcpf/1.1", "title").item(0);


        if (parameterTitleElement != null) {
            //System.out.println(parameterTitleElement.getTextContent());
            lpbln.setTitle(parameterTitleElement.getTextContent());
        }

        Element parameterValueElement = (Element)parameterElement.getElementsByTagNameNS("http://www.conterra.de/xcpf/1.1", "value").item(0);

        if (parameterValueElement != null) {
            if (parameterValueElement.getTextContent().equals("true")) {
                lpbln.setValue(true);
            }
            else {
                lpbln.setValue(false);
            }
        }


        return lpbln;
    }


    /**
     * Creates LicenseParamDisplay object
     *
     * @param parameterElement
     * @param parameterClass - parameter class (predefinedParameter || precalculatedParameter || referencedParameter || resultParameter || configurationParameter)
     * @return
     */
    private static LicenseParamDisplay createLicenseParamDisplay(Element parameterElement, String parameterClass) {
        LicenseParamDisplay lpd = new LicenseParamDisplay();
        lpd.setParameterClass(parameterClass);

        NamedNodeMap parameterElementAttributeMap = parameterElement.getAttributes();

        for (int i = 0; i < parameterElementAttributeMap.getLength(); i++) {
            Attr attrs = (Attr) parameterElementAttributeMap.item(i);

            if (attrs.getNodeName().equals("name") ) {
                lpd.setName(attrs.getNodeValue());
            }

        }

        Element parameterTitleElement = (Element)parameterElement.getElementsByTagNameNS("http://www.conterra.de/xcpf/1.1", "title").item(0);

        if (parameterTitleElement != null) {
            lpd.setTitle(parameterTitleElement.getTextContent());
        }

        NodeList valueElementList = parameterElement.getElementsByTagNameNS("http://www.conterra.de/xcpf/1.1", "value");

        for (int j = 0; j < valueElementList.getLength(); j++) {
            Element valueElement = (Element)valueElementList.item(j);

            lpd.addValue(valueElement.getTextContent());

        }

        return lpd;
    }


    /**
     * Creates LicenseParamEnum object
     *
     * @param parameterElement
     * @param parameterClass - parameter class (predefinedParameter || precalculatedParameter || referencedParameter || resultParameter || configurationParameter)
     * @return
     */
    private static LicenseParamEnum createLicenseParamEnum(Element parameterElement, String parameterClass) {
        Boolean multiAttribute = false;
        LicenseParamEnum lpEnum = new LicenseParamEnum();
        lpEnum.setParameterClass(parameterClass);

        NamedNodeMap parameterElementAttributeMap = parameterElement.getAttributes();

        for (int i = 0; i < parameterElementAttributeMap.getLength(); i++) {
            Attr attrs = (Attr) parameterElementAttributeMap.item(i);

            if (attrs.getNodeName().equals("name") ) {
                lpEnum.setName(attrs.getNodeValue());
            }

            if (attrs.getNodeName().equals("multi")) {
                if (attrs.getNodeValue().equals(true)) {
                    multiAttribute = true;
                }
                else {
                    multiAttribute = false;
                }
            }

        }

        lpEnum.setMulti(multiAttribute);

        Element parameterTitleElement = (Element)parameterElement.getElementsByTagNameNS("http://www.conterra.de/xcpf/1.1", "title").item(0);

        if (parameterTitleElement != null) {
            lpEnum.setTitle(parameterTitleElement.getTextContent());
        }

        NodeList valueElementList = parameterElement.getElementsByTagNameNS("http://www.conterra.de/xcpf/1.1", "value");

        for (int j = 0; j < valueElementList.getLength(); j++) {
            Element valueElement = (Element)valueElementList.item(j);

            NamedNodeMap valueElementAttributeMap = valueElement.getAttributes();

            for (int k = 0; k < valueElementAttributeMap.getLength(); k++) {
                Attr attrs = (Attr) valueElementAttributeMap.item(k);

                if (attrs.getNodeName().equals("selected") ) {
                    if (attrs.getNodeValue().equals("true")) {
                        lpEnum.setDefaultValue(valueElement.getTextContent());
                    }
                }

            }

            lpEnum.addOption(valueElement.getTextContent());
        }

        return lpEnum;
    }


    /**
     * Creates LicenseParamInt object
     *
     * @param parameterElement
     * @param parameterClass - parameter class (predefinedParameter || precalculatedParameter || referencedParameter || resultParameter || configurationParameter)
     * @return
     */
    private static LicenseParamInt createLicenseParamInt(Element parameterElement, String parameterClass) {
        LicenseParamInt lpInt = new LicenseParamInt();
        lpInt.setParameterClass(parameterClass);

        NamedNodeMap parameterElementAttributeMap = parameterElement.getAttributes();

        for (int i = 0; i < parameterElementAttributeMap.getLength(); i++) {
            Attr attrs = (Attr) parameterElementAttributeMap.item(i);

            if (attrs.getNodeName().equals("name") ) {
                lpInt.setName(attrs.getNodeValue());
            }

        }

        Element parameterTitleElement = (Element)parameterElement.getElementsByTagNameNS("http://www.conterra.de/xcpf/1.1", "title").item(0);

        if (parameterTitleElement != null) {
            lpInt.setTitle(parameterTitleElement.getTextContent());
        }

        Element valueElement = (Element)parameterElement.getElementsByTagNameNS("http://www.conterra.de/xcpf/1.1", "value").item(0);

        if (valueElement != null) {
            lpInt.setValue(Integer.parseInt(valueElement.getTextContent()));
        }


        return lpInt;
    }


    /**
     * Creates LicenseParamText object
     *
     * @param parameterElement
     * @param parameterClass - parameter class (predefinedParameter || precalculatedParameter || referencedParameter || resultParameter || configurationParameter)
     * @return
     */
    private static LicenseParamText createLicenseParamText(Element parameterElement, String parameterClass) {
        LicenseParamText lpt = new LicenseParamText();
        lpt.setParameterClass(parameterClass);

        NamedNodeMap parameterElementAttributeMap = parameterElement.getAttributes();

        for (int i = 0; i < parameterElementAttributeMap.getLength(); i++) {
            Attr attrs = (Attr) parameterElementAttributeMap.item(i);

            if (attrs.getNodeName().equals("name") ) {
                lpt.setName(attrs.getNodeValue());
            }

        }

        Element parameterTitleElement = (Element)parameterElement.getElementsByTagNameNS("http://www.conterra.de/xcpf/1.1", "title").item(0);

        if (parameterTitleElement != null) {
            lpt.setTitle(parameterTitleElement.getTextContent());
        }

        NodeList valueElementList = parameterElement.getElementsByTagNameNS("http://www.conterra.de/xcpf/1.1", "value");


        for (int j = 0; j < valueElementList.getLength(); j++) {
            Element valueElement = (Element)valueElementList.item(j);

            lpt.addValue(valueElement.getTextContent());

        }

        return lpt;
    }




    /**
     * Creates DOM Document object from XML string
     *
     * @param xmlString
     * @return xml document
     * @throws Exception
     */
    public static Document createXMLDocumentFromString(String xmlString) throws Exception {
        Document document = null;

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);

            DocumentBuilder builder = dbf.newDocumentBuilder();
            InputStream is  = new ByteArrayInputStream(xmlString.getBytes("UTF-8"));
            document = builder.parse(is);

        } catch(SAXParseException spe) {
            Locator2Impl locator = new Locator2Impl();
            throw new SAXParseException("", locator);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return document;
    }
}
