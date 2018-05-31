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
package com.tika.catontowebservice.web.service;

import com.tika.catontowebservice.semantic.service.OntologyProcessor;
import com.tika.catontowebservice.semantic.vocabulary.Catonto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

/**
 *
 * @author Timur Kalugin
 */
public class OntologyWebService {
    
    public static Map<String, String[]> findBreeds(Map<String, String[]> propertyMap) {
        return OntologyProcessor.findBreeds(propertyMap);
    }
    
    public static String getOntoEntityLabel(String entityName) {
        String type = OntologyProcessor.getEntityType(entityName);
        if(StringUtils.equals(type, OWLRDFVocabulary.OWL_INDIVIDUAL.toString())) {
            return getIndividualLabel(entityName);
        }
        if(StringUtils.equals(type, OWLRDFVocabulary.OWL_OBJECT_PROPERTY.toString())) {
            return getObjectPropertyLabel(entityName);
        }
        if(StringUtils.equals(type, OWLRDFVocabulary.OWL_DATA_PROPERTY.toString())) {
            return getDataPropertyLabel(entityName);
        }
        return entityName;
    }
    
    private static String getIndividualLabel(String individualName) {
        return OntologyProcessor.getRuLabel(OntologyProcessor.getOntoIndividual(individualName));
    }
    
    private static String getObjectPropertyLabel(String propertyName) {
        return OntologyProcessor.getRuLabel(OntologyProcessor.getOntoObjectProperty(propertyName));
    }
    
    private static String getDataPropertyLabel(String propertyName) {
        return OntologyProcessor.getRuLabel(OntologyProcessor.getOntoDataProperty(propertyName));
    }
    
    public static String getOntoEntityComment(String entityName) {
        String type = OntologyProcessor.getEntityType(entityName);
        if(StringUtils.equals(type, OWLRDFVocabulary.OWL_INDIVIDUAL.toString())) {
            return getIndividualComment(entityName);
        }
        if(StringUtils.equals(type, OWLRDFVocabulary.OWL_OBJECT_PROPERTY.toString())) {
            return getObjectPropertyComment(entityName);
        }
        if(StringUtils.equals(type, OWLRDFVocabulary.OWL_DATA_PROPERTY.toString())) {
            return getDataPropertyComment(entityName);
        }
        return entityName;
    }
    
    private static String getIndividualComment(String individualName) {
        return OntologyProcessor.getRuComment(OntologyProcessor.getOntoIndividual(individualName));
    }
    
    private static String getObjectPropertyComment(String propertyName) {
        return OntologyProcessor.getRuComment(OntologyProcessor.getOntoObjectProperty(propertyName));
    }
    
    private static String getDataPropertyComment(String propertyName) {
        return OntologyProcessor.getRuComment(OntologyProcessor.getOntoDataProperty(propertyName));
    }
    
    public static ArrayList<String> getImagePathList(String breedName) {
        return OntologyProcessor.getImagePathList(OntologyProcessor.getOntoIndividual(breedName));
    }
    
    public static String getOrigin(String breedName) {
        return OntologyProcessor.getBreedOrigin(OntologyProcessor.getOntoIndividual(breedName));
    }
    
    public static ArrayList<String> getBreeds() {
        OWLClass breed = OntologyProcessor.getOntoClass(Catonto.BREED);
        ArrayList<String> breedList = new ArrayList<>();
        OntologyProcessor.getClassInstances(breed).forEach(individual -> breedList
                .add(OntologyProcessor.resolveEntityName(individual.getIRI())));
        return breedList;
    }
    
    public static Map<String, ArrayList<String>> getBodyBreedCharacteristics() {
        OWLClass breed = OntologyProcessor.getOntoClass(Catonto.BREED);
        OWLObjectProperty body = OntologyProcessor.getOntoObjectProperty(Catonto.ObjectProperty.HAS_BODY_DESCRIPTOR);
        OWLDataProperty body_data = OntologyProcessor.getOntoDataProperty(Catonto.DataProperty.HAS_BODY_DESCRIPTOR); 
        Map<String, ArrayList<String>> map = new HashMap<>();
        OntologyProcessor.getObjectPropertyValues(breed, body).forEach((prop, vals) -> {
            ArrayList<String> values = new ArrayList<>();
            vals.forEach(val -> values.add(OntologyProcessor.resolveEntityName(val.getIRI())));
            map.put(OntologyProcessor.resolveEntityName(prop.asOWLObjectProperty().getIRI()), values);
        });
        OntologyProcessor.getSpecifiedClassDomainDataProperties(breed, body_data).forEach(property -> 
                map.put(OntologyProcessor.resolveEntityName(property.asOWLDataProperty().getIRI()), new ArrayList<>(1)));
        return map;
    }
    
    public static Map<String, ArrayList<String>> getPersonalityBreedCharacteristics() {
        OWLClass breed = OntologyProcessor.getOntoClass(Catonto.BREED);
        OWLObjectProperty personality = OntologyProcessor.getOntoObjectProperty(Catonto.ObjectProperty.HAS_PERSONALITY_DESCRIPTOR);  
        Map<String, ArrayList<String>> map = new HashMap<>();
        OntologyProcessor.getObjectPropertyValues(breed, personality).forEach((prop, vals) -> {
            ArrayList<String> values = new ArrayList<>();
            vals.forEach(val -> values.add(OntologyProcessor.resolveEntityName(val.getIRI())));
            map.put(OntologyProcessor.resolveEntityName(prop.asOWLObjectProperty().getIRI()), values);
        });
        return map;
    }
    
    public static Map<String, ArrayList<String>> getBodyBreedCharacteristics(String breedName) {
        OWLNamedIndividual breed = OntologyProcessor.getOntoIndividual(breedName);
        OWLObjectProperty body = OntologyProcessor.getOntoObjectProperty(Catonto.ObjectProperty.HAS_BODY_DESCRIPTOR);
        OWLDataProperty body_data = OntologyProcessor.getOntoDataProperty(Catonto.DataProperty.HAS_BODY_DESCRIPTOR);
        Map<String, ArrayList<String>> map = new HashMap<>();
        OntologyProcessor.getObjectPropertyValuesForBreedIndividual(breed, body).forEach((prop, vals) -> {
            ArrayList<String> values = new ArrayList<>();
            vals.forEach(val -> values.add(OntologyProcessor.resolveEntityName(val.getIRI())));
            if(!values.isEmpty()) {
                map.put(OntologyProcessor.resolveEntityName(prop.asOWLObjectProperty().getIRI()), values);
            }
        });
        return map;
    }
    
    public static Map<String, ArrayList<String>> getPersonalityBreedCharacteristics(String breedName) {
        OWLNamedIndividual breed = OntologyProcessor.getOntoIndividual(breedName);
        OWLObjectProperty personality = OntologyProcessor.getOntoObjectProperty(Catonto.ObjectProperty.HAS_PERSONALITY_DESCRIPTOR);
        Map<String, ArrayList<String>> map = new HashMap<>();
        OntologyProcessor.getObjectPropertyValuesForBreedIndividual(breed, personality).forEach((prop, vals) -> {
            ArrayList<String> values = new ArrayList<>();
            vals.forEach(val -> values.add(OntologyProcessor.resolveEntityName(val.getIRI())));
            if(!values.isEmpty()) {
                map.put(OntologyProcessor.resolveEntityName(prop.asOWLObjectProperty().getIRI()), values);
            }
        });
        return map;
    }
}
