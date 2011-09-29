/*
 * This file is part of the Echo Web Application Framework (hereinafter "Echo").
 * Copyright (C) 2002-2009 NextApp, Inc.
 * 
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 * 
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * 
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or the
 * GNU Lesser General Public License Version 2.1 or later (the "LGPL"), in which
 * case the provisions of the GPL or the LGPL are applicable instead of those
 * above. If you wish to allow use of your version of this file only under the
 * terms of either the GPL or the LGPL, and not to allow others to use your
 * version of this file under the terms of the MPL, indicate your decision by
 * deleting the provisions above and replace them with the notice and other
 * provisions required by the GPL or the LGPL. If you do not delete the
 * provisions above, a recipient may use your version of this file under the
 * terms of any one of the MPL, the GPL or the LGPL.
 */

package nextapp.echo.webcontainer.util;

import java.io.StringReader;
import java.io.StringWriter;

import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

/** Compresses a String using the Yahoo JS Compressor and Mozilla Rhino. */
public class JavaScriptCompressor {

    /**
     * Compresses a String containing JavaScript by removing comments and whitespace.
     *
     * @param script the String to compress
     * @return a compressed version
     */
    public static String compress(String script) {
        try {
            StringReader scriptReader = new StringReader(script);
            CountingErrorReporter errorReporter = new CountingErrorReporter();
            com.yahoo.platform.yui.compressor.JavaScriptCompressor jsc =
                    new com.yahoo.platform.yui.compressor.JavaScriptCompressor(scriptReader, errorReporter);
            StringWriter scriptWriter = new StringWriter();
            jsc.compress(scriptWriter, -1, true, false, false, false);
            if (errorReporter.isClean()) {
                return scriptWriter.getBuffer().toString();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        // Fallback in case of problems
        return script;
    }

    private static class CountingErrorReporter implements ErrorReporter {

        int problems = 0;

        public void warning(String message, String sourceName,
                            int line, String lineSource, int lineOffset) {
            if (line < 0) {
                System.err.println("\n[WARNING] " + message);
            } else {
                System.err.println("\n[WARNING] " + line + ':' + lineOffset + ':' + message);
            }
            System.err.println("[ERROR] source code line: "+lineSource);
            problems++;
        }

        public void error(String message, String sourceName,
                          int line, String lineSource, int lineOffset) {
            if (line < 0) {
                System.err.println("\n[ERROR] " + message);
            } else {
                System.err.println("\n[ERROR] " + line + ':' + lineOffset + ':' + message);
            }
            System.err.println("[ERROR] source code line: "+lineSource);
            problems++;
        }

        public EvaluatorException runtimeError(String message, String sourceName,
                                               int line, String lineSource, int lineOffset) {
            error(message, sourceName, line, lineSource, lineOffset);
            return new EvaluatorException(message);
        }

        public boolean isClean() {
            return problems == 0;
        }
    }
}
