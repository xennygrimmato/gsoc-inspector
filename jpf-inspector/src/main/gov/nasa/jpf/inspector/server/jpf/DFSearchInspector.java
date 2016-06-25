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

import gov.nasa.jpf.Config;
import gov.nasa.jpf.vm.VM;
import gov.nasa.jpf.search.DFSearch;

/**
 * The DFSearchInspector is a subclass of DFSearch and has the same functionality, except that it also
 * implements the {@link SearchInspectorExtension} interface which is neccessary in order for the Search class
 * to work with the Inspector.
 *
 * This is the default search class when using the Inspector.
 *
 * You may use this class as a reference implementation for your own search classes if you don't want to use
 * the {@link SearchWrapper} search class which handles interfacing with the Inspector for you.
 */
public class DFSearchInspector extends DFSearch implements SearchInspectorExtension {
  private JPFInspector inspector = null;

  public DFSearchInspector (Config config, VM vm) {
    super(config, vm);
  }

  @Override
  public void setInspector (JPFInspector inspector) {
    this.inspector = inspector;
  }

  @Override
  public void terminate () {
    super.terminate();
    if (inspector != null) {
      inspector.getStopHolder().terminating();
    }
  }
}