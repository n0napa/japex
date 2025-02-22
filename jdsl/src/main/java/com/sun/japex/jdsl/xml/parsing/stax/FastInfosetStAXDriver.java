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

package com.sun.japex.jdsl.xml.parsing.stax;

import javax.xml.stream.XMLStreamReader;

import com.sun.japex.TestCase;
import com.sun.japex.jdsl.xml.BaseParserDriver;
import com.sun.japex.jdsl.xml.DriverConstants;
import com.sun.japex.jdsl.xml.FastInfosetParserDriver;
import com.sun.xml.fastinfoset.stax.StAXDocumentParser;
import org.jvnet.fastinfoset.FastInfosetParser;


public class FastInfosetStAXDriver extends BaseParserDriver implements FastInfosetParserDriver {
    
    StAXDocumentParser _staxParser = null;
    
    public void initializeDriver() {
        _staxParser = new StAXDocumentParser();
        _staxParser.setStringInterning(getBooleanParam(DriverConstants.STRING_INTERNING_PROPERTY));
    }
    
    public FastInfosetParser getParser() {
        return _staxParser;
    }
    
    @SuppressWarnings("unused")
	public void run(TestCase testCase) {
        try {
            _inputStream.reset();
            _staxParser.setInputStream(_inputStream);       // Instead of creating new instance
            
            char[] text;
            int acount, ncount;
            String local, prefix, uri;
            
            while (_staxParser.hasNext()) {
                int event = _staxParser.next();
                _staxParser.getEventType();
                switch (event) {
                    case XMLStreamReader.START_ELEMENT:
                        local = _staxParser.getLocalName();
                        prefix = _staxParser.getPrefix();
                        uri = _staxParser.getNamespaceURI();
                        acount = _staxParser.getAttributeCount();
                        ncount = _staxParser.getNamespaceCount();
                        break;
                    case XMLStreamReader.END_ELEMENT:
                        local = _staxParser.getLocalName();
                        prefix = _staxParser.getPrefix();
                        uri = _staxParser.getNamespaceURI();
                        break;
                    case XMLStreamReader.CHARACTERS:
                    case XMLStreamReader.SPACE:
                        text = _staxParser.getTextCharacters();
                        break;
                    default:
                        break;
                }
            }            
            _staxParser.close();
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
