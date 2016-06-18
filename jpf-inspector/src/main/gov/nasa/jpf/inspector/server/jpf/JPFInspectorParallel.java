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

package gov.nasa.jpf.inspector.server.jpf;

import gov.nasa.jpf.inspector.interfaces.*;
import gov.nasa.jpf.inspector.interfaces.AssertCreationInformation;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorParsingErrorException;
import gov.nasa.jpf.inspector.common.pse.ProgramStateEntry;
import gov.nasa.jpf.inspector.common.pse.PSEThread;
import gov.nasa.jpf.inspector.utils.ChoiceGeneratorWrapper;

import java.util.List;
import java.util.Map;

/**
 * Represents the concrete server part of the Inspector.
 * It is a mystery to me to why the server was separated into {@link JPFInspector} and {@link JPFInspectorParallel}.
 * Maybe it should be merged?
 */
public class JPFInspectorParallel extends JPFInspector {

  public JPFInspectorParallel (InspectorCallbacks callBack) {
    super(callBack);
  }

  @Override
  public void start () throws JPFInspectorGenericErrorException {
    cmdMgr.start();
  }

  @Override
  public void waitUntilStopped() {
    cmdMgr.waitUntilStopped();
  }

  @Override
  public boolean isPaused() {
    return cmdMgr.isPaused();
  }

  @Override
  public void stop () throws JPFInspectorGenericErrorException {
    cmdMgr.stop();
  }

  @Override
  public BreakpointStatus createBreakPoint (BreakpointCreationInformation newBP) throws JPFInspectorParsingErrorException, JPFInspectorGenericErrorException {
    return breakpointMgr.createBreakPoint(newBP);
  }

  @Override
  public AssertStatus createAssert (AssertCreationInformation newAssert) throws JPFInspectorParsingErrorException, JPFInspectorGenericErrorException {
    return breakpointMgr.createAssert(newAssert);
  }

  @Override
  public boolean deleteBreakPoint (int bpID) {
    return breakpointMgr.deleteBreakPoint(bpID);
  }

  @Override
  public List<BreakpointStatus> getBreakpoints() {
    return breakpointMgr.getBreakpoints();
  }

  @Override
  public void backwardStep (StepType type) throws JPFInspectorException {
    cmdMgr.backwardStep(type);
  }

  @Override
  public void forwardStep (StepType type) throws JPFInspectorException {
    cmdMgr.forwardStep(type);

  }

  @Override
  public List<ChoiceGeneratorWrapper> getUsedChoiceGenerators (boolean wait) throws JPFInspectorException {
    return cgMgr.getUsedChoiceGenerators(wait);
  }

  @Override
  public Map<Integer, PSEThread> getThreads (Integer threadNum) throws JPFInspectorException {
    return stateMgr.getThreads(threadNum);
  }

  @Override
  public Map<Integer, InstructionPosition> getThreadsPC (Integer threadNum) throws JPFInspectorException {
    return stateMgr.getThreadsPC(threadNum);
  }

  @Override
  public ProgramStateEntry evaluateStateExpression (String expr) throws JPFInspectorException {
    return stateMgr.evaluateStateExpression(expr);
  }

  @Override
  public void modifyCGNotifications (CGNotificationSpecification spec) {
    cgMgr.modifyCGNotifications(spec);
  }

  @Override
  public CGNotificationSpecification[] getCGNotificationStatus () {
    return cgMgr.getCGNotificationStatus();
  }

  @Override
  public void selectChoice (int selectedChoice) throws JPFInspectorException {
    cgMgr.selectChoice(selectedChoice);
  }

  @Override
  public void setValue (String expr) throws JPFInspectorException {
    stateMgr.setValue(expr);
  }
}
