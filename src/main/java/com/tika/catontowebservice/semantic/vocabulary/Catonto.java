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
package com.tika.catontowebservice.semantic.vocabulary;

import org.semanticweb.owlapi.model.IRI;

/**
 *
 * @author Timur Kalugin
 */
public class Catonto {
    
    public static final String NAMESPACE = "http://www.semanticweb.org/admin/ontologies/2018/2/catonto#";
    public static final String PREFIX = "catonto";
    
    public static final IRI BREED;
    
    public static final IRI BREED_DESCRIPTOR;
    
    public static final IRI BODY;
        public static final IRI HEAD_SIZE;
        public static final IRI LIMB_SIZE;
        public static final IRI TORSO_SIZE;
        
    public static final IRI COAT;
        public static final IRI COAT_COLOR;
        public static final IRI COAT_LENGTH;
        
    public static final IRI PERSONALITY;
        public static final IRI ATTITUDE_TOWARDS_HUMAN;
        public static final IRI ATTITUDE_TOWARDS_ANIMALS;
        public static final IRI INTELLIGENCE;
        public static final IRI TEMPER;
        
    static {
        BREED = getIRI("Breed");
        BREED_DESCRIPTOR = getIRI("BreedDescriptor");
        BODY = getIRI("Body");
        HEAD_SIZE = getIRI("HeadSize");
        LIMB_SIZE = getIRI("LimbSize");
        TORSO_SIZE = getIRI("TorsoSize");
        COAT = getIRI("Coat");
        COAT_COLOR = getIRI("Color");
        COAT_LENGTH = getIRI("Length");
        PERSONALITY = getIRI("Personality");
        ATTITUDE_TOWARDS_HUMAN = getIRI("AttitudeTowardsHuman");
        ATTITUDE_TOWARDS_ANIMALS = getIRI("AttitudeTowardsAnimals");
        INTELLIGENCE = getIRI("Intelligence");
        TEMPER = getIRI("Temper");
    }
    
    private static IRI getIRI(String name) {
        return IRI.create(NAMESPACE + name);
    }
    
}
