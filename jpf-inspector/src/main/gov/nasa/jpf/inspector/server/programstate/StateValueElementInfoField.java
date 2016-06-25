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

package gov.nasa.jpf.inspector.server.programstate;

import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorInvalidFieldIndexException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorInvalidFieldNameException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorInvalidStaticFieldIndexException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorInvalidStaticFieldNameException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorNoStaticElementInfoException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorNoSuperClassException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorNotInnerClassException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorNotInstanceException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorNotSuperClassException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorNullValueException;
import gov.nasa.jpf.inspector.common.pse.PSEVariable;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.FieldInfo;
import gov.nasa.jpf.vm.Fields;
import gov.nasa.jpf.vm.StaticElementInfo;

/**
 * @author Alf
 * 
 */
public class StateValueElementInfoField extends StateWritableValue {

  private final ElementInfo ei;
  private final FieldInfo fieldInfo;

  public static StateValueElementInfoField createInstanceNamedField (StateReadableValue srvi, String varName) throws JPFInspectorException {

    ClassInfo ci = srvi.getClassInfo();
    FieldInfo fi = ci.getInstanceField(varName);

    if (fi == null) {
      throw new JPFInspectorInvalidFieldNameException(varName);
    }

    ElementInfo ei = srvi.getReferenceValue();
    if (ei == null) {
      // Represents null value
      throw new JPFInspectorNullValueException(varName, srvi.getClassInfo());
    }

    return new StateValueElementInfoField(srvi, true, srvi.getStateExpr() + '.' + varName, ei, fi);
  }

  public static StateReadableValue createStaticNamedField (StateStackFrame ssf, String varName) throws JPFInspectorException {

    ClassInfo ci = ssf.getClassInfo();
    return StateValueElementInfoField.createStaticNamedFieldGeneric(ssf, ci, varName);
  }

  public static StateReadableValue createStaticNamedField (StateReadableValue srvi, String varName) throws JPFInspectorException {

    ClassInfo ci = srvi.getClassInfo();
    return StateValueElementInfoField.createStaticNamedFieldGeneric(srvi, ci, varName);
  }

  private static StateReadableValue createStaticNamedFieldGeneric(StateNodeInterface sni, ClassInfo ci, String varName)
      throws JPFInspectorInvalidStaticFieldNameException, JPFInspectorNoStaticElementInfoException {
    FieldInfo fi = ci.getStaticField(varName);

    if (fi == null) {
      throw new JPFInspectorInvalidStaticFieldNameException(varName);
    }

    StaticElementInfo sei = ci.getStaticElementInfo();
    if (sei == null) {
      throw new JPFInspectorNoStaticElementInfoException(ci);
    }

    return new StateValueElementInfoField(sni, true, sni.getStateExpr() + '.' + varName, sei, fi);
  }
  public static StateValueElementInfoField createStaticFieldFromIndex (StateReadableValue srvi,
                                                                       int fieldIndex
                                                                       )throws JPFInspectorException
  {
    return createStaticFieldFromIndex(srvi, fieldIndex, true);

  }
  public static StateValueElementInfoField createStaticFieldFromIndex (StateReadableValue srvi,
                                                                       int fieldIndex,
                                                                       boolean expandMembers)
      throws JPFInspectorException {

    ClassInfo ci = srvi.getClassInfo();

    if (fieldIndex < 0 || fieldIndex >= ci.getNumberOfStaticFields()) {
      throw new JPFInspectorInvalidStaticFieldIndexException(fieldIndex, ci);
    }

    FieldInfo fi = ci.getStaticField(fieldIndex);
    assert (fi != null);

    StaticElementInfo sei = ci.getStaticElementInfo();
    assert (sei != null);

    return new StateValueElementInfoField(srvi,
                                          expandMembers,
                                          srvi.getStateExpr() + '.' + PSEVariable.EXPRESSION_STATIC + '[' + fieldIndex + ']',
                                          sei,
                                          fi);
  }
  public static StateValueElementInfoField createFieldFromIndex (StateReadableValue srvi,
                                                                 int fieldIndex
                                                                 )
          throws JPFInspectorException {
    return createFieldFromIndex(srvi, fieldIndex, true);
  }
  public static StateValueElementInfoField createFieldFromIndex (StateReadableValue srvi,
                                                                 int fieldIndex,
                                                                 boolean expandMembers)
      throws JPFInspectorException {

    ClassInfo ci = srvi.getClassInfo();

    if (fieldIndex < 0 || fieldIndex >= ci.getNumberOfInstanceFields()) {
      throw new JPFInspectorInvalidFieldIndexException(fieldIndex, ci);
    }

    FieldInfo fi = ci.getInstanceField(fieldIndex);
    assert (fi != null);

    ElementInfo ei = srvi.getReferenceValue();
    if (ei == null) {
      // Represents null value
      throw new JPFInspectorNullValueException(PSEVariable.EXPRESSION_VARIABLE_FIELD + '[' + fieldIndex + "] (" + fi.getName() + ')', srvi.getClassInfo());
    }

    return new StateValueElementInfoField(srvi, expandMembers, srvi.getStateExpr() + '.' + PSEVariable.EXPRESSION_VARIABLE_FIELD + '[' + fieldIndex + ']', ei,
        fi);
  }

  /**
   * @return Creates representation of the outer class (or throw exception if no enclosing class exists)
   */
  public static StateReadableValue createOuterClass (StateReadableValue srvi) throws JPFInspectorException {
    assert (srvi != null);

    ClassInfo ci = srvi.getClassInfo();
    ClassInfo ciOuter = ci.getEnclosingClassInfo();
    if (ciOuter == null) {
      throw new JPFInspectorNotInnerClassException(ci);
    }

    String newStateExpr = srvi.getStateExpr() + "." + PSEVariable.EXPRESSION_OUTER_CLASS;
    ElementInfo ei = srvi.getReferenceValue();
    if (StateWritableValue.isStaticElementInfo(ei)) {
      return StateElementInfo.createStaticOuterClass(srvi);
    }
    // Outer class can exists
    if (ei == null) {
      throw new JPFInspectorNotInnerClassException(ci);
    }
    // Implementation based on {@link DynamicElementInfo#getEnclosingElementInfo()}
    for (FieldInfo fi : ci.getDeclaredInstanceFields()) {
      // Note name can be compiler dependent
      // after the '$' is number representing depth of nesting
      if (fi.getName().startsWith("this$")) {
        return new StateValueElementInfoField(srvi, srvi.shouldExpandMembers(), newStateExpr, ei, fi);
      }
    }

    assert false; // Should be have outer class but not Field "this$"num not exists
    throw new JPFInspectorNotInnerClassException(ci);
  }

  private StateValueElementInfoField(StateNodeInterface sni, boolean expandMembers, String stateExpression, ElementInfo ei, FieldInfo fieldInfo) {
    super(sni, expandMembers, fieldInfo.getTypeClassInfo(), stateExpression);

    assert (ei != null);
    this.ei = ei;
    this.fieldInfo = fieldInfo;
  }

  /**
   * Creates different(restricted) view on the represented value.
   * 
   * @throws JPFInspectorNotSuperClassException
   */
  private StateValueElementInfoField(StateValueElementInfoField me, ClassInfo superClassInfo, String stateExpression)
      throws JPFInspectorNotSuperClassException {
    super(me, superClassInfo, stateExpression);

    this.ei = me.ei;
    this.fieldInfo = me.fieldInfo;
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateReadableValue#getReferenceValue() */
  @Override
  public ElementInfo getReferenceValue () {
    if (fieldInfo.isReference()) {
      int ref = ei.getReferenceField(fieldInfo);
      return getReferenceValueImpl(ref);
    }

    return null;
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateReadableValue#getValue() */
  @Override
  public Object getValue () {
    final Fields fields = ei.getFields();

    return fieldInfo.getValueObject(fields);
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateNode#getResultExpression(java.lang.String, int) */
  @Override
  public PSEVariable toHierarchy3() throws JPFInspectorException {
    final String varName = fieldInfo.getName();
    final String definedIn = StateWritableValue.getSimpleName(fieldInfo.getClassInfo());

    return StateReadableValue.createPSEVariable(this, varName, fieldInfo.getFieldIndex(), definedIn);
  }

  /**
   * 
   * At first look up the field in given class, then in predecessors.
   * Look for both instance fields and static fields.
   * Ignores field visibility.
   * 
   * @param ci Class where to find field with given name
   * @param fieldName Name of the field to find
   * @return Representation of the field or null if no field with given name exists.
   */
  public static FieldInfo fieldNameJavaBasedLookup (ClassInfo ci, String fieldName) {
    FieldInfo fi;

    while (ci != null) {
      fi = ci.getDeclaredInstanceField(fieldName);
      if (fi != null)
        return fi;
      fi = ci.getDeclaredStaticField(fieldName);
      if (fi != null)
        return fi;
      ci = ci.getSuperClass();
    }
    return null;
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateReadableValue#createSuper() */
  @Override
  public StateValueElementInfoField createSuper () throws JPFInspectorException {
    ClassInfo superClassInfo = ci.getSuperClass();
    if (superClassInfo == null) {
      throw new JPFInspectorNoSuperClassException(ci);
    }
    return new StateValueElementInfoField(this, superClassInfo, getStateExpr() + '.' + PSEVariable.EXPRESSION_SUPER);
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateReadableValue#createPredecessorClass(gov.nasa.jpf.jvm.ClassInfo) */
  @Override
  public StateValueElementInfoField createPredecessorClass (ClassInfo ci) throws JPFInspectorNotSuperClassException {
    return new StateValueElementInfoField(this, ci, getStateExpr() + '.' + StateWritableValue.getSimpleName(ci));
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateReadableValue#createThisValue() */
  @Override
  public StateValueElementInfoField createThisValue () throws JPFInspectorException {
    if (ci.isArray() || ci.isPrimitive()) {
      throw new JPFInspectorNotInstanceException(ci);
    }

    return new StateValueElementInfoField(this, ci, getStateExpr() + '.' + PSEVariable.EXPRESSION_VARIABLE_THIS);
  }

  // *************************************************************************
  // ** Modify represented value infrastructure
  // *************************************************************************

  /* @see gov.nasa.jpf.inspector.server.programstate.StateWritableValue#assignValueBoolean(boolean) */
  @Override
  protected void assignValueBoolean (boolean newVal) {
    ei.setBooleanField(fieldInfo, newVal);
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateWritableValue#assignValueChar(char) */
  @Override
  protected void assignValueChar (char newVal) {
    ei.setCharField(fieldInfo, newVal);

  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateWritableValue#assignValueByte(byte) */
  @Override
  protected void assignValueByte (byte newVal) {
    ei.setByteField(fieldInfo, newVal);

  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateWritableValue#assignValueShort(short) */
  @Override
  protected void assignValueShort (short newVal) {
    ei.setShortField(fieldInfo, newVal);
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateWritableValue#assignValueInt(int) */
  @Override
  protected void assignValueInt (int newVal) {
    ei.setIntField(fieldInfo, newVal);
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateWritableValue#assignValueLong(long) */
  @Override
  protected void assignValueLong (long newVal) {
    ei.setLongField(fieldInfo, newVal);
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateWritableValue#assignValueFloat(float) */
  @Override
  protected void assignValueFloat (float newVal) {
    ei.setFloatField(fieldInfo, newVal);
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateWritableValue#assignValueDouble(double) */
  @Override
  protected void assignValueDouble (double newVal) {
    ei.setDoubleField(fieldInfo, newVal);
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateWritableValue#assignValueRef(int) */
  @Override
  protected void assignValueRef (int newValRef) {
    ei.setReferenceField(fieldInfo, newValRef);
  }

}