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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLPropertyExpression;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author Timur Kalugin
 */
public class OntologyWebService {
    
    public static Map<Map<String, String>, String[]> findBreeds(Map<String, String[]> propertyMap) {
        return OntologyProcessor.findBreeds(propertyMap);
    }
    
    public static String getBreedName(String breedName) {
        return OntologyProcessor.getRuLabel(OntologyProcessor.getOntoIndividual(breedName));
    }
    
    public static Map<String, String> getBreeds() {
        OWLClass breed = OntologyProcessor.getOntoClass(Catonto.BREED);
        Map<String, String> mapStr = new HashMap<>();
        OntologyProcessor.getClassInstances(breed).forEach((individual) -> {
            mapStr.put(OntologyProcessor.resolveEntityName(individual.getIRI()),
                    OntologyProcessor.getRuLabel(individual));
        });
        return mapStr;
    }
    
    public static Map<String, Collection<String>> getAllBreedCharacteristics(String breedName) {
        OWLNamedIndividual breed = OntologyProcessor.getOntoIndividual(breedName);
        Map<OWLPropertyExpression, Set<OWLNamedIndividual>> map = OntologyProcessor.getPropertyValuesForBreedIndividual(breed);
        return getStringMap(map, false);
    }
    
    public static Map<Map<String, String>, Map<String, String>> getBodyBreedCharacteristics() {
        OWLClass breed = OntologyProcessor.getOntoClass(Catonto.BREED);
        OWLObjectProperty body = OntologyProcessor.getOntoObjectProperty(Catonto.ObjectProperty.HAS_BODY_DESCRIPTOR);
        OWLDataProperty body_data = OntologyProcessor.getOntoDataProperty(Catonto.DataProperty.HAS_BODY_DESCRIPTOR);     
        Map<OWLPropertyExpression, Set<OWLNamedIndividual>> map = OntologyProcessor
                .getObjectPropertyValues(breed, body);
        OntologyProcessor.getSpecifiedClassDomainDataProperties(breed, body_data)
                .forEach(property -> map.put(property, Collections.EMPTY_SET));
        return getStringMultiMap(map);
    }
    
    public static Map<Map<String, String>, Map<String, String>> getPersonalityBreedCharacteristics() {
        OWLClass breed = OntologyProcessor.getOntoClass(Catonto.BREED);
        OWLObjectProperty personality = OntologyProcessor.getOntoObjectProperty(Catonto.ObjectProperty.HAS_PERSONALITY_DESCRIPTOR);  
        Map<OWLPropertyExpression, Set<OWLNamedIndividual>> map = OntologyProcessor
                .getObjectPropertyValues(breed, personality);
        return getStringMultiMap(map);
    }
    
    public static Map<String, Collection<String>> getBodyBreedCharacteristics(String breedName) {
        OWLNamedIndividual breed = OntologyProcessor.getOntoIndividual(breedName);
        OWLObjectProperty body = OntologyProcessor.getOntoObjectProperty(Catonto.ObjectProperty.HAS_BODY_DESCRIPTOR);
        OWLDataProperty body_data = OntologyProcessor.getOntoDataProperty(Catonto.DataProperty.HAS_BODY_DESCRIPTOR);
        Map<OWLPropertyExpression, Set<OWLNamedIndividual>> map = OntologyProcessor
                .getObjectPropertyValuesForBreedIndividual(breed, body);
        map.putAll(OntologyProcessor.getDataPropertyValuesForBreedIndividual(breed, body_data));
        return getStringMap(map, false);
    }
    
    public static Map<String, Collection<String>> getPersonalityBreedCharacteristics(String breedName) {
        OWLNamedIndividual breed = OntologyProcessor.getOntoIndividual(breedName);
        OWLObjectProperty personality = OntologyProcessor.getOntoObjectProperty(Catonto.ObjectProperty.HAS_PERSONALITY_DESCRIPTOR);
        Map<OWLPropertyExpression, Set<OWLNamedIndividual>> map = OntologyProcessor
                .getObjectPropertyValuesForBreedIndividual(breed, personality);
        return getStringMap(map, false);
    }
    
    private static Map<String, Collection<String>> 
        getStringMap(Map<OWLPropertyExpression, Set<OWLNamedIndividual>> map, boolean emptyDataValuesAllowed) {
        Map<String, Collection<String>> mapStr = new HashMap<>();
        map.entrySet().forEach((entry) -> {
            String property;
            boolean dataProperty = false;
            Collection<String> values;
            if (entry.getKey().isDataPropertyExpression()) {
                dataProperty = true;
                property = OntologyProcessor.getRuLabel(entry.getKey().asOWLDataProperty());
                values = new ArrayList<>();
                entry.getValue().forEach((literal) -> {
                    values.add(OntologyProcessor.resolveEntityName(literal.getIRI()));
                });
            }
            else {
                property = OntologyProcessor.getRuLabel(entry.getKey().asOWLObjectProperty());
                values = new HashSet<>();
                entry.getValue().forEach((individual) -> {
                    values.add(OntologyProcessor.getRuLabel(individual));
                });
            }
            if (!CollectionUtils.isEmpty(values) || (dataProperty && emptyDataValuesAllowed)) {
                mapStr.put(property, values);
            }
        });
        return mapStr;
    }
        
    private static Map<Map<String, String>, Map<String, String>> 
        getStringMultiMap(Map<OWLPropertyExpression, Set<OWLNamedIndividual>> map) {
        Map<Map<String, String>, Map<String, String>> _map = new HashMap<>();
        map.entrySet().forEach((entry) -> {
            String property;
            Map<String, String> propertyMap = new HashMap<>();
            Map<String, String> valuesMap;
            if (entry.getKey().isDataPropertyExpression()) {
                property = OntologyProcessor.getRuLabel(entry.getKey().asOWLDataProperty());
                propertyMap.put(entry.getKey().asOWLDataProperty().getIRI().getShortForm(), property);
                valuesMap = new HashMap<>();
                entry.getValue().forEach((literal) -> {
                    valuesMap.put("dataPropValue", OntologyProcessor.resolveEntityName(literal.getIRI()));
                });
            }
            else {
                property = OntologyProcessor.getRuLabel(entry.getKey().asOWLObjectProperty());
                propertyMap.put(entry.getKey().asOWLObjectProperty().getIRI().getShortForm(), property);
                valuesMap = new HashMap<>();
                entry.getValue().forEach((individual) -> {
                    valuesMap.put(individual.getIRI().getShortForm(), OntologyProcessor.getRuLabel(individual));
                });
            }
            _map.put(propertyMap, valuesMap);
        });
        return _map;
    }
}
