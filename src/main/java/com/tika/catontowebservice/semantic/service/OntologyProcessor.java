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
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.openrdf.model.vocabulary.RDFS;
import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLClassExpressionVisitor;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectCardinalityRestriction;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectVisitor;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.model.OWLPropertyExpression;
import org.semanticweb.owlapi.model.OWLQuantifiedObjectRestriction;
import org.semanticweb.owlapi.model.OWLStorerNotFoundException;
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
    
    private static final OWLClass BREED_CLASS = getOntoClass(Catonto.BREED);
    
    public static void main(String[] args) throws OWLOntologyStorageException {
//        setPropertyValueForIndividual(getOntoIndividual("TestBreedInst"), 
//                getOntoObjectProperty("hasLimbSize"), 
//                getOntoIndividual("LimbSizeLong"));
//        reasoner.getObjectPropertyValues(getOntoIndividual("TestBreedInst"), 
//                getOntoObjectProperty("hasLimbSize"))
//                .forEach(System.out::println);
        for (Map.Entry<OWLPropertyExpression, Set<OWLNamedIndividual>> map : 
                getPropertyValuesForBreedIndividual(getOntoIndividual("TestBreedInst")).entrySet()) {
            if(map.getKey().isDataPropertyExpression()) {
                System.out.println(getRuLabel(map.getKey().asOWLDataProperty()));
                for (OWLNamedIndividual literal : map.getValue()) System.out.println(resolveEntityName(literal.getIRI()));
            }
            else {
                System.out.println(getRuLabel(map.getKey().asOWLObjectProperty()));
                for (OWLNamedIndividual individual : map.getValue()) System.out.println("\t" + getRuLabel(individual));
            }
        };
        getAllBreeds().forEach(ind -> System.out.println(resolveEntityName(ind.getIRI()) + "\t" + getRuLabel(ind)));
    }
    
    public static void setPropertyValuesForIndividual(Map<String, String[]> valuesMap, String individual) {
        ArrayList<OWLAxiom> axioms = new ArrayList<>();
        OWLNamedIndividual _individual = getOntoIndividual(individual);
        valuesMap.entrySet().forEach((entry) -> {
            if(ontology.containsObjectPropertyInSignature(getIri(entry.getKey()))) {
                OWLObjectProperty property = getOntoObjectProperty(entry.getKey());
                for(String value : entry.getValue()) {
                    if(ontology.containsIndividualInSignature(getIri(value))) {
                        axioms.add(dataFactory.getOWLObjectPropertyAssertionAxiom(property, _individual, 
                                getOntoIndividual(value)));
                    }
                }
            } else {
                if(ontology.containsDataPropertyInSignature(getIri(entry.getKey()))) {
                    OWLDataProperty property = getOntoDataProperty(entry.getKey());
                    for(String value : entry.getValue()) {
                        if(NumberUtils.isDigits(value)) {
                            axioms.add(dataFactory.getOWLDataPropertyAssertionAxiom(property, _individual,
                                    dataFactory.getOWLLiteral(Integer.parseInt(value))));
                        } else {
                            if(StringUtils.equals("true", value.toLowerCase()) 
                                    || StringUtils.equals("false", value.toLowerCase())) {
                                axioms.add(dataFactory.getOWLDataPropertyAssertionAxiom(property, _individual,
                                    dataFactory.getOWLLiteral(Boolean.parseBoolean(value))));
                            } else {
                                axioms.add(dataFactory.getOWLDataPropertyAssertionAxiom(property, _individual,
                                    dataFactory.getOWLLiteral(value)));
                            }
                        }
                    }
                }
            }
        });
        ontology.add(axioms);
    }
    
    public static <T> void setPropertyValueForIndividual
        (OWLNamedIndividual individual, OWLProperty property, T value) throws OWLOntologyStorageException {
            OWLAxiom axiom;
            if(property instanceof OWLObjectProperty && value instanceof OWLNamedIndividual) {
                axiom = dataFactory.getOWLObjectPropertyAssertionAxiom((OWLObjectProperty) property, 
                        individual, 
                        (OWLNamedIndividual) value);
                ontology.add(axiom);
            } else {
                if(property instanceof OWLDataProperty) {
                    OWLLiteral literal = null;
                    if(value instanceof String) {
                        literal = dataFactory.getOWLLiteral((String) value);
                    } else if(Integer.class.isInstance(value)) {
                        literal = dataFactory.getOWLLiteral((Integer) value);
                    } else if(Boolean.class.isInstance(value)) {
                        literal = dataFactory.getOWLLiteral((Boolean) value);
                    } else if(Double.class.isInstance(value)) {
                        literal = dataFactory.getOWLLiteral((Double) value);
                    } else if(Float.class.isInstance(value)) {
                        literal = dataFactory.getOWLLiteral((Float) value);
                    }
                    if(literal != null) {
                        axiom = dataFactory.getOWLDataPropertyAssertionAxiom((OWLDataProperty) property, 
                                individual, 
                                literal);
                        ontology.add(axiom);
                    }
                }
            }
    }
    
    public static Map<OWLPropertyExpression, Set<OWLNamedIndividual>>
            getPropertyValuesForBreedIndividual(OWLNamedIndividual individual) {
        Map<OWLPropertyExpression, Set<OWLNamedIndividual>> propertyValuesMap = new HashMap<>();
        getSpecifiedClassDomainObjectProperties(BREED_CLASS)
                .forEach(property -> propertyValuesMap.put(property, 
                        reasoner.getObjectPropertyValues(individual, property)
                                .entities().filter(ind -> 
                                    EntitySearcher.getRanges(property, ontology)
                                            .anyMatch(range -> EntitySearcher.getTypes(ind, ontology)
                                                    .anyMatch(type -> range.asOWLClass().getIRI().equals(type.asOWLClass().getIRI())))
                                ).collect(Collectors.toSet())));
        getSpecifiedClassDomainDataProperties(BREED_CLASS).forEach(property -> {
            Set<OWLNamedIndividual> litsAsInds = new HashSet<>();
            reasoner.getDataPropertyValues(individual, property.asOWLDataProperty())
                    .forEach(value -> litsAsInds.add(getOntoIndividual(value.getLiteral())));
            propertyValuesMap.put(property, litsAsInds);
        });
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
            
    private static ArrayList<OWLDataPropertyExpression>
            getSpecifiedClassDomainDataProperties(OWLClass ontoClass) {
        ArrayList<OWLDataPropertyExpression> properties = new ArrayList<>();
        ontology.dataPropertiesInSignature()
                .filter(property -> reasoner.getDataPropertyDomains(property).entities()
                        .filter(domain -> ontoClass.equals(domain)).findFirst().isPresent())
                .forEach(property -> properties.add(property));
        return properties;
    }
    
    public static OWLNamedIndividual addBreed(String breedName, Map<String, String[]> propertyValueMap) 
                throws OWLOntologyStorageException {
        OWLNamedIndividual breed = getOntoIndividual(breedName);
        OWLClassAssertionAxiom axiom = dataFactory.getOWLClassAssertionAxiom(BREED_CLASS, breed);
        ontology.addAxiom(axiom);
        setPropertyValuesForIndividual(propertyValueMap, breedName);
        OntologyUtil.saveOntology();
        reasoner.flush();
        return breed;
    }
    
    public static String resolveEntityName(IRI iri) {
        return iri.getShortForm().replace(Catonto.PREFIX + "#", "");
    }
    
    public static String getRuLabel(OWLEntity entity) {
        Optional<OWLAnnotation> labelAnnotationOptional = EntitySearcher.getAnnotations(entity, ontology, dataFactory.getRDFSLabel())
                .filter((label) -> {
                    Optional<OWLLiteral> labelLiteralOptional = label.getValue().asLiteral();
                    if (!labelLiteralOptional.isPresent()) {
                        return false;
                    }
                    return labelLiteralOptional.get().hasLang("ru");
                }).findFirst();
        IRI entityIri = entity.getIRI();
        String returnString = entityIri.getShortForm();
        if(labelAnnotationOptional.isPresent()) {
            Optional<OWLLiteral> labelLiteralOptional = labelAnnotationOptional.get().getValue().asLiteral();
            if(labelLiteralOptional.isPresent()) returnString = labelLiteralOptional.get().getLiteral();
        } else {
            Optional<String> remainder = entityIri.getRemainder();
            if(remainder.isPresent()) returnString = remainder.get();
        }
        return returnString;
    }
    
    public static Stream<OWLNamedIndividual> getAllBreeds() {
        return reasoner.getInstances(BREED_CLASS).entities();
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
    
    public static OWLObjectProperty getOntoObjectProperty(IRI iri) {
        return dataFactory.getOWLObjectProperty(iri);
    }
    
    public static OWLObjectProperty getOntoObjectProperty(String name) {
        return getOntoObjectProperty(getIri(name));
    }
    
    public static OWLDataProperty getOntoDataProperty(IRI iri) {
        return dataFactory.getOWLDataProperty(iri);
    }
    
    public static OWLDataProperty getOntoDataProperty(String name) {
        return getOntoDataProperty(getIri(name));
    }
}
