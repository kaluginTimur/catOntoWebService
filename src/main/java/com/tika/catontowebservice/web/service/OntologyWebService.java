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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLPropertyExpression;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author Timur Kalugin
 */
public class OntologyWebService {
    
    public static Map<String, String> getBreeds() {
        Map<String, String> mapStr = new HashMap<>();
        OntologyProcessor.getAllBreeds().forEach((individual) -> {
            mapStr.put(OntologyProcessor.resolveEntityName(individual.getIRI()),
                    OntologyProcessor.getRuLabel(individual));
        });
        return mapStr;
    }
    
    public static Map<String, Collection<String>> getBreedCharacteristics(String breedName) {
        OWLNamedIndividual breed = OntologyProcessor.getOntoIndividual(breedName);
        Map<OWLPropertyExpression, Set<OWLNamedIndividual>> map = OntologyProcessor.getPropertyValuesForBreedIndividual(breed);
        Map<String, Collection<String>> mapStr = new HashMap<>();
        map.entrySet().forEach((entry) -> {
            String property;
            Collection<String> values;
            if (entry.getKey().isDataPropertyExpression()) {
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
            if (!CollectionUtils.isEmpty(values)) {
                mapStr.put(property, values);
            }
        });
        return mapStr;
    }
    
}
