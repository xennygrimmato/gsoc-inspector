
package gov.nasa.jpf.inspector.server.expression.expressions;

import gov.nasa.jpf.inspector.common.pse.ProgramStateEntry;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.interfaces.CustomHitCondition;
import gov.nasa.jpf.inspector.server.breakpoints.BreakPointModes;
import gov.nasa.jpf.inspector.server.expression.ExpressionBooleanLeaf;
import gov.nasa.jpf.inspector.server.expression.ExpressionStateRootNode;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.server.programstate.StateNodeInterface;
import gov.nasa.jpf.inspector.utils.expressions.Expressions;

public class ExpressionBreakpointCustomHitCondition extends ExpressionBooleanLeaf {
  private static final boolean DEBUG = true;
  private final JPFInspector inspector;
  private final String name;
  private final Expressions parameters;
  private final CustomHitCondition customHitCondition;


  public ExpressionBreakpointCustomHitCondition(JPFInspector inspector, String name, CustomHitCondition customHitCondition, Expressions expressions) {
    assert name != null;
    assert inspector != null;
    assert expressions != null;
    this.inspector = inspector;
    this.parameters = expressions;
    this.name = name;
    this.customHitCondition = customHitCondition;
  }

  @Override
  public boolean evaluateExpression (InspectorState state) {
    assert state != null;
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".evaluateExpression(...)");
    }
    // TODO handle exceptions
    return false;
  }

  private ProgramStateEntry[] evaluateArguments(InspectorState state) throws JPFInspectorException {
    ProgramStateEntry[] entries = new ProgramStateEntry[parameters.size()];
    for (int i = 0; i < parameters.size(); i++) {
      ExpressionStateRootNode arg = parameters.get(i);
      StateNodeInterface resultExpression = arg.getResultExpression(inspector, state);
      ProgramStateEntry programStateEntry = resultExpression.getResultExpression("", 0);
      entries[i] = programStateEntry;
    }
    return entries;
  }

  @Override
  public BreakPointModes getBPMode () {
    return BreakPointModes.BP_MODE_USER_CONDITION;
  }

  @Override
  public String getDetails (InspectorState state) {
    if (state != null && evaluateExpression(state)) {
      try {
        return customHitCondition.getDetails(state, evaluateArguments(state));
      } catch (Exception e) {
        // TODO handle exceptions
        return null;
      }
    } else {
      return "";
    }
  }

  @Override
  public String getNormalizedExpression () {
    String string = name + "(" + parameters.toString() + ")";

    return string;
  }

}
