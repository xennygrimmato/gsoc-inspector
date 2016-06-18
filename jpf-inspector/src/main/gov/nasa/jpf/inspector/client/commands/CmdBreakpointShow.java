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

package gov.nasa.jpf.inspector.client.commands;

import gov.nasa.jpf.inspector.client.ClientCommand;
import gov.nasa.jpf.inspector.client.JPFInspectorClient;
import gov.nasa.jpf.inspector.interfaces.BreakpointCreationInformation;
import gov.nasa.jpf.inspector.interfaces.BreakpointStatus;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;

import java.io.PrintStream;
import java.util.List;

/**
 * Represents the "show breakpoint" command that prints all existing breakpoints.
 */
public class CmdBreakpointShow extends ClientCommand {

  @Override
  public void execute(JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream) {
    List<BreakpointStatus> bps = inspector.getBreakpoints();
    for (BreakpointStatus bp : bps) {
      outStream.println(breakpointToString(bp));
    }
    if (bps.isEmpty()) {
      outStream.println("No breakpoints.");
    }
  }

  @Override
  public String getNormalizedCommand () {
    return "show breakpoint";
  }

  /**
   * Transforms breakpoint representation into the string.
   * 
   * @param bp Information about a breakpoint.
   * @return String representation of the brekapoint status.
   */
  public static String breakpointToString (BreakpointStatus bp) {
    StringBuilder bpText = new StringBuilder(80);
    bpText.append(bp.getBPID());
    bpText.append(" : ");
    if (bp.getName() != null && !bp.getName().equals("")) {
      bpText.append(" name=");
      bpText.append(bp.getName());
    }
    bpText.append(" state=");
    bpText.append(bp.getState().toString());

    bpText.append(" hits=");
    bpText.append(bp.getHitCounter());

    bpText.append(" hitsTotal=");
    bpText.append(bp.getHitCounterTotal());

    if (bp.bpHitCountLowerBound() != null || bp.bpHitCountUpperBound() != null) {
      bpText.append(' ');
      Integer lowerBound = bp.bpHitCountLowerBound();
      Integer upperBound = bp.bpHitCountUpperBound();
      if (lowerBound != null && (!lowerBound.equals(BreakpointCreationInformation.DEFAULT_LOWER_BOUND))) {
        bpText.append(lowerBound);
        bpText.append("<=");
      }
      bpText.append("hit_count");
      if (upperBound != null && (!upperBound.equals(BreakpointCreationInformation.DEFAULT_UPPER_BOUND))) {
        bpText.append("<=");
        bpText.append(upperBound);
      }
    }

    bpText.append(' ');
    bpText.append(bp.getNormalizedBreakpointExpression());

    return bpText.toString();
  }

}