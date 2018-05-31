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
import java.util.*;
import java.util.stream.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

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
    
    public static Map<String, String[]> findBreeds(Map<String, String[]> propertyMap) {
        Map<String, String[]> targetMap = new LinkedHashMap<>();
        getClassInstances(BREED_CLASS).forEach(breed -> {
            String breedName = breed.getIRI().getShortForm();
            ArrayList<String> propertyList = new ArrayList<>();
            propertyMap.entrySet().forEach(propertyEntry -> {
                if (ontology.containsDataPropertyInSignature(getIri(propertyEntry.getKey()))) {
                    OWLDataProperty property = getOntoDataProperty(propertyEntry.getKey());
                    reasoner.getDataPropertyValues(breed, property).forEach(literal -> {
                        for (String value : propertyEntry.getValue()) {
                            if (StringUtils.equals(literal.getLiteral(), value)) {
                                propertyList.add(property.getIRI().getShortForm());
                                break;
                            }
                        }
                    });
                } else {
                    if (ontology.containsObjectPropertyInSignature(getIri(propertyEntry.getKey()))) {
                        OWLObjectProperty property = getOntoObjectProperty(propertyEntry.getKey());
                        reasoner.getObjectPropertyValues(breed, property).entities().forEach(breedPropValue -> {
                            for (String value : propertyEntry.getValue()) {
                                if (StringUtils.equals(value, breedPropValue.getIRI().getShortForm())) {
                                    propertyList.add(property.getIRI().getShortForm());
                                    break;
                                }
                            }
                        });
                    }
                }
            });
            if (!propertyList.isEmpty()) {
                targetMap.put(breedName, propertyList.toArray(new String[propertyList.size()]));
            }
        });
        if(!targetMap.isEmpty()) {
            List<Map.Entry<String, String[]>> list = new LinkedList<>(targetMap.entrySet());
            Collections.sort(list, (Map.Entry<String, String[]> o1, Map.Entry<String, String[]> o2) -> {
                return Integer.valueOf(o2.getValue().length).compareTo(o1.getValue().length);
            });
            targetMap.clear();
            list.forEach(entry -> targetMap.put(entry.getKey(), entry.getValue()));
        }
        return targetMap;
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
            getObjectPropertyValuesForBreedIndividual(OWLNamedIndividual individual, OWLObjectProperty superProperty) {
        Map<OWLPropertyExpression, Set<OWLNamedIndividual>> propertyValuesMap = new HashMap<>();
        getSpecifiedClassDomainObjectProperties(BREED_CLASS, superProperty)
                .forEach(property -> propertyValuesMap.put(property, 
                        reasoner.getObjectPropertyValues(individual, property)
                                .entities().filter(ind -> 
                                    EntitySearcher.getRanges(property, ontology)
                                            .anyMatch(range -> EntitySearcher.getTypes(ind, ontology)
                                                    .anyMatch(type -> range.asOWLClass().getIRI().equals(type.asOWLClass().getIRI())))
                                ).collect(Collectors.toSet())));
        return propertyValuesMap;
    }
            
    public static Map<OWLPropertyExpression, Set<OWLNamedIndividual>>
            getDataPropertyValuesForBreedIndividual(OWLNamedIndividual individual, OWLDataProperty superProperty) {
        Map<OWLPropertyExpression, Set<OWLNamedIndividual>> propertyValuesMap = new HashMap<>();
        getSpecifiedClassDomainDataProperties(BREED_CLASS, superProperty).forEach(property -> {
            Set<OWLNamedIndividual> litsAsInds = new HashSet<>();
            reasoner.getDataPropertyValues(individual, property.asOWLDataProperty())
                    .forEach(value -> litsAsInds.add(getOntoIndividual(value.getLiteral())));
            propertyValuesMap.put(property, litsAsInds);
        });
        return propertyValuesMap;
    }
    
    public static Map<OWLPropertyExpression, Set<OWLNamedIndividual>>
            getPropertyValuesForBreedIndividual(OWLNamedIndividual individual) {
        Map<OWLPropertyExpression, Set<OWLNamedIndividual>> propertyValuesMap = new HashMap<>();
        propertyValuesMap.putAll(getObjectPropertyValuesForBreedIndividual(individual, null));
        propertyValuesMap.putAll(getDataPropertyValuesForBreedIndividual(individual, null));
        return propertyValuesMap;
    }
            
    public static Map<OWLPropertyExpression, Set<OWLNamedIndividual>> 
        getObjectPropertyValues(OWLClass ontoClass, OWLObjectProperty superProperty) {
        Map<OWLPropertyExpression, Set<OWLNamedIndividual>> propertyValuesMap = new HashMap<>();
        ontology.objectPropertiesInSignature()
                .filter(property -> reasoner.getObjectPropertyDomains(property).entities()
                        .filter(domain -> ontoClass.equals(domain)).findFirst().isPresent() 
                        && (superProperty != null ? reasoner.getSuperObjectProperties(property).containsEntity(superProperty) : true))
                .forEach(property -> {
                    Set<OWLNamedIndividual> individuals = new HashSet<>();
                    EntitySearcher.getRanges(property, ontology)
                            .forEach(range -> {
                                individuals.addAll(reasoner.getInstances(range, true).entities().collect(Collectors.toSet()));
                            });
                    propertyValuesMap.put(property, individuals);
                });
        return propertyValuesMap;
    }
        
    public static ArrayList<OWLObjectPropertyExpression>
            getSpecifiedClassDomainObjectProperties(OWLClass ontoClass, OWLObjectProperty superProperty) {
        ArrayList<OWLObjectPropertyExpression> properties = new ArrayList<>();
        ontology.objectPropertiesInSignature()
                .filter(property -> reasoner.getObjectPropertyDomains(property).entities()
                        .filter(domain -> ontoClass.equals(domain)).findFirst().isPresent() 
                        && (superProperty != null ? reasoner.getSuperObjectProperties(property).containsEntity(superProperty) : true))
                .forEach(property -> properties.add(property));
        return properties;
    }
            
    public static ArrayList<OWLDataPropertyExpression>
            getSpecifiedClassDomainDataProperties(OWLClass ontoClass, OWLDataProperty superProperty) {
        ArrayList<OWLDataPropertyExpression> properties = new ArrayList<>();
        ontology.dataPropertiesInSignature()
                .filter(property -> reasoner.getDataPropertyDomains(property).entities()
                        .filter(domain -> ontoClass.equals(domain)).findFirst().isPresent()
                        && (superProperty != null ? reasoner.getSuperDataProperties(property).containsEntity(superProperty) : true))
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
    
    public static String getRuComment(OWLEntity entity) {
        Optional<OWLAnnotation> commentAnnotationOptional = EntitySearcher.getAnnotations(entity, ontology, dataFactory.getRDFSComment())
                .filter((comment) -> {
                    Optional<OWLLiteral> commentLiteralOptional = comment.getValue().asLiteral();
                    if (!commentLiteralOptional.isPresent()) {
                        return false;
                    }
                    return commentLiteralOptional.get().hasLang("ru");
                }).findFirst();
        String returnString = "";
        if(commentAnnotationOptional.isPresent()) {
            Optional<OWLLiteral> labelLiteralOptional = commentAnnotationOptional.get().getValue().asLiteral();
            if(labelLiteralOptional.isPresent()) returnString = labelLiteralOptional.get().getLiteral();
        } 
        return returnString;
    }
    
    public static ArrayList<String> getImagePathList(OWLNamedIndividual breed) {
        ArrayList<String> pathList = new ArrayList<>();
        getDataPropertyValuesForBreedIndividual(breed, getOntoDataProperty(Catonto.DataProperty.HAS_BREED_DESCRIPTOR))
                .forEach((prop, values) -> {
                    if(StringUtils.equals(prop.asOWLDataProperty().getIRI().getShortForm(), 
                            Catonto.DataProperty.HAS_IMAGE_PATH.getShortForm())) {
                        values.forEach(value -> pathList.add(value.getIRI().toString().replace(Catonto.NAMESPACE, "")));
                    }
                });
        return pathList;
    }
    
    public static String getBreedOrigin(OWLNamedIndividual breed) {
        ArrayList<String> origin = new ArrayList<>();
        getObjectPropertyValuesForBreedIndividual(breed, null)
                .forEach((prop, values) -> {
                    if(StringUtils.equals(prop.asOWLObjectProperty().getIRI().getShortForm(), 
                            Catonto.ObjectProperty.HAS_ORIGIN.getShortForm())) {
                        values.forEach(value -> origin.add(resolveEntityName(value.getIRI())));
                    }
                });
        return origin.isEmpty() ? "" : origin.get(0);
    }
    
    public static String getEntityType(String entityName) {
        if(ontology.containsIndividualInSignature(getIri(entityName))) {
            return OWLRDFVocabulary.OWL_INDIVIDUAL.toString();
        } 
        if(ontology.containsObjectPropertyInSignature(getIri(entityName))) {
            return OWLRDFVocabulary.OWL_OBJECT_PROPERTY.toString();
        }
        if(ontology.containsDataPropertyInSignature(getIri(entityName))) {
            return OWLRDFVocabulary.OWL_DATA_PROPERTY.toString();
        }
        return entityName;
    }
    
    public static Stream<OWLNamedIndividual> getClassInstances(OWLClass ontoClass) {
        return reasoner.getInstances(ontoClass).entities();
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
