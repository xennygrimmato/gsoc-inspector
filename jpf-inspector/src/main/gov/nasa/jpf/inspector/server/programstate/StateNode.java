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

package gov.nasa.jpf.inspector.server.programstate;

import gov.nasa.jpf.inspector.common.pse.ProgramStateEntry;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;

/**
 * A node of the intermediate representation of an expression (hierarchy 2).
 * Hierarchy 2 class names start with "State".
 */
public abstract class StateNode implements StateNodeInterface {

  /**
   * An expression that, if evaluated in the same program state,
   * would result in an equal {@link ProgramStateEntry}.
   */
  private String stateExpr;

  /**
   * The Inspector server.
   */
  private final JPFInspector inspector;

  protected StateNode (JPFInspector inspector) {
    assert inspector != null;

    this.inspector = inspector;
  }

  @Override
  public JPFInspector getInspector () {
    return inspector;
  }

  /**
   * Sets the expression that, if evaluated using print, would give rise to this.
   */
  protected void setStateExpr (String stateExpr) {
    assert (stateExpr != null); // Illegal state expression
    assert (this.stateExpr == null); // State expression must be set only once.
    this.stateExpr = stateExpr;
  }

  @Override
  public String getStateExpr () {
    assert (stateExpr != null); // State expression has to be set exactly from the constructor
    return stateExpr;
  }
}