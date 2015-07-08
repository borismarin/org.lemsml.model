package org.lemsml.model.compiler.semantic.visitors;

import org.lemsml.model.extended.interfaces.HasLems;
import org.lemsml.visitors.Visitor;

public interface ILemsProcessor extends HasLems, Visitor<Boolean, Throwable> {

}
