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
package relations;

import gui.ScenarioPanel;
import gui.TemplateView;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Relation implements Serializable {
    /**
     * The name of this relation
     */
    public final String name;
    /**
     * Description of the relation.
     */
    public final String description;
    /**
     * If this relation has a reverse relation, add it here:
     */
    transient public Relation inverse;
    /**
     * A list of entities from which this relation can point.
     */
    private Set<TemplateView> domains = new HashSet<>();
    /**
     * A list of entities to which this relation can point.
     */
    private Set<TemplateView> ranges = new HashSet<>();

    public Relation(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void addDomain(TemplateView domain) {
        domains.add(domain);
    }

    public void addRange(TemplateView range) {
        ranges.add(range);
    }

    public void addDomains(Set<TemplateView> domains) {
        this.domains.addAll(domains);
    }

    public void addRanges(Set<TemplateView> ranges) {
        this.ranges.addAll(ranges);
    }

    public Set<TemplateView> getParentDomains() {
        return domains;
    }

    public Set<TemplateView> getParentRanges() {
        return ranges;
    }

    public Set<TemplateView> getAllDomains(ScenarioPanel scenarioPanel) {
        Set<TemplateView> all = new HashSet<>();
        all.addAll(domains);
        all.addAll(getChildren(domains, scenarioPanel));
        return all;
    }

    public Set<TemplateView> getAllRanges(ScenarioPanel scenarioPanel) {
        Set<TemplateView> all = new HashSet<>();
        all.addAll(ranges);
        all.addAll(getChildren(ranges, scenarioPanel));
        return all;
    }

    /**
     * For all user added domain and range template also the children of these templates need to be added to the range
     * and domain of this relation.
     */
    private Collection<TemplateView> getChildren(Set<TemplateView> passedParents, ScenarioPanel scenarioPanel) {
        Set<TemplateView> childs = new HashSet<>();
        for (TemplateView parent : passedParents) {
            Set<TemplateView> parents = new HashSet<>();
            parents.add(parent);
            childs.addAll(recursivelyAddChildren(parents, scenarioPanel));
        }
        return childs;
    }

    private Set<TemplateView> recursivelyAddChildren(Set<TemplateView> parents, ScenarioPanel scenarioPanel) {
        Set<TemplateView> children = new HashSet<>();
        for (TemplateView template : scenarioPanel.getAllTemplates()) {
            for (TemplateView parentTemplate : template.parentTemplates) {
                if (parents.contains(parentTemplate)) {
                    children.add(template);
                }
            }
        }
        if (children.size() == 0) {
            return parents; // There are no more children
        } else {
            parents.addAll(children);
            parents.addAll(recursivelyAddChildren(children, scenarioPanel));
            return parents;
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
