/*
 * Copyright, 2004-2007 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Software is licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may
 * obtain a copy of the License at:
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations.
 *
 *    Sun supports and benefits from the global community of open source
 * developers, and thanks the community for its important contributions and
 * open standards-based technology, which Sun has adopted into many of its
 * products.
 *
 *    Please note that portions of Software may be provided with notices and
 * open source licenses from such communities and third parties that govern the
 * use of those portions, and any licenses granted hereunder do not alter any
 * rights and obligations you may have under such open source licenses,
 * however, the disclaimer of warranty and limitation of liability provisions
 * in this License will apply to all Software in this distribution.
 *
 *    You acknowledge that the Software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of any nuclear
 * facility.
 *
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 */

package com.sun.japex.jdsl.xml.roundtrip.dom;

import java.io.ByteArrayOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import com.sun.japex.TestCase;
import com.sun.japex.jdsl.xml.BaseParserDriver;
import com.sun.japex.jdsl.xml.DriverConstants;


public abstract class BaseJAXPDOMDriver extends BaseParserDriver {
    protected ByteArrayOutputStream _outputStream; 
    protected DocumentBuilder _builder;
    protected Document _d;

    public void initializeDriver() {
        try {
            super.initializeDriver();       
            
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            builderFactory.setNamespaceAware(true);
            builderFactory.setAttribute("http://apache.org/xml/features/dom/defer-node-expansion", 
                    getBooleanParam(DriverConstants.DEFER_NODE_EXPANSION));
            _builder = builderFactory.newDocumentBuilder();
        } 
        catch (Exception e) {
            e.printStackTrace();
        }        
    }
    
    public void prepare(TestCase testCase) {
        super.prepare(testCase);        // Initializes _inputStream        
        _outputStream = new ByteArrayOutputStream();
    }    
}
