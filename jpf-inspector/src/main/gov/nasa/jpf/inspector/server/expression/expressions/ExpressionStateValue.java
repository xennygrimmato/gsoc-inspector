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

package gov.nasa.jpf.inspector.server.expression.expressions;

import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.server.expression.ExpressionStateUnaryOperator;
import gov.nasa.jpf.inspector.server.programstate.StateReadableValue;

/**
 * In the expression tree hierarchy, this is a node that has a value, such as an array element or a field,
 * but not a thread or a stack frame.
 */
public abstract class ExpressionStateValue extends ExpressionStateUnaryOperator<ExpressionStateValue> {

  /**
   * Initializes a new instance of this class.
   *
   * @param child A member access expression attached to this expression. May be null to signify that this is the final expression in the acces chain.
   */
  protected ExpressionStateValue (ExpressionStateValue child) {
    super(child);
  }

  /**
   * Returns the hierarchy-2 expression equivalent of this expression.
   *
   * @param parent The expression that this expression is a member of.  Must not be null.
   * @return The hierarchy-2 expression.
   */
  public abstract StateReadableValue toHierarchy2(StateReadableValue parent)
          throws JPFInspectorException;
}