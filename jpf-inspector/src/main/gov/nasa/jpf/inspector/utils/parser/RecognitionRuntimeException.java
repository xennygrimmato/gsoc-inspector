//
// Copyright (C) 2010 United States Government as represented by the
// Administrator of the National Aeronautics and Space Administration
// (NASA).  All Rights Reserved.
// 
// This software is distributed under the NASA Open Source Agreement
// (NOSA), version 1.3.  The NOSA has been approved by the Open Source
// Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
// directory tree for the complete NOSA document.
// 
// THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
// KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
// LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
// SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
// A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
// THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
// DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
//  

package gov.nasa.jpf.inspector.utils.parser;

import org.antlr.runtime.RecognitionException;

/**
 * Internal class. Is used to traverse errors between parser and parser facade.
 * The ANTLR-generated parsers throw a RecognitionException when they encoutner a parse error
 * and this class merely wraps that exception as a RuntimeException (the original exception is not unchecked).
 */
public class RecognitionRuntimeException extends RuntimeException {
  private static final long serialVersionUID = 20100602L;

  private final RecognitionException recognitionException;

  public RecognitionRuntimeException (String msg, RecognitionException recognitionException) {
    super(msg);
    this.recognitionException = recognitionException;
  }

  public RecognitionException getRecognitionException () {
    return recognitionException;
  }

}