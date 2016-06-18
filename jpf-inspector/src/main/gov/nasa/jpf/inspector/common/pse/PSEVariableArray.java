//
// Copyright (C) 2011 United States Government as represented by the
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

package gov.nasa.jpf.inspector.common.pse;

import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;

/**
 * @author Alf
 * 
 */
public class PSEVariableArray extends PSEVariable {

  private static final long serialVersionUID = -7445210497451473309L;

  /**
   * Length of the represented array
   */
  private final int length;
  /**
   * Content of the array
   */
  private PSEVariable[] refArrayItems;

  /**
   * Creates full representation of the array with references to array entries
   */
  public PSEVariableArray(String varName, String varTypeName,
                          String varValue, boolean isStatic,
                          String definedIn, int index, int length, PSEVariable[] refArrayItems) {
    super(varName, varTypeName, varValue, isStatic, definedIn, index);

    this.length = length;
    this.refArrayItems = refArrayItems;
  }

  public int getLength() {
    return length;
  }

  public PSEVariable[] getArrayItems () throws JPFInspectorException {
    return refArrayItems;
  }

  @Override
  public <T> T visit (PSEVisitor<T> visitor) throws JPFInspectorException {
    return visitor.visitPSEVariableArray(this);
  }

}