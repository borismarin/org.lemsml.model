package org.lemsml.model.compiler.semantic.visitors;

import org.lemsml.model.Assign;
import org.lemsml.model.Attachments;
import org.lemsml.model.Case;
import org.lemsml.model.Child;
import org.lemsml.model.ChildInstance;
import org.lemsml.model.Children;
import org.lemsml.model.ComponentReference;
import org.lemsml.model.ComponentRequirement;
import org.lemsml.model.ConditionalDerivedVariable;
import org.lemsml.model.Constant;
import org.lemsml.model.DataDisplay;
import org.lemsml.model.DataWriter;
import org.lemsml.model.DerivedParameter;
import org.lemsml.model.DerivedVariable;
import org.lemsml.model.Dynamics;
import org.lemsml.model.EventConnection;
import org.lemsml.model.EventOut;
import org.lemsml.model.EventPort;
import org.lemsml.model.Exposure;
import org.lemsml.model.Fixed;
import org.lemsml.model.ForEach;
import org.lemsml.model.Include;
import org.lemsml.model.IndexParameter;
import org.lemsml.model.InstanceRequirement;
import org.lemsml.model.KineticScheme;
import org.lemsml.model.Lems;
import org.lemsml.model.Link;
import org.lemsml.model.MultiInstantiate;
import org.lemsml.model.OnCondition;
import org.lemsml.model.OnEntry;
import org.lemsml.model.OnEvent;
import org.lemsml.model.OnStart;
import org.lemsml.model.Parameter;
import org.lemsml.model.Path;
import org.lemsml.model.Property;
import org.lemsml.model.Record;
import org.lemsml.model.Regime;
import org.lemsml.model.Requirement;
import org.lemsml.model.Run;
import org.lemsml.model.Simulation;
import org.lemsml.model.StateAssignment;
import org.lemsml.model.StateVariable;
import org.lemsml.model.Structure;
import org.lemsml.model.Target;
import org.lemsml.model.Text;
import org.lemsml.model.TimeDerivative;
import org.lemsml.model.Transition;
import org.lemsml.model.Tunnel;
import org.lemsml.model.With;
import org.lemsml.visitors.DepthFirstTraverserImpl;
import org.lemsml.visitors.Visitor;

public class DepthFirstTraverserExt<E extends Throwable> extends DepthFirstTraverserImpl<E> {


    @Override
    public void traverse(Assign aBean, Visitor<?, E> aVisitor)
        throws E
    {
    }

    @Override
    public void traverse(Attachments aBean, Visitor<?, E> aVisitor)
        throws E
    {
    }

    @Override
    public void traverse(Case aBean, Visitor<?, E> aVisitor)
        throws E
    {
    }

    @Override
    public void traverse(Child aBean, Visitor<?, E> aVisitor)
        throws E
    {
    }

    @Override
    public void traverse(ChildInstance aBean, Visitor<?, E> aVisitor)
        throws E
    {
    }

    @Override
    public void traverse(Children aBean, Visitor<?, E> aVisitor)
        throws E
    {
    }

    @Override
    public void traverse(org.lemsml.model.Component aBean, Visitor<?, E> aVisitor)
        throws E
    {
        for (org.lemsml.model.extended.Component bean: aBean.getComponent()) {
        	bean.accept(aVisitor);
        }
    }

    @Override
    public void traverse(ComponentReference aBean, Visitor<?, E> aVisitor)
        throws E
    {
    }

    @Override
    public void traverse(ComponentRequirement aBean, Visitor<?, E> aVisitor)
        throws E
    {
    }

    @Override
    public void traverse(org.lemsml.model.ComponentType aBean, Visitor<?, E> aVisitor)
        throws E
    {
        for (Parameter bean: aBean.getParameters()) {
            bean.accept(aVisitor);
        }
        for (Fixed bean: aBean.getFixeds()) {
            bean.accept(aVisitor);
        }
        for (Constant bean: aBean.getConstants()) {
            bean.accept(aVisitor);
        }
        for (DerivedParameter bean: aBean.getDerivedParameters()) {
            bean.accept(aVisitor);
        }
        for (Requirement bean: aBean.getRequirements()) {
            bean.accept(aVisitor);
        }
        for (Dynamics bean: aBean.getDynamics()) {
            bean.accept(aVisitor);
        }
        for (Property bean: aBean.getProperties()) {
            bean.accept(aVisitor);
        }
        for (IndexParameter bean: aBean.getIndexParameters()) {
            bean.accept(aVisitor);
        }
        for (Child bean: aBean.getChildren()) {
            bean.accept(aVisitor);
        }
        for (Children bean: aBean.getChildrens()) {
            bean.accept(aVisitor);
        }
        for (Link bean: aBean.getLinks()) {
            bean.accept(aVisitor);
        }
        for (ComponentReference bean: aBean.getComponentReferences()) {
            bean.accept(aVisitor);
        }
        for (Attachments bean: aBean.getAttachments()) {
            bean.accept(aVisitor);
        }
        for (EventPort bean: aBean.getEventPorts()) {
            bean.accept(aVisitor);
        }
        for (Exposure bean: aBean.getExposures()) {
            bean.accept(aVisitor);
        }
        for (ComponentRequirement bean: aBean.getComponentRequirements()) {
            bean.accept(aVisitor);
        }
        for (InstanceRequirement bean: aBean.getInstanceRequirements()) {
            bean.accept(aVisitor);
        }
        for (Path bean: aBean.getPaths()) {
            bean.accept(aVisitor);
        }
        for (Text bean: aBean.getTexts()) {
            bean.accept(aVisitor);
        }
        for (Structure bean: aBean.getStructures()) {
            bean.accept(aVisitor);
        }
        for (Simulation bean: aBean.getSimulations()) {
            bean.accept(aVisitor);
        }
    }

    @Override
    public void traverse(ConditionalDerivedVariable aBean, Visitor<?, E> aVisitor)
        throws E
    {
        for (Case bean: aBean.getCase()) {
            bean.accept(aVisitor);
        }
    }

    @Override
    public void traverse(Constant aBean, Visitor<?, E> aVisitor)
        throws E
    {
    }

    @Override
    public void traverse(DataDisplay aBean, Visitor<?, E> aVisitor)
        throws E
    {
    }

    @Override
    public void traverse(DataWriter aBean, Visitor<?, E> aVisitor)
        throws E
    {
    }

    @Override
    public void traverse(DerivedParameter aBean, Visitor<?, E> aVisitor)
        throws E
    {
    }

    @Override
    public void traverse(DerivedVariable aBean, Visitor<?, E> aVisitor)
        throws E
    {
    }

    @Override
    public void traverse(org.lemsml.model.Dimension aBean, Visitor<?, E> aVisitor)
        throws E
    {
    }

    @Override
    public void traverse(Dynamics aBean, Visitor<?, E> aVisitor)
        throws E
    {
        for (StateVariable bean: aBean.getStateVariables()) {
            bean.accept(aVisitor);
        }
        for (DerivedVariable bean: aBean.getDerivedVariables()) {
            bean.accept(aVisitor);
        }
        for (ConditionalDerivedVariable bean: aBean.getConditionalDerivedVariables()) {
            bean.accept(aVisitor);
        }
        for (TimeDerivative bean: aBean.getTimeDerivatives()) {
            bean.accept(aVisitor);
        }
        for (OnStart bean: aBean.getOnStarts()) {
            bean.accept(aVisitor);
        }
        for (OnEvent bean: aBean.getOnEvents()) {
            bean.accept(aVisitor);
        }
        for (OnCondition bean: aBean.getOnConditions()) {
            bean.accept(aVisitor);
        }
        for (Regime bean: aBean.getRegimes()) {
            bean.accept(aVisitor);
        }
        for (KineticScheme bean: aBean.getKineticSchemes()) {
            bean.accept(aVisitor);
        }
    }

    @Override
    public void traverse(EventConnection aBean, Visitor<?, E> aVisitor)
        throws E
    {
        if (aBean.getAssign()!= null) {
            aBean.getAssign().accept(aVisitor);
        }
    }

    @Override
    public void traverse(EventOut aBean, Visitor<?, E> aVisitor)
        throws E
    {
    }

    @Override
    public void traverse(EventPort aBean, Visitor<?, E> aVisitor)
        throws E
    {
    }

    @Override
    public void traverse(Exposure aBean, Visitor<?, E> aVisitor)
        throws E
    {
    }

    @Override
    public void traverse(Fixed aBean, Visitor<?, E> aVisitor)
        throws E
    {
    }

    @Override
    public void traverse(ForEach aBean, Visitor<?, E> aVisitor)
        throws E
    {
        if (aBean.getMultiInstantiate()!= null) {
            aBean.getMultiInstantiate().accept(aVisitor);
        }
    }

    @Override
    public void traverse(Include aBean, Visitor<?, E> aVisitor)
        throws E
    {
    }

    @Override
    public void traverse(IndexParameter aBean, Visitor<?, E> aVisitor)
        throws E
    {
    }

    @Override
    public void traverse(InstanceRequirement aBean, Visitor<?, E> aVisitor)
        throws E
    {
    }

    @Override
    public void traverse(KineticScheme aBean, Visitor<?, E> aVisitor)
        throws E
    {
    }

    @Override
    public void traverse(Lems aBean, Visitor<?, E> aVisitor)
        throws E
    {
        for (Target bean: aBean.getTargets()) {
            bean.accept(aVisitor);
        }
        for (Include bean: aBean.getIncludes()) {
            bean.accept(aVisitor);
        }
        for (org.lemsml.model.extended.Dimension bean: aBean.getDimensions()) {
            bean.accept(aVisitor);
        }
        for (org.lemsml.model.extended.Unit bean: aBean.getUnits()) {
            bean.accept(aVisitor);
        }
        for (Constant bean: aBean.getConstants()) {
            bean.accept(aVisitor);
        }
        for (org.lemsml.model.extended.ComponentType bean: aBean.getComponentTypes()) {
            bean.accept(aVisitor);
        }
        for (org.lemsml.model.extended.Component bean: aBean.getComponents()) {
            bean.accept(aVisitor);
        }
    }

    @Override
    public void traverse(Link aBean, Visitor<?, E> aVisitor)
        throws E
    {
    }

    @Override
    public void traverse(MultiInstantiate aBean, Visitor<?, E> aVisitor)
        throws E
    {
    }

    @Override
    public void traverse(OnCondition aBean, Visitor<?, E> aVisitor)
        throws E
    {
        for (StateAssignment bean: aBean.getStateAssignments()) {
            bean.accept(aVisitor);
        }
        for (EventOut bean: aBean.getEventOuts()) {
            bean.accept(aVisitor);
        }
        for (Transition bean: aBean.getTransitions()) {
            bean.accept(aVisitor);
        }
    }

    @Override
    public void traverse(OnEntry aBean, Visitor<?, E> aVisitor)
        throws E
    {
        for (StateAssignment bean: aBean.getStateAssignment()) {
            bean.accept(aVisitor);
        }
    }

    @Override
    public void traverse(OnEvent aBean, Visitor<?, E> aVisitor)
        throws E
    {
        for (StateAssignment bean: aBean.getStateAssignments()) {
            bean.accept(aVisitor);
        }
        for (EventOut bean: aBean.getEventOuts()) {
            bean.accept(aVisitor);
        }
    }

    @Override
    public void traverse(OnStart aBean, Visitor<?, E> aVisitor)
        throws E
    {
        for (StateAssignment bean: aBean.getStateAssignment()) {
            bean.accept(aVisitor);
        }
    }

    @Override
    public void traverse(Parameter aBean, Visitor<?, E> aVisitor)
        throws E
    {
    }

    @Override
    public void traverse(Path aBean, Visitor<?, E> aVisitor)
        throws E
    {
    }

    @Override
    public void traverse(Property aBean, Visitor<?, E> aVisitor)
        throws E
    {
    }

    @Override
    public void traverse(Record aBean, Visitor<?, E> aVisitor)
        throws E
    {
    }

    @Override
    public void traverse(Regime aBean, Visitor<?, E> aVisitor)
        throws E
    {
        for (TimeDerivative bean: aBean.getTimeDerivatives()) {
            bean.accept(aVisitor);
        }
        for (OnStart bean: aBean.getOnEntries()) {
            bean.accept(aVisitor);
        }
        for (OnCondition bean: aBean.getOnConditions()) {
            bean.accept(aVisitor);
        }
    }

    @Override
    public void traverse(Requirement aBean, Visitor<?, E> aVisitor)
        throws E
    {
    }

    @Override
    public void traverse(Run aBean, Visitor<?, E> aVisitor)
        throws E
    {
    }

    @Override
    public void traverse(Simulation aBean, Visitor<?, E> aVisitor)
        throws E
    {
        for (DataDisplay bean: aBean.getDataDisplaies()) {
            bean.accept(aVisitor);
        }
        for (Record bean: aBean.getRecords()) {
            bean.accept(aVisitor);
        }
        for (Run bean: aBean.getRuns()) {
            bean.accept(aVisitor);
        }
        for (DataWriter bean: aBean.getDataWriters()) {
            bean.accept(aVisitor);
        }
    }

    @Override
    public void traverse(StateAssignment aBean, Visitor<?, E> aVisitor)
        throws E
    {
    }

    @Override
    public void traverse(StateVariable aBean, Visitor<?, E> aVisitor)
        throws E
    {
    }

    @Override
    public void traverse(Structure aBean, Visitor<?, E> aVisitor)
        throws E
    {
        for (ChildInstance bean: aBean.getChildInstances()) {
            bean.accept(aVisitor);
        }
        for (MultiInstantiate bean: aBean.getMultiInstantiates()) {
            bean.accept(aVisitor);
        }
        for (ForEach bean: aBean.getForEaches()) {
            bean.accept(aVisitor);
        }
        for (With bean: aBean.getWiths()) {
            bean.accept(aVisitor);
        }
        for (Tunnel bean: aBean.getTunnels()) {
            bean.accept(aVisitor);
        }
        for (EventConnection bean: aBean.getEventConnections()) {
            bean.accept(aVisitor);
        }
    }

    @Override
    public void traverse(Target aBean, Visitor<?, E> aVisitor)
        throws E
    {
    }

    @Override
    public void traverse(Text aBean, Visitor<?, E> aVisitor)
        throws E
    {
    }

    @Override
    public void traverse(TimeDerivative aBean, Visitor<?, E> aVisitor)
        throws E
    {
    }

    @Override
    public void traverse(Transition aBean, Visitor<?, E> aVisitor)
        throws E
    {
    }

    @Override
    public void traverse(Tunnel aBean, Visitor<?, E> aVisitor)
        throws E
    {
    }

    @Override
    public void traverse(org.lemsml.model.Unit aBean, Visitor<?, E> aVisitor)
        throws E
    {
    }

    @Override
    public void traverse(With aBean, Visitor<?, E> aVisitor)
        throws E
    {
    }

    
   // method body manually created for extended classes (basically c&p from the nonextended) 
    
    @Override
    public void traverse(org.lemsml.model.extended.Component aBean, Visitor<?, E> aVisitor)
        throws E
    {
    	for (org.lemsml.model.extended.Component bean: aBean.getComponent()) {
        	bean.accept(aVisitor);
        }
    }

    @Override
    public void traverse(org.lemsml.model.extended.Dimension aBean, Visitor<?, E> aVisitor)
        throws E
    {
    }

    @Override
    public void traverse(org.lemsml.model.extended.Unit aBean, Visitor<?, E> aVisitor)
        throws E
    {
    }

    @Override
    public void traverse(org.lemsml.model.extended.ComponentType aBean, Visitor<?, E> aVisitor)
        throws E
    {
    	for (Parameter bean: aBean.getParameters()) {
            bean.accept(aVisitor);
        }
        for (Fixed bean: aBean.getFixeds()) {
            bean.accept(aVisitor);
        }
        for (Constant bean: aBean.getConstants()) {
            bean.accept(aVisitor);
        }
        for (DerivedParameter bean: aBean.getDerivedParameters()) {
            bean.accept(aVisitor);
        }
        for (Requirement bean: aBean.getRequirements()) {
            bean.accept(aVisitor);
        }
        for (Dynamics bean: aBean.getDynamics()) {
            bean.accept(aVisitor);
        }
        for (Property bean: aBean.getProperties()) {
            bean.accept(aVisitor);
        }
        for (IndexParameter bean: aBean.getIndexParameters()) {
            bean.accept(aVisitor);
        }
        for (Child bean: aBean.getChildren()) {
            bean.accept(aVisitor);
        }
        for (Children bean: aBean.getChildrens()) {
            bean.accept(aVisitor);
        }
        for (Link bean: aBean.getLinks()) {
            bean.accept(aVisitor);
        }
        for (ComponentReference bean: aBean.getComponentReferences()) {
            bean.accept(aVisitor);
        }
        for (Attachments bean: aBean.getAttachments()) {
            bean.accept(aVisitor);
        }
        for (EventPort bean: aBean.getEventPorts()) {
            bean.accept(aVisitor);
        }
        for (Exposure bean: aBean.getExposures()) {
            bean.accept(aVisitor);
        }
        for (ComponentRequirement bean: aBean.getComponentRequirements()) {
            bean.accept(aVisitor);
        }
        for (InstanceRequirement bean: aBean.getInstanceRequirements()) {
            bean.accept(aVisitor);
        }
        for (Path bean: aBean.getPaths()) {
            bean.accept(aVisitor);
        }
        for (Text bean: aBean.getTexts()) {
            bean.accept(aVisitor);
        }
        for (Structure bean: aBean.getStructures()) {
            bean.accept(aVisitor);
        }
        for (Simulation bean: aBean.getSimulations()) {
            bean.accept(aVisitor);
        }
    }

}
