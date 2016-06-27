package gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy;

import gov.nasa.jpf.inspector.frontends.swing.explorer.AttachmentInformation;
import gov.nasa.jpf.inspector.frontends.swing.explorer.ExplorerNodeFactory;
import gov.nasa.jpf.inspector.frontends.swing.explorer.ProgramStateTreeModel;
import gov.nasa.jpf.inspector.server.programstate.StateReadableValue;
import gov.nasa.jpf.inspector.server.programstate.StateWritableValue;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.FieldInfo;

import java.util.ArrayList;

public class ExplorerJavaObject extends ExplorerComplexNode {
  private AttachmentInformation attachmentInformation;
  private final ElementInfo elementInfo;

  public ExplorerJavaObject(AttachmentInformation attachmentInformation, ElementInfo elementInfo,
                            ProgramStateTreeModel model, ExplorerNode parent) {
    super(model, parent);

    this.attachmentInformation = attachmentInformation;
    this.elementInfo = elementInfo;
  }


  @Override
  protected ArrayList<ExplorerNode> populateChildren() {
    ArrayList<ExplorerNode> children = new ArrayList<>();
    for (FieldInfo fieldInfo : elementInfo.getClassInfo().getInstanceFields()) {
       String fieldInfoName = fieldInfo.getName();
       ExplorerNode child = ExplorerNodeFactory.createFromField(fieldInfoName, fieldInfo, elementInfo.getFields(), model, this);
       children.add(child);
    }
    return children;
  }

  @Override
  public void updateComplexNodeFromJpf(ExplorerNode newVersion) {
  }

  @Override
  public String toString() {
    if (elementInfo == null) {
      return attachmentInformation.getName() + ": " + null;
    }
    String typeName = StateWritableValue.demangleTypeName(elementInfo.getType());
    String shortFormValue = StateReadableValue.elementInfo2String(elementInfo);
    return attachmentInformation.getName() + " (" + typeName + ") = " + shortFormValue;
  }

  @Override
  public boolean isRecognizableAs(ExplorerNode oldNode) {
    return oldNode instanceof ExplorerJavaObject &&
            ((ExplorerJavaObject)oldNode).elementInfo == this.elementInfo &&
            ((ExplorerJavaObject)oldNode).attachmentInformation == this.attachmentInformation;
  }
}
