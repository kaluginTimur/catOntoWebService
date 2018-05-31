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
    
    public static class ObjectProperty {
        
        public static final IRI HAS_BREED_DESCRIPTOR;
        public static final IRI HAS_BODY_DESCRIPTOR;
        public static final IRI HAS_PERSONALITY_DESCRIPTOR;
        public static final IRI HAS_ORIGIN;
        
        static {
            HAS_BREED_DESCRIPTOR = getIRI("hasBreedDescriptor");
            HAS_BODY_DESCRIPTOR = getIRI("hasBodyDescriptor");
            HAS_PERSONALITY_DESCRIPTOR = getIRI("hasPersonalityDescriptor");
            HAS_ORIGIN = getIRI("hasOrigin");
        }
        
    }
    
    public static class DataProperty {
        
        public static final IRI HAS_BREED_DESCRIPTOR;
        public static final IRI HAS_BODY_DESCRIPTOR;
        public static final IRI HAS_IMAGE_PATH;
        
        static {
            HAS_BREED_DESCRIPTOR = getIRI("hasBreedDataDescriptor");
            HAS_BODY_DESCRIPTOR = getIRI("hasBodyDataDescriptor");
            HAS_IMAGE_PATH = getIRI("hasImagePath");
        }
        
    }
    
    public static class Individual {
        
        //Body
        public static final IRI BODY_AVERAGE;
        public static final IRI BODY_FAT;
        public static final IRI BODY_MUSCULAR;
        public static final IRI BODY_SLIM;
        
        //Head size
        public static final IRI HEAD_SIZE_AVERAGE;
        public static final IRI HEAD_SIZE_BIG;
        public static final IRI HEAD_SIZE_SMALL;
        
        //Limb size
        public static final IRI LIMB_SIZE_AVERAGE;
        public static final IRI LIMB_SIZE_LONG;
        public static final IRI LIMB_SIZE_SHORT;
        
        //Torso size
        public static final IRI TORSO_SIZE_AVERAGE;
        public static final IRI TORSO_SIZE_BIG;
        public static final IRI TORSO_SIZE_SMALL;
        
        //Color variants
        public static final IRI COLOR_FEW;
        public static final IRI COLOR_MONOTON;
        public static final IRI COLOR_VARIOUS;
        
        //Color length
        public static final IRI COAT_LENGTH_HAIRLESS;
        public static final IRI COAT_LENGTH_LONG;
        public static final IRI COAT_LENGTH_MEDIUM;
        public static final IRI COAT_LENGTH_SHORT;
        
        static {
            BODY_AVERAGE = getIRI("BodyAverage");
            BODY_FAT = getIRI("BodyFat");
            BODY_MUSCULAR = getIRI("BodyMuscular");
            BODY_SLIM = getIRI("BodySlim");
            
            HEAD_SIZE_AVERAGE = getIRI("HeadSizeAverage");
            HEAD_SIZE_BIG = getIRI("HeadSizeBig");
            HEAD_SIZE_SMALL = getIRI("HeadSizeSmall");
            
            LIMB_SIZE_AVERAGE = getIRI("LimbSizeAverage");
            LIMB_SIZE_LONG = getIRI("LimbSizeLong");
            LIMB_SIZE_SHORT = getIRI("LimbSizeShort");
            
            TORSO_SIZE_AVERAGE = getIRI("TorsoSizeAverage");
            TORSO_SIZE_BIG = getIRI("TorsoSizeBig");
            TORSO_SIZE_SMALL = getIRI("TorsoSizeSmall");
            
            COLOR_FEW = getIRI("ColorFew");
            COLOR_MONOTON = getIRI("ColorMonoton");
            COLOR_VARIOUS = getIRI("ColorVarious");
            
            COAT_LENGTH_HAIRLESS = getIRI("CoatLengthHairless");
            COAT_LENGTH_LONG = getIRI("CoatLengthLong");
            COAT_LENGTH_MEDIUM = getIRI("CoatLengthMedium");
            COAT_LENGTH_SHORT = getIRI("CoatLengthShort");
        }
    }
    
    private static IRI getIRI(String name) {
        return IRI.create(NAMESPACE + name);
    }
}
