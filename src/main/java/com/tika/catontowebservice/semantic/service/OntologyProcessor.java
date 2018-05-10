/*
 * Copyright (C) 2018 Timur Kalugin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.tika.catontowebservice.semantic.service;

import com.tika.catontowebservice.semantic.util.OntologyUtil;
import com.tika.catontowebservice.semantic.vocabulary.Catonto;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLClassExpressionVisitor;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectCardinalityRestriction;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectVisitor;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLQuantifiedObjectRestriction;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.SimpleRenderer;

/**
 *
 * @author Timur Kalugin
 */
public class OntologyProcessor {
    
    private static final OWLOntology ontology;
    private static final OWLDataFactory dataFactory;
    private static final OWLReasoner reasoner;
    
    static {
        ontology = OntologyUtil.getOntology();
        dataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();
        reasoner = OntologyUtil.getReasoner();
    }
    
    public static void main(String[] args) {
//        Stream<OWLClassAxiom> tempAx = ontology.axioms(getOntoClass("TestBreed"));
//        tempAx.forEach((axiom) -> {
//            axiom.nestedClassExpressions().filter(ex -> ex.getClassExpressionType() != ClassExpressionType.OWL_CLASS)
//                    .forEach((classExpression) -> {
//                        System.out.println(classExpression);
//                    });
//        });
        //shouldLookAtRestrictions();
        
//        reasoner.getObjectPropertyValues(dataFactory.getOWLNamedIndividual(getIri("TestBreedInst")),
//                dataFactory.getOWLObjectProperty(getIri("hasTorsoSize")))
//                .forEach(e -> System.out.println(e.getRepresentativeElement().getIRI().getRemainder().get()));
//        EntitySearcher.getTypes(getOntoIndividual("TestBreedInst"), ontology).forEach((t) -> {
//            System.out.println(t.asOWLClass());
//        });
        //reasoner.getTypes(getOntoIndividual("TestBreedInst")).forEach(System.out::println);
        System.out.println(getPropertyValuesForBreedIndividual(getOntoIndividual("TestBreedInst")));
//        SimpleRenderer renderer = new SimpleRenderer();
//        EntitySearcher.getObjectPropertyValues(dataFactory.getOWLNamedIndividual(getIri("TestBreedInst")), ontology)
//                .asMap().forEach((property, indCollection) -> {
//                    System.out.println(property);
//                    indCollection.forEach(ind -> System.out.println("\t" + renderer.render(ind)));
//                });
    }
    
    public static Map<OWLObjectPropertyExpression, Set<OWLNamedIndividual>>
            getPropertyValuesForBreedIndividual(OWLNamedIndividual individual) {
        Map<OWLObjectPropertyExpression, Set<OWLNamedIndividual>> propertyValuesMap = new HashMap<>();
        getSpecifiedClassDomainObjectProperties(getOntoClass(Catonto.BREED))
                .forEach(property -> propertyValuesMap.put(property, 
                        reasoner.getObjectPropertyValues(individual, property)
                                .entities().filter(ind -> 
                                    EntitySearcher.getRanges(property, ontology)
                                            .anyMatch(range -> EntitySearcher.getTypes(ind, ontology)
                                                    .anyMatch(type -> range.asOWLClass().getIRI().equals(type.asOWLClass().getIRI())))
                                    
                                ).collect(Collectors.toSet())));
        return propertyValuesMap;
    }
        
    private static ArrayList<OWLObjectPropertyExpression>
            getSpecifiedClassDomainObjectProperties(OWLClass ontoClass) {
        ArrayList<OWLObjectPropertyExpression> properties = new ArrayList<>();
        ontology.objectPropertiesInSignature()
                .filter(property -> reasoner.getObjectPropertyDomains(property).entities()
                        .filter(domain -> ontoClass.equals(domain)).findFirst().isPresent())
                .forEach(property -> properties.add(property));
        return properties;
    }
    
    public static void shouldLookAtRestrictions() {
        
        OWLClass testBreed = getOntoClass("TestBreed");
        RestrictionVisitor restrictionVisitor = new RestrictionVisitor(
                Collections.singleton(ontology));
        for (OWLSubClassOfAxiom ax : ontology
                .getSubClassAxiomsForSubClass(testBreed)) {
            OWLClassExpression superCls = ax.getSuperClass();
            superCls.accept(restrictionVisitor);
        }
        System.out.println("Restricted properties for " + testBreed
                + ": " + restrictionVisitor.getRestrictedProperties().size());
        restrictionVisitor.getRestrictedElements().forEach((predicat, object) -> {
            System.out.println(predicat + " " + object);
        });
    }

    private static class RestrictionVisitor implements OWLClassExpressionVisitor {

        private final Set<OWLClass> processedClasses;
        private final Set<OWLObjectPropertyExpression> restrictedProperties;
        private final Set<OWLClassExpression> restrictedObjects;
        private final Map<OWLObjectPropertyExpression, OWLIndividual> restrictedElements;
        private final Set<OWLOntology> ontology;

        public RestrictionVisitor(Set<OWLOntology> ontology) {
            restrictedProperties = new HashSet<>();
            restrictedObjects = new HashSet<>();
            restrictedElements = new HashMap<>();
            processedClasses = new HashSet<>();
            this.ontology = ontology;
        }

        public Set<OWLObjectPropertyExpression> getRestrictedProperties() {
            return restrictedProperties;
        }
        
        public Map getRestrictedElements() {
            return restrictedElements;
        }

        @Override
        public void visit(OWLClass ontoClass) {
            if (!processedClasses.contains(ontoClass)) {
                processedClasses.add(ontoClass);
                ontology.forEach((o) -> {
                    o.subClassAxiomsForSubClass(ontoClass)
                            .forEach(axiom -> axiom.getSuperClass().accept(this));
                });
            }
        }

        @Override
        public void visit(OWLObjectHasValue restriction) {
            //restrictedObjects.add(restriction.getFiller());
            //restrictedProperties.add(restriction.getProperty());
            restrictedElements.put(restriction.getProperty(), restriction.getFiller());
        }
    }
    
    public static void main1(String[] args) {

        System.out.println("Read and classes their axioms...\n");

        // get all classes in the ontology
        for (OWLClass oc : ontology.classesInSignature().collect(Collectors.toSet())) {
            System.out.println("Class: " + oc.toString());

            // get all axioms for each class
            for (OWLAxiom axiom : ontology.axioms(oc).collect(Collectors.toSet())) {
                System.out.println("\tAxiom: " + axiom.toString());

                // create an object visitor to get to the subClass restrictions
                axiom.accept(new OWLObjectVisitor() {

                    // found the subClassOf axiom  
                    public void visit(OWLSubClassOfAxiom subClassAxiom) {

                        // create an object visitor to read the underlying (subClassOf) restrictions
                        subClassAxiom.getSuperClass().accept(new OWLObjectVisitor() {
                            
                            public void visit(OWLObjectSomeValuesFrom someValuesFromAxiom) {
                                printQuantifiedRestriction(oc, someValuesFromAxiom);
                            }

                            public void visit(OWLObjectExactCardinality exactCardinalityAxiom) {
                                printCardinalityRestriction(oc, exactCardinalityAxiom);
                            }

                            public void visit(OWLObjectMinCardinality minCardinalityAxiom) {
                                printCardinalityRestriction(oc, minCardinalityAxiom);
                            }

                            public void visit(OWLObjectMaxCardinality maxCardinalityAxiom) {
                                printCardinalityRestriction(oc, maxCardinalityAxiom);
                            }

                            // TODO: same for AllValuesFrom etc.
                        });
                    }
                });

            }

            System.out.println();
        }
    }
    
    public static void printQuantifiedRestriction(OWLClass oc, OWLQuantifiedObjectRestriction restriction) {
        System.out.println("\t\tClass: " + oc.toString());
        System.out.println("\t\tClassExpressionType: " + restriction.getClassExpressionType().toString());
        System.out.println("\t\tProperty: " + restriction.getProperty().toString());
        System.out.println("\t\tObject: " + restriction.getFiller().toString());
        System.out.println();
    }

    public static void printCardinalityRestriction(OWLClass oc, OWLObjectCardinalityRestriction restriction) {
        System.out.println("\t\tClass: " + oc.toString());
        System.out.println("\t\tClassExpressionType: " + restriction.getClassExpressionType().toString());
        System.out.println("\t\tCardinality: " + restriction.getCardinality());
        System.out.println("\t\tProperty: " + restriction.getProperty().toString());
        System.out.println("\t\tObject: " + restriction.getFiller().toString());
        System.out.println();
    }
    
    public static OWLClass addBreed(String breedName) {
        OWLClass topBreed = getOntoClass(Catonto.BREED);
        OWLClass newBreed = getOntoClass(breedName);
        OWLSubClassOfAxiom axiom = dataFactory.getOWLSubClassOfAxiom(newBreed, topBreed);
        System.out.println(axiom);
        return newBreed;
    }
    
    public static IRI getIri(String entityName) {
        return IRI.create(Catonto.NAMESPACE + entityName);
    }
    
    public static OWLClass getOntoClass(String name) {
        return getOntoClass(getIri(name));
    }
    
    public static OWLClass getOntoClass(IRI iri) {
        return dataFactory.getOWLClass(iri);
    }
    
    public static OWLNamedIndividual getOntoIndividual(String name) {
        return getOntoIndividual(getIri(name));
    }
    
    public static OWLNamedIndividual getOntoIndividual(IRI iri) {
        return dataFactory.getOWLNamedIndividual(iri);
    }
}
