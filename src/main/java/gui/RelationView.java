/*
 * Copyright 2016 Anna Eggers - Göttingen State and University Library
 * The work has been developed in the PERICLES Project by Members of the PERICLES Consortium.
 * This project has received funding from the European Union’s Seventh Framework Programme for research, technological
 * development and demonstration under grant agreement no FP7- 601138 PERICLES.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at:   http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied, including without
 * limitation, any warranties or conditions of TITLE, NON-INFRINGEMENT, MERCHANTIBITLY, or FITNESS FOR A PARTICULAR
 * PURPOSE. In no event and under no legal theory, whether in tort (including negligence), contract, or otherwise,
 * unless required by applicable law or agreed to in writing, shall any Contributor be liable for damages, including
 * any direct, indirect, special, incidental, or consequential damages of any character arising as a result of this
 * License or out of the use or inability to use the Work.
 * See the License for the specific language governing permissions and limitation under the License.
 */
package gui;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import relations.DEMRelation;
import relations.Relation;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represents the view of a {@link DEMRelation} in the graphical user interface.
 * In contrast to the {@link DEMRelation} it is not created in a model yet. The {@link DEMRelationView} already has an
 * associated {@link DEMRelation} which belongs to one of the DEM models.,
 */
public class RelationView extends GridPane implements Serializable {
    public final EntityView parentEntity;
    public final Set<TargetEntityView> childTargetEntities = new HashSet<>();
    public final Set<RangeValueView> childValueEntities = new HashSet<RangeValueView>();

    public final Relation relation;

    public RelationView(Relation relation, final EntityView parentEntity, final EntityView targetEntity) {
        this(relation, parentEntity);
        addTarget(targetEntity);
    }

    public RelationView(Relation relation, EntityView parentEntity, String value) {
        this(relation, parentEntity);
        addValueTarget(value);
    }

    public RelationView(Relation relation, EntityView parentEntity) {
        this.parentEntity = parentEntity;
        this.relation = relation;
        getStyleClass().add(EcoBuilder.PANE);
        parentEntity.childRelations.add(this);
        setId(EcoBuilder.RELATION_ID);
        Text nameLabel = new Text(this.toString());
        Button remove = new Button("X");
        remove.setId(EcoBuilder.REMOVE_BUTTON_ID);
        remove.setOnAction(e -> removeThisRelationView());
        setConstraints(remove, 2, 0);
        setConstraints(nameLabel, 0, 0);
        getChildren().addAll(nameLabel, remove);
    }


    /**
     * Adds a target to this relation view
     *
     * @param targetEntity
     */
    protected TargetEntityView addTarget(EntityView targetEntity) {
        TargetEntityView target = new TargetEntityView(targetEntity, this);
        childTargetEntities.add(target);
        setConstraints(target, 1, childTargetEntities.size() + childValueEntities.size());
        getChildren().add(target);
        return  target;
    }

    /**
     * Adds a text as target referred by this relation
     *
     * @param value target text
     */
    public void addValueTarget(String value) {
        RangeValueView view = new RangeValueView(value, this);
        childValueEntities.add(view);
        setConstraints(view, 1, childTargetEntities.size() + childValueEntities.size());
        getChildren().add(view);
    }

    /**
     * This method is called if the user clicked the X-Button at a {@link TargetEntityView}. It will remove the target
     * from the relation view. It there are no other targets, then the whole RelationView is removed.
     *
     * @param target
     */
    protected void removeTarget(TargetEntityView target) {
        removeTargetView(target);
        removeInverseRelation(target);
    }

    /**
     * A target was removed. If this relation has an inverse relation, then that one has to be removed, too. The subject
     * of the inverse relation is the target.sameEntity, the predicate is the relation.inverse, and the object is the
     * parentEntity.
     *
     * @param target
     */
    private void removeInverseRelation(TargetEntityView target) {
        if (relation.inverse == null) {
            return; // no inverse to be removed
        }
        //This triple will be removed:
        final EntityView subject = target.sameEntity;
        final Relation predicate = relation.inverse;
        final EntityView object = parentEntity;
        RelationView predicateView = getPredicateView(subject, predicate);
        if (predicateView == null) {
            return;
        }
        TargetEntityView objectView = getObjectView(predicateView, object);
        if (objectView == null) {
            return;
        }
        predicateView.removeInverseTarget(objectView);
    }

    private RelationView getPredicateView(EntityView subject, Relation predicate) {
        for (RelationView relationView : subject.childRelations) {
            if (relationView.relation == predicate) {
                return relationView;
            }
        }
        return null;
    }

    private TargetEntityView getObjectView(RelationView predicateView, EntityView object) {
        for (TargetEntityView targetView : predicateView.childTargetEntities) {
            if (targetView.sameEntity == object) {
                return targetView;
            }
        }
        return null;
    }

    /**
     * This is called in the process of removing an inverse relation
     *
     * @param target
     */
    protected void removeInverseTarget(TargetEntityView target) {
        removeTargetView(target);
    }

    private void removeTargetView(TargetEntityView target) {
        childTargetEntities.remove(target);
        int removedIndex = getChildren().indexOf(target);
        getChildren().remove(target);
        revalidateRelationGUI(removedIndex);
    }

    /**
     * Removes a text target.
     */

    protected void removeValueTarget(RangeValueView targetEntity) {
        childValueEntities.remove(targetEntity);
        int removedIndex = getChildren().indexOf(targetEntity);
        getChildren().remove(targetEntity);
        revalidateRelationGUI(removedIndex);
    }

    /**
     * This is called if a relation target was removed.
     * If the target was the last entity referred by this relation, remove the complete relation view.
     * Adjusts also the alignment of the other targets, otherwise there are holes in the GUI.
     *
     * @param removedIndex Index of the removed target in the list of targets of this relation.
     */
    private void revalidateRelationGUI(int removedIndex) {
        if (childTargetEntities.size() == 0 && childValueEntities.size() == 0) {
            parentEntity.removeRelation(this);
        } else {
            adjustChildren(removedIndex);
        }
    }

    /**
     * The x-Button was clicked to remove the whole relation view with all relations. This will first remove all targets
     * and afterwards this view.
     */
    private void removeThisRelationView() {
        while (childValueEntities.size() > 0) {
            removeValueTarget(childValueEntities.iterator().next());
        }
        while (childTargetEntities.size() > 0) {
            removeTarget(childTargetEntities.iterator().next());
        }
    }


    /**
     * Adjust the indices of all other children after removeValueTarget
     *
     * @param removedIndex
     */
    private void adjustChildren(int removedIndex) {
        for (Node child : getChildren()) {
            int currentIndex = getChildren().indexOf(child);
            if (currentIndex >= removedIndex && (child instanceof TargetEntityView || child instanceof RangeValueView)) {
                setConstraints(child, 1, currentIndex - 1);
            }
        }
    }

    @Override
    public String toString() {
        return relation.name;
    }

}
